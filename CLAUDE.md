# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Review Queue is a web service that helps users periodically review learned content. Users create study records, log daily learning with keywords, and configure review schedules. The system sends reminders via Server-Sent Events (SSE) when review dates arrive.

**Tech Stack:**
- Java 17, Spring Boot 3.3.5, Spring Data JPA
- MySQL, Redis
- QueryDSL for complex queries
- JWT authentication with email/password
- SSE for real-time notifications
- Docker, Jenkins

## Build and Test Commands

**Build the project:**
```bash
./gradlew build
```

**Run all tests:**
```bash
./gradlew test
```

**Run a single test class:**
```bash
./gradlew test --tests "com.example.reviewqueue.study.service.StudyServiceTest"
```

**Run a specific test method:**
```bash
./gradlew test --tests "com.example.reviewqueue.study.service.StudyServiceTest.testMethodName"
```

**Run the application (local profile):**
```bash
./gradlew bootRun
```

**Run with specific profile:**
```bash
./gradlew bootRun --args='--spring.profiles.active=test'
```

## Architecture Overview

### Domain Model Hierarchy

The application follows a core domain flow: **Member → Study → DailyStudy → StudyKeyword → Review → ReviewReminder**

1. **Member**: Users authenticated via email/password
2. **Study**: Top-level study container (types: 강의/COURSE, 도서/BOOK, 코테/ALGORITHM)
3. **DailyStudy**: Daily learning record within a Study
4. **StudyKeyword**: Keywords and detailed content for each DailyStudy
5. **Review**: Review items generated from DailyStudy based on review conditions (frequency: 0-5 times, interval: 1-14 days)
6. **ReviewReminder**: Scheduled notifications sent daily at 5:00 AM for reviews due that day

### Package Structure

Each domain follows a consistent layered structure:
```
domain/
├── controller/      # REST endpoints
├── service/         # Business logic
│   └── dto/        # Service-layer DTOs
├── repository/      # Data access (JPA + QueryDSL)
├── domain/          # JPA entities
└── exception/       # Domain-specific exceptions
```

**Common package** (`com.example.reviewqueue.common/`):
- `config/`: Spring configuration (WebConfig, SecurityConfig, JPA, QueryDSL, JWT)
- `jwt/`: JWT token management (JwtManager, JwtAuthenticationFilter, handlers)
- `response/`: Standardized API responses (ResponseForm, ResponseCode, CommonResponseBodyAdvice)
- `exception/`: Global exception handling (GlobalExceptionHandler, GlobalException)
- `resolver/`: Custom argument resolvers (AuthenticatedMemberResolver for extracting member from security context)
- `util/`: Shared utilities (GlobalValidator, ResponseManager)
- `domain/`: BaseEntity with auditing fields

### Authentication Flow

1. Users register via `POST /auth/signup` or login via `POST /auth/login` (AuthController)
2. AuthService validates credentials using BCryptPasswordEncoder
3. Issues JWT access + refresh tokens stored in cookies (TokenService)
4. JwtAuthenticationFilter validates tokens on subsequent requests
5. AuthenticatedMemberResolver extracts authenticated member for controller methods
6. Logout via `POST /auth/logout` clears cookies

**Token Storage**: Refresh tokens stored in Redis, access tokens validated via JwtManager

### Review System Flow

1. User creates a Study (강의/도서/코테)
2. Logs DailyStudy with StudyKeywords (핵심 키워드 + 설명)
3. Sets review conditions (횟수 0-5, 주기 1-14일) → generates Review entities
4. ReviewReminderScheduler runs daily at 5:00 AM (cron: `0 0 5 * * ?`)
5. Creates ReviewReminder for reviews due that day
6. Sends SSE notifications to online users, stores reminders in DB for offline users
7. Users mark reviews as completed, removing them from review queue

**SSE Implementation**: SseService manages real-time connections with 30-minute timeout. SseEmitterRepository stores active connections per member.

## Configuration Notes

**Profiles:**
- `local`: Uses MySQL (localhost:3307), Redis (localhost:6379)
- `test`: Uses H2 in-memory database, ddl-auto: create

**CORS Configuration**: Allows `http://localhost:5173` (frontend dev server) with credentials

**Scheduled Tasks**: ReviewReminderScheduler uses Spring `@Scheduled` annotation. Ensure `@EnableScheduling` is configured.

## Code Patterns

**Exception Handling:**
- Domain-specific exceptions extend `GlobalException`
- All exceptions contain a `ResponseCode` enum
- GlobalExceptionHandler catches and formats all exceptions to ResponseForm
- Input validation errors mapped to field-level error messages

**DTO Pattern:**
- Controller DTOs in `controller/dto/` (request/response)
- Service DTOs in `service/dto/` (business logic)
- Avoid exposing JPA entities directly in API responses

**QueryDSL Usage:**
- Complex queries use QueryDSL (Q-classes generated from entities)
- Repository interfaces extend `JpaRepository` and custom QueryDSL repositories

**Testing:**
- Tests use `test` profile with H2 database
- Test data can be initialized via `src/main/resources/*.sql` files (member.sql, study.sql)

## Known TODOs

- Implement bulk INSERT for ReviewReminder using JdbcTemplate (currently uses saveAll) - see ReviewReminderScheduler:29
- Implement access token blacklist on logout (Member domain feature)
- Add filtering by StudyType (강의/도서/코테) for Study queries
