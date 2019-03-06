package org.dayu.core.repository;

import org.dayu.core.model.RuntimeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * RuntimeConfigRepository
 *
 * @author Sean
 * @date 2019/2/5
 */
@Repository
public interface RuntimeConfigRepository extends JpaRepository<RuntimeConfig, String> {

  boolean existsByRuntimeKey(String runtimeKey);

  RuntimeConfig findByRuntimeKey(String runtimeKey);

}
