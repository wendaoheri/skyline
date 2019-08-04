package org.skyline.common.data;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.Map;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@Data
public class AdviseDetail {


  private Severity severity;

  @JSONField(name = "advisor_name")
  private String advisorName;

  private double measure;

  private double weight;

  private double score;

  private Map<String, Object> variables;

  @JSONField(name = "handler_status")
  private HandlerStatus handlerStatus;

}
