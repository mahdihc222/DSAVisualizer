package DataStructures;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class BST extends DSAbstract<ItemNode>{
    
    private class TreeNode{
        int element;
        TreeNode left;
        TreeNode right;
        int x,y;
        TreeNode(int elem, TreeNode left, TreeNode right){
            element = elem;
            this.left = left;
            this.right = right;
        }
    }

    TreeNode root;
    private int currentX, currentY; //current inserting position in animPane
    //private Integer index = 1;
    private int distance = 140;
    private HashMap<TreeNode,ItemNode> map = new HashMap<>();
    

    public BST() {
        super();
        currentX = startingX = 250;
        currentY = startingY = 100;
        root = null;
        initializeControls();
        VisualPage.getCodeBox().setText(getCode());
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    }

    @Override
    public String getCode() {
        return "...";
    }

    @Override
    protected void initializeControls() {
        TextField pushField = new TextField();
        pushField.setPromptText("Enter value");
        Button pushButton = new Button("Insert");
        HBox pushRow = new HBox(10, pushField, pushButton);

        TextField removeField = new TextField();
        pushField.setPromptText("Enter value");
        Button removeButton = new Button("Remove");
        HBox popRow = new HBox(10, pushField, pushButton);

        VBox pushPopBox = new VBox();
        pushPopBox.getChildren().addAll(pushRow,popRow);

        pushButton.setOnAction(e -> insertValue(pushField));
        pushField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                insertValue(pushField);
        });
        removeButton.setOnAction(e -> removeNode());
        Controls.add(pushPopBox);
    }

    private void insertValue(TextField pushField) {
        String input = pushField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                addNode(value);
                pushField.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }
    }

    private void removeNode(){

    }

    private TreeNode findParentNodeForInsertion(TreeNode curr, int val){
        if(curr.left==null && curr.right == null) return curr;
            if(curr.element>val) return findParentNodeForInsertion(curr.left, val);
            else  return findParentNodeForInsertion(curr.right, val);
    }

    @Override
    protected void addNode(int val) {

        if(root==null){
            root = new TreeNode(val,null, null);
            ItemNode rootItem = new ItemNode(val, startingX, startingY,false,null);
            map.put(root,rootItem);
            dataNodes.add(rootItem);
        }
        else{
            TreeNode parent = findParentNodeForInsertion(root, val);
            TreeNode newNode = new TreeNode(val,null, null);
            double x = map.get(parent).getX();
            double y = map.get(parent).getY()+40;
            if(parent.element>val){
                parent.left = newNode;
                x-=distance;
            }
            else{
                parent.right = newNode;
                x+=distance;
            }
            ItemNode newItem = new ItemNode(val,(int) x,(int) y,false,map.get(parent));
            dataNodes.add(newItem);
            map.put(newNode, newItem);
        }
        VisualPage.getAnimationPane().getChildren().setAll(dataNodes);

    }
    /*
    private void heapify(int index) {
        AtomicInteger i = new AtomicInteger(index);
        AtomicInteger parentIndex = new AtomicInteger();
        if (i.get() % 2 == 1)
            parentIndex.set(i.get() / 2); // for odd parent is at /2
        else
            parentIndex.set((i.get() - 1) / 2); // for even, parent is at (i-1)/2, i.e. for node at index 2 , parent = 0

        runHeapifyStep(i, parentIndex);
    }

    private void runHeapifyStep(AtomicInteger i, AtomicInteger parentIndex) {

        if (!(i.get() > 0 && hasMorePriority(dataNodes.get(i.get()),dataNodes.get(parentIndex.get())))) {
            dataNodes.get(i.get()).setTextColor(Color.BLACK);
            return;
        }
        Timeline timeline = new Timeline();

        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(0), e -> {
                    dataNodes.get(i.get()).setTextColor(Color.BLUE);
                    dataNodes.get(parentIndex.get()).setTextColor(Color.RED);
                }),
                new KeyFrame(Duration.seconds(1), e -> {
                    dataNodes.get(i.get()).exchangeValueWith(dataNodes.get(parentIndex.get()));
                }),
                new KeyFrame(Duration.seconds(2), e -> {
                    dataNodes.get(i.get()).setTextColor(Color.BLACK);
                    i.set(parentIndex.get());
                    if (i.get() % 2 == 1)
                        parentIndex.set(i.get() / 2); // for odd parent is at /2
                    else
                        parentIndex.set((i.get() - 1) / 2); // for even, parent is at (i-1)/2, i.e. for node
                    PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
                    pause.setOnFinished(ev -> {
                        runHeapifyStep(i, parentIndex);
                    });
                    pause.play();
                }));
        timeline.play();

    }

    @Override
    protected void removeLastNode() {
        if (dataNodes.size() <= 0){
            VisualPage.getAnimationPane().getChildren().clear();
            Label returnLabel = new Label("Empty Heap, Nothing to return");
            returnLabel.setFont(new Font("Consolas",14));
            returnLabel.setLayoutX(5);
            returnLabel.setLayoutY(5);
            VisualPage.getAnimationPane().getChildren().add(returnLabel);
            return;
        }
            
        int popped = dataNodes.getFirst().getElement();
        dataNodes.getLast().setTextColor(Color.RED);
        if (dataNodes.size() > 1)
            dataNodes.getFirst().setTextColor(Color.BLUE);
        PauseTransition pause = new PauseTransition(Duration.seconds(0.4));
        pause.setOnFinished(e -> {
            if (dataNodes.size() > 1)
                dataNodes.getLast().exchangeValueWith(dataNodes.getFirst());
            dataNodes.removeLast();
            VisualPage.getAnimationPane().getChildren().clear();
            Label returnLabel = new Label("Returned: "+popped);
            returnLabel.setFont(new Font("Consolas",14));
            returnLabel.setLayoutX(5);
            returnLabel.setLayoutY(5);
            VisualPage.getAnimationPane().getChildren().add(returnLabel);
            VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
            index--;
            if (dataNodes.size() > 1)
                fixHeap(new AtomicInteger(0));
        });
        pause.play();

    }

    private void fixHeap(AtomicInteger i) {
        AtomicInteger left = new AtomicInteger(i.get() * 2 + 1);
        AtomicInteger right = new AtomicInteger(i.get() * 2 + 2);
        AtomicInteger next;
        if (left.get() >= dataNodes.size() && right.get() >= dataNodes.size()) {
            dataNodes.get(i.get()).setTextColor(Color.BLACK);
            return;
        } else if (left.get() >= dataNodes.size())
            next = right;
        else if (right.get() >= dataNodes.size())
            next = left;
        else {
            if (hasMorePriority(dataNodes.get(left.get()), dataNodes.get(right.get()))) {
                next = left;
            } 
            else {
                next = right;
            }
        }

        if (hasMorePriority(dataNodes.get(i.get()), dataNodes.get(next.get()))) {
            dataNodes.get(i.get()).setTextColor(Color.BLACK);
            return;
        }
        Timeline timeline = new Timeline();

        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(0), e -> {
                    dataNodes.get(i.get()).setTextColor(Color.RED);
                    dataNodes.get(next.get()).setTextColor(Color.BLUE);
                }),
                new KeyFrame(Duration.seconds(1), e -> {
                    dataNodes.get(i.get()).exchangeValueWith(dataNodes.get(next.get()));
                }),
                new KeyFrame(Duration.seconds(2), e -> {
                    dataNodes.get(i.get()).setTextColor(Color.BLACK);
                    PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
                    pause.setOnFinished(ev -> {
                        fixHeap(next);
                    });
                    pause.play();
                }));
        timeline.play();

    }

    */

    @Override
    protected void removeLastNode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeLastNode'");
    }

    
}
