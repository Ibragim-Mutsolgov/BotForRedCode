package com.tsecho.bots.database;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

//Идентификатор для отклика на работу
@Entity
@Data
public class WorkResponseId {
    @Id
    String idResponseWork;
}
