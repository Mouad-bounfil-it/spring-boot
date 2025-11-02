package com.banque.service;

import com.banque.entity.*;

import java.util.List;

public interface BanqueService {
    
    // Client operations
    Client addClient(Client client);
    List<Client> consulterClients(String motCle);
    Client getClient(Long codeClient);
    List<Client> getAllClients();
    
    // Employee operations
    Employe addEmploye(Employe emp, Long codeSup);
    Employe getEmploye(Long codeEmploye);
    List<Employe> getAllEmployes();
    
    // Group operations
    Groupe addGroupe(Groupe groupe);
    void addEmployeToGroupe(Long codeEmp, Long codeGroupe);
    Groupe getGroupe(Long codeGroupe);
    List<Groupe> getAllGroupes();
    
    // Account operations
    Compte addCompte(Compte compte, Long codeClient, Long codeEmploye);
    Compte consulterCompte(String codeCompte);
    List<Compte> getComptesByClient(Long codeClient);
    List<Compte> getComptesByEmploye(Long codeEmploye);
    List<Compte> getAllComptes();
    
    // Banking operations
    void versement(String codeCompte, double montant, Long codeEmploye);
    void retrait(String codeCompte, double montant, Long codeEmploye);
    void virement(String codeCompte1, String codeCompte2, double montant, Long codeEmploye);
    
    // Operation queries
    List<Operation> consulterOperations(String codeCompte);
    Operation getOperation(Long numOperation);
}

