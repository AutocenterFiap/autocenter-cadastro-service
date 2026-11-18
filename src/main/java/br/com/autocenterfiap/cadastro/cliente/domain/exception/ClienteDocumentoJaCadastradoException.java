package br.com.autocenterfiap.cadastro.cliente.domain.exception;

public class ClienteDocumentoJaCadastradoException extends DomainException {

    public ClienteDocumentoJaCadastradoException(String documento) {
        super(String.format("Cliente com documento %s já foi cadastrado", documento));
    }
}

