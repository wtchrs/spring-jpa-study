package study.typeconverter.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class IpPort {

    private String ip;
    private Integer port;

    public IpPort(String ip) {
        this.ip = ip;
    }

    public IpPort(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }
}
