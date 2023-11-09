package com.example.springtx.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void order(Order order) throws NotEnoughMoneyException {
        log.info("[OrderService.order]");
        orderRepository.save(order);

        log.info("[Start payment process]");

        if (order.getUsername().equals("Exception")) {
            log.info("[System exception occurs]");
            throw new RuntimeException("System exception occurs");
        }

        if (order.getUsername().equals("John")) {
            log.info("[Insufficient balance]");
            order.setPayStatus(PayStatus.WAIT);
            throw new NotEnoughMoneyException("Insufficient balance");
        }

        log.info("[Normal approval]");
        order.setPayStatus(PayStatus.COMPLETE);

        log.info("[Complete payment process]");
    }

}
