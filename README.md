# AlgaShop - Ordering

Microsserviço responsável pela emissão e pelo gerenciamento de pedidos do AlgaShop.

> **Status do projeto**
>
> Este microsserviço está em desenvolvimento e será construído de forma incremental.
> A documentação e a arquitetura serão atualizadas conforme novas decisões forem tomadas e novas funcionalidades forem implementadas.

## Objetivo

O microsserviço **Ordering** é responsável por coordenar o processo de emissão de pedidos na plataforma AlgaShop.

Ele concentra as regras relacionadas à criação e à evolução do pedido, mantendo independência em relação aos demais contextos do sistema.

## Responsabilidades

Inicialmente, este microsserviço será responsável por:

- emitir pedidos;
- registrar os itens do pedido;
- calcular o valor total;
- manter o estado do pedido;
- consultar os dados necessários do catálogo de produtos;
- iniciar o processo de cobrança;
- acompanhar o resultado do pagamento;
- disponibilizar informações do pedido para outros serviços.

## Fora do escopo

Este microsserviço não será responsável por:

- cadastrar produtos;
- manter preços e informações comerciais dos produtos;
- controlar estoque;
- processar pagamentos diretamente;
- realizar entregas;
- autenticar usuários;
- armazenar dados internos de gateways externos.

Essas responsabilidades pertencem a outros contextos ou sistemas.

## Contexto no domínio

O Ordering representa o **Bounded Context de Emissão de Pedidos**.

Ele se relaciona com os seguintes contextos e sistemas:

- Gestão de Identidades;
- Catálogo de Produtos;
- Cobranças;
- Empresa de Entregas;
- Authorization Server;
- Message Broker.

## Integrações previstas

### Comunicação síncrona

As integrações que exigirem resposta imediata poderão utilizar APIs REST.

Exemplos:

- validação de identidade e autorização;
- consulta de informações de produtos;
- consulta ou cálculo de informações de entrega.

### Comunicação assíncrona

Eventos de negócio poderão ser publicados por meio de um Message Broker.

Exemplos:

- pedido emitido;
- pedido cancelado;
- pagamento confirmado;
- pagamento recusado;
- pedido pronto para entrega.

Os contratos de integração serão definidos conforme a evolução do projeto.

## Persistência

O Ordering possui seu próprio banco de dados.

Nenhum outro microsserviço deve acessar diretamente as tabelas deste serviço.

Da mesma forma, o Ordering não deve acessar diretamente os bancos de dados dos demais microsserviços.

## Princípios arquiteturais

Este microsserviço será desenvolvido considerando:

- Domain-Driven Design;
- linguagem ubíqua;
- separação de responsabilidades;
- alta coesão;
- baixo acoplamento;
- banco de dados por serviço;
- integração por contratos;
- evolução incremental da arquitetura.

## Tecnologias

Tecnologias inicialmente previstas:

- Java 21;
- Spring Boot;
- Spring Data JPA;
- PostgreSQL;
- Flyway;
- Docker;
- Gradle;
- OpenAPI;
- mensageria, a ser definida durante a evolução do projeto.

## Estrutura inicial

```text
ordering/
├── src/
├── docs/
├── docker/
├── build.gradle
├── settings.gradle
└── README.md