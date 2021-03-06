package org.skyline.core.handler.advisor.mr;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.DisplayMessage;
import org.skyline.common.data.HandlerResult;
import org.skyline.common.data.mr.MRApplicationData;
import org.skyline.core.TestBeanEntry;
import org.skyline.core.handler.advisor.SpELHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.expression.spel.support.StandardEvaluationContext;
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

  @Autowired
  private SpELHelper spELHelper;

  @Test
  public void testAdvisor() {
    ApplicationData applicationData = generateApplicationData();
    HandlerResult handlerResult = mrAdvisor.handle(applicationData);
    handlerResult.setApplicationData(applicationData);
    log.info("Result summary : {}", JSON.toJSONString(handlerResult.getAdviseDetails(), true));

    DisplayMessage ds = mrAdvisor.display(handlerResult);
    log.info("Display Message : {}", JSON.toJSONString(ds, true));
  }

  @Test
  public void testExpression() {
    MRApplicationData data = generateApplicationData();
    StandardEvaluationContext context = spELHelper.getContext(data);
    Object value = spELHelper.eval("T(java.lang.Long).parseLong(conf.getProperty('mapreduce.map.memory.mb'))", context, null);
    log.info("Expression value : {}", value);
  }

}
