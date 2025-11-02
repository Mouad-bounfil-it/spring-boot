package com.banque.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeEmploye;
    
    @Column(nullable = false)
    private String nomEmploye;
    
    // Self-referencing relationship: An employee can have a superior
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_employe_sup")
    @JsonIgnore
    private Employe employeSup;
    
    // Many-to-Many with Groupe
    @ManyToMany(mappedBy = "employes", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Groupe> groupes = new ArrayList<>();
    
    // Accounts managed by this employee
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Compte> comptes = new ArrayList<>();
    
    // Operations performed by this employee
    @OneToMany(mappedBy = "employe", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Operation> operations = new ArrayList<>();
}

