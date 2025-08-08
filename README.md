# Auth-API
[![Author](https://img.shields.io/badge/author-lcsdiniz-6DB33F)](https://www.linkedin.com/in/lcsdiniz/)
[![Languages](https://img.shields.io/github/languages/count/lcsdiniz/auth-api?color=6DB33F)](#)
[![Stars](https://img.shields.io/github/stars/lcsdiniz/auth-api?color=6DB33F)](#)

---

## ℹ️ Sobre

Este projeto foi desenvolvido seguindo rigorosamente os princípios da **Clean Architecture**, proporcionando alta **manutenibilidade**, **testabilidade** e **desacoplamento** entre as camadas.  

- **Independência de frameworks:** as **entidades de domínio** não dependem de detalhes de implementação ou tecnologias específicas.  
- **Isolamento das regras de negócio:** a lógica central do sistema permanece protegida da infraestrutura e adaptadores externos.  
- **DTOs e Mappers:** a comunicação com o mundo externo é feita exclusivamente por **DTOs** (Data Transfer Objects), com **mappers** responsáveis por converter entre **modelos de domínio** e **DTOs**, evitando a exposição direta das entidades.

Além da arquitetura robusta, o sistema implementa autenticação segura com **Access Tokens** e **Refresh Tokens**, cada um com seu tempo de expiração próprio, garantindo maior proteção e confiabilidade.

---

## 🛠 Tecnologias

- **Java 22**  
- **Spring Boot**  
- **Spring Security**  
- **JWT (JSON Web Token)**  
- **Spring Data JPA**  
- **PostgreSQL**  
- **Docker & Docker Compose**  
- **Swagger**  

---

## 🚀 Como rodar

### Pré-requisitos

- **Docker** e **Docker Compose** instalados  
- **Java 22** (se for rodar localmente sem Docker)

### Passos

```bash
# Clone o repositório
git clone https://github.com/lcsdiniz/auth-api.git

# Entre na pasta do projeto
cd auth-api

# Crie um arquivo .env baseado no .env.example e preencha-o com as variáveis necessárias

# Suba o banco e a aplicação com Docker Compose
docker-compose up --build
```
## 📌 A documentação completa da API estará disponível via Swagger em: [Documentação](http://localhost:8080/swagger-ui/index.html)
