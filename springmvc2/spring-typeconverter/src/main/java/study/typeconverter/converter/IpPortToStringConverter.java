package study.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import study.typeconverter.type.IpPort;

@Slf4j
public class IpPortToStringConverter implements Converter<IpPort, String> {

    @Override
    public String convert(IpPort source) {
        log.info("[IpPort to String convert] source = {}", source);
        return source.getIp() + ":" + source.getPort();
    }
}
