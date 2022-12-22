RUN apt-get update && apt-get -y install --no-install-recommends

# загрузить официальный базовый образ
FROM node:17-alpine3.14

# создание директории приложения
WORKDIR /usr/src/app

RUN npm install --production