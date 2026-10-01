package com.vienna.demo.customer.mapper;

import com.vienna.demo.customer.dto.CustomerAliasDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SQL의 AS 별칭으로 한글 컬럼명을 영문 필드에 매핑하는 예제
 * (CustomerMapper.xml의 findByIdUsingAlias / findAllUsingAlias)를 검증합니다.
 */
@SpringBootTest
class CustomerAliasMapperTest {

    @Autowired
    private CustomerMapper customerMapper;

    @Test
    void 단건조회시_AS별칭을_통해_한글컬럼이_영문필드에_매핑된다() {
        CustomerAliasDto customer = customerMapper.findByIdUsingAlias(1002L);

        assertThat(customer).isNotNull();
        assertThat(customer.getCustomerId()).isEqualTo(1002L);
        assertThat(customer.getCustomerName()).isEqualTo("김철수");
        assertThat(customer.getBirthDate()).isEqualTo(LocalDate.of(1985, 3, 21));
        assertThat(customer.getEnabled()).isFalse();
    }

    @Test
    void 전체조회시_AS별칭을_통해_모든_행이_매핑된다() {
        List<CustomerAliasDto> customers = customerMapper.findAllUsingAlias();

        assertThat(customers).hasSize(2);
        assertThat(customers)
                .extracting(CustomerAliasDto::getCustomerId)
                .containsExactly(1001L, 1002L);

        CustomerAliasDto first = customers.get(0);
        assertThat(first.getCustomerName()).isEqualTo("홍길동");
        assertThat(first.getBirthDate()).isEqualTo(LocalDate.of(1990, 5, 10));
        assertThat(first.getEnabled()).isTrue();
    }

    @Test
    void 존재하지_않는_ID는_null을_반환한다() {
        CustomerAliasDto customer = customerMapper.findByIdUsingAlias(9999L);

        assertThat(customer).isNull();
    }
}
