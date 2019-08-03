package org.skyline.core.data;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.assertj.core.util.Lists;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.skyline.common.data.YarnApplication;
import org.skyline.common.data.YarnApplication.ApplicationType;
import org.skyline.common.data.YarnApplication.FinalStatus;
import org.skyline.common.data.YarnApplication.State;
import org.skyline.core.utils.SkylineUtils;

/**
 * @author Sean Liu
 * @date 2019-07-07
 */
@Slf4j
public class ApplicationAnswer implements Answer<String> {

  private static final long clusterId = SkylineUtils
      .randomTimestamp(System.currentTimeMillis() - 2 * SkylineUtils.DAY_MS,
          System.currentTimeMillis() - SkylineUtils.DAY_MS);
  private int invokeTimes = 0;

  public static YarnApplication getTemplate() {
    long curr = System.currentTimeMillis();
    long idNum = SkylineUtils.randomRange(0, 1000000L);
    YarnApplication app = new YarnApplication();
    app.setState(SkylineUtils.randomChoice(State.class).name());
    app.setUser(SkylineUtils.randomStr("user", 10));
    app.setApplicationType(SkylineUtils.randomChoice(ApplicationType.class).name());
    app.setClusterId(clusterId);
    app.setApplicationId(String.format("application_%s_%s", clusterId, idNum));
    app.setId(app.getApplicationId());
    app.setFinalStatus(SkylineUtils.randomChoice(FinalStatus.class).name());
    app.setProgress((int) SkylineUtils.randomRange(0, 100));
    app.setName(SkylineUtils.randomStr("name", 1000));
    app.setQueue(SkylineUtils.randomStr("queue", 10));
    app.setAllocatedMB(SkylineUtils.randomRange(0, 1000000));
    app.setAllocatedVCores(SkylineUtils.randomRange(0, 1000));
    app.setRunningContainers((int) SkylineUtils.randomRange(0, 1000));
    app.setMemorySeconds(SkylineUtils.randomRange(0, 100000000L));
    app.setVcoreSeconds(SkylineUtils.randomRange(0, 100000000L));
    app.setStartedTime(SkylineUtils.randomTimestamp(clusterId, curr - 60 * 3600));
    app.setFinishedTime(SkylineUtils.randomTimestamp(app.getStartedTime(), curr));
    app.setElapsedTime((int) (app.getFinishedTime() - app.getStartedTime()));
    return app;
  }

  @Override
  public String answer(InvocationOnMock invocationOnMock) throws Throwable {
    invokeTimes++;
    log.info("invoke times : {}", invokeTimes);
    if (invokeTimes % 2 == 1) {
      throw new IOException();
    }
    String url = invocationOnMock.getArgument(0);
    String queryStr = url.split("\\?")[1];
    List<NameValuePair> paramPair = URLEncodedUtils.parse(queryStr, Charset.forName("UTF-8"));
    Map<String, String> params = paramPair.stream()
        .collect(Collectors.toMap(NameValuePair::getName, v -> v.getValue()));

    int limit = Integer.parseInt(params.getOrDefault("limit", "10"));

    List<YarnApplication> app = Lists.newArrayList();
    for (int i = 0; i < limit; i++) {
      app.add(getApplication(params));
    }
    return "{\"apps\":{\"app\":" + JSON.toJSONString(app) + "}}";
  }

  public YarnApplication getApplication(Map<String, String> params) {

    YarnApplication app = getTemplate();

    if (params.containsKey("states")) {
      app.setState(SkylineUtils.randomChoice(params.get("states").split(",")));
    }
    if (params.containsKey("state")) {
      app.setState(params.get("state"));
    }
    if (params.containsKey("finalStatus")) {
      app.setFinalStatus(params.get("finalStatus"));
    }
    if (params.containsKey("user")) {
      app.setUser(params.get("user"));
    }
    if (params.containsKey("queue")) {
      app.setQueue(params.get("queue"));
    }
    if (params.containsKey("applicationTypes")) {
      app.setApplicationType(SkylineUtils.randomChoice(params.get("applicationTypes").split(",")));
    }
    if (params.containsKey("applicationTags")) {
      app.setApplicationTags(SkylineUtils.randomChoice(params.get("applicationTags").split(",")));
    }
    if (params.containsKey("startedTimeBegin") || params.containsKey("startedTimeEnd")) {
      app.setStartedTime(
          SkylineUtils.randomTimestamp(Long.valueOf(params.getOrDefault("startedTimeBegin", "0")),
              Long.valueOf(params.getOrDefault("startedTimeEnd", "0"))));
    }
    if (params.containsKey("finishedTimeBegin") || params.containsKey("finishedTimeEnd")) {
      app.setStartedTime(
          SkylineUtils.randomTimestamp(Long.valueOf(params.getOrDefault("finishedTimeBegin", "0")),
              Long.valueOf(params.getOrDefault("finishedTimeEnd", "0"))));
    }

    return app;
  }

}
