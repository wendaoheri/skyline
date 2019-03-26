package org.dayu.core.service.impl;

import com.google.common.collect.Lists;
import java.util.List;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dayu.core.dto.SearchRequestDTO;
import org.dayu.core.model.ApplicationScheduleInfo;
import org.dayu.core.model.RuntimeConfig;
import org.dayu.core.model.YarnApplication;
import org.dayu.core.repository.ApplicationScheduleInfoRepository;
import org.dayu.core.repository.RuntimeConfigRepository;
import org.dayu.core.repository.YarnApplicationRepository;
import org.dayu.core.service.YarnApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author sean
 */
@Service
@Slf4j
public class YarnApplicationServiceImpl implements YarnApplicationService {

  @Autowired
  private RuntimeConfigRepository runtimeConfigRepository;

  @Autowired
  private YarnApplicationRepository yarnApplicationRepository;

  @Autowired
  private ApplicationScheduleInfoRepository applicationScheduleInfoRepository;

  @Override
  public long getLastFetchTime() {
    RuntimeConfig rc = runtimeConfigRepository
        .findByRuntimeKey(TASK_FETCHER_LAST_FETCH_TIME_KEY);
    if (null != rc) {
      String lastFetchTimeStr = rc.getRuntimeValue();
      return Long.parseLong(lastFetchTimeStr);
    }
    return 0;
  }

  @Override
  public void setLastFetchTime(long lastFetchTime) {
    String lastFetchTimeStr = String.valueOf(lastFetchTime);
    RuntimeConfig rc = runtimeConfigRepository
        .findByRuntimeKey(TASK_FETCHER_LAST_FETCH_TIME_KEY);
    if (null != rc) {
      rc.setRuntimeValue(lastFetchTimeStr);
    } else {
      rc = new RuntimeConfig();
      rc.setRuntimeKey(TASK_FETCHER_LAST_FETCH_TIME_KEY);
      rc.setRuntimeValue(lastFetchTimeStr);
    }
    runtimeConfigRepository.save(rc);
  }

  @Override
  public void saveApplicationList(List<YarnApplication> apps) {
    yarnApplicationRepository.saveAll(apps);
    log.info("Saved application list success : {}", apps.size());
  }

  @Override
  public List<String> getIdsWithoutScheduleInfo(long begin, long end) {
    return yarnApplicationRepository.findIdsWithoutScheduleInfo(begin, end);
  }

  @Override
  public void saveApplicationScheduleInfoList(List<ApplicationScheduleInfo> asList) {
    applicationScheduleInfoRepository.saveAll(asList);
  }

  @Override
  public List<YarnApplication> getApplications(long begin, long end) {
    return yarnApplicationRepository.findByFinishedTimeBetween(begin, end);
  }

  @Override
  public YarnApplication getApplicationById(String applicationId) {
    return yarnApplicationRepository.findById(applicationId).get();
  }

  @Override
  public List<YarnApplication> search(SearchRequestDTO request) {
    return yarnApplicationRepository.findAll((Specification<YarnApplication>) (root, query, cb) -> {
      List<Predicate> where = Lists.newArrayList();
      List<Order> orders = Lists.newArrayList();
      String keyword = request.getKeyword();
      if (StringUtils.isNotEmpty(keyword)) {
        Predicate keywordPredicate = cb
            .or(cb.like(cb.upper(root.get("name")), "%" + keyword.toUpperCase() + "%"),
                cb.like(cb.upper(root.get("id")), "%" + keyword.toUpperCase() + "%"));
        where.add(keywordPredicate);
      }
      if (request.getFilters() != null) {
        request.getFilters().forEach(x -> {
          switch (x.getFilterType()) {
            case EQ:
              where.add(cb.equal(root.get(x.getName()), x.getValue()));
              break;
            case GT:
              where.add(cb.greaterThanOrEqualTo(root.get(x.getName()), x.getValue()));
              break;
            case LT:
              where.add(cb.lessThanOrEqualTo(root.get(x.getName()), x.getValue()));
              break;
            default:
              log.warn("No Mapped filter");
              break;
          }
        });
      }
      if (request.getOrders() != null) {
        request.getOrders().forEach(x -> {
          switch (x.getOrderType()) {
            case DESC:
              orders.add(cb.desc(root.get(x.getName())));
              break;
            case ASC:
              orders.add(cb.asc(root.get(x.getName())));
              break;
            default:
              orders.add(cb.asc(root.get(x.getName())));
              break;
          }
        });
      }

      Predicate[] predicates = where.toArray(new Predicate[where.size()]);

      return query.where(predicates).orderBy(orders).getGroupRestriction();
    });
  }
}
