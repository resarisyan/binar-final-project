package com.binar.byteacademy.common.util;

import com.binar.byteacademy.exception.ServiceBusinessException;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageUtil {
    private final Cloudinary cloudinary;

    public CompletableFuture<String> base64UploadImage(String base64Image) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] decodedBytes = Base64.getMimeDecoder().decode(base64Image.split(",")[1]);
                return cloudinary.uploader().upload(
                        decodedBytes,
                        Map.of("public_id", UUID.randomUUID().toString())
                ).get("url").toString();
            } catch (Exception e) {
                throw new ServiceBusinessException("Failed to upload image");
            }
        });
    }

    @Async
    public void deleteImage(String imageUrl) {
       try {
           String publicId = extractPublicId(imageUrl);
           cloudinary.uploader().destroy(publicId, Map.of("invalidate", true));
       } catch (Exception e) {
           throw new ServiceBusinessException("Failed to delete image");
       }
    }

    private String extractPublicId(String imageUrl) {
        try {
            int startIndex = imageUrl.lastIndexOf("/") + 1;
            int endIndex = imageUrl.lastIndexOf(".");
            return imageUrl.substring(startIndex, endIndex);
        } catch (Exception e) {
            throw new ServiceBusinessException("Failed to extract public id");
        }
    }
}
