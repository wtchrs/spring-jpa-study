package study.springexception.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.springexception.exception.UserException;

@Slf4j
@RestController
@RequestMapping("/api/v2")
public class ApiExceptionV2Controller {

    /**
     * {@link study.springexception.exhandler.advice.ExControllerAdvice}
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
}
