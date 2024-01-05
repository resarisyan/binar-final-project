package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.CheckDataUtil;
import com.binar.byteacademy.common.util.SlugUtil;
import com.binar.byteacademy.dto.request.CreateChapterRequest;
import com.binar.byteacademy.dto.request.UpdateChapterRequest;
import com.binar.byteacademy.dto.response.ChapterResponse;
import com.binar.byteacademy.entity.Chapter;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.ChapterRepository;
import com.binar.byteacademy.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.CHAPTER_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.COURSE_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.TableName.CHAPTER_TABLE;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "chapters")
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;
    private final SlugUtil slugUtil;
    private final CheckDataUtil checkDataUtil;

    @Override
    @CacheEvict(value = "allChapters", allEntries = true, condition = "#result != null")
    public ChapterResponse addChapter(CreateChapterRequest request) {
        try {
            String slug = Optional.ofNullable(request.getSlugChapter())
                    .orElse(slugUtil.toSlug(CHAPTER_TABLE, "slug_chapter", request.getTitle() + "-" + request.getSlugCourse()));
            return courseRepository.findFirstBySlugCourse(request.getSlugCourse())
                    .map(course -> {
                        Chapter chapter = Chapter.builder()
                                .noChapter(request.getNoChapter())
                                .title(request.getTitle())
                                .chapterDuration(request.getChapterDuration())
                                .course(course)
                                .slugChapter(slug)
                                .build();
                        Chapter savedChapter = chapterRepository.save(chapter);
                        course.setTotalChapter(course.getTotalChapter() + 1);
                        courseRepository.save(course);
                        return ChapterResponse.builder()
                                .slugChapter(savedChapter.getSlugChapter())
                                .noChapter(savedChapter.getNoChapter())
                                .title(savedChapter.getTitle())
                                .chapterDuration(savedChapter.getChapterDuration())
                                .build();
                    }).orElseThrow(() -> new DataNotFoundException(COURSE_NOT_FOUND));
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "allChapters", key = "'getListChapterBySlugCourse-' + #slugCourse", unless = "#result == null")
    public List<ChapterResponse> getListChapterBySlugCourse(String slugCourse) {
        try {
            List<Chapter> chapterList = chapterRepository.findAllByCourse_SlugCourse(slugCourse).orElseThrow(() -> new DataNotFoundException(CHAPTER_NOT_FOUND));
            return chapterList.stream().map(chapter -> ChapterResponse.builder()
                    .slugChapter(chapter.getSlugChapter())
                    .noChapter(chapter.getNoChapter())
                    .title(chapter.getTitle())
                    .chapterDuration(chapter.getChapterDuration())
                    .build()).toList();
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get list chapter");
        }
    }

    @Override
    @CachePut(key = "'getChapterDetail-' + #slugChapter", unless = "#result == null")
    @CacheEvict(value = {"allChapters", "materials", "allMaterials"}, allEntries = true, condition = "#result != null")
    public ChapterResponse updateChapter(String slugChapter, UpdateChapterRequest request) {
        try {
            return chapterRepository.findFirstBySlugChapter(slugChapter)
                    .map(chapter -> courseRepository.findFirstBySlugCourse(request.getSlugCourse())
                            .map(course -> {
                                checkDataUtil.checkDataField(CHAPTER_TABLE, "slug_chapter", request.getSlugChapter(), "chapter_id", chapter.getId());
                                chapter.setSlugChapter(request.getSlugChapter());
                                chapter.setNoChapter(request.getNoChapter());
                                chapter.setTitle(request.getTitle());
                                chapter.setChapterDuration(request.getChapterDuration());
                                chapter.setCourse(course);
                                chapterRepository.save(chapter);
                                return ChapterResponse.builder()
                                        .slugChapter(chapter.getSlugChapter())
                                        .noChapter(chapter.getNoChapter())
                                        .title(chapter.getTitle())
                                        .chapterDuration(chapter.getChapterDuration())
                                        .build();
                            })
                            .orElseThrow(() -> new DataNotFoundException(COURSE_NOT_FOUND)))
                    .orElseThrow(() -> new DataNotFoundException(CHAPTER_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to update chapter");
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'getChapterDetail-' + #slugChapter"),
            @CacheEvict(value = {"allChapters", "materials", "allMaterials"}, allEntries = true)
    })
    public void deleteChapter(String slugChapter) {
        try {
            chapterRepository.findFirstBySlugChapter(slugChapter)
                    .ifPresentOrElse(chapter -> courseRepository.findFirstBySlugCourse(chapter.getCourse().getSlugCourse())
                            .ifPresentOrElse(course -> {
                                course.setTotalChapter(course.getTotalChapter() - 1);
                                courseRepository.save(course);
                                chapterRepository.delete(chapter);
                            }, () -> {
                                throw new DataNotFoundException(COURSE_NOT_FOUND);
                            }), () -> {
                        throw new DataNotFoundException(CHAPTER_NOT_FOUND);
                    });
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to delete chapter");
        }
    }

    @Override
    @Cacheable(value = "allChapters", key = "'getAllChapter-' + #pageable.pageNumber + '-' + #pageable.pageSize", unless = "#result == null")
    public Page<ChapterResponse> getAllChapter(Pageable pageable) {
        try {
            Page<Chapter> chapterPage = Optional.of(chapterRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(CHAPTER_NOT_FOUND));
            return chapterPage.map(chapter -> ChapterResponse.builder()
                    .slugChapter(chapter.getSlugChapter())
                    .noChapter(chapter.getNoChapter())
                    .title(chapter.getTitle())
                    .chapterDuration(chapter.getChapterDuration())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get all chapter");
        }
    }

    @Override
    @Cacheable(key = "'getChapterDetail-' + #slugChapter", unless = "#result == null")
    public ChapterResponse getChapterDetail(String slugChapter) {
        try {
            Chapter chapter = chapterRepository.findFirstBySlugChapter(slugChapter)
                    .orElseThrow(() -> new DataNotFoundException(CHAPTER_NOT_FOUND));
            return ChapterResponse.builder()
                    .slugChapter(chapter.getSlugChapter())
                    .noChapter(chapter.getNoChapter())
                    .title(chapter.getTitle())
                    .chapterDuration(chapter.getChapterDuration())
                    .build();
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get chapter detail");
        }
    }
}
