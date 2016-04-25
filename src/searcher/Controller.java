package searcher;

import common.domain.Container;
import common.domain.Graph;
import common.domain.Node;
import common.domain.NodeType;

public class Controller {

    private static final String GLOBAL_HELP = "add\nprint\nquit";
    private static final String ADD_HELP = "add node nodeType [nodeId] nodeName";
    private static final String PRINT_HELP = "print node";
    private static final String PRINT_NODE_HELP = "print node all\nprint node type1 [type2 ...]";

    private Graph graph;

    private boolean readyToQuit = false;

    private NodeType getType(String type) {
        if (type == null) return null;
        switch (type) {
            case "author":
                return NodeType.AUTHOR;
            case "paper":
                return NodeType.PAPER;
            case "conference":
                return NodeType.CONF;
            case "term":
                return NodeType.TERM;
            case "label":
                return NodeType.LABEL;
            default:
                return null;
        }
    }

    private String getFirstWord(String line) {
        if (line == null) {
            return "";
        } else {
            return line.split("\\s+", 2)[0].toLowerCase();
        }
    }

    private String getRestOfWords(String line) {
        if (line == null) {
            return "";
        } else {
            String[] words = line.split("\\s+", 2);
            if (words.length == 1) {
                return "";
            } else {
                return words[1];
            }
        }
    }

    Controller() {
        graph = new Graph();
    }

    boolean isReadyToQuit() {
        return readyToQuit;
    }

    String executeLine(String line) {
        String command = getFirstWord(line);
        String parameters = getRestOfWords(line);

        switch (command) {
            case "add":
                return executeAdd(parameters);
            case "print":
                return executePrint(parameters);
            case "":
                return null;
            case "help":
                return GLOBAL_HELP;
            case "quit":
                this.close();
                return null;
            default:
                return "Comanda no reconeguda. Escriu 'help' per obtenir un llistat de comandes valides";
        }
    }

    private String executeAdd(String line) {
        String command = getFirstWord(line);
        String parameters = getRestOfWords(line);

        switch (command) {
            case "node":
                return addNode(parameters);
            default:
                return ADD_HELP;
        }
    }

    private String addNode(String line) {
        String type = getFirstWord(line);
        String value = getRestOfWords(line);

        NodeType nodeType = getType(type);

        if (nodeType == null) {
            return type + " no es un tipus valid de node!";
        }

        if (value == null || value.equals("")) {
            return "El node ha de tenir un nom!";
        }

        if (value.matches("\\d+\\s+.*")) {
            value = value.replaceFirst("\\d+\\s+", "");
        }

        Node node = graph.createNode(nodeType, value);
        try {
            graph.addNode(node);
            return "Node added successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "EXCEPTION RAISED!";
        }
    }

    private String executePrint(String line) {
        String command = getFirstWord(line);
        String parameters = getRestOfWords(line);

        switch (command) {
            case "node":
                return printNode(parameters);
            default:
                return PRINT_HELP;
        }
    }

    private String printNode(String parameter) {
        boolean didSomething = false;
        String[] args = parameter.split("\\s+");
        if (args[0].equals("all")) {
            args = new String[] {"author", "conference", "paper", "label", "term"};
        }
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            NodeType nt = getType(arg);
            if (nt != null) {
                sb.append('\n' + arg + ":\n");
                sb.append(printAllNodeType(nt));
                didSomething = true;
            }
        }
        if (didSomething) {
            return sb.toString();
        } else {
            return PRINT_NODE_HELP;
        }
    }

    private String printAllNodeType(NodeType nodeType) {
        StringBuilder sb = new StringBuilder();
        Container.ContainerIterator it = graph.getNodeIterator(nodeType);
        while (it.hasNext()) {
            Node node = (Node) it.next();
            sb.append(node.getId() + "    " + node.getValue() + '\n');
        }
        return sb.toString();
    }

    private void close() {
        readyToQuit = true;
    }

}
