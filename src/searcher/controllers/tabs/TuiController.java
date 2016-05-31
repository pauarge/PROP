package searcher.controllers.tabs;

import common.domain.*;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import searcher.Utils;
import searcher.controllers.BaseController;
import searcher.models.SemanticPath;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static searcher.Utils.closeWindow;


public class TuiController extends BaseController {

    private static final String GLOBAL_HELP = "add\nremove\nprint\nsearch\nimport\nrelevance\nquit";
    private static final String ADD_HELP = "add node node_type node_name\nadd relation name node_type1 [node_type2 ...] node_typen";
    private static final String PRINT_HELP = "print node";
    private static final String PRINT_NODE_HELP = "print node all\nprint node type1 [type2 ...]";
    private static final String IMPORT_HELP = "import nodes path\nimport edges path\nimport all folder_path";
    private static final String EXPORT_HELP = "export file_path";
    private static final String ADD_RELATION_HELP = "add relation name node_type1 [node_type2 ...] node_typen";
    private static final String RELEVANCE_HELP = "relevance relation_name [free]\nrelevance relation_name node_origin_id [node_dest_id]";

    private boolean readyToQuit = false;

    @FXML
    private TextArea console;
    @FXML
    private TextField input;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        console.setText("");
    }

    @FXML
    private void handleInput() {
        String line = input.getText();
        input.clear();
        if (line.equals("clear")) {
            console.clear();
        } else {
            console.appendText('>' + line + '\n');
            console.appendText(executeLine(line) + '\n');
        }
    }

    private String executeLine(String line) {
        String command = Utils.getFirstWord(line);
        String parameters = Utils.getRestOfWords(line);

        switch (command) {
            case "add":
                return executeAdd(parameters);
            case "remove":
                return executeRemove(parameters);
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
            case "help":
                return GLOBAL_HELP;
            case "quit":
                Stage stage = (Stage) console.getScene().getWindow();
                closeWindow(stage);
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
            Relation r = Utils.getDefaultRelation(nta, ntb);
            alr.add(r);

            currentType = nextType;
        }

        try {
            RelationStructure rs = new RelationStructure(firstType, alr, Utils.getType(currentType));
            semanticPaths.add(new SemanticPath(name, firstType, Utils.getType(currentType), rs));
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

    private String executeRemove(String line) {
        String type = Utils.getFirstWord(line);
        String value = Utils.getRestOfWords(line);

        if (type.isEmpty()) {
            return "remove node_type node_id";
        }

        NodeType nodeType = Utils.getType(type);
        if (nodeType == null) {
            return type + " no es un tipus valid de node!";
        }

        if (value.isEmpty()) {
            return "El node ha de tenir un nom!";
        }

        try {
            int nodeId = Integer.parseInt(value);
            graph.removeNode(nodeType, nodeId);
            return "Node eliminat correctament.";
        } catch (GraphException e) {
            e.printStackTrace();
            return "EXCEPTION RAISED!";
        } catch (NumberFormatException e) {
            return "node_id ha de ser un enter.";
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
        for (SemanticPath path : semanticPaths) {
            sb.append(path);
            sb.append(":   ");

            for (Relation r : path.getPath()) {
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
        pc.importGraph(path);
        return "Graf importat amb exit.";
    }

    private String importNode(String params) {
        String path = Utils.getFirstWord(params);
        if (path.isEmpty()) {
            return IMPORT_HELP;
        }
        pc.importNodes(path);
        return "Nodes importats amb èxit.";
    }

    private String importEdge(String params) {
        String path = Utils.getFirstWord(params);
        if (path.isEmpty()) {
            return IMPORT_HELP;
        }
        pc.importEdges(path);
        return "Arestes importades amb èxit.";
    }

    private String executeExport(String path) {
        if (path.isEmpty()) {
            return EXPORT_HELP;
        } else {
            try {
                pc.exportGraph(path);
                return "Session exported successfully";
            } catch (Exception e) {
                e.printStackTrace();
                return "EXCEPTION RAISED!";
            }
        }
    }

    private String executeRelevance(String parameters) {
        String semanticPathName = Utils.getFirstWord(parameters);
        String targets = Utils.getRestOfWords(parameters);
        if (semanticPathName.isEmpty()) return RELEVANCE_HELP;

        String origin = Utils.getFirstWord(targets);
        String dest = Utils.getFirstWord(Utils.getRestOfWords(targets));

        FilteredList<SemanticPath> fl = semanticPaths.filtered(
                (path -> path.getName().equals(semanticPathName))
        );
        if (fl.size() == 0) return "Relacio " + semanticPathName + " no trobada.";
        RelationStructure rs = fl.get(0).getPath();

        GraphSearch graphSearch = null;

        try {
            if (origin.isEmpty() || origin.equals("free")) {
                graphSearch = new FreeSearch(graph, rs);
            } else {
                int originId = Integer.parseInt(origin);
                NodeType originType = fl.get(0).getInitialType();
                Node originNode = graph.getNode(originType, originId);
                if (dest.isEmpty()) {
                    graphSearch = new OriginSearch(graph, rs, originNode);
                } else {
                    int destId = Integer.parseInt(dest);
                    NodeType destType = fl.get(0).getFinalType();
                    Node destNode = graph.getNode(destType, destId);
                    graphSearch = new OriginDestinationSearch(graph, rs, originNode, destNode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        graphSearch.search();
        StringBuilder sb = new StringBuilder();
        for (GraphSearch.Result r : graphSearch.getResults()) {
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

}
