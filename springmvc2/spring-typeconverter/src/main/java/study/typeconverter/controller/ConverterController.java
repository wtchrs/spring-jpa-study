package study.typeconverter.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import study.typeconverter.type.IpPort;

@Controller
public class ConverterController {

    @GetMapping("/converter-view")
    public String converterView(Model model) {
        model.addAttribute("number", 10000);
        model.addAttribute("ipPort", new IpPort("127.0.0.1", 8080));
        return "converter-view";
    }

    @GetMapping("/converter-form")
    public String converterForm(Model model) {
        model.addAttribute("form", new Form(new IpPort("127.0.0.1", 8080)));
        return "converter-form";
    }

    @PostMapping("/converter-form")
    public String converterFormProcess(@ModelAttribute Form form, Model model) {
        model.addAttribute("ipPort", form.getIpPort());
        return "converter-view";
    }

    @Getter
    @Setter
    static class Form {

        private IpPort ipPort;

        public Form(IpPort ipPort) {
            this.ipPort = ipPort;
        }
    }
}
