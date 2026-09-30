package com.example.demo.customer.mapper;

import com.example.demo.customer.dto.CustomerDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CustomerMapper {

    CustomerDto findById(@Param("customerId") Long customerId);

    List<CustomerDto> findAll();
}
