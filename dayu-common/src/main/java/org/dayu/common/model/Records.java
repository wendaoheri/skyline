package org.dayu.common.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.collect.Lists;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Sean Liu
 * @date 2019-04-17
 */
@Slf4j
public class Records {

  public static List<Record> fromObject(Collection col) {
    List<Record> records = Lists.newArrayList();
    if (col == null) {
      return records;
    }
    for (Object o : col) {
      records.add(fromObject(o));
    }
    return records;
  }

  public static Record fromObject(Object o) {
    Record r = new Record();
    if (null == o) {
      return r;
    }
    Field[] fields = o.getClass().getDeclaredFields();
    for (Field field : fields) {
      try {
        field.setAccessible(true);
        int modifiers = field.getModifiers();
        if (!Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers)) {
          Object object = field.get(o);
          JSONField jsonField = field.getAnnotation(JSONField.class);
          String fieldName = jsonField == null ? field.getName() : jsonField.name();
          if (null != object) {
            if (object instanceof Collection) {
              Collection<?> c = (Collection<?>) object;
              int i = 0;
              Iterator<?> it = c.iterator();
              while (it.hasNext()) {
                Object _o = it.next();
                r.put(fieldName + "_" + i, _o);
                i++;
              }
            } else {
              r.put(fieldName, object);
            }
          }
        }
      } catch (IllegalArgumentException e) {
        log.error(e.getMessage());
      } catch (IllegalAccessException e) {
        log.error(e.getMessage());
      }
    }
    return r;
  }
}
