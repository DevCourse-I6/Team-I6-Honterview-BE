#빌드된 JAR 파일을 실행하기 위한 새로운 Docker 이미지
FROM openjdk:17

# 작업 디렉토리 설정
WORKDIR /app

ARG JAR_PATH=build/libs
# 1단계에서 빌드한 JAR 파일을 현재 이미지로 복사
COPY ${JAR_PATH}/honterview-0.0.1-SNAPSHOT.jar honterview.jar

# 컨테이너가 시작될 때 애플리케이션 실행
ENTRYPOINT ["java","-Duser.timezone=Asia/Seoul","-jar","app.jar"]
