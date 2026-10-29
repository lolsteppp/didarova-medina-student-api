#!/bin/bash
# =====================================================================
# Part 3 - Docker & Docker Compose demonstration (Tasks 3.1 - 3.4)
# Student: Didarova Medina | ID 37944 | Group IT2-2313
# Run from the project root:  ./scripts/Didarova_Medina_docker_demo.sh
# Output is also saved to logs/Didarova_Medina_docker_demo.log
# =====================================================================
cd "$(dirname "$0")/.." || exit 1
LOG="logs/Didarova_Medina_docker_demo.log"
IMAGE="didarova-medina-student-api"
TAG="v1.0-didarova-medina-37944"
TAG2="v1.1-didarova-medina-37944"
APP="didarova-medina-app"
DB="didarova-medina-postgres"

exec > >(tee "$LOG") 2>&1
run() { echo; echo "medina\$ $*"; eval "$@"; }
title() { echo; echo "######################################################"; echo "# $1"; echo "######################################################"; }
wait_app() {
  echo "Waiting for the application on http://localhost:8080 ..."
  for i in $(seq 1 60); do
    curl -s -o /dev/null http://localhost:8080/api/info && { echo "Application is UP"; return 0; }
    sleep 2
  done
  echo "Application did not start in time"; docker logs --tail 50 $APP; return 1
}

echo "Docker demo by Didarova Medina - $(date '+%Y-%m-%d %H:%M:%S')"

# .env with database credentials (never committed)
if [ ! -f .env ]; then
  cp .env.example .env
  sed -i.bak "s/^POSTGRES_PASSWORD=.*/POSTGRES_PASSWORD=Didarova_$(date +%s)/" .env && rm -f .env.bak
  echo ".env created from .env.example (password generated)"
fi

title "Task 3.1 - Build multi-stage image"
run "docker build -t $IMAGE:$TAG ."
run "docker images $IMAGE"
run "docker image inspect $IMAGE:$TAG --format 'Created: {{.Created}} | Size: {{.Size}} bytes | Author: {{index .Config.Labels \"org.opencontainers.image.authors\"}}'"
run "docker history $IMAGE:$TAG | head -12"

title "Task 3.2 - Docker Compose (app + PostgreSQL)"
run "docker compose config --services"
run "docker compose up -d"
wait_app
run "docker compose ps"
run "docker network ls | grep didarova"
run "docker volume ls | grep didarova"
run "docker inspect $DB --format 'PostgreSQL health: {{.State.Health.Status}}'"
run "docker exec $APP printenv SPRING_DATASOURCE_URL"

title "Task 3.3 - Container management"
run "docker ps"
run "docker logs --tail 25 $APP"
run "docker inspect $APP --format 'Image={{.Config.Image}} | Ports={{json .NetworkSettings.Ports}} | Networks={{range \$k,\$v := .NetworkSettings.Networks}}{{\$k}} {{end}} | Restart={{.HostConfig.RestartPolicy.Name}}'"
run "docker exec $APP sh -c 'whoami; java -version 2>&1 | head -1; ls -lh /app'"
run "docker compose stop didarova-app"
run "docker compose ps -a"
run "docker compose start didarova-app"
wait_app
run "docker compose ps"

title "Task 3.4 - API testing with curl"
run "./scripts/Didarova_Medina_api_test.sh http://localhost:8080"

title "Task 3.3.7 - Data persists after restarting services"
run "curl -s -X POST http://localhost:8080/api/students -H 'Content-Type: application/json' -d '{\"firstName\":\"Persist\",\"lastName\":\"Didarova\",\"email\":\"persist.didarova.37944@student.kz\",\"groupName\":\"IT2-2313\",\"course\":3}'"
run "docker exec $DB sh -c 'psql -U \$POSTGRES_USER -d \$POSTGRES_DB -c \"SELECT id, first_name, last_name, email FROM students;\"'"
run "docker compose down"
echo "(containers removed, volume didarova_medina_pgdata is kept)"
run "docker volume ls | grep didarova"
run "docker compose up -d"
wait_app
run "docker exec $DB sh -c 'psql -U \$POSTGRES_USER -d \$POSTGRES_DB -c \"SELECT id, first_name, last_name, email FROM students;\"'"
run "curl -s http://localhost:8080/api/students"

title "Task 3.3.6 - Rebuild image after changing the application version"
run "APP_VERSION=$TAG2 docker compose up -d --build didarova-app"
wait_app
run "docker images $IMAGE"
run "curl -s http://localhost:8080/api/info"
run "docker compose ps"

echo
echo "Docker demo finished by Didarova Medina - $(date '+%Y-%m-%d %H:%M:%S'). Log: $LOG"
