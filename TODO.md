# Итерация 2: User Management и Авторизация

## 🗄️ Модель данных и БД
- [x] Миграция Liquibase (таблица `users` с полями: id, login, password_hash, role)
- [x] UserRole enum (USER, ADMIN) в папке model/
- [x] UserEntity (JPA-сущность)
    - [x] Поля: id, login, passwordHash, role, age
    - [x] @Enumerated(EnumType.STRING) для role
    - [x] unique = true для login
    - [x] @PrePersist/@PreUpdate валидация
- [x] UserRepository (JpaRepository)

## 📦 DTO и маппинг
- [x] UserRegisterRequest (login, password, age)
- [x] UserResponse (id, login, age, role) — без пароля!
- [x] UserMapper (MapStruct для конвертации UserEntity ↔ DTO)

## 🎯 Эндпоинты (UserController)
- [x] POST /users — регистрация (публичный, только USER)
    - [x] Проверка уникальности login
    - [x] Хеширование пароля (пока через BCrypt, но без Security)
    - [x] Роль всегда USER
    - [x] Возврат UserResponse (без пароля)
- [x] POST /users/auth — логин (публичный)
    - [x] Проверка credentials
    - [x] Генерация JWT
    - [x] Возврат JwtResponse
- [x] GET /users/{userId} — получение пользователя (защищённый)
    - [x] Проверка, что пользователь существует
    - [x] Возврат UserResponse

## 🔒 Spring Security
- [x] Добавить зависимость spring-boot-starter-security
- [x] Создать SecurityConfig с @EnableWebSecurity
- [x] Настроить PasswordEncoder (BCryptPasswordEncoder)
- [x] Не возвращать e.getMessage() в ошибках безопасности, чтобы не было утечек, только фиксированные сообщения
- [x] Настроить фильтры:
    - [x] Отключить CSRF (для JWT)
    - [x] sessionManagement(STATELESS)
    - [x] httpBasic отключить
    - [x] formLogin отключить
    - [x] добавить JwtFilter перед UsernamePasswordAuthenticationFilter

## 🎫 JWT
- [x] Создать JwtService/TokenProvider
    - [x] Генерация токена (access-only, без refresh)
    - [x] Валидация токена
    - [x] Извлечение userId и role из токена
    - [x] Понять надо ли пароль доставать?
- [x] Создать JwtFilter (OncePerRequestFilter)
    - [x] Извлечение Bearer token из заголовка
    - [x] Валидация токена
    - [x] Создание Authentication
    - [x] Установка в SecurityContextHolder
    - [x] Пропуск запроса дальше по цепочке

## 🛡️ Обработка ошибок безопасности
- [x] Создать CustomAuthenticationEntryPoint (для 401)
    - [x] Возврат ErrorMessageResponse в JSON
- [x] Создать CustomAccessDeniedHandler (для 403)
    - [x] Возврат ErrorMessageResponse в JSON
- [x] Подключить в SecurityConfig:
    - [x] exceptionHandling().authenticationEntryPoint()
    - [x] exceptionHandling().accessDeniedHandler()

## 👑 Администратор при старте
- [x] Создать @PostConstruct или CommandLineRunner
- [x] Проверка: есть ли ADMIN в БД
- [x] Если нет — создать с логином admin и паролем (захэшировать)
- [x] Убедиться, что не дублируется при повторных стартах

## 🎯 Настройка доступа (матчинг ролей)
- [x] Публичные эндпоинты (permitAll):
    - [x] POST /users
    - [x] POST /users/auth
- [x] Доступ только для аутентифицированных:
    - [x] GET /users/{userId}
    - [x] GET /locations
    - [x] GET /locations/{locationId}
- [x] Доступ только для ADMIN:
    - [x] POST /locations
    - [x] PUT /locations/{locationId}
    - [x] DELETE /locations/{locationId}

## 📝 Дополнительно
- [x] Добавить поле age в UserEntity (если ещё нет)
- [x] Проверить, что пароль не возвращается ни в одном ответе
- [x] Проверить, что 401/403 возвращают ErrorMessageResponse
- [x] Проверить, что ADMIN не создаётся через регистрацию
- [x] Проверить, что все ошибки в едином формате
- [x] Понять, почему не работают переменные окружения (пароль админа тоже добавить туда)

## 🧪 Тестирование и проверка
- [x] Запустить PostgreSQL через docker-compose
- [x] Проверить регистрацию нового пользователя
- [x] Проверить логин и получение JWT
- [x] Проверить GET /users/{userId} без токена → 401
- [x] Проверить GET /users/{userId} с токеном USER → 403 (если запрашивает другого)
- [x] Проверить GET /locations с токеном USER → 200
- [x] Проверить POST /locations с токеном USER → 403
- [x] Проверить POST /locations с токеном ADMIN → 201
- [x] Проверить, что пароль в БД хранится как хэш (не plain text)
- [x] Проверить, что ADMIN создался при старте (и только один раз)
- [x] Открыть Swagger: http://localhost:8080/swagger-ui/index.html
- [x] Сверить поведение с OpenAPI-контрактом

## 🔧 Возможные проблемы (держать в уме)
- [x] Не забыть добавить age в UserEntity
- [x] Не забыть про уникальность login при регистрации
- [x] Не путать 401 (нет аутентификации) и 403 (нет прав)
- [x] В JWT класть userId и role (не весь объект)
- [x] Фильтр должен пропускать запрос дальше, даже если токена нет
- [x] AuthenticationEntryPoint должен возвращать JSON, а не HTML