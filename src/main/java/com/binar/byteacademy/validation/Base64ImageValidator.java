package com.binar.byteacademy.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

@Slf4j
public class Base64ImageValidator implements ConstraintValidator<Base64Image, String> {

    @Override
    public void initialize(Base64Image base64Image) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        try {
            byte[] decodedBytes = Base64.getMimeDecoder().decode(value.split(",")[1]);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(decodedBytes));
            log.info("Image is {}", decodedBytes);
            return image != null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
