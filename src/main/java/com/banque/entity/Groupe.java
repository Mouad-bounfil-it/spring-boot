package com.banque.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groupes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Groupe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codeGroupe;
    
    @Column(nullable = false, unique = true)
    private String nomGroupe;
    
    // Many-to-Many with Employe
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "employe_groupe",
        joinColumns = @JoinColumn(name = "code_groupe"),
        inverseJoinColumns = @JoinColumn(name = "code_employe")
    )
    @JsonIgnore
    private List<Employe> employes = new ArrayList<>();
}

