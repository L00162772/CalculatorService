FROM jenkins/jenkins:lts
# if we want to install via apt
USER root
RUN apt-get update && apt-get install -y maven
# drop back to the regular jenkins user - good practice
USER jenkins


# build and run container with following commands
#docker build -t jenkins-maven .
#docker run -p 8080:8080 --name=jenkins-maven -d jenkins-maven