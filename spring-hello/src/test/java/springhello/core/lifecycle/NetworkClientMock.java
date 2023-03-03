package springhello.core.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class NetworkClientMock {

    private String url;

    public NetworkClientMock() {
        System.out.println("Constructor: url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void connect() {
        System.out.println("connect: " + url);
    }

    public void call(String message) {
        System.out.println("call: " + url + ", message = " + message);
    }

    public void disconnect() {
        System.out.println("disconnect: " + url);
    }

    @PostConstruct
    public void init() {
        System.out.println("NetworkClientMock.init");
        connect();
        call("Initialization connect message");
    }

    @PreDestroy
    public void close() {
        System.out.println("NetworkClientMock.close");
        disconnect();
    }
}
