#!/bin/bash
# =====================================================================
# Task 3.4 - Individual API testing with curl
# Student: Didarova Medina | ID 37944 | Group IT2-2313
# Usage: ./scripts/Didarova_Medina_api_test.sh [base_url]
# =====================================================================
BASE_URL="${1:-http://localhost:8080}"
API="$BASE_URL/api/students"
H='Content-Type: application/json'

step() { echo; echo "================ $1 ================"; echo "\$ $2"; }
call() { step "$1" "curl $2"; eval "curl -s -w '\nHTTP status: %{http_code}\n' $2"; }

call "0. App info (shows student name)"  "'$BASE_URL/api/info'"

step "1. CREATE student with my name" "curl -X POST $API ..."
RESPONSE=$(curl -s -X POST "$API" -H "$H" -d '{
  "firstName": "Medina",
  "lastName": "Didarova",
  "email": "medina.didarova.37944@student.kz",
  "groupName": "IT2-2313",
  "course": 3
}' -w '\n%{http_code}')
echo "$RESPONSE" | sed '$d'
echo "HTTP status: $(echo "$RESPONSE" | tail -n 1)"
NEW_ID=$(echo "$RESPONSE" | sed '$d' | grep -o '"id": *[0-9]*' | head -1 | grep -o '[0-9]*$')
echo "Created student id = $NEW_ID"

call "2. GET all students"            "'$API'"
call "3. GET one student by id=$NEW_ID" "'$API/$NEW_ID'"
call "4. UPDATE student id=$NEW_ID (course 3 -> 4)" \
     "-X PUT '$API/$NEW_ID' -H '$H' -d '{\"firstName\":\"Medina\",\"lastName\":\"Didarova\",\"email\":\"medina.didarova.37944@student.kz\",\"groupName\":\"IT2-2313\",\"course\":4}'"
call "5. DELETE student id=$NEW_ID"   "-X DELETE '$API/$NEW_ID'"
call "6. GET deleted / nonexistent student id=$NEW_ID (expect 404)" "'$API/$NEW_ID'"
call "7. GET nonexistent student id=999999 (expect 404)" "'$API/999999'"
echo
echo "API test finished by Didarova Medina at $(date '+%Y-%m-%d %H:%M:%S')"
