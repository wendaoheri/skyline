package org.dayu.core.data;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Sean Liu
 * @date 2019-07-22
 */
public class YarnMetricsAnswer implements Answer<String> {

  private static final String result = "{\n"
      + "    \"clusterMetrics\":{\n"
      + "      \"appsSubmitted\":0,\n"
      + "      \"appsCompleted\":0,\n"
      + "      \"appsPending\":0,\n"
      + "      \"appsRunning\":0,\n"
      + "      \"appsFailed\":0,\n"
      + "      \"appsKilled\":0,\n"
      + "      \"reservedMB\":0,\n"
      + "      \"availableMB\":17408,\n"
      + "      \"allocatedMB\":0,\n"
      + "      \"reservedVirtualCores\":0,\n"
      + "      \"availableVirtualCores\":7,\n"
      + "      \"allocatedVirtualCores\":1,\n"
      + "      \"containersAllocated\":0,\n"
      + "      \"containersReserved\":0,\n"
      + "      \"containersPending\":0,\n"
      + "      \"totalMB\":17408,\n"
      + "      \"totalVirtualCores\":8,\n"
      + "      \"totalNodes\":1,\n"
      + "      \"lostNodes\":0,\n"
      + "      \"unhealthyNodes\":0,\n"
      + "      \"decommissionedNodes\":0,\n"
      + "      \"rebootedNodes\":0,\n"
      + "      \"activeNodes\":1\n"
      + "    }\n"
      + "  }";

  @Override
  public String answer(InvocationOnMock invocation) throws Throwable {
    return result;
  }
}
