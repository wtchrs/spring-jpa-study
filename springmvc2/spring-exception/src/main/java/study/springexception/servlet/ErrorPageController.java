package study.springexception.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.RequestDispatcher.*;

@Slf4j
@Controller
@RequestMapping("/error-page")
public class ErrorPageController {

    @RequestMapping("/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("ErrorPageController.errorPage404");
        logErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response) {
        log.info("ErrorPageController.errorPage500");
        logErrorInfo(request);
        return "error-page/500";
    }

    @RequestMapping(value = "/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request, HttpServletResponse response) {
        log.info("ErrorPageController.errorPage500Api");
        Map<String, Object> result = new HashMap<>();

        Exception errorEx = (Exception) request.getAttribute(ERROR_EXCEPTION);
        Integer statusCode = (Integer) request.getAttribute(ERROR_STATUS_CODE);
        result.put("status", statusCode);
        result.put("message", errorEx.getMessage());

        return new ResponseEntity<>(result, HttpStatusCode.valueOf(statusCode));
    }

    private void logErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION = {}", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE = {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE = {}", request.getAttribute(ERROR_MESSAGE));
        log.info("ERROR_REQUEST_URI = {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME = {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE = {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("request.getDispatcherType() = {}", request.getDispatcherType());
    }
}
