package com.binar.byteacademy.service;

import com.binar.byteacademy.dto.request.ChapterRequest;
import com.binar.byteacademy.dto.response.ChapterResponse;
import com.binar.byteacademy.entity.Chapter;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.ChapterRepository;
import com.binar.byteacademy.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.CHAPTER_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.COURSE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;

    @Override
    public ChapterResponse addChapter(ChapterRequest request) {
        try{
            return courseRepository.findFirstBySlugCourse(request.getSlugCourse())
                    .map(course -> {
                        Chapter chapter = Chapter.builder()
                                .noChapter(request.getNoChapter())
                                .title(request.getTitle())
                                .chapterDuration(request.getChapterDuration())
                                .course(course)
                                .build();
                        Chapter savedChapter = chapterRepository.save(chapter);
                        return ChapterResponse.builder()
                                .id(savedChapter.getId())
                                .noChapter(savedChapter.getNoChapter())
                                .title(savedChapter.getTitle())
                                .chapterDuration(savedChapter.getChapterDuration())
                                .build();
                    }).orElseThrow(() -> new DataNotFoundException(COURSE_NOT_FOUND));
        } catch (Exception e){
            throw new ServiceBusinessException("Failed to create chapter");
        }
    }

    @Override
        public void updateChapter(UUID id, ChapterRequest request) {
        try{
            chapterRepository.findById(id)
                    .ifPresentOrElse(chapter -> courseRepository.findFirstBySlugCourse(request.getSlugCourse())
                            .ifPresentOrElse(course -> {
                                chapter.setNoChapter(request.getNoChapter());
                                chapter.setTitle(request.getTitle());
                                chapter.setChapterDuration(request.getChapterDuration());
                                chapter.setCourse(course);
                                chapterRepository.save(chapter);
                            }, () -> {
                                throw new DataNotFoundException(COURSE_NOT_FOUND);
                            }), () -> {
                        throw new DataNotFoundException(CHAPTER_NOT_FOUND);
                    });
        } catch (DataNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new ServiceBusinessException("Failed to update chapter");
        }
    }

    @Override
    public void deleteChapter(UUID id) {
        try{
            chapterRepository.findById(id)
                    .ifPresentOrElse(chapterRepository::delete, () -> {
                        throw new DataNotFoundException(CHAPTER_NOT_FOUND);
                    });
        } catch (DataNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new ServiceBusinessException("Failed to delete chapter");
        }
    }

    @Override
    public Page<ChapterResponse> getAllChapter(Pageable pageable) {
        try{
            Page<Chapter> chapterPage = Optional.of(chapterRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(CHAPTER_NOT_FOUND));
            return chapterPage.map(chapter -> ChapterResponse.builder()
                    .id(chapter.getId())
                    .noChapter(chapter.getNoChapter())
                    .title(chapter.getTitle())
                    .chapterDuration(chapter.getChapterDuration())
                    .build());
        } catch (DataNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new ServiceBusinessException("Failed to get all chapter");
        }
    }

    @Override
    public ChapterResponse getChapterDetail(UUID id) {
        try{
            Chapter chapter = chapterRepository.findById(id)
                    .orElseThrow(() -> new DataNotFoundException(CHAPTER_NOT_FOUND));
            return ChapterResponse.builder()
                    .id(chapter.getId())
                    .noChapter(chapter.getNoChapter())
                    .title(chapter.getTitle())
                    .chapterDuration(chapter.getChapterDuration())
                    .build();
        } catch (DataNotFoundException e){
            throw e;
        } catch (Exception e){
            throw new ServiceBusinessException("Failed to get chapter detail");
        }
    }
}
