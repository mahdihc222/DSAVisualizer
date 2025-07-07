package DataStructures;

import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Graph extends DSAbstract<ItemNode> {

    int nodeCount = 0;
    boolean isDirected = true;
    ToggleGroup selectionGroup;
    int centerX = 250;
    int centerY = 300;
    int radius = 150;

    public Graph() {
        selectionGroup = new ToggleGroup();
        initializeControls();
        VisualPage.getCodeBox().setText(getCode());
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    }

    public Graph(boolean isDirected) {
        selectionGroup = new ToggleGroup();
        initializeControls();
        VisualPage.getCodeBox().setText(getCode());
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    }

    

    @Override
    public String getCode() {
        // TODO Auto-generated method stub
        return "not implemented yet.";
    }

    @Override
    protected void initializeControls() {
        // TODO Auto-generated method stub
        Button addNodeButton = new Button("Add node");
        TextField nodeField = new TextField();
        nodeField.setPromptText("Enter value");
        Button removeNodeButton = new Button("Remove node");
        HBox nodeRow = new HBox(10, nodeField,  removeNodeButton);

        TextField edgeNodeField1 = new TextField();
        edgeNodeField1.setPromptText("Enter value");
        TextField edgeNodeField2 = new TextField();
        edgeNodeField2.setPromptText("Enter value");
        Button addEdgeButton = new Button("Add Edge");
        Button removeEdgeButton = new Button("Remove Edge");
        HBox edgeRow = new HBox(10, edgeNodeField1, edgeNodeField2, addEdgeButton, removeEdgeButton);

        VBox pushPopBox = new VBox();
        pushPopBox.getChildren().addAll(addNodeButton, nodeRow, edgeRow);

        RadioButton isUndirGraphButton = new RadioButton("Undirected graph");
        RadioButton isDirGraphButton = new RadioButton("Directed graph");

        isDirGraphButton.setToggleGroup(selectionGroup);
        isUndirGraphButton.setToggleGroup(selectionGroup);
        isUndirGraphButton.setSelected(true);
        isUndirGraphButton.setOnAction(e->{
            if(isDirected){
                isDirected = false;
                dataNodes.clear();
                VisualPage.getAnimationPane().getChildren().clear();
                nodeCount = 0;
            }
        });

        isDirGraphButton.setOnAction(e->{
            if(!isDirected){
                isDirected = true;
                dataNodes.clear();
                VisualPage.getAnimationPane().getChildren().clear();
                nodeCount = 0;
            }
        });

        VBox selectionBox = new VBox();
        selectionBox.getChildren().addAll(isUndirGraphButton, isDirGraphButton);

        HBox totalBox = new HBox(50);

        totalBox.getChildren().addAll(pushPopBox,selectionBox);

        addNodeButton.setOnAction(e -> addNode(0));
        nodeField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                removeGraphNode(nodeField);
        });
        removeNodeButton.setOnAction(e -> removeGraphNode(nodeField));
        Controls.add(totalBox);
    }


    @Override
    protected void addNode(int val) {
        // TODO Auto-generated method stub
        nodeCount++;
        dataNodes.clear();
        for (int i = 0; i < nodeCount; i++) {
            double angle = 2 * Math.PI * i / nodeCount;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            ItemNode node = new ItemNode(i+1, (int)x, (int)y, false);  
            dataNodes.add(node);
        }   
        VisualPage.getAnimationPane().getChildren().setAll(dataNodes);
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
        int removeIdx = -1;
        for(int i = 0; i < nodeCount; ++i) {
            if(dataNodes.get(i).getElement() == value) {
                // dataNodes.remove(i);
                removeIdx = i; break;
            }
        }
        if(removeIdx != -1) {
            dataNodes.remove(removeIdx);
        }
        VisualPage.getAnimationPane().getChildren().setAll(dataNodes);
    }

    @Override
    protected void removeLastNode() {
        // TODO Auto-generated method stub
        
    }

    
    
}
