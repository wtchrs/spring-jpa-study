package study.typeconverter.converter;

import org.junit.jupiter.api.Test;
import study.typeconverter.type.IpPort;

import static org.assertj.core.api.Assertions.assertThat;

public class ConverterTest {

    @Test
    void stringToInteger() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer result = converter.convert("100");

        assertThat(result).isEqualTo(100);
    }

    @Test
    void integerToString() {
        IntegerToStringConverter converter = new IntegerToStringConverter();
        String result = converter.convert(100);

        assertThat(result).isEqualTo("100");
    }

    @Test
    void stringToIpPort() {
        StringToIpPortConverter converter = new StringToIpPortConverter();
        IpPort ipOnly = converter.convert("127.0.0.1");
        IpPort ipAndPort = converter.convert("127.0.0.1:8080");

        assertThat(ipOnly).isNotNull();
        assertThat(ipOnly.getIp()).isEqualTo("127.0.0.1");
        assertThat(ipOnly.getPort()).isNull();

        assertThat(ipAndPort).isNotNull();
        assertThat(ipAndPort.getIp()).isEqualTo("127.0.0.1");
        assertThat(ipAndPort.getPort()).isEqualTo(8080);
    }

    @Test
    void ipPortToString() {
        IpPortToStringConverter converter = new IpPortToStringConverter();
        String ipOnly = converter.convert(new IpPort("127.0.0.1"));
        String ipAndPort = converter.convert(new IpPort("127.0.0.1", 8080));

        assertThat(ipOnly).isEqualTo("127.0.0.1");
        assertThat(ipAndPort).isEqualTo("127.0.0.1:8080");
    }
}
