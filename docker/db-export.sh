#!/bin/bash

rm -f data/forms-full.csv
rm -f data/forms-join.csv

docker exec docker_database_1 sh -c 'mysql hunnor < /sql/export.sql'
