package DataStructures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import Helpers.ItemNode;
import Pages.VisualPage;

public class Stack extends DSAbstract<ItemNode> {
    private List<ItemNode> Nodes = new ArrayList<>();
    public Stack(int startingX, int startingY){
        super();
        this.startingX = startingX;
        this.startingY = startingY;
        initializeControls();
        VisualPage.getCodeBox().setText(getCode());
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(Nodes);
    }
    @Override
    public String getCode() {
        try {
            String code = Files.readString(Path.of("src/code/stackHeader.txt"));
            return code;
        } catch (IOException e) {
            return "Failed to load code.";
        }
    }

    @Override
    protected void initializeControls() {
        TextField pushField = new TextField();
        pushField.setPromptText("Enter value");
        Button pushButton = new Button("Push");
        HBox pushRow = new HBox(10, pushField, pushButton);

        Button popButton = new Button("Pop");
        HBox popRow = new HBox(10, popButton);

        pushButton.setOnAction(e -> {
            String input = pushField.getText().trim();

            if (!input.isEmpty()) {
                try {
                    int value = Integer.parseInt(input);
                    addNode(value);
                    pushField.clear();
                    VisualPage.getAnimationPane().getChildren().setAll(Nodes);
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
        ItemNode node = new ItemNode(val, startingX, startingY - Nodes.size() * 20);
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
                VisualPage.getAnimationPane().getChildren().setAll(Nodes);
            });
            pause.play();
        }
    }
}
