package org.skyline.core.graph;


import com.alibaba.fastjson.JSON;
import com.google.common.graph.ImmutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author Sean Liu
 * @date 2019-08-10
 */
@Slf4j
public class GraphTest {

  @Test
  public void testGraph() {

    // v1 - e1 -> v3
    // v2 - e2 -> v3
    // v4 - e3 -> v5
    // v3 - e4 -> v5

    TezVertex v1 = TezVertex.newInstance();
    TezVertex v2 = TezVertex.newInstance();
    TezVertex v3 = TezVertex.newInstance();
    TezVertex v4 = TezVertex.newInstance();
    TezVertex v5 = TezVertex.newInstance();

    TezEdge e1 = TezEdge.newInstance();
    TezEdge e2 = TezEdge.newInstance();
    TezEdge e3 = TezEdge.newInstance();
    TezEdge e4 = TezEdge.newInstance();

    ImmutableValueGraph<TezVertex, TezEdge> dag = ValueGraphBuilder
        .directed()
        .<TezVertex, TezEdge>immutable()
        .putEdgeValue(v1, v3, e1)
        .putEdgeValue(v2, v3, e2)
        .putEdgeValue(v4, v5, e3)
        .putEdgeValue(v3, v5, e4)
        .build();
    log.info("Dag : {}", dag);
    log.info("Dag JSON : {}", JSON.toJSONString(dag,true));
  }

}
