FROM tomcat:9.0-jdk11-openjdk
WORKDIR /usr/local/tomcat
ADD scc2021-project-0.1.war webapps
EXPOSE 8080