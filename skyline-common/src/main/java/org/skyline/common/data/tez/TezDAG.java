package org.skyline.common.data.tez;

import java.util.List;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-08-09
 */
@Data
public class TezDAG {

  private List<TezEvent> events;

  private String dagId;

}
