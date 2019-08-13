package org.skyline.common.data;

import com.alibaba.fastjson.JSONArray;
import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.Test;

/**
 * @author Sean Liu
 * @date 2019-08-13
 */
public class RecordsTest {

  @Test
  public void testFromObject(){
    List<YarnApplication> lists = Lists.newArrayList();
    for(int i = 0;i < 10;i ++){
      YarnApplication app = new YarnApplication();
      app.setApplicationId("testId");
      lists.add(app);
    }
    JSONArray ja = Records.fromObject(lists);
    System.out.println(ja);
  }

}
