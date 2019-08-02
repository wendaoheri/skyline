package org.dayu.common.data;

import java.util.Map;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@Data
public class AdviseDetail {


  private Severity severity;

  private String advisorName;

  private double measure;

  private double weight;

  private double score;

  private Map<String, Object> variables;

  private HandlerStatus handlerStatus;

}
