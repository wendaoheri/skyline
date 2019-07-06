package org.dayu.core.storage.es;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.dayu.common.model.Record;
import org.dayu.common.model.Records;
import org.dayu.common.model.RuntimeConfig;
import org.dayu.common.model.YarnApplication;
import org.dayu.core.storage.IStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

  @Test
  public void testUpsert() {

    RuntimeConfig rc = new RuntimeConfig();
    rc.setId("runtime_key");
    rc.setValue("test_values");
    Record r = Records.fromObject(rc);

    log.info(r.toString());

    storage.upsert(RuntimeConfig.DATABASE_NAME, RuntimeConfig.TABLE_NAME, r);

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

}
