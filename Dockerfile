#FROM openjdk:17
#ARG JAR_FILE=presentation/build/libs/*.jar
#ARG DEBIAN_FRONTEND=noninteractive
#ENV TZ=Asia/Seoul
#COPY ${JAR_FILE} app.jar
#
#ENTRYPOINT ["java","-jar","/app.jar"]

# 빌드 단계
FROM openjdk:17-alpine as build
WORKDIR /presentation
COPY . .
RUN ./gradlew clean build

# 실행 단계
FROM openjdk:17-alpine
WORKDIR /presentation
COPY --from=build /presentation/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
