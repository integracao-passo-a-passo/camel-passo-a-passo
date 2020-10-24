package activemq.app.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.sjms2.Sjms2Component;
import org.apache.qpid.jms.JmsConnectionFactory;

public class MyRouteBuilder extends RouteBuilder {
    public void configure() {

        JmsConnectionFactory connFactory = new JmsConnectionFactory("amqp://localhost:5672");

        // Usuário e senha da instância do broker Artemis
        connFactory.setUsername("admin");
        connFactory.setPassword("admin");

        // Configuração do component SJMS 2 para que use QPid JMS
        // como provedor das connection factories
        Sjms2Component component = new Sjms2Component();

        component.setConnectionFactory(connFactory);

        // Mapeamos endpoint sjms2 para o componente previamente configurado
        getContext().addComponent("sjms2", component);

        from("file:src/data?noop=true")
            .choice()
                .when(xpath("/person/city = 'London'"))
                    .log("UK message")
                    .to("sjms2://uk")
                .otherwise()
                    .log("Other message")
                    .to("sjms2://others");
    }

}
