package kafka.app.quarkus;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/*
 * Essa anotação é necessária para que a injeção da configuração, feita através
 * da anotação @ConfigProperty, funcione corretamente. De modo geral, não seria
 * necessário e, até mesmo, não recomendável, pois esse escopo tem um ciclo de vida mais
 * complexo e causa um aumento no tempo de inicialização.
 */
@ApplicationScoped
public class KafkaRoute extends RouteBuilder {

    /*
     * Permite que a configuração do endereço do Kafka seja feita através de
     * qualquer um dos mecanismos de configuração suportados pelo Quarkus
     */
    @ConfigProperty(name = "kafka.bootstrap.address")
    String bootstrapAddress;

    @Override
    public void configure() throws Exception {
        fromF("kafka:exemplo?brokers=%s", bootstrapAddress)
            .to("log:info ${body}");
    }
}