package excessoes.app.camel;

import java.security.InvalidParameterException;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * A Camel Java DSL Router
 */
public class RotaSemTratamento extends RouteBuilder {

    public static int randomWithRange(int min, int max) {
        int range = (max - min) + 1;

        return (int)(Math.random() * range) + min;
    }

    /*
    Configura uma rota baseado no componente timer que gera um número aleatório
    entre 0 e 100. Se o número for maior que 50, gera uma excessão.
    */
    public void configure() {
        from("timer:meuTimer?fixedRate=true&period=1s")
            .process(new Processor(){
                @Override
                public void process(Exchange exchange) throws Exception {
                    int value = randomWithRange(0, 100);

                    exchange.getMessage().setBody(value);
                }
            })
            .to("direct:start");

        from("direct:start")
            .process(new Processor(){
                @Override
                public void process(Exchange exchange) throws Exception {
                    int value = exchange.getMessage().getBody(Integer.class);

                    if (value > 50) {
                        throw new InvalidParameterException("O valor é muito alto!");
                    }
                }
            })
            .to("log:info");
    }

}
