package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Marshaller;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Calendar;

import junit.framework.TestCase;

public class SimpleRecordMarshallTest extends TestCase {

  private Marshaller marshaller;
  private SimpleRecord record;
  private StringWriter stringWriter;

  protected void setUp() throws Exception {
    super.setUp();

    record = new SimpleRecord();
    record.setName("FEDERICO");
    record.setSurname("FISSORE");
    record.setTaxCode("ABCDEF88L99H123B");

    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 1979);
    calendar.set(Calendar.MONTH, 4);
    calendar.set(Calendar.DAY_OF_MONTH, 18);
    record.setBirthday(calendar.getTime());

    record.setOneInteger(new Integer(81));
    record.setOneFloat(new Float(1.97f));

    marshaller = new Marshaller(new InputStreamReader(SimpleRecordMarshallTest.class
        .getResourceAsStream("/simple.def.properties")));

    stringWriter = new StringWriter();
  }

  public void testMarshallALot() throws Exception {
    for (int i = 0; i < 100000; i++) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals(7000000, stringWriter.toString().length());
  }

  public void testMarshallMore() throws Exception {
    marshaller.marshall(record, stringWriter);
    marshaller.marshall(record, stringWriter);

    assertEquals("FEDERICO            FISSORE             ABCDEF88L99H123B1979051881197\n"
        + "FEDERICO            FISSORE             ABCDEF88L99H123B1979051881197\n", stringWriter.toString());
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals("FEDERICO            FISSORE             ABCDEF88L99H123B1979051881197\n", stringWriter.toString());
  }
}