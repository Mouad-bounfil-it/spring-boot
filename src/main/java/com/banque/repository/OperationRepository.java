package com.banque.repository;

import com.banque.entity.Compte;
import com.banque.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    
    // Find operations by account
    List<Operation> findByCompte(Compte compte);
    
    // Find operations by account number
    List<Operation> findByCompteNumCompte(String numCompte);
    
    // Find operations by account, ordered by date descending
    List<Operation> findByCompteOrderByDateOperationDesc(Compte compte);
    
    // Find operations by date range
    @Query("SELECT o FROM Operation o WHERE o.compte.numCompte = :numCompte AND o.dateOperation BETWEEN :dateDebut AND :dateFin ORDER BY o.dateOperation DESC")
    List<Operation> findByCompteAndDateRange(
        @Param("numCompte") String numCompte,
        @Param("dateDebut") LocalDate dateDebut,
        @Param("dateFin") LocalDate dateFin
    );
}

