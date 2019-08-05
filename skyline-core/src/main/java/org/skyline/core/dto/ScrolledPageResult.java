package org.skyline.core.dto;

import java.util.List;
import lombok.Data;

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

}
