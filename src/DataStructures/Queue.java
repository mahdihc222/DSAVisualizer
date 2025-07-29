package DataStructures;

import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Queue extends DSAbstract<ItemNode> {

    int queueX = 100;
    int queueY = 200;

    public Queue() {
        initializeControls();
        showCode();
        VisualPage.getControlBox().getChildren().addAll(Controls);
    }

    @Override
    protected void addNode(int val) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initializeControls() {
        Button enqueueButton = new Button("Enqueue");
        TextField inputField = new TextField();
        inputField.setPrefWidth(100);
        inputField.setPromptText("Enter value");
        HBox enqueueRow = new HBox(10, inputField, enqueueButton);

        Button dequeueButton = new Button("Dequeue");
        HBox dequeueRow = new HBox(10, dequeueButton);

        VBox layout = new VBox(15); // 15 is the vertical spacing
        layout.getChildren().addAll(enqueueRow, dequeueRow);

        inputField.setOnAction(e -> {
            enqueueButton.fire(); // Simulates a button click
        });

        enqueueButton.setOnAction(e -> {
            String input = inputField.getText().trim();
            if (!input.isEmpty()) {
                try {
                    int value = Integer.parseInt(input);
                    ItemNode node = new ItemNode(value, queueX, queueY);
                    queueX += 40;
                    dataNodes.add(node);
                    inputField.clear();
                    refresh();
                    node.flash(Color.LIMEGREEN);
                } catch (NumberFormatException ex) {
                    return;
                }
            }
        });

        dequeueButton.setOnAction(e -> {
            if (!dataNodes.isEmpty()) {
                dataNodes.getFirst().flash(Color.RED);
                PauseTransition pause = new PauseTransition(Duration.seconds(0.6));
                pause.setOnFinished(event -> {
                    dataNodes.removeFirst();
                    for (ItemNode node : dataNodes) {
                        node.setLocation(node.getX() - 40, node.getY());
                    }
                    queueX -= 40;
                    refresh();
                });
                pause.play();

            }
        });

        Controls.add(layout);
    }

    @Override
    protected void removeLastNode() {

    }

    @Override
    protected void showCode() {
        Tab queueTab = new Tab("Array Queue");
        queueTab.setContent(getCodeTextArea("ArrayQueue"));

        Tab linkedQueueTab = new Tab("Linked List Queue");
        linkedQueueTab.setContent(getCodeTextArea("ListQueue"));

        VisualPage.getCodePane().getTabs().addAll(queueTab, linkedQueueTab);
        // Disable closing of tabs
        VisualPage.getCodePane().getTabs().forEach(tab -> tab.setClosable(false));

    }

    void refresh() {
        VisualPage.getAnimationPane().getChildren().clear();
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    }

}
