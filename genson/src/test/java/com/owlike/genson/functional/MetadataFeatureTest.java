package com.owlike.genson.functional;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.owlike.genson.GensonBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.owlike.genson.Genson;

public class MetadataFeatureTest {
  private Genson genson;

  @Before
  public void setUp() {
    genson = new GensonBuilder().useClassMetadata(true).addAlias("bean", Bean.class).create();
  }

  @Test
  public void testSerializeUnknownType() {
    Bean bean = new Bean();
    bean.value = new Date();
    String dateFormated = SimpleDateFormat.getDateInstance().format((Date) bean.value);
    assertEquals("{\"@class\":\"bean\",\"value\":\"" + dateFormated + "\"}", genson.serialize(bean));
  }

  @Test
  public void testDeserializeToUnknownType() {
    Bean bean = (Bean) genson.deserialize("{\"@class\":\"bean\",\"value\":{\"@class\":\"bean\"}}", Object.class);
    assertTrue(bean.value instanceof Bean);

    bean = (Bean) genson.deserialize("{\"@class\":\"bean\",\"value\":{\"@class\":\"bean\"}}", Bean.class);
    assertTrue(bean.value instanceof Bean);
  }

  @Test public void testClassMetadataShouldNotBeSerializedForStaticTypes() {
    Genson genson = new GensonBuilder().useClassMetadata(true).useClassMetadataWithStaticType(false).create();

    Bean bean = new Bean();

    assertEquals("{\"value\":null}", genson.serialize(bean));
  }

  static class Bean {
    Object value;
  }
}
