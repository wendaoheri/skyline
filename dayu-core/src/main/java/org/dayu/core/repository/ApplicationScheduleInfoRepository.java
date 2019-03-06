package org.dayu.core.repository;

import org.dayu.core.model.ApplicationScheduleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationScheduleInfoRepository extends JpaRepository<ApplicationScheduleInfo, String> {

}
