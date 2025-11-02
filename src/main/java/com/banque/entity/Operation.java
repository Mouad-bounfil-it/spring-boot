package com.banque.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDate;

@Entity
@Table(name = "operations")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_operation", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Versement.class, name = "V"),
    @JsonSubTypes.Type(value = Retrait.class, name = "R")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Operation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numOperation;
    
    @Column(nullable = false)
    private LocalDate dateOperation;
    
    @Column(nullable = false)
    private double montant;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "num_compte", nullable = false)
    @JsonBackReference("compte-operations")
    private Compte compte;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code_employe", nullable = false)
    private Employe employe;
}

