package study.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import study.typeconverter.type.IpPort;

import java.util.regex.Pattern;

@Slf4j
public class StringToIpPortConverter implements Converter<String, IpPort> {

    private static final Pattern IP_PORT_PATTERN = Pattern.compile(
            "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}(:([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5]))?$");

    @Override
    public IpPort convert(String source) {
        log.info("[String to IpPort convert] source = {}", source);

        if (!IP_PORT_PATTERN.matcher(source).matches()) throw new IllegalArgumentException("Not an ip port format");
        String[] split = source.split(":");

        if (split.length == 1) return new IpPort(split[0]);
        return new IpPort(split[0], Integer.valueOf(split[1]));
    }
}
