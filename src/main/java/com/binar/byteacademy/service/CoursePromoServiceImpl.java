package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.CoursePromoRequest;
import com.binar.byteacademy.dto.response.CategoryResponse;
import com.binar.byteacademy.dto.response.CoursePromoResponse;
import com.binar.byteacademy.dto.response.CourseResponse;
import com.binar.byteacademy.dto.response.PromoResponse;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.CoursePromo;
import com.binar.byteacademy.entity.Promo;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.exception.ValidationException;
import com.binar.byteacademy.repository.CoursePromoRepository;
import com.binar.byteacademy.repository.CourseRepository;
import com.binar.byteacademy.repository.PromoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.COURSE_PROMO_NOT_FOUND;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "coursePromos")
public class CoursePromoServiceImpl implements CoursePromoService {
    private final CoursePromoRepository coursePromoRepository;
    private final CourseRepository courseRepository;
    private final PromoRepository promoRepository;

    @Override
    public CoursePromoResponse addCoursePromo(CoursePromoRequest request) {
        try {
            Course course = courseRepository.findFirstBySlugCourse(request.getSlugCourse())
                    .orElseThrow(() -> new DataNotFoundException("Course not found"));
            Promo promo = promoRepository.findByPromoCode(request.getPromoCode()).orElseThrow(() -> new DataNotFoundException("Promo not found"));
            coursePromoRepository.findFirstByCourseAndPromo(course, promo)
                    .ifPresentOrElse(coursePromo -> {
                        throw new ValidationException("Course promo already exist");
                    }, () -> {
                        CoursePromo coursePromo = CoursePromo.builder()
                                .course(course)
                                .promo(promo)
                                .build();
                        coursePromoRepository.save(coursePromo);
                    });

            return CoursePromoResponse.builder()
                    .course(
                            CourseResponse.builder()
                                    .courseName(course.getCourseName())
                                    .instructorName(course.getInstructorName())
                                    .courseLevel(course.getCourseLevel())
                                    .courseType(course.getCourseType())
                                    .price(course.getPrice())
                                    .totalCourseRate(course.getTotalCourseRate())
                                    .courseDuration(course.getCourseDuration())
                                    .totalChapters(course.getTotalChapter())
                                    .slugCourse(course.getSlugCourse())
                                    .pathCourseImage(course.getPathCourseImage())
                                    .targetMarket(course.getTargetMarket())
                                    .category(
                                            CategoryResponse.builder().
                                                    categoryName(course.getCategory().getCategoryName())
                                                    .slugCategory(course.getCategory().getSlugCategory())
                                                    .pathCategoryImage(course.getCategory().getPathCategoryImage())
                                                    .build())
                                    .build()
                    )
                    .promo(
                            PromoResponse.builder()
                                    .promoName(promo.getPromoName())
                                    .promoCode(promo.getPromoCode())
                                    .discount(promo.getDiscount())
                                    .promoDescription(promo.getPromoDescription())
                                    .startDate(promo.getStartDate())
                                    .endDate(promo.getEndDate())
                                    .build()
                    )
                    .build();
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to add course promo");
        }
    }

    @Override
    @CachePut(key = "'getCoursePromoDetail-' + #id")
    @CacheEvict(value = "allCoursePromos", allEntries = true)
    public void updateCoursePromo(UUID id, CoursePromoRequest request) {
        try {
            coursePromoRepository.findById(id)
                    .ifPresentOrElse(coursePromo -> {
                        Course course = courseRepository.findFirstBySlugCourse(request.getSlugCourse())
                                .orElseThrow(() -> new DataNotFoundException("Course not found"));
                        Promo promo = promoRepository.findByPromoCode(request.getPromoCode()).orElseThrow(() -> new DataNotFoundException("Promo not found"));

                        coursePromoRepository.findFirstByCourseAndPromo(course, promo)
                                .ifPresentOrElse(data -> {
                                    throw new ValidationException("Course promo already exist");
                                }, () -> {
                                    coursePromo.setCourse(course);
                                    coursePromo.setPromo(promo);
                                    coursePromoRepository.save(coursePromo);
                                });
                    }, () -> {
                        throw new DataNotFoundException(COURSE_PROMO_NOT_FOUND);
                    });
        } catch (DataNotFoundException | ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to update course promo");
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'getCoursePromoDetail-' + #id"),
            @CacheEvict(value = "allCoursePromos", allEntries = true)
    })
    public void deleteCoursePromo(UUID id) {
        try {
            coursePromoRepository.findById(id)
                    .ifPresentOrElse(coursePromoRepository::delete, () -> {
                        throw new DataNotFoundException(COURSE_PROMO_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to delete course promo");
        }
    }

    @Override
    @Cacheable(key = "'getCoursePromoDetail-' + #id")
    public CoursePromoResponse getCoursePromoDetail(UUID id) {
        try {
            return coursePromoRepository.findById(id)
                    .map(coursePromo -> CoursePromoResponse.builder()
                            .id(coursePromo.getId())
                            .course(
                                    CourseResponse.builder()
                                            .courseName(coursePromo.getCourse().getCourseName())
                                            .instructorName(coursePromo.getCourse().getInstructorName())
                                            .courseLevel(coursePromo.getCourse().getCourseLevel())
                                            .courseType(coursePromo.getCourse().getCourseType())
                                            .price(coursePromo.getCourse().getPrice())
                                            .totalCourseRate(coursePromo.getCourse().getTotalCourseRate())
                                            .courseDuration(coursePromo.getCourse().getCourseDuration())
                                            .totalChapters(coursePromo.getCourse().getTotalChapter())
                                            .slugCourse(coursePromo.getCourse().getSlugCourse())
                                            .pathCourseImage(coursePromo.getCourse().getPathCourseImage())
                                            .targetMarket(coursePromo.getCourse().getTargetMarket())
                                            .category(
                                                    CategoryResponse.builder().
                                                            categoryName(coursePromo.getCourse().getCategory().getCategoryName())
                                                            .slugCategory(coursePromo.getCourse().getCategory().getSlugCategory())
                                                            .pathCategoryImage(coursePromo.getCourse().getCategory().getPathCategoryImage())
                                                            .build())
                                            .build()
                            )
                            .promo(
                                    PromoResponse.builder()
                                            .promoName(coursePromo.getPromo().getPromoName())
                                            .promoCode(coursePromo.getPromo().getPromoCode())
                                            .discount(coursePromo.getPromo().getDiscount())
                                            .promoDescription(coursePromo.getPromo().getPromoDescription())
                                            .startDate(coursePromo.getPromo().getStartDate())
                                            .endDate(coursePromo.getPromo().getEndDate())
                                            .build()
                            )
                            .build())
                    .orElseThrow(() -> new DataNotFoundException(COURSE_PROMO_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get course promo detail");
        }
    }

    @Override
    @Cacheable(value = "allCoursePromos", key = "'getAllCoursePromo-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<CoursePromoResponse> getAllCoursePromo(Pageable pageable) {
        try {
            Page<CoursePromo> coursePromoPage = Optional.of(coursePromoRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(COURSE_PROMO_NOT_FOUND));
            return coursePromoPage.map(coursePromo -> CoursePromoResponse.builder()
                    .id(coursePromo.getId())
                    .course(
                            CourseResponse.builder()
                                    .courseName(coursePromo.getCourse().getCourseName())
                                    .instructorName(coursePromo.getCourse().getInstructorName())
                                    .courseLevel(coursePromo.getCourse().getCourseLevel())
                                    .courseType(coursePromo.getCourse().getCourseType())
                                    .price(coursePromo.getCourse().getPrice())
                                    .totalCourseRate(coursePromo.getCourse().getTotalCourseRate())
                                    .courseDuration(coursePromo.getCourse().getCourseDuration())
                                    .totalChapters(coursePromo.getCourse().getTotalChapter())
                                    .slugCourse(coursePromo.getCourse().getSlugCourse())
                                    .pathCourseImage(coursePromo.getCourse().getPathCourseImage())
                                    .targetMarket(coursePromo.getCourse().getTargetMarket())
                                    .category(
                                            CategoryResponse.builder().
                                                    categoryName(coursePromo.getCourse().getCategory().getCategoryName())
                                                    .slugCategory(coursePromo.getCourse().getCategory().getSlugCategory())
                                                    .pathCategoryImage(coursePromo.getCourse().getCategory().getPathCategoryImage())
                                                    .build())
                                    .build()
                    )
                    .promo(
                            PromoResponse.builder()
                                    .promoName(coursePromo.getPromo().getPromoName())
                                    .promoCode(coursePromo.getPromo().getPromoCode())
                                    .discount(coursePromo.getPromo().getDiscount())
                                    .promoDescription(coursePromo.getPromo().getPromoDescription())
                                    .startDate(coursePromo.getPromo().getStartDate())
                                    .endDate(coursePromo.getPromo().getEndDate())
                                    .build()
                    )
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get all course promo");
        }
    }
}
