package DataStructures;


import java.util.concurrent.atomic.AtomicInteger;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

public class Heap extends DSAbstract<ItemNode> {
    private int currentX, currentY; //current inserting position in animPane
    private Integer index = 1;
    private final int HSPACINGBETWEENNODES = 280;
    private int levels =1;
    private boolean isMinHeap = true; // by default minimum heap is implemented
    //but user can change to maximum heap
    private ToggleGroup selectionGroup; //to keep track of min or max only one is selected
    public Heap() {
        super();
        currentX = startingX = 250;
        currentY = startingY = 100;
        selectionGroup = new ToggleGroup();
        initializeControls();
        //VisualPage.getCodeBox().setText(getCode());
        showCode();
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    }

    public Heap(boolean isMaxHeap) {
        super();
        currentX = startingX = 250;
        currentY = startingY = 100;
        initializeControls();
        showCode();
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    }

    @Override
    protected void showCode() {
        Tab minHeapTab = new Tab("Min Heap",getCodeTextArea("MinHeap"));
        Tab maxHeapTab = new Tab("Max Heap",getCodeTextArea("MaxHeap"));
        minHeapTab.setClosable(false);
        maxHeapTab.setClosable(false);
        VisualPage.getCodePane().getTabs().add(minHeapTab);
        VisualPage.getCodePane().getTabs().add(maxHeapTab);
    }

    

    @Override
    protected void initializeControls() {
        TextField pushField = new TextField();
        pushField.setPromptText("Enter value");
        Button pushButton = new Button("Insert");
        HBox pushRow = new HBox(10, pushField, pushButton);

        Button popButton = new Button("Pop");
        HBox popRow = new HBox(10, popButton);

        VBox pushPopBox = new VBox(10,pushRow,popRow);

        RadioButton isMinHeapButton = new RadioButton("Minimum Heap");
        RadioButton isMaxHeapButton = new RadioButton("Maximum Heap");

        // ToggleGroup selectionGroup = new ToggleGroup();
        isMaxHeapButton.setToggleGroup(selectionGroup);
        isMinHeapButton.setToggleGroup(selectionGroup);
        isMinHeapButton.setSelected(true);
        isMinHeapButton.setOnAction(e->{
            if(!isMinHeap){
                isMinHeap=true;
                
                VisualPage.getAnimationPane().getChildren().clear();
                dataNodes.clear();
                index=1;
                levels =1;
                currentX = startingX;
                currentY = startingY;
                VisualPage.getCodePane().getSelectionModel().select(0);
            }
        });

        isMaxHeapButton.setOnAction(e->{
            if(isMinHeap){
                isMinHeap=false;
                VisualPage.getAnimationPane().getChildren().clear();
                dataNodes.clear();
                levels =1;
                index=1;
                currentX = startingX;
                currentY = startingY;
                VisualPage.getCodePane().getSelectionModel().select(1);
            }
        });
        VBox selectionBox = new VBox();
        selectionBox.getChildren().addAll(isMinHeapButton,isMaxHeapButton);

        HBox totalBox = new HBox(50);


        totalBox.getChildren().addAll(pushPopBox,selectionBox);

        pushButton.setOnAction(e -> pushValue(pushField));
        pushField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                pushValue(pushField);
        });
        popButton.setOnAction(e -> removeLastNode());
        Controls.add(totalBox);
    }

    private void pushValue(TextField pushField) {
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

    @Override
    protected void addNode(int val) {
        if (Integer.highestOneBit(index) == index && index > 1) {
            currentY += 100;
            levels++;
        }
        if (index > 1) {
            double offset = (HSPACINGBETWEENNODES * Math.pow(0.6, levels));

            if (index % 2 == 0)
                currentX = (int) (dataNodes.get(index / 2 - 1).getX() - offset);
            else
                currentX = (int) (dataNodes.get(index / 2 - 1).getX() + offset);
        }
        int parentIndex;
        if (index == 1)
            parentIndex = -1;
        else if ((index - 1) % 2 == 1)
            parentIndex = (index - 1) / 2;
        else
            parentIndex = (index - 2) / 2;
        ItemNode node;
        if (parentIndex < 0)
            node = new ItemNode(val, currentX, currentY, false, null);
        else
            node = new ItemNode(val, currentX, currentY, false, dataNodes.get(parentIndex));
        index++;
        dataNodes.add(node);
        if (index > 2)
            heapify(index - 2);
        VisualPage.getAnimationPane().getChildren().add(node);
    }

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
            VisualPage.getAnimationPane().getChildren().setAll(dataNodes);
            VisualPage.getAnimationPane().getChildren().add(returnLabel);
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

    /*
     *** returns if a has more priority than b, i.e. then a will be in higher position
     * in the heap
     */
    private boolean hasMorePriority(ItemNode a, ItemNode b) {
        if (isMinHeap) {
            if (a.getElement() < b.getElement())
                return true;
            else
                return false;
        } else {
            if (a.getElement() < b.getElement())
                return false;
            else
                return true;
        }
    }

}
