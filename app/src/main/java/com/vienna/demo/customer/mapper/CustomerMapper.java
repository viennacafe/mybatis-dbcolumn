package com.vienna.demo.customer.mapper;

import com.vienna.demo.customer.dto.CustomerAliasDto;
import com.vienna.demo.customer.dto.CustomerDto;
import com.vienna.demo.customer.dto.CustomerResultMapDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {

    CustomerDto findById(@Param("customerId") Long customerId);

    List<CustomerDto> findAll();

    // resultMap으로 한글 컬럼 ↔ 영문 필드를 매핑하는 예제
    CustomerResultMapDto findByIdUsingResultMap(@Param("customerId") Long customerId);

    List<CustomerResultMapDto> findAllUsingResultMap();

    // SQL의 AS 별칭으로 한글 컬럼 ↔ 영문 필드를 매핑하는 예제
    CustomerAliasDto findByIdUsingAlias(@Param("customerId") Long customerId);

    List<CustomerAliasDto> findAllUsingAlias();
}
