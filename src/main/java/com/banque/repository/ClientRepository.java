package com.banque.repository;

import com.banque.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    // Search clients by name (case-insensitive)
    @Query("SELECT c FROM Client c WHERE LOWER(c.nomClient) LIKE LOWER(CONCAT('%', :motCle, '%')) OR LOWER(c.prenomClient) LIKE LOWER(CONCAT('%', :motCle, '%'))")
    List<Client> findByMotCle(@Param("motCle") String motCle);
    
    // Find by exact name
    List<Client> findByNomClientContainingIgnoreCase(String nom);
}

