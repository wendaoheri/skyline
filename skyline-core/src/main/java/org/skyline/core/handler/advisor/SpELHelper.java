package org.skyline.core.handler.advisor;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.ApplicationData;
import org.skyline.core.utils.DisplayUtils;
import org.skyline.core.utils.StatUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author Sean Liu
 * @date 2019-08-01
 */
@SuppressWarnings("ALL")
@Configuration
@Component
@Slf4j
public class SpELHelper {

  private static ExpressionParser parser = new SpelExpressionParser();


  /**
   * evalValue a expression with given rootObject and variables
   *
   * @param el expression
   */
  public double evalValue(String el, StandardEvaluationContext context) {
    Object result = this.eval(el, context, null);
    return (double) result;

  }

  public String evalTmplate(String el, StandardEvaluationContext context) {
    Object result = this.eval(el, context, TemplateParserContext.INSTANCE);

    return (String) result;
  }

  private Object eval(String el, StandardEvaluationContext context, ParserContext parserContext) {

    Expression exp = parser.parseExpression(el, parserContext);

    return exp.getValue(context);

  }

  /**
   * Get common context, and add some functions
   */
  public StandardEvaluationContext getContext(ApplicationData applicationData) {
    StandardEvaluationContext context = new StandardEvaluationContext(applicationData);
    context.setVariable("STAT", StatUtils.class);
    context.setVariable("DISPLAY", DisplayUtils.class);
    return context;
  }

  /**
   * Add variables to context, if variable is string, parse it as Expression Language
   *
   * @return evaluated variables
   */
  public Map<String, Object> addVariables(final StandardEvaluationContext context,
      final Map<String, Object> variables, boolean evalVariable) {
    if (evalVariable) {
      return addVariables(context, variables);
    } else {
      context.setVariables(variables);
    }
    return variables;
  }

  /**
   * Evaluate variable recursive
   */
  private Map<String, Object> addVariables(final StandardEvaluationContext context,
      final Map<String, Object> variables) {
    Map<String, Object> result = Maps.newHashMap();
    variables.forEach((k, v) -> {
      if (v instanceof String) {
        String expStr = (String) v;
        if (context.lookupVariable(k) == null) { // prevent eval multi times
          Map<String, Object> references = findReferences(expStr, variables);
          if (!CollectionUtils.isEmpty(references)) {
            result.putAll(addVariables(context, references));
          }
          Expression exp = parser.parseExpression(expStr);
          Object variable = exp.getValue(context);
          context.setVariable(k, variable);
          result.put(k, variable);
        }
      } else {
        context.setVariable(k, v);
        result.put(k, v);
      }


    });

    return result;
  }

  private Map<String, Object> findReferences(String exp, Map<String, Object> variables) {
    return variables.entrySet().parallelStream().filter(x -> exp.contains("#" + x.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  static class TemplateParserContext implements ParserContext {

    public static final TemplateParserContext INSTANCE = new TemplateParserContext();

    @Override
    public String getExpressionPrefix() {
      return "${";
    }

    @Override
    public String getExpressionSuffix() {
      return "}";
    }

    @Override
    public boolean isTemplate() {
      return true;
    }
  }

}
