package com.vienna.mybatis.dbcolumn.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * DB의 실제 컬럼명을 자바 필드에 매핑하기 위한 애노테이션입니다.
 *
 * <pre>{@code
 * public class CustomerDto {
 *     @DbColumn("고객번호")
 *     private Long customerId;
 * }
 * }</pre>
 *
 * {@code resultMap}이나 SQL {@code AS} 별칭 없이도, MyBatis의
 * {@code ObjectWrapperFactory} 확장점을 통해 이 애노테이션 값과
 * 일치하는 컬럼을 해당 필드에 자동으로 매핑합니다.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DbColumn {

    String value();
}
