package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.response.DashboardResponse;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.repository.CourseRepository;
import com.binar.byteacademy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "dashboard")
public class DashboardServiceImpl implements DashboardService{
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    @Cacheable(key = "'dashboard'", unless = "#result == null")
    public DashboardResponse getAdminDashboard() {
        int activeUser = userRepository.countByStatus(EnumStatus.ACTIVE);
        int nonActiveUser = userRepository.countByStatus(EnumStatus.INACTIVE);
        int activeCourse = courseRepository.countByCourseStatus(EnumStatus.ACTIVE);
        int nonActiveCourse = courseRepository.countByCourseStatus(EnumStatus.INACTIVE);
        int premiumCourse = courseRepository.countByCourseType(EnumCourseType.PREMIUM);
        int freeCourse = courseRepository.countByCourseType(EnumCourseType.FREE);

        return DashboardResponse.builder()
                .activeUser(activeUser)
                .nonActiveUser(nonActiveUser)
                .activeCourse(activeCourse)
                .nonActiveCourse(nonActiveCourse)
                .premiumCourse(premiumCourse)
                .freeCourse(freeCourse)
                .build();
    }
}
