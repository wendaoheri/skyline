package org.dayu.core.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-07
 */
@Component
public class HadoopHACallService {

  @Autowired
  private HttpCallService httpCallService;

}
