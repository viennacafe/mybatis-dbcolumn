package com.example.demo.customer.mapper;

import com.example.demo.customer.dto.CustomerResultMapDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * resultMap으로 한글 컬럼명을 영문 필드에 매핑하는 예제
 * (CustomerMapper.xml의 customerResultMap)를 검증합니다.
 */
@SpringBootTest
class CustomerResultMapMapperTest {

    @Autowired
    private CustomerMapper customerMapper;

    @Test
    void 단건조회시_resultMap을_통해_한글컬럼이_영문필드에_매핑된다() {
        CustomerResultMapDto customer = customerMapper.findByIdUsingResultMap(1001L);

        assertThat(customer).isNotNull();
        assertThat(customer.getCustomerId()).isEqualTo(1001L);
        assertThat(customer.getCustomerName()).isEqualTo("홍길동");
        assertThat(customer.getBirthDate()).isEqualTo(LocalDate.of(1990, 5, 10));
        assertThat(customer.getEnabled()).isTrue();
    }

    @Test
    void 전체조회시_resultMap을_통해_모든_행이_매핑된다() {
        List<CustomerResultMapDto> customers = customerMapper.findAllUsingResultMap();

        assertThat(customers).hasSize(2);
        assertThat(customers)
                .extracting(CustomerResultMapDto::getCustomerId)
                .containsExactly(1001L, 1002L);

        CustomerResultMapDto second = customers.get(1);
        assertThat(second.getCustomerName()).isEqualTo("김철수");
        assertThat(second.getBirthDate()).isEqualTo(LocalDate.of(1985, 3, 21));
        assertThat(second.getEnabled()).isFalse();
    }

    @Test
    void 존재하지_않는_ID는_null을_반환한다() {
        CustomerResultMapDto customer = customerMapper.findByIdUsingResultMap(9999L);

        assertThat(customer).isNull();
    }
}
