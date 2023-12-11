package com.binar.byteacademy.service;

import com.binar.byteacademy.common.util.CheckDataUtil;
import com.binar.byteacademy.common.util.SlugUtil;
import com.binar.byteacademy.dto.request.MaterialRequest;
import com.binar.byteacademy.dto.response.MaterialResponse;
import com.binar.byteacademy.entity.Material;
import com.binar.byteacademy.exception.DataNotFoundException;
import com.binar.byteacademy.exception.ServiceBusinessException;
import com.binar.byteacademy.repository.ChapterRepository;
import com.binar.byteacademy.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.binar.byteacademy.common.util.Constants.ControllerMessage.CHAPTER_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.ControllerMessage.MATERIAL_NOT_FOUND;
import static com.binar.byteacademy.common.util.Constants.TableName.MATERIAL_TABLE;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {
    private final MaterialRepository materialRepository;
    private final ChapterRepository chapterRepository;
    private final CheckDataUtil checkDataUtil;
    private final SlugUtil slugUtil;

    @Override
    public MaterialResponse addMaterial(MaterialRequest request) {
        try {
            return chapterRepository.findById(request.getChapterId()).map(chapter -> {
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
    public void updateMaterial(String slugMaterial, MaterialRequest request) {
        try {
            materialRepository.findBySlugMaterial(slugMaterial).ifPresentOrElse(
                    material -> chapterRepository.findById(request.getChapterId()).ifPresentOrElse(
                            chapter -> {
                                material.setMaterialName(request.getMaterialName());
                                material.setSerialNumber(request.getSerialNumber());
                                material.setVideoLink(request.getVideoLink());
                                material.setMaterialDuration(request.getMaterialDuration());
                                material.setMaterialType(request.getMaterialType());
                                material.setChapter(chapter);
                                Optional.ofNullable(request.getSlugMaterial())
                                        .ifPresent(newSlug -> {
                                            checkDataUtil.checkDataField(MATERIAL_TABLE, "slug_material", newSlug, "material_id", material.getId());
                                            material.setSlugMaterial(newSlug);
                                        });
                                materialRepository.save(material);
                            },
                            () -> {
                                throw new DataNotFoundException(CHAPTER_NOT_FOUND);
                            }),
                    () -> {
                        throw new DataNotFoundException(MATERIAL_NOT_FOUND);
                    }
            );
        } catch (DataNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to update material");
        }
    }

    @Override
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
    public MaterialResponse getMaterialDetail(String slugMaterial) {
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
}