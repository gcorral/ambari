/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ambari.server.state.kerberos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import junit.framework.Assert;
import org.apache.ambari.server.AmbariException;
import org.junit.Test;

import java.util.*;

public class KerberosPrincipalDescriptorTest {
  public static final String JSON_VALUE =
      "{" +
          "\"value\": \"service/_HOST@_REALM\"," +
          "\"configuration\": \"service-site/service.component.kerberos.principal\"" +
          "}";

  public static final Map<String, Object> MAP_VALUE =
      new HashMap<String, Object>() {
        {
          put("value", "HTTP/_HOST@_REALM");
          put("configuration", "service-site/service.component.kerberos.https.principal");
        }
      };

  public static void validateFromJSON(KerberosPrincipalDescriptor principalDescriptor) {
    Assert.assertNotNull(principalDescriptor);
    Assert.assertFalse(principalDescriptor.isContainer());
    Assert.assertEquals("service/_HOST@_REALM", principalDescriptor.getValue());
    Assert.assertEquals("service-site/service.component.kerberos.principal", principalDescriptor.getConfiguration());
  }

  public static void validateFromMap(KerberosPrincipalDescriptor principalDescriptor) {
    Assert.assertNotNull(principalDescriptor);
    Assert.assertFalse(principalDescriptor.isContainer());
    Assert.assertEquals("HTTP/_HOST@_REALM", principalDescriptor.getValue());
    Assert.assertEquals("service-site/service.component.kerberos.https.principal", principalDescriptor.getConfiguration());
  }

  public static void validateUpdatedData(KerberosPrincipalDescriptor principalDescriptor) {
    Assert.assertNotNull(principalDescriptor);
    Assert.assertEquals("HTTP/_HOST@_REALM", principalDescriptor.getValue());
    Assert.assertEquals("service-site/service.component.kerberos.https.principal", principalDescriptor.getConfiguration());
  }

  private static KerberosPrincipalDescriptor createFromJSON() {
    Map<?, ?> map = new Gson().fromJson(JSON_VALUE,
        new TypeToken<Map<?, ?>>() {
        }.getType());
    return new KerberosPrincipalDescriptor(map);
  }

  private static KerberosPrincipalDescriptor createFromMap() {
    return new KerberosPrincipalDescriptor(MAP_VALUE);
  }

  @Test
  public void testJSONDeserialize() {
    validateFromJSON(createFromJSON());
  }

  @Test
  public void testMapDeserialize() {
    validateFromMap(createFromMap());
  }

  @Test
  public void testEquals() throws AmbariException {
    Assert.assertTrue(createFromJSON().equals(createFromJSON()));
    Assert.assertFalse(createFromJSON().equals(createFromMap()));
  }

  @Test
  public void testToMap() throws AmbariException {
    KerberosPrincipalDescriptor descriptor = createFromMap();
    Assert.assertNotNull(descriptor);
    Assert.assertEquals(MAP_VALUE, descriptor.toMap());
  }

  @Test
  public void testUpdate() {
    KerberosPrincipalDescriptor principalDescriptor = createFromJSON();
    KerberosPrincipalDescriptor updatedPrincipalDescriptor = createFromMap();

    Assert.assertNotNull(principalDescriptor);
    Assert.assertNotNull(updatedPrincipalDescriptor);

    principalDescriptor.update(updatedPrincipalDescriptor);

    validateUpdatedData(principalDescriptor);
  }
}