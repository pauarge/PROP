package searcher;

import common.domain.Graph;
import common.domain.NodeType;

public class Controller {

    private static final String GLOBAL_HELP = "Global help";
    private static final String COMMAND_NOT_FOUND = "Comanda no reconeguda. Escriu 'help', o una comanda seguit de 'help' per l'ajuda.";
    private static final String ADD_HELP = "Add help";

    private Graph graph;

    Controller() {
        graph = new Graph();
    }

    private String getFirstWord(String line) {
        return line.split("\\s+", 2)[0];
    }

    private String getRestOfWords(String line) {
        String[] words = line.split("\\s+", 2);
        if (words.length == 1) {
            return null;
        } else {
            return words[1];
        }
    }

    private String addNode(String line) {
        String type = getFirstWord(line);
        String value = getRestOfWords(line);

        NodeType nodeType;
        switch (type) {
            case "author":
                nodeType = NodeType.AUTHOR;
                break;
            case "paper":
                nodeType = NodeType.PAPER;
                break;
            case "conference":
                nodeType = NodeType.CONF;
                break;
            case "term":
                nodeType = NodeType.TERM;
                break;
            case "label":
                nodeType = NodeType.LABEL;
                break;
            default:
                return COMMAND_NOT_FOUND;
        }

        if (value == null || value.equals("")) {
            return COMMAND_NOT_FOUND;
        }

        if (value.matches("\\d+\\s+.*")) {
            value = value.replaceFirst("\\d+\\s+", "");
        }

        graph.createNode(nodeType, value);
        return "Node added successfully.";
    }

    private String executeAdd(String line) {
        return null;

        //TODO: Arreglar els problemes amb punters nuls
        /*String command = getFirstWord(line);
        String parameters = getRestOfWords(line);

        if (parameters == null) {
            return ADD_HELP;
        }

        switch (command) {
            case "node":
                return addNode(parameters);
            default:
                return ADD_HELP;
        }*/
    }

    String executeLine(String line) {
        String command = getFirstWord(line);
        String parameters = getRestOfWords(line);

        switch (command) {
            case "":
                return null;
            case "help":
                return GLOBAL_HELP;
            case "quit":
                System.exit(0);
            case "add":
                return executeAdd(parameters);
            default:
                return COMMAND_NOT_FOUND;
        }
    }

}
