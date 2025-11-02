package com.banque.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comptes")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_compte", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CompteCourant.class, name = "CC"),
    @JsonSubTypes.Type(value = CompteEpargne.class, name = "CE")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Compte {
    
    @Id
    private String numCompte;
    
    @Column(nullable = false)
    private LocalDate dateCreation;
    
    @Column(nullable = false)
    private double solde;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code_client", nullable = false)
    @JsonBackReference("client-comptes")
    private Client client;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code_employe", nullable = false)
    private Employe employe;
    
    @OneToMany(mappedBy = "compte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("compte-operations")
    private List<Operation> operations = new ArrayList<>();
}

