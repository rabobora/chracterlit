# Build Stage
FROM node:20.10.0 as build

WORKDIR /app
# 작업 디렉토리를 /app으로 설정
COPY package.json /app/
#현재 디렉토리의 package.json 파일을 컨테이너 내의 /app/ 디렉토리로 복사
# EXPOSE 5173
RUN npm install
# npm을 사용하여 애플리케이션의 종속성을 설치
COPY . /app/
# 재 디렉토리의 모든 파일을 컨테이너 내의 /app/ 디렉토리로 복사
RUN npm run build
# npm 스크립트를 실행하여 애플리케이션을 빌드

ENV TZ=Asia/Seoul

#Runtime Stage
FROM nginx:alpine

RUN rm -rf /etc/nginx/conf.d/default.conf
COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf
#NGINX의 기본 설정 파일을 컨테이너 내의 /etc/nginx/conf.d/ 디렉토리로 복사

RUN rm -rf /usr/share/nginx/html/*
COPY --from=build /app/dist /usr/share/nginx/html
# 빌드 단계에서 생성된 정적 파일을 NGINX가 서비스하는 디렉토리로 복사
EXPOSE 5173
# 호스트와 컨테이너 간의 포트 5173 노출
CMD ["nginx", "-g", "daemon off;"]
# 컨테이너가 시작될 때 NGINX를 실행하고 NGINX가 백그라운드에서 동작