package org.example.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Album extends Item {

    private String artist;
    private String etc;
}
