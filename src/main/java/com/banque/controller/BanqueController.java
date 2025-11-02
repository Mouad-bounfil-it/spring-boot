package com.banque.controller;

import com.banque.entity.*;
import com.banque.service.BanqueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Banque API", description = "API for banking operations")
@CrossOrigin("*")
public class BanqueController {
    
    private final BanqueService banqueService;
    
    // ==================== CLIENT ENDPOINTS ====================
    
    @PostMapping("/clients")
    @io.swagger.v3.oas.annotations.Operation(summary = "Add a new client")
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        Client savedClient = banqueService.addClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }
    
    @GetMapping("/clients")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get all clients")
    public ResponseEntity<List<Client>> getAllClients() {
        return ResponseEntity.ok(banqueService.getAllClients());
    }
    
    @GetMapping("/clients/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get client by ID")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(banqueService.getClient(id));
    }
    
    @GetMapping("/clients/search")
    @io.swagger.v3.oas.annotations.Operation(summary = "Search clients by keyword")
    public ResponseEntity<List<Client>> searchClients(@RequestParam(required = false) String q) {
        return ResponseEntity.ok(banqueService.consulterClients(q));
    }
    
    // ==================== EMPLOYEE ENDPOINTS ====================
    
    @PostMapping("/employes")
    @io.swagger.v3.oas.annotations.Operation(summary = "Add a new employee")
    public ResponseEntity<Employe> addEmploye(
            @RequestBody Employe employe,
            @RequestParam(required = false) Long codeSup) {
        Employe savedEmploye = banqueService.addEmploye(employe, codeSup);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmploye);
    }
    
    @GetMapping("/employes")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get all employees")
    public ResponseEntity<List<Employe>> getAllEmployes() {
        return ResponseEntity.ok(banqueService.getAllEmployes());
    }
    
    @GetMapping("/employes/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get employee by ID")
    public ResponseEntity<Employe> getEmploye(@PathVariable Long id) {
        return ResponseEntity.ok(banqueService.getEmploye(id));
    }
    
    // ==================== GROUP ENDPOINTS ====================
    
    @PostMapping("/groupes")
    @io.swagger.v3.oas.annotations.Operation(summary = "Add a new group")
    public ResponseEntity<Groupe> addGroupe(@RequestBody Groupe groupe) {
        Groupe savedGroupe = banqueService.addGroupe(groupe);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGroupe);
    }
    
    @GetMapping("/groupes")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get all groups")
    public ResponseEntity<List<Groupe>> getAllGroupes() {
        return ResponseEntity.ok(banqueService.getAllGroupes());
    }
    
    @GetMapping("/groupes/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get group by ID")
    public ResponseEntity<Groupe> getGroupe(@PathVariable Long id) {
        return ResponseEntity.ok(banqueService.getGroupe(id));
    }
    
    @PostMapping("/groupes/{groupeId}/employes/{empId}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Add employee to group")
    public ResponseEntity<String> addEmployeToGroupe(
            @PathVariable Long groupeId,
            @PathVariable Long empId) {
        banqueService.addEmployeToGroupe(empId, groupeId);
        return ResponseEntity.ok("Employee added to group successfully");
    }
    
    // ==================== ACCOUNT ENDPOINTS ====================
    
    @PostMapping("/comptes")
    @io.swagger.v3.oas.annotations.Operation(summary = "Create a new account")
    public ResponseEntity<Compte> addCompte(
            @RequestBody Compte compte,
            @RequestParam Long codeClient,
            @RequestParam Long codeEmploye) {
        Compte savedCompte = banqueService.addCompte(compte, codeClient, codeEmploye);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCompte);
    }
    
    @GetMapping("/comptes")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get all accounts")
    public ResponseEntity<List<Compte>> getAllComptes() {
        return ResponseEntity.ok(banqueService.getAllComptes());
    }
    
    @GetMapping("/comptes/{code}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get account by code")
    public ResponseEntity<Compte> consulterCompte(@PathVariable String code) {
        return ResponseEntity.ok(banqueService.consulterCompte(code));
    }
    
    @GetMapping("/comptes/{code}/operations")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get all operations for an account")
    public ResponseEntity<List<com.banque.entity.Operation>> consulterOperations(@PathVariable String code) {
        return ResponseEntity.ok(banqueService.consulterOperations(code));
    }
    
    @GetMapping("/clients/{id}/comptes")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get all accounts for a client")
    public ResponseEntity<List<Compte>> getComptesByClient(@PathVariable Long id) {
        return ResponseEntity.ok(banqueService.getComptesByClient(id));
    }
    
    @GetMapping("/employes/{id}/comptes")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get all accounts managed by an employee")
    public ResponseEntity<List<Compte>> getComptesByEmploye(@PathVariable Long id) {
        return ResponseEntity.ok(banqueService.getComptesByEmploye(id));
    }
    
    // ==================== BANKING OPERATIONS ENDPOINTS ====================
    
    @PostMapping("/operations/versement")
    @io.swagger.v3.oas.annotations.Operation(summary = "Make a deposit")
    public ResponseEntity<String> versement(@RequestBody Map<String, Object> request) {
        String codeCompte = (String) request.get("codeCompte");
        double montant = ((Number) request.get("montant")).doubleValue();
        Long codeEmploye = ((Number) request.get("codeEmploye")).longValue();
        
        banqueService.versement(codeCompte, montant, codeEmploye);
        return ResponseEntity.ok("Deposit successful");
    }
    
    @PostMapping("/operations/retrait")
    @io.swagger.v3.oas.annotations.Operation(summary = "Make a withdrawal")
    public ResponseEntity<String> retrait(@RequestBody Map<String, Object> request) {
        String codeCompte = (String) request.get("codeCompte");
        double montant = ((Number) request.get("montant")).doubleValue();
        Long codeEmploye = ((Number) request.get("codeEmploye")).longValue();
        
        banqueService.retrait(codeCompte, montant, codeEmploye);
        return ResponseEntity.ok("Withdrawal successful");
    }
    
    @PostMapping("/operations/virement")
    @io.swagger.v3.oas.annotations.Operation(summary = "Make a transfer between accounts")
    public ResponseEntity<String> virement(@RequestBody Map<String, Object> request) {
        String codeCompte1 = (String) request.get("codeCompte1");
        String codeCompte2 = (String) request.get("codeCompte2");
        double montant = ((Number) request.get("montant")).doubleValue();
        Long codeEmploye = ((Number) request.get("codeEmploye")).longValue();
        
        banqueService.virement(codeCompte1, codeCompte2, montant, codeEmploye);
        return ResponseEntity.ok("Transfer successful");
    }
    
    // ==================== OPERATION QUERIES ====================
    
    @GetMapping("/operations/{id}")
    @io.swagger.v3.oas.annotations.Operation(summary = "Get operation by ID")
    public ResponseEntity<com.banque.entity.Operation> getOperation(@PathVariable Long id) {
        return ResponseEntity.ok(banqueService.getOperation(id));
    }
    
    // ==================== EXCEPTION HANDLER ====================
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
}

