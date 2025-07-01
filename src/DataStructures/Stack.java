package DataStructures;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import Helpers.MyNode;

public class Stack {
    private static List<MyNode> Nodes = new ArrayList<>();
    private static int startingX = 200, startingY = 200;
    private static boolean initializedControls;
    private static List<Node> Controls = new ArrayList<>();

    public static String getCode() {

        try {
            String code = Files.readString(Path.of("src/code/stackHeader.txt"));
            return code;
        } catch (IOException e) {
            return "Failed to load code.";
        }
    }

    public static List<MyNode> getAnimationNodes() {
        return Nodes;
    }

    public static List<Node> getControlNodes(Pane animationPane) {
        if (!initializedControls)
            initializeControls(animationPane);
        return Controls;
    }

    private static void initializeControls(Pane animationPane) {
        initializedControls = true;
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
                    animationPane.getChildren().setAll(Stack.getAnimationNodes());
                } catch (NumberFormatException ex) {
                    
                }
            }
        });

        popButton.setOnAction(e -> {
            Nodes.getLast().flash(Color.RED);
            PauseTransition pause = new PauseTransition(Duration.seconds(0.6));
            pause.setOnFinished(event -> {
                Nodes.removeLast();
                animationPane.getChildren().setAll(Stack.getAnimationNodes());
            });
            pause.play();

        });
        Controls.add(pushRow);
        Controls.add(popRow);
    }

    private static void addNode(int value) {
        MyNode node = new MyNode(value, startingX, startingY - Nodes.size() * 20);
        Nodes.add(node);
        node.flash(Color.LIMEGREEN);
    }
}
