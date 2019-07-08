package org.dayu.common.model;

import java.util.HashMap;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
public class Record extends HashMap<String, Object> {

  private static final long serialVersionUID = -6173379487544700818L;

  @Override
  public Object put(String key, Object value) {
    key = key.toLowerCase();
    return super.put(key, value);
  }

  @Override
  public boolean containsKey(Object key) {
    return super.containsKey(key.toString().toLowerCase());
  }

  @Override
  public Object get(Object key) {
    return super.get(key.toString().toLowerCase());
  }

}
