/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pronoia.splunk.jmx.eventcollector.eventbuilder;

import static com.pronoia.splunk.eventcollector.EventCollectorInfo.EVENT_BODY_KEY;

import com.pronoia.splunk.eventcollector.EventBuilder;
import com.pronoia.splunk.eventcollector.eventbuilder.JacksonEventBuilderSupport;
import com.pronoia.splunk.jmx.eventcollector.eventbuilder.util.OpenTypeJSONUtils;

import java.util.HashMap;
import java.util.Map;

import javax.management.Notification;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;

public class JmxNotificationEventBuilder extends JacksonEventBuilderSupport<Notification> {
  public static final String NOTIFICATION_TYPE_KEY = "notificationType";
  public static final String NOTIFICATION_MESSAGE_KEY = "notificationMessage";
  public static final String NOTIFICATION_SEQUENCE_NUMBER_KEY = "notificationSequenceNumber";
  public static final String NOTIFICATION_SOURCE_KEY = "notificationSource";
  public static final String NOTIFICATION_USER_DATA_KEY = "userData";

  boolean includeNotificationMessage = false;
  boolean includeNotificationSequenceNumber = false;
  boolean includeNotificationSource = false;
  boolean includeNotificationType = false;
  boolean includeUserData = true;

  public boolean isIncludeNotificationMessage() {
    return includeNotificationMessage;
  }

  public void setIncludeNotificationMessage(boolean includeNotificationMessage) {
    this.includeNotificationMessage = includeNotificationMessage;
  }

  public boolean isIncludeNotificationSequenceNumber() {
    return includeNotificationSequenceNumber;
  }

  public void setIncludeNotificationSequenceNumber(boolean includeNotificationSequenceNumber) {
    this.includeNotificationSequenceNumber = includeNotificationSequenceNumber;
  }

  public boolean isIncludeNotificationSource() {
    return includeNotificationSource;
  }

  public void setIncludeNotificationSource(boolean includeNotificationSource) {
    this.includeNotificationSource = includeNotificationSource;
  }

  public boolean isIncludeNotificationType() {
    return includeNotificationType;
  }

  public void setIncludeNotificationType(boolean includeNotificationType) {
    this.includeNotificationType = includeNotificationType;
  }

  public boolean isIncludeUserData() {
    return includeUserData;
  }

  public void setIncludeUserData(boolean includeUserData) {
    this.includeUserData = includeUserData;
  }

  @Override
  public void setEvent(Notification eventBody) {
    super.setEvent(eventBody);
    setTimestamp(eventBody.getTimeStamp());
    if (!hasSource()) {
      setSource(eventBody.getSource().toString());
    }
    if (!hasSourcetype()) {
      setSourcetype(eventBody.getType());
    }
  }

  @Override
  protected void serializeFields(Map<String, Object> eventObject) {
    if (includeNotificationType) {
      addField(NOTIFICATION_TYPE_KEY, getEvent().getType());
    }

    if (includeNotificationMessage) {
      addField(NOTIFICATION_MESSAGE_KEY, getEvent().getMessage());
    }

    if (includeNotificationSequenceNumber) {
      addField(NOTIFICATION_SEQUENCE_NUMBER_KEY, Long.toString(getEvent().getSequenceNumber()));
    }

    if (includeNotificationSource) {
      addField(NOTIFICATION_SOURCE_KEY, getEvent().getSource().toString());
    }

    super.serializeFields(eventObject);
  }

  @Override
  protected void serializeBody(Map<String, Object> eventObject) {
    Map<String, Object> notificationEvent = new HashMap<>();

    if (includeUserData) {
      Object userData = getEvent().getUserData();
      if (userData != null) {
        if (userData instanceof CompositeData) {
          log.trace("Processing Composite Data for 'userData'");
          OpenTypeJSONUtils.addCompositeData(notificationEvent, (CompositeData) userData);
        } else if (userData instanceof TabularData) {
          log.trace("Processing Tabular Data for 'userData'");
          OpenTypeJSONUtils.addTabularData(notificationEvent, (TabularData) userData);
        } else {
          log.debug("Processing {} for {}", userData.getClass().getName(), "userData");
          notificationEvent.put(NOTIFICATION_USER_DATA_KEY, userData.toString());
        }
      }

      eventObject.put(EVENT_BODY_KEY, notificationEvent);
    }
  }

  @Override
  public EventBuilder<Notification> duplicate() {
    JmxNotificationEventBuilder answer = new JmxNotificationEventBuilder();

    answer.copyConfiguration(this);

    return answer;
  }
}