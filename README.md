# API REST com Spring Boot, H2, HATEOAS e Docker.

Este projeto consiste em uma API REST para gerenciar despesas(expenses) utilizando o framework Spring Boot, o banco de dados em memória H2, Docker e implementando o conceito HATEOAS(Hypertext As The Engine Of Application State).

### Pré-requisitos
- Java JDK 17+
- Maven

### Dependências
- Spring HATEOAS
- Spring Web
- H2 Database 
- Spring Data JDBC
- Spring Boot DevTools

## Docker
Para gerar uma imagem Docker com a API, siga os seguintes passos:
- `mvn clean package`
- `docker build -t expenses-api .`

Para rodar a imagem gerada:
- `docker run -d --name expenses-api -p 8080:8080 expenses-api
  `
