package com.tsecho.bots.database;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class WebID {
    @Id
    String idWeb;
}
