package Algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.text.DefaultEditorKit.PasteAction;

import org.w3c.dom.traversal.NodeIterator;

import DataStructures.DSAbstract;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.ParallelCamera;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
        selectionSortButton.setOnAction(e -> selectionSortAnimated());
        Button insertionSortButton = new Button("Insertion Sort");
        insertionSortButton.setOnAction(e -> newInsertionSortAnimated());
        Button mergeSortButton = new Button("Merge Sort");
        mergeSortButton.setOnAction(e -> animateMergeSort());
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
        Tab bubbleTab = new Tab("Bubble Sort", getCodeTextArea("BubbleSort"));
        Tab selectionTab = new Tab("Selection Sort", getCodeTextArea("SelectionSort"));
        Tab insertionSortTab = new Tab("Insertion Sort", getCodeTextArea("InsertionSort"));
        Tab mergeTab = new Tab("Merge Sort", getCodeTextArea("MergeSort"));
        Tab quickSortTab = new Tab("Quick Sort", getCodeTextArea("QuickSort"));

        VisualPage.getCodePane().getTabs().addAll(bubbleTab, selectionTab, insertionSortTab, mergeTab, quickSortTab);
        VisualPage.getCodePane().getTabs().forEach(tab -> tab.setClosable(false));
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
    private SequentialTransition animateSwap(ItemNode left, ItemNode right, int diff) {
        double downOffset = NODEWIDTH * 0.8; // how far down
        double dx = (NODEWIDTH + 10) * diff; // horizontal distance

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

    // md = 1 means right move, =-1 means left move by 1
    private SequentialTransition moveLabel(Label l, int distance, int md) {

        TranslateTransition tr = new TranslateTransition(Duration.millis(100), l);
        tr.setByX(distance * md);

        return new SequentialTransition(new PauseTransition(Duration.seconds(0.1)), tr);
    }

    private void bubbleSortAnimated() {

        SequentialTransition wholeSort = new SequentialTransition(); // master timeline
        int n = dataNodes.size();
        int i;
        Label iLabel = new Label("i");
        iLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
        iLabel.setTextFill(Color.RED);

        iLabel.setLayoutX(dataNodes.getFirst().getX() + 10);
        iLabel.setLayoutY(arrayY - 15);
        Label jLabel = new Label("j");
        jLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
        jLabel.setTextFill(Color.GREEN);
        jLabel.setLayoutX(dataNodes.getFirst().getX() + NODEWIDTH / 2);
        jLabel.setLayoutY(arrayY - 15);
        VisualPage.getAnimationPane().getChildren().addAll(iLabel, jLabel);
        for (i = 0; i < n - 1; i++) {
            boolean swappedAny = false;
            if (i > 0) {
                wholeSort.getChildren().add(moveLabel(iLabel, NODEWIDTH + 10, 1));
                wholeSort.getChildren().add(moveLabel(jLabel, (n - i) * (NODEWIDTH + 10), -1));
            }

            for (int j = 0; j < n - i - 1; j++) {
                final int idx = j;

                // colour nodes being compared
                ItemNode A = dataNodes.get(idx);
                ItemNode B = dataNodes.get(idx + 1);

                wholeSort.getChildren().add(animateColorChangePair(A, B, Color.ORANGE)); // highlight compare

                if (A.getElement() > B.getElement()) {
                    swappedAny = true;

                    // visual swap
                    wholeSort.getChildren().add(animateSwap(A, B, 1));

                    // swap in list

                    Collections.swap(dataNodes, idx, idx + 1);
                    PauseTransition pause = new PauseTransition(Duration.seconds(0.01));
                    pause.setOnFinished(e -> {
                        A.setIndex(idx + 1);
                        B.setIndex(idx);
                    });
                    wholeSort.getChildren().add(pause);
                }

                // reset colour
                wholeSort.getChildren().add(animateColorChangePair(A, B, Color.LIGHTBLUE));
                wholeSort.getChildren().add(moveLabel(jLabel, NODEWIDTH + 10, 1));
            }

            // mark last sorted
            ItemNode sorted = dataNodes.get(n - i - 1);
            wholeSort.getChildren().add(animateColorChange(sorted, Color.LIGHTGREEN));

            if (!swappedAny)
                break; // list already sorted
        }
        PauseTransition labelRemover = new PauseTransition(Duration.seconds(0.01));
        labelRemover.setOnFinished(e -> {
            VisualPage.getAnimationPane().getChildren().remove(iLabel);
            VisualPage.getAnimationPane().getChildren().remove(jLabel);
        });
        wholeSort.getChildren().add(labelRemover);
        // remaining elements sorted at end
        for (int k = n - i - 1; k >= 0; k--) {
            wholeSort.getChildren().add(animateColorChange(dataNodes.get(k), Color.LIGHTGREEN));
        }

        wholeSort.getChildren().add(setColorOfAll(dataNodes, Color.WHITE));

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

    private PauseTransition animateColorChangeFast(ItemNode node, Color color) {
        PauseTransition pause = new PauseTransition(Duration.millis(1));
        pause.setOnFinished(e -> node.setNodeColor(color));
        return pause;
    }

    private PauseTransition setColorOfAll(List<ItemNode> nodes, Color color) {
        PauseTransition pause = new PauseTransition(Duration.seconds(0.2));
        pause.setOnFinished(e -> {
            for (ItemNode node : nodes) {
                node.setNodeColor(color);
            }
        });
        return pause;
    }

    private void newInsertionSortAnimated() {
        int i, j = 0;
        int n = dataNodes.size();
        Label keylabel = new Label("Key");
        keylabel.setFont(Font.font("Consolas", FontWeight.SEMI_BOLD, 12));
        keylabel.setTextFill(Color.RED);
        keylabel.setLayoutX(dataNodes.getFirst().getX() + NODEWIDTH / 2 + NODEWIDTH + 10 - 20);
        keylabel.setLayoutY(dataNodes.getFirst().getY() - 20);
        VisualPage.getAnimationPane().getChildren().add(keylabel);
        SequentialTransition master = new SequentialTransition();
        for (i = 1; i < n; i++) {
            master.getChildren().add(moveLabel(keylabel, (i - j - 1) * (NODEWIDTH + 10), 1));
            for (int k = 0; k < i; k++) {
                master.getChildren().add(animateColorChangeFast(dataNodes.get(k), Color.GREEN));
            }
            master.getChildren().add(animateColorChange(dataNodes.get(i), Color.ORANGE));
            j = i - 1;
            while (j >= 0 && dataNodes.get(j).getElement() > dataNodes.get(j + 1).getElement()) {

                ItemNode A = dataNodes.get(j);
                ItemNode B = dataNodes.get(j + 1);
                master.getChildren().add(animateColorChangePair(A, B, Color.ORANGE));
                master.getChildren().add(animateSwap(A, B, 1));
                master.getChildren().add(moveLabel(keylabel, NODEWIDTH + 10, -1));
                master.getChildren().add(animateColorChangePair(A, B, Color.SKYBLUE));
                PauseTransition pause = new PauseTransition(Duration.seconds(0.01));
                final int jj = j;
                pause.setOnFinished(e -> {
                    A.setIndex(jj + 1);
                    B.setIndex(jj);
                });
                master.getChildren().add(pause);
                Collections.swap(dataNodes, j, j + 1);

                j--;
            }
        }
        PauseTransition pr = new PauseTransition(Duration.millis(50));
        pr.setOnFinished(e -> {
            VisualPage.getAnimationPane().getChildren().remove(keylabel);
        });
        master.getChildren().add(pr);
        for (int k = 0; k < n; k++)
            master.getChildren().add(animateColorChangeFast(dataNodes.get(k), Color.WHITE));

        master.play();
    }

    // //depricated
    private void insertionSortAnimated() {
        Label keyLabel = new Label("Key: ");
        keyLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        keyLabel.setLayoutX(arrayX + 10);
        keyLabel.setLayoutY(arrayY + 150);
        VisualPage.getAnimationPane().getChildren().add(keyLabel);

        SequentialTransition master = new SequentialTransition();
        int n = dataNodes.size();

        for (int i = 1; i < n; ++i) {
            final int ii = i;
            // Color sorted part
            for (int k = 0; k < i; k++) {
                final int kk = k;
                master.getChildren().add(animateColorChange(dataNodes.get(kk), Color.LIMEGREEN));
            }

            ItemNode innerKeyNode = duplicateItemNode(dataNodes.get(ii));
            PauseTransition initializerPause = new PauseTransition(Duration.seconds(0.1));
            initializerPause.setOnFinished(e -> {
                VisualPage.getAnimationPane().getChildren().add(innerKeyNode);
            });
            master.getChildren().add(initializerPause);
            // master.getChildren().add(moveNode(innerKeyNode, innerKeyNode.getLayoutX() +
            // innerKeyNode.getTranslateX()
            // - (keyLabel.getLayoutX() + keyLabel.getTranslateX()), -1));
            master.getChildren().add(moveNode(innerKeyNode, 150, -2));

            // Use a helper to animate the inner loop
            master.getChildren().add(animateInsertionInnerLoop(ii - 1, innerKeyNode));
            PauseTransition pr = new PauseTransition(Duration.millis(1));
            pr.setOnFinished(e -> {
                System.out.print("Current data: ");
                for (ItemNode node : dataNodes)
                    System.out.print(node.getElement() + " ");
                System.out.println();
            });
            master.getChildren().add(pr);

        }

        master.play();
    }

    /**
     * Recursively animates the inner loop of insertion sort for index j.
     * Returns a SequentialTransition that performs the necessary steps.
     */
    private SequentialTransition animateInsertionInnerLoop(int j, ItemNode keyNode) {
        SequentialTransition seq = new SequentialTransition();

        if (j < 0) {
            System.out.println("Now j: " + j + ", terminating with keyNode: " + keyNode.getElement());
            seq.getChildren()
                    .add(animateKeyGoingUp(keyNode, dataNodes.get(j + 1), 0));
            return seq;
        }

        if (dataNodes.get(j).getElement() <= keyNode.getElement()) {
            // Place the keyNode at the correct position
            System.out.println("Now j: " + j + ", terminating with keyNode: " + keyNode.getElement() + " jnode: "
                    + dataNodes.get(j).getElement());
            seq.getChildren()
                    .add(animateKeyGoingUp(keyNode, dataNodes.get(j + 1), dataNodes.indexOf(keyNode) - (j + 1)));
            return seq;
        }
        System.out
                .println("Now j: " + j + ", key: " + keyNode.getElement() + " jNode: " + dataNodes.get(j).getElement());

        // Animate comparison
        seq.getChildren().add(animateColorChangePair(dataNodes.get(j), dataNodes.get(j + 1), Color.ORANGE));
        // Animate shift
        seq.getChildren().add(animateValueShiftRight(dataNodes.get(j), dataNodes.get(j + 1)));
        // Reset color
        seq.getChildren().add(animateColorChangePair(dataNodes.get(j), dataNodes.get(j + 1), Color.SKYBLUE));
        // dataNodes.set(j + 1, dataNodes.get(j));
        // Recursively animate the next step
        seq.getChildren().add(animateInsertionInnerLoop(j - 1, keyNode));
        return seq;
    }

    private SequentialTransition animateKeyGoingUp(ItemNode A, ItemNode B, int diff) {
        double dy = -150;
        double dx = (NODEWIDTH + 10) * diff;
        // double dx = -100;
        TranslateTransition rightMove = new TranslateTransition(Duration.millis(300),
                A);
        rightMove.setByX(dx);
        TranslateTransition upMove = new TranslateTransition(Duration.millis(300),
                A);
        upMove.setByY(dy);

        // when finished: restore layoutX/Y & clear translate offsets
        PauseTransition pr = new PauseTransition(Duration.millis(200));
        pr.setOnFinished(e -> {
            B.setElement(A.getElement());
            VisualPage.getAnimationPane().getChildren().remove(A);
            // int idxB = dataNodes.indexOf(B);
            // if (idxB != -1) {
            // dataNodes.set(idxB, A); // A is the key node
            // }
        });
        SequentialTransition seq = new SequentialTransition(rightMove, upMove, pr);

        return new SequentialTransition(
                new PauseTransition(Duration.millis(50)), // tiny gap before swap
                seq);
    }

    // this simulates shifting of vaue of A to B
    private SequentialTransition animateValueShiftRight(ItemNode A, ItemNode B) {
        final ItemNode temp = duplicateItemNode(A);
        PauseTransition pr1 = new PauseTransition(Duration.millis(10));
        pr1.setOnFinished(e -> {

            temp.setNodeColor(Color.CORAL);
            VisualPage.getAnimationPane().getChildren().add(temp);
        });

        SequentialTransition st = new SequentialTransition(pr1);
        st.getChildren().add(moveNode(temp, NODEWIDTH * 1.6, -2));
        st.getChildren().add(moveNode(temp, NODEWIDTH + 10, 1));
        st.getChildren().add(moveNode(temp, NODEWIDTH * 1.6, 2));
        PauseTransition pr = new PauseTransition(Duration.millis(10));
        pr.setOnFinished(e -> {
            B.setElement(A.getElement());
            VisualPage.getAnimationPane().getChildren().remove(temp);
            // int idxA = dataNodes.indexOf(A);
            // int idxB = dataNodes.indexOf(B);
            // if (idxA != -1 && idxB != -1) {
            // dataNodes.set(idxB, dataNodes.get(idxA));
            // }
        });
        st.getChildren().add(pr);
        return st;

    }

    private SequentialTransition moveNode(Node n, double distance, int dir) {
        ItemNode it = (ItemNode) n;
        TranslateTransition tr = new TranslateTransition(Duration.seconds(0.2), it);
        switch (dir) {
            case 1: // RIGHT
                tr.setByX(distance);
                break;
            case -1: // LEFT
                tr.setByX(distance * -1);
                break;
            case 2: // UP
                tr.setByY(distance * -1);
                break;
            case -2:// DOWN
                tr.setByY(distance);
                break;
            default:
                break;
        }
        return new SequentialTransition(tr);
    }

    private void selectionSortAnimated() {
        int min_id;
        int n = dataNodes.size();

        // label initialization
        Label iLabel = new Label("i");
        iLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
        iLabel.setTextFill(Color.RED);

        iLabel.setLayoutX(dataNodes.getFirst().getX() + 10);
        iLabel.setLayoutY(arrayY - 15);
        Label jLabel = new Label("j");
        jLabel.setFont(Font.font("Consolas", FontWeight.BOLD, 12));
        jLabel.setTextFill(Color.GREEN);
        jLabel.setLayoutX(dataNodes.getFirst().getX() + NODEWIDTH / 2);
        jLabel.setLayoutY(arrayY - 15);
        VisualPage.getAnimationPane().getChildren().addAll(iLabel, jLabel);
        SequentialTransition master = new SequentialTransition();
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                master.getChildren().add(moveLabel(iLabel, NODEWIDTH + 10, 1));
                master.getChildren().add(moveLabel(jLabel, (n - i - 1) * (NODEWIDTH + 10), -1));
            }
            min_id = i;
            master.getChildren().add(animateColorChange(dataNodes.get(min_id), Color.ORANGE));
            for (int j = i + 1; j < n; ++j) {
                master.getChildren().add(moveLabel(jLabel, NODEWIDTH + 10, 1));
                master.getChildren().add(animateColorChange(dataNodes.get(j), Color.SKYBLUE));
                if (dataNodes.get(j).getElement() < dataNodes.get(min_id).getElement()) {

                    master.getChildren().add(animateColorChange(dataNodes.get(min_id), Color.SKYBLUE));
                    min_id = j;
                    master.getChildren().add(animateColorChange(dataNodes.get(min_id), Color.ORANGE));
                }

            }
            if (min_id != i) {
                master.getChildren().add(animateSwap(dataNodes.get(i), dataNodes.get(min_id), min_id - i));
                ItemNode A = dataNodes.get(i);
                ItemNode B = dataNodes.get(min_id);
                Collections.swap(dataNodes, i, min_id);
                final int ii = i;
                final int mins = min_id;
                PauseTransition pause = new PauseTransition(Duration.millis(10));
                pause.setOnFinished(e -> {
                    A.setIndex(mins);
                    B.setIndex(ii);
                });
                master.getChildren().add(pause);

            }

            master.getChildren().add(animateColorChange(dataNodes.get(i), Color.LIMEGREEN));
            for (int k = i + 1; k < n; k++) {
                master.getChildren().add(animateColorChangeFast(dataNodes.get(k), Color.WHITE));
            }

        }
        master.getChildren().add(setColorOfAll(dataNodes, Color.WHITE));
        PauseTransition labelRemover = new PauseTransition(Duration.seconds(0.01));
        labelRemover.setOnFinished(e -> {
            VisualPage.getAnimationPane().getChildren().remove(iLabel);
            VisualPage.getAnimationPane().getChildren().remove(jLabel);
        });
        master.getChildren().add(labelRemover);
        master.play();

    }

    private ItemNode duplicateItemNode(ItemNode item) {
        ItemNode it = new ItemNode(item.getElement(), (int) (item.getX()), (int) (item.getY()));
        it.setLayoutX(item.getLayoutX() + item.getTranslateX());
        it.setLayoutY(item.getLayoutY() + item.getTranslateY());
        return it;
    }

    private void animateMergeSort() {
        if (dataNodes.isEmpty())
            return;

        mergeSort(0, dataNodes.size() - 1, dataNodes, 1).play();
    }

    private SequentialTransition mergeSort(int low, int high, List<ItemNode> prevList, int lvl) {
        if (low == high)
            return new SequentialTransition(new PauseTransition(Duration.seconds(0.001)));
        SequentialTransition sq = new SequentialTransition();
        int mid = (low + high) / 2;
        ArrayList<ItemNode> leftNodes = new ArrayList<>();
        for (int i = 0; i <= (mid - low); i++) {
            leftNodes.add(duplicateItemNode(prevList.get(i)));
        }
        PauseTransition prl = new PauseTransition(Duration.millis(10));
        prl.setOnFinished(e -> {
            VisualPage.getAnimationPane().getChildren().addAll(leftNodes);
        });
        sq.getChildren().add(prl);

        ParallelTransition moveLefts = new ParallelTransition();
        for (ItemNode it : leftNodes) {
            TranslateTransition tr = new TranslateTransition(Duration.seconds(0.5), it);
            tr.setByY(70 * lvl);
            tr.setByX(-10 * lvl);
            moveLefts.getChildren().add(tr);
        }
        sq.getChildren().add(moveLefts);

        sq.getChildren().add(mergeSort(low, mid, leftNodes, lvl + 1));

        ArrayList<ItemNode> rightNodes = new ArrayList<>();
        for (int i = mid + 1 - low; i <= high - low; i++) {
            rightNodes.add(duplicateItemNode(prevList.get(i)));
        }
        PauseTransition prr = new PauseTransition(Duration.millis(10));
        prr.setOnFinished(e -> {
            VisualPage.getAnimationPane().getChildren().addAll(rightNodes);
        });
        sq.getChildren().add(prr);

        ParallelTransition moveRights = new ParallelTransition();
        for (ItemNode it : rightNodes) {
            TranslateTransition tr = new TranslateTransition(Duration.seconds(0.5), it);
            tr.setByY(70 * lvl);
            tr.setByX(10 * lvl);
            moveRights.getChildren().add(tr);

        }

        sq.getChildren().add(moveRights);
        sq.getChildren().add(mergeSort(mid + 1, high, rightNodes, lvl + 1));

        sq.getChildren().add(merge(leftNodes, rightNodes, prevList));
        PauseTransition pr = new PauseTransition(Duration.millis(1));
        pr.setOnFinished(e -> {
            VisualPage.getAnimationPane().getChildren().removeAll(leftNodes);
            VisualPage.getAnimationPane().getChildren().removeAll(rightNodes);
        });
        sq.getChildren().add(pr);

        return sq;

    }

    private SequentialTransition merge(ArrayList<ItemNode> left, ArrayList<ItemNode> right, List<ItemNode> parent) {
        SequentialTransition sq = new SequentialTransition(new PauseTransition(Duration.millis(10)));
        if (parent.size() == 1)
            return sq;
        int li = 0;
        int ri = 0;
        int i = 0;

        while (li < left.size() && ri < right.size()) {
            ItemNode lftNode = left.get(li);
            ItemNode rghtNode = right.get(ri);
            ItemNode currentNode = parent.get(i);
            sq.getChildren().add(animateColorChangePair(lftNode, rghtNode, Color.ORANGE));
            if (lftNode.getElement() <= rghtNode.getElement()) {

                sq.getChildren().add(animateColorChangePair(lftNode, currentNode, Color.SKYBLUE));
                sq.getChildren().add(animateValueSet(currentNode, lftNode.getElement()));
               // currentNode.setElement(lftNode.getElement());
                li++;
                i++;
            } else {
                sq.getChildren().add(animateColorChangePair(rghtNode, currentNode, Color.SKYBLUE));

                sq.getChildren().add(animateValueSet(currentNode, rghtNode.getElement()));
                //currentNode.setElement(rghtNode.getElement());
                ri++;
                i++;
            }

        }
        while (li < left.size()) {
            ItemNode lftNode = left.get(li);
            ItemNode currentNode = parent.get(i);

            sq.getChildren().add(animateColorChangePair(lftNode, currentNode, Color.ORANGE));
            sq.getChildren().add(animateValueSet(currentNode, lftNode.getElement()));
            // currentNode.setElement(lftNode.getElement());
            sq.getChildren().add(animateColorChangePair(lftNode, currentNode, Color.SKYBLUE));
            li++;
            i++;
        }

        while (ri < right.size()) {
            ItemNode rghtNode = right.get(ri);
            ItemNode currentNode = parent.get(i);
            sq.getChildren().add(animateColorChangePair(rghtNode, currentNode, Color.ORANGE));
            sq.getChildren().add(animateValueSet(currentNode, rghtNode.getElement()));
            // currentNode.setElement(rghtNode.getElement());
            sq.getChildren().add(animateColorChangePair(rghtNode, currentNode, Color.SKYBLUE));
            ri++;
            i++;
        }



        return sq;
    }

    private PauseTransition animateValueSet(ItemNode A, int a) {
        PauseTransition pr = new PauseTransition(Duration.millis(100));
        pr.setOnFinished(e -> {
            A.setElement(a);
        });

        return pr;
    }

    private void clearList() {
        dataNodes.clear();
        VisualPage.getAnimationPane().getChildren().clear();
        currentIndex = 0;
        lastX = 50;
    }

}
