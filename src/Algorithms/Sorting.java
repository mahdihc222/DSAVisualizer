package Algorithms;

import java.security.spec.ECGenParameterSpec;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import DataStructures.DSAbstract;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Sorting extends DSAbstract<ItemNode> {

    private int lastX;
    private final int NODEWIDTH = 40;
    private int currentIndex;
    private int arrayX, arrayY;
    private final double DOWN_OFFSET = NODEWIDTH * 0.8;

    public Sorting() {
        super();
        arrayX = lastX = 50;
        arrayY = 100;
        currentIndex = 0;
        initializeControls();

        VisualPage.getControlBox().getChildren().addAll(Controls);
        showCode();
    }

    @Override
    protected void initializeControls() {
        TextField pushField = new TextField();
        pushField.setPromptText("Enter value");
        Button pushButton = new Button("Insert");
        // standardizeButton(pushButton);
        HBox pushRow = new HBox(20, pushField, pushButton);
        pushButton.setOnAction(e -> {
            insert(pushField);
        });
        pushField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                insert(pushField);
            }
        });
        TextField removeField = new TextField();
        removeField.setPromptText("Enter value");
        Button removeButton = new Button("Remove");
        // standardizeButton(removeButton);
        removeButton.setOnAction(e -> {
            delete(removeField);
        });
        HBox popRow = new HBox(20, removeField, removeButton);

        Button randomArrButton = new Button("Generate Random Array");
        randomArrButton.setOnAction(e -> generateRandomArray());
        VBox pushPopBox = new VBox(40, pushRow, popRow, randomArrButton);

        Button bubbleSortButton = new Button("Bubble Sort");
        bubbleSortButton.setOnAction(e -> bubbleSortAnimated());
        Button selectionSortButton = new Button("Selection Sort");
        Button insertionSortButton = new Button("Insertion Sort");
        Button mergeSortButton = new Button("Merge Sort");
        Button quickSortButton = new Button("Quick Sort");

        VBox rightSideControlBox = new VBox(10, bubbleSortButton, selectionSortButton, insertionSortButton,
                mergeSortButton, quickSortButton);

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> clearList());
        VBox clears = new VBox(10, clearButton);
        HBox totalBox = new HBox(20, pushPopBox, rightSideControlBox, clears);

        Controls.add(totalBox);
    }

    @Override
    protected void showCode() {
        Tab bubbleTab = new Tab("Bubble Sort",getCodeTextArea("BubbleSort"));
        Tab insertionSortTab = new Tab("Insertion Sort",getCodeTextArea("InsertionSort"));
        Tab selectionTab = new Tab("Selection Sort",getCodeTextArea("SelectionSort"));
        Tab mergeTab = new Tab("Merge Sort",getCodeTextArea("MergeSort"));
        Tab quickSortTab = new Tab("Quick Sort",getCodeTextArea("QuickSort"));

        VisualPage.getCodePane().getTabs().addAll(bubbleTab,insertionSortTab,selectionTab,mergeTab,quickSortTab);
        VisualPage.getCodePane().getTabs().forEach(tab-> tab.setClosable(false));
    }

    private void delete(TextField removeField) {
        String input = removeField.getText().trim();

        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                removeNode(value);
                removeField.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        } else {
            removeLastNode();
        }
    }

    private void insert(TextField pushField) {
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
        ItemNode newNode = new ItemNode(val, lastX, arrayY, currentIndex);
        dataNodes.add(currentIndex, newNode);
        currentIndex++;
        for (int i = currentIndex; i < dataNodes.size(); i++) {
            ItemNode curNode = dataNodes.get(i);
            curNode.setLocation(curNode.getX() + 10 + NODEWIDTH, curNode.getY());
            curNode.setIndex(i);
        }
        lastX += 10 + NODEWIDTH;

        VisualPage.getAnimationPane().getChildren().add(newNode);
    }

    @Override
    protected void removeLastNode() {
        if (currentIndex > 0) {
            currentIndex--;
            VisualPage.getAnimationPane().getChildren().removeAll(dataNodes);
            dataNodes.get(currentIndex).flash(Color.RED);
            dataNodes.remove(currentIndex);
            for (int i = currentIndex; i < dataNodes.size(); i++) {
                ItemNode curNode = dataNodes.get(i);
                curNode.setLocation(curNode.getX() - (10 + NODEWIDTH), curNode.getY());
                curNode.setIndex(i);
            }
            lastX -= 10 + NODEWIDTH;
            VisualPage.getAnimationPane().getChildren().addAll(dataNodes);

        }
    }

    private void removeNode(int value) {
        VisualPage.getAnimationPane().getChildren().removeAll(dataNodes);
        int i;
        for (i = 0; i < dataNodes.size(); i++) {
            ItemNode curNode = dataNodes.get(i);
            if (curNode.getElement() == value) {
                break;
            }
        }
        // dataNodes.get(i).flash(Color.RED);
        final int INDEXTOREMOVE = i;
        dataNodes.remove(INDEXTOREMOVE);

        for (int j = INDEXTOREMOVE; j < dataNodes.size(); j++) {
            ItemNode nextNode = dataNodes.get(j);
            nextNode.setLocation(nextNode.getX() - (10 + NODEWIDTH), nextNode.getY());
            nextNode.setIndex(j);
        }

        lastX -= (10 + NODEWIDTH);
        if (currentIndex >= INDEXTOREMOVE) {
            currentIndex--;
        }
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    }

    private void generateRandomArray() {
        Random r = new Random();

        for (int i = 0; i < 9; i++) {
            addNode(r.nextInt(98) + 1);
        }
    }

    /** Animates two ItemNodes swapping places: down → sideways → up. */
    private SequentialTransition animateSwap(ItemNode left, ItemNode right) {
        double downOffset = NODEWIDTH * 0.8; // how far down
        double dx = NODEWIDTH+10; // horizontal distance

        // --- LEFT BLOCK ---
        TranslateTransition leftDown = new TranslateTransition(Duration.millis(200), left);
        leftDown.setByY(downOffset);

        TranslateTransition leftSide = new TranslateTransition(Duration.millis(300), left);
        leftSide.setByX(dx);

        TranslateTransition leftUp = new TranslateTransition(Duration.millis(200), left);
        leftUp.setByY(-downOffset);

        SequentialTransition leftSeq = new SequentialTransition(leftDown, leftSide, leftUp);

        // --- RIGHT BLOCK ---
        TranslateTransition rightDown = new TranslateTransition(Duration.millis(200), right);
        rightDown.setByY(downOffset);

        TranslateTransition rightSide = new TranslateTransition(Duration.millis(300), right);
        rightSide.setByX(-dx);

        TranslateTransition rightUp = new TranslateTransition(Duration.millis(200), right);
        rightUp.setByY(-downOffset);

        SequentialTransition rightSeq = new SequentialTransition(rightDown, rightSide, rightUp);

        // run both sides in parallel
        ParallelTransition swapAnim = new ParallelTransition(leftSeq, rightSeq);

        // when finished: restore layoutX/Y & clear translate offsets
        swapAnim.setOnFinished(e -> {
            // reset translation deltas
            left.setTranslateX(0);
            left.setTranslateY(0);
            right.setTranslateX(0);
            right.setTranslateY(0);

            // swap logical positions
            double lx = left.getX(), rx = right.getX();
            left.setLocation(rx, left.getY());
            right.setLocation(lx, right.getY());
        });

        return new SequentialTransition(
                new PauseTransition(Duration.millis(50)), // tiny gap before swap
                swapAnim);
    }

    private void bubbleSortAnimated() {

        SequentialTransition wholeSort = new SequentialTransition(); // master timeline
        int n = dataNodes.size();
        int i;
        for (i = 0; i < n - 1; i++) {
            boolean swappedAny = false;

            for (int j = 0; j < n - i - 1; j++) {
                final int idx = j;

                // colour nodes being compared
                ItemNode A = dataNodes.get(idx);
                ItemNode B = dataNodes.get(idx + 1);

                wholeSort.getChildren().add(
                        animateColorChangePair(A, B, Color.ORANGE)); // highlight compare

                if (A.getElement() > B.getElement()) {
                    swappedAny = true;

                    // visual swap
                    wholeSort.getChildren().add(
                            animateSwap(A, B));

                    // swap in list

                    Collections.swap(dataNodes, idx, idx + 1);
                }

                // reset colour
                wholeSort.getChildren().add(
                        animateColorChangePair(A, B, Color.LIGHTBLUE));
            }

            // mark last sorted
            ItemNode sorted = dataNodes.get(n - i - 1);
            wholeSort.getChildren().add(animateColorChange(sorted, Color.LIGHTGREEN));

            if (!swappedAny)
                break; // list already sorted
        }

        // remaining elements sorted at end
        for (int k = n - i - 1; k >= 0; k--) {
            wholeSort.getChildren().add(
                    animateColorChange(dataNodes.get(k), Color.LIGHTGREEN));
        }

        wholeSort.play();
    }

    /* ---------- small helpers ---------- */

    private PauseTransition animateColorChangePair(ItemNode n1, ItemNode n2, Color color) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
        pause.setOnFinished(e -> {
            n1.setNodeColor(color);
            n2.setNodeColor(color);
        });
        return pause;
    }

    private PauseTransition animateColorChange(ItemNode node, Color color) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.3));
        pause.setOnFinished(e -> node.setNodeColor(color));
        return pause;
    }

    private void clearList() {
        dataNodes.clear();
        VisualPage.getAnimationPane().getChildren().clear();
        currentIndex = 0;
        lastX = 50;

    }

}
