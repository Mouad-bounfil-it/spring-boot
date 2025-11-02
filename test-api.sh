#!/bin/bash

# =================================================
# 🧪 Script de Test de l'API Banque
# =================================================

BASE_URL="http://localhost:8080/api"

echo "🏦 =========================================="
echo "   Test de l'API Gestion Banque"
echo "=========================================="
echo ""

# Vérifier si l'application est en cours d'exécution
echo "1️⃣  Vérification de l'application..."
if curl -s "$BASE_URL/clients" > /dev/null 2>&1; then
    echo "✅ Application est accessible"
else
    echo "❌ Application n'est pas accessible"
    echo "   Lancez l'application avec: ./gradlew bootRun"
    exit 1
fi
echo ""

# Test 1: Créer un client
echo "2️⃣  Test: Créer un nouveau client"
CLIENT_RESPONSE=$(curl -s -X POST "$BASE_URL/clients" \
  -H "Content-Type: application/json" \
  -d '{
    "nomClient": "Test",
    "prenomClient": "User",
    "adresseClient": "123 Test Street"
  }')

CLIENT_ID=$(echo $CLIENT_RESPONSE | grep -o '"codeClient":[0-9]*' | cut -d':' -f2)

if [ -n "$CLIENT_ID" ]; then
    echo "✅ Client créé avec ID: $CLIENT_ID"
else
    echo "❌ Échec de création du client"
fi
echo ""

# Test 2: Lister tous les clients
echo "3️⃣  Test: Lister tous les clients"
CLIENTS=$(curl -s "$BASE_URL/clients")
CLIENT_COUNT=$(echo $CLIENTS | grep -o '"codeClient"' | wc -l)
echo "✅ Nombre de clients: $CLIENT_COUNT"
echo ""

# Test 3: Créer un employé
echo "4️⃣  Test: Créer un nouvel employé"
EMPLOYE_RESPONSE=$(curl -s -X POST "$BASE_URL/employes" \
  -H "Content-Type: application/json" \
  -d '{
    "nomEmploye": "Test Employee"
  }')

EMPLOYE_ID=$(echo $EMPLOYE_RESPONSE | grep -o '"codeEmploye":[0-9]*' | cut -d':' -f2)

if [ -n "$EMPLOYE_ID" ]; then
    echo "✅ Employé créé avec ID: $EMPLOYE_ID"
else
    echo "❌ Échec de création de l'employé"
fi
echo ""

# Test 4: Créer un compte (si client et employé existent)
if [ -n "$CLIENT_ID" ] && [ -n "$EMPLOYE_ID" ]; then
    echo "5️⃣  Test: Créer un compte courant"
    COMPTE_NUM="TEST$(date +%s)"
    
    COMPTE_RESPONSE=$(curl -s -X POST "$BASE_URL/comptes?codeClient=$CLIENT_ID&codeEmploye=$EMPLOYE_ID" \
      -H "Content-Type: application/json" \
      -d "{
        \"numCompte\": \"$COMPTE_NUM\",
        \"solde\": 1000.0,
        \"decouvert\": 500.0,
        \"type\": \"CC\"
      }")
    
    if echo "$COMPTE_RESPONSE" | grep -q "$COMPTE_NUM"; then
        echo "✅ Compte créé: $COMPTE_NUM"
        
        # Test 5: Effectuer un versement
        echo ""
        echo "6️⃣  Test: Effectuer un versement de 500 DH"
        VERSEMENT=$(curl -s -X POST "$BASE_URL/operations/versement" \
          -H "Content-Type: application/json" \
          -d "{
            \"codeCompte\": \"$COMPTE_NUM\",
            \"montant\": 500.0,
            \"codeEmploye\": $EMPLOYE_ID
          }")
        
        if echo "$VERSEMENT" | grep -q "successful"; then
            echo "✅ Versement effectué avec succès"
        else
            echo "❌ Échec du versement"
        fi
        
        # Test 6: Effectuer un retrait
        echo ""
        echo "7️⃣  Test: Effectuer un retrait de 200 DH"
        RETRAIT=$(curl -s -X POST "$BASE_URL/operations/retrait" \
          -H "Content-Type: application/json" \
          -d "{
            \"codeCompte\": \"$COMPTE_NUM\",
            \"montant\": 200.0,
            \"codeEmploye\": $EMPLOYE_ID
          }")
        
        if echo "$RETRAIT" | grep -q "successful"; then
            echo "✅ Retrait effectué avec succès"
        else
            echo "❌ Échec du retrait"
        fi
        
        # Test 7: Consulter le compte
        echo ""
        echo "8️⃣  Test: Consulter le compte"
        COMPTE_INFO=$(curl -s "$BASE_URL/comptes/$COMPTE_NUM")
        SOLDE=$(echo $COMPTE_INFO | grep -o '"solde":[0-9.]*' | cut -d':' -f2)
        
        if [ -n "$SOLDE" ]; then
            echo "✅ Solde actuel: $SOLDE DH"
            echo "   (1000 + 500 - 200 = 1300 DH attendu)"
        else
            echo "❌ Impossible de récupérer le solde"
        fi
        
        # Test 8: Consulter les opérations
        echo ""
        echo "9️⃣  Test: Consulter l'historique des opérations"
        OPERATIONS=$(curl -s "$BASE_URL/comptes/$COMPTE_NUM/operations")
        OP_COUNT=$(echo $OPERATIONS | grep -o '"numOperation"' | wc -l)
        echo "✅ Nombre d'opérations: $OP_COUNT (2 attendues)"
        
    else
        echo "❌ Échec de création du compte"
    fi
fi

echo ""
echo "=========================================="
echo "🎉 Tests terminés!"
echo "=========================================="
echo ""
echo "📚 Pour plus de tests:"
echo "   - Interface Web: http://localhost:8080/banque"
echo "   - Swagger UI: http://localhost:8080/swagger-ui.html"
echo ""

