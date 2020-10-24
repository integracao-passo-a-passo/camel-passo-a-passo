package excessoes.app.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class RotaComTratamentoDSL extends RouteBuilder {

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;

        return (int)(Math.random() * range) + min;
    }

    /*
    Configura uma rota baseado no componente timer que gera um número aleatório
    entre 0 e 100. Se o número for maior que 50, gera uma excessão.

    Quando a excessão é gerada, o mecanismo doTry/doCatch é usado para gerenciar a
    excessão naquele ponto da rota e, posteriormente, logar tanto a mensagem da excessão
    gerada bem como o valor que causou a excessão
    */
    public void configure() {
        /*
        Utilizamos o componente timer para gerar números aleatórios a cada 1 segundo
        */
        from("timer:meuTimer?fixedRate=true&period=1s")
            .process(ex -> ex.getMessage().setBody(randomWithRange(0, 100)))
            .to("direct:generated");

        /*
        Definimos um segundo ponto que recebe o número gerado e avalia seu valor.

        Se for maior que 50, lança uma excessão que deverá ser capturada pelo mecanismo
        doTry / doCatch da Java DSL. Caso contrário, loga o valor gerado.
         */
        from("direct:generated")
            .doTry()
                .process(new Processor(){
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        int value = exchange.getMessage().getBody(Integer.class);

                        if (value > 50) {
                            throw new IllegalArgumentException("O valor é muito alto!");
                        }
                    }
                })
                .log("Valor aleatório gerado com sucesso: ${body}")
            .doCatch(IllegalArgumentException.class)
                .log("DSL -> ${exchangeProperty.CamelExceptionCaught} -> ${body}")
            .doFinally()
                .log("Fim do processamento para ${body}")
            .end();
    }

}
