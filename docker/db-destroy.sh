#!/bin/bash

docker exec docker_database_1 sh -c 'mysql hunnor < /sql/drop.sql'
