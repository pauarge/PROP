package searcher;

public class Controller {

    Controller() {

    }

    String executeLine(String line) {
        switch (line) {
            case "help":
                return "Pagina d'ajuda.";
            default:
                return "Comanda no reconeguda. Escriu help per l'ajuda.";
        }
    }

}
