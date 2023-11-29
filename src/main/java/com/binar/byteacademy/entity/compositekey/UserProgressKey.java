package com.binar.byteacademy.entity.compositekey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class UserProgressKey implements Serializable {
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "course_id")
    private UUID courseId;
}
