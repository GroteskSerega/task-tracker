# Базовый образ, содержащий Java
# FROM openjdk:27-ea-trixie
FROM eclipse-temurin:21-jre-alpine

# Директория приложения внутри контейнера
WORKDIR /app

# Копирование JAR-файла приложения в контейнер
COPY build/libs/task-tracker-0.0.1-SNAPSHOT.jar app.jar

# Определение переменной среды
ENV APP_SERVER_PORT=8080
ENV MONGODB_HOST=localhost
ENV MONGODB_PORT=27017
ENV MONGODB_DBNAME=appdatabase
ENV MONGODB_USER=root
ENV MONGODB_PASSWORD=root

# Команда для запуска приложения
CMD ["java", "-jar", "app.jar"]