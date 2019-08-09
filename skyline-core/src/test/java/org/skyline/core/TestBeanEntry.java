package org.skyline.core;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.annotation.EnableJms;

/**
 * TestBeanEntry
 *
 * @author Sean
 * @date 2019/2/4
 */
@EnableAutoConfiguration
@ComponentScan({"org.skyline"})
@EnableJms
public class TestBeanEntry {

}
