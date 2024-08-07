from openjdk:17
copy /target/spring-boot-docker.jar  spring-boot-docker.jar
Entrypoint ["java","-jar","spring-boot-docker-0.0.1-SNAPSHOT.jar"]
export 8080