package study.typeconverter.formatter;

import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;
import study.typeconverter.converter.IpPortToStringConverter;
import study.typeconverter.converter.StringToIpPortConverter;
import study.typeconverter.type.IpPort;

import static org.assertj.core.api.Assertions.assertThat;

public class FormattingConversionServiceTest {

    @Test
    void formattingConversionService() {
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());
        conversionService.addFormatter(new NumberFormatter());

        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isNotNull();
        assertThat(ipPort.getIp()).isEqualTo("127.0.0.1");
        assertThat(ipPort.getPort()).isEqualTo(8080);

        Long number = conversionService.convert("1,000", Long.class);
        assertThat(number).isEqualTo(1000L);

        String numberStr = conversionService.convert(1000, String.class);
        assertThat(numberStr).isEqualTo("1,000");
    }
}
