package com.example.demo.customer.controller;

import com.example.demo.customer.dto.CustomerDto;
import com.example.demo.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public CustomerDto findById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @GetMapping
    public List<CustomerDto> findAll() {
        return customerService.findAll();
    }
}
