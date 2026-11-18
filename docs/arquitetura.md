# Arquitetura do Cadastro Service

## Responsabilidade

Dono dos dados cadastrais do AutoCenter: clientes, veiculos, catalogo de servicos e produtos (com estoque). Os outros
servicos guardam so os IDs e consultam este servico por REST quando precisam do dado atual. Ele nao publica nem
consome Kafka e fica fora da Saga da OS.

## Camadas

Hexagonal por modulo (`cliente`, `veiculo`, `servico`, `produto`), cada um com `domain` (entidades e regras, sem
Spring), `application` (casos de uso, portas, DTOs), `adapter` (controllers REST, mapeadores, handlers de erro) e
`infrastructure` (JPA, configuracao dos beans). O pacote `comum` tem a seguranca JWT e o formato unico de erro
(`ErroResposta`).

## Dados

MySQL 8 proprio (`cadastro_db`), versionado com Flyway:

| Migration | Conteudo |
|---|---|
| V1, V2 | `servicos` e dados de exemplo |
| V3, V4 | `produtos` e dados de exemplo |
| V5, V6 | `clientes` (com `status`) e dados de exemplo, um deles `INATIVO` |
| V7, V8 | `veiculos` e dados de exemplo |

Veiculo nao tem vinculo com cliente (igual ao monolito); quem relaciona os dois e a OS.

## Decisoes

- **Exclusao sem checagem de OS.** O monolito bloqueava a exclusao quando havia OS associada, lendo a tabela de OS.
  Essa tabela agora e do OS Service e nenhum servico le o banco de outro. Por isso cliente e veiculo sao apagados
  direto e servico e produto sao inativados. Se o grupo quiser o bloqueio de volta, o caminho e o Cadastro consultar
  uma rota do OS Service.
- **So token interno.** O cliente final nao consulta o cadastro diretamente; ele acompanha a OS pelo OS Service.
- **Contrato estavel para os consumidores.** OS Service, Execucao e Billing dependem de `status` (cliente), `status` e
  `valor` (servico), `nome`, `precoUnitario` e `ativo` (produto), `email` e `telefone` (cliente). Mudar esses campos
  quebra a integracao.

## Falhas

Os consumidores tratam o Cadastro fora do ar como indisponibilidade (503) e nao gravam nem publicam nada. Assim, uma OS
nunca e aberta e um diagnostico nunca e concluido sem a checagem.
