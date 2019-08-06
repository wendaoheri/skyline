package org.skyline.core.handler.advisor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.AdviseDetail;
import org.skyline.common.data.AdvisorConfig;
import org.skyline.common.data.ApplicationData;
import org.skyline.common.data.DisplayMessage;
import org.skyline.common.data.HandlerResult;
import org.skyline.common.data.Records;
import org.skyline.common.data.Severity;
import org.skyline.common.data.YarnApplication.ApplicationType;
import org.skyline.core.handler.ApplicationTuningAdvisor;
import org.skyline.core.storage.IStorage;
import org.skyline.core.utils.SeverityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;

/**
 * @author Sean Liu
 * @date 2019-07-29
 */
@SuppressWarnings({"ALL", "AlibabaLowerCamelCaseVariableNaming"})
@Slf4j
public abstract class AbstractAdvisor implements ApplicationTuningAdvisor {

  private static final String ADVISOR_CONFIG = "advisors.json";

  private static Map<ApplicationType, List<AdvisorConfig>> advisorMap = Maps.newHashMap();

  @Autowired
  private IStorage storage;

  @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
  @Autowired
  private SpELHelper spELHelper;

  @PostConstruct
  public void initConfig() {
    if (storage.indexExists(AdvisorConfig.INDEX_NAME)) {
      List<AdvisorConfig> configs = storage
          .findAll(AdvisorConfig.INDEX_NAME, AdvisorConfig.TYPE_NAME, AdvisorConfig.class, true);
      if (CollectionUtils.isEmpty(configs)) {
        initConfigFromFile();
      } else {
        initConfigMap(configs);
      }
    } else {
      initConfigFromFile();
    }

  }

  private void initConfigMap(Collection<AdvisorConfig> configs) {
    for (AdvisorConfig ac : configs) {
      ApplicationType applicationType = ac.getApplicationType();

      List<AdvisorConfig> typeConfigs = advisorMap.get(applicationType);
      if (typeConfigs == null) {
        typeConfigs = Lists.newArrayList();
        advisorMap.put(applicationType, typeConfigs);
      }
      typeConfigs.add(ac);
    }
  }

  private void initConfigFromFile() {
    try {
      // Read advisors from file
      JSONArray ja = JSON
          .parseObject(this.getClass().getClassLoader().getResourceAsStream(ADVISOR_CONFIG),
              JSONArray.class);
      List<AdvisorConfig> configs = Lists.newArrayList();
      for (int i = 0; i < ja.size(); i++) {
        AdvisorConfig advisorConfig = ja.getObject(i, AdvisorConfig.class);
        configs.add(advisorConfig);
      }
      initConfigMap(configs);
      // Save advisors to es
      try {
        storage
            .bulkUpsert(AdvisorConfig.INDEX_NAME, AdvisorConfig.TYPE_NAME,
                Records.fromObject(configs));
      } catch (ExecutionException | InterruptedException e) {
        log.error("Save advisor config error", e);
      }

      log.info("Loaded advisor configs : {}", advisorMap);
    } catch (IOException e) {
      log.error("Init advisors error ", e);
    }
  }

  @Override
  public HandlerResult handle(ApplicationData applicationData) {
    HandlerResult handlerResult = new HandlerResult();
    handlerResult.setId(applicationData.getApplicationId());

    ApplicationType applicationType = applicationData.getApplicationType();
    List<AdvisorConfig> advisorConfigs = getAdvisorConfigByType(applicationType);
    // Sort by order
    advisorConfigs.sort(Comparator.comparingInt(AdvisorConfig::getOrder));

    for (AdvisorConfig advisorConfig : advisorConfigs) {

      StandardEvaluationContext context = spELHelper.getContext(applicationData);
      AdviseDetail detail = new AdviseDetail();

      // This is variables that can be read by el, e.g. measureExp,weightExp,display
      // display exp context is different, so add variable to AdviseDetail, it can
      // be read in method #display()
      Map<String, Object> variables = advisorConfig.getVariables();
      if (!CollectionUtils.isEmpty(variables)) {
        Map<String, Object> variableValues = spELHelper.addVariables(context, variables, true);
        detail.setVariables(variableValues);
      }

      // calculate measure and put it to variables
      String measureExp = advisorConfig.getMeasureExp();
      double measure = spELHelper.evalValue(measureExp, context);
      detail.setMeasure(measure);
      context.setVariable("measure", measure);

      // calculate severity and put it to variables
      Severity severity = SeverityUtils.calSeverity(measure, advisorConfig.getLimits());
      detail.setSeverity(severity);
      context.setVariable("severity", severity);

      // calculate weight and put it to variables
      String weightExp = advisorConfig.getWeightExp();
      double weight = spELHelper.evalValue(weightExp, context);
      detail.setWeight(weight);
      context.setVariable("weight", weight);

      // calculate score
      String scoreExp = advisorConfig.getScoreExp();
      double score = spELHelper.evalValue(scoreExp, context);
      detail.setScore(score);

      detail.setAdvisorName(advisorConfig.getName());

      handlerResult.addDetail(detail);
    }
    storage.upsert(HandlerResult.INDEX_NAME, HandlerResult.TYPE_NAME,
        Records.fromObject(handlerResult));
    return handlerResult;
  }

  @Override
  public DisplayMessage display(HandlerResult handlerResult) {
    DisplayMessage displayMessage = new DisplayMessage();

    ApplicationData applicationData = handlerResult.getApplicationData();
    List<AdviseDetail> adviseDetails = handlerResult.getAdviseDetails();

    StandardEvaluationContext context = spELHelper.getContext(applicationData);

    for (AdviseDetail adviseDetail : adviseDetails) {
      String advisorName = adviseDetail.getAdvisorName();
      AdvisorConfig advisorConfig = getAdvisorConfigByTypeAndName(
          applicationData.getApplicationType(), advisorName);

      // Put advise detail to context
      // And put variables filed to top level
      // And needn't eval variable again
      Map<String, Object> variables = Maps.newHashMap();
      variables.putAll(BeanMap.create(adviseDetail));
      variables.putAll(adviseDetail.getVariables());
      spELHelper.addVariables(context, variables, false);

      String displayExp = advisorConfig.getDisplay();
      String message = spELHelper.evalTmplate(displayExp, context);
      displayMessage.setContent(message);

      String displayDetailExp = advisorConfig.getDisplayDetail();
      String detailMessage = spELHelper.evalTmplate(displayDetailExp, context);
      displayMessage.setDetail(detailMessage);

    }
    return displayMessage;
  }

  protected List<AdvisorConfig> getAdvisorConfigByType(ApplicationType applicationType) {
    List<AdvisorConfig> advisorConfigs = advisorMap.get(applicationType);
    return advisorConfigs;
  }

  protected AdvisorConfig getAdvisorConfigByTypeAndName(ApplicationType applicationType,
      String name) {
    List<AdvisorConfig> advisors = getAdvisorConfigByType(applicationType);
    AdvisorConfig advisorConfig = advisors.parallelStream()
        .filter(x -> x.getName().equalsIgnoreCase(name)).findFirst().get();
    return advisorConfig;
  }

}
