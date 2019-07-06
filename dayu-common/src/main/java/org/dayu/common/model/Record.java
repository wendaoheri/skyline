package org.dayu.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;

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


  public void merge(Object o) {
    Field[] fileds = o.getClass().getDeclaredFields();
    for (Field field : fileds) {
      try {
        field.setAccessible(true);
        int modifiers = field.getModifiers();
        if (!Modifier.isTransient(modifiers)) {
          Object object = field.get(o);
          JSONField jsonField = field.getAnnotation(JSONField.class);
          String fieldName = jsonField == null ? field.getName() : jsonField.name();
          if (null != object) {
            this.put(fieldName, object);
          }
        }
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public void merge(String fieldName, List<String> list) {
    this.put(fieldName, list);
  }
}
