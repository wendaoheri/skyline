package org.dayu.core.handler.advisor.mr;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.InputStream;
import org.dayu.common.data.mr.MRApplicationData;
import org.dayu.core.MockBeanTest;

/**
 * @author Sean Liu
 * @date 2019-07-31
 */
public class BaseAdvisorTest extends MockBeanTest {

  public MRApplicationData generateApplicationData() {
    InputStream in = this.getClass().getClassLoader()
        .getResourceAsStream("data/applicationData.json");
    try {
      return JSON.parseObject(in, MRApplicationData.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
