package com.banque.repository;

import com.banque.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Long> {
    
    // Find employees by superior
    List<Employe> findByEmployeSup(Employe employeSup);
    
    // Find employees without a superior (top-level employees)
    List<Employe> findByEmployeSupIsNull();
    
    // Search by name
    @Query("SELECT e FROM Employe e WHERE LOWER(e.nomEmploye) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Employe> findByNomContaining(@Param("nom") String nom);
}

