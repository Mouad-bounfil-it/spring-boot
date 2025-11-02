package com.banque.repository;

import com.banque.entity.Groupe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupeRepository extends JpaRepository<Groupe, Long> {
    
    // Find group by name
    Optional<Groupe> findByNomGroupe(String nomGroupe);
    
    // Check if group exists by name
    boolean existsByNomGroupe(String nomGroupe);
}

