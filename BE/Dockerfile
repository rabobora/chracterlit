# base image
FROM openjdk:17-jdk-slim
# 타임존을 Asia/Seoul로 설정
ENV TZ Asia/Seoul
# 타임존 데이터 설치
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
# 변수설정 (빌드파일 경로)
ARG JAR_FILE=/build/libs/*.jar
# 빌드파일을 컨테이너로 복사
COPY ${JAR_FILE} app.jar
# 외부 호스트 8080 포트로 노출
EXPOSE 8080
# jar 파일 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]