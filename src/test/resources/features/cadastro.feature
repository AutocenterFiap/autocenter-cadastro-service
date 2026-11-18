# language: pt
Funcionalidade: Cadastro de Cliente
  Como atendente do AutoCenter
  Quero registrar e consultar clientes
  Para que os demais servicos possam validar sua existencia antes de abrir uma OS

  Cenario: Registrar cliente pessoa fisica e consulta-lo por ID
    Dado que nenhum cliente esta cadastrado
    Quando eu cadastrar um cliente PF com nome "Joao Silva" e CPF "11144477735"
    Entao o cliente deve ser criado com status "ATIVO"
    E deve ser possivel consulta-lo pelo ID retornado

  Cenario: Consultar cliente inexistente retorna 404
    Dado que nenhum cliente esta cadastrado
    Quando eu consultar um cliente com ID "999"
    Entao devo receber resposta com status 404
