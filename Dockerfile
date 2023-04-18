
# Use a imagem do Tomcat como base
FROM tomcat:9

# Copie o WAR da aplicação para o diretório de implantação do Tomcat
COPY target/funcionarios-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/funcionarios.war

# Exponha a porta 8080 para acesso externo
EXPOSE 8080