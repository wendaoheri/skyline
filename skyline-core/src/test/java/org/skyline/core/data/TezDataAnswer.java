package org.skyline.core.data;

import java.io.IOException;
import java.net.URL;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.skyline.core.utils.SkylineUtils;

/**
 * @author Sean Liu
 * @date 2019-08-09
 */
public class TezDataAnswer implements Answer<String> {

  private static final String APPLICATION_DATA_PATH = "data/tez/application.json";
  private static final String DAG_DATA_PATH = "data/tez/dag.json";
  private static final String VERTEX_DATA_PATH = "data/tez/vertex.json";
  private static final String TASK_DATA_PATH = "data/tez/task.json";
  private static final String TASK_ATTEMPT_PATH = "data/tez/task_attempt.json";

  @Override
  public String answer(InvocationOnMock invocation) throws Throwable {

    String arg0 = invocation.getArgument(0);
    URL url = new URL(arg0);
    String path = url.getPath();
    /**
     *private static final String TEZ_DAG_URL = "/ws/v1/timeline/TEZ_DAG_ID?primaryFilter=applicationId:%s";
     *   private static final String TEZ_APPLICATION_URL = "/ws/v1/timeline/TEZ_APPLICATION/tez_%s";
     *   private static final String TEZ_VERTEX_URL = "/ws/v1/timeline/TEZ_VERTEX_ID?primaryFilter=TEZ_DAG_ID:%s";
     *   private static final String TEZ_TASK_URL = "/ws/v1/timeline/TEZ_TASK_ID?primaryFilter=TEZ_DAG_ID:%s&limits=%s";
     *   private static final String TEZ_TASK_ATTEMPT_URL = "/ws/v1/timeline/TEZ_TASK_ATTEMPT_ID?primaryFilter=TEZ_DAG_ID:%s&limits=%s";
     */

    if (path.endsWith("TEZ_DAG_ID")) {
      return readAllFromFile(DAG_DATA_PATH);
    } else if (path.endsWith("TEZ_VERTEX_ID")) {
      return readAllFromFile(VERTEX_DATA_PATH);
    } else if (path.endsWith("TEZ_TASK_ID")) {
      return readAllFromFile(TASK_DATA_PATH);
    } else if (path.endsWith("TEZ_TASK_ATTEMPT_ID")) {
      return readAllFromFile(TASK_ATTEMPT_PATH);
    } else {
      return readAllFromFile(APPLICATION_DATA_PATH);
    }

  }

  private String readAllFromFile(String path) {
    try {
      return SkylineUtils.readFile(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
