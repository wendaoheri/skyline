package org.dayu.core.es;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dayu.TestBeanEntry;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.TransportAddress;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = TestBeanEntry.class)
@RunWith(SpringRunner.class)
@Slf4j
public class ESClient {

  @Autowired
  private TransportClient client;

  @Test
  public void get(){
    List<TransportAddress> addressList = client.transportAddresses();
    log.info(addressList.toString());
  }

}
