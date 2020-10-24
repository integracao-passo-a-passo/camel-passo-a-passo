package excessoes.app.camel;

import org.apache.camel.main.Main;

/**
 * A Camel Application
 */
public class MainApp {

    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();

        String tratamento = System.getProperty("tipo", "desligado");

        System.out.printf("Usando tratamento de excessões: %s%n", tratamento);
        switch (tratamento) {
            case "armadilha": {
                main.configure().addRoutesBuilder(new RotaComTratamentoOnException());
                break;
            }
            case "dsl": {
                main.configure().addRoutesBuilder(new RotaComTratamentoDSL());
                break;
            }
            case "errorhandler": {
                main.configure().addRoutesBuilder(new RotaComTratamentoErrorHandler());
                break;
            }
            case "desligado":
            default:
             {
                main.configure().addRoutesBuilder(new RotaSemTratamento());
                break;
            }
        }

        main.run(args);
    }

}

