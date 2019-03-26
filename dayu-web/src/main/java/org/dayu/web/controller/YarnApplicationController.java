package org.dayu.web.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dayu.core.dto.SearchRequestDTO;
import org.dayu.core.model.YarnApplication;
import org.dayu.core.service.YarnApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sean
 */
@RestController
@RequestMapping(value = "/applications")
@Slf4j
public class YarnApplicationController {

  @Autowired
  private YarnApplicationService yarnApplicationService;

  @GetMapping(produces = "application/json;charset=UTF-8")
  public List<YarnApplication> getList() {
    return yarnApplicationService.getApplications(1551863919496L, 1551864708358L);
  }

  @PostMapping(path = "search", produces = "application/json;charset=UTF-8", consumes = "application/json;charset=UTF-8")
  public List<YarnApplication> search(@RequestBody SearchRequestDTO request) {
    log.info("request:{}", request);
    return yarnApplicationService.search(request);
  }

  @GetMapping(path = "{applicationId}", produces = "application/json;charset=UTF-8")
  public YarnApplication get(@PathVariable String applicationId) {
    return yarnApplicationService.getApplicationById(applicationId);
  }


}
