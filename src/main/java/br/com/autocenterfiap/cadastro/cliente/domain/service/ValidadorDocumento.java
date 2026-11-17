package br.com.autocenterfiap.cadastro.cliente.domain.service;

public interface ValidadorDocumento {

    boolean validar(String documento);

    String getTipoDocumento();

    int getTamanhoEsperado();
}

