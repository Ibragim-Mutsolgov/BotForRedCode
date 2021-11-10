package com.tsecho.bots.database;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

//Идентификатор для удаления работы
@Entity
@Data
public class WorkDeleteId {
    @Id
    String idDeleteWork;
}
