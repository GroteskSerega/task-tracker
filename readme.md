# Task Tracker: High-Performance AOT Reference
Микросервис управления задачами с ролевой моделью доступа (RBAC), оптимизированный для запуска в среде **Axiom NIK**.

## Core Features
- **RBAC Model**: Предустановленный ADMIN для инициализации системы, поддержка ролей MODERATOR и USER.
- **Async Engine**: Полностью неблокирующий стек на базе Spring WebFlux.
- **Cloud Native**: Полная готовность к нативной компиляции и работе в Read-Only файловых системах.

## Build & Deployment
Проект поддерживает стандартный цикл сборки Gradle и контейнеризацию:

```bash
# Сборка артефакта
./gradlew bootJar
```
```bash
docker build -t task-tracker .
```

## Environment Variables

| Переменная         | Описание                         | Дефолтное значение                              |
|--------------------|----------------------------------|-------------------------------------------------|
| MONGODB_URI        | Строка подключения к MongoDB     | mongodb://root:root@localhost:27017/appdatabase |
| APP_SERVER_PORT    | Порт публикации сервиса          | 8080                                            |
| APP_ADMIN_USERNAME | Логин системного администратора  | admin                                           |          
| APP_ADMIN_PASSWORD | Пароль системного администратора | admin                                           |

## Deployment Options

Приложение подготовлено к работе в различных окружениях: от локальной разработки до высокоплотных кластеров.

### 1. Bare Metal / Local JRE
ля запуска классического JAR-файла (используется Axiom JDK 21+):
```bash
java -jar task-tracker.jar --spring.config.import=optional:file:./application.yml
```

### 2. Docker (Standalone)
```bash
docker run --rm -p 8080:8080 \
  --name task-tracker-svc \
  -e MONGODB_HOST=host.docker.internal \
  -e MONGODB_USER=root \
  -e MONGODB_PASSWORD=root \
  task-tracker
```

### 3. Docker Compose (Full Stack)
```bash
# Развертывание стека в фоновом режиме
docker-compose up -d

# Мониторинг логов
docker-compose logs -f app
```

### 4. Native Image Execution (Axiom NIK)
```bash
./task-tracker
```

# Продвинутый блок (исследования)
## Сборка проекта с разными Docker-образами

### 1. Слоеная сборка (Temurin)
Оптимизированная слоеная сборка.
**Benefit**: Разделение зависимостей и прикладного кода позволяет эффективно использовать Docker Cache, минимизируя трафик при CI/CD.

```
docker build -t tracker:layers .
```

### 2. Uber-JAR (Legacy)
Классическая сборка «все-в-одном».
**Deprecated** (2026): Избыточный размер пересылаемых данных при минимальных изменениях в коде.
```
docker build -f Dockerfile_fat_jar -t tracker:fat .
```

### 3. Axiom (Alpaquita + Musl) - Самый интересный
Оптимизированная слоеная сборка на базе Axiom образа. Разделение зависимостей и прикладного кода позволяет эффективно использовать Docker Cache, минимизируя трафик при CI/CD.
- **Base Image**: `bellsoft/liberica-runtime-alpine` (или Alpaquita).
- **Benefit**: Баланс между скоростью сборки и эффективностью кеширования.
```
docker build -f Dockerfile_axiom -t tracker:axiom .
```

### 4. Axiom (musl) Native Image Kit (NIK)
Вершина оптимизации: компиляция в нативный бинарник с использованием **musl libc**.
- **Build Metrics**: ~9.8 мин (на i7-3720QM). Требует 8GB+ RAM для фазы анализа графа объектов.
- **Startup**: **0.548s** (Ready to serve).
- **Security**: Исполнение в среде **Alpaquita Cloud Native OS** от не-привилегированного пользователя (Non-root).
- **Verdict**: Идеально для масштабируемых микросервисов и Serverless.
```
docker build -f Dockerfile_axiom_native -t tracker:native .
```

### 5. Vanilla (debian:bookworm-slim) Native Image Kit (NIK)
Сравнение с эталонным GraalVM на базе GLIBC (Debian Slim).
- **Build Metrics**: ~16.8 мин (в 1.7 раза медленнее Axiom).
- **Startup**: **0.435s** (Агрессивная оптимизация ценой времени сборки).
- **Issues**: Нестабильность сетевых загрузок при сборке и увеличенный размер итогового образа из-за веса Debian-слоев
```
docker build -f Dockerfile_axiom_native -t tracker:vanila-native .
```

## Сводный результат
### Результаты всех сборок. Статистика по работе в Docker контейнерах.
Ниже приведён результат собранных образов, размеры.
- Размеры образов:

| REPOSITORY | TAG           | IMAGE ID     | CREATED        | SIZE  |
|------------|---------------|--------------|----------------|-------|
| tracker    | axiom         | bbf0665b55ac | 9 minutes ago  | 262MB |
| tracker    | layers        | a197b88cc74c | 12 minutes ago | 360MB |
| tracker    | fat           | 97717199662b | 15 minutes ago | 359MB |
| tracker    | native(gcc)   | d6b94d41b13a | 5 minutes ago  | 751MB |
| tracker    | native(musl)  | 8ef2b5645c5a | 5 minutes ago  | 199MB |
| tracker    | vanila-native | 8496efc15f49 | 7 minutes ago  | 313MB |

- Ниже представлен результат команды Docker stat (1-й запуск).

Внимание заслуживают показатели MEM USAGE и PIDS (количество системных потоков)
<p> 1-й запуск:

| CONTAINER ID  | NAME               | CPU % | MEM USAGE / LIMIT   | MEM % | NET I/O         | BLOCK I/O     | PIDS |
|---------------|--------------------|-------|---------------------|-------|-----------------|---------------|------|
| 37584a4b3733  | tracker-axiom      | 0.22% | 256.6MiB / 15.58GiB | 1.61% | 1.17kB / 126B   | 0B / 172kB    | 47   |
| 8936e3f0dd69  | tracker-layers     | 1.90% | 195.5MiB / 15.58GiB | 1.22% | 1.17kB / 126B   | 0B / 152kB    | 47   |
| e2c77b0e046f  | tracker-fat        | 0.21% | 237.3MiB / 15.58GiB | 1.49% | 1.17kB / 126B   | 0B / 172kB    | 49   |
| f41c55b36121  | tracker-native-run | 0.00% | 59.91MiB / 15.58GiB | 0.38% | 1.17kB / 126B   | 0B / 102kB    | 26   |
| ce367618ee6d  | task-tracker-app   | 0.00% | 87.12MiB / 15.58GiB | 0.55% | 4.33kB / 4.19kB | 4.1kB / 102kB | 19   |
| 40cff549a5bd  | task-tracker-app   | 0.00% | 85.63MiB / 15.58GiB | 0.54% | 4.11kB / 3.89kB | 109MB / 102kB | 14   |

- Ниже представлен результат команды Docker stat (2-й запуск).

Внимание заслуживают показатели MEM USAGE и PIDS (количество системных потоков)
<p> 2-й запуск:

| CONTAINER ID | NAME               | CPU % | MEM USAGE / LIMIT   | MEM % | NET I/O         | BLOCK I/O  | PIDS |
|--------------|--------------------|-------|---------------------|-------|-----------------|------------|------|
| 37584a4b3733 | tracker-axiom      | 1.87% | 257.7MiB / 15.58GiB | 1.61% | 1.17kB / 126B   | 0B / 135kB | 47   |
| 8936e3f0dd69 | tracker-layers     | 1.20% | 199.7MiB / 15.58GiB | 1.25% | 1.17kB / 126B   | 0B / 135kB | 47   |
| e2c77b0e046f | tracker-fat        | 2.09% | 240.5MiB / 15.58GiB | 1.51% | 1.17kB / 126B   | 0B / 143kB | 49   |
| f41c55b36121 | tracker-native-run | 0.00% | 59.89MiB / 15.58GiB | 0.38% | 1.17kB / 126B   | 0B / 102kB | 26   |
| ce367618ee6d | task-tracker-app   | 0.00% | 74.47MiB / 15.58GiB | 0.47% | 4.2kB / 3.44kB  | 0B / 102kB | 14   |
| 40cff549a5bd | task-tracker-app   | 0.00% | 74.46MiB / 15.58GiB | 0.47% | 3.43kB / 3.19kB | 0B / 102kB | 14   |

Необходимо отметить, что скрость запуска образов с JRE составила 2,5 - 5 секунд
Скорость запуска нативных образов - 0,5 секунд

### Итоги в виде таблицы
JRE vs Axiom NIK vs Vanilla Native
Конфигурация: Java 21, Spring Boot 3.5.x, WebFlux, MongoDB. Железо: Intel i7-3720QM (4/8), 16GB RAM.

| Метрика          | JRE (Axiom/Temurin) | Axiom NIK (Musl) | Vanilla Native (GLIBC) | Лучший результат                |
|------------------|---------------------|------------------|------------------------|---------------------------------|
| Время сборки     | ~0.5 мин            | ~9.8 мин         | ~16.8 мин              | Axiom NIK (среди Native)        |
| Размер образа    | ~360 MB             | 199 MB           | 313 MB                 | Axiom NIK (-45% от JRE)         |
| Старт (Startup)  | ~3.500 сек          | 0.548 сек        | 0.435 сек              | Vanilla Native (рекорд)         |
| RAM (Idle)       | ~256 MiB            | 59.9 MiB         | 85.6 MiB               | Axiom NIK                       |
| RAM (Active)     | ~240 MiB            | 74.4 MiB         | 74.4 MiB               | Паритет                         |
| Потоки (PIDS)    | 47-49               | 14               | 14                     | Native Image (в 3.5 раза легче) |
| ОС (Runtime)     | Alpine              | Alpaquita        | Debian-slim            | Alpaquita (самый легкий)        |

### Краткие выводы
- Эффективность рантайма: Оба нативных бинарника (Axiom и Vanilla) показали идентичное потребление памяти в работе (74.4 MiB) и одинаковое количество потоков (14). Это доказывает, что сама технология AOT в 2026 году достигла пика оптимизации ресурсов.
- Скорость «холодного» старта: Vanilla Native (Oracle) оказалась чуть агрессивнее оптимизирована, выиграв у Axiom около 110 мс. Это может быть критично для сверхчувствительных Serverless-функций.
- Инфраструктурная выгода: Axiom NIK безоговорочно побеждает в размере образа и времени сборки. Разница в 114 MB (между Alpaquita и Debian) — это колоссальная экономия трафика и места в Registry при масштабировании на тысячи узлов.
- Сборка: Axiom собрала проект почти в 2 раза быстрее, чем Vanilla, что делает её более пригодной для активного CI/CD цикла.

### Экономические выводы
- Serverless Ready: Приложение можно запускать в режиме "Scale to Zero" (поднимать только при поступлении запроса), так как оно стартует мгновенно.
- Density (Плотность): На одном сервере теперь можно запустить в 4 раза больше инстансов этого сервиса.
- Cloud Costs: Прямая экономия на аренде мощностей в облаке (Axiom Cloud / Yandex Cloud) за счет минимальных лимитов по RAM.