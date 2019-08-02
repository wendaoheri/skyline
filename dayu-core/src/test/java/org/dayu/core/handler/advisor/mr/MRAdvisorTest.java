package org.dayu.core.handler.advisor.mr;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.DisplayMessage;
import org.dayu.common.data.HandlerResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-08-01
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MRAdvisorTest extends BaseAdvisorTest {

  @Autowired
  private MRAdvisor mrAdvisor;

  @Test
  public void testAdvisor() {
    ApplicationData applicationData = generateApplicationData();
    HandlerResult handlerResult = mrAdvisor.handle(applicationData);
    handlerResult.setApplicationData(applicationData);
    log.info("Result summary : {}", JSON.toJSONString(handlerResult.getAdviseDetails(), true));

    DisplayMessage ds = mrAdvisor.display(handlerResult);
    log.info("Display Message : {}", JSON.toJSONString(ds, true));
  }

}
