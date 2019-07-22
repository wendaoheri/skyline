package org.dayu.core.data;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Sean Liu
 * @date 2019-07-22
 */
public class NodesAnswer implements Answer<String> {

  private static String result = "{\n"
      + "  \"nodes\":\n"
      + "  {\n"
      + "    \"node\":\n"
      + "    [\n"
      + "      {\n"
      + "        \"rack\":\"\\/default-rack\",\n"
      + "        \"state\":\"NEW\",\n"
      + "        \"id\":\"h2:1235\",\n"
      + "        \"nodeHostName\":\"h2\",\n"
      + "        \"nodeHTTPAddress\":\"h2:2\",\n"
      + "        \"healthStatus\":\"Healthy\",\n"
      + "        \"lastHealthUpdate\":1324056895432,\n"
      + "        \"healthReport\":\"Healthy\",\n"
      + "        \"numContainers\":0,\n"
      + "        \"usedMemoryMB\":0,\n"
      + "        \"availMemoryMB\":8192,\n"
      + "        \"usedVirtualCores\":0,\n"
      + "        \"availableVirtualCores\":8\n"
      + "      },\n"
      + "      {\n"
      + "        \"rack\":\"\\/default-rack\",\n"
      + "        \"state\":\"NEW\",\n"
      + "        \"id\":\"h1:1234\",\n"
      + "        \"nodeHostName\":\"h1\",\n"
      + "        \"nodeHTTPAddress\":\"h1:2\",\n"
      + "        \"healthStatus\":\"Healthy\",\n"
      + "        \"lastHealthUpdate\":1324056895092,\n"
      + "        \"healthReport\":\"Healthy\",\n"
      + "        \"numContainers\":0,\n"
      + "        \"usedMemoryMB\":0,\n"
      + "        \"availMemoryMB\":8192,\n"
      + "        \"usedVirtualCores\":0,\n"
      + "        \"availableVirtualCores\":8\n"
      + "      }\n"
      + "    ]\n"
      + "  }\n"
      + "}\n";

  @Override
  public String answer(InvocationOnMock invocation) throws Throwable {
    return result;
  }
}
