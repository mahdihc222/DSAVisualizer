package DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import Helpers.ItemNode;
import Helpers.Edge;
import Pages.VisualPage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.List;
import java.util.Queue;


public class Graph extends DSAbstract<ItemNode> {
    List<Edge> edges = new ArrayList<>();
    
    HashMap<ItemNode, List<ItemNode>> adj = new HashMap<>();
    int nodeCount = 0;
    boolean isDirected = false;
    ToggleGroup selectionGroup;
    int centerX = 250;
    int centerY = 300;
    int radius = 150;

    HashMap<ItemNode, Boolean> vis = new HashMap<>();

    public Graph() {
        selectionGroup = new ToggleGroup();
        initializeControls();
        //VisualPage.getCodeBox().setText(getCode());
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    }


    public Graph(boolean isDirected) {
        selectionGroup = new ToggleGroup();
        initializeControls();
        showCode();
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    }

    @Override
    protected void showCode(){
        Tab dfsTab = new Tab("DFS");
        Tab bfsTab = new Tab("BFS");

        //call getCodeTextArea with appropriate titles
    }

    @Override
    protected void initializeControls() {
        // TODO Auto-generated method stub
        Button addNodeButton = new Button("Add node");
        TextField nodeField = new TextField();
        nodeField.setPrefWidth(100);
        nodeField.setPromptText("Enter value");
        Button removeNodeButton = new Button("Remove node");
        HBox nodeRow = new HBox(10, nodeField, addNodeButton, removeNodeButton);

        TextField edgeNodeField1 = new TextField(); 
        edgeNodeField1.setPrefWidth(100);
        edgeNodeField1.setPromptText("Enter value");
        TextField edgeNodeField2 = new TextField();
        edgeNodeField2.setPromptText("Enter value");
        edgeNodeField2.setPrefWidth(100);
        Button addEdgeButton = new Button("Add Edge");
        Button removeEdgeButton = new Button("Remove Edge");
        HBox edgeRow = new HBox(10, edgeNodeField1, edgeNodeField2, addEdgeButton, removeEdgeButton);

        VBox pushPopBox = new VBox(15);
        pushPopBox.getChildren().addAll( nodeRow, edgeRow);

        RadioButton isUndirGraphButton = new RadioButton("Undirected graph");
        RadioButton isDirGraphButton = new RadioButton("Directed graph");

        isDirGraphButton.setToggleGroup(selectionGroup);
        isUndirGraphButton.setToggleGroup(selectionGroup);
        isUndirGraphButton.setSelected(true);
        isUndirGraphButton.setOnAction(e->{
            if(isDirected){
                isDirected = false;
                dataNodes.clear();
                edges.clear();
                VisualPage.getAnimationPane().getChildren().clear();
                nodeCount = 0;
            }
        });

        isDirGraphButton.setOnAction(e->{
            if(!isDirected){
                isDirected = true;
                dataNodes.clear();
                edges.clear();
                VisualPage.getAnimationPane().getChildren().clear();
                nodeCount = 0;
            }
        });

        VBox selectionBox = new VBox(15);
        selectionBox.getChildren().addAll(isUndirGraphButton, isDirGraphButton);

        HBox totalBox = new HBox(50);

        totalBox.getChildren().addAll(pushPopBox,selectionBox);

        addNodeButton.setOnAction(e -> addGraphNode(nodeField));
        nodeField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                removeGraphNode(nodeField);
        });
        removeNodeButton.setOnAction(e -> removeGraphNode(nodeField));
        addEdgeButton.setOnAction(e-> addGraphEdge(edgeNodeField1, edgeNodeField2));
        removeEdgeButton.setOnAction(e->removeGraphEdge(edgeNodeField1, edgeNodeField2));



        Controls.add(totalBox);
        TextField dfsField = new TextField();
        dfsField.setPromptText("Starting node:");
        Button startDFSbutton = new Button("Start DFS");
        HBox dfsBox = new HBox(10);
        VBox.setMargin(dfsBox, new Insets(15, 0, 0, 0));
        dfsBox.getChildren().addAll(dfsField, startDFSbutton);
        
        startDFSbutton.setOnAction(e->{
            String input = dfsField.getText().trim();
            if (!input.isEmpty()) {
                try {
                    int value = Integer.parseInt(input);
                    ItemNode node = new  ItemNode(value, 0, 0, false);
                    if(!dataNodes.contains(node)) return;
                    for(ItemNode el : dataNodes) if(el.equals(node)) node = el;
                    
                    dfsField.clear();    
                    vis.clear();
                     
                    animateDFS(node);

                } catch (NumberFormatException ex) {
                    return;
                }
            }
            
        });

        Controls.add(dfsBox);

        TextField bfsField = new TextField();
        bfsField.setPromptText("Starting node:");
        Button startBFSbutton = new Button("Start BFS");
        HBox bfsBox = new HBox(10);
        VBox.setMargin(bfsBox, new Insets(15, 0, 0, 0));
        bfsBox.getChildren().addAll(bfsField, startBFSbutton);
        startBFSbutton.setOnAction(e->{
            String input = bfsField.getText().trim();
            if (!input.isEmpty()) {
                try {
                    int value = Integer.parseInt(input);
                    ItemNode node = new  ItemNode(value, 0, 0, false);
                    if(!dataNodes.contains(node)) return;
                    for(ItemNode el : dataNodes) if(el.equals(node)) node = el;
                    
                    bfsField.clear();    
                    vis.clear();
                     
                    animateBFS(node);

                } catch (NumberFormatException ex) {
                    return;
                }
            }
        });
        Controls.add(bfsBox);
        
    }

    private void animateDFS(ItemNode node) {
        List<ItemNode> dfsOrder = new ArrayList<>();
        getDfsOrder(node, dfsOrder);
        List<ItemNode> isGry = new ArrayList<>();
        Timeline timeline = new Timeline();



        for(int i = 0; i < dfsOrder.size(); ++i){
            ItemNode n = dfsOrder.get(i);

            if(!isGry.contains(n)) {
                KeyFrame frame = new KeyFrame(Duration.seconds(i * 1.5), ev -> {
                    n.setNodeColor(Color.GREY);

                });
                timeline.getKeyFrames().add(frame);
                isGry.add(n);

                
            }
            else{
                KeyFrame frame = new KeyFrame(Duration.seconds(i * 1.5), ev -> {
                    n.setNodeColor(Color.BLACK);
                    n.setTextColor(Color.WHITE);

                });
                timeline.getKeyFrames().add(frame);
            }

        }
        KeyFrame frame = new KeyFrame(Duration.seconds(dfsOrder.size() * 1.5), ev -> {
                    for(ItemNode nod : dataNodes) {
                        nod.setNodeColor(Color.WHITE);
                        nod.setTextColor(Color.BLACK);
                    }
                });
                timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private void animateBFS(ItemNode node) {
        List<ItemNode> bfsOrder = new ArrayList<>();
        getBfsOrder(node, bfsOrder);
        List<ItemNode> isGry = new ArrayList<>();
        Timeline timeline = new Timeline();



        for(int i = 0; i < bfsOrder.size(); ++i){
            ItemNode n = bfsOrder.get(i);

            if(!isGry.contains(n)) {
                KeyFrame frame = new KeyFrame(Duration.seconds(i * 1.5), ev -> {
                    n.setNodeColor(Color.GREY);

                });
                timeline.getKeyFrames().add(frame);
                isGry.add(n);

                
            }
            else{
                KeyFrame frame = new KeyFrame(Duration.seconds(i * 1.5), ev -> {
                    n.setNodeColor(Color.BLACK);
                    n.setTextColor(Color.WHITE);

                });
                timeline.getKeyFrames().add(frame);
            }

        }
        KeyFrame frame = new KeyFrame(Duration.seconds(bfsOrder.size() * 1.5), ev -> {
                    for(ItemNode nod : dataNodes) {
                        nod.setNodeColor(Color.WHITE);
                        nod.setTextColor(Color.BLACK);
                    }
                });
                timeline.getKeyFrames().add(frame);
        timeline.play();
    }


    void addGraphNode(TextField nodeField) {
        String input = nodeField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                addNode(value);
                nodeField.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }
    }


    @Override
    protected void addNode(int val) {
        // TODO Auto-generated method stub
        ItemNode newnode = new ItemNode(val, 0, 0, false);
        if(dataNodes.contains(newnode)) return;
        nodeCount++;
        dataNodes.add(newnode);
        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI * i / nodeCount;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            dataNodes.get(i).setLocation((int)x, (int)y);
        }
        for(int i = 0; i < edges.size(); ++i) {
            edges.get(i).redraw();
        }
        adj.put(newnode, new ArrayList<>());
        refresh();
    }


    protected void removeGraphNode(TextField nodeField) {
        // TODO Auto-generated method stub
        String input = nodeField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                removeNode(value);
                nodeField.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }
    }


    private void removeNode(int value) {
        // TODO Auto-generated method stub
        ItemNode node = new ItemNode(value, 0, 0, false);
        int removeIdx = -1;
        for(int i = 0; i < nodeCount; ++i) {
            if(dataNodes.get(i).getElement() == value) {
                // dataNodes.remove(i);
                removeIdx = i; break;
            }
        }
        if(removeIdx != -1) {
            List<Edge> removedEdges = new ArrayList<>();
            for(int i = 0; i < edges.size(); ++i){
                if(edges.get(i).containsNode(dataNodes.get(removeIdx))){
                    removedEdges.add(edges.get(i));
                }
            }
            for(Edge e : removedEdges) {
                edges.remove(e);
            }
            dataNodes.remove(removeIdx);
            nodeCount--;
            adj.get(node).clear();
        }
        refresh();
    }


    @Override
    protected void removeLastNode() {
        // TODO Auto-generated method stub
        
    }


    void addGraphEdge(TextField t1, TextField t2) {
        String input1 = t1.getText().trim();
        String input2 = t2.getText().trim();
        if (!input1.isEmpty() && !input2.isEmpty()) {
            try {
                int value1 = Integer.parseInt(input1);
                int value2 = Integer.parseInt(input2);
                ItemNode n1 = null, n2 = null;
                for(int i = 0; i < nodeCount; ++i) {
                    if(dataNodes.get(i).getElement() == value1) n1 = dataNodes.get(i);
                    if(dataNodes.get(i).getElement() == value2) n2 = dataNodes.get(i);
                }
                if(n1 == null) {
                    addNode(value1);
                    n1 = dataNodes.getLast();
                }
                if(n2 == null) {
                    addNode(value2);
                    n2 = dataNodes.getLast();
                }
                addEdge(n1, n2);
                t1.clear();
                t2.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }

    }


    void removeGraphEdge(TextField t1, TextField t2) {
        String input1 = t1.getText().trim();
        String input2 = t2.getText().trim();
        if (!input1.isEmpty() && !input2.isEmpty()) {
            try {
                int value1 = Integer.parseInt(input1);
                int value2 = Integer.parseInt(input2);
                ItemNode n1 = null, n2 = null;
                for(int i = 0; i < nodeCount; ++i) {
                    if(dataNodes.get(i).getElement() == value1) n1 = dataNodes.get(i);
                    if(dataNodes.get(i).getElement() == value2) n2 = dataNodes.get(i);
                }
                if(n1 == null) {
                    return;
                }
                if(n2 == null) {
                    return;
                }
                removeEdge(n1, n2);
                t1.clear();
                t2.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }
        refresh();
    }


    void addEdge(ItemNode n1, ItemNode n2) { 
        edges.add(new Edge(n1, n2, isDirected));
        refresh();

        adj.get(n1).add(n2);
        if(!isDirected) adj.get(n2).add(n1);
    }

    
    void refresh() {
        VisualPage.getAnimationPane().getChildren().clear();
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
        VisualPage.getAnimationPane().getChildren().addAll(edges);
    }

                                          
    void removeEdge(ItemNode n1, ItemNode n2) {
        for(int i = 0; i < edges.size(); ++i) {
            if(edges.get(i).connects(n1, n2)) {
                edges.remove(i);
                break;
            }
        }
        refresh();
        adj.get(n1).remove(n2);
        adj.get(n2).remove(n1);
    }


    void getDfsOrder(ItemNode node, List<ItemNode> dfsOrder) {
        dfsOrder.add(node);
        vis.put(node, true);
        for(ItemNode e : adj.get(node)){
            if(vis.containsKey(e)) continue;
            getDfsOrder(e, dfsOrder);
        }
        dfsOrder.add(node);
    }


    private void getBfsOrder(ItemNode node, List<ItemNode> bfsOrder) {
        // TODO Auto-generated method stub
        Queue<ItemNode> q = new LinkedList<>();
        q.add(node);
        List<ItemNode> vis = new ArrayList<>();
        vis.add(node);
        bfsOrder.add(node);
        while(!q.isEmpty()) {
            ItemNode cur = q.remove();

            for(ItemNode e : adj.get(cur)) {
                if(vis.contains(e)) continue;
                vis.add(e);
                q.add(e);
                bfsOrder.add(e);
            }

            bfsOrder.add(cur);
        }
    }

}

