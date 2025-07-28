package DataStructures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;

import Helpers.ItemNode;
import Helpers.Edge;
import Pages.VisualPage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.TreeMap;


public class Graph extends DSAbstract<ItemNode> {
    List<Edge> edges = new ArrayList<>();
    List<Label> labels = new ArrayList<>();

    Map<ItemNode, List<ItemNode>> adj = new TreeMap<>(
        Comparator.comparingInt(ItemNode::getElement)
    );
    Label adjListLabel = new Label("Adjacency Lists:");

    int nodeCount = 0;
    boolean isDirected = false;
    ToggleGroup selectionGroup;
    int centerX = 250;
    int centerY = 180;
    int radius = 150;

    List<ItemNode> isGry = new ArrayList<>();
    Timeline timeline;
    

    // dfs related:
    List<ItemNode> dfsOrder = new ArrayList<>();
    boolean dfsIsPaused = false;
    int curDfsIdx = -1;
    List<ItemNode> dfsCallStack = new ArrayList<>();
    int stackX = 550;
    int stackY = 350;
    Label dfsStackLabel = new Label("DFS Call Stack");

    // bfs 
    List<ItemNode> bfsOrder = new ArrayList<>();
    boolean bfsIsPaused = false;
    int curBfsIdx = -1;
    List<ItemNode> bfsQueue =  new ArrayList<>();
    int queueX = 250;
    int queueY = 380;
    Label bfsQueueLabel = new Label("BFS Queue");

    HashMap<ItemNode, Boolean> vis = new HashMap<>();

    public Graph() {
        selectionGroup = new ToggleGroup();
        initializeControls();
        //VisualPage.getCodeBox().setText(getCode());
        VisualPage.getControlBox().getChildren().addAll(Controls);
        // VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
        // VisualPage.getAnimationPane().getChildren().addAll(dfsCallStack);
        // VisualPage.getAnimationPane().getChildren().addAll(bfsQueue);
        dfsStackLabel.setLayoutX(stackX - 30); dfsStackLabel.setLayoutY(stackY + 50);
        dfsStackLabel.setVisible(false);
        dfsStackLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        bfsQueueLabel.setLayoutX(160); bfsQueueLabel.setLayoutY(queueY );
        bfsQueueLabel.setVisible(false);
        bfsQueueLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        adjListLabel.setLayoutX(10); adjListLabel.setLayoutY(420);
        adjListLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        labels.add(adjListLabel); labels.add(dfsStackLabel); labels.add(bfsQueueLabel);
        VisualPage.getAnimationPane().getChildren().addAll(labels);
    }

    // public Graph(boolean isDirected) {
    //     selectionGroup = new ToggleGroup();
    //     initializeControls();
    //     VisualPage.getCodeBox().setText(getCode());
    //     VisualPage.getControlBox().getChildren().addAll(Controls);
    //     VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    //     VisualPage.getAnimationPane().getChildren().addAll(dfsCallStack);

    // }


    @Override
    protected void showCode(){
        Tab dfsTab = new Tab("DFS");
        Tab bfsTab = new Tab("BFS");

        //dfsTab.setContent(getCodeTextArea("DFS"));
        //call getCodeTextArea with appropriate titles
       // VisualPage.getCodePane().getTabs().add(dfsTab);
        //VisualPage.getCodePane().getTabs().forEach(tab-> tab.setClosable(false));

    }

    @Override
    protected void initializeControls() {
        VisualPage.getAnimationPane().getChildren().clear();
        VisualPage.getControlBox().getChildren().clear();

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
        pushPopBox.getChildren().addAll(nodeRow, edgeRow);

        RadioButton isUndirGraphButton = new RadioButton("Undirected graph");
        RadioButton isDirGraphButton = new RadioButton("Directed graph");

        isDirGraphButton.setToggleGroup(selectionGroup);
        isUndirGraphButton.setToggleGroup(selectionGroup);
        isUndirGraphButton.setSelected(true);
        isUndirGraphButton.setOnAction(e -> {
            if (isDirected) {
                isDirected = false;
                dataNodes.clear();
                edges.clear();
                VisualPage.getAnimationPane().getChildren().clear();
                nodeCount = 0;
            }
        });

        isDirGraphButton.setOnAction(e -> {
            if (!isDirected) {
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

        totalBox.getChildren().addAll(pushPopBox, selectionBox);

        addNodeButton.setOnAction(e -> addGraphNode(nodeField));
        nodeField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                removeGraphNode(nodeField);
        });
        removeNodeButton.setOnAction(e -> removeGraphNode(nodeField));
        addEdgeButton.setOnAction(e -> addGraphEdge(edgeNodeField1, edgeNodeField2));
        removeEdgeButton.setOnAction(e -> removeGraphEdge(edgeNodeField1, edgeNodeField2));

        VBox randBox = new VBox();
        Button randButton = new Button("Generate Random Graph");
        randBox.getChildren().addAll(randButton);
        VBox.setMargin(randButton, new Insets(0, 0, 15, 0));

        Controls.add(randBox);
        Controls.add(totalBox);

        randButton.setOnAction(e->{
            Random random = new Random();
            List<Integer> rem = new ArrayList<>();
            for(ItemNode node : dataNodes) rem.add(node.getElement());
            for(int i : rem) removeNode(i);

            int targetEdges = (int) (10);
            for(int i = 0; i < targetEdges; ++i) {
                int n1 = random.nextInt(8) + 1;
                int n2 = random.nextInt(8) + 1;
                if(n1 == n2) {
                    i--; 
                }
                else if(!addEdge(n1, n2)) {
                    i--;
                }
            }
            refresh();
        });

        TextField dfsField = new TextField();
        dfsField.setPromptText("Starting node:");
        Button startDFSbutton = new Button("Start DFS");
        Button previousDFSbutton = new Button("Previous");
        Button nextDFSbutton = new Button("Next");
        Button pauseDFSbutton = new Button("Pause");
        Button resumeDFSbutton = new Button("Resume");
        HBox dfsBox = new HBox(10);
        VBox.setMargin(dfsBox, new Insets(15, 0, 0, 0));
        dfsBox.getChildren().addAll(dfsField, startDFSbutton, previousDFSbutton, nextDFSbutton, pauseDFSbutton,
                resumeDFSbutton);

        startDFSbutton.setOnAction(e -> {
            String input = dfsField.getText().trim();
            if (!input.isEmpty()) {
                try { 
                    int value = Integer.parseInt(input); 
                    ItemNode node = null;
                    for (ItemNode el : dataNodes)
                        if (el.getElement() == value)
                            node = el;
                    if (node == null)
                        return;

                    dfsField.clear();
                    vis.clear();

                    animateDFS(node);

                } catch (NumberFormatException ex) {
                    return;
                }
            }

        });

        previousDFSbutton.setOnAction(e -> {
            if (dfsIsPaused) {
                if (curDfsIdx >= 0) {
                    ItemNode n = dfsOrder.get(curDfsIdx);
                    unHighlightCurDfsNode();
                    if (isGry.contains(n)) { // gray
                        isGry.remove(n);
                        n.setNodeColor(Color.WHITE);
                        n.setTextColor(Color.BLACK);
                        StackRemove();
                    } else { // black
                        isGry.add(n);
                        n.setNodeColor(Color.GREY);
                        n.setTextColor(Color.WHITE);
                        dfsCallStack.add(new ItemNode(n.getElement(), stackX, stackY));
                        stackY -= 40; 
                        highlightCurDfsNode();
                        refresh();
                    }

                    curDfsIdx--;

                }
            }
        });

        nextDFSbutton.setOnAction(e -> { // copied from AnimateDfs
            if (dfsIsPaused) {
                if (curDfsIdx < dfsOrder.size()) {
                    unHighlightCurDfsNode();
                    curDfsIdx++;
                    ItemNode n = dfsOrder.get(curDfsIdx);
                    if (!isGry.contains(n)) { // white
                        isGry.add(n);
                        n.setNodeColor(Color.GREY);
                        n.setTextColor(Color.WHITE);
                        dfsCallStack.add(new ItemNode(n.getElement(), stackX, stackY));
                        stackY -= 40; 
                        highlightCurDfsNode();
                        refresh();
                    } else { // grey
                        isGry.remove(n);
                        n.setNodeColor(Color.BLACK);
                        n.setTextColor(Color.WHITE);
                        StackRemove();
                    }
                }
            }
        });

        pauseDFSbutton.setOnAction(e -> {
            dfsIsPaused = true;
            timeline.pause();
        });

        resumeDFSbutton.setOnAction(e -> {
            if (dfsIsPaused) {
                dfsIsPaused = false;
                timeline.play();
            }
        });

        Controls.add(dfsBox);

        TextField bfsField = new TextField();
        bfsField.setPromptText("Starting node:");
        Button startBFSbutton = new Button("Start BFS");
        Button previousBFSbutton = new Button("Previous");
        Button nextBFSbutton = new Button("Next");
        Button pauseBFSbutton = new Button("Pause");
        Button resumeBFSbutton = new Button("Resume");
        HBox bfsBox = new HBox(10);
        VBox.setMargin(bfsBox, new Insets(15, 0, 0, 0));
        bfsBox.getChildren().addAll(bfsField, startBFSbutton, previousBFSbutton, nextBFSbutton, pauseBFSbutton,
                resumeBFSbutton);
        startBFSbutton.setOnAction(e -> {
            String input = bfsField.getText().trim();
            if (!input.isEmpty()) {
                try {
                    int value = Integer.parseInt(input);
                    ItemNode node = null;
                    for (ItemNode nod : dataNodes) {
                        if (nod.getElement() == value)
                            node = nod;
                    }
                    if (node == null)
                        return;
                    for (ItemNode el : dataNodes)
                        if (el.equals(node))
                            node = el;

                    bfsField.clear();
                    vis.clear();

                    animateBFS(node);

                } catch (NumberFormatException ex) {
                    return;
                }
            }
        });
        Controls.add(bfsBox);

        previousBFSbutton.setOnAction(e -> {
            if (bfsIsPaused) {
                if (curBfsIdx >= 0) { 
                    ItemNode n = bfsOrder.get(curBfsIdx);
                    if (isGry.contains(n)) {
                        isGry.remove(n);
                        n.setNodeColor(Color.WHITE);
                        n.setTextColor(Color.BLACK);
                        bfsQueue.removeLast(); queueX -= 40;
                        refresh();
                    } else { // black
                        isGry.add(n);
                        n.setNodeColor(Color.GREY);
                        n.setTextColor(Color.WHITE);
                        n.highlight();
                        for(ItemNode node : dataNodes) {
                            if(node.getElement() == bfsQueue.getFirst().getElement()) {
                                node.unHighlight();
                            }
                        }
                        for(ItemNode el : bfsQueue) {
                            el.setLocation(el.getX() + 40, el.getY());
                        }
                        bfsQueue.addFirst(new ItemNode(n.getElement(), 100, queueY));
                        queueX += 40;
                        refresh();
                    }

                    curBfsIdx--;

                }
            }
        });

        nextBFSbutton.setOnAction(e -> { // copied from AnimateDfs
            if (bfsIsPaused) {
                if (curBfsIdx < bfsOrder.size()) {
                    curBfsIdx++;
                    ItemNode n = bfsOrder.get(curBfsIdx);
                    if (!isGry.contains(n)) { // white
                        isGry.add(n);
                        n.setNodeColor(Color.GREY);
                        n.setTextColor(Color.WHITE);
                        bfsQueue.add(new ItemNode(n.getElement(), queueX, queueY));
                        queueX += 40; refresh();
                    } else { // grey
                        isGry.remove(n);
                        n.setNodeColor(Color.BLACK);
                        n.setTextColor(Color.WHITE);
                        QueueRemove(); refresh();
                    }
                }
            }
        });

        pauseBFSbutton.setOnAction(e -> {
            bfsIsPaused = true;
            timeline.pause();
        });

        resumeBFSbutton.setOnAction(e -> {
            if (bfsIsPaused) {
                bfsIsPaused = false;
                timeline.play();
            }
        });

    }

    private void animateDFS(ItemNode node) {

        getDfsOrder(node, dfsOrder);
        dfsStackLabel.setVisible(true);

        timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            if (curDfsIdx < dfsOrder.size() - 1) {
                unHighlightCurDfsNode();
                curDfsIdx++;
                ItemNode n = dfsOrder.get(curDfsIdx);
                if (!isGry.contains(n)) { // white
                    isGry.add(n);
                    n.setNodeColor(Color.GREY);
                    n.setTextColor(Color.WHITE);
                    dfsCallStack.add(new ItemNode(n.getElement(), stackX, stackY));
                    stackY -= 40;
                    highlightCurDfsNode();
                    refresh();
                } else { // grey
                    isGry.remove(n);
                    n.setNodeColor(Color.BLACK);
                    n.setTextColor(Color.WHITE);
                    StackRemove(); 
                }
            } else {
                timeline.stop(); // end of animation
                dfsStackLabel.setVisible(false);
                for (ItemNode el : dataNodes) {
                    el.setNodeColor(Color.WHITE);
                    el.setTextColor(Color.BLACK);
                }
                dfsOrder.clear();
                isGry.clear();
                dfsIsPaused = true;
                curDfsIdx = -1;
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();

    }

    private void animateBFS(ItemNode node) {

        getBfsOrder(node, bfsOrder);
        bfsQueueLabel.setVisible(true);

        timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            if (curBfsIdx < bfsOrder.size() - 1) {
                curBfsIdx++;
                ItemNode n = bfsOrder.get(curBfsIdx);
                if (!isGry.contains(n)) { // white
                    isGry.add(n);
                    n.setNodeColor(Color.GREY);
                    n.setTextColor(Color.WHITE);
                    bfsQueue.add(new ItemNode(n.getElement(), queueX, queueY));
                    queueX += 40;
                    refresh();
                } else { // grey
                    isGry.remove(n);
                    n.setNodeColor(Color.BLACK);
                    n.setTextColor(Color.WHITE);
                    QueueRemove();
                    refresh();
                }
            } else {
                timeline.stop();
                bfsQueueLabel.setVisible(false);
                for (ItemNode el : dataNodes) {
                    el.setNodeColor(Color.WHITE);
                    el.setTextColor(Color.BLACK);
                }
                bfsOrder.clear();
                isGry.clear();
                bfsIsPaused = true;
                curBfsIdx = -1;
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
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
        for (ItemNode nod : dataNodes) {
            if (nod.getElement() == val)
                return;
        }

        nodeCount++;
        ItemNode newnode = new ItemNode(val, 0, 0, false);
        dataNodes.add(newnode);
        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI * i / nodeCount;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            dataNodes.get(i).setLocation((int) x, (int) y);
        }
        for (int i = 0; i < edges.size(); ++i) {
            edges.get(i).redraw();
        }
        adj.put(newnode, new ArrayList<>());
        refresh();
    }

    protected void removeGraphNode(TextField nodeField) {
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
        ItemNode nodeToRemove = null;
        for(ItemNode el : dataNodes) {
            if(el.getElement() == value) {
                nodeToRemove = el; break;
            }
        }
        if(nodeToRemove == null) {
            return;
        }

        List<Edge> removedEdges = new ArrayList<>();
        for(Edge e : edges) {
            if (e.containsNode(nodeToRemove)) {
                removedEdges.add(e);
            }
        }
        for (Edge e : removedEdges) {
            edges.remove(e);
        }
        for (List<ItemNode> neighbors : adj.values()) {
            neighbors.remove(nodeToRemove);
        }
        adj.remove(nodeToRemove);

        dataNodes.remove(nodeToRemove);
        nodeCount--;
        
        refresh();
    }

    @Override
    protected void removeLastNode() {
        //Not needed here
    }

    void addGraphEdge(TextField t1, TextField t2) {
        String input1 = t1.getText().trim();
        String input2 = t2.getText().trim();
        if (!input1.isEmpty() && !input2.isEmpty()) {
            try {
                int value1 = Integer.parseInt(input1);
                int value2 = Integer.parseInt(input2);
                ItemNode n1 = null, n2 = null;
                for (int i = 0; i < nodeCount; ++i) {
                    if (dataNodes.get(i).getElement() == value1)
                        n1 = dataNodes.get(i);
                    if (dataNodes.get(i).getElement() == value2)
                        n2 = dataNodes.get(i);
                }
                if (n1 == null) {
                    addNode(value1);
                    n1 = dataNodes.getLast();
                }
                if (n2 == null) {
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
                for (int i = 0; i < nodeCount; ++i) {
                    if (dataNodes.get(i).getElement() == value1)
                        n1 = dataNodes.get(i);
                    if (dataNodes.get(i).getElement() == value2)
                        n2 = dataNodes.get(i);
                }
                if (n1 == null) {
                    return;
                }
                if (n2 == null) {
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

    boolean addEdge(ItemNode n1, ItemNode n2) {
        for(Edge ed : edges) {
            if(ed.connects(n1, n2)) return false;
        }

        edges.add(new Edge(n1, n2, isDirected, true));
        refresh();

        adj.get(n1).add(n2);
        if (!isDirected)
            adj.get(n2).add(n1);
        return true;
    }

    boolean addEdge(int n1, int n2) {
        addNode(n1); addNode(n2);
        ItemNode node1 = null;
        ItemNode node2 = null;
        for(ItemNode node : dataNodes) {
            if(node.getElement() == n1) node1 = node;
            if(node.getElement() == n2) node2 = node;
        }
        return addEdge(node1, node2);
    }

    void refresh() {
        VisualPage.getAnimationPane().getChildren().clear();
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
        VisualPage.getAnimationPane().getChildren().addAll(edges);
        VisualPage.getAnimationPane().getChildren().addAll(dfsCallStack);
        VisualPage.getAnimationPane().getChildren().addAll(bfsQueue);
        VisualPage.getAnimationPane().getChildren().addAll(labels);
        displayAdjList();
        highlightCurBfsNode();
    }

    void createAdjacencyLists() {
        
    }

    void removeEdge(ItemNode n1, ItemNode n2) {
        for (int i = 0; i < edges.size(); ++i) {
            if (edges.get(i).connects(n1, n2)) {
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
        for (ItemNode e : adj.get(node)) {
            if (vis.containsKey(e))
                continue;
            getDfsOrder(e, dfsOrder);
        }
        dfsOrder.add(node);
    }

    private void getBfsOrder(ItemNode node, List<ItemNode> bfsOrder) {
        Queue<ItemNode> q = new LinkedList<>();
        q.add(node);
        List<ItemNode> vis = new ArrayList<>();
        vis.add(node);
        bfsOrder.add(node);
        while (!q.isEmpty()) {
            ItemNode cur = q.remove();

            for (ItemNode e : adj.get(cur)) {
                if (vis.contains(e))
                    continue;
                vis.add(e);
                q.add(e);
                bfsOrder.add(e);
            }

            bfsOrder.add(cur);
        }
    }

    private void StackRemove() {
        dfsCallStack.getLast().flash(Color.RED);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.6));
            pause.setOnFinished(event -> {
                dfsCallStack.removeLast(); 
                highlightCurDfsNode();
                refresh();
            });
            pause.play();
        
        stackY += 40;
    }

    private void QueueRemove() {
        bfsQueue.getFirst().flash(Color.RED);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.6));
            pause.setOnFinished(event -> {
                unHighlightCurBfsNode();
                bfsQueue.removeFirst(); 
                for(ItemNode el : bfsQueue) {
                    el.setLocation(el.getX() - 40, el.getY());
                }
                refresh();
            });
            pause.play();
        
        queueX -= 40;
    }

    private void highlightCurDfsNode() {
        if(!dfsCallStack.isEmpty()) {
                    dfsCallStack.getLast().highlight();
                    int cur = dfsCallStack.getLast().getElement();
                    ItemNode curNode = null;
                    for(ItemNode el : dataNodes) {
                        if(el.getElement() == cur) {
                            curNode = el; break;
                        }
                    }
                    curNode.highlight();
                }
    }

    private void unHighlightCurDfsNode() {
        if(!dfsCallStack.isEmpty()) {
                    dfsCallStack.getLast().unHighlight();
                    int cur = dfsCallStack.getLast().getElement();
                    ItemNode curNode = null;
                    for(ItemNode el : dataNodes) {
                        if(el.getElement() == cur) {
                            curNode = el; break;
                        }
                    }
                    curNode.unHighlight();
                }
    }

    private void highlightCurBfsNode() {
        if(!bfsQueue.isEmpty()) {
                    bfsQueue.getFirst().highlight();
                    int cur = bfsQueue.getFirst().getElement();
                    ItemNode curNode = null;
                    for(ItemNode el : dataNodes) {
                        if(el.getElement() == cur) {
                            curNode = el; break;
                        }
                    }
                    curNode.highlight();
                }
    }

    private void unHighlightCurBfsNode() {
        if(!bfsQueue.isEmpty()) {
                    bfsQueue.getFirst().unHighlight();
                    int cur = bfsQueue.getFirst().getElement();
                    ItemNode curNode = null;
                    for(ItemNode el : dataNodes) {
                        if(el.getElement() == cur) {
                            curNode = el; break;
                        }
                    }
                    curNode.unHighlight();
                }
    }

    void displayAdjList() {
        List<Node> foo = new ArrayList<>();
        int x = 50;
        int y = 450;
        int c = 0;
        for(ItemNode key : adj.keySet()) {
            int listX = x;
            int listY = y + c * 50;
            ItemNode node = new ItemNode(key.getElement(), listX, listY);
            node.highlight();
            ItemNode prev = node;
            foo.add(node);
            for(ItemNode neigh : adj.get(key)) {
                listX += 70;
                ItemNode next = new ItemNode(neigh.getElement(), listX, listY);
                Edge ed = new Edge(prev, next, true, false);
                foo.add(ed);
                foo.add(next);
                prev = next;
            }
            c++;
        }
        VisualPage.getAnimationPane().getChildren().addAll(foo);
    }

}
