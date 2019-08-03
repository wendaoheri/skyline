package org.dayu.core.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.dayu.TestBeanEntry;
import org.dayu.common.data.YarnApplication;
import org.dayu.core.dto.Filter;
import org.dayu.core.dto.Filter.FilterType;
import org.dayu.core.dto.Order;
import org.dayu.core.dto.Order.OrderType;
import org.dayu.core.dto.SearchRequestDTO;
import org.dayu.core.http.HttpCallService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class YarnApplicationServiceTest {

  @Autowired
  private YarnApplicationService yarnApplicationService;

  @Autowired
  private HttpCallService httpCallService;

  @Test
  public void testGetApplications() {
    List<YarnApplication> applications = yarnApplicationService
        .getApplications(1551863919496L, 1551864708358L);

    for (YarnApplication ya : applications) {
      log.info(ya.toString());
    }
  }

  @Test
  public void testSearch() {
    SearchRequestDTO request = new SearchRequestDTO();
    request.setKeyword("hive");
    List<Filter> filters = Lists.newArrayList();
    filters.add(new Filter(FilterType.EQ, "queue", "root.dsp"));
    filters.add(new Filter(FilterType.GT, "startedTime", "1551863721234"));
    filters.add(new Filter(FilterType.LT, "startedTime", "1551863722370"));
    request.setFilters(filters);

    List<Order> orders = Lists.newArrayList();
    orders.add(new Order(OrderType.ASC, "elapsedTime"));
    orders.add(new Order(OrderType.DESC, "finishedTime"));
    request.setOrders(orders);

    String json = JSON.toJSONString(request);
    log.info(json);
    Page<YarnApplication> page = yarnApplicationService.search(request);
    long totalElements = page.getTotalElements();
    int totalPages = page.getTotalPages();
    List<YarnApplication> apps = page.getContent();
    log.info("total elements : {}, total pages : {}", totalElements, totalPages);
    log.info(JSON.toJSONString(apps));

  }

  @Test
  public void saveApplicationList() throws IOException {
    String result = httpCallService.doGet(
        "http://10.17.183.85:8088/ws/v1/cluster/apps?finalStatus=SUCCEEDED&finishedTimeBegin=1555483300000&finishedTimeEnd=1555483402000");
    log.info(result);
    List<YarnApplication> apps = parseRespToYarnApplicationList(result);
    yarnApplicationService.saveApplicationList(apps);
    yarnApplicationService.saveApplicationListTrace(apps);
  }

  protected List<YarnApplication> parseRespToYarnApplicationList(String resp) {
    JSONObject jo = JSON.parseObject(resp);
    List<YarnApplication> apps = null;
    try {
      apps = jo.getJSONObject("apps").getJSONArray("app")
          .toJavaList(YarnApplication.class);
    } catch (NullPointerException e) {
      log.info("None application list");
    }
    return apps;
  }

  @Test
  public void testSetLastFetchTime() {
    yarnApplicationService.setLastFetchTime(System.currentTimeMillis());
  }

  @Test
  public void testGetWithoutScheduleInfo() {
    List<YarnApplication> apps = yarnApplicationService
        .getWithoutScheduleInfo(1555466987305L, 1555466987305L);
    log.info(apps.toString());
  }
}