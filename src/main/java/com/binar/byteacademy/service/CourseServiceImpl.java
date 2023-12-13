package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.CheckDataUtil;
import com.binar.byteacademy.common.util.ImageUtil;
import com.binar.byteacademy.common.util.JwtUtil;
import com.binar.byteacademy.common.util.SlugUtil;
import com.binar.byteacademy.dto.request.CreateCourseRequest;
import com.binar.byteacademy.dto.request.UpdateCourseRequest;
import com.binar.byteacademy.dto.response.*;
import com.binar.byteacademy.entity.Category;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumFilterCoursesBy;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CategoryRepository;
import com.binar.byteacademy.repository.CourseRepository;
import com.binar.byteacademy.repository.specification.CourseFilterSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.CATEGORY_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.COURSE_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.TableName.COURSE_TABLE;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final JwtUtil jwtUtil;
    private final ImageUtil imageUtil;
    private final CheckDataUtil checkDataUtil;
    private final SlugUtil slugUtil;

    @Override
    public CompletableFuture<CourseResponse> addCourse(CreateCourseRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String pathCourseImage = imageUtil.base64UploadImage(request.getPathCourseImage()).join();
                String slug = Optional.ofNullable(request.getSlugCourse()).orElseGet(() -> slugUtil.toSlug(COURSE_TABLE, "slug_course", request.getCourseName()));

                return categoryRepository.findBySlugCategory(request.getSlugCategory()).map(category -> {
                    Course course = Course.builder()
                            .courseName(request.getCourseName()).
                            instructorName(request.getInstructorName())
                            .courseLevel(request.getCourseLevel())
                            .courseType(request.getCourseType())
                            .courseStatus(request.getCourseStatus())
                            .totalCourseRate(0.0)
                            .price(request.getPrice())
                            .courseDuration(request.getCourseDuration())
                            .slugCourse(slug)
                            .pathCourseImage(pathCourseImage)
                            .category(category)
                            .groupLink(request.getGroupLink())
                            .targetMarket(request.getTargetMarket())
                            .totalModules(0)
                            .courseDescription(request.getCourseDescription())
                            .build();

                    courseRepository.save(course);

                    return CourseResponse.builder()
                            .courseName(course.getCourseName())
                            .instructorName(course.getInstructorName())
                            .courseLevel(course.getCourseLevel())
                            .courseType(course.getCourseType())
                            .price(course.getPrice())
                            .totalCourseRate(course.getTotalCourseRate())
                            .courseDuration(course.getCourseDuration())
                            .totalModules(course.getTotalModules())
                            .slugCourse(course.getSlugCourse())
                            .pathCourseImage(course.getPathCourseImage())
                            .targetMarket(course.getTargetMarket())
                            .category(
                                    CategoryResponse.builder().
                                            categoryName(category.getCategoryName())
                                            .slugCategory(category.getSlugCategory())
                                            .pathCategoryImage(category.getPathCategoryImage())
                                            .build())
                            .build();
                }).orElseThrow(() -> new DataNotFoundException("Category not found"));
            } catch (Exception e) {
                throw new ServiceBusinessException(e.getMessage());
            }
        });
    }


    @Override
    public CompletableFuture<Void> updateCourse(String slugCourse, UpdateCourseRequest request) {
        return CompletableFuture.runAsync(() -> {
            try {
                courseRepository.findFirstBySlugCourse(slugCourse).map(course -> {
                    checkDataUtil.checkDataField(COURSE_TABLE, "course_name", request.getCourseName(), "course_id", course.getId());
                    course.setCourseName(request.getCourseName());
                    course.setInstructorName(request.getInstructorName());
                    course.setCourseLevel(request.getCourseLevel());
                    course.setCourseType(request.getCourseType());
                    course.setPrice(request.getPrice());
                    course.setCourseDuration(request.getCourseDuration());
                    course.setTargetMarket(request.getTargetMarket());
                    course.setGroupLink(request.getGroupLink());
                    course.setCourseStatus(request.getCourseStatus());
                    course.setCourseDescription(request.getCourseDescription());

                    Optional.ofNullable(request.getSlugCourse()).ifPresent(slug -> {
                        checkDataUtil.checkDataField(COURSE_TABLE, "slug_course", slug, "course_id", course.getId());
                        course.setSlugCourse(slug);
                    });

                    Optional.ofNullable(request.getPathCourseImage()).ifPresent(image -> {
                        imageUtil.deleteImage(course.getPathCourseImage());
                        String pathCourseImage = imageUtil.base64UploadImage(image).join();
                        course.setPathCourseImage(pathCourseImage);
                    });

                    Optional.ofNullable(request.getSlugCategory()).ifPresent(slugCategory -> {
                        Category category = categoryRepository.findBySlugCategory(slugCategory).orElseThrow(() -> new DataNotFoundException(CATEGORY_NOT_FOUND));
                        course.setCategory(category);
                    });

                    return course;
                }).ifPresentOrElse(courseRepository::save, () -> {
                    throw new DataNotFoundException(COURSE_NOT_FOUND);
                });
            } catch (Exception e) {
                throw new ServiceBusinessException("Failed to update course");
            }
        });
    }

    @Override
    public Page<CourseResponse> getAllCourse(Pageable pageable) {
        try {
            return Optional.of(courseRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .map(coursePage -> coursePage
                            .map(course -> CourseResponse.builder()
                                    .courseName(course.getCourseName())
                                    .instructorName(course.getInstructorName())
                                    .courseLevel(course.getCourseLevel())
                                    .courseType(course.getCourseType())
                                    .price(course.getPrice())
                                    .totalCourseRate(course.getTotalCourseRate())
                                    .courseDuration(course.getCourseDuration())
                                    .totalModules(course.getTotalModules())
                                    .slugCourse(course.getSlugCourse())
                                    .pathCourseImage(course.getPathCourseImage())
                                    .category(CategoryResponse.builder()
                                            .categoryName(course.getCategory().getCategoryName())
                                            .slugCategory(course.getCategory().getSlugCategory())
                                            .pathCategoryImage(course.getCategory().getPathCategoryImage())
                                            .build()).build()))
                    .orElseThrow(() -> new DataNotFoundException(COURSE_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get list course");
        }
    }

    @Transactional
    @Override
    public void deleteCourse(String slugCourse) {
        try {
            courseRepository.findFirstBySlugCourse(slugCourse).ifPresentOrElse(courseRepository::delete, () -> {
                throw new DataNotFoundException(COURSE_NOT_FOUND);
            });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to delete course");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Course> getAllCourseByCriteria(List<String> categoryNames, List<EnumCourseLevel> courseLevels, List<EnumCourseType> courseTypes, List<EnumStatus> courseStatuses, List<EnumFilterCoursesBy> filterCoursesBy, String keyword, String username, Pageable pageable) throws DataNotFoundException {
        categoryNames = Optional.ofNullable(categoryNames).map(val -> val.stream().map(String::toLowerCase).collect(Collectors.toList())).orElse(Collections.emptyList());
        courseLevels = Optional.ofNullable(courseLevels).orElse(Collections.emptyList());
        courseTypes = Optional.ofNullable(courseTypes).orElse(Collections.emptyList());
        courseStatuses = Optional.ofNullable(courseStatuses).orElse(Collections.emptyList());
        filterCoursesBy = Optional.ofNullable(filterCoursesBy).orElse(Collections.emptyList());
        return Optional.of(courseRepository.findAll(CourseFilterSpecification.filterCourses(categoryNames, courseLevels, courseTypes, courseStatuses, filterCoursesBy, keyword, username), pageable)).filter(Page::hasContent).orElseThrow(() -> new DataNotFoundException("Course Not  Found"));
    }

    @Override
    public Page<SearchCourseResponse> getCourseListForWeb(List<String> categoryNames, List<EnumCourseLevel> courseLevels, List<EnumCourseType> courseTypes, List<EnumStatus> courseStatuses, List<EnumFilterCoursesBy> filterCoursesBy, String keyword, Pageable pageable) {
        try {
            Page<Course> coursePage = getAllCourseByCriteria(categoryNames, courseLevels, courseTypes, courseStatuses, filterCoursesBy, keyword, null, pageable);
            return coursePage.map(course -> SearchCourseResponse.builder().courseName(course.getCourseName()).categoryName(course.getCategory().getCategoryName()).instructorName(course.getInstructorName()).pathImage(course.getPathCourseImage()).price(course.getPrice()).courseType(course.getCourseType()).courseLevel(course.getCourseLevel()).totalCourseRate(course.getTotalCourseRate()).totalModules(course.getTotalModules()).courseDuration(course.getCourseDuration()).build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed get course with filter");
        }
    }

    @Override
    public Page<AdminCourseResponse> getCourseListForAdmin(List<String> categoryNames, List<EnumCourseLevel> courseLevels, List<EnumCourseType> courseTypes, List<EnumStatus> courseStatuses, List<EnumFilterCoursesBy> filterCoursesBy, String keyword, Pageable pageable) {
        try {
            Page<Course> coursePage = getAllCourseByCriteria(categoryNames, courseLevels, courseTypes, courseStatuses, filterCoursesBy, keyword, null, pageable);
            return coursePage.map(course -> AdminCourseResponse.builder().slugCourse(course.getSlugCourse()).courseName(course.getCourseName()).categoryName(course.getCategory().getCategoryName()).price(course.getPrice()).courseType(course.getCourseType()).courseLevel(course.getCourseLevel()).courseStatus(course.getCourseStatus()).build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed get course with filter");
        }
    }

    @Override
    public Page<MyCourseResponse> getMyCourseList(List<String> categoryNames, List<EnumCourseLevel> courseLevels, List<EnumCourseType> courseTypes, List<EnumStatus> courseStatuses, List<EnumFilterCoursesBy> filterCoursesBy, String keyword, Pageable pageable) {
        try {
            User user = jwtUtil.getUser();
            Page<Course> coursePage = getAllCourseByCriteria(categoryNames, courseLevels, courseTypes, courseStatuses, filterCoursesBy, keyword, user.getUsername(), pageable);
            return coursePage.map(course -> MyCourseResponse.builder().courseName(course.getCourseName()).categoryName(course.getCategory().getCategoryName()).courseLevel(course.getCourseLevel()).instructorName(course.getInstructorName()).pathImage(course.getPathCourseImage()).courseLevel(course.getCourseLevel()).totalCourseRate(course.getTotalCourseRate()).totalModules(course.getTotalModules()).courseDuration(course.getCourseDuration()).coursePercentage(course.getUserProgresses().get(0).getCoursePercentage()).build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed get my course with filter");
        }
    }

    @Override
    public CourseDetailResponse getCourseDetail(String courseSlug) {
        try {
            Course course = courseRepository.findFirstBySlugCourse(courseSlug).orElseThrow(() -> new DataNotFoundException("Course not found"));
            return CourseDetailResponse.builder().courseName(course.getCourseName()).instructorName(course.getInstructorName()).totalCourseRate(course.getTotalCourseRate()).totalModules(course.getTotalModules()).courseDuration(course.getCourseDuration()).groupLink(course.getGroupLink()).slugCourse(course.getSlugCourse()).courseType(course.getCourseType()).pathCourseImage(course.getPathCourseImage()).courseLevel(course.getCourseLevel()).category(CategoryResponse.builder().categoryName(course.getCategory().getCategoryName()).pathCategoryImage(course.getCategory().getPathCategoryImage()).build()).chapters(course.getChapters().stream().map(chapter -> ChapterCourseDetailResponse.builder().title(chapter.getTitle()).chapterDuration(chapter.getChapterDuration()).noChapter(chapter.getNoChapter()).materials(chapter.getMaterials().stream().map(material -> MaterialCourseDetailResponse.builder().materialType(material.getMaterialType()).materialDuration(material.getMaterialDuration()).slugMaterial(material.getSlugMaterial()).build()).collect(Collectors.toList())).build()).collect(Collectors.toList())).build();
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get course detail");
        }
    }
}