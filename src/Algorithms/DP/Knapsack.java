package Algorithms.DP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.plaf.basic.BasicSeparatorUI;

import DataStructures.DSAbstract;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
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
    List<ItemNode> weightNodes = new  ArrayList<>();
    List<Integer> values = new ArrayList<>();
    List<ItemNode> valueNodes = new ArrayList<>();
    List<List<ItemNode>> table = new ArrayList<>();
    List<ItemNode> headerRow = new ArrayList<>();
    List<ItemNode> rowHeaders = new ArrayList<>();
    int weightArrX = 80; int weightArrY = 20;
    int valueArrX = 80; int valueArrY = 80;
    int W;
    int itemCnt;
    Label valueLabel = new Label("Values:");
    Label weightLabel = new Label("Weights:");
    Label WLabel = new Label("W:");
    List<Label> labels = new ArrayList<>();
    int curIdx = -1;
    int curAnimationIdx = -1;

    Timeline timeline;
    List<ItemNode> curNode = new ArrayList<>();
    List<ItemNode> prevNodeCase1 = new ArrayList<>();
    List<ItemNode> prevNodeCase2 = new ArrayList<>();
    Text dpText = new Text(200, 200, "");
    boolean animIsPaused = false;

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


        Button randButton = new Button("Generate random");
        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button resmeButton = new Button("Resume");
        Button prevButton = new Button("Previous");
        Button nexButton = new Button("Next");

        HBox animControlBox = new HBox(10);
        animControlBox.getChildren().addAll(startButton, pauseButton, resmeButton, prevButton, nexButton);

        randButton.setOnAction(e->{
            weights.clear(); weightNodes.clear();
            values.clear(); valueNodes.clear();
            weightArrX = 80; weightArrY = 20;
            valueArrX = 80; valueArrY = 80;

            Random random = new Random();
            for(int i = 0; i < 10; ++i) {
                addItem(random.nextInt(90)+10, random.nextInt(13)+2);
            }
            itemCnt = 8;
            W = 13 + random.nextInt(3);
            ItemNode WNode = new ItemNode(W, 530, 35);
            VisualPage.getAnimationPane().getChildren().addAll(WNode);
        });

        startButton.setOnAction(e->{
            drawTable();
            getSteps();
            System.out.println(curNode.size());
            timeline = new Timeline(new KeyFrame(Duration.seconds(2), er -> {
            if (curAnimationIdx < curNode.size()) {
                playCurIdx();
                

            } else {
                timeline.stop(); // end of animation
                
            }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.playFromStart();
        });

        pauseButton.setOnAction(e->{
            animIsPaused = true;
            timeline.pause();
        });

        resmeButton.setOnAction(e->{
            if(animIsPaused) {
                animIsPaused = false;
                timeline.play();
            }
        });

        prevButton.setOnAction(e->{
            revCurIdx();
        });

        nexButton.setOnAction(e->{
            playCurIdx();
        });
        
        Controls.add(randButton);
        Controls.add(animControlBox);

        valueLabel.setLayoutX(20); valueLabel.setLayoutY(80);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        weightLabel.setLayoutX(10); weightLabel.setLayoutY(20);
        weightLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        WLabel.setLayoutX(500); WLabel.setLayoutY(40);
        WLabel.setFont(Font.font("System", FontWeight.BOLD, 16));

        labels.add(valueLabel); labels.add(weightLabel); labels.add(WLabel);
        VisualPage.getAnimationPane().getChildren().addAll(labels);
        
    }

    void playCurIdx() {
        curAnimationIdx++;
        valueNodes.get(curAnimationIdx/(1+W)).setNodeColor(Color.WHEAT);
        weightNodes.get(curAnimationIdx/(1+W)).setNodeColor(Color.WHEAT);
        if(curAnimationIdx> W && curAnimationIdx % (W+1) == 0) {
            valueNodes.get(curAnimationIdx/(1+W)  -1).setNodeColor(Color.WHITE);
            weightNodes.get(curAnimationIdx/(1+W)  -1).setNodeColor(Color.WHITE);
        }

                curNode.get(curAnimationIdx).flash(Color.ORANGE);
                prevNodeCase1.get(curAnimationIdx).flash(Color.YELLOW);
                if(prevNodeCase2.get(curAnimationIdx) != null) {
                    prevNodeCase2.get(curAnimationIdx).flash(Color.YELLOW);
                }

                VisualPage.getAnimationPane().getChildren().remove(dpText);
                dpText.setText(
                    "Not taking current element:\n" +
                    "dp[i-1][j] = " + prevNodeCase1.get(curAnimationIdx).getElement() +
                    "\n\nTaking current element:\n" +
                    (prevNodeCase2.get(curAnimationIdx) == null ? 
                    "Not possible" 
                    : 
                    "dp[i-1][j-w[i]] + val[i] = " + 
                    (prevNodeCase2.get(curAnimationIdx).getElement() + values.get(curAnimationIdx/(1+W))))
                );
                VisualPage.getAnimationPane().getChildren().addAll(dpText);

                int ansc1 = prevNodeCase1.get(curAnimationIdx).getElement();
                int ansc2 = 0;
                if(prevNodeCase2.get(curAnimationIdx) != null) 
                ansc2 = (prevNodeCase2.get(curAnimationIdx).getElement() + values.get(curAnimationIdx/(1+W)));

                curNode.get(curAnimationIdx).setElement(Math.max(ansc1, ansc2));
    }

    private void revCurIdx() {
        if(!animIsPaused) return;

        curNode.get(curAnimationIdx).setElement(0);

         if(curAnimationIdx> W && curAnimationIdx % (W+1) == 0) {
            valueNodes.get(curAnimationIdx/(1+W) ).setNodeColor(Color.WHITE);
            weightNodes.get(curAnimationIdx/(1+W) ).setNodeColor(Color.WHITE);

            valueNodes.get(curAnimationIdx/(1+W)  -1).setNodeColor(Color.WHEAT);
            weightNodes.get(curAnimationIdx/(1+W)  -1).setNodeColor(Color.WHEAT);
        }

        curAnimationIdx -= 2;
        playCurIdx();
    }

    private void addItem(int value, int w) {
        values.add(value); weights.add(w);
        valueNodes.add(new ItemNode(value, valueArrX, valueArrY));
        weightNodes.add(new ItemNode(w, weightArrX, weightArrY));
        weightArrX += 40; valueArrX += 40;
        refresh();
    }

    @Override
    protected void removeLastNode() {
        // TODO Auto-generated method stub
        
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
        int tableX = 80; int tableY = 400; // -40 for headers

        for(int i = 0; i <= W; ++i) {
            headerRow.add(new ItemNode(i, tableX+i*40, tableY-40));
            headerRow.getLast().highlight();
        }
        for(int i = 0; i <= itemCnt; ++i) {
            rowHeaders.add(new ItemNode(i, tableX-40, tableY+i*40));
            rowHeaders.getLast().highlight();
        }

        VisualPage.getAnimationPane().getChildren().addAll(headerRow);
        VisualPage.getAnimationPane().getChildren().addAll(rowHeaders);

        for(int i = 0; i <= itemCnt; ++i) {
            table.add(new ArrayList<ItemNode>());

            for(int j = 0; j <= W; ++j) {
                table.get(i).add(new ItemNode(0, tableX + j*40, tableY + i*40));
            }
            VisualPage.getAnimationPane().getChildren().addAll(table.get(i));
        }
    }

    void getSteps() {
        int [][] dp = new int[itemCnt+1][W+1];

        for(int i = 1; i <= itemCnt; ++i) {
            for(int j = 0; j <= W; ++j) {
                // ++curIdx;
                curNode.add(table.get(i).get(j));

                prevNodeCase1.add(table.get(i-1).get(j));

                // not taking
                dp[i][j] = dp[i-1][j];

                // taking
                if(weights.get(i-1) <= j) {
                    dp[i][j] = Math.max(dp[i][j], dp[i-1][j-weights.get(i-1)]);
                    prevNodeCase2.add(table.get(i-1).get(j-weights.get(i-1)));
                } else{
                    prevNodeCase2.add(null);
                }
            }
        }
    }

}

