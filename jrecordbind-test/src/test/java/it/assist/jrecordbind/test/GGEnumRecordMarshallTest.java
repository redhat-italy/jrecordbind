/*
 * JRecordBind, fixed-length file (un)marshaller
 * Copyright 2009, Assist s.r.l., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package it.assist.jrecordbind.test;

import it.assist.jrecordbind.Marshaller;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;

import junit.framework.TestCase;

public class GGEnumRecordMarshallTest extends TestCase {

  private Marshaller<MyGGEnumRecord> marshaller;
  private MyGGEnumRecord record;
  private StringWriter stringWriter;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    record = new MyGGEnumRecord();
    record.setMyEnum(MyEnum.ONE);
    record.setBigNumber(BigDecimal.TEN);

    marshaller = new Marshaller<MyGGEnumRecord>(new InputStreamReader(GGEnumRecordMarshallTest.class
        .getResourceAsStream("/generationGap.def.xsd")));

    stringWriter = new StringWriter();
  }

  public void testMarshallALot() throws Exception {
    for (int i = 0; i < 100000; i++) {
      marshaller.marshall(record, stringWriter);
    }

    assertEquals(10100000, stringWriter.toString().length());
  }

  public void testMarshallMore() throws Exception {
    marshaller.marshall(record, stringWriter);
    marshaller.marshall(record, stringWriter);

    assertEquals(
        "ONE                 10                                                                              \n"
            + "ONE                 10                                                                              \n",
        stringWriter.toString());

    assertEquals(202, stringWriter.toString().length());
  }

  public void testMarshallOne() throws Exception {
    marshaller.marshall(record, stringWriter);

    assertEquals(
        "ONE                 10                                                                              \n",
        stringWriter.toString());

    assertEquals(101, stringWriter.toString().length());
  }

  public void testMarshallOneExceedsLength() throws Exception {
    record.setBigNumber(BigDecimal.ZERO);
    marshaller.marshall(record, stringWriter);

    assertEquals(
        "ONE                 0                                                                               \n",
        stringWriter.toString());

    assertEquals(101, stringWriter.toString().length());
  }
}