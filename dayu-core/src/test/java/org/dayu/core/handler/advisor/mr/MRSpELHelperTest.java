package org.dayu.core.handler.advisor.mr;

import lombok.extern.slf4j.Slf4j;
import org.dayu.common.data.mr.MRApplicationData;
import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Sean Liu
 * @date 2019-08-01
 */
@Slf4j
public class MRSpELHelperTest extends BaseAdvisorTest {

  private MRSpELHelper helper = new MRSpELHelper();

  @Test
  public void testEval() throws NoSuchMethodException {

//    String el = "taskDataList.?[type.name()=='MAP'].![elapsedTime]";
    String el = "#summary(taskDataList.?[type.name()=='MAP'].![taskCounterData.getCounterValue('HDFS_BYTES_READ')]).cv";
    MRApplicationData applicationData = generateApplicationData();
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression(el);

    StandardEvaluationContext context = new StandardEvaluationContext(applicationData);

    Object value = exp.getValue(context);

    log.info("Value : {}", value);
  }

}
