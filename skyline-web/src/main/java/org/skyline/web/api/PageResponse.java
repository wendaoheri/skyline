package org.skyline.web.api;

import com.alibaba.fastjson.JSON;
import java.util.List;
import lombok.Data;
import org.skyline.core.dto.ScrolledPageResult;

/**
 * @author Sean Liu
 * @date 2019-03-27
 */
@Data
public class PageResponse<T> {

  List<T> data;

  private int totalPages;
  private long totalElements;
  private int size;
  private boolean first;
  private boolean last;
  private boolean empty;

  public static <T> String fromResult(ScrolledPageResult<T> result) {
    PageResponse<T> pageResponse = new PageResponse();
    pageResponse.setData(result.getContent());
    pageResponse.setTotalPages(result.getTotalPages());
    pageResponse.setTotalElements(result.getTotal());
    pageResponse.setSize(result.getSize());
    pageResponse.setFirst(result.isFirst());
    pageResponse.setLast(result.isLast());
    pageResponse.setEmpty(result.isEmpty());
    return JSON.toJSONString(pageResponse);
  }

}
