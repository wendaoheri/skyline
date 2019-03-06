package org.dayu.core.repository;

import java.util.List;
import org.dayu.core.model.YarnApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface YarnApplicationRepository extends JpaRepository<YarnApplication, String> {

  @Query("select A.id "
      + "from YarnApplication A "
      + "left join ApplicationScheduleInfo B "
      + "on A.id = B.applicationScheduleId.applicationId "
      + "where B.applicationScheduleId.applicationId is null and A.finishedTime BETWEEN ?1 and ?2")
  List<String> findIdsWithoutScheduleInfo(long begin, long end);
}
