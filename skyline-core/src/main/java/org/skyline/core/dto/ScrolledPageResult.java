package org.skyline.core.dto;

import java.util.Collections;
import java.util.List;
import lombok.Data;
import org.springframework.util.CollectionUtils;

/**
 * @author Sean Liu
 * @date 2019-08-05
 */
@Data
public class ScrolledPageResult<T> {


  private String scrollId;

  private List<T> content;

  private int page;

  private int size;

  private long total;

  public int getTotalPages() {
    return (int) (total % size == 0 ? total / size : (total / size) + 1);
  }

  public boolean isFirst() {
    return page == 1;
  }

  public boolean isLast() {
    return page == getTotalPages();
  }

  public boolean isEmpty() {
    return CollectionUtils.isEmpty(this.content);
  }
}
