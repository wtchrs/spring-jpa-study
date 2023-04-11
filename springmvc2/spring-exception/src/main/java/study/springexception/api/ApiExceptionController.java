package study.springexception.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import study.springexception.exception.BadRequestException;
import study.springexception.exception.UserException;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiExceptionController {

    /**
     * bad -> {@link study.springexception.resolver.MyHandlerExceptionResolver}<br>
     * user-ex -> {@link study.springexception.resolver.UserHandlerExceptionResolver}
     */
    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable("id") String memberId) {
        switch (memberId) {
            case "ex" -> throw new RuntimeException("Wrong user");
            case "bad" -> throw new IllegalArgumentException("Bad argument");
            case "user-ex" -> throw new UserException("User exception");
        }

        return new MemberDto(memberId, "Hello, " + memberId);
    }

    /**
     * {@link org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver}
     */
    @GetMapping("/response-status-ex1")
    public String responseStatusEx1() {
        throw new BadRequestException();
    }

    /**
     * {@link org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver}
     */
    @GetMapping("/response-status-ex2")
    public String responseStatusEx2() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "error.bad", new IllegalArgumentException());
    }

    /**
     * {@link org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver}
     */
    @GetMapping("/default-handler-exception")
    public String defaultHandlerEx(@RequestParam Integer num) {
        return "ok";
    }
}
