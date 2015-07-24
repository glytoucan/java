#!/bin/sh
#mvn --debug spring-boot:run
sudo docker run -it --rm -h local.rdf.glytoucan -v /opt/rdf.glytoucan/maven:/root/.m2 -v "$(pwd)":/workspace -w /workspace --name="rdf.glytoucan" maven:3.3.3-jdk-8 mvn package
#docker run -it --rm -v "$(pwd)":/workspace -w /workspace -p 8080:8080 maven:3.3.3-jdk-8 mvn --debug spring-boot:run
