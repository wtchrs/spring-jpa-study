package com.example.springmvc.basic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogTestController {

    private final Logger log = LoggerFactory.getLogger(getClass()); // or class level annotation @Slf4j

    @GetMapping("/log-test")
    public String logTest() {
        String name = "Spring";

        log.trace("name = {}", name);
        log.debug("name = {}", name);
        log.info("name = {}", name);
        log.warn("name = {}", name);
        log.error("name = {}", name);

        return "ok";
    }
}
