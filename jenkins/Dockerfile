# Jenkins controller with Docker CLI (Docker-capable build agent)
# Student: Didarova Medina | ID 37944
FROM jenkins/jenkins:lts-jdk17
USER root
RUN apt-get update \
 && apt-get install -y --no-install-recommends docker-cli git \
 && rm -rf /var/lib/apt/lists/*
RUN jenkins-plugin-cli --plugins \
    workflow-aggregator git github timestamper junit credentials-binding docker-workflow pipeline-stage-view
USER jenkins
