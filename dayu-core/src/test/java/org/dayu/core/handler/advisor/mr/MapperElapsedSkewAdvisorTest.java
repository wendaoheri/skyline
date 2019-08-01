package org.dayu.core.handler.advisor.mr;

import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.dayu.common.data.ApplicationData;
import org.dayu.common.data.ResultDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-07-31
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class MapperElapsedSkewAdvisorTest extends BaseAdvisorTest {

  @Autowired
  private MapperElapsedSkewAdvisor advisor;

  @Test
  public void testAdvise() {
    ApplicationData applicationData = generateApplicationData();
    log.info("applicationData : {}", applicationData);

    ResultDetail detail = advisor.advise(applicationData);
    log.info("result detail : {}", detail);

    log.info("Describe : {}", advisor.describe());
  }

}
