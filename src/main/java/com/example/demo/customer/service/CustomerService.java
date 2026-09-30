package com.example.demo.customer.service;

import com.example.demo.customer.dto.CustomerAliasDto;
import com.example.demo.customer.dto.CustomerDto;
import com.example.demo.customer.dto.CustomerResultMapDto;
import com.example.demo.customer.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerMapper customerMapper;

    public CustomerDto findById(Long id) {
        return customerMapper.findById(id);
    }

    public List<CustomerDto> findAll() {
        return customerMapper.findAll();
    }

    public CustomerResultMapDto findByIdUsingResultMap(Long id) {
        return customerMapper.findByIdUsingResultMap(id);
    }

    public List<CustomerResultMapDto> findAllUsingResultMap() {
        return customerMapper.findAllUsingResultMap();
    }

    public CustomerAliasDto findByIdUsingAlias(Long id) {
        return customerMapper.findByIdUsingAlias(id);
    }

    public List<CustomerAliasDto> findAllUsingAlias() {
        return customerMapper.findAllUsingAlias();
    }
}
