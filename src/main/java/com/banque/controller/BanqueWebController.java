package com.banque.controller;

import com.banque.entity.Compte;
import com.banque.entity.Operation;
import com.banque.service.BanqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BanqueWebController {
    
    private final BanqueService banqueService;
    
    @GetMapping("/")
    public String home() {
        return "redirect:/banque";
    }
    
    @GetMapping("/banque")
    public String banque(Model model) {
        model.addAttribute("clients", banqueService.getAllClients());
        model.addAttribute("employes", banqueService.getAllEmployes());
        model.addAttribute("comptes", banqueService.getAllComptes());
        return "banque";
    }
    
    @GetMapping("/comptes/{code}")
    public String compteDetails(@PathVariable String code, Model model) {
        Compte compte = banqueService.consulterCompte(code);
        List<Operation> operations = banqueService.consulterOperations(code);
        
        model.addAttribute("compte", compte);
        model.addAttribute("operations", operations);
        return "compte";
    }
    
    @GetMapping("/operations")
    public String operations(
            @RequestParam(required = false) String codeCompte,
            Model model) {
        if (codeCompte != null && !codeCompte.isEmpty()) {
            List<Operation> ops = banqueService.consulterOperations(codeCompte);
            model.addAttribute("operations", ops);
            model.addAttribute("compte", banqueService.consulterCompte(codeCompte));
        }
        model.addAttribute("comptes", banqueService.getAllComptes());
        return "operation";
    }
    
    // ==================== OPERATIONS FORM ====================
    
    @GetMapping("/operations-form")
    public String operationsForm(Model model) {
        model.addAttribute("comptes", banqueService.getAllComptes());
        model.addAttribute("employes", banqueService.getAllEmployes());
        return "operations-form";
    }
    
    @PostMapping("/operations-form/versement")
    public String effectuerVersement(
            @RequestParam String codeCompte,
            @RequestParam double montant,
            @RequestParam Long codeEmploye,
            RedirectAttributes redirectAttributes) {
        try {
            banqueService.versement(codeCompte, montant, codeEmploye);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", 
                "✅ Versement de " + montant + " DH effectué avec succès sur le compte " + codeCompte);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "❌ Erreur : " + e.getMessage());
        }
        return "redirect:/operations-form";
    }
    
    @PostMapping("/operations-form/retrait")
    public String effectuerRetrait(
            @RequestParam String codeCompte,
            @RequestParam double montant,
            @RequestParam Long codeEmploye,
            RedirectAttributes redirectAttributes) {
        try {
            banqueService.retrait(codeCompte, montant, codeEmploye);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", 
                "✅ Retrait de " + montant + " DH effectué avec succès sur le compte " + codeCompte);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "❌ Erreur : " + e.getMessage());
        }
        return "redirect:/operations-form";
    }
    
    @PostMapping("/operations-form/virement")
    public String effectuerVirement(
            @RequestParam String codeCompte1,
            @RequestParam String codeCompte2,
            @RequestParam double montant,
            @RequestParam Long codeEmploye,
            RedirectAttributes redirectAttributes) {
        try {
            banqueService.virement(codeCompte1, codeCompte2, montant, codeEmploye);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", 
                "✅ Virement de " + montant + " DH effectué avec succès de " + codeCompte1 + " vers " + codeCompte2);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "❌ Erreur : " + e.getMessage());
        }
        return "redirect:/operations-form";
    }
}

