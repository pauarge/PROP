package searcher;

import common.domain.*;
import common.persistence.PersistenceController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private static final String GLOBAL_HELP = "add\nprint\nsearch\nimport\nexport\nrelevance\nquit";
    private static final String ADD_HELP = "add node node_type [node_id] node_name\nadd relation name node_type1 [node_type2 ...] node_typen";
    private static final String PRINT_HELP = "print node";
    private static final String PRINT_NODE_HELP = "print node all\nprint node type1 [type2 ...]";
    private static final String IMPORT_HELP = "import nodes node_type file_path\nimport edges node_type1 node_type2 file_path\nimport all folder_path";
    private static final String EXPORT_HELP = "export file_path";
    private static final String ADD_RELATION_HELP = "add relation name node_type1 [node_type2 ...] node_typen";

    private Graph graph;
    private PersistenceController persistenceController;
    private HashMap<String, RelationStructure> semanticPaths;

    private boolean readyToQuit = false;

    private NodeType getType(String type) {
        if (type == null) return null;
        type = removePlural(type);
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

    private String removePlural(String word) {
        if (word.matches(".+s")) {
            word = word.substring(0, word.length() - 1);
        }
        return word;
    }

    private String getFirstWord(String line) {
        if (line == null) {
            return "";
        } else {
            String ret = line.split("\\s+", 2)[0].toLowerCase();
            return removePlural(ret);
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
        persistenceController = new PersistenceController(graph);
        semanticPaths = new HashMap<>();
    }

    boolean isReadyToQuit() {
        return readyToQuit;
    }

    public String executeLine(String line) {
        String command = getFirstWord(line);
        String parameters = getRestOfWords(line);

        switch (command) {
            case "add":
                return executeAdd(parameters);
            case "print":
                return executePrint(parameters);
            case "search":
                return executeSearch(parameters);
            case "import":
                return executeImport(parameters);
            case "export":
                return executeExport(parameters);
            case "relevance":
                return executeRelevance(parameters);
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
            case "relation":
            case "relationship":
                return addRelationship(parameters);
            default:
                return ADD_HELP;
        }
    }

    private String addRelationship(String line) {
        String name = getFirstWord(line);
        String structure = getRestOfWords(line);

        if (name.isEmpty()) return ADD_RELATION_HELP;

        String currentType = getFirstWord(structure);
        structure = getRestOfWords(structure);
        NodeType firstType = getType(currentType);
        String nextType;
        ArrayList<Relation> alr = new ArrayList<>();
        while (!(nextType = getFirstWord(structure)).isEmpty()) {
            structure = getRestOfWords(structure);


            System.err.println("Afegint component: " + currentType + '-' + nextType);
            NodeType nta = getType(currentType);
            NodeType ntb = getType(nextType);
            Relation r = new Relation(nta, ntb, "");
            alr.add(r);

            currentType = nextType;
        }

        try {
            RelationStructure rs = new RelationStructure(firstType, alr, getType(currentType));
            System.err.println("rs created");
            semanticPaths.put(name, rs);
            System.err.println("rs added to map");
            return "Cami semantic afegit correctament";
        } catch (Exception e) {
            e.printStackTrace();
            return "EXCEPTION!";
        }
    }

    private String addNode(String line) {
        String type = getFirstWord(line);
        String value = getRestOfWords(line);

        NodeType nodeType = getType(type);

        if (nodeType == null) {
            return type + " no es un tipus valid de node!";
        }

        if (value.isEmpty()) {
            return "El node ha de tenir un nom!";
        }

        if (value.matches("\\d+\\s+.*")) {
            value = value.replaceFirst("\\d+\\s+", "");
        }

        Node node = graph.createNode(nodeType, value);
        try {
            graph.addNode(node);
            return "Node afegit correctament.";
        } catch (Exception e) {
            e.printStackTrace();
            return "EXCEPTION RAISED!";
        }
    }

    private String executeSearch(String line) {
        String type = getFirstWord(line);
        String value = getRestOfWords(line);

        NodeType nodeType = getType(type);

        if (nodeType == null) {
            return type + " no es un tipus valid de node!";
        }

        if (value.isEmpty()) {
            return "El node ha de tenir un nom!";
        }

        SimpleSearch ss = new SimpleSearch(graph, nodeType, value);
        ss.search();
        StringBuilder sb = new StringBuilder();
        for (GraphSearch.Result r : ss.getResults()) {
            sb.append(r.from.getId());
            sb.append("    ");
            sb.append(r.from.getValue());
            sb.append('\n');
        }
        return sb.toString();
    }

    private String executePrint(String line) {
        String command = getFirstWord(line);
        String parameters = getRestOfWords(line);

        switch (command) {
            case "node":
                return printNode(parameters);
            case "edge":
                return printEdge(parameters);
            case "relation":
            case "relationship":
                return printPaths();
            default:
                return PRINT_HELP;
        }
    }

    private String printNode(String parameter) {
        boolean didSomething = false;
        String[] args = parameter.split("\\s+");
        if (args[0].equals("all")) {
            args = new String[]{"author", "conference", "paper", "label", "term"};
        }
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            NodeType nt = getType(arg);
            if (nt != null) {
                sb.append('\n');
                sb.append(arg);
                sb.append(":\n");
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


    private String printEdge(String parameters) {
        String type1 = getFirstWord(parameters);
        String type2 = getRestOfWords(parameters);
        type2 = getFirstWord(type2);

        return null;
    }

    private String printAllNodeType(NodeType nodeType) {
        StringBuilder sb = new StringBuilder();
        Container.ContainerIterator it = graph.getNodeIterator(nodeType);
        while (it.hasNext()) {
            Node node = (Node) it.next();
            sb.append(node.getId());
            sb.append("    ");
            sb.append(node.getValue());
            sb.append('\n');
        }
        return sb.toString();
    }

    private String printPaths() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String,RelationStructure> e : semanticPaths.entrySet()) {
            sb.append(e.getKey());
            sb.append(":   ");

            for (Relation r : e.getValue()) {
                sb.append(r.toString());
                sb.append("  ");
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private String executeImport(String line) {
        String command = getFirstWord(line);
        String args = getRestOfWords(line);

        switch (command) {
            case "node":
                return importNode(args);
            case "edge":
                return importEdge(args);
            case "all":
                return importAll(args);
            default:
                return IMPORT_HELP;
        }

    }

    private String importAll(String path) {
        if (path.isEmpty()) return IMPORT_HELP;
        persistenceController.importGraph(path);
        return "Graf importat amb exit.";
    }

    private String importNode(String params) {
        String type = getFirstWord(params);
        String path = getRestOfWords(params);

        if (path.isEmpty()) {
            return IMPORT_HELP;
        }

        persistenceController.importNodes(path, getType(type));
        return "Fitxer importat amb exit.";
    }

    private String importEdge(String params) {
        String type1 = getFirstWord(params);
        String type2 = getRestOfWords(params);
        String path = getRestOfWords(type2);
        type2 = getFirstWord(type2);

        if (path.isEmpty()) {
            return IMPORT_HELP;
        }

        persistenceController.importEdges(path, getType(type1), getType(type2));
        return "Fitxer importat amb exit.";
    }

    private String executeExport(String path) {
        //TODO:Arreglar aixo
        return "LA FUNCIO exportGraph FA COSES LLETGES";
  /*      if (path.isEmpty()) {
            return EXPORT_HELP;
        } else {
            try {
                persistenceController.exportGraph(path);
                return "Session exported successfully";
            } catch (Exception e) {
                e.printStackTrace();
                return "EXCEPTION RAISED!";
            }
        }*/
    }

    private String executeRelevance(String parameters) {

        return "RELEVANCE FALTA IMPLEMENTAR";
    }

    private void close() {
        readyToQuit = true;
    }

}
