package com.humanbooster.businesscase.controller;

import com.humanbooster.businesscase.model.Utilisateur;
import com.humanbooster.businesscase.model.RoleUtilisateur;
import com.humanbooster.businesscase.service.UtilisateurService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping
    public ResponseEntity<?> createUtilisateur(@Valid @RequestBody Utilisateur utilisateur,
                                               @RequestParam(required = false) Long idAdresse) {
        try {
            // La gestion sécurisée du mot de passe (hachage) doit être faite dans le service ou avant.
            // Pour la création, l'ID de l'utilisateur doit être null (généré par la BD).
            Utilisateur savedUtilisateur = utilisateurService.save(utilisateur, idAdresse);
            return new ResponseEntity<>(savedUtilisateur, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) { // Si l'adresse n'est pas trouvée
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) { // Pour d'autres erreurs, ex: violation d'unicité email/pseudo
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Utilisateur> getUtilisateurByEmail(@PathVariable String email) {
        return utilisateurService.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurService.findAll();
        return ResponseEntity.ok(utilisateurs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUtilisateur(@PathVariable Long id,
                                               @Valid @RequestBody Utilisateur utilisateurDetails,
                                               @RequestParam(required = false) Long idAdresse) {
        try {
            Utilisateur updatedUtilisateur = utilisateurService.update(id, utilisateurDetails, idAdresse);
            return ResponseEntity.ok(updatedUtilisateur);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) { // Pour d'autres erreurs, ex: violation d'unicité email/pseudo
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<?> changerRoleUtilisateur(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String roleStr = payload.get("role");
            if (roleStr == null) {
                return ResponseEntity.badRequest().body("Le champ 'role' est requis.");
            }
            RoleUtilisateur nouveauRole = RoleUtilisateur.valueOf(roleStr.toUpperCase());
            Utilisateur utilisateurMaj = utilisateurService.changerRole(id, nouveauRole);
            return ResponseEntity.ok(utilisateurMaj);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Rôle invalide: " + payload.get("role"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        try {
            utilisateurService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) { // Si la suppression est empêchée par des dépendances
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null); // Ou e.getMessage()
        }
    }
}