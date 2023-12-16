package com.binar.byteacademy.seeders;

import com.binar.byteacademy.common.util.ImageUtil;
import com.binar.byteacademy.entity.Category;
import com.binar.byteacademy.entity.Chapter;
import com.binar.byteacademy.entity.Course;
import com.binar.byteacademy.entity.Material;
import com.binar.byteacademy.enumeration.EnumCourseLevel;
import com.binar.byteacademy.enumeration.EnumCourseType;
import com.binar.byteacademy.enumeration.EnumMaterialType;
import com.binar.byteacademy.enumeration.EnumStatus;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.repository.CategoryRepository;
import com.binar.byteacademy.repository.ChapterRepository;
import com.binar.byteacademy.repository.CourseRepository;
import com.binar.byteacademy.repository.MaterialRepository;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class Seeder implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;
    private final ChapterRepository chapterRepository;
    private final MaterialRepository materialRepository;
    private final ResourceLoader resourceLoader;
    private final ImageUtil imageUtil;

    @Override
    public void run(String... args) {
        seedCategory();
        seedCourse();
        seedChapter();
        seedMaterial();
    }

    private void seedCategory() {
        Resource resource = resourceLoader.getResource("classpath:csv/category.csv");
        try(Reader reader = Files.newBufferedReader(Paths.get(resource.getFile().getAbsolutePath()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            String[] nextRecord;
            List<Category> categories = new ArrayList<>();
            while ((nextRecord = csvReader.readNext()) != null) {
                if (!categoryRepository.existsByCategoryNameOrSlugCategory(nextRecord[0], nextRecord[1])) {
                    Category category = Category.builder()
                            .categoryName(nextRecord[0])
                            .slugCategory(nextRecord[1])
                            .pathCategoryImage(imageUtil.base64UploadImage(nextRecord[2]).join())
                            .build();
                    categories.add(category);
                }
            }
            categoryRepository.saveAll(categories);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void seedCourse() {
        Resource resource = resourceLoader.getResource("classpath:csv/course.csv");
        try(Reader reader = Files.newBufferedReader(Paths.get(resource.getFile().getAbsolutePath()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            String[] nextRecord;
            List<Course> courses = new ArrayList<>();
            while ((nextRecord = csvReader.readNext()) != null) {
                if (!courseRepository.existsBySlugCourse(nextRecord[9])) {
                    Category category = categoryRepository.findBySlugCategory(nextRecord[0])
                            .orElseThrow(() -> new DataNotFoundException("Category not found, cannot add course from csv"));
                    Course course = Course.builder()
                            .courseName(nextRecord[1])
                            .instructorName(nextRecord[2])
                            .price(Double.parseDouble(nextRecord[3]))
                            .totalCourseRate(Double.parseDouble(nextRecord[4]))
                            .totalModules(Integer.parseInt(nextRecord[5]))
                            .courseDuration(Integer.parseInt(nextRecord[6]))
                            .courseDescription(nextRecord[7])
                            .targetMarket(nextRecord[8])
                            .slugCourse(nextRecord[9])
                            .pathCourseImage(imageUtil.base64UploadImage(nextRecord[10]).join())
                            .groupLink(nextRecord[11])
                            .courseType(EnumCourseType.valueOf(nextRecord[12]))
                            .courseLevel(EnumCourseLevel.valueOf(nextRecord[13]))
                            .courseStatus(EnumStatus.valueOf(nextRecord[14]))
                            .category(category)
                            .build();
                    courses.add(course);
                }
            }
            courseRepository.saveAll(courses);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void seedChapter() {
        Resource resource = resourceLoader.getResource("classpath:csv/chapter.csv");
        try(Reader reader = Files.newBufferedReader(Paths.get(resource.getFile().getAbsolutePath()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            String[] nextRecord;
            List<Chapter> chapters = new ArrayList<>();
            while ((nextRecord = csvReader.readNext()) != null) {
                if (!chapterRepository.existsBySlugChapter(nextRecord[0])) {
                    Course course = courseRepository.findFirstBySlugCourse(nextRecord[1])
                            .orElseThrow(() -> new  DataNotFoundException("Course not found, cannot add chapter from csv"));
                    Chapter chapter = Chapter.builder()
                            .noChapter(Integer.parseInt(nextRecord[2]))
                            .title(nextRecord[3])
                            .chapterDuration(Integer.parseInt(nextRecord[4]))
                            .slugChapter(nextRecord[0])
                            .course(course)
                            .build();
                    chapters.add(chapter);
                }
            }
            chapterRepository.saveAll(chapters);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void seedMaterial() {
        Resource resource = resourceLoader.getResource("classpath:csv/material.csv");
        try(Reader reader = Files.newBufferedReader(Paths.get(resource.getFile().getAbsolutePath()));
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()) {
            String[] nextRecord;
            List<Material> materials = new ArrayList<>();
            while ((nextRecord = csvReader.readNext()) != null) {
                if (!materialRepository.existsBySlugMaterial(nextRecord[5])) {
                    Chapter chapter = chapterRepository.findFirstBySlugChapter(nextRecord[0])
                            .orElseThrow(() -> new DataNotFoundException("Chapter not found, cannot add material from csv"));
                    Material material = Material.builder()
                            .materialName(nextRecord[1])
                            .serialNumber(Integer.parseInt(nextRecord[2]))
                            .videoLink(nextRecord[3])
                            .materialDuration(Integer.parseInt(nextRecord[4]))
                            .slugMaterial(nextRecord[5])
                            .materialType(EnumMaterialType.valueOf(nextRecord[6]))
                            .chapter(chapter)
                            .build();
                    materials.add(material);
                }
            }
            materialRepository.saveAll(materials);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
