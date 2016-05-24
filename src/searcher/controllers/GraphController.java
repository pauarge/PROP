package searcher.controllers;

import common.domain.GraphException;
import common.domain.Node;
import common.domain.NodeType;
import common.domain.Relation;
import common.domain.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GraphController {

    private Graph g;

    private ArrayList<Relation> getRelationsForTypeAux(NodeType type){
        ArrayList<Relation> toReturn = new ArrayList<>();
        Iterator it = g.getRelationIterator();
        while (it.hasNext()) {
            Relation r = (Relation) it.next();
            if(r.getNodeTypeA() == type || r.getNodeTypeB() == type){
                toReturn.add(r);
            }
        }
        return toReturn;
    }

    private String assign(NodeType n) {
        String s;
        if (n == NodeType.AUTHOR) s = "A";
        else if (n == NodeType.PAPER) s = "P";
        else if (n == NodeType.CONF) s = "C";
        else if (n == NodeType.LABEL) s = "L";
        else s = "T";
        return s;
    }

    private String assignColor(String s) {
        if (s == "A") return "red";
        else if (s == "P") return "black";
        else if (s == "C") return "yellow";
        else if (s == "T") return "green";
        else return "blue";
    }

    private NodeType getNodeType(String s) {
        NodeType nt;
        if (s == "A") nt = NodeType.AUTHOR;
        else if (s == "P") nt = NodeType.PAPER;
        else if (s == "C") nt = NodeType.CONF;
        else if (s == "L") nt = NodeType.LABEL;
        else nt = NodeType.TERM;
        return nt;
    }

    private void spanNode(org.graphstream.graph.Graph graph, common.domain.Graph g, Node nodeInici, NodeType nt, int distance, Set<String> nAfegits) throws GraphException {
        if (distance > 0) {
            String s;
            String color;
            s = assign(nt);
            ArrayList<Relation> rs = getRelationsForTypeAux(nt);
            for (Relation rel : rs) {
                ArrayList<Node> node_list = g.getEdges(rel.getId(), nodeInici);
                for (int i = 0; i < node_list.size(); ++i) {
                    Node n2 = node_list.get(i);
                    String s2;
                    if (nt == rel.getNodeTypeA()) s2 = assign(rel.getNodeTypeB());
                    else s2 = assign(rel.getNodeTypeA());
                    color = assignColor(s2);
                    String id = s2 + Integer.toString(n2.getId());
                    if (!nAfegits.contains(id)) {
                        org.graphstream.graph.Node node = graph.addNode(id);
                        node.addAttribute("ui.label", n2.getValue());
                        graph.getNode(id).addAttribute("ui.style", "fill-color:" + color + ";");
                        graph.addEdge(id, s + Integer.toString(nodeInici.getId()), s2 + Integer.toString(n2.getId()));
                        nAfegits.add(id);
                        int d = distance - 1;
                        spanNode(graph, g, n2, getNodeType(s2), d, nAfegits);
                    }
                }
            }
        }
    }

    public GraphController(Graph g){
        this.g = g;
    }

    public void execute(int ni, String valor, String ntstr, int distance) {
        org.graphstream.graph.Graph graph = new SingleGraph("Prova");
        NodeType nt = NodeType.valueOf(ntstr);
        String s = assign(nt);
        String color = assignColor(s);
        String id = s + Integer.toString(ni);
        org.graphstream.graph.Node nIni = graph.addNode(id);
        graph.getNode(nIni.getId()).addAttribute("ui.style", "fill-color:" + color + ";");
        nIni.addAttribute("ui.label", valor);
        Set<String> nodesAfegits = new HashSet<>();
        nodesAfegits.add(id);
        try {
            spanNode(graph, g, g.getNode(nt, ni), nt, distance, nodesAfegits);
        } catch (GraphException e) {
            e.printStackTrace();
        }
        graph.addAttribute("ui.stylesheet", "node { size: 15px; text-size: 15px; }");
        graph.display();
    }

}
