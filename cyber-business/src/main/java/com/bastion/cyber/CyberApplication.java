package com.bastion.cyber;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Administrator
 */
@SpringBootApplication
@MapperScan("com.bastion.cyber.mapper")
public class CyberApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(CyberApplication.class, args);
    }
}
