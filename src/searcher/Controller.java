package searcher;

public class Controller {

    static final private String GLOBAL_HELP = "This is the help placeholder.";

    Controller() {}

    private String getCommand(String line) {
        return line.split("\\s+", 2)[0];
    }

    private String getParams(String line) {
        String[] words = line.split("\\s+", 2);
        if (words.length == 1) {
            return null;
        } else {
            return words[1];
        }
    }

    String executeLine(String line) {
        String command = getCommand(line);
        String parameters = getParams(line);

        switch (command) {
            case "":
                return null;
            case "help":
                return GLOBAL_HELP;
            case "quit":
                System.exit(0);
            default:
                return "Comanda no reconeguda. Escriu 'help' per l'ajuda.";
        }
    }

}
