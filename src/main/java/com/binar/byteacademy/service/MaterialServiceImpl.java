package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.CheckDataUtil;
import com.binar.byteacademy.common.util.SlugUtil;
import com.binar.byteacademy.dto.request.MaterialRequest;
import com.binar.byteacademy.dto.response.MaterialResponse;
import com.binar.byteacademy.entity.Material;
import com.binar.byteacademy.entity.MaterialActivity;
import com.binar.byteacademy.entity.User;
import com.binar.byteacademy.entity.compositekey.MaterialActivityKey;
import com.binar.byteacademy.enumeration.EnumMaterialType;
import com.binar.byteacademy.enumeration.EnumPurchaseStatus;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ForbiddenException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.CHAPTER_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.MATERIAL_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.TableName.MATERIAL_TABLE;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "materials")
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final ChapterRepository chapterRepository;
    private final CheckDataUtil checkDataUtil;
    private final SlugUtil slugUtil;
    private final PurchaseRepository purchaseRepository;
    private final MaterialActivityRepository materialActivityRepository;
    private final UserProgressRepository userProgressRepository;

    @Override
    @CacheEvict(value = "allMaterials", allEntries = true)
    public MaterialResponse addMaterial(MaterialRequest request) {
        try {
            return chapterRepository.findFirstBySlugChapter(request.getSlugChapter()).map(chapter -> {
                String slug = request.getSlugMaterial() != null ? request.getSlugMaterial() :
                        slugUtil.toSlug(MATERIAL_TABLE, "slug_material", request.getMaterialName());
                Material material = Material.builder().
                        materialName(request.getMaterialName()).
                        serialNumber(request.getSerialNumber()).
                        videoLink(request.getVideoLink()).
                        materialDuration(request.getMaterialDuration()).
                        materialType(request.getMaterialType())
                        .slugMaterial(slug)
                        .chapter(chapter).build();
                Material savedMaterial = materialRepository.save(material);
                return MaterialResponse.builder().serialNumber(savedMaterial.getSerialNumber()).videoLink(savedMaterial.getVideoLink()).materialDuration(savedMaterial.getMaterialDuration()).materialType(savedMaterial.getMaterialType()).build();
            }).orElseThrow(() -> new DataNotFoundException(CHAPTER_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'getMaterialDetailAdmin-' + #slugMaterial"),
            @CacheEvict(key = "'getMaterialDetailCustomer-' + #slugMaterial"),
            @CacheEvict(value = "allMaterials", allEntries = true, condition = "#result != null")
    })
    public MaterialResponse updateMaterial(String slugMaterial, MaterialRequest request) {
        try {
            return materialRepository.findBySlugMaterial(slugMaterial).map(material -> {
                material.setMaterialName(request.getMaterialName());
                material.setSerialNumber(request.getSerialNumber());
                material.setVideoLink(request.getVideoLink());
                material.setMaterialDuration(request.getMaterialDuration());
                material.setMaterialType(request.getMaterialType());
                Optional.ofNullable(request.getSlugMaterial())
                        .ifPresent(newSlug -> {
                            checkDataUtil.checkDataField(MATERIAL_TABLE, "slug_material", newSlug, "material_id", material.getId());
                            material.setSlugMaterial(newSlug);
                        });
                Material savedMaterial = materialRepository.save(material);
                return MaterialResponse.builder().materialName(savedMaterial.getMaterialName()).serialNumber(savedMaterial.getSerialNumber()).videoLink(savedMaterial.getVideoLink()).materialDuration(savedMaterial.getMaterialDuration()).materialType(savedMaterial.getMaterialType()).build();
            }).orElseThrow(() -> new DataNotFoundException(MATERIAL_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to update material");
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(key = "'getMaterialDetailCustomer-' + #slugMaterial"),
            @CacheEvict(key = "'getMaterialDetailAdmin-' + #slugMaterial"),
            @CacheEvict(value = "allMaterials", allEntries = true)
    })
    public void deleteMaterial(String slugMaterial) {
        try {
            materialRepository.findBySlugMaterial(slugMaterial).ifPresentOrElse(
                    materialRepository::delete,
                    () -> {
                        throw new DataNotFoundException(MATERIAL_NOT_FOUND);
                    }
            );
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to delete material");
        }
    }

    @Override
    @Cacheable(value = "allMaterials", key = "'getAllMaterial-' + #pageable.pageNumber + '-' + #pageable.pageSize", unless = "#result == null")
    public Page<MaterialResponse> getAllMaterial(Pageable pageable) {
        try {
            Page<Material> materialPage = Optional.of(materialRepository.findAll(pageable))
                    .filter(Page::hasContent)
                    .orElseThrow(() -> new DataNotFoundException(MATERIAL_NOT_FOUND));
            return materialPage.map(material -> MaterialResponse.builder()
                    .materialName(material.getMaterialName())
                    .serialNumber(material.getSerialNumber())
                    .slugMaterial(material.getSlugMaterial())
                    .videoLink(material.getVideoLink())
                    .materialDuration(material.getMaterialDuration())
                    .materialType(material.getMaterialType())
                    .build());
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get all material");
        }
    }

    @Override
    @Cacheable(key = "'getMaterialDetailAdmin-' + #slugMaterial", unless = "#result == null")
    public MaterialResponse getMaterialDetailAdmin(String slugMaterial) {
        try {
            return materialRepository.findBySlugMaterial(slugMaterial).map(material -> MaterialResponse.builder()
                    .materialName(material.getMaterialName())
                    .serialNumber(material.getSerialNumber())
                    .videoLink(material.getVideoLink())
                    .materialDuration(material.getMaterialDuration())
                    .materialType(material.getMaterialType())
                    .build()).orElseThrow(() -> new DataNotFoundException(MATERIAL_NOT_FOUND));
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get material");
        }
    }

    @Override
    @Cacheable(key = "'getMaterialDetailCustomer-' + #slugMaterial", unless = "#result == null")
    public MaterialResponse getMaterialDetailCustomer(String slugMaterial, Principal principal) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            return materialRepository.findBySlugMaterial(slugMaterial)
                    .map(material -> {
                        if (material.getMaterialType().equals(EnumMaterialType.FREE)) {
                            return MaterialResponse.builder()
                                    .materialName(material.getMaterialName())
                                    .serialNumber(material.getSerialNumber())
                                    .videoLink(material.getVideoLink())
                                    .materialDuration(material.getMaterialDuration())
                                    .materialType(material.getMaterialType())
                                    .build();
                        } else {
                            return purchaseRepository.findByUserAndCourseAndPurchaseStatus(user, material.getChapter().getCourse(), EnumPurchaseStatus.PAID)
                                    .map(purchase -> MaterialResponse.builder()
                                            .materialName(material.getMaterialName())
                                            .serialNumber(material.getSerialNumber())
                                            .videoLink(material.getVideoLink())
                                            .materialDuration(material.getMaterialDuration())
                                            .materialType(material.getMaterialType())
                                            .build())
                                    .orElseThrow(() -> new ForbiddenException("You don't have access to this material"));
                        }
                    })
                    .orElseThrow(() -> new DataNotFoundException(MATERIAL_NOT_FOUND));
        } catch (DataNotFoundException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to get material");
        }
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allMaterials", allEntries = true),
            @CacheEvict(key = "'getMaterialDetailCustomer-' + #slugMaterial"),
            @CacheEvict(key = "'getMaterialDetailAdmin-' + #slugMaterial"),
    })
    public void completeMaterial(String slugMaterial, Principal principal) {
        try {
            User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            materialRepository.findBySlugMaterial(slugMaterial)
                    .ifPresentOrElse(
                            material -> purchaseRepository.findByUserAndCourseAndPurchaseStatus(user, material.getChapter().getCourse(), EnumPurchaseStatus.PAID)
                                    .ifPresentOrElse(
                                            purchase -> {
                                                materialActivityRepository.save(
                                                        MaterialActivity.builder()
                                                                .id(
                                                                        MaterialActivityKey.builder()
                                                                                .materialId(material.getId())
                                                                                .userId(user.getId())
                                                                                .build()
                                                                )
                                                                .material(material)
                                                                .user(user)
                                                                .build()
                                                );

                                                Integer totalMaterial = materialRepository.countByChapter_Course(material.getChapter().getCourse());
                                                Integer totalCompletedMaterial = materialActivityRepository.countByMaterial_Chapter_CourseAndUser(material.getChapter().getCourse(), user);
                                                Integer progress = (totalCompletedMaterial * 100) / totalMaterial;
                                                userProgressRepository.findByUserAndCourse(user, material.getChapter().getCourse())
                                                        .ifPresent(
                                                                userProgress -> {
                                                                    if (progress.equals(100)) {
                                                                        userProgress.setIsCompleted(true);
                                                                        userProgress.setCompletionDate(LocalDateTime.now());
                                                                    }
                                                                    userProgress.setCoursePercentage(progress);
                                                                    userProgressRepository.save(userProgress);
                                                                });
                                            },
                                            () -> {
                                                throw new ForbiddenException("You don't have access to this material");
                                            }
                                    ),
                            () -> {
                                throw new DataNotFoundException(MATERIAL_NOT_FOUND);
                            }
                    );
        } catch (DataNotFoundException | ForbiddenException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException(e.getMessage());
        }
    }
}
