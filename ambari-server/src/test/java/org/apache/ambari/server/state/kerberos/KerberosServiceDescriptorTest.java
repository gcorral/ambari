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

import junit.framework.Assert;
import org.apache.ambari.server.AmbariException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KerberosServiceDescriptorTest {
  public static final String JSON_VALUE =
      "{" +
          "  \"name\": \"SERVICE_NAME\"," +
          "  \"identities\": [" +
          KerberosIdentityDescriptorTest.JSON_VALUE +
          "]," +
          "  \"components\": [" +
          KerberosComponentDescriptorTest.JSON_VALUE +
          "]," +
          "  \"configurations\": [" +
          "    {" +
          "      \"service-site\": {" +
          "        \"service.property1\": \"value1\"," +
          "        \"service.property2\": \"value2\"" +
          "      }" +
          "    }" +
          "  ]" +
          "}";

  public static final Map<String, Object> MAP_VALUE =
      new HashMap<String, Object>() {
        {
          put("name", "A_DIFFERENT_SERVICE_NAME");
          put(KerberosDescriptorType.IDENTITY.getDescriptorPluralName(), new ArrayList<Object>() {{
            add(KerberosIdentityDescriptorTest.MAP_VALUE);
          }});
          put(KerberosDescriptorType.COMPONENT.getDescriptorPluralName(), new ArrayList<Object>() {{
            add(KerberosComponentDescriptorTest.MAP_VALUE);
          }});
          put(KerberosDescriptorType.CONFIGURATION.getDescriptorPluralName(), new ArrayList<Map<String, Object>>() {{
            add(new HashMap<String, Object>() {
              {
                put("service-site", new HashMap<String, String>() {
                  {
                    put("service.property1", "red");
                    put("service.property", "green");
                  }
                });
              }
            });
          }});
        }
      };

  public static void validateFromJSON(KerberosServiceDescriptor serviceDescriptor) {
    Assert.assertNotNull(serviceDescriptor);
    Assert.assertTrue(serviceDescriptor.isContainer());

    Assert.assertEquals("SERVICE_NAME", serviceDescriptor.getName());

    Map<String, KerberosComponentDescriptor> componentDescriptors = serviceDescriptor.getComponents();
    Assert.assertNotNull(componentDescriptors);
    Assert.assertEquals(1, componentDescriptors.size());

    for (KerberosComponentDescriptor componentDescriptor : componentDescriptors.values()) {
      KerberosComponentDescriptorTest.validateFromJSON(componentDescriptor);
    }

    List<KerberosIdentityDescriptor> identityDescriptors = serviceDescriptor.getIdentities();
    Assert.assertNotNull(identityDescriptors);
    Assert.assertEquals(1, identityDescriptors.size());

    for (KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
      KerberosIdentityDescriptorTest.validateFromJSON(identityDescriptor);
    }

    Map<String, KerberosConfigurationDescriptor> configurations = serviceDescriptor.getConfigurations();

    Assert.assertNotNull(configurations);
    Assert.assertEquals(1, configurations.size());

    KerberosConfigurationDescriptor configuration = configurations.get("service-site");

    Assert.assertNotNull(configuration);

    Map<String, String> properties = configuration.getProperties();

    Assert.assertEquals("service-site", configuration.getType());
    Assert.assertNotNull(properties);
    Assert.assertEquals(2, properties.size());
    Assert.assertEquals("value1", properties.get("service.property1"));
    Assert.assertEquals("value2", properties.get("service.property2"));
  }

  public static void validateFromMap(KerberosServiceDescriptor serviceDescriptor) {
    Assert.assertNotNull(serviceDescriptor);
    Assert.assertTrue(serviceDescriptor.isContainer());

    Assert.assertEquals("A_DIFFERENT_SERVICE_NAME", serviceDescriptor.getName());

    Map<String, KerberosComponentDescriptor> componentDescriptors = serviceDescriptor.getComponents();
    Assert.assertNotNull(componentDescriptors);
    Assert.assertEquals(1, componentDescriptors.size());

    for (KerberosComponentDescriptor componentDescriptor : componentDescriptors.values()) {
      KerberosComponentDescriptorTest.validateFromMap(componentDescriptor);
    }

    List<KerberosIdentityDescriptor> identityDescriptors = serviceDescriptor.getIdentities();
    Assert.assertNotNull(identityDescriptors);
    Assert.assertEquals(1, identityDescriptors.size());

    for (KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
      KerberosIdentityDescriptorTest.validateFromMap(identityDescriptor);
    }

    Map<String, KerberosConfigurationDescriptor> configurations = serviceDescriptor.getConfigurations();

    Assert.assertNotNull(configurations);
    Assert.assertEquals(1, configurations.size());

    KerberosConfigurationDescriptor configuration = configurations.get("service-site");

    Assert.assertNotNull(configuration);

    Map<String, String> properties = configuration.getProperties();

    Assert.assertEquals("service-site", configuration.getType());
    Assert.assertNotNull(properties);
    Assert.assertEquals(2, properties.size());
    Assert.assertEquals("red", properties.get("service.property1"));
    Assert.assertEquals("green", properties.get("service.property"));
  }

  public void validateUpdatedData(KerberosServiceDescriptor serviceDescriptor) {
    Assert.assertNotNull(serviceDescriptor);

    Assert.assertEquals("A_DIFFERENT_SERVICE_NAME", serviceDescriptor.getName());

    Map<String, KerberosComponentDescriptor> componentDescriptors = serviceDescriptor.getComponents();
    Assert.assertNotNull(componentDescriptors);
    Assert.assertEquals(2, componentDescriptors.size());

    KerberosComponentDescriptorTest.validateFromJSON(serviceDescriptor.getComponent("COMPONENT_NAME"));
    KerberosComponentDescriptorTest.validateFromMap(serviceDescriptor.getComponent("A_DIFFERENT_COMPONENT_NAME"));

    List<KerberosIdentityDescriptor> identityDescriptors = serviceDescriptor.getIdentities();
    Assert.assertNotNull(identityDescriptors);
    Assert.assertEquals(1, identityDescriptors.size());

    for (KerberosIdentityDescriptor identityDescriptor : identityDescriptors) {
      KerberosIdentityDescriptorTest.validateUpdatedData(identityDescriptor);
    }

    Map<String, KerberosConfigurationDescriptor> configurations = serviceDescriptor.getConfigurations();

    Assert.assertNotNull(configurations);
    Assert.assertEquals(1, configurations.size());

    KerberosConfigurationDescriptor configuration = configurations.get("service-site");

    Assert.assertNotNull(configuration);

    Map<String, String> properties = configuration.getProperties();

    Assert.assertEquals("service-site", configuration.getType());
    Assert.assertNotNull(properties);
    Assert.assertEquals(3, properties.size());
    Assert.assertEquals("red", properties.get("service.property1"));
    Assert.assertEquals("value2", properties.get("service.property2"));
    Assert.assertEquals("green", properties.get("service.property"));
  }

  private KerberosServiceDescriptor createFromJSON() {
    return KerberosServiceDescriptor.fromJSON("SERVICE_NAME", JSON_VALUE);
  }

  private KerberosServiceDescriptor createFromMap() throws AmbariException {
    return new KerberosServiceDescriptor(MAP_VALUE);
  }

  @Test
  public void testJSONDeserialize() {
    validateFromJSON(createFromJSON());
  }


  @Test
  public void testMapDeserialize() throws AmbariException {
    validateFromMap(createFromMap());
  }

  @Test
  public void testEquals() throws AmbariException {
    Assert.assertTrue(createFromJSON().equals(createFromJSON()));
    Assert.assertFalse(createFromJSON().equals(createFromMap()));
  }

  @Test
  public void testToMap() throws AmbariException {
    KerberosServiceDescriptor descriptor = createFromMap();
    Assert.assertNotNull(descriptor);
    Assert.assertEquals(MAP_VALUE, descriptor.toMap());
  }

  @Test
  public void testUpdate() throws AmbariException {
    KerberosServiceDescriptor serviceDescriptor = createFromJSON();
    KerberosServiceDescriptor updatedServiceDescriptor = createFromMap();

    Assert.assertNotNull(serviceDescriptor);
    Assert.assertNotNull(updatedServiceDescriptor);

    serviceDescriptor.update(updatedServiceDescriptor);

    validateUpdatedData(serviceDescriptor);
  }

}