package com.binar.byteacademy.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DashboardResponse implements Serializable {
    private int activeUser;
    private int nonActiveUser;
    private int activeCourse;
    private int nonActiveCourse;
    private int premiumCourse;
    private int freeCourse;
}
