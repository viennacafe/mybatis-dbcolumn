package com.example.demo.customer.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * {@code @DbColumn}이나 {@code resultMap} 없이,
 * SQL의 {@code AS} 별칭만으로 DB의 한글 컬럼명을 영문 필드에
 * 매핑하는 예제용 DTO입니다.
 *
 * 별칭이 필드명과 정확히 일치하므로 MyBatis의 기본
 * {@code resultType} 매핑만으로 바인딩됩니다.
 * 실제 별칭은 CustomerMapper.xml의 select 구문을 참고하세요.
 */
@Getter
@Setter
@ToString
public class CustomerAliasDto {

    private Long customerId;

    private String customerName;

    private LocalDate birthDate;

    private Boolean enabled;
}
