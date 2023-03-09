package study.thymeleaf.basic;

import org.springframework.stereotype.Component;

@Component("helloBean")
class HelloBean {
    public String hello(String data) {
        return "Hello" + data + "!";
    }
}
