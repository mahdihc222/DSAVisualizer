package DataStructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class Queue extends DSAbstract<ItemNode> {

    int queueX = 100;
    int queueY = 200;
    Label arrLabel = new Label("Array implementation: ");
    Label frontLabel = new Label("Front");
    Label rearLabel = new Label("Rear");
    Label frontLabelQueue = new Label("Front");
    Label rearLabelQueue = new Label("Rear");
    int arrImpX = 100;
    int arrImpY = 600;
    List<ItemNode> arrImpNodes = new ArrayList<>();
    int capacity = 2;
    int curSize = 0;
    int frontIdx = -1, rearIdx = -1;
    List<Label> labelList = new ArrayList<>();

    public Queue() {
        initializeControls();
        showCode();
        VisualPage.getControlBox().getChildren().addAll(Controls);
    }

    @Override
    protected void addNode(int val) {


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

        arrImpNodes.add(new ItemNode("", arrImpX, arrImpY, 0)); arrImpX += 40;
        arrImpNodes.add(new ItemNode("", arrImpX, arrImpY, 1)); arrImpX += 40;

        arrLabel.setLayoutX(70); arrLabel.setLayoutY(500);
        arrLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        inputField.setOnAction(e -> {
            enqueueButton.fire(); 
        });

        enqueueButton.setOnAction(e -> {
            String input = inputField.getText().trim();
            if (!input.isEmpty()) {
                try {
                    int value = Integer.parseInt(input);
                    ItemNode node = new ItemNode(value, queueX, queueY);
                    queueX += 40;
                    
                    if((rearIdx + 1) % capacity == frontIdx) {
                        doubleCapacity();
                    }

                    rearIdx = (rearIdx + 1) % capacity;
                    arrImpNodes.get(rearIdx).setElement(value);
                    if(frontIdx == -1) frontIdx = 0;

                    
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
                    if(frontIdx == rearIdx) {
                        arrImpNodes.get(frontIdx).setText("");
                        frontIdx = rearIdx = -1;
                    }
                    else{
                        arrImpNodes.get(frontIdx).setText("");
                        frontIdx = (frontIdx + 1) % capacity;
                    }
                    
                    refresh();
                });
                pause.play();

            }
        });

        Controls.add(layout);
        Collections.addAll(labelList, frontLabel, rearLabel, frontLabelQueue, rearLabelQueue);
    }

    void doubleCapacity() {

        for(int i = 0; i < capacity; ++i) {
            arrImpNodes.add(new ItemNode("", arrImpX, arrImpY, i + capacity)); arrImpX += 40;
        }
        if(frontIdx > rearIdx) {
            for(int i = 0; i <= rearIdx; ++i) {
                arrImpNodes.get(i+capacity).setElement(arrImpNodes.get(i).getElement());
                arrImpNodes.get(i).setText(null);
            }
            rearIdx = rearIdx + capacity;
        }
        
        capacity *= 2;
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
        VisualPage.getAnimationPane().getChildren().addAll(arrImpNodes);
        frontLabel.setLayoutX(100 + frontIdx * 40); frontLabel.setLayoutY(650);
        rearLabel.setLayoutX(100 + rearIdx * 40); rearLabel.setLayoutY(570);
        frontLabelQueue.setLayoutX(50); frontLabelQueue.setLayoutY(210);
        rearLabelQueue.setLayoutX(110+ dataNodes.size() * 40); rearLabelQueue.setLayoutY(210);
        VisualPage.getAnimationPane().getChildren().addAll(labelList);
        
    }

}

