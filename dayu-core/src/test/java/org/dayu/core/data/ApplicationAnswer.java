package org.dayu.core.data;

import static org.dayu.core.utils.DayuUtils.DAY_MS;

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
import org.dayu.common.model.YarnApplication;
import org.dayu.common.model.YarnApplication.ApplicationType;
import org.dayu.common.model.YarnApplication.FinalStatus;
import org.dayu.common.model.YarnApplication.State;
import org.dayu.core.utils.DayuUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * @author Sean Liu
 * @date 2019-07-07
 */
@Slf4j
public class ApplicationAnswer implements Answer<String> {

  private static final long clusterId = DayuUtils
      .randomTimestamp(System.currentTimeMillis() - 2 * DAY_MS,
          System.currentTimeMillis() - DAY_MS);
  private int invokeTimes = 0;

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
      app.setState(DayuUtils.randomChoice(params.get("states").split(",")));
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
      app.setApplicationType(DayuUtils.randomChoice(params.get("applicationTypes").split(",")));
    }
    if (params.containsKey("applicationTags")) {
      app.setApplicationTags(DayuUtils.randomChoice(params.get("applicationTags").split(",")));
    }
    if (params.containsKey("startedTimeBegin") || params.containsKey("startedTimeEnd")) {
      app.setStartedTime(
          DayuUtils.randomTimestamp(Long.valueOf(params.getOrDefault("startedTimeBegin", "0")),
              Long.valueOf(params.getOrDefault("startedTimeEnd", "0"))));
    }
    if (params.containsKey("finishedTimeBegin") || params.containsKey("finishedTimeEnd")) {
      app.setStartedTime(
          DayuUtils.randomTimestamp(Long.valueOf(params.getOrDefault("finishedTimeBegin", "0")),
              Long.valueOf(params.getOrDefault("finishedTimeEnd", "0"))));
    }

    return app;
  }

  public YarnApplication getTemplate() {
    long curr = System.currentTimeMillis();
    long idNum = DayuUtils.randomRange(0, 1000000L);
    YarnApplication app = new YarnApplication();
    app.setState(DayuUtils.randomChoice(State.class).name());
    app.setUser(DayuUtils.randomStr("user", 10));
    app.setApplicationType(DayuUtils.randomChoice(ApplicationType.class).name());
    app.setClusterId(clusterId);
    app.setApplicationId(String.format("application_%s_%s", clusterId, idNum));
    app.setId(app.getApplicationId());
    app.setFinalStatus(DayuUtils.randomChoice(FinalStatus.class).name());
    app.setProgress((int) DayuUtils.randomRange(0, 100));
    app.setName(DayuUtils.randomStr("name", 1000));
    app.setQueue(DayuUtils.randomStr("queue", 10));
    app.setAllocatedMB(DayuUtils.randomRange(0, 1000000));
    app.setAllocatedVCores(DayuUtils.randomRange(0, 1000));
    app.setRunningContainers((int) DayuUtils.randomRange(0, 1000));
    app.setMemorySeconds(DayuUtils.randomRange(0, 100000000L));
    app.setVcoreSeconds(DayuUtils.randomRange(0, 100000000L));
    app.setStartedTime(DayuUtils.randomTimestamp(clusterId, curr - 60 * 3600));
    app.setFinishedTime(DayuUtils.randomTimestamp(app.getStartedTime(), curr));
    app.setElapsedTime((int) (app.getFinishedTime() - app.getStartedTime()));
    return app;
  }

}
