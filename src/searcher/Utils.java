package searcher;

import common.domain.NodeType;
import common.domain.Relation;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.StringConverter;

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
        }
    }

    static public void launchAlert(Stage stage, String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
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
            case "author":
            case "autor":
                return NodeType.AUTHOR;
            case "paper":
                return NodeType.PAPER;
            case "conf":
            case "conference":
            case "conferència":
                return NodeType.CONF;
            case "term":
            case "terme":
                return NodeType.TERM;
            case "etiqueta":
            case "label":
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

    public static StringConverter<NodeType> getNodeTypeStringConverter() {
        return nodeTypeStringConverter;
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
            if (b == NodeType.LABEL) return new Relation(a,b,"AuthorLabel",3);
            if (b == NodeType.PAPER) return new Relation(a,b,"AuthorPaper",0);
        } else if (a == NodeType.CONF) {
            if (b == NodeType.LABEL) return new Relation(a,b,"ConferenceLabel",5);
            if (b == NodeType.PAPER) return new Relation(a,b,"ConferencePaper",1);
        } else if (a == NodeType.LABEL) {
            if (b == NodeType.PAPER) return new Relation(b,a,"PaperLabel",4);
        } else if (a == NodeType.PAPER) {
            if (b == NodeType.TERM) return new Relation(b,a,"TermPaper",2);
        }
        return null;
    }

    public static String convertToText(NodeType[] nodeTypes) {
        String path = "";
        if (nodeTypes.length == 0) return path;
        for (NodeType nt : nodeTypes) {
            path = path.concat(getName(nt) + " - ");
        }
        return path.substring(0, path.length()-3);
    }

}
