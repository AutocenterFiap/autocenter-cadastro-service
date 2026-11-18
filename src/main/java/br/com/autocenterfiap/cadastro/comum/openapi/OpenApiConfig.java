package br.com.autocenterfiap.cadastro.comum.openapi;

import br.com.autocenterfiap.cadastro.comum.erro.ErroResposta;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String ESQUEMA = "bearerAuth";
    private static final String PREFIXO_API = "/v1/";
    private static final String SCHEMA_ERRO = "ErroResposta";

    @Bean
    OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info().title("AutoCenter - Cadastro").version("1.0.0")
                        .description("Cadastro de clientes, veículos, serviços e produtos da oficina, com controle "
                                + "de estoque. É consultado pelos serviços de OS, Billing e Execução para validar "
                                + "clientes e veículos e obter descrição e preço do catálogo."))
                .components(new Components().addSecuritySchemes(ESQUEMA,
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                .description("JWT de funcionário, com o perfil interno")))
                .addSecurityItem(new SecurityRequirement().addList(ESQUEMA));
    }

    @Bean
    OpenApiCustomizer respostasPadrao() {
        return openApi -> {
            ModelConverters.getInstance().read(ErroResposta.class).forEach(openApi.getComponents()::addSchemas);
            var corpoErro = new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                    new MediaType().schema(new Schema<>().$ref("#/components/schemas/" + SCHEMA_ERRO)));
            openApi.getPaths().forEach((caminho, item) -> {
                if (!caminho.startsWith(PREFIXO_API)) {
                    return;
                }
                item.readOperations().forEach(operacao -> {
                    var respostas = operacao.getResponses() == null ? new ApiResponses() : operacao.getResponses();
                    respostas.forEach((codigo, resposta) -> {
                        if (ehErro(codigo)) {
                            resposta.setContent(corpoErro);
                        }
                    });
                    respostas.computeIfAbsent("401",
                            c -> new ApiResponse().description("Token ausente, inválido ou expirado"));
                    respostas.computeIfAbsent("403",
                            c -> new ApiResponse().description("Token sem o perfil interno (funcionário)"));
                    operacao.setResponses(respostas);
                });
            });
        };
    }

    private static boolean ehErro(String codigo) {
        return codigo.startsWith("4") || codigo.startsWith("5");
    }
}
