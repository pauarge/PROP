package searcher;

import common.domain.*;
import common.persistence.PersistenceController;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TextController {

    private static final String GLOBAL_HELP = "add\nprint\nsearch\nimport\nrelevance\nquit";
    private static final String ADD_HELP = "add node node_type [node_id] node_name\nadd relation name node_type1 [node_type2 ...] node_typen";
    private static final String PRINT_HELP = "print node";
    private static final String PRINT_NODE_HELP = "print node all\nprint node type1 [type2 ...]";
    private static final String IMPORT_HELP = "import nodes node_type file_path\nimport edges node_type1 node_type2 file_path\nimport all folder_path";
    private static final String EXPORT_HELP = "export file_path";
    private static final String ADD_RELATION_HELP = "add relation name node_type1 [node_type2 ...] node_typen";
    private static final String RELEVANCE_HELP = "relevance relation_name [free]\nrelevance relation_name node_origin_id [node_dest_id]";

    private Graph graph;
    private PersistenceController persistenceController;
    private HashMap<String, RelationStructure> semanticPaths;
    private HashMap<String, Pair<NodeType,NodeType>> semanticExtremes;

    private boolean readyToQuit = false;

    TextController() {
        graph = new Graph();
        persistenceController = new PersistenceController(graph);
        semanticPaths = new HashMap<>();
        semanticExtremes = new HashMap<>();
    }

    boolean isReadyToQuit() {
        return readyToQuit;
    }

    public String executeLine(String line) {
        String command = Utils.getFirstWord(line);
        String parameters = Utils.getRestOfWords(line);

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
                return "Comanda no reconeguda. Escriu 'help' per obtenir un llistat de comandes valides.";
        }
    }

    private String executeAdd(String line) {
        String command = Utils.getFirstWord(line);
        String parameters = Utils.getRestOfWords(line);

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
        String name = Utils.getFirstWord(line);
        String structure = Utils.getRestOfWords(line);

        if (name.isEmpty()) return ADD_RELATION_HELP;

        String currentType = Utils.getFirstWord(structure);
        structure = Utils.getRestOfWords(structure);
        NodeType firstType = Utils.getType(currentType);
        String nextType;
        ArrayList<Relation> alr = new ArrayList<>();
        while (!(nextType = Utils.getFirstWord(structure)).isEmpty()) {
            structure = Utils.getRestOfWords(structure);

            NodeType nta = Utils.getType(currentType);
            NodeType ntb = Utils.getType(nextType);
            Relation r = Utils.getDefaultRelation(nta,ntb);
            alr.add(r);

            currentType = nextType;
        }

        try {
            RelationStructure rs = new RelationStructure(firstType, alr, Utils.getType(currentType));
            semanticPaths.put(name, rs);
            semanticExtremes.put(name, new Pair<>(firstType, Utils.getType(currentType)));
            return "Cami semantic afegit correctament.";
        } catch (Exception e) {
            e.printStackTrace();
            return "EXCEPTION!";
        }
    }

    private String addNode(String line) {
        String type = Utils.getFirstWord(line);
        String value = Utils.getRestOfWords(line);

        NodeType nodeType = Utils.getType(type);

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
        String type = Utils.getFirstWord(line);
        String value = Utils.getRestOfWords(line);

        NodeType nodeType = Utils.getType(type);

        if (nodeType == null) {
            return type + "search node_type search_term";
        }

        if (value.isEmpty()) {
            return "search node_type search_term";
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
        String command = Utils.getFirstWord(line);
        String parameters = Utils.getRestOfWords(line);

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
            NodeType nt = Utils.getType(arg);
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
//        TODO:Implenetar
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
        String command = Utils.getFirstWord(line);
        String args = Utils.getRestOfWords(line);

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
        /*
        String type = Utils.getFirstWord(params);
        String path = Utils.getRestOfWords(params);

        if (path.isEmpty()) {
            return IMPORT_HELP;
        }

        persistenceController.importNodes(path, Utils.getType(type));
        return "Fitxer importat amb exit.";
        */
        // TODO: Nova funcio a persistencia
        return "No implementat";
    }

    private String importEdge(String params) {
        /*
        String type1 = Utils.getFirstWord(params);
        String type2 = Utils.getRestOfWords(params);
        String path = Utils.getRestOfWords(type2);
        type2 = Utils.getFirstWord(type2);

        if (path.isEmpty()) {
            return IMPORT_HELP;
        }

        persistenceController.importEdges(path, Utils.getType(type1), Utils.getType(type2));
        return "Fitxer importat amb exit.";
        */
        // TODO: Nova funcio a persistencia
        return "No implementat";
    }

    private String executeExport(String path) {
        if (path.isEmpty()) {
            return EXPORT_HELP;
        } else {
            try {
                persistenceController.exportGraph(path);
                return "Session exported successfully";
            } catch (Exception e) {
                e.printStackTrace();
                return "EXCEPTION RAISED!";
            }
        }
    }

    private String executeRelevance(String parameters) {
        String semanticPath = Utils.getFirstWord(parameters);
        String targets = Utils.getRestOfWords(parameters);
        if (semanticPath.isEmpty()) return RELEVANCE_HELP;

        String origin = Utils.getFirstWord(targets);
        String dest = Utils.getFirstWord(Utils.getRestOfWords(targets));

        RelationStructure rs = semanticPaths.get(semanticPath);
        if (rs == null) return "Relacio " + semanticPath + " no trobada.";

        GraphSearch graphSearch = null;

        try {
            if (origin.isEmpty() || origin.equals("free")) {
                graphSearch = new FreeSearch(graph, rs);
            } else {
                int originId = Integer.parseInt(origin);
                NodeType originType = semanticExtremes.get(semanticPath).getKey();
                Node originNode = graph.getNode(originType, originId);
                if (dest.isEmpty()) {
                    graphSearch = new OriginSearch(graph, rs, originNode);
                } else {
                    int destId = Integer.parseInt(dest);
                    NodeType destType = semanticExtremes.get(semanticPath).getKey();
                    Node destNode = graph.getNode(destType, destId);
                    graphSearch = new OriginDestinationSearch(graph, rs, originNode, destNode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        graphSearch.search();
        StringBuilder sb = new StringBuilder();
        for (GraphSearch.Result r: graphSearch.getResults()) {
            sb.append("From: ");
            sb.append(r.from.getValue());
            sb.append("\t\t\tTo: ");
            sb.append(r.to.getValue());
            sb.append("\t\t\tRelevance: ");
            sb.append(r.hetesim);
            sb.append('\n');
        }

        return sb.toString();
    }

    private void close() {
        readyToQuit = true;
    }

}
