# @DbColumn + MyBatis ObjectWrapperFactory - Gradle Demo

Spring Boot + MyBatis에서 DB의 한글 컬럼명을 Java의 영어 필드명으로
자동 매핑하는 예제입니다.

## 기술 구성

- Java 25
- Spring Boot 4.1.1
- MyBatis Spring Boot Starter 4.1.0
- Lombok 1.18.48
- H2 2.5.252
- Gradle 9.8.0
- Gradle Wrapper

## 핵심 DTO

```java
@Getter
@Setter
public class CustomerDto {

    @DbColumn("고객번호")
    private Long customerId;

    @DbColumn("고객명")
    private String customerName;
}
```

Mapper XML은 `resultMap`이나 SQL alias 없이 일반 `resultType`을 사용합니다.

```xml
<select id="findById"
        resultType="com.example.demo.customer.dto.CustomerDto">
    SELECT
        고객번호,
        고객명,
        생년월일,
        사용여부
    FROM 고객
    WHERE 고객번호 = #{customerId}
</select>
```

## 실행

Linux / macOS:

```bash
./gradlew bootRun
```

Windows:

```bat
gradlew.bat bootRun
```

첫 실행 시 Gradle Wrapper가 Gradle 9.8.0 배포본을 내려받습니다.

## 테스트

```bash
./gradlew test
```

## API 확인

같은 데이터를 세 가지 매핑 방식으로 각각 조회할 수 있습니다.

### 1) `@DbColumn` + 커스텀 ObjectWrapperFactory

```bash
curl http://localhost:8080/customers
curl http://localhost:8080/customers/1001
```

### 2) MyBatis `resultMap`

```bash
curl http://localhost:8080/customers/resultmap
curl http://localhost:8080/customers/resultmap/1001
```

### 3) SQL `AS` 별칭

```bash
curl http://localhost:8080/customers/alias
curl http://localhost:8080/customers/alias/1001
```

세 API 모두 예상 결과는 동일합니다:

```json
{
  "customerId": 1001,
  "customerName": "홍길동",
  "birthDate": "1990-05-10",
  "enabled": true
}
```

## H2 콘솔

브라우저에서 데이터를 직접 확인하고 싶다면 `application.yml`의
`spring.h2.console.enabled: true` 설정으로 H2 웹 콘솔을 켤 수 있습니다.

```
http://localhost:8080/h2-console
```

| 항목 | 값 |
|---|---|
| JDBC URL | `jdbc:h2:mem:testdb;MODE=DB2;DEFAULT_NULL_ORDERING=HIGH;DB_CLOSE_DELAY=-1` |
| User Name | `sa` |
| Password | (공백) |

> Spring Boot 4부터 H2 콘솔 자동설정이 `spring-boot-h2console` 모듈로 분리되어,
> `build.gradle`에 `runtimeOnly 'org.springframework.boot:spring-boot-h2console'`를
> 별도로 추가해야 콘솔이 동작합니다.

## 프로젝트 구조

```text
.
├── build.gradle
├── settings.gradle
├── gradlew
├── gradlew.bat
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
└── src/
    └── main/
        ├── java/
        │   └── com/example/demo/
        │       ├── DbColumnMybatisDemoApplication.java
        │       ├── config/MyBatisConfig.java
        │       ├── customer/
        │       └── mybatis/
        └── resources/
            ├── mapper/CustomerMapper.xml
            ├── application.yml
            ├── schema.sql
            └── data.sql
```

## 매핑 흐름

```text
DB 컬럼 "고객번호"
      ↓
DbColumnObjectWrapperFactory
      ↓
DbColumnBeanWrapper.findProperty()
      ↓
@DbColumn("고객번호")
      ↓
Java property "customerId"
      ↓
CustomerDto.setCustomerId(...)
```

## 매핑 방식 비교

| 방식 | DTO | 매핑 위치 | 비고 |
|---|---|---|---|
| `@DbColumn` + 커스텀 ObjectWrapperFactory | `CustomerDto` | 애노테이션 (Java) | 이 프로젝트의 핵심 예제. SQL/XML은 alias 없이 그대로 유지 |
| `resultMap` | `CustomerResultMapDto` | `CustomerMapper.xml`의 `<resultMap>` (XML) | MyBatis 표준 방식. 컬럼-필드 매핑이 XML에 명시적으로 드러남 |
| SQL `AS` 별칭 | `CustomerAliasDto` | SQL문 자체 (XML) | 매핑 규칙이 SQL 안에 있어 가장 직관적이지만, 쿼리마다 별칭을 반복해야 함 |


