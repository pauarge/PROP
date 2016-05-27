package searcher.controllers;

import common.domain.*;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.AnchorPane;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import searcher.Utils;
import searcher.models.NodeModel;

import javax.swing.*;
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
        switch (s) {
            case "A":
                return "red";
            case "P":
                return "black";
            case "C":
                return "yellow";
            case "T":
                return "green";
            default:
                return "blue";
        }
    }

    private NodeType getNodeType(String s) {
        NodeType nt;
        switch (s) {
            case "A":
                nt = NodeType.AUTHOR;
                break;
            case "P":
                nt = NodeType.PAPER;
                break;
            case "C":
                nt = NodeType.CONF;
                break;
            case "L":
                nt = NodeType.LABEL;
                break;
            default:
                nt = NodeType.TERM;
                break;
        }
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
                for (Node n2 : node_list) {
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

    public SwingNode getGraph(NodeModel model, int distance) {
        Node node = model.getNode();
        NodeType nodeType = model.getNodeType();
        int ni = node.getId();
        String valor = node.getValue();
        String ntstr = Utils.getName(nodeType);
        return getGraph(ni, valor, ntstr, distance);
    }

    public SwingNode getGraph(int ni, String valor, String ntstr, int distance) {
        org.graphstream.graph.Graph graph = new SingleGraph("Prova");
        NodeType nt = Utils.getType(ntstr);
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

      /*  Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        View view = viewer.addDefaultView(false);
        viewer.enableAutoLayout();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);
        SwingNode swingNode = new SwingNode();
        swingNode.setContent((JComponent) view);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(swingNode);
        return anchorPane;*/


        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        View view = viewer.addDefaultView(false);
        viewer.enableAutoLayout();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

        SwingNode swingNode = new SwingNode();
        swingNode.setContent((JComponent) view);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(swingNode);
        return swingNode;

        /*JFrame jFrame = new JFrame();
        jFrame.add((Component) view);
        jFrame.setDefaultCloseOperation(jFrame.DISPOSE_ON_CLOSE);
        jFrame.setSize(800, 600);
        jFrame.setVisible(true);
        return new AnchorPane();*/
    }

}
