package com.banque.service;

import com.banque.entity.*;
import com.banque.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BanqueServiceImpl implements BanqueService {
    
    private final ClientRepository clientRepository;
    private final EmployeRepository employeRepository;
    private final GroupeRepository groupeRepository;
    private final CompteRepository compteRepository;
    private final OperationRepository operationRepository;
    
    // ==================== CLIENT OPERATIONS ====================
    
    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }
    
    @Override
    public List<Client> consulterClients(String motCle) {
        if (motCle == null || motCle.trim().isEmpty()) {
            return clientRepository.findAll();
        }
        return clientRepository.findByMotCle(motCle);
    }
    
    @Override
    public Client getClient(Long codeClient) {
        return clientRepository.findById(codeClient)
                .orElseThrow(() -> new RuntimeException("Client not found with code: " + codeClient));
    }
    
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
    
    // ==================== EMPLOYEE OPERATIONS ====================
    
    @Override
    public Employe addEmploye(Employe emp, Long codeSup) {
        if (codeSup != null) {
            Employe employeSup = employeRepository.findById(codeSup)
                    .orElseThrow(() -> new RuntimeException("Superior employee not found with code: " + codeSup));
            emp.setEmployeSup(employeSup);
        }
        return employeRepository.save(emp);
    }
    
    @Override
    public Employe getEmploye(Long codeEmploye) {
        return employeRepository.findById(codeEmploye)
                .orElseThrow(() -> new RuntimeException("Employee not found with code: " + codeEmploye));
    }
    
    @Override
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }
    
    // ==================== GROUP OPERATIONS ====================
    
    @Override
    public Groupe addGroupe(Groupe groupe) {
        return groupeRepository.save(groupe);
    }
    
    @Override
    public void addEmployeToGroupe(Long codeEmp, Long codeGroupe) {
        Employe employe = getEmploye(codeEmp);
        Groupe groupe = getGroupe(codeGroupe);
        
        if (!groupe.getEmployes().contains(employe)) {
            groupe.getEmployes().add(employe);
            groupeRepository.save(groupe);
        }
    }
    
    @Override
    public Groupe getGroupe(Long codeGroupe) {
        return groupeRepository.findById(codeGroupe)
                .orElseThrow(() -> new RuntimeException("Group not found with code: " + codeGroupe));
    }
    
    @Override
    public List<Groupe> getAllGroupes() {
        return groupeRepository.findAll();
    }
    
    // ==================== ACCOUNT OPERATIONS ====================
    
    @Override
    public Compte addCompte(Compte compte, Long codeClient, Long codeEmploye) {
        Client client = getClient(codeClient);
        Employe employe = getEmploye(codeEmploye);
        
        compte.setClient(client);
        compte.setEmploye(employe);
        compte.setDateCreation(LocalDate.now());
        
        return compteRepository.save(compte);
    }
    
    @Override
    public Compte consulterCompte(String codeCompte) {
        return compteRepository.findById(codeCompte)
                .orElseThrow(() -> new RuntimeException("Account not found with code: " + codeCompte));
    }
    
    @Override
    public List<Compte> getComptesByClient(Long codeClient) {
        return compteRepository.findByClientCodeClient(codeClient);
    }
    
    @Override
    public List<Compte> getComptesByEmploye(Long codeEmploye) {
        return compteRepository.findByEmployeCodeEmploye(codeEmploye);
    }
    
    @Override
    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }
    
    // ==================== BANKING OPERATIONS ====================
    
    @Override
    public void versement(String codeCompte, double montant, Long codeEmploye) {
        if (montant <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        Compte compte = consulterCompte(codeCompte);
        Employe employe = getEmploye(codeEmploye);
        
        // Create deposit operation
        Versement versement = new Versement();
        versement.setMontant(montant);
        versement.setDateOperation(LocalDate.now());
        versement.setCompte(compte);
        versement.setEmploye(employe);
        
        // Update account balance
        compte.setSolde(compte.getSolde() + montant);
        
        // Save operation and update account
        operationRepository.save(versement);
        compteRepository.save(compte);
    }
    
    @Override
    public void retrait(String codeCompte, double montant, Long codeEmploye) {
        if (montant <= 0) {
            throw new RuntimeException("Amount must be positive");
        }
        
        Compte compte = consulterCompte(codeCompte);
        Employe employe = getEmploye(codeEmploye);
        
        // Check balance constraints
        double newSolde = compte.getSolde() - montant;
        
        if (compte instanceof CompteCourant) {
            CompteCourant cc = (CompteCourant) compte;
            if (newSolde < -cc.getDecouvert()) {
                throw new RuntimeException("Insufficient balance. Overdraft limit exceeded.");
            }
        } else if (compte instanceof CompteEpargne) {
            if (newSolde < 0) {
                throw new RuntimeException("Insufficient balance. Savings account cannot be negative.");
            }
        }
        
        // Create withdrawal operation
        Retrait retrait = new Retrait();
        retrait.setMontant(montant);
        retrait.setDateOperation(LocalDate.now());
        retrait.setCompte(compte);
        retrait.setEmploye(employe);
        
        // Update account balance
        compte.setSolde(newSolde);
        
        // Save operation and update account
        operationRepository.save(retrait);
        compteRepository.save(compte);
    }
    
    @Override
    public void virement(String codeCompte1, String codeCompte2, double montant, Long codeEmploye) {
        // Transfer = withdrawal from account1 + deposit to account2
        retrait(codeCompte1, montant, codeEmploye);
        versement(codeCompte2, montant, codeEmploye);
    }
    
    // ==================== OPERATION QUERIES ====================
    
    @Override
    public List<Operation> consulterOperations(String codeCompte) {
        return operationRepository.findByCompteNumCompte(codeCompte);
    }
    
    @Override
    public Operation getOperation(Long numOperation) {
        return operationRepository.findById(numOperation)
                .orElseThrow(() -> new RuntimeException("Operation not found with number: " + numOperation));
    }
}

