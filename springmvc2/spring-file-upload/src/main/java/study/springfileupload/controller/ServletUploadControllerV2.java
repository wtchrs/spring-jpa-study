package study.springfileupload.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String uploadForm() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request = {}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName = {}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts = {}", parts);

        for (Part part : parts) {
            log.info("======== PART(name = {}) ========", part.getName());
            log.info("Headers");
            for (String headerName : part.getHeaderNames()) {
                log.info(" {}: {}", headerName, part.getHeader(headerName));
            }
            String submittedFileName = part.getSubmittedFileName();
            log.info("submittedFileName = {}", submittedFileName);
            log.info("size = {}", part.getSize());

            if (StringUtils.hasText(submittedFileName)) {
                String filePath = fileDir + submittedFileName;
                part.write(filePath);
                log.info("saved file(filePath = {})", filePath);
            }
        }

        return "upload-form";
    }
}
