package br.com.autocenterfiap.cadastro.cliente.application.port;

import br.com.autocenterfiap.cadastro.cliente.domain.service.ValidadorDocumento;

public interface ValidadorDocumentoPort {

    ValidadorDocumento obterValidador(String tipoCliente);
}

