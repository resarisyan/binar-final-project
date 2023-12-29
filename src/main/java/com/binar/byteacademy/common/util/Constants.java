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
        public static final String ADMIN_COURSE_PATS = BASE_URL + "/admin/course";
        public static final String ADMIN_COURSE_PATS_ALL = ADMIN_COURSE_PATS + "/**";
    }

    public static final class UserPats {
        public static final String CUSTOMER_USER_PATS = BASE_URL + "/customer/user";
        public static final String ADMIN_USER_PATS = BASE_URL + "/admin/user";
        public static final String ADMIN_USER_PATS_ALL = ADMIN_USER_PATS + "/**";
        public static final String CUSTOMER_USER_PATS_ALL = CUSTOMER_USER_PATS + "/**";
    }

    public static final class ReplyPats {
        public static final String CUSTOMER_REPLY_PATS = BASE_URL + "/customer/reply";
        public static final String CUSTOMER_REPLY_PATS_ALL = CUSTOMER_REPLY_PATS + "/**";
    }

    public static final class DiscussionPats {
        public static final String CUSTOMER_DISCUSSION_PATS = BASE_URL + "/customer/discussion";
        public static final String CUSTOMER_DISCUSSION_PATS_ALL = CUSTOMER_DISCUSSION_PATS + "/**";
    }

    public static final class CommentPats {
        public static final String CUSTOMER_COMMENT_PATS = BASE_URL + "/customer/comment";
        public static final String CUSTOMER_COMMENT_PATS_ALL = CUSTOMER_COMMENT_PATS + "/**";
    }

    public static final class CallbackPats {
        public static final String CALLBACK_PATS = BASE_URL + "/callback";
    }

    public static final class PurchasePats {
        public static final String CUSTOMER_PURCHASE_PATS = BASE_URL + "/customer/purchase";
        public static final String CUSTOMER_PURCHASE_PATS_ALL = CUSTOMER_PURCHASE_PATS + "/**";
        public static final String ADMIN_PURCHASE_PATS = BASE_URL + "/admin/purchase";
        public static final String ADMIN_PURCHASE_PATS_ALL = ADMIN_PURCHASE_PATS + "/**";
    }

    public static final class NotificationPats {
        public static final String CUSTOMER_NOTIFICATION_PATS = BASE_URL + "/customer/notification";
        public static final String CUSTOMER_NOTIFICATION_PATS_ALL = CUSTOMER_NOTIFICATION_PATS + "/**";
        public static final String ADMIN_NOTIFICATION_PATS = BASE_URL + "/admin/notification";
        public static final String ADMIN_NOTIFICATION_PATS_ALL = ADMIN_NOTIFICATION_PATS + "/**";
    }

    public static final class ChapterPats {
        public static final String ADMIN_CHAPTER_PATS = BASE_URL + "/admin/chapter";
        public static final String ADMIN_CHAPTER_PATS_ALL = ADMIN_CHAPTER_PATS + "/**";
    }

    public static final class MaterialPats {
        public static final String ADMIN_MATERIAL_PATS = BASE_URL + "/admin/material";
        public static final String ADMIN_MATERIAL_PATS_ALL = ADMIN_MATERIAL_PATS + "/**";
        public static final String CUSTOMER_MATERIAL_PATS = BASE_URL + "/customer/material";
        public static final String CUSTOMER_MATERIAL_PATS_ALL = CUSTOMER_MATERIAL_PATS + "/**";
    }

    public static final class PromoPats {
        public static final String ADMIN_PROMO_PATS = BASE_URL + "/admin/promo";
        public static final String ADMIN_PROMO_PATS_ALL = ADMIN_PROMO_PATS + "/**";
    }

    public static final class CoursePromoPats {
        public static final String ADMIN_COURSE_PROMO_PATS = BASE_URL + "/admin/course-promo";
        public static final String ADMIN_COURSE_PROMO_PATS_ALL = ADMIN_COURSE_PROMO_PATS + "/**";
    }

    public static final class DashboardPats {
        public static final String ADMIN_DASHBOARD_PATS = BASE_URL + "/admin/dashboard";
    }

    public static final class WebsocketPats {
        public static final String BROKER_ALL = "/all/**";
        public static final String BROKER_USER = "/user/**";
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
                PurchasePats.CUSTOMER_PURCHASE_PATS_ALL,
                DiscussionPats.CUSTOMER_DISCUSSION_PATS_ALL,
                CommentPats.CUSTOMER_COMMENT_PATS_ALL,
                ReplyPats.CUSTOMER_REPLY_PATS_ALL,
                PurchasePats.ADMIN_PURCHASE_PATS_ALL,
                MaterialPats.CUSTOMER_MATERIAL_PATS_ALL,
                NotificationPats.CUSTOMER_NOTIFICATION_PATS_ALL,
                NotificationPats.ADMIN_NOTIFICATION_PATS_ALL,
                UserPats.ADMIN_USER_PATS_ALL,
                UserPats.CUSTOMER_USER_PATS_ALL,
                WebsocketPats.BROKER_ALL,
                WebsocketPats.BROKER_USER
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
        public static final String DISCUSSION_NOT_FOUND = "Discussion not found";
        public static final String COMMENT_NOT_FOUND = "Comment not found";
        public static final String REPLY_NOT_FOUND = "Reply not found";
        public static final String TOKEN_NOT_FOUND = "Token not found";
        public static final String TOKEN_EXPIRED = "Token expired";
        public static final String USER_NOT_FOUND = "User not found";
    }

    public static final class TableName {
        public static final String CATEGORY_TABLE = "categories";
        public static final String DISCUSSION_TABLE = "discussions";
        public static final String CHAPTER_TABLE = "chapters";
        public static final String COURSE_TABLE = "courses";
        public static final String MATERIAL_TABLE = "materials";
        public static final String PROMO_TABLE = "promos";
    }
}
