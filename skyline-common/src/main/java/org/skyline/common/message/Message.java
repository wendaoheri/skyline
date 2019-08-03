package org.skyline.common.message;

import java.io.Serializable;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Data
public class Message<T> implements Serializable {

  private MessageType messageType;

  private T messageContent;

}
