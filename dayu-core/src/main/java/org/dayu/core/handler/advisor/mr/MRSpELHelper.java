package org.dayu.core.handler.advisor.mr;

import lombok.extern.slf4j.Slf4j;
import org.dayu.common.data.mr.MRApplicationData;
import org.dayu.core.utils.StatUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-08-01
 */
@Configuration
@Component
@Slf4j
public class MRSpELHelper {


  public double eval(String el, MRApplicationData applicationData) {
    ExpressionParser parser = new SpelExpressionParser();
    Expression exp = parser.parseExpression(el);
    EvaluationContext context = new StandardEvaluationContext(applicationData);
    try {
      context.setVariable("summary", StatUtils.class.getDeclaredMethod("summary", double[].class));
    } catch (NoSuchMethodException e) {
      log.error("No summary method in StatUtils", e);
    }
    Double result = exp.getValue(context, Double.TYPE);
    return result;

  }

}
