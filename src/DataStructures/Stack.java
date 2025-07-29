package DataStructures;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import Helpers.ItemNode;
import Pages.VisualPage;

public class Stack extends DSAbstract<ItemNode> {
    Line leftLine, bottomLine, rightLine;

    private List<ItemNode> Nodes = new ArrayList<>();
    public Stack(){
        super();
        this.startingX = 200;
        this.startingY = 400;
        
        initializeControls();
        showCode();
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(Nodes);
    }
    

    @Override
    protected void showCode(){
        Tab arrayStackTab = new Tab("Array Stack");
        arrayStackTab.setContent(getCodeTextArea("ArrayStack"));
        VisualPage.getCodePane().getTabs().add(arrayStackTab);

        Tab llStackTab = new Tab("Linked List Stack");
        llStackTab.setContent(getCodeTextArea("ListStack"));
        VisualPage.getCodePane().getTabs().add(llStackTab);
        VisualPage.getCodePane().getTabs().forEach(tab-> tab.setClosable(false));
    }

    @Override
    protected void initializeControls() {
        TextField pushField = new TextField();
        pushField.setPromptText("Enter value");
        Button pushButton = new Button("Push");
        HBox pushRow = new HBox(10, pushField, pushButton);

        Button popButton = new Button("Pop");
        HBox popRow = new HBox(10, popButton);

        leftLine = new Line(startingX, startingY+40, startingX, startingY - 350);
        bottomLine = new Line(startingX, startingY+40, startingX+40, startingY+40);
        rightLine = new Line(startingX+40, startingY+40, startingX+40, startingY-350);

        pushButton.setOnAction(e -> {
            String input = pushField.getText().trim();

            if (!input.isEmpty()) {
                try {
                    int value = Integer.parseInt(input);
                    addNode(value);
                    pushField.clear();
                    refresh();
                    
                } catch (NumberFormatException ex) {
                    
                }
            }
        });

        popButton.setOnAction(e -> removeLastNode());
        Controls.add(pushRow);
        Controls.add(popRow);

        

    }
    @Override
    protected void addNode(int val) {
        ItemNode node = new ItemNode(val, startingX, startingY - Nodes.size() * 40);
        Nodes.add(node);
        node.flash(Color.LIMEGREEN);
    }

    @Override
    protected void removeLastNode(){
        if(!Nodes.isEmpty()){
            Nodes.getLast().flash(Color.RED);
            PauseTransition pause = new PauseTransition(Duration.seconds(0.6));
            pause.setOnFinished(event -> {
                Nodes.removeLast();
                refresh();
            });
            pause.play();
        }
    }

    void refresh() {    
        VisualPage.getAnimationPane().getChildren().clear();
        VisualPage.getAnimationPane().getChildren().addAll(Nodes);
        VisualPage.getAnimationPane().getChildren().addAll(leftLine);
        VisualPage.getAnimationPane().getChildren().addAll(bottomLine);
        VisualPage.getAnimationPane().getChildren().addAll(rightLine);
    }
}
