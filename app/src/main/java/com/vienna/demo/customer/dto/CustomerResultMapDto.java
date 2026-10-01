package com.vienna.demo.customer.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * {@code @DbColumn} 없이 MyBatis {@code resultMap}으로만
 * DB의 한글 컬럼명을 영문 필드에 매핑하는 예제용 DTO입니다.
 *
 * 실제 컬럼 ↔ 필드 매핑은 CustomerMapper.xml의
 * {@code customerResultMap} 정의를 참고하세요.
 */
@Getter
@Setter
@ToString
public class CustomerResultMapDto {

    private Long customerId;

    private String customerName;

    private LocalDate birthDate;

    private Boolean enabled;
}
