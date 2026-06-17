#!/bin/bash
DATA=$(date +%Y%m%d_%H%M%S)
DIR_DESTINO="/home/postgres/backups"

pg_dumpall -U postgres -f "$DIR_DESTINO/bkp_logico_$DATA.sql"
find $DIR_DESTINO -name "bkp_logico_*" -mtime +7 -delete
