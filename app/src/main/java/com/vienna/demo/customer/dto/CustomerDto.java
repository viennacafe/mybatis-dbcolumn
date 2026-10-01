package com.vienna.demo.customer.dto;

import com.vienna.mybatis.dbcolumn.annotation.DbColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class CustomerDto {

    @DbColumn("고객번호")
    private Long customerId;

    @DbColumn("고객명")
    private String customerName;

    @DbColumn("생년월일")
    private LocalDate birthDate;

    @DbColumn("사용여부")
    private Boolean enabled;
}
