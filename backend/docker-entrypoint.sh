#!/usr/bin/env bash
set -e

DB_HOST="${DB1_HOST:-easynote-db}"
DB_PORT="${DB1_PORT:-3306}"

echo "Aguardando o banco em ${DB_HOST}:${DB_PORT}..."
until (echo > "/dev/tcp/${DB_HOST}/${DB_PORT}") 2>/dev/null; do
  echo "   banco ainda nao respondeu..."
  sleep 3
done
echo "Banco disponivel. Iniciando a aplicacao..."

exec java -Xms128m -Xmx256m -jar app.jar
