package org.skyline.web.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.skyline.common.data.YarnApplication;
import org.skyline.core.dto.SearchRequestDTO;
import org.skyline.core.service.YarnApplicationService;
import org.skyline.web.api.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/ws/v1/applications")
@Slf4j
public class YarnApplicationController {

  @Autowired
  private YarnApplicationService yarnApplicationService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<YarnApplication> getList() {
    return yarnApplicationService.getApplications(1551863919496L, 1551864708358L);
  }

  @PostMapping(path = "search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public PageResponse search(@RequestBody SearchRequestDTO request) {
    log.info("request:{}", request);
    return PageResponse.fromPage(yarnApplicationService.search(request));
  }

  @GetMapping(path = "{applicationId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public YarnApplication get(@PathVariable String applicationId) {
    return yarnApplicationService.getApplicationById(applicationId);
  }


}
