-- Script SQL pour insérer des données de test dans la base de données

-- ============================================
-- 1. CLIENTS
-- ============================================
INSERT INTO clients (code_client, nom_client, prenom_client, adresse_client) VALUES 
(1, 'Alami', 'Mohammed', '123 Boulevard Mohammed V, Casablanca'),
(2, 'Benali', 'Fatima', '456 Avenue Hassan II, Rabat'),
(3, 'Chraibi', 'Youssef', '789 Rue de la Liberté, Marrakech'),
(4, 'Drissi', 'Amina', '321 Avenue des FAR, Fès'),
(5, 'El Fassi', 'Karim', '654 Boulevard Zerktouni, Casablanca');

-- ============================================
-- 2. EMPLOYÉS
-- ============================================
-- Employés de niveau supérieur (sans superviseur)
INSERT INTO employes (code_employe, nom_employe, code_employe_sup) VALUES 
(1, 'Directeur General', NULL),
(2, 'Chef Agence Casablanca', 1),
(3, 'Chef Agence Rabat', 1);

-- Employés subordonnés
INSERT INTO employes (code_employe, nom_employe, code_employe_sup) VALUES 
(4, 'Conseiller Casablanca 1', 2),
(5, 'Conseiller Casablanca 2', 2),
(6, 'Conseiller Rabat 1', 3),
(7, 'Conseiller Rabat 2', 3);

-- ============================================
-- 3. GROUPES
-- ============================================
INSERT INTO groupes (code_groupe, nom_groupe) VALUES 
(1, 'Direction'),
(2, 'Conseillers Commerciaux'),
(3, 'Service Clientèle');

-- ============================================
-- 4. ASSOCIATION EMPLOYÉS-GROUPES
-- ============================================
-- Directeur dans Direction
INSERT INTO employe_groupe (code_groupe, code_employe) VALUES (1, 1);

-- Chefs d'agence dans Direction
INSERT INTO employe_groupe (code_groupe, code_employe) VALUES (1, 2);
INSERT INTO employe_groupe (code_groupe, code_employe) VALUES (1, 3);

-- Conseillers dans Conseillers Commerciaux
INSERT INTO employe_groupe (code_groupe, code_employe) VALUES (2, 4);
INSERT INTO employe_groupe (code_groupe, code_employe) VALUES (2, 5);
INSERT INTO employe_groupe (code_groupe, code_employe) VALUES (2, 6);
INSERT INTO employe_groupe (code_groupe, code_employe) VALUES (2, 7);

-- Conseillers dans Service Clientèle
INSERT INTO employe_groupe (code_groupe, code_employe) VALUES (3, 4);
INSERT INTO employe_groupe (code_groupe, code_employe) VALUES (3, 5);
INSERT INTO employe_groupe (code_groupe, code_employe) VALUES (3, 6);

-- ============================================
-- 5. COMPTES (Courants et Épargne)
-- ============================================
-- Comptes Courants (type_compte = 'CC')
INSERT INTO comptes (num_compte, date_creation, solde, type_compte, decouvert, taux_interet, code_client, code_employe) VALUES 
('CC001', '2024-01-15', 5000.00, 'CC', 1000.00, NULL, 1, 4),
('CC002', '2024-02-20', 3500.50, 'CC', 500.00, NULL, 2, 5),
('CC003', '2024-03-10', -200.00, 'CC', 1500.00, NULL, 3, 6);

-- Comptes Épargne (type_compte = 'CE')
INSERT INTO comptes (num_compte, date_creation, solde, type_compte, decouvert, taux_interet, code_client, code_employe) VALUES 
('CE001', '2024-01-20', 15000.00, 'CE', NULL, 2.5, 1, 4),
('CE002', '2024-02-25', 8000.00, 'CE', NULL, 2.5, 2, 5),
('CE003', '2024-03-15', 25000.00, 'CE', NULL, 3.0, 4, 6),
('CE004', '2024-04-01', 12000.00, 'CE', NULL, 2.5, 5, 7);

-- ============================================
-- 6. OPÉRATIONS (Versements et Retraits)
-- ============================================
-- Versements (type_operation = 'V')
INSERT INTO operations (num_operation, date_operation, montant, type_operation, num_compte, code_employe) VALUES 
(1, '2024-01-15', 5000.00, 'V', 'CC001', 4),
(2, '2024-01-20', 15000.00, 'V', 'CE001', 4),
(3, '2024-02-20', 4000.00, 'V', 'CC002', 5),
(4, '2024-02-25', 8000.00, 'V', 'CE002', 5),
(5, '2024-03-10', 1000.00, 'V', 'CC003', 6),
(6, '2024-03-15', 25000.00, 'V', 'CE003', 6),
(7, '2024-04-01', 12000.00, 'V', 'CE004', 7);

-- Retraits (type_operation = 'R')
INSERT INTO operations (num_operation, date_operation, montant, type_operation, num_compte, code_employe) VALUES 
(8, '2024-02-01', 500.00, 'R', 'CC001', 4),
(9, '2024-02-28', 500.50, 'R', 'CC002', 5),
(10, '2024-03-20', 1200.00, 'R', 'CC003', 6),
(11, '2024-03-25', 1000.00, 'R', 'CE002', 5);

-- Opérations supplémentaires pour historique
INSERT INTO operations (num_operation, date_operation, montant, type_operation, num_compte, code_employe) VALUES 
(12, '2024-05-10', 2000.00, 'V', 'CC001', 4),
(13, '2024-05-15', 300.00, 'R', 'CC001', 4),
(14, '2024-06-01', 1500.00, 'V', 'CC002', 5),
(15, '2024-06-10', 500.00, 'R', 'CC002', 5),
(16, '2024-07-01', 5000.00, 'V', 'CE001', 4),
(17, '2024-07-15', 3000.00, 'V', 'CE003', 6);

-- ============================================
-- RÉSUMÉ DES DONNÉES CRÉÉES
-- ============================================
-- 5 Clients
-- 7 Employés (avec hiérarchie)
-- 3 Groupes
-- 7 Comptes (3 courants, 4 épargne)
-- 17 Opérations (11 versements, 6 retraits)

-- ============================================
-- RÉINITIALISATION DES SÉQUENCES
-- ============================================
-- Important: Réinitialiser les séquences après insertion manuelle des IDs
SELECT setval(pg_get_serial_sequence('operations', 'num_operation'), (SELECT COALESCE(MAX(num_operation), 1) FROM operations));
SELECT setval(pg_get_serial_sequence('clients', 'code_client'), (SELECT COALESCE(MAX(code_client), 1) FROM clients));
SELECT setval(pg_get_serial_sequence('employes', 'code_employe'), (SELECT COALESCE(MAX(code_employe), 1) FROM employes));
SELECT setval(pg_get_serial_sequence('groupes', 'code_groupe'), (SELECT COALESCE(MAX(code_groupe), 1) FROM groupes));

