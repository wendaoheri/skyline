package org.skyline.core.http;

import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Sean Liu
 * @date 2019-07-07
 */
@Component
@Slf4j
public class HadoopHACallService {

  @Autowired
  private HttpCallService httpCallService;

  private Map<String, Integer> currDomainIndexMap = Maps.newConcurrentMap();

  public String doGet(String domain, String pathAndParam) throws IOException {
    String currDomain;
    String url;
    String[] domains = domain.split(",");

    for (int i = 0; i < domains.length; i++) {
      domains[i] = domains[i].replace("http://", "");
    }

    String domainKey = String.join(",", domains);
    currDomain = getCurrDomain(domainKey, domains);

    url = "http://" + currDomain + pathAndParam;
    try {
      return httpCallService.doGet(url);
    } catch (IOException e) {
      log.info("Invoke url error : {}", url);
      if (domains.length > 1) {
        switchDomain(domainKey, domains.length);
        currDomain = getCurrDomain(domainKey, domains);
        url = "http://" + currDomain + pathAndParam;
        return httpCallService.doGet(url);
      } else {
        throw e;
      }
    }
  }

  private String getCurrDomain(String domainKey, String[] domains) {
    String currDomain;
    if (domains.length == 1) {
      currDomain = domains[0];
    } else {
      Integer currDomainIndex = currDomainIndexMap.get(domainKey);
      if (currDomainIndex == null) {
        currDomainIndex = 0;
        currDomainIndexMap.put(domainKey, currDomainIndex);
      }
      currDomain = domains[currDomainIndex];
    }
    return currDomain;
  }

  private void switchDomain(String domainKey, int domainSize) {
    log.info("Domain key : {}, domain size : {}", domainKey, domainSize);
    Integer lastIndex = currDomainIndexMap.get(domainKey);
    Integer currIndex = (lastIndex + 1) >= domainSize ? 0 : lastIndex + 1;
    currDomainIndexMap.put(domainKey, currIndex);
    log.info("Switch available domain {} from {} to {}", domainKey, lastIndex, currIndex);
  }


}
