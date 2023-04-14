package study.typeconverter;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import study.typeconverter.converter.IpPortToStringConverter;
import study.typeconverter.converter.StringToIpPortConverter;
import study.typeconverter.formatter.NumberFormatter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Converters and formatters are not applied to {@link org.springframework.http.converter.HttpMessageConverter}. If
     * you want to use formatter in {@link org.springframework.http.converter.HttpMessageConverter}, please check
     * "Jackson data format" or else.
     * They are only applied to fields annotated with following annotations:
     * <ul>
     * <li>{@link org.springframework.web.bind.annotation.RequestParam}</li>
     * <li>{@link org.springframework.web.bind.annotation.ModelAttribute}</li>
     * <li>{@link org.springframework.web.bind.annotation.PathVariable}</li>
     * </ul>
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
        registry.addFormatter(new NumberFormatter());
    }
}
