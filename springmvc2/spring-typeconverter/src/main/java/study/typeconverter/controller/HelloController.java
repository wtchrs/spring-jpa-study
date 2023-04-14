package study.typeconverter.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import study.typeconverter.type.IpPort;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
        String data = request.getParameter("data");
        Integer value = Integer.valueOf(data);
        log.info("data = {}", value);
        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        log.info("data = {}", data);
        return "ok";
    }

    @GetMapping("/string-to-ipport")
    public IpPort stringToIpPort(@RequestParam IpPort ipPort) {
        log.info("ipPort = {}", ipPort);
        return ipPort;
    }
}
