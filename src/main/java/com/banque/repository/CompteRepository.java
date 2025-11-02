package com.banque.repository;

import com.banque.entity.Compte;
import com.banque.entity.Client;
import com.banque.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompteRepository extends JpaRepository<Compte, String> {
    
    // Find accounts by client
    List<Compte> findByClient(Client client);
    
    // Find accounts by client ID
    List<Compte> findByClientCodeClient(Long codeClient);
    
    // Find accounts by employee
    List<Compte> findByEmploye(Employe employe);
    
    // Find accounts by employee ID
    List<Compte> findByEmployeCodeEmploye(Long codeEmploye);
}

