# @DbColumn + MyBatis ObjectWrapperFactory

Spring Boot + MyBatis에서 DB의 한글 컬럼명을 Java의 영어 필드명으로
자동 매핑하는 예제입니다.

`@DbColumn` 매핑 기능 자체는 Spring Boot 애플리케이션(`app`)과 분리된
재사용 가능한 라이브러리(`mybatis-dbcolumn-core` /
`mybatis-dbcolumn-spring-boot-starter`)로 모듈화되어 있습니다.
자세한 내용은 [모듈 구성](#모듈-구성)을 참고하세요.

## 기술 구성

- Java 25
- Spring Boot 4.1.1
- MyBatis Spring Boot Starter 4.1.0
- Lombok 1.18.48
- H2 2.5.252
- Gradle 9.8.0 (멀티 모듈)

## 핵심 DTO

```java
import com.vienna.mybatis.dbcolumn.annotation.DbColumn;

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
        resultType="com.vienna.demo.customer.dto.CustomerDto">
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

멀티 모듈 구성이라 실행 가능한 애플리케이션이 있는 `app` 모듈을 지정합니다.

Linux / macOS:

```bash
./gradlew :app:bootRun
```

Windows:

```bat
gradlew.bat :app:bootRun
```

첫 실행 시 Gradle Wrapper가 Gradle 9.8.0 배포본을 내려받습니다.

## 테스트

```bash
./gradlew test
```

(루트에서 `test`를 실행하면 테스트가 있는 `app` 모듈의 테스트까지
전체 모듈을 대상으로 실행됩니다.)

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

## 모듈 구성

`@DbColumn` 매핑 기능은 데모 애플리케이션과 무관하게 재사용할 수 있도록
2개의 라이브러리 모듈로 분리되어 있습니다. MyBatis가 `mybatis` →
`mybatis-spring` → `mybatis-spring-boot-starter`로 계층을 나누는 방식과
동일한 구조입니다.

| 모듈 | 역할 | Spring 의존 여부 |
|---|---|---|
| `mybatis-dbcolumn-core` | `@DbColumn`, `DbColumnMetadataCache`, `DbColumnObjectWrapperFactory`, `DbColumnBeanWrapper`. 순수 MyBatis 확장(`ObjectWrapperFactory`)만 포함 | 없음 (MyBatis만 의존) |
| `mybatis-dbcolumn-spring-boot-starter` | `DbColumnAutoConfiguration`으로 core를 Spring Boot에 자동 연결. 이 의존성 하나만 추가하면 `mybatis-spring-boot-starter`까지 함께 따라옴 | Spring Boot 자동설정 |
| `app` | 위 스타터를 사용하는 데모 Spring Boot 애플리케이션. Controller/Service/Mapper/DTO 등 예제 코드 | Spring Boot 애플리케이션 |

이전에는 `app`에 `MyBatisConfig`라는 `@Configuration` 클래스를 직접 만들어
`DbColumnObjectWrapperFactory` 빈을 수동으로 등록해야 했지만, 라이브러리로
분리한 뒤에는 `mybatis-dbcolumn-spring-boot-starter`의
`META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
에 등록된 `DbColumnAutoConfiguration`이 이를 자동으로 처리합니다. 그래서
`app` 모듈에는 더 이상 MyBatis 관련 설정 클래스가 없습니다.

## 사내 Nexus(Maven)에 배포하기

`mybatis-dbcolumn-core`와 `mybatis-dbcolumn-spring-boot-starter`에는
[gradle/publishing.gradle](gradle/publishing.gradle) 공통 스크립트를 통해
`maven-publish` 설정이 이미 되어 있습니다(`app`은 라이브러리가 아니라
실행용 애플리케이션이라 배포 대상에서 제외했습니다). jar와 함께
sources/javadoc jar도 자동으로 생성됩니다.

### 1) 접속 정보 설정

Nexus 주소와 인증 정보를 **프로젝트 파일이 아니라** 사용자 홈의
`~/.gradle/gradle.properties`에 적어두세요. 이 파일은 git으로 관리되지
않으므로 credential이 저장소에 올라갈 위험이 없습니다.

```properties
# ~/.gradle/gradle.properties (git에 포함되지 않음)
nexusReleasesUrl=https://nexus.company.com/repository/maven-releases/
nexusSnapshotsUrl=https://nexus.company.com/repository/maven-snapshots/
nexusUsername=deploy-user
nexusPassword=********
```

CI 환경이라면 같은 이름의 환경변수(`NEXUS_RELEASES_URL`,
`NEXUS_SNAPSHOTS_URL`, `NEXUS_USERNAME`, `NEXUS_PASSWORD`)로 대체할 수
있습니다. Nexus가 release/snapshot 저장소를 따로 두지 않고 리포지토리
하나만 쓴다면 두 URL을 동일하게 맞추면 됩니다.

현재 버전(`0.0.1-SNAPSHOT`)은 `-SNAPSHOT`으로 끝나므로 자동으로
`nexusSnapshotsUrl`로 배포됩니다. 정식 릴리스를 배포하려면
[build.gradle](build.gradle)의 `version` 값에서 `-SNAPSHOT`을 떼면
`nexusReleasesUrl`로 전환됩니다.

접속 정보가 비어 있으면 `nexus` 저장소 등록 자체를 건너뛰도록
방어 코드가 들어 있어서, 설정 전에도 `./gradlew build`나
`publishToMavenLocal`은 평소대로 동작합니다.

### 2) 배포 실행

```bash
# 로컬 ~/.m2에 먼저 테스트 삼아 배포
./gradlew publishToMavenLocal

# 실제 사내 Nexus로 배포 (두 라이브러리 모듈 전체)
./gradlew publish

# 특정 모듈만 배포하고 싶다면
./gradlew :mybatis-dbcolumn-core:publish
./gradlew :mybatis-dbcolumn-spring-boot-starter:publish
```

### 3) 사용하는 쪽 설정

프로젝트의 `settings.gradle` 또는 `build.gradle`
`repositories {}`에 같은 Nexus 주소를 추가하면 일반 의존성처럼 받아
쓸 수 있습니다.

```gradle
repositories {
    mavenCentral()
    maven { url = uri("https://nexus.company.com/repository/maven-public/") }
}

dependencies {
    implementation "com.vienna:mybatis-dbcolumn-spring-boot-starter:0.0.1-SNAPSHOT"
}
```

> 현재 `group`은 `com.vienna`입니다.

## JitPack 배포

GitHub 저장소에 코드를 push하고 Release를 만들면 JitPack이 빌드와 배포를
처리합니다. 별도의 `publish` 명령이나 Nexus 계정은 필요하지 않습니다.
루트 `jitpack.yml`은 이 프로젝트의 Java 25 빌드 환경을 준비하고,
JitPack 빌드에서는 GitHub 소유자/저장소/태그를 Maven 좌표에 사용합니다.
일반 로컬 빌드와 Nexus 배포 좌표(`com.vienna`)에는 영향이 없습니다.

예를 들어 GitHub 저장소가
`https://github.com/<소유자>/mybatis-dbcolumn`이고 태그가 `v0.0.1`이면,
JitPack에서 저장소를 조회해 해당 버전을 빌드합니다. 사용하는 프로젝트에는
다음을 추가합니다.

```gradle
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation "com.github.viennacafe.mybatis-dbcolumn:mybatis-dbcolumn-spring-boot-starter:v0.0.1"
}
```

`<소유자>`는 GitHub 사용자명 또는 조직명입니다. starter가 core 모듈을
전이 의존성으로 포함하므로 보통 starter 하나만 선언하면 됩니다.

## 프로젝트 구조

```text
.
├── build.gradle                  # 루트: 공통 플러그인/toolchain 설정
├── settings.gradle                # 모듈 include
├── gradlew / gradlew.bat
├── gradle/wrapper/
│
├── mybatis-dbcolumn-core/         # 순수 MyBatis 확장 라이브러리
│   ├── build.gradle
│   └── src/main/java/com/vienna/mybatis/dbcolumn/
│       ├── annotation/DbColumn.java
│       ├── mapping/DbColumnMetadataCache.java
│       └── wrapper/
│           ├── DbColumnObjectWrapperFactory.java
│           └── DbColumnBeanWrapper.java
│
├── mybatis-dbcolumn-spring-boot-starter/   # Spring Boot 자동설정
│   ├── build.gradle
│   └── src/main/
│       ├── java/com/vienna/mybatis/dbcolumn/autoconfigure/
│       │   └── DbColumnAutoConfiguration.java
│       └── resources/META-INF/spring/
│           └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
│
└── app/                           # 데모 Spring Boot 애플리케이션
    ├── build.gradle
    └── src/
        ├── main/
        │   ├── java/com/vienna/demo/
        │   │   ├── DbColumnMybatisDemoApplication.java
        │   │   └── customer/
        │   └── resources/
        │       ├── mapper/CustomerMapper.xml
        │       ├── application.yml
        │       ├── schema.sql
        │       └── data.sql
        └── test/java/com/vienna/demo/customer/mapper/
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
