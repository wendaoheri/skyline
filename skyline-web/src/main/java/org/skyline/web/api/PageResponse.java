package org.skyline.web.api;

import java.util.List;
import lombok.Data;
import org.springframework.data.domain.Page;

/**
 * @author Sean Liu
 * @date 2019-03-27
 */
@Data
public class PageResponse {

  List<Object> data;

  private int totalPages;
  private long totalElements;
  private int size;
  private boolean first;
  private boolean last;
  private boolean empty;

  public static PageResponse fromPage(Page page) {
    PageResponse pageResponse = new PageResponse();
    pageResponse.setData(page.getContent());
    pageResponse.setTotalPages(page.getTotalPages());
    pageResponse.setTotalElements(page.getTotalElements());
    pageResponse.setSize(page.getSize());
    pageResponse.setFirst(page.isFirst());
    pageResponse.setLast(page.isLast());
    pageResponse.setEmpty(page.isEmpty());
    return pageResponse;
  }

}
