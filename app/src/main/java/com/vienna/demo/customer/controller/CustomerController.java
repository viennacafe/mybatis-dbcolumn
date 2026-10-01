package com.vienna.demo.customer.controller;

import com.vienna.demo.customer.dto.CustomerAliasDto;
import com.vienna.demo.customer.dto.CustomerDto;
import com.vienna.demo.customer.dto.CustomerResultMapDto;
import com.vienna.demo.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // 예제 1: DTO 어노테이션으로 한글 컬럼 ↔ 영문 필드를 매핑
    @GetMapping("/{id}")
    public CustomerDto findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @GetMapping
    public List<CustomerDto> findAll() {
        return customerService.findAll();
    }

    // 예제 2: resultMap으로 한글 컬럼 ↔ 영문 필드를 매핑
    @GetMapping("/resultmap/{id}")
    public CustomerResultMapDto findByIdUsingResultMap(@PathVariable Long id) {
        return customerService.findByIdUsingResultMap(id);
    }

    @GetMapping("/resultmap")
    public List<CustomerResultMapDto> findAllUsingResultMap() {
        return customerService.findAllUsingResultMap();
    }

    // 예제 3: SQL AS 별칭으로 한글 컬럼 ↔ 영문 필드를 매핑
    @GetMapping("/alias/{id}")
    public CustomerAliasDto findByIdUsingAlias(@PathVariable Long id) {
        return customerService.findByIdUsingAlias(id);
    }

    @GetMapping("/alias")
    public List<CustomerAliasDto> findAllUsingAlias() {
        return customerService.findAllUsingAlias();
    }
}
