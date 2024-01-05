package com.binar.byteacademy.job;

import com.binar.byteacademy.dto.request.NotificationRequest;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.repository.UserRepository;
import com.binar.byteacademy.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RequiredArgsConstructor
public class LoginReminderJob implements Job {

    private final NotificationService notificationService;
    private final UserRepository userRepository;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
       userRepository.findInactiveUsers()
                .ifPresent(users -> {
                     users.forEach(user -> {
                          notificationService.sendToUser(NotificationRequest.builder()
                                  .title("Learn Reminder")
                                  .body("You haven't learned for 7 weeks")
                                  .build(), user.getUsername());
                     });
                });
    }
}

