package com.example.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Test
    void complete() throws NotEnoughMoneyException {
        Order order = new Order();
        order.setUsername("Bob");

        orderService.order(order);

        log.info("order.getId() = {}", order.getId());

        Order saved = orderRepository.findById(order.getId()).orElseThrow();
        assertThat(saved.getUsername()).isEqualTo("Bob");
        assertThat(saved.getPayStatus()).isEqualTo(PayStatus.COMPLETE);
    }

    @Test
    void runtimeException() {
        Order order = new Order();
        order.setUsername("Exception");

        assertThatThrownBy(() -> orderService.order(order))
                .isExactlyInstanceOf(RuntimeException.class);

        log.info("order.getId() = {}", order.getId());

        Optional<Order> saved = orderRepository.findById(order.getId());
        assertThat(saved.isPresent()).isFalse();
    }

    @Test
    void notEnoughMoneyException() {
        Order order = new Order();
        order.setUsername("John");

        assertThatThrownBy(() -> orderService.order(order))
                .isExactlyInstanceOf(NotEnoughMoneyException.class);

        log.info("order.getId() = {}", order.getId());

        Optional<Order> savedOptional = orderRepository.findById(order.getId());
        assertThat(savedOptional.isPresent()).isTrue();

        Order saved = savedOptional.get();
        assertThat(saved.getUsername()).isEqualTo("John");
        assertThat(saved.getPayStatus()).isEqualTo(PayStatus.WAIT);
    }

}