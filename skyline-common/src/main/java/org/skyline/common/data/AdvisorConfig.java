package org.skyline.common.data;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.util.SortedMap;
import lombok.Data;
import org.skyline.common.data.YarnApplication.ApplicationType;

/**
 * @author Sean Liu
 * @date 2019-08-02
 */
@Data
public class AdvisorConfig {

  public static final String INDEX_NAME = "advisor_config";
  public static final String TYPE_NAME = "_doc";

  @JSONField(name = "_id")
  private String id;

  private String name;
  private String describe;
  private String display;

  @JSONField(name = "display_detail")
  private String displayDetail;

  @JSONField(name = "measure_exp")
  private String measureExp;

  @JSONField(name = "weight_exp")
  private String weightExp;

  @JSONField(name = "score_exp")
  private String scoreExp;

  private double[] limits;
  private Integer order;
  private SortedMap<String, Object> variables;

  @JSONField(name = "application_type")
  private ApplicationType applicationType;
}
