package org.skyline.core.storage.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.common.data.AdvisorConfig;
import org.skyline.common.data.Records;
import org.skyline.common.data.RuntimeConfig;
import org.skyline.common.data.YarnApplication;
import org.skyline.core.TestBeanEntry;
import org.skyline.core.dto.Order;
import org.skyline.core.dto.Order.OrderType;
import org.skyline.core.dto.ScrolledPageResult;
import org.skyline.core.dto.SearchRequest;
import org.skyline.core.queue.MessageQueue;
import org.skyline.core.storage.IStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */

@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class ESStorageTest {

  @Autowired
  private IStorage storage;

  @MockBean
  private MessageQueue messageQueue;

  @Test
  public void testUpsert() {

    RuntimeConfig rc = new RuntimeConfig();
    rc.setId("runtime_key");
    rc.setValue("test_values");
    JSONObject r = Records.fromObject(rc);

    log.info(r.toString());

    storage.upsert(RuntimeConfig.INDEX_NAME, RuntimeConfig.TYPE_NAME, r);

  }

  @Test
  public void testFindById() {
    RuntimeConfig rc = storage.findById("test", "test", "runtime_key", RuntimeConfig.class);
    log.info(rc.toString());
  }

  @Test
  public void testFindByDsl() {
    List<YarnApplication> result = storage
        .findByDSL("yarn_application", "yarn_application", "{\n"
            + "    \"ids\": {\n"
            + "      \"values\": [\n"
            + "        \"application_1553247628289_1396961\"\n"
            + "      ]\n"
            + "    }\n"
            + "  }", YarnApplication.class, "application_type", "final_status");
    log.info(result.toString());
  }

  @Test
  public void testScrollSearch() {
    SearchRequest searchRequest = new SearchRequest();
    searchRequest.setKeyword("application_*");

    searchRequest.setFields(Lists.newArrayList(
        "application_id",
        "application_type",
        "elapsed_time",
        "started_time")
    );

    searchRequest.setPage(1);

    searchRequest.setOrders(Lists.newArrayList(new Order(OrderType.DESC, "elapsed_time")));

    ScrolledPageResult<YarnApplication> result = storage
        .scrollSearch(YarnApplication.INDEX_NAME, YarnApplication.TYPE_NAME, searchRequest,
            YarnApplication.class);
    log.info("Search result : {}", JSON.toJSONString(result, true));
  }

  @Test
  public void testFindAll() {
    List<AdvisorConfig> configs = storage
        .findAll(AdvisorConfig.INDEX_NAME, AdvisorConfig.TYPE_NAME, AdvisorConfig.class, false,"display");
    log.info("Advisor configs : {}", JSON.toJSONString(configs, true));
  }

}
