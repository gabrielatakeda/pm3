# EasyNote

Sistema simples para controle de empréstimo de notebooks a alunos, feito para uso por funcionários (ex: laboratório, biblioteca, secretaria).

## Sobre o projeto

O EasyNote facilita o controle de empréstimo e devolução de notebooks institucionais, vinculando cada empréstimo ao RA (Registro Acadêmico) do aluno e ao número de patrimônio do equipamento.

## Funcionalidades

- **Login**
  - Autenticação do funcionário para acesso ao sistema.

- **Cadastro de equipamento**
  - Cadastro dos notebooks disponíveis, identificados por número de patrimônio.

- **Cadastro de funcionário**
  - Cadastro dos funcionários responsáveis por registrar os empréstimos.

- **Iniciar empréstimo**
  - O aluno informa o **RA**.
  - O funcionário registra o **número de patrimônio** do notebook.
  - O sistema inicia o empréstimo, vinculando aluno e equipamento.

- **Encerrar empréstimo**
  - O funcionário localiza o empréstimo pelo **número de patrimônio**.
  - Clica em "Encerrar" para finalizar a devolução.

## Fluxo de uso

```
1. Funcionário faz login no sistema
2. Aluno informa o RA
3. Funcionário digita o número de patrimônio do notebook
4. Funcionário clica em "Iniciar" -> empréstimo é registrado
   ...
5. Na devolução: funcionário busca o número de patrimônio
6. Funcionário clica em "Encerrar" -> empréstimo é finalizado
```

## Tecnologias utilizadas

- **Frontend:** Angular
- **Backend:** Spring Boot (Java 17)
- **Banco de dados:** MySQL

## Contato

- [Davi Sobreira](https://www.linkedin.com/in/davi-sobreira)
- [Beatriz Schindler](https://www.linkedin.com/in/beatriz-schindler-b26476363/?locale=pt)
- [João Girardi](https://www.linkedin.com/in/joao-girardi/)
