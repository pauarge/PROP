package searcher;

import common.domain.NodeType;
import common.domain.Relation;


public class Utils {

    static NodeType getType(String type) {
        if (type == null) return null;
        type = removePlural(type);
        switch (type) {
            case "author":
                return NodeType.AUTHOR;
            case "paper":
                return NodeType.PAPER;
            case "conf":
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

    static private String removePlural(String word) {
        if (word.matches(".+s")) {
            word = word.substring(0, word.length() - 1);
        }
        return word;
    }

    static String getFirstWord(String line) {
        if (line == null) {
            return "";
        } else {
            String ret = line.split("\\s+", 2)[0].toLowerCase();
            return removePlural(ret);
        }
    }

    static String getRestOfWords(String line) {
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

    static Relation getDefaultRelation(NodeType a, NodeType b) {
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
}
