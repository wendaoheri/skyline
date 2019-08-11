package org.skyline.core.graph;

import java.util.UUID;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-08-10
 */
@Data
public class TezEdge {
  private String edgeId;

  private String edgeProps;

  public static TezEdge newInstance(){
    TezEdge tezEdge = new TezEdge();
    tezEdge.setEdgeId(UUID.randomUUID().toString());
    tezEdge.setEdgeProps(UUID.randomUUID().toString());
    return tezEdge;
  }
}
