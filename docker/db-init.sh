#!/bin/bash

docker exec docker_database_1 sh -c 'mysql hunnor < /sql/create.sql'

docker exec docker_database_1 sh -c 'mysql -v -v -v --local-infile hunnor < /sql/import.sql'

docker exec docker_database_1 sh -c 'mysql hunnor < /sql/hn.create.sql'
