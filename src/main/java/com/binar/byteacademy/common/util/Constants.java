package com.binar.byteacademy.common.util;

public class Constants {

    public static final String API_VERSION = "/v1";
    public static final String BASE_URL = "/api" + API_VERSION;

    public static final class AuthPats {
        public static final String AUTH_PATS = BASE_URL + "/auth";
    }
    public static final class CategoryPats {
        public static final String CATEGORY_PATS = BASE_URL + "/category";
        public static final String CATEGORY_PATS_ALL = BASE_URL + "/category/**";
    }
    public static final class CoursePats {
        public static final String COURSE_PATS = BASE_URL + "/course";
        public static final String ADMIN_COURSE_PATS = BASE_URL + "/admin/course";
        public static final String COURSE_PATS_ALL = BASE_URL + "/course/**";
        public static final String COURSE_PATS_ADMIN = COURSE_PATS + "/all";
        public static final String COURSE_PATS_CUSTOMER = COURSE_PATS + "/my-course";
    }

    public static final class CommonPats {
        public static final String[] WHITE_LIST_PATS = {
                "/api/v1/auth/**",
                "/api/v1/course/search",
                "/v1/auth/**",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html",
                "/docs",
                "/api/v1/course"
        };
    }

    public static final class ControllerMessage{
        public static final String SUCCESS_GET_COURSE = "Course successfully retrieved";
    }

//    public static final class Exception {
//        public static final class Common {
//            public static final String INVALID_PARAM_CODE = "000001";
//            public static final String INVALID_PARAM = "Invalid request params";
//        }
//    }
//
//    public static final class Client {
//        public static final String BASE_URL = "https://byteacademy.github.io/";
//    }

}
