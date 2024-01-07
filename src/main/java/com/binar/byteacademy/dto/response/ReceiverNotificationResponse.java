package com.binar.byteacademy.dto.response;

import com.binar.byteacademy.enumeration.EnumNotificationStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReceiverNotificationResponse implements Serializable {
    private UUID id;
    private transient NotificationResponse notification;
    private EnumNotificationStatus status;
}
