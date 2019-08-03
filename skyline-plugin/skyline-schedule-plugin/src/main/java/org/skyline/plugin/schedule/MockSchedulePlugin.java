package org.skyline.plugin.schedule;

import java.util.Random;

public class MockSchedulePlugin implements SchedulePlugin {

  private Random r = new Random();

  @Override
  public ScheduleTrigger getScheduleIdByApplicationId(String applicationId) {
    if (r.nextBoolean()) {
      return null;
    }
    return null;
  }
}
