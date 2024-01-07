# Binar Final Project - API Online Course Platform ByteAcademy

## Overview

This repository contains the final project for the online course platform developed by a team of four members using the Spring Boot framework. The project integrates various technologies, including PostgreSQL, Twilio, JWT (JSON Web Tokens), SMTP Gmail for email notifications, Cloudinary for image storage, OpenAI for language processing, and OAuth Google for authentication. The documentation is generated using SpringDoc and Postman.

## Installation

To get started with the project, follow these steps:

```bash
git clone https://github.com/resarisyan/binar-final-project
cd binar-final-project
mvn clean install
```
## Configuration

Configure the application by rename the `env-example.properties` to `env.properties` with the necessary information for PostgreSQL, Twilio, JWT, SMTP Gmail, Cloudinary, OpenAI, and OAuth Google.

```bash
# Database Configuration
spring.datasource.url=jdbc:postgresql://your-postgresql-host:5432/your-database-name
spring.datasource.username=your-username
spring.datasource.password=your-password

# Twilio Configuration
twilio.accountSid=your-account-sid
twilio.authToken=your-auth-token
twilio.phoneNumber=+1234567890

# JWT Configuration
jwt.secret=your-secret-key
application.security.jwt.expiration=86400000
application.security.jwt.refresh-expiration=604800000

# SMTP Gmail Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-email-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Cloudinary Configuration
cloudinary.cloud-name=your-cloud-name
cloudinary.api-key=your-api-key
cloudinary.api-secret=your-api-secret

# OpenAI Configuration
openai.api-key=your-api-key

# OAuth Google Configuration
spring.security.oauth2.client.registration.google.client-id=your-client-id
spring.security.oauth2.client.registration.google.client-secret=your-client-secret
```

## API Documentation

The API documentation is generated using SpringDoc. Access the documentation by visiting [http://localhost:8080/swagger-ui.html](https://byteacademy.as.r.appspot.com/swagger-ui/index.html) after running the application.

```bash
# Usage

To run the application, use the following command:

mvn spring-boot:run
```

## Contributors

- Resa Auliana Risyan
- Najib Sauqi Rubbayani
- Akbar Oktaviadi
- Lutfi Isnan Safrudin

## Acknowledgments

We would like to express our gratitude to the following technologies and frameworks that made this project possible:

- Spring Boot
- PostgreSQL
- Twilio
- JWT
- SMTP Gmail
- Cloudinary
- OpenAI
- OAuth Google

## License

This project is licensed under the [MIT License](https://choosealicense.com/licenses/mit/).
