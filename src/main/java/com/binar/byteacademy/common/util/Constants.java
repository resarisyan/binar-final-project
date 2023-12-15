package com.binar.byteacademy.common.util;

public class Constants {

    public static final String API_VERSION = "/v1";
    public static final String BASE_URL = "/api" + API_VERSION;

    public static final class AuthPats {
        public static final String AUTH_PATS = BASE_URL + "/auth";
    }

    public static final class SettingPats {
        public static final String SETTING_PATS = BASE_URL + "/setting";
    }

    public static final class CategoryPats {
        public static final String CATEGORY_PATS = BASE_URL + "/category";
        public static final String CATEGORY_PATS_ALL = BASE_URL + "/category/**";
        public static final String ADMIN_CATEGORY_PATS = BASE_URL + "/admin/category";

    }

    public static final class CoursePats {
        public static final String COURSE_PATS = BASE_URL + "/course";
        public static final String CUSTOMER_COURSE_PATS = COURSE_PATS + "/my-course";

        public static final String ADMIN_COURSE_PATS = BASE_URL + "/admin/course";
        public static final String COURSE_PATS_ALL = BASE_URL + "/course/**";

        public static final String CUSTOMER_USER_PATS = BASE_URL + "/customer/user";

    }

    public static final class ChapterPats {
        public static final String ADMIN_CHAPTER_PATS = BASE_URL + "/admin/chapter";
    }

    public static final class MaterialPats {
        public static final String ADMIN_MATERIAL_PATS = BASE_URL + "/admin/material";
    }

    public static final class PromoPats {
        public static final String ADMIN_PROMO_PATS = BASE_URL + "/admin/promo";
    }

    public static final class CoursePromoPats {
        public static final String ADMIN_COURSE_PROMO_PATS = BASE_URL + "/admin/course-promo";
    }
    public static final class DiscussionPats {
        public static final String DISCUSSION_PATS = BASE_URL + "/discussion";
        public static final String DISCUSSION_PATS_ALL = BASE_URL + "/discussion/**";
    }
    public static final class ReplyPats {
        public static final String REPLY_PATS = BASE_URL + "/replies";
        public static final String REPLY_PATS_ALL = BASE_URL + "/replies/**";
    }
    public static final class CommentPats {
        public static final String COMMENT_PATS = BASE_URL + "/comment";
        public static final String COMMENT_PATS_ALL = BASE_URL + "/comment/**";
    }

    public static final class CommonPats {
        public static final String[] WHITE_LIST_PATS = {
                "/api/v1/auth/login",
                "/api/v1/auth/register",
                "/api/v1/discussion/**",
                "/api/v1/replies/**",
                "/api/v1/auth/verify-register-phone",
                "/api/v1/auth/regenerate-otp-register",
                "/api/v1/auth/verify-email-register",
                "/api/v1/auth/regenerate-register-email",
                "/api/v1/auth/verify-forgot-password-email",
                "/api/v1/auth/forgot-password-email",
                "/api/v1/course/search",
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

    public static final class ControllerMessage {
        public static final String SUCCESS_GET_COURSE = "Course successfully retrieved";
        public static final String CATEGORY_NOT_FOUND = "Category not found";
        public static final String COURSE_NOT_FOUND = "Course not found";
        public static final String CHAPTER_NOT_FOUND = "Chapter not found";
        public static final String MATERIAL_NOT_FOUND = "Material not found";
        public static final String PROMO_NOT_FOUND = "Promo not found";
        public static final String COURSE_PROMO_NOT_FOUND = "Course promo not found";
    }

    public static final class TableName {
        public static final String CATEGORY_TABLE = "categories";
        public static final String COURSE_TABLE = "courses";
        public static final String MATERIAL_TABLE = "materials";
        public static final String PROMO_TABLE = "promos";
        public static final String CUSTOMER_DETAIL_TABLE = "customer_details";
        public static final String USER_TABLE = "users";
    }
}
