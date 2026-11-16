package br.com.autocenterfiap.cadastro.cliente.domain.exception;

public class ClienteDocumentoInvalidoException extends DomainException {

    public ClienteDocumentoInvalidoException(String tipoDocumento, String documento) {
        super(String.format("%s inválido: %s", tipoDocumento, documento));
    }
}

