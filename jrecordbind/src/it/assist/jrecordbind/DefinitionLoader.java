package it.assist.jrecordbind;

import it.assist.jrecordbind.RecordDefinition.Property;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefinitionLoader {

  private final static Pattern propertyName;

  static {
    propertyName = Pattern.compile("row\\.(\\d+)\\.(\\w+)");
  }

  private RecordDefinition recordDefinition;

  public DefinitionLoader() {
    recordDefinition = new RecordDefinition();
  }

  public RecordDefinition getDefinition() {
    return recordDefinition;
  }

  public DefinitionLoader load(Reader input) throws IOException {
    BufferedReader reader = new BufferedReader(input);
    String line = null;
    while ((line = reader.readLine()) != null) {
      String[] split = line.split("=");
      if (split.length == 2) {
        Matcher propertyNameMatcher = propertyName.matcher(split[0].trim());
        if ("classname".equals(split[0].trim())) {
          String fqn = split[1].trim();
          recordDefinition.setClassName(fqn.substring(fqn.lastIndexOf(".") + 1));
          recordDefinition.setPackageName(fqn.substring(0, fqn.lastIndexOf(".")));
        } else if ("separator".equals(split[0].trim())) {
          recordDefinition.setSeparator(split[1].trim());
        } else if (propertyNameMatcher.find()) {
          Property property = new Property(propertyNameMatcher.group(2));
          recordDefinition.getProperties().add(property);
          String row = propertyNameMatcher.group(1);
          recordDefinition.addRowNumber(row);
          property.setRow(Integer.parseInt(row));
          String[] params = split[1].trim().split(",");
          property.setLength(Integer.valueOf(params[0].trim()).intValue());
          property.setType(params[1].trim());
          if (params.length > 2) {
            property.setConverter(params[2].trim());
          } else {
            property.setConverter("it.assist.jrecordbind.converters." + property.getType() + "Converter");
          }
        }
      }
    }

    return this;
  }

}
