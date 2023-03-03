package org.example.domain;

import lombok.Getter;
import lombok.Setter;
import org.example.domain.item.Item;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "orders_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;

    private int count;
}
