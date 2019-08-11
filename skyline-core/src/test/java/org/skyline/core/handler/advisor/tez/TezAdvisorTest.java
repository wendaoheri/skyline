package org.skyline.core.handler.advisor.tez;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.HandlerResult;
import org.skyline.common.data.YarnApplication;
import org.skyline.common.data.tez.TezApplicationData;
import org.skyline.core.MockBeanTest;
import org.skyline.core.TestBeanEntry;
import org.skyline.core.data.TezDataAnswer;
import org.skyline.core.handler.advisor.SpELHelper;
import org.skyline.core.handler.fetcher.TezFetcher;
import org.skyline.core.http.HttpCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Sean Liu
 * @date 2019-08-11
 */
@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class TezAdvisorTest extends MockBeanTest {

  @Autowired
  private TezFetcher tezFetcher;

  @Autowired
  private TezAdvisor tezAdvisor;

  @Autowired
  private SpELHelper spELHelper;

  @MockBean
  private HttpCallService httpCallService;

  String applicationId = "application_1562840950978_1740308";
  String dagId = "dag_1562840950978_1740308_1";

  @Before
  public void before() throws IOException {
    when(httpCallService.doGet(anyString())).thenAnswer(new TezDataAnswer());
  }

  private ApplicationData generateTezApplicationData() {
    TezApplicationData tezApplicationData = new TezApplicationData();
    YarnApplication application = new YarnApplication();
    application.setApplicationId(applicationId);
    tezApplicationData.setApplication(application);

    HandlerResult handlerResult = tezFetcher.handle(tezApplicationData);
    return handlerResult.getApplicationData();
  }

  @Test
  public void testAdvise() {

    tezAdvisor.handle(this.generateTezApplicationData());

  }

  @Test
  public void testExpression() {
    ApplicationData data = generateTezApplicationData();
    StandardEvaluationContext context = spELHelper.getContext(data);
    Map<String, Object> variables = Maps.newHashMap("fsCounterGroup", "'org.apache.tez.common.counters.FileSystemCounter'");
    variables.put("tasks","dag.getVertices('MAP').![tasks.![counters.getCounterValue(#fsCounterGroup,'HDFS_BYTES_READ')]]");
    spELHelper.addVariables(context,variables,true);
    Object value = spELHelper.eval("#STAT.summary(#tasks.![#STAT.summary(#this).cv]).max", context, null);
    log.info("Expression value : {}", value);
  }
}
