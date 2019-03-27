package org.dayu.core.repository;

import java.util.List;
import org.dayu.core.model.ScheduleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author Sean Liu
 * @date 2019-03-26
 */
@Repository
public interface ScheduleInfoRepository extends JpaSpecificationExecutor<ScheduleInfo>,
    JpaRepository<ScheduleInfo, String> {

  List<String> findScheduleIdByScheduleIdIn(Iterable<String> ids);


}
