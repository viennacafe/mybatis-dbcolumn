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

```bash
curl http://localhost:8080/customers
```

```bash
curl http://localhost:8080/customers/1001
```

예상 결과:

```json
{
  "customerId": 1001,
  "customerName": "홍길동",
  "birthDate": "1990-05-10",
  "enabled": true
}
```

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
