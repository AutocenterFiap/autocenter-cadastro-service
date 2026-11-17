package br.com.autocenterfiap.cadastro.produto.domain.exception;

public class CodigoJaCadastradoException extends RuntimeException {

    public CodigoJaCadastradoException(String codigo) {
        super("Já existe um produto cadastrado com o código: " + codigo);
    }
}
