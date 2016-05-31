package searcher;

import common.domain.NodeType;
import common.domain.Relation;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import searcher.models.SemanticPath;

import java.util.ArrayList;
import java.util.Optional;


public final class Utils {

    static public void closeWindow(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sortida del programa");
        alert.setHeaderText("Estàs segur de tancar el programa?");
        alert.setContentText("Recorda que tots els canvis que no hagis guardat es perdràn si continues.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            stage.close();
            System.exit(0);
        }
    }

    static public void launchAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Alerta");
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    public static String getName(NodeType type) {
        if (type == null) return null;
        switch (type) {
            case AUTHOR:
                return "Autor";
            case PAPER:
                return "Paper";
            case CONF:
                return "Conferència";
            case TERM:
                return "Terme";
            case LABEL:
                return "Etiqueta";
            default:
                return type.toString();
        }
    }

    public static NodeType getType(String name) {
        if (name == null) return null;
        name = removePlural(name).toLowerCase();
        switch (name) {
            case "a":
            case "author":
            case "autor":
                return NodeType.AUTHOR;
            case "p":
            case "paper":
                return NodeType.PAPER;
            case "c":
            case "conf":
            case "conference":
            case "conferència":
                return NodeType.CONF;
            case "t":
            case "term":
            case "terme":
                return NodeType.TERM;
            case "l":
            case "label":
            case "etiqueta":
                return NodeType.LABEL;
            default:
                return null;
        }
    }

    private static StringConverter<NodeType> nodeTypeStringConverter = new StringConverter<NodeType>() {
        @Override
        public String toString(NodeType object) {
            return getName(object);
        }

        @Override
        public NodeType fromString(String string) {
            return getType(string);
        }
    };

    public static StringConverter<NodeType> nodeTypeStringConverter() {
        return nodeTypeStringConverter;
    }

    private static StringConverter<Relation> relationStringConverter = new StringConverter<Relation>() {
        @Override
        public String toString(Relation object) {
            return object.getValue();
        }

        @Override
        public Relation fromString(String string) {
            return null;
        }
    };

    public static StringConverter<Relation> relationStringConverter() {
        return relationStringConverter;
    }

    private static String removePlural(String word) {
        if (word.matches(".+s")) {
            word = word.substring(0, word.length() - 1);
        }
        return word;
    }

    public static String getFirstWord(String line) {
        if (line == null) {
            return "";
        } else {
            String ret = line.split("\\s+", 2)[0].toLowerCase();
            return removePlural(ret);
        }
    }

    public static String getRestOfWords(String line) {
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

    public static Relation getDefaultRelation(NodeType a, NodeType b) {
        if (a.name().compareTo(b.name()) > 0) {
            NodeType tmp = a;
            a = b;
            b = tmp;
        }

        if (a == NodeType.AUTHOR) {
            if (b == NodeType.LABEL) return new Relation(a, b, "Especialitat", 3);
            if (b == NodeType.PAPER) return new Relation(a, b, "Autoria", 0);
        } else if (a == NodeType.CONF) {
            if (b == NodeType.LABEL) return new Relation(a, b, "Tematica", 5);
            if (b == NodeType.PAPER) return new Relation(a, b, "Presentacio", 1);
        } else if (a == NodeType.LABEL) {
            if (b == NodeType.PAPER) return new Relation(b, a, "Ambit", 4);
        } else if (a == NodeType.PAPER) {
            if (b == NodeType.TERM) return new Relation(b, a, "Mencio", 2);
        }
        return null;
    }

    public static Relation[] getDefaultRelations() {
        Relation[] ret = new Relation[6];
        ret[3] = new Relation(NodeType.AUTHOR, NodeType.LABEL, "Es especialista", 3);
        ret[0] = new Relation(NodeType.AUTHOR, NodeType.PAPER, "Es autor", 0);
        ret[5] = new Relation(NodeType.CONF, NodeType.LABEL, "Es tracta ", 5);
        ret[1] = new Relation(NodeType.CONF, NodeType.PAPER, "S'exposa a", 1);
        ret[4] = new Relation(NodeType.PAPER, NodeType.LABEL, "Tracta de", 4);
        ret[2] = new Relation(NodeType.TERM, NodeType.PAPER, "Conte", 2);
        return ret;
    }

    public static String convertToText(NodeType[] nodeTypes) {
        String path = "";
        if (nodeTypes.length == 0) return path;
        for (NodeType nt : nodeTypes) {
            path = path.concat(getName(nt) + " - ");
        }
        return path.substring(0, path.length() - 3);
    }

    public static NodeType[] toTypeArray(SemanticPath semanticPath) {
        return toTypeArray(semanticPath.getInitialType(), semanticPath.getPath());
    }

    public static NodeType[] toTypeArray(NodeType initialType, ArrayList<Relation> path) {
        NodeType[] ret = new NodeType[path.size() + 1];
        NodeType prev = ret[0] = initialType;
        for (int i = 0; i < path.size(); ++i) {
            if (path.get(i).getNodeTypeA() == prev) {
                prev = path.get(i).getNodeTypeB();
            } else {
                prev = path.get(i).getNodeTypeA();
            }
            ret[i + 1] = prev;
        }
        return ret;
    }

    public static void openEdgeEditor() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(searcher.Main.class.getResource("layouts/popups/edges.fxml"));
            AnchorPane pane = loader.load();

            Stage edges = new Stage();
            edges.setTitle("Arestes");
            edges.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(pane);
            edges.setScene(scene);
            edges.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
