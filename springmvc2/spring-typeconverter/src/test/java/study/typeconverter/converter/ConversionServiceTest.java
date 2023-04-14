package study.typeconverter.converter;

import org.junit.jupiter.api.Test;
import org.springframework.core.convert.support.DefaultConversionService;
import study.typeconverter.type.IpPort;

import static org.assertj.core.api.Assertions.assertThat;

public class ConversionServiceTest {

    @Test
    void conversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new IntegerToStringConverter());
        conversionService.addConverter(new StringToIntegerConverter());
        conversionService.addConverter(new IpPortToStringConverter());
        conversionService.addConverter(new StringToIpPortConverter());

        assertThat(conversionService.convert("10", Integer.class)).isEqualTo(10);
        assertThat(conversionService.convert(10, String.class)).isEqualTo("10");

        IpPort ipPortResult = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPortResult).isNotNull();
        assertThat(ipPortResult.getIp()).isEqualTo("127.0.0.1");
        assertThat(ipPortResult.getPort()).isEqualTo(8080);

        String strResult = conversionService.convert(ipPortResult, String.class);
        assertThat(strResult).isEqualTo("127.0.0.1:8080");
    }
}
