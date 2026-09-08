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
- [ ] UserRegisterRequest (login, password, age)
- [ ] UserResponse (id, login, age, role) — без пароля!
- [ ] UserMapper (MapStruct для конвертации UserEntity ↔ DTO)

## 🎯 Эндпоинты (UserController)
- [ ] POST /users — регистрация (публичный, только USER)
    - [ ] Проверка уникальности login
    - [ ] Хеширование пароля (пока через BCrypt, но без Security)
    - [ ] Роль всегда USER
    - [ ] Возврат UserResponse (без пароля)
- [ ] POST /users/auth — логин (публичный)
    - [ ] Проверка credentials
    - [ ] Генерация JWT
    - [ ] Возврат JwtResponse
- [ ] GET /users/{userId} — получение пользователя (защищённый)
    - [ ] Проверка, что пользователь существует
    - [ ] Возврат UserResponse

## 🔒 Spring Security
- [x] Добавить зависимость spring-boot-starter-security
- [ ] Создать SecurityConfig с @EnableWebSecurity
- [ ] Настроить PasswordEncoder (BCryptPasswordEncoder)
- [ ] Настроить фильтры:
    - [ ] Отключить CSRF (для JWT)
    - [ ] sessionManagement(STATELESS)
    - [ ] httpBasic отключить
    - [ ] formLogin отключить
    - [ ] добавить JwtFilter перед UsernamePasswordAuthenticationFilter

## 🎫 JWT
- [ ] Создать JwtService/TokenProvider
    - [ ] Генерация токена (access-only, без refresh)
    - [ ] Валидация токена
    - [ ] Извлечение userId и role из токена
- [ ] Создать JwtFilter (OncePerRequestFilter)
    - [ ] Извлечение Bearer token из заголовка
    - [ ] Валидация токена
    - [ ] Создание Authentication
    - [ ] Установка в SecurityContextHolder
    - [ ] Пропуск запроса дальше по цепочке

## 🛡️ Обработка ошибок безопасности
- [ ] Создать CustomAuthenticationEntryPoint (для 401)
    - [ ] Возврат ErrorMessageResponse в JSON
- [ ] Создать CustomAccessDeniedHandler (для 403)
    - [ ] Возврат ErrorMessageResponse в JSON
- [ ] Подключить в SecurityConfig:
    - [ ] exceptionHandling().authenticationEntryPoint()
    - [ ] exceptionHandling().accessDeniedHandler()

## 👑 Администратор при старте
- [ ] Создать @PostConstruct или CommandLineRunner
- [ ] Проверка: есть ли ADMIN в БД
- [ ] Если нет — создать с логином admin и паролем (захешировать)
- [ ] Убедиться, что не дублируется при повторных стартах

## 🎯 Настройка доступа (матчинг ролей)
- [ ] Публичные эндпоинты (permitAll):
    - [ ] POST /users
    - [ ] POST /users/auth
- [ ] Доступ только для аутентифицированных:
    - [ ] GET /users/{userId}
    - [ ] GET /locations
    - [ ] GET /locations/{locationId}
- [ ] Доступ только для ADMIN:
    - [ ] POST /locations
    - [ ] PUT /locations/{locationId}
    - [ ] DELETE /locations/{locationId}

## 📝 Дополнительно
- [ ] Добавить поле age в UserEntity (если ещё нет)
- [ ] Проверить, что пароль не возвращается ни в одном ответе
- [ ] Проверить, что 401/403 возвращают ErrorMessageResponse
- [ ] Проверить, что ADMIN не создаётся через регистрацию
- [ ] Проверить, что все ошибки в едином формате

## 🧪 Тестирование и проверка
- [ ] Запустить PostgreSQL через docker-compose
- [ ] Проверить регистрацию нового пользователя
- [ ] Проверить логин и получение JWT
- [ ] Проверить GET /users/{userId} без токена → 401
- [ ] Проверить GET /users/{userId} с токеном USER → 403 (если запрашивает другого)
- [ ] Проверить GET /locations с токеном USER → 200
- [ ] Проверить POST /locations с токеном USER → 403
- [ ] Проверить POST /locations с токеном ADMIN → 201
- [ ] Проверить, что пароль в БД хранится как хэш (не plain text)
- [ ] Проверить, что ADMIN создался при старте (и только один раз)
- [ ] Открыть Swagger: http://localhost:8080/swagger-ui/index.html
- [ ] Сверить поведение с OpenAPI-контрактом

## 🔧 Возможные проблемы (держать в уме)
- [ ] Не забыть добавить age в UserEntity
- [ ] Не забыть про уникальность login при регистрации
- [ ] Не путать 401 (нет аутентификации) и 403 (нет прав)
- [ ] В JWT класть userId и role (не весь объект)
- [ ] Фильтр должен пропускать запрос дальше, даже если токена нет
- [ ] AuthenticationEntryPoint должен возвращать JSON, а не HTML