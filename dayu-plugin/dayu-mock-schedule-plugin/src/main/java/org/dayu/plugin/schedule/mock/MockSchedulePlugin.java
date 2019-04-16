package org.dayu.plugin.schedule.mock;

import java.util.Random;
import org.dayu.plugin.schedule.SchedulePlugin;

public class MockSchedulePlugin implements SchedulePlugin {

  private Random r = new Random();

  @Override
  public String getScheduleIdByApplicationId(String applicationId) {
    if (r.nextBoolean()) {
      return null;
    }
    return null;
  }
}
