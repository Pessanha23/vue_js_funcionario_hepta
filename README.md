# POC
Essa é uma POC de uma aplicação utilizando JDK8, desenvolvido em ambiente JDK17 com o auxílio do framework [Jersey](https://eclipse-ee4j.github.io/jersey/) e uma base
de dados [MySQL](https://www.mysql.com/), e TOMCAT 9. Rara rodas as depêndencias externas foi utilizado o [Docker](https://www.docker.com/). Para os testes de integração foram utilizados o [JUnit](https://junit.org/junit5/) e [JerseyTest](https://eclipse-ee4j.github.io/jersey/).

## Conceito
A idéia é construir uma API REST de cadastro de funcionário, onde cada funcionário deverá conter alguns atributos como nome, email, idade, setor.
- nome
- email
- idade
- setor

## Stack
- Ambiente Java 17
- Desenvolvimento JDK 8
- Jersey
- MySQL
- Postman
- Docker file / compose
- Tomcat 9
- API REST

## Estrutura do projeto
A estrutura do projeto foi criada baseada em alguns exemplos de DDD como
o [ddd-by-example](https://github.com/joolu/ddd-sample) e de Arquitetura Hexagonal como
o [Netflix](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749).
Basicamente temos 3 camadas:
* **rest**: Camada responsável por expor os endpoints da API e receber as requisições.
* **persistence**: Camada responsável por conter as regras de negócio e os domínios da aplicação.
* **persistence.xml**: Camada responsável por conter as implementações de infraestrutura como banco de dados;

## Testes
### Integração

Os testes de integração são utilizados para integrações com serviços externos, como banco de dados e APIs. Utilizando JerseyTest para fazer a cobertura da integração entre os frameworks.
- **rest**: Testes integrados dos *rest*

## Exemplos para testar
### 200 OK - POST
Request
```
{
    {
        "nome": "Joao Ricardo",
        "setor": {
            "id": 9,
            "nome": "DEV"
        },
        "salario": 3500.0,
        "email": "joao@gmail.com",
        "idade": 29
    }
}
```

Response
```
//////
```


## DOCKER
```
mvn clean
mvn package
# Use a imagem do Tomcat como base
FROM tomcat:9
# Copie o WAR da aplicação para o diretório de implantação do Tomcat
COPY target/funcionarios-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/funcionarios.war
# Exponha a porta 8080 para acesso externo
EXPOSE 8080
docker compose up
docker ps
```