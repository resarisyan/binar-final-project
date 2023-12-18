package com.binar.byteacademy.common.util;

public class Constants {

    public static final String API_VERSION = "/v1";
    public static final String BASE_URL = "/api" + API_VERSION;

    public static final class AuthPats {
        public static final String AUTH_PATS = BASE_URL + "/auth";
    }

    public static final class SettingPats {
        public static final String SETTING_PATS = BASE_URL + "/setting";
        public static final String SETTING_PATS_ALL = SETTING_PATS + "/**";
    }

    public static final class CategoryPats {
        public static final String CATEGORY_PATS = BASE_URL + "/category";
        public static final String ADMIN_CATEGORY_PATS = BASE_URL + "/admin/category";
        public static final String ADMIN_CATEGORY_PATS_ALL = ADMIN_CATEGORY_PATS + "/**";
    }

    public static final class CoursePats {
        public static final String COURSE_PATS = BASE_URL + "/course";
        public static final String CUSTOMER_COURSE_PATS = COURSE_PATS + "/my-course";
        public static final String CUSTOMER_USER_PATS = BASE_URL + "/customer/user";
        public static final String ADMIN_COURSE_PATS = BASE_URL + "/admin/course";
        public static final String ADMIN_COURSE_PATS_ALL = ADMIN_COURSE_PATS + "/**";
    }

    public static final class CustomerPurchasePats {
        public static final String CUSTOMER_PURCHASE_PATS = BASE_URL + "/customer/purchase";
    }

    public static final class ChapterPats {
        public static final String ADMIN_CHAPTER_PATS = BASE_URL + "/admin/chapter";
        public static final String ADMIN_CHAPTER_PATS_ALL = ADMIN_CHAPTER_PATS + "/**";
    }

    public static final class MaterialPats {
        public static final String ADMIN_MATERIAL_PATS = BASE_URL + "/admin/material";
        public static final String ADMIN_MATERIAL_PATS_ALL = ADMIN_MATERIAL_PATS + "/**";
    }

    public static final class PromoPats {
        public static final String ADMIN_PROMO_PATS = BASE_URL + "/admin/promo";
        public static final String ADMIN_PROMO_PATS_ALL = ADMIN_PROMO_PATS + "/**";
    }

    public static final class CoursePromoPats {
        public static final String ADMIN_COURSE_PROMO_PATS = BASE_URL + "/admin/course-promo";
        public static final String ADMIN_COURSE_PROMO_PATS_ALL = ADMIN_COURSE_PROMO_PATS + "/**";
    }

    public static final class CommonPats {
        public static final String[] SECURE_LIST_PATS = {
                CoursePats.ADMIN_COURSE_PATS_ALL,
                CategoryPats.ADMIN_CATEGORY_PATS_ALL,
                ChapterPats.ADMIN_CHAPTER_PATS_ALL,
                MaterialPats.ADMIN_MATERIAL_PATS_ALL,
                PromoPats.ADMIN_PROMO_PATS_ALL,
                CoursePromoPats.ADMIN_COURSE_PROMO_PATS_ALL,
                SettingPats.SETTING_PATS_ALL,
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
        public static final String PURCHASE_NOT_FOUND = "Purchase not found";
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
