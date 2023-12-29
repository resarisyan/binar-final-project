package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.CheckDataUtil;
import com.binar.byteacademy.common.util.ImageUtil;
import com.binar.byteacademy.common.util.JwtUtil;
import com.binar.byteacademy.common.util.SlugUtil;
import com.binar.byteacademy.dto.request.CreateCourseRequest;
import com.binar.byteacademy.dto.request.UpdateCourseRequest;
import com.binar.byteacademy.dto.response.*;
import com.binar.byteacademy.entity.*;
import com.binar.byteacademy.enumeration.*;
import com.binar.byteacademy.exception.DataConflictException;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.CategoryRepository;
import com.binar.byteacademy.repository.CourseRepository;
import com.binar.byteacademy.repository.PurchaseRepository;
import com.binar.byteacademy.repository.specification.CourseFilterSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.CATEGORY_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.COURSE_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.TableName.COURSE_TABLE;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "courses")
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final PurchaseRepository purchaseRepository;
    private final JwtUtil jwtUtil;
    private final ImageUtil imageUtil;
    private final CheckDataUtil checkDataUtil;
    private final SlugUtil slugUtil;

    @Override
    @Async("asyncExecutor")
    @CacheEvict(value = "allCourses", allEntries = true)
    public CompletableFuture<CourseResponse> addCourse(CreateCourseRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String slug = Optional.ofNullable(request.getSlugCourse()).orElseGet(() -> slugUtil.toSlug(COURSE_TABLE, "slug_course", request.getCourseName()));
                return categoryRepository.findBySlugCategory(request.getSlugCategory()).map(category -> {
                    String pathCourseImage = imageUtil.base64UploadImage(request.getPathCourseImage()).join();
                    Course course = Course.builder()
                            .courseName(request.getCourseName()).
                            instructorName(request.getInstructorName())
                            .courseLevel(request.getCourseLevel())
                            .courseType(request.getCourseType())
                            .courseStatus(request.getCourseStatus())
                            .totalCourseRate(0.0)
                            .price(request.getCourseType() == EnumCourseType.FREE ? 0 : request.getPrice())
                            .courseDuration(request.getCourseDuration())
                            .slugCourse(slug)
                            .pathCourseImage(pathCourseImage)
                            .category(category)
                            .groupLink(request.getGroupLink())
                            .targetMarket(request.getTargetMarket())
                            .totalChapter(0)
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
                            .totalChapters(course.getTotalChapter())
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
                }).orElseThrow(() -> new DataNotFoundException(CATEGORY_NOT_FOUND));
            } catch (DataNotFoundException | DataConflictException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceBusinessException("Failed to create course");
            }
        });
    }


    @Override
    @Caching(put = {
            @CachePut(key = "'getCustomerCourseDetail-' + #slugCourse"),
            @CachePut(key = "'getAdminCourseDetail-' + #slugCourse")
    })
    @CacheEvict(value = {
            "allCourses", "chapters", "allChapters", "materials", "allMaterials",
            "allPurchases", "discussions", "allDiscussion",
            "allCoursePromos", "coursePromos",
            "allPromos", "promos",
    }, allEntries = true)    public CompletableFuture<Void> updateCourse(String slugCourse, UpdateCourseRequest request) {
        return CompletableFuture.runAsync(() -> {
            try {
                categoryRepository.findBySlugCategory(request.getSlugCategory())
                        .ifPresentOrElse(category -> courseRepository.findFirstBySlugCourse(slugCourse)
                                .ifPresentOrElse(course -> {
                                    checkDataUtil.checkDataField(COURSE_TABLE, "course_name", request.getCourseName(), "course_id", course.getId());
                                    checkDataUtil.checkDataField(COURSE_TABLE, "slug_course", request.getSlugCourse(), "course_id", course.getId());
                                    course.setCourseName(request.getCourseName());
                                    course.setInstructorName(request.getInstructorName());
                                    course.setCourseLevel(request.getCourseLevel());
                                    course.setCourseType(request.getCourseType());
                                    course.setCourseStatus(request.getCourseStatus());
                                    course.setPrice(request.getCourseType() == EnumCourseType.FREE ? 0 : request.getPrice());
                                    course.setCourseDuration(request.getCourseDuration());
                                    course.setSlugCourse(request.getSlugCourse());
                                    course.setGroupLink(request.getGroupLink());
                                    course.setTargetMarket(request.getTargetMarket());
                                    course.setCourseDescription(request.getCourseDescription());
                                    Optional.ofNullable(request.getPathCourseImage())
                                            .ifPresent(image -> {
                                                imageUtil.deleteImage(course.getPathCourseImage());
                                                String pathCourseImage = imageUtil.base64UploadImage(request.getPathCourseImage()).join();
                                                course.setPathCourseImage(pathCourseImage);
                                            });
                                    course.setCategory(category);
                                    courseRepository.save(course);
                                }, () -> {
                                    throw new DataNotFoundException(COURSE_NOT_FOUND);
                                }), () -> {
                            throw new DataNotFoundException(CATEGORY_NOT_FOUND);
                        });
            } catch (DataNotFoundException | DataConflictException e) {
                throw e;
            } catch (Exception e) {
                throw new ServiceBusinessException("Failed to update course");
            }
        });
    }

    @Override
    @Cacheable(value = "allCourses", key = "'getListCourse'")
    public List<CourseResponse> getListCourse() {
        try {
            List<Course> courseList = courseRepository.findAll();
            return courseList.stream().map(course ->
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
                            .category(CategoryResponse.builder()
                                    .categoryName(course.getCategory().getCategoryName())
                                    .slugCategory(course.getCategory().getSlugCategory())
                                    .pathCategoryImage(course.getCategory().getPathCategoryImage())
                                    .build())
                            .build()
            ).toList();
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get list course");
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'getAdminCourseDetail-' + #slugCourse"),
            @CacheEvict(value = {
                    "allCourses", "chapters", "allChapters", "materials", "allMaterials",
                    "allPurchases", "discussions", "allDiscussion",
                    "allCoursePromos", "coursePromos",
                    "allPromos", "promos", "dashboard"
            }, allEntries = true)
    })
    public void deleteCourse(String slugCourse) {
        try {
            courseRepository.findFirstBySlugCourse(slugCourse).ifPresentOrElse(courseRepository::delete, () -> {
                throw new DataNotFoundException("Course not found for slug " + slugCourse);
            });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to delete course");
        }
    }

    @Override
    public Page<Course> getAllCourseByCriteria(List<String> categoryNames, List<EnumCourseLevel> courseLevels, List<EnumCourseType> courseTypes, List<EnumStatus> courseStatuses, List<EnumFilterCoursesBy> filterCoursesBy, String keyword, String username, Pageable pageable) throws DataNotFoundException {
        categoryNames = Optional.ofNullable(categoryNames).map(val -> val.stream().map(String::toLowerCase).toList()).orElse(Collections.emptyList());
        courseLevels = Optional.ofNullable(courseLevels).orElse(Collections.emptyList());
        courseTypes = Optional.ofNullable(courseTypes).orElse(Collections.emptyList());
        courseStatuses = Optional.ofNullable(courseStatuses).orElse(Collections.emptyList());
        filterCoursesBy = Optional.ofNullable(filterCoursesBy).orElse(Collections.emptyList());
        keyword = Optional.ofNullable(keyword).map(String::toLowerCase).orElse(null);
        return Optional.of(courseRepository.findAll(CourseFilterSpecification.filterCourses(categoryNames, courseLevels, courseTypes, courseStatuses, filterCoursesBy, keyword, username), pageable)).filter(Page::hasContent).orElseThrow(() -> new DataNotFoundException("Course Not  Found"));
    }

    @Override
    @Cacheable(value = "allCourses", key = "'getCourseListForWeb-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<CourseResponse> getCourseListForWeb(List<String> categoryNames, List<EnumCourseLevel> courseLevels, List<EnumCourseType> courseTypes, List<EnumStatus> courseStatuses, List<EnumFilterCoursesBy> filterCoursesBy, String keyword, Pageable pageable) {
        try {
            Page<Course> coursePage = getAllCourseByCriteria(categoryNames, courseLevels, courseTypes, courseStatuses, filterCoursesBy, keyword, null, pageable);
            return coursePage.map(course -> CourseResponse.builder().courseName(course.getCourseName()).instructorName(course.getInstructorName()).price(course.getPrice()).totalCourseRate(course.getTotalCourseRate()).totalChapters(course.getTotalChapter()).courseDuration(course.getCourseDuration()).slugCourse(course.getSlugCourse()).pathCourseImage(course.getPathCourseImage()).courseType(course.getCourseType()).courseLevel(course.getCourseLevel()).category(CategoryResponse.builder().categoryName(course.getCategory().getCategoryName()).pathCategoryImage(course.getCategory().getPathCategoryImage()).build()).build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed get course with filter");
        }
    }

    @Override
    @Cacheable(value = "allCourses", key = "'getCourseListForAdmin-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<AdminCourseResponse> getCourseListForAdmin(List<String> categoryNames, List<EnumCourseLevel> courseLevels, List<EnumCourseType> courseTypes, List<EnumStatus> courseStatuses, List<EnumFilterCoursesBy> filterCoursesBy, String keyword, Pageable pageable) {
        try {
            Page<Course> coursePage = getAllCourseByCriteria(categoryNames, courseLevels, courseTypes, courseStatuses, filterCoursesBy, keyword, null, pageable);
            return coursePage.map(course -> AdminCourseResponse.builder().slugCourse(course.getSlugCourse()).courseName(course.getCourseName()).price(course.getPrice()).courseType(course.getCourseType()).courseLevel(course.getCourseLevel()).courseStatus(course.getCourseStatus()).category(CategoryResponse.builder().categoryName(course.getCategory().getCategoryName()).pathCategoryImage(course.getCategory().getPathCategoryImage()).build()).build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed get course with filter");
        }
    }

    @Override
    @Cacheable(value = "allCourses", key = "'getMyCourseList-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<MyCourseResponse> getMyCourseList(List<String> categoryNames, List<EnumCourseLevel> courseLevels, List<EnumCourseType> courseTypes, List<EnumStatus> courseStatuses, List<EnumFilterCoursesBy> filterCoursesBy, String keyword, Pageable pageable) {
        try {
            User user = jwtUtil.getUser();
            Page<Course> coursePage = getAllCourseByCriteria(categoryNames, courseLevels, courseTypes, courseStatuses, filterCoursesBy, keyword, user.getUsername(), pageable);
            return coursePage.map(course -> MyCourseResponse.builder().courseName(course.getCourseName()).instructorName(course.getInstructorName()).totalCourseRate(course.getTotalCourseRate()).totalModules(course.getTotalChapter()).courseDuration(course.getCourseDuration()).slugCourse(course.getSlugCourse()).courseLevel(course.getCourseLevel()).pathCourseImage(course.getPathCourseImage()).category(CategoryResponse.builder().categoryName(course.getCategory().getCategoryName()).pathCategoryImage(course.getCategory().getPathCategoryImage()).build()).userProgressResponse(UserProgressResponse.builder().completionDate(course.getUserProgresses().get(0).getCompletionDate()).coursePercentage(course.getUserProgresses().get(0).getCoursePercentage()).isCompleted(course.getUserProgresses().get(0).getIsCompleted()).build()).build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed get my course with filter");
        }
    }

    @Override
    @Cacheable(key = "'getAdminCourseDetail-' + #slugCourse")
    public AdminCourseDetailResponse getAdminCourseDetail(String slugCourse) {
        try {
            return courseRepository.findFirstBySlugCourse(slugCourse)
                    .map(
                            course -> AdminCourseDetailResponse.builder()
                                    .courseName(course.getCourseName())
                                    .instructorName(course.getInstructorName())
                                    .totalCourseRate(course.getTotalCourseRate())
                                    .totalChapter(course.getTotalChapter())
                                    .courseDuration(course.getCourseDuration())
                                    .slugCourse(course.getSlugCourse())
                                    .courseType(course.getCourseType())
                                    .pathCourseImage(course.getPathCourseImage())
                                    .courseDescription(course.getCourseDescription())
                                    .price(course.getPrice())
                                    .targetMarket(course.getTargetMarket())
                                    .courseLevel(course.getCourseLevel())
                                    .groupLink(course.getGroupLink())
                                    .slugCategory(course.getCategory().getSlugCategory())
                                    .build()
                    ).orElseThrow(() -> new DataNotFoundException(COURSE_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public CustomerCourseDetailResponse getCustomerCourseDetail(String slugCourse) {
        try {
            Course course = courseRepository.findFirstBySlugCourse(slugCourse)
                    .orElseThrow(() -> new DataNotFoundException(COURSE_NOT_FOUND));
            User user;
            AtomicBoolean isMaterialLocked = new AtomicBoolean(true);
            AtomicBoolean isMaterialCompleted = new AtomicBoolean(false);

            if (jwtUtil.getTokenFromRequest() != null) {
                user = jwtUtil.getUser();
                purchaseRepository.findByUserAndCourseAndPurchaseStatus(user, course, EnumPurchaseStatus.PAID)
                        .ifPresent(purchase -> {
                            isMaterialLocked.set(false);
                            isMaterialCompleted.set(true);
                        });
            } else {
                user = null;
            }

            List<ChapterCourseDetailResponse> sortedChapters = course.getChapters().stream()
                    .sorted(Comparator.comparing(Chapter::getNoChapter))
                    .map(chapter -> {
                        List<MaterialCourseDetailResponse> sortedMaterials = chapter.getMaterials().stream()
                                .sorted(Comparator.comparing(Material::getSerialNumber))
                                .map(material -> {
                                    if (isMaterialCompleted.get()) {
                                        return MaterialCourseDetailResponse.builder()
                                                .materialName(material.getMaterialName())
                                                .materialType(material.getMaterialType())
                                                .materialDuration(material.getMaterialDuration())
                                                .slugMaterial(material.getSlugMaterial())
                                                .isLocked(isMaterialLocked.get())
                                                .isCompleted(
                                                        material.getMaterialActivities().stream()
                                                                .anyMatch(materialActivities -> materialActivities.getUser().equals(user))
                                                )
                                                .build();
                                    } else {
                                        return MaterialCourseDetailResponse.builder()
                                                .materialName(material.getMaterialName())
                                                .materialType(material.getMaterialType())
                                                .materialDuration(material.getMaterialDuration())
                                                .slugMaterial(EnumMaterialType.FREE.equals(material.getMaterialType()) ? material.getSlugMaterial() : null)
                                                .isLocked(!EnumMaterialType.FREE.equals(material.getMaterialType()))
                                                .isCompleted(isMaterialCompleted.get())
                                                .build();
                                    }
                                })
                                .toList();

                        return ChapterCourseDetailResponse.builder()
                                .title(chapter.getTitle())
                                .chapterDuration(chapter.getChapterDuration())
                                .noChapter(chapter.getNoChapter())
                                .materials(sortedMaterials)
                                .build();
                    })
                    .toList();

            return CustomerCourseDetailResponse.builder()
                    .courseName(course.getCourseName())
                    .instructorName(course.getInstructorName())
                    .totalCourseRate(course.getTotalCourseRate())
                    .totalChapter(course.getTotalChapter())
                    .courseDuration(course.getCourseDuration())
                    .groupLink(course.getGroupLink())
                    .slugCourse(course.getSlugCourse())
                    .courseType(course.getCourseType())
                    .courseDescription(course.getCourseDescription())
                    .targetMarket(course.getTargetMarket())
                    .pathCourseImage(course.getPathCourseImage())
                    .price(course.getPrice())
                    .courseLevel(course.getCourseLevel())
                    .category(CategoryResponse.builder()
                            .categoryName(course.getCategory().getCategoryName())
                            .pathCategoryImage(course.getCategory().getPathCategoryImage())
                            .build())
                    .chapters(sortedChapters)
                    .build();

        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }
}