package org.dayu;

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
@ComponentScan({"org.dayu"})
@EnableJms
@PropertySource("classpath:/application-handler.yml")
public class TestBeanEntry {

}
