package searcher.controllers;

import common.domain.*;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import searcher.models.NodeModel;

import javax.swing.*;
import java.awt.*;
import java.util.*;


public class GraphController {

    private final Graph g;
    private final Map<Node, Boolean> vis = new HashMap<>();
    private final Map<Pair<Node, NodeType>, Pair<Node, NodeType>> prev = new HashMap<>();

    private ArrayList<Relation> getRelationsForTypeAux(NodeType type) {
        ArrayList<Relation> toReturn = new ArrayList<>();
        Iterator it = g.getRelationIterator();
        while (it.hasNext()) {
            Relation r = (Relation) it.next();
            if (r.getNodeTypeA() == type || r.getNodeTypeB() == type) {
                toReturn.add(r);
            }
        }
        return toReturn;
    }

    private String assignColor(NodeType nt) {
        switch (nt) {
            case AUTHOR:
                return "red";
            case PAPER:
                return "black";
            case CONF:
                return "yellow";
            case TERM:
                return "green";
            default:
                return "blue";
        }
    }

    private void spanNode(org.graphstream.graph.Graph graph, common.domain.Graph g, Node nodeInici, NodeType nt, int distance, Set<String> nAfegits) throws GraphException {
        if (distance > 0) {
            ArrayList<Relation> rs = getRelationsForTypeAux(nt);
            for (Relation rel : rs) {
                ArrayList<Node> node_list = g.getEdges(rel.getId(), nodeInici);
                for (Node n2 : node_list) {
                    NodeType nt2;
                    if (nt == rel.getNodeTypeA()) nt2 = rel.getNodeTypeB();
                    else nt2 = rel.getNodeTypeA();
                    String id = nt2.toString() + Integer.toString(n2.getId());
                    if (!nAfegits.contains(id)) {
                        org.graphstream.graph.Node node = graph.addNode(id);
                        node.addAttribute("ui.label", n2.getValue());
                        graph.getNode(id).addAttribute("ui.style", "fill-color:" + assignColor(nt2) + ";");
                        graph.addEdge(id, nt + Integer.toString(nodeInici.getId()), nt2 + Integer.toString(n2.getId()));
                        nAfegits.add(id);
                        spanNode(graph, g, n2, nt2, distance - 1, nAfegits);
                    }
                }
            }
        }
    }

    public GraphController(Graph g) {
        this.g = g;
    }

    public SwingNode getGraph(NodeModel model, int distance) {
        Node node = model.getNode();
        return getGraph(node.getId(), node.getValue(), model.getNodeType(), distance);
    }

    private SwingNode getGraph(int ni, String valor, NodeType nt, int distance) {
        org.graphstream.graph.Graph graph = new SingleGraph("Prova");
        String color = assignColor(nt);
        String id = nt.toString() + Integer.toString(ni);
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

        Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        View view = viewer.addDefaultView(false);
        viewer.enableAutoLayout();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.EXIT);

        SwingNode swingNode = new SwingNode();
        swingNode.setContent((JComponent) view);
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.getChildren().add(swingNode);
        return swingNode;

    }

    private ArrayList shortestPath(Node start, NodeType ntStart, Node finish, NodeType ntFinish) throws GraphException {
        ArrayList<Pair<Node, NodeType>> directions = new ArrayList<>();
        Queue<Pair<Node, NodeType>> q = new LinkedList<>();
        Node current = start;
        Pair<Node, NodeType> p = new Pair<>(current, ntStart);
        q.add(p);
        vis.put(current, true);
        while (!q.isEmpty()) {
            p = q.remove();
            current = p.getKey();
            NodeType ntCurrent = p.getValue();
            if (current.equals(finish) && ntFinish == ntCurrent) {
                break;
            }
            ArrayList<Relation> rs = getRelationsForTypeAux(p.getValue());
            for (Relation rel : rs) {
                ArrayList<Node> node_list = g.getEdges(rel.getId(), current);
                for (Node node : node_list) {
                    if (!vis.containsKey(node)) {
                        NodeType nodeType = rel.getNodeTypeA();
                        if (ntCurrent == rel.getNodeTypeA()) nodeType = rel.getNodeTypeB();
                        Pair<Node, NodeType> p2 = new Pair<>(node, nodeType);
                        q.add(p2);
                        vis.put(node, true);
                        prev.put(p2, p);
                    }
                }
            }
        }
        for (Pair<Node, NodeType> i = new Pair<>(finish, ntFinish); i != null; i = prev.get(i)) {
            directions.add(i);
        }
        return directions;

    }

    public void getShortestPath(Node ini, NodeType ntype1, Node fi, NodeType ntype2) throws GraphException {
        ArrayList<Pair<Node, NodeType>> path = shortestPath(ini, ntype1, fi, ntype2);
        org.graphstream.graph.Graph graph = new SingleGraph("Prova");
        int cont = 0;
        for (Pair<Node, NodeType> n : path) {
            String id = n.getValue() + Integer.toString(n.getKey().getId());
            org.graphstream.graph.Node nodeUI = graph.addNode(id);
            String color = assignColor(n.getValue());
            graph.getNode(nodeUI.getId()).addAttribute("ui.style", "fill-color:" + color + ";");
            nodeUI.addAttribute("ui.label", n.getKey().getValue());
            if (cont > 0) {
                graph.addEdge(id, graph.getNode(cont - 1).getId(), id);
            }
            ++cont;
        }
        graph.addAttribute("ui.stylesheet", "node { size: 15px; text-size: 15px; }");
        Viewer viewer = graph.display();
        View view = viewer.addDefaultView(false);
        JFrame jFrame = new JFrame();
        jFrame.add((Component) view);
        jFrame.setDefaultCloseOperation(2);
        jFrame.setSize(800, 600);
        jFrame.setVisible(true);
    }

}
