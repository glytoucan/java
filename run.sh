#!/bin/sh
#mvn --debug spring-boot:run
docker run -it --rm -v "$(pwd)":/workspace -w /workspace maven:3.3.3-jdk-8 mvn --debug spring-boot:run
