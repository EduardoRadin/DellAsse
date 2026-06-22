#!/bin/bash
DATA=$(date +%Y%m%d_%H%M%S)
DIR_DESTINO="/home/postgres/backups/fisico_$DATA"

mkdir -p $DIR_DESTINO

pg_basebackup -U postgres -D "$DIR_DESTINO" -Ft -z -P
find /home/postgres/backups -name "fisico_*" -type d -mtime +7 -exec rm -rf {} +
