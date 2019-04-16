package org.dayu.core.repository;

import java.util.List;
import javax.transaction.Transactional;
import org.dayu.core.model.YarnApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author sean
 */
@Repository
public interface YarnApplicationRepository extends JpaSpecificationExecutor<YarnApplication>,
    JpaRepository<YarnApplication, String> {

//  @Query("select A.id "
//      + "from YarnApplication A "
//      + "left join ApplicationScheduleInfo B "
//      + "on A.id = B.applicationScheduleId.applicationId "
//      + "where B.applicationScheduleId.applicationId is null and A.finishedTime BETWEEN ?1 and ?2")
//  List<String> findIdsWithoutScheduleInfo(long begin, long end);

  List<YarnApplication> findByScheduleIdIsNullAndFinishedTimeBetween(long begin, long end);

  List<YarnApplication> findByFinishedTimeBetween(long begin, long end);

  @Transactional(rollbackOn = Exception.class)
  @Modifying
  @Query("update YarnApplication ya set ya.scheduleId = :scheduleId,ya.newSchedule = :newSchedule where ya.id = :id")
  int setScheduleInfo(String id, String scheduleId,int newSchedule);
}
