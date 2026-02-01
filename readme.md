# Описание проекта
Проект предназначен для ведения задач, упрвление задачами, отслеживанием статусов задач.

# Сборка проекта
- Запустить gradle сборку через команду Gradle -> Tasks -> build -> bootJar
- В каталоге build/libs будет jar файл
- Для сборки docker образа необходимо использовать команду в терминале проекта:
```
docker build -t task-tracker .
```

# Подготовка к запуску проекта
- Создать каталог C:/apps/task-tracker
- Разместить в каталоге jar файл
- Разместить в каталоге файл application.yml

## Сконфигурировать параметры приложения в application.yml
```yaml
spring:
  application:
    name: task-tracker
  data:
    mongodb:
      # Пользователь, пароль, хост Mongodb, порт, имя БД
      uri: mongodb://${MONGODB_USER:root}:${MONGODB_PASSWORD:root}@${MONGODB_HOST:localhost}:${MONGODB_PORT:27017}/${MONGODB_DBNAME:appdatabase}?authSource=admin

server:
  port: ${APP_SERVER_PORT:8080}
```

# Запуск проекта
- Запуск приложения на локальной машине:
```
java -jar -Dspring.config.location=application.yml task-tracker-0.0.1-SNAPSHOT.jar
```
- Запуск приложения в Docker контейнере:
```
docker run --rm -e MONGODB_HOST=localhost -e MONGODB_PORT=27017 -e MONGODB_DBNAME=appdatabase -e MONGODB_USER=root -e MONGODB_PASSWORD=root -e APP_SERVER_PORT=8080 task-tracker
```
- Запуск приложения с помощью Docker-compose:
```
docker-compose up
```

- Остановка приложения с помощью Docker-compose:
```
docker-compose down
```
