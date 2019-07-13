package org.dayu.common.message;

import java.io.Serializable;
import lombok.Data;

/**
 * @author Sean Liu
 * @date 2019-07-13
 */
@Data
public class Message implements Serializable {

  private MessageType messageType;

  private Object messageContent;

}
