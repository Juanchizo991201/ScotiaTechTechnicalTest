#!/bin/bash

echo "================================================"
echo "Scotia Tech Technical Test - API Demo"
echo "================================================"
echo ""

BASE_URL="http://localhost:8080/api/students"

echo "1. Creating a new student (ACTIVO)..."
echo "Request: POST $BASE_URL"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "STU001",
    "nombre": "Juan",
    "apellido": "Pérez",
    "estado": "ACTIVO",
    "edad": 25
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "2. Creating another student (ACTIVO)..."
echo "Request: POST $BASE_URL"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "STU002",
    "nombre": "Maria",
    "apellido": "González",
    "estado": "ACTIVO",
    "edad": 30
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "3. Creating a student (INACTIVO)..."
echo "Request: POST $BASE_URL"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "STU003",
    "nombre": "Carlos",
    "apellido": "López",
    "estado": "INACTIVO",
    "edad": 35
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "4. Attempting to create duplicate student (should fail with 409)..."
echo "Request: POST $BASE_URL"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "STU001",
    "nombre": "Juan",
    "apellido": "Pérez",
    "estado": "ACTIVO",
    "edad": 25
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "5. Getting all ACTIVE students..."
echo "Request: GET $BASE_URL/active"
curl -X GET "$BASE_URL/active" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo "6. Validation test - Invalid age (0)..."
echo "Request: POST $BASE_URL"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "STU004",
    "nombre": "Ana",
    "apellido": "Sánchez",
    "estado": "ACTIVO",
    "edad": 0
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "7. Validation test - Invalid status..."
echo "Request: POST $BASE_URL"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "STU005",
    "nombre": "Luis",
    "apellido": "Martínez",
    "estado": "PENDIENTE",
    "edad": 40
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "8. Validation test - Empty name..."
echo "Request: POST $BASE_URL"
curl -X POST "$BASE_URL" \
  -H "Content-Type: application/json" \
  -d '{
    "id": "STU006",
    "nombre": "",
    "apellido": "García",
    "estado": "ACTIVO",
    "edad": 28
  }' \
  -w "\nStatus: %{http_code}\n\n"

echo "9. Final - Getting all ACTIVE students (should show STU001 and STU002)..."
echo "Request: GET $BASE_URL/active"
curl -X GET "$BASE_URL/active" \
  -H "Accept: application/json" \
  -w "\nStatus: %{http_code}\n\n"

echo "================================================"
echo "Demo Complete!"
echo "================================================"
echo ""