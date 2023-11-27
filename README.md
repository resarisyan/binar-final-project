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
            "courseName": "Sample Course",
            "courseSubTitle": "Sample Subtitle",
            "instructorName": "Instructor Name",
            "price": 99.99,
            "courseType": "FREE",
            "courseLevel": null,
            "totalCourseRate": 4.5,
            "totalModules": 10,
            "courseDuration": 30
        },
        {
            "courseName": "Another Course",
            "courseSubTitle": "Subtitle for Another Course",
            "instructorName": "Another Instructor",
            "price": 49.99,
            "courseType": "PREMIUM",
            "courseLevel": null,
            "totalCourseRate": 4.2,
            "totalModules": 8,
            "courseDuration": 25
        },
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
            "courseName": "Another Course",
            "courseSubTitle": "Subtitle for Another Course",
            "instructorName": "Another Instructor",
            "price": 49.99,
            "courseType": "PREMIUM",
            "courseLevel": "BEGINNER",
            "totalCourseRate": 4.2,
            "totalModules": 8,
            "courseDuration": 25
        },
        {
            "courseName": "Another Course",
            "courseSubTitle": "Subtitle for Another Course",
            "instructorName": "Another Instructor",
            "price": 49.99,
            "courseType": "PREMIUM",
            "courseLevel": "BEGINNER",
            "totalCourseRate": 4.2,
            "totalModules": 8,
            "courseDuration": 25
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
            "courseName": "Sample Course",
            "courseSubTitle": "Sample Subtitle",
            "instructorName": "Instructor Name",
            "price": 99.99,
            "courseType": "FREE",
            "courseLevel": "BEGINNER",
            "totalCourseRate": 4.5,
            "totalModules": 10,
            "courseDuration": 30
        },
        {
            "courseName": "Sample Course",
            "courseSubTitle": "Sample Subtitle",
            "instructorName": "Instructor Name",
            "price": 99.99,
            "courseType": "FREE",
            "courseLevel": "BEGINNER",
            "totalCourseRate": 4.5,
            "totalModules": 10,
            "courseDuration": 30
        }
    ],
    "status": "OK",
    "code": 200,
    "message": "List of courses retrieved successfully"
}
```

**GET /api/v1/course?{type}/{courseId}**

Return Detail Free Course by course id
# SOON

[//]: # ()
[//]: # (* **URL Params**  )

[//]: # (  *required:* `type=[FREE]/JVSB01`)

[//]: # (* **Data Params**  )

[//]: # (  None)

[//]: # (* **Headers**  )

[//]: # (  Content-Type: application/json)

[//]: # (* **Success Response:**)

[//]: # (* **Code:** 200  )

[//]: # (  **Content:**)

[//]: # ()
[//]: # (```)

[//]: # ({)

[//]: # (   courseName: "Frontend Development",)

[//]: # (   courseSubTitle: "Modern JavaScript Frameworks",)

[//]: # (   intructorName: "John Doe")

[//]: # (   price: 0)

[//]: # (   courseDuration: 120)

[//]: # (   courseDescription: "Master the latest JavaScript frameworks for frontend development.")

[//]: # (   slugCourse: "/java-spring-boot")

[//]: # (   pathCourseImage: "http://yourimageurl.com/image_sub_path_1")

[//]: # (   groupLink: "http://yourgroupurl.com/class)

[//]: # (   courseType: "FREE")

[//]: # (   courseLevel: "INTERMEDIATE")

[//]: # (   courseStatus: "ACTIVE" )

[//]: # (   categoryId: {)

[//]: # (          chapterId: "JSFW01",)

[//]: # (          categoryName: "JavaScript Frameworks",)

[//]: # (          pathCategoryImage: "http://yourimageurl.com/category_image_path_2 ")

[//]: # (   })

[//]: # (})

[//]: # (```)

[//]: # ()
[//]: # (**GET /list-courses/{courseType}/{courseId}**)

[//]: # ()
[//]: # (Return Detail Paid Course by course id)

[//]: # ()
[//]: # (* **URL Params**  )

[//]: # (  *required:* `courseType=[String]/JVSB01`)

[//]: # (* **Data Params**  )

[//]: # (  None)

[//]: # (* **Headers**  )

[//]: # (  Content-Type: application/json)

[//]: # (* **Success Response:**)

[//]: # (* **Code:** 200  )

[//]: # (  **Content:**)

[//]: # ()
[//]: # (```)

[//]: # ({)

[//]: # (   courseName: "Backend Development",)

[//]: # (   courseSubTitle: "Java SpringBoot for beginner",)

[//]: # (   intructorName: "John Doe")

[//]: # (   price: 0)

[//]: # (   courseDuration: 120)

[//]: # (   courseDescription: Java spring boot for backend development)

[//]: # (   slugCourse: /java-spring-boot)

[//]: # (   pathCourseImage: "http://yourimageurl.com/image_sub_path_1")

[//]: # (   groupLink: "http://yourgroupurl.com/class)

[//]: # (   courseType: "PAID")

[//]: # (   courseLevel: "BEGINNER")

[//]: # (   courseStatus: "ACTIVE")

[//]: # (   categoryId: {)

[//]: # (          chapterId: "JVSPB01",)

[//]: # (          categoryName: "Java Spring Boot",)

[//]: # (          pathCategoryImage: "http://yourimageurl.com/category_image_path_2")

[//]: # (   })

[//]: # (})
[//]: # (```)

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




