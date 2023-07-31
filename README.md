# Spring Boot 3.1.2 JPA application-user creation & session management + LDAP user authentication & authorizaton

## Requirements

    $ java -version

>     java version "17.0.7" 2023-04-18 LTS
>     Java(TM) SE Runtime Environment (build 17.0.7+8-LTS-224)
>     Java HotSpot(TM) 64-Bit Server VM (build 17.0.7+8-LTS-224, mixed mode, sharing)

    $ mvn -version
>     Apache Maven 3.9.2 (c9616018c7a021c1c39be70fb2843d6f5f9b8a1c)
>     Maven home: ~/.sdkman/candidates/maven/current
>     Java version: 17.0.7, vendor: Oracle Corporation, runtime: C:\Users\m259408\.sdkman\candidates\java\current
>     Default locale: en_US, platform encoding: UTF-8
>     OS name: "linux", version: "5.15.0-72-generic", arch: "amd64", family: "unix"

## Update src/main/resources/application.properties username and password

    spring.ldap.password=<USER_PASSWORD>
    spring.ldap.username=CN=<USER_ACCOUNT>,OU=Normal,OU=WorkAccounts,DC=mfad,DC=mfroot,DC=org
    
## Build and run project

    $ mvn spring-boot:run

## Using application framework

Login & Authenticate LDAP user:

    localhost:8080/login

Register new user:

    localhost:8080/register

Show existing users (once Authenticated):

    localhost:8080/users

## Viewing DB contents

DB console:

    localhost:8080/h2-console/

*user: sa*  
*password: // blank*

Show all users:

    SELECT * FROM USERS;
    SELECT * FROM APPUSER_AUTHUSER;
    SELECT * FROM APPUSER;
    SELECT * FROM AUTHORITIES;

## Auto imported dependencies reference

    $ mvn dependency:tree
    
>     +- org.springframework.boot:spring-boot-devtools:jar:3.1.2:compile
>     |  +- org.springframework.boot:spring-boot:jar:3.1.2:compile
>     +- org.springframework.boot:spring-boot-configuration-processor:jar:3.1.2:compile
>     |  +- org.springframework:spring-web:jar:6.0.11:compile
>     |  \- org.springframework:spring-webmvc:jar:6.0.11:compile
>     +- org.springframework.boot:spring-boot-starter:jar:3.1.2:compile
>     |  +- org.springframework:spring-core:jar:6.0.11:compile
>     +- org.springframework.boot:spring-boot-starter-security:jar:3.1.2:compile
>     +- org.springframework.security:spring-security-ldap:jar:6.1.2:compile
>     |  +- org.springframework.security:spring-security-core:jar:6.1.2:compile
>     |  \- org.springframework.ldap:spring-ldap-core:jar:3.1.0:compile

