/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pronoia.splunk.jmx.eventcollector.eventbuilder;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.ObjectName;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JmxAttributeListEventBuilderTest {
    JmxAttributeListEventBuilder instance;
    AttributeList eventBody;

    /**
     * Setup an event body for the tests.
     *
     * @throws Exception raised in the event of a test error
     */
    @Before
    public void setUp() throws Exception {
        instance = new JmxAttributeListEventBuilder();

        eventBody = new AttributeList();
        eventBody.add(new Attribute("nullStringAttribute", null));
        eventBody.add(new Attribute("stringAttribute", "stringAttributeValue"));
        eventBody.add(new Attribute("emptyStringAttribute", ""));
        eventBody.add(new Attribute("zeroAttribute", Integer.valueOf(0)));

        ObjectName[] objectNames = new ObjectName[] {
                new ObjectName("edu.ucla.mednet", "key", "value1"),
                new ObjectName("edu.ucla.mednet", "key", "value2"),
                new ObjectName("edu.ucla.mednet", "key", "value3")
        };

    }

    @Test
    public void testIsIncludeEmptyAttributes() throws Exception {
        assertFalse("Default value should be false", instance.includeEmptyAttributes);
        assertFalse("Should return false", instance.isIncludeEmptyAttributes());

        instance.includeEmptyAttributes = true;
        assertTrue("Should return true", instance.isIncludeEmptyAttributes());
    }

    @Test
    public void testSetIncludeEmptyAttributes() throws Exception {
        assertFalse("Default value should be false", instance.includeEmptyAttributes);

        instance.setIncludeEmptyAttributes(true);
        assertTrue("Should be true", instance.includeEmptyAttributes);

        instance.setIncludeEmptyAttributes(false);
        assertFalse("Should be false", instance.includeEmptyAttributes);
    }

    @Test
    public void testIsIncludeEmptyLists() throws Exception {
        assertFalse("Default value should be false", instance.includeEmptyObjectNameLists);
        assertFalse("Should return false", instance.isIncludeEmptyObjectNameLists());

        instance.includeEmptyObjectNameLists = true;
        assertTrue("Should return true", instance.isIncludeEmptyObjectNameLists());
    }

    @Test
    public void testSetIncludeEmptyLists() throws Exception {
        assertFalse("Default value should be false", instance.includeEmptyObjectNameLists);

        instance.setIncludeEmptyObjectNameLists(true);
        assertTrue("Should be true", instance.includeEmptyObjectNameLists);

        instance.setIncludeEmptyObjectNameLists(false);
        assertFalse("Should be false", instance.includeEmptyObjectNameLists);

    }

    @Test
    public void testSerializeBodyWithEmptyAttributesDisabled() throws Exception {
        // @formatter:off
        final String expected
                = "{event="
                +   "{"
                +     "zeroAttribute=0, "
                +     "stringAttribute=stringAttributeValue"
                +   "}"
                +  "}";
        // @formatter:on

        Map<String, Object> eventObject = new LinkedHashMap<>();

        instance.setIncludeEmptyAttributes(false);

        instance.eventBody(eventBody);
        instance.addEventBodyToMap(eventObject);

        assertEquals(expected, eventObject.toString());
    }

    /*
      @Test
      public void testSerializeBodyWithEmptyListsEnabled() throws Exception {
        // @formatter:off
        final String expected
            = "{"
            +     "\"event\":"
            +         "{"
            +             "\"zeroAttribute\":\"0\","
            +             "\"emptyStringAttribute\":\"\","
            +             "\"objectNameList\":"
            +                 "["
            +                     "\"edu.ucla.mednet:key=value1\","
            +                     "\"edu.ucla.mednet:key=value2\","
            +                     "\"edu.ucla.mednet:key=value3\""
            +                 "],"
            +             "\"emptyObjectNameList\":[],"
            +             "\"stringAttribute\":\"stringAttributeValue\","
            +             "\"nullObjectNameList\":null,"
            +             "\"nullStringAttribute\":null"
            +         "}"
            + "}";
        // @formatter:on

        JSONObject jsonObject = new JSONObject();

        instance.setIncludeEmptyObjectNameLists(true);

        instance.event(eventBody);
        instance.serializeBody(jsonObject);

        assertEquals(expected, jsonObject.toJSONString());
      }

      @Test
      public void testSerializeBodyWithEmptyAttributesDisabledAndEmptyListsEnabled() throws Exception {
        // @formatter:off
        final String expected
            = "{"
            +     "\"event\":"
            +         "{"
            +             "\"objectNameList\":"
            +                 "["
            +                     "\"edu.ucla.mednet:key=value1\","
            +                     "\"edu.ucla.mednet:key=value2\","
            +                     "\"edu.ucla.mednet:key=value3\""
            +                 "],"
            +             "\"emptyObjectNameList\":[],"
            +             "\"stringAttribute\":\"stringAttributeValue\""
            +         "}"
            + "}";
        // @formatter:on

        JSONObject jsonObject = new JSONObject();

        instance.setIncludeEmptyAttributes(false);
        instance.setIncludeEmptyObjectNameLists(true);

        instance.event(eventBody);
        instance.serializeBody(jsonObject);

        assertEquals(expected, jsonObject.toJSONString());
      }

      @Test
      public void testSerializeBodyWithDefaults() throws Exception {
        // @formatter:off
        final String expected
            = "{"
            +     "\"event\":"
            +         "{"
            +             "\"zeroAttribute\":\"0\","
            +             "\"emptyStringAttribute\":\"\","
            +             "\"objectNameList\":"
            +                 "["
            +                     "\"edu.ucla.mednet:key=value1\","
            +                     "\"edu.ucla.mednet:key=value2\","
            +                     "\"edu.ucla.mednet:key=value3\""
            +                 "],"
            +             "\"stringAttribute\":\"stringAttributeValue\","
            +             "\"nullObjectNameList\":null,"
            +             "\"nullStringAttribute\":null"
            +         "}"
            + "}";
        // @formatter:on

        JSONObject jsonObject = new JSONObject();

        instance.event(eventBody);
        instance.serializeBody(jsonObject);

        assertEquals(expected, jsonObject.toJSONString());
      }
      */
}