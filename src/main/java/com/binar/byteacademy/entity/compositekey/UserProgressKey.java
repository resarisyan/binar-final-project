package com.binar.byteacademy.entity.compositekey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserProgressKey implements Serializable {
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "course_id")
    private UUID courseId;
}
