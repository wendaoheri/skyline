package org.dayu.core.service;

import com.alibaba.fastjson.JSON;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.dayu.TestBeanEntry;
import org.dayu.core.dto.Filter;
import org.dayu.core.dto.Filter.FilterType;
import org.dayu.core.dto.Order;
import org.dayu.core.dto.Order.OrderType;
import org.dayu.core.dto.SearchRequestDTO;
import org.dayu.core.model.YarnApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class YarnApplicationServiceTest {

  @Autowired
  private YarnApplicationService yarnApplicationService;

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
    List<YarnApplication> applications = yarnApplicationService.search(request);
    for (YarnApplication ya : applications) {
      log.info(ya.toString());
    }
  }

}
