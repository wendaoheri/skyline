package org.skyline.common.data;

import lombok.Data;

/**
 * This class is used to display message for end-user
 *
 * @author Sean Liu
 * @date 2019-07-13
 */
@Data
public class DisplayMessage {

  public static final DisplayMessage EMPTY_MESSAGE = new DisplayMessage();

  private String content;

  private String detail;

}
