package org.skyline.core.handler.advisor.mr;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.skyline.common.data.mr.MRApplicationData;
import org.skyline.core.handler.advisor.SpELHelper;
import org.skyline.core.utils.StatUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * @author Sean Liu
 * @date 2019-08-01
 */
@Slf4j
public class SpELHelperTest extends BaseAdvisorTest {

  private SpELHelper helper = new SpELHelper();

  @Test
  public void testEval() throws NoSuchMethodException {

    String el = "#expTest";
//    String el = "#summary(taskDataList.?[type.name()=='MAP'].![taskCounterData.getCounterValue('HDFS_BYTES_READ')]).cv";
    MRApplicationData applicationData = generateApplicationData();
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression(el);

    StandardEvaluationContext context = new StandardEvaluationContext(applicationData);
    context.setVariable("summary", StatUtils.class.getDeclaredMethod("summary", double[].class));

    Map<String, String> variables = Maps.newHashMap("expTest", "#summary({1,2,3,4})");

    addExtraVariables(context, variables);

    Object value = exp.getValue(context);

    log.info("Value : {}", value);
  }

  private void addExtraVariables(final EvaluationContext rootContext,
      Map<String, String> variables) {
    variables.forEach((name, expStr) -> {
      ExpressionParser parser = new SpelExpressionParser();
      Expression exp = parser.parseExpression(expStr);
      Object variable = exp.getValue(rootContext);
      rootContext.setVariable(name, variable);
    });
  }
}
