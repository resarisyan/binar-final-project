## API Contract

To use this FE demo, you're required to comply with this API contract

### User

**POST /api/v1/auth/register**

Registering new user

* **URL Params**  
  None
* **Data Params**

```
{
    username: "dummy username",
    email: "dummyemail@gmail.com",
    password: "dummypassword",
    name: "dummy",
    phoneNumber: "0812345678"
}
```

* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 201  
  **Content:**

```
{
    status: "CREATED",
    message: "Customer successfully created"
}
```

**POST /api/v1/auth/login**

Login as User

* **URL Params**  
  None
* **Data Params**

```
{
    credential: "dummycredential@gmail.com"
    password: "dummyPassword"
}
```

* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**

```
{
    status: "OK",
    message: "Login Success"
}
```

**POST /api/v1/auth/verify-account**

Verify Account

* **URL Params**  
  *required:* `username=[String]&otp=[String]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**

```
{
    status: "OK",
    message: "Account successfully verified"
}
```

**POST /api/v1/auth/regenerate-otp**

Regenerate Token OTP

* **URL Params**  
  *required:* `username=[String]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**

```
{
    status: "OK",
    message: "OTP successfully regenerated"
}
```

**POST /api/v1/auth/refresh-token**

Refresh token

* **URL Params**  
  None
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 201  
  **Content:**

```
{
    status: "CREATED",
    message: "Refresh token success"
}
```

### Course

**GET /api/v1/course**

Return all course

* **URL Params**  
  None
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**

```
{
  "results": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "courseName": "Introduction to Programming",
      "instructorName": "Jane Smith",
      "price": 0,
      "totalCourseRate": 4,
      "totalModules": 8,
      "courseDuration": 20,
      "slugCourse": "introduction-to-programming",
      "courseType": "FREE",
      "courseLevel": "BEGINNER",
      "pathCourseImage": "/images/intro-to-programming.jpg",
      "category": {
        "categoryId": "550e8400-e29b-41d4-a716-446655440000",
        "categoryName": "Programming",
        "pathCategoryImage": "/images/example.jpg"
      }
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440004",
      "courseName": "Full Stack Web Development Bootcamp",
      "instructorName": "Alex Turner",
      "price": 79.99,
      "totalCourseRate": 4.9,
      "totalModules": 20,
      "courseDuration": 60,
      "slugCourse": "full-stack-web-development-bootcamp",
      "courseType": "PREMIUM",
      "courseLevel": "INTERMEDIATE",
      "pathCourseImage": "/images/webdev-bootcamp.jpg",
      "category": {
        "categoryId": "550e8400-e29b-41d4-a716-446655440002",
        "categoryName": "Web Development",
        "pathCategoryImage": "/images/webdev.jpg"
      }
    }
  ],
  "status": "OK",
  "code": 200,
  "message": "List of courses retrieved successfully"
}
```

**GET /api/v1/course?{type}**

Return All PREMIUM Course 

* **URL Params**  
  *required:* `type=[PREMIUM]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**

```
{
  "results": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440003",
      "courseName": "Advanced Machine Learning",
      "instructorName": "Chris Anderson",
      "price": 49.99,
      "totalCourseRate": 4.8,
      "totalModules": 15,
      "courseDuration": 40,
      "slugCourse": "advanced-machine-learning",
      "courseType": "PREMIUM",
      "courseLevel": "ADVANCE",
      "pathCourseImage": "/images/advanced-ml.jpg",
      "category": {
        "categoryId": "550e8400-e29b-41d4-a716-446655440001",
        "categoryName": "Data Science",
        "pathCategoryImage": "/images/datascience.jpg"
      }
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440004",
      "courseName": "Full Stack Web Development Bootcamp",
      "instructorName": "Alex Turner",
      "price": 79.99,
      "totalCourseRate": 4.9,
      "totalModules": 20,
      "courseDuration": 60,
      "slugCourse": "full-stack-web-development-bootcamp",
      "courseType": "PREMIUM",
      "courseLevel": "INTERMEDIATE",
      "pathCourseImage": "/images/webdev-bootcamp.jpg",
      "category": {
        "categoryId": "550e8400-e29b-41d4-a716-446655440002",
        "categoryName": "Web Development",
        "pathCategoryImage": "/images/webdev.jpg"
      }
    }
  ],
  "status": "OK",
  "code": 200,
  "message": "List of courses retrieved successfully"
}
```

**GET /api/v1/course?{type}**

Return All FREE Course

* **URL Params**  
  *required:* `type=[FREE]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**

```
{
  "results": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440001",
      "courseName": "Introduction to Programming",
      "instructorName": "Jane Smith",
      "price": 0,
      "totalCourseRate": 4,
      "totalModules": 8,
      "courseDuration": 20,
      "slugCourse": "introduction-to-programming",
      "courseType": "FREE",
      "courseLevel": "BEGINNER",
      "pathCourseImage": "/images/intro-to-programming.jpg",
      "category": {
        "categoryId": "550e8400-e29b-41d4-a716-446655440000",
        "categoryName": "Programming",
        "pathCategoryImage": "/images/example.jpg"
      }
    },
    {
      "id": "550e8400-e29b-41d4-a716-446655440002",
      "courseName": "Introduction to Web Development",
      "instructorName": "Mike Johnson",
      "price": 0,
      "totalCourseRate": 4.2,
      "totalModules": 10,
      "courseDuration": 25,
      "slugCourse": "introduction-to-web-development",
      "courseType": "FREE",
      "courseLevel": "BEGINNER",
      "pathCourseImage": "/images/intro-to-webdev.jpg",
      "category": {
        "categoryId": "550e8400-e29b-41d4-a716-446655440002",
        "categoryName": "Web Development",
        "pathCategoryImage": "/images/webdev.jpg"
      }
    }
  ],
  "status": "OK",
  "code": 200,
  "message": "List of courses retrieved successfully"
}
```

**GET /api/v1/course?{type}&{courseId}**

Return Detail Free Course by course id

* **URL Params**
  *required:* `type=[FREE]&id=[UUID]`
* **Data Params**
  None
* **Headers**
  Content-Type: application/json
* **Success Response:**
* **Code:** 200
* **Content:**
```

{
  "results": {
    "id": "550e8400-e29b-41d4-a716-446655440001",
    "courseName": "Introduction to Programming",
    "instructorName": "Jane Smith",
    "totalCourseRate": 4,
    "totalModules": 8,
    "courseDuration": 20,
    "slugCourse": "intro-to-programming",
    "pathCourseImage": "/images/intro-to-programming.jpg",
    "groupLink": "https://example.com/intro-group",
    "courseType": "FREE",
    "courseLevel": "BEGINNER",
    "listChapter": [
      {
        "chapterId": "6b7920a1-b96e-4d89-9b60-2c3ba6e3f476",
        "noChapter": 1,
        "title": "Introduction to the Course",
        "chapterDuration": 15,
        "materials": [
          {
            "materialId": "5e4b52ba-98d0-4a9f-8ebd-c6c8a9b908de",
            "serialNumber": 1,
            "videoLink": "https://videos.com/chapter1/video1.mp4",
            "materialDuration": 120,
            "slugMaterial": "video1-material",
            "materialType": "FREE"
          },
          {
            "materialId": "7f7b3ea2-9d5d-4d0b-b66c-8e41a977e4e1",
            "serialNumber": 2,
            "videoLink": "https://videos.com/chapter1/video2.mp4",
            "materialDuration": 90,
            "slugMaterial": "video2-material",
            "materialType": "FREE"
          }
        ]
      },
      {
        "chapterId": "6b7920a1-b96e-4d89-9b60-2c3ba6e3f477",
        "noChapter": 2,
        "title": "what is programming?",
        "chapterDuration": 30,
        "materials": [
          {
            "materialId": "a5e7f8d9-12c4-4b32-aeb2-34b58092f318",
            "serialNumber": 1,
            "videoLink": "https://docs.com/chapter2/document1.pdf",
            "materialDuration": 60,
            "slugMaterial": "document1-material",
            "materialType": "PAID"
          }
        ]
      }
    ],
    "category": {
      "categoryId": "550e8400-e29b-41d4-a716-446655440000",
      "categoryName": "Programming",
      "pathCategoryImage": "/images/example.jpg"
    }
  },
  "status": "OK",
  "code": 200,
  "message": "Course details retrieved successfully"
}

```

**GET /api/v1/course?{type}&{id}**

Return Detail Premium Course by course id

* **URL Params**
  *required:* `type=[String]&id=[UUID]`
* **Data Params**
  None
* **Headers**
  Content-Type: application/json
* **Success Response:**
* **Code:** 200
  
* **Content:**

```
{
  "results": {
    "id": "550e8400-e29b-41d4-a716-446655440003",
    "courseName": "Advanced Machine Learning",
    "instructorName": "Chris Anderson",
    "totalCourseRate": 4.8,
    "totalModules": 15,
    "courseDuration": 40,
    "slugCourse": "advanced-ml",
    "pathCourseImage": "/images/advanced-ml.jpg",
    "groupLink": "https://example.com/ml-group",
    "courseType": "PREMIUM",
    "courseLevel": "ADVANCE",
    "listChapter": [
      {
        "chapterId": "550e8400-e29b-41d4-a716-446655440004",
        "noChapter": 1,
        "title": "Introduction to Advanced Machine Learning",
        "chapterDuration": 10,
        "materials": [
          {
            "materialId": "550e8400-e29b-41d4-a716-446655440006",
            "serialNumber": 1,
            "videoLink": "https://videos.com/chapter1/video1.mp4",
            "materialDuration": 120,
            "slugMaterial": "video1-material",
            "materialType": "FREE"
          },
          {
            "materialId": "550e8400-e29b-41d4-a716-446655440007",
            "serialNumber": 2,
            "videoLink": "https://videos.com/chapter1/video2.mp4",
            "materialDuration": 90,
            "slugMaterial": "video2-material",
            "materialType": "PAID"
          }
        ]
      },
      {
        "chapterId": "550e8400-e29b-41d4-a716-446655440005",
        "noChapter": 2,
        "title": "Deep Learning Techniques",
        "chapterDuration": 15,
        "materials": [
          {
            "materialId": "550e8400-e29b-41d4-a716-446655440008",
            "serialNumber": 1,
            "videoLink": "https://docs.com/chapter2/document1.pdf",
            "materialDuration": 60,
            "slugMaterial": "document1-material",
            "materialType": "PAID"
          }
        ]
      }
    ],
    "category": {
      "categoryId": "550e8400-e29b-41d4-a716-446655440001",
      "categoryName": "Data Science",
      "pathCategoryImage": "/images/datascience.jpg"
    }
  },
  "status": "OK",
  "code": 200,
  "message": "Course details retrieved successfully"
}
```

### Admin

**GET /admin/dashboard**

Return All Course

* **URL Params**  
  None
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**

```
{
   courseId: "FED01",
   categoryId: {
          chapterId: "JSFW01",
          categoryName: "JavaScript Frameworks",
          pathCategoryImage: "http://yourimageurl.com/category_image_path_2 ",
   },
   courseName: "Frontend Development",
   courseType: "FREE",
   courseLevel: "INTERMEDIATE",
   price: 0,
   courseDescription: "Master the latest JavaScript frameworks for frontend development."
}
```

**POST /admin/manage-class/add-course**

Create new Course

* **URL Params**  
  None
* **Data Params**

```
{
   courseName: "Frontend Development",
   categoryId: {
          chapterId: "JSFW01",
          categoryName: "JavaScript Frameworks",
          pathCategoryImage: "http://yourimageurl.com/category_image_path_2 ",
   },
   courseType: "FREE",
   courseLevel: "INTERMEDIATE",
   price: 0,
   courseDescription: "Master the latest JavaScript frameworks for frontend development."
}
```

* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 201  
  **Content:**

```
{
    status: "CREATED",
    message: "Course successfully created"
}
```

**DELETE /admin/manage-class/{courseId}**

Delete Course by course id

* **URL Params**  
  *required:* `courseId=[String]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**

```
{
    status: "OK",
    message: "Course successfully Deleted"
}
```

**PUT /admin/manage-class/{courseId}**

Edit Course by course id

* **URL Params**  
  *required:* `courseId=[String]`
* **Data Params**  
  None
* **Headers**  
  Content-Type: application/json
* **Success Response:**
* **Code:** 200  
  **Content:**

```
{
    status: "OK",
    message: "Course successfully Edited"
}
```




