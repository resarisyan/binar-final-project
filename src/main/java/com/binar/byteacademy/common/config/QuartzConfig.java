package com.binar.byteacademy.common.config;

import com.binar.byteacademy.job.LoginReminderJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail loginReminderJobDetail() {
        return JobBuilder.newJob(LoginReminderJob.class)
                .withIdentity("loginReminderJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger loginReminderJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(loginReminderJobDetail())
                .withIdentity("loginReminderJobTrigger")
                .withSchedule(dailyAtHourAndMinute(9,1))
                .build();
    }
}