package org.dayu.common.data;

import lombok.Data;
import org.dayu.common.model.YarnApplication;

/**
 * @author Sean Liu
 * @date 2019-07-14
 */
@Data
public class MRApplicationData implements ApplicationData {

  private YarnApplication application;

}
