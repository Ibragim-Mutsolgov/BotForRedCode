package com.tsecho.bots.database;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

//Идентификатор для публикаци работы
@Entity
@Data
public class WorkSendId {
    @Id
    String idSendWork;
}
