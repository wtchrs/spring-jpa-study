package springhello.core.singleton;

public class StatefulService {

    // should not use mutable member variable(stateful)
    private int price;

    void order(String user, int price) {
        this.price = price;
    }

    int getPrice() {
        return price;
    }
}
