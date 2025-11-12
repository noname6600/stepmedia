package com.thanhvh.shopmanagement;

import com.thanhvh.jpa.datasource.DatabaseConfiguration;
import com.thanhvh.scheduler.config.EnableSchedulerAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Import(DatabaseConfiguration.class)
@EnableAsync
@EnableSchedulerAutoConfiguration
public class ShopManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopManagementApplication.class, args);
    }

}
