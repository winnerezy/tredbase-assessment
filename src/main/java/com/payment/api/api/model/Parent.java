package com.payment.api.api.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "parent")
public class Parent {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Double balance;

    @ManyToMany(mappedBy = "parents")
    private List<Student> students;
}