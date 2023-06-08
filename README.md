# Spring Boot 3.0.7 JPA Hibernate User Creation, Authentication, Authorizaton &amp; Session Management

## Requirements

    $ java -version

>     openjdk version "20.0.1" 2023-04-18
>     OpenJDK Runtime Environment (build 20.0.1+9-29)
>     OpenJDK 64-Bit Server VM (build 20.0.1+9-29, mixed mode, sharing)

    $ mvn -version
>     Apache Maven 3.9.2 (c9616018c7a021c1c39be70fb2843d6f5f9b8a1c)
>     Maven home: ~/.sdkman/candidates/maven/current
>     Java version: 20.0.1, vendor: Oracle Corporation, runtime: ~/.sdkman/candidates/java/20.0.1-open
>     Default locale: en_US, platform encoding: UTF-8
>     OS name: "linux", version: "5.15.0-72-generic", arch: "amd64", family: "unix"

## Build and run project

    $ mvn spring-boot:run

## Using application framework
Register new user:

    localhost:8080/register

Login & Authenticate user:

    localhost:8080/login

Show existing users (once Authenticated):

    localhost:8080/users

## Viewing DB contents

DB console:

    localhost:8080/h2-console/

*user: sa*  
*password: // blank*

Show all users:

    SELECT * FROM USERS;
    SELECT * FROM APP_USER_AUTH_USER;
    SELECT * FROM APPUSER;
    SELECT * FROM AUTHORITIES;
