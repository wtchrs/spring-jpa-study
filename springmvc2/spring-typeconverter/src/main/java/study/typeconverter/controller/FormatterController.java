package study.typeconverter.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;

@Controller
public class FormatterController {

    @GetMapping("/formatter-form")
    public String formatterForm(Model model) {
        model.addAttribute("form", new Form(1000, LocalDateTime.now()));
        return "formatter-form";
    }

    @PostMapping("/formatter-form")
    public String formatterFormProcess(@ModelAttribute Form form) {
        return "formatter-view";
    }

    @Getter
    @Setter
    static class Form {

        @NumberFormat(pattern = "###,###")
        private Integer number;
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localDateTime;

        public Form(Integer number, LocalDateTime localDateTime) {
            this.number = number;
            this.localDateTime = localDateTime;
        }
    }
}
