package excessoes.app.camel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class RotaComTratamentoErrorHandler extends RouteBuilder {

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;

        return (int)(Math.random() * range) + min;
    }

    /*
    Configura uma rota baseado no componente timer que gera um número aleatório
    entre 0 e 100. Se o número for maior que 50, gera uma excessão.

    Quando a excessão é gerada, o mecanismo errorHandler, que, neste exemplo, direciona
    as exchanges com erros para um dead letter channel, aonde ela será logada.
    */
    public void configure() {
        errorHandler(deadLetterChannel("direct:errors"));

        /*
        Utilizamos o componente timer para gerar números aleatórios a cada 1 segundo
        */
        from("timer:meuTimer?fixedRate=true&period=1s")
            .process(ex -> ex.getMessage().setBody(randomWithRange(0, 100)))
            .to("direct:generated");

        /*
        Definimos um segundo ponto que recebe o número gerado e avalia seu valor.

        Se for maior que 50, lança uma excessão que deverá ser capturada pelo OnException.
        Caso contrário, loga o valor gerado.
         */
        from("direct:generated")
            .process(new Processor(){
                @Override
                public void process(Exchange exchange) throws Exception {
                    int value = exchange.getMessage().getBody(Integer.class);

                    if (value > 50) {
                        throw new IllegalArgumentException("O valor é muito alto!");
                    }
                }
            })
            .log("Valor aleatório gerado com sucesso: ${body}");

        from("direct:errors")
            .log("errorHandler -> ${exchangeProperty.CamelExceptionCaught} -> ${body}");
    }

}
