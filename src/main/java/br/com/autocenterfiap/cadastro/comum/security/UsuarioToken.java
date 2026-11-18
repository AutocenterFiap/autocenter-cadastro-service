package br.com.autocenterfiap.cadastro.comum.security;

public record UsuarioToken(String subject, boolean cliente, Long clienteId) {
}
