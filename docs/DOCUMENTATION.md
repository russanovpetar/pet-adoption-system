# Pet Adoption System – Документация

## Функционалност

Система за осиновяване на домашни любимци. Потребителите се регистрират като ADOPTER, SHELTER_STAFF или ADMIN. Приютът (shelter) има животни (Pet); адоптерите подават кандидатури за осиновяване (AdoptionApplication), които шелтър персоналът одобрява или отхвърля. Има блог с постове (Post) и оценки (PostRating). Администраторът управлява приюти, потребители и може да изтрива приют (при липса на животни). Качване на снимки за животни, списъци за преглед и филтриране.

## Архитектура

Класически трислойен дизайн: презентация (UI + REST API), бизнес логика (services), достъп до данни (repositories). REST контролерите са под `/api/`, HTML страниците се обслужват от UI контролери под `/ui/`, шаблоните са Thymeleaf. Spring Security управлява аутентикация (форма) и авторизация по роли.

## Реализация

- **controller/** – REST API за pets, posts, adoptions, shelters, users, auth, shelter staff.
- **ui/** – Thymeleaf контролери за начална страница, логин/регистрация, списъци и детайли за приюти, животни, постове, кандидатури, админ и staff страници.
- **service/** – бизнес логика за всяка от областите; валидации и изключения (напр. ShelterHasPetsException при опит за изтриване на приют с животни).
- **repository/** – JpaRepository за User, Shelter, Pet, Post, PostRating, AdoptionApplication.
- **dto/request**, **dto/response** – входни/изходни обекти за API.
- **exception/** – домейн изключения (UserNotFoundException, ShelterNotFoundException и др.), обработвани в контролерите с подходящи HTTP отговори.
- **config/** – SecurityConfig (правила за пътища и роли), PasswordConfig (BCrypt), WebConfig, CustomUserDetailsService, AdminBootstrap (първоначален админ при стартиране).

## Модел на данните

- **User** – username, password, Role (ADOPTER, SHELTER_STAFF, ADMIN), опционална връзка към Shelter.
- **Shelter** – name, location.
- **Pet** – name, species, age, adopted, imagePath, createdAt; ManyToOne към Shelter.
- **AdoptionApplication** – pet, adopter (User), status (PENDING/APPROVED/REJECTED), message, appliedAt.
- **Post** – title, content, author (User), createdAt.
- **PostRating** – оценка от потребител към пост (съответната връзка в entity).

Релации: User N:1 Shelter; Pet N:1 Shelter; AdoptionApplication N:1 Pet, N:1 User; Post N:1 User.

## Конфигурация

- **application.properties**: H2 in-memory БД (`jdbc:h2:mem:petdb`), Hibernate `ddl-auto=update`, H2 конзола на `/h2-console`, име на приложението, настройки за качване на файлове (pet.upload-dir, max-file-size 10MB).
- **Security**: публични пътища – начална, auth, статични ресурси, GET за постове и животни; останалите изискват логване; методова сигурност с `@PreAuthorize` за админ операции (напр. създаване/изтриване приют).

## Използвани технологии и библиотеки

- Java 17, Spring Boot 3.5.8.
- Spring Web, Spring Data JPA, Spring Security, Spring Validation.
- Thymeleaf, thymeleaf-extras-springsecurity6.
- H2 (in-memory).
- Maven за зависимости и сборка.

## Проблеми и решения

- **Изтриване на приют**: приют с животни не може да се изтрие поради FK ограничения и семантика; в сервиса се проверява дали има pets и се хвърля ShelterHasPetsException; преди изтриване се откачат потребителите от приюта (shelter = null).
- **Достъп до API и UI**: публични GET за списъци/детайли за постове и животни; останалите операции изискват аутентикация и при нужда роля ADMIN/SHELTER_STAFF.
- **H2 конзола**: frameOptions sameOrigin, за да работи конзолата в браузър; CSRF се изключва за `/api/**` и `/h2-console/**` според изискванията на приложението.

## Стартиране на приложението

**Изисквания:** Java 17, Maven.

- **С Maven (от корена на проекта):**
  ```
    mvn spring-boot:ru
  ```
  На Windows: `mvnw.cmd spring-boot:run`

- **От IDE:** стартирай главния клас `pet.adoption.system.PetAdoptionSystemApplication` (Run).

След стартиране приложението е достъпно на `http://localhost:8080`. Начална страница и вход: `/ui/home`, `/ui/auth/login`. H2 конзола (за разработка): `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:petdb`, user: `sa`, password: празно).

## Референции

- [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Thymeleaf](https://www.thymeleaf.org/documentation.html)
- [H2 Database](https://www.h2database.com/html/main.html)
