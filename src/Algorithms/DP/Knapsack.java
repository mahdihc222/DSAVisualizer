package Algorithms.DP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import DataStructures.DSAbstract;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Knapsack extends DSAbstract<ItemNode> {
    List<Integer> weights = new ArrayList<>();
    List<ItemNode> weightNodes = new ArrayList<>();
    List<Integer> values = new ArrayList<>();
    List<ItemNode> valueNodes = new ArrayList<>();
    List<List<ItemNode>> table = new ArrayList<>();
    List<ItemNode> headerRow = new ArrayList<>();
    List<ItemNode> rowHeaders = new ArrayList<>();
    int weightArrX = 80;
    int weightArrY = 20;
    int valueArrX = 80;
    int valueArrY = 80;
    int W;
    int itemCnt;
    Label valueLabel = new Label("Values:");
    Label weightLabel = new Label("Weights:");
    Label WLabel = new Label("W:");
    List<Label> labels = new ArrayList<>();
    int curIdx = -1;
    int curAnimationIdx = -1;
    int delay = 2;

    Timeline timeline;
    List<ItemNode> curNode = new ArrayList<>();
    List<ItemNode> prevNodeCase1 = new ArrayList<>();
    List<ItemNode> prevNodeCase2 = new ArrayList<>();
    Text dpText = new Text(200, 200, "");
    boolean animIsPaused = false;

    Text anim_i = new Text(100 ,350, "i");
    Text anim_j = new Text(20, 460, "j");

    public Knapsack() {
        System.out.println("in constuctor");
        initializeControls();
        VisualPage.getControlBox().getChildren().addAll(Controls);
        showCode();
    }

    @Override
    protected void addNode(int val) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void initializeControls() {
        VisualPage.getAnimationPane().getChildren().clear();
        VisualPage.getControlBox().getChildren().clear();
        dpText.setFont(Font.font("System", FontWeight.BOLD, 18));

        Button randButton = new Button("Generate random");
        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button resmeButton = new Button("Resume");
        Button prevButton = new Button("Previous");
        Button nexButton = new Button("Next");
        ComboBox<String> speedBox = new ComboBox<>();
        anim_i.setFill(Color.BLUE); anim_j.setFill(Color.RED);

        HBox animControlBox = new HBox(10);
        animControlBox.getChildren().addAll(startButton, pauseButton, resmeButton, prevButton, nexButton, speedBox);

        randButton.setOnAction(e -> {
            weights.clear();
            weightNodes.clear();
            values.clear();
            valueNodes.clear();
            weightArrX = 80;
            weightArrY = 20;
            valueArrX = 80;
            valueArrY = 80;

            Random random = new Random();
            for (int i = 0; i < 7; ++i) {
                addItem(random.nextInt(20) + 40, random.nextInt(9) + 2);
            }
            itemCnt = 7;
            W = 10 + random.nextInt(3);
            ItemNode WNode = new ItemNode(W, 530, 35);
            VisualPage.getAnimationPane().getChildren().addAll(WNode);
        });

        startButton.setOnAction(e -> {
            drawTable();
            getSteps();
            VisualPage.getAnimationPane().getChildren().addAll(anim_i);
            VisualPage.getAnimationPane().getChildren().addAll(anim_j);
            System.out.println(curNode.size());
            timeline = new Timeline(new KeyFrame(Duration.seconds(delay), er -> {
                if (curAnimationIdx < curNode.size() - 1) {
                    playCurIdx();

                } else {
                    timeline.stop(); // end of animation
                    valueNodes.getLast().setNodeColor(Color.WHITE);
                    weightNodes.getLast().setNodeColor(Color.WHITE);
                    curNode.getLast().setNodeColor(Color.WHITE);
                    prevNodeCase1.getLast().setNodeColor(Color.WHITE);
                    prevNodeCase2.getLast().setNodeColor(Color.WHITE);
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.playFromStart();
        });

        pauseButton.setOnAction(e -> {
            animIsPaused = true;
            timeline.pause();
        });

        resmeButton.setOnAction(e -> {
            if (animIsPaused) {
                animIsPaused = false;
                timeline.play();
            }
        });

        prevButton.setOnAction(e -> {
            if (animIsPaused)
                revCurIdx();
        });

        nexButton.setOnAction(e -> {
            if (animIsPaused)
                playCurIdx();
        });

        speedBox.getItems().addAll("0.5x", "1x", "2x");
        speedBox.setValue("1x"); // default selection
        speedBox.setOnAction(e -> {
            String selected = speedBox.getValue();
            switch (selected) {
                case "0.5x":
                    timeline.setRate(0.5);
                    break;
                case "1x":
                    timeline.setRate(1.0);
                    break;
                case "2x":
                    timeline.setRate(2.0);
                    break;
            }
        });

        Controls.add(randButton);
        Controls.add(animControlBox);

        valueLabel.setLayoutX(20);
        valueLabel.setLayoutY(80);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        weightLabel.setLayoutX(10);
        weightLabel.setLayoutY(20);
        weightLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        WLabel.setLayoutX(500);
        WLabel.setLayoutY(40);
        WLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        labels.add(valueLabel);
        labels.add(weightLabel);
        labels.add(WLabel);
        VisualPage.getAnimationPane().getChildren().addAll(labels);

    }

    void playCurIdx() {
        curAnimationIdx++;
        valueNodes.get(curAnimationIdx / (1 + W)).setNodeColor(Color.WHEAT);
        weightNodes.get(curAnimationIdx / (1 + W)).setNodeColor(Color.WHEAT);
        if (curAnimationIdx > W && curAnimationIdx % (W + 1) == 0) {
            valueNodes.get(curAnimationIdx / (1 + W) - 1).setNodeColor(Color.WHITE);
            weightNodes.get(curAnimationIdx / (1 + W) - 1).setNodeColor(Color.WHITE);

            update_anim_j("y", anim_j.getY() + 40);
            update_anim_i("x", 100);
        }

        if(curAnimationIdx > 0) {
            curNode.get(curAnimationIdx-1).setNodeColor(Color.WHITE);
            prevNodeCase1.get(curAnimationIdx-1).setNodeColor(Color.WHITE);
            if (prevNodeCase2.get(curAnimationIdx-1) != null) 
            prevNodeCase2.get(curAnimationIdx-1).setNodeColor(Color.WHITE);

            if(curAnimationIdx % (W + 1) != 0)
            update_anim_i("x", anim_i.getX() + 40);
        }

        curNode.get(curAnimationIdx).setNodeColor(Color.ORANGE);
        prevNodeCase1.get(curAnimationIdx).setNodeColor(Color.YELLOW);
        if (prevNodeCase2.get(curAnimationIdx) != null) {
            prevNodeCase2.get(curAnimationIdx).setNodeColor(Color.YELLOW);
        }

        VisualPage.getAnimationPane().getChildren().remove(dpText);
        dpText.setText(
                "Not taking current element:\n" +
                        "dp[i-1][j] = " + prevNodeCase1.get(curAnimationIdx).getElement() +
                        "\n\nTaking current element:\n" +
                        (prevNodeCase2.get(curAnimationIdx) == null ? "Not possible"
                                : "dp[i-1][j-w[i]] + val[i] = " +
                                    prevNodeCase2.get(curAnimationIdx).getElement() + " + " +
                                        values.get(curAnimationIdx / (1 + W)) + " = " +
                                        (prevNodeCase2.get(curAnimationIdx).getElement()
                                                + values.get(curAnimationIdx / (1 + W)))));
        VisualPage.getAnimationPane().getChildren().addAll(dpText);
        

        int ansc1 = prevNodeCase1.get(curAnimationIdx).getElement();
        int ansc2 = 0;
        if (prevNodeCase2.get(curAnimationIdx) != null)
            ansc2 = (prevNodeCase2.get(curAnimationIdx).getElement() + values.get(curAnimationIdx / (1 + W)));

        curNode.get(curAnimationIdx).setElement(Math.max(ansc1, ansc2));
    }

    private void revCurIdx() {
        if (!animIsPaused)
            return;

        curNode.get(curAnimationIdx).setElement(0);
        if(curAnimationIdx > 0) {
            curNode.get(curAnimationIdx).setNodeColor(Color.WHITE);
            prevNodeCase1.get(curAnimationIdx).setNodeColor(Color.WHITE);
            if(prevNodeCase2.get(curAnimationIdx) != null) 
            prevNodeCase2.get(curAnimationIdx).setNodeColor(Color.WHITE);
        }

        if (curAnimationIdx > W && curAnimationIdx % (W + 1) == 0) {
            valueNodes.get(curAnimationIdx / (1 + W)).setNodeColor(Color.WHITE);
            weightNodes.get(curAnimationIdx / (1 + W)).setNodeColor(Color.WHITE);

            valueNodes.get(curAnimationIdx / (1 + W) - 1).setNodeColor(Color.WHEAT);
            weightNodes.get(curAnimationIdx / (1 + W) - 1).setNodeColor(Color.WHEAT);
        }

        if (curAnimationIdx > W && curAnimationIdx % (W + 1) == 0) {
            update_anim_j("y", anim_j.getY() - 40);
            update_anim_i("x", 100 + W * 40 );
        }
        if (curAnimationIdx-1 > W && (curAnimationIdx-1) % (W + 1) == 0) {
            update_anim_j("y", anim_j.getY() - 40);
            update_anim_i("x", 100 + W * 40 );
        }

        if(curAnimationIdx > 0) {
            if(curAnimationIdx % (W + 1) != 0)
            update_anim_i("x", anim_i.getX() - 40);
        }
        if(curAnimationIdx-1 > 0) {
            if((curAnimationIdx-1) % (W + 1) != 0)
            update_anim_i("x", anim_i.getX() - 40);
        }




        curAnimationIdx -= 2;
        playCurIdx();
    }

    private void addItem(int value, int w) {
        values.add(value);
        weights.add(w);
        valueNodes.add(new ItemNode(value, valueArrX, valueArrY));
        weightNodes.add(new ItemNode(w, weightArrX, weightArrY));
        weightArrX += 40;
        valueArrX += 40;
        refresh();
    }

    @Override
    protected void removeLastNode() {
        // TODO Auto-generated method stub

    }

    void update_anim_i(String axis, double targ){
        VisualPage.getAnimationPane().getChildren().remove(anim_i);
        if(axis.equals("x")) anim_i.setX(targ);
        else anim_i.setY(targ);
        VisualPage.getAnimationPane().getChildren().addAll(anim_i);
    }

    void update_anim_j(String axis, double targ){
        VisualPage.getAnimationPane().getChildren().remove(anim_j);
        if(axis.equals("x")) anim_j.setX(targ);
        else anim_j.setY(targ);
        VisualPage.getAnimationPane().getChildren().addAll(anim_j);
    }

    @Override
    protected void showCode() {
        // TODO Auto-generated method stub
        Tab problemTab = new Tab("Problem", getCodeTextArea("Problems/Knapsack"));
        Tab codeTab = new Tab("Code", getCodeTextArea("Knapsack"));
        VisualPage.getCodePane().getTabs().addAll(problemTab, codeTab);
        VisualPage.getCodePane().getTabs().forEach(tab -> tab.setClosable(false));
    }

    private void refresh() {
        VisualPage.getAnimationPane().getChildren().clear();
        VisualPage.getAnimationPane().getChildren().addAll(valueNodes);
        VisualPage.getAnimationPane().getChildren().addAll(weightNodes);
        VisualPage.getAnimationPane().getChildren().addAll(labels);
    }

    private void drawTable() {
        int tableX = 80;
        int tableY = 400; // -40 for headers

        for (int i = 0; i <= W; ++i) {
            headerRow.add(new ItemNode(i, tableX + i * 40, tableY - 40));
            headerRow.getLast().highlight();
        }
        for (int i = 0; i <= itemCnt; ++i) {
            rowHeaders.add(new ItemNode(i, tableX - 40, tableY + i * 40));
            rowHeaders.getLast().highlight();
        }

        VisualPage.getAnimationPane().getChildren().addAll(headerRow);
        VisualPage.getAnimationPane().getChildren().addAll(rowHeaders);

        for (int i = 0; i <= itemCnt; ++i) {
            table.add(new ArrayList<ItemNode>());

            for (int j = 0; j <= W; ++j) {
                table.get(i).add(new ItemNode(0, tableX + j * 40, tableY + i * 40));
            }
            VisualPage.getAnimationPane().getChildren().addAll(table.get(i));
        }
    }

    void getSteps() {
        int[][] dp = new int[itemCnt + 1][W + 1];

        for (int i = 1; i <= itemCnt; ++i) {
            for (int j = 0; j <= W; ++j) {
                // ++curIdx;
                curNode.add(table.get(i).get(j));

                prevNodeCase1.add(table.get(i - 1).get(j));

                // not taking
                dp[i][j] = dp[i - 1][j];

                // taking
                if (weights.get(i - 1) <= j) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - 1][j - weights.get(i - 1)]);
                    prevNodeCase2.add(table.get(i - 1).get(j - weights.get(i - 1)));
                } else {
                    prevNodeCase2.add(null);
                }
            }
        }
    }

}
