package Algorithms.DP;

import java.util.ArrayList;
import java.util.List;

import DataStructures.DSAbstract;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class LCS extends DSAbstract<ItemNode> {
    List<ItemNode> headerRow = new ArrayList<>();
    List<ItemNode> rowHeaders = new ArrayList<>();
    List<List<ItemNode>> table = new ArrayList<>();
    List<ItemNode> curNode = new ArrayList<>();
    List<ItemNode> prevNodeCase1 = new ArrayList<>();
    List<ItemNode> prevNodeCase2 = new ArrayList<>();
    List<ItemNode> prevNodeCase3 = new ArrayList<>();

    List<ItemNode> reconStr = new ArrayList<>();
    List<ItemNode> curReconNode = new ArrayList<>();
    List<ItemNode> curReconStr1Node = new ArrayList<>();
    List<ItemNode> curReconStr2Node = new ArrayList<>(); 
    List<ItemNode> curReconPrev1Node = new ArrayList<>();
    List<ItemNode> curReconPrev2Node = new ArrayList<>();
    List<ItemNode> lcsNodes = new ArrayList<>();
    String lcs = new String();

    int curAnimationIdx = -1;
    Timeline timeline;

    int n, m;
    String input1, input2;
    List<ItemNode> string1Nodes = new ArrayList<>();
    List<ItemNode> string2Nodes = new ArrayList<>();
    int string1X, string1Y, string2X, string2Y;

    Text dpText = new Text(200, 200, "");
    boolean animIsPaused = false;
    boolean dpTableDone = false;
    int lcsIdx = -1;

    public LCS() {
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
        TextField stringField1 = new TextField();
        stringField1.setPromptText("Enter first string");
        TextField stringField2 = new TextField();
        stringField2.setPromptText("Enter second string");
        Button startButton = new Button("Start");
        Button resumeButton = new Button("Resume");
        Button pauseButton = new Button("Pause");
        Button prevButton = new Button("Previous");
        Button nextButton = new Button("Next");
        ComboBox<String> speedBox = new ComboBox<>();

        HBox animContRow = new HBox(10);
        animContRow.getChildren().addAll(startButton, pauseButton, resumeButton, prevButton, nextButton, speedBox);

        startButton.setOnAction(e -> {
            input1 = stringField1.getText().trim();
            input2 = stringField2.getText().trim();
            if (!input1.isEmpty() && !input2.isEmpty()) {
                stringField1.clear();
                stringField2.clear();
                n = input1.length();
                m = input2.length();

                string1Nodes.clear();
                string2Nodes.clear();
                string1X = 80;
                string1Y = 20;
                string2X = 80;
                string2Y = 80;
                for (int i = 0; i < n; ++i) {
                    string1Nodes.add(new ItemNode(String.valueOf(input1.charAt(i)), string1X, string1Y));
                    string1X += 40;
                }
                for (int i = 0; i < m; ++i) {
                    string2Nodes.add(new ItemNode(String.valueOf(input2.charAt(i)), string2X, string2Y));
                    string2X += 40;
                }
                refresh();
                drawTable();
                getSteps();
                timeline = new Timeline(new KeyFrame(Duration.seconds(2), er -> {
                    if (curAnimationIdx < curNode.size() - 1) {
                        playCurIdx();

                    } else {

                        dpTableDone = true;
                        timeline.stop(); // end of animation
                        string1Nodes.getLast().setNodeColor(Color.WHITE);
                        string2Nodes.getLast().setNodeColor(Color.WHITE);
                        curNode.getLast().setNodeColor(Color.WHITE);
                        if (prevNodeCase1.getLast() != null)
                            prevNodeCase1.getLast().setNodeColor(Color.WHITE);
                        if (prevNodeCase2.getLast() != null)
                            prevNodeCase2.getLast().setNodeColor(Color.WHITE);
                        if (prevNodeCase3.getLast() != null)
                            prevNodeCase3.getLast().setNodeColor(Color.WHITE);

                        reconstruct();
                    }
                }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.playFromStart();
            }

        });

        pauseButton.setOnAction(e -> {
            animIsPaused = true;
            timeline.pause();
        });

        resumeButton.setOnAction(e -> {
            if (animIsPaused) {
                animIsPaused = false;
                timeline.play();
            }
        });

        prevButton.setOnAction(e -> {
            if (animIsPaused && !dpTableDone)
                revCurIdx();
        });

        nextButton.setOnAction(e -> {
            if (animIsPaused && !dpTableDone)
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

        Controls.add(stringField1);
        Controls.add(stringField2);
        Controls.add(animContRow);
    }

    void playCurIdx() {
        if (curAnimationIdx >= 0) {
            string2Nodes.get(curAnimationIdx % m).setNodeColor(Color.WHITE);
        }
        curAnimationIdx++;
        if (curAnimationIdx >= m && curAnimationIdx % m == 0) {
            string1Nodes.get(curAnimationIdx / m - 1).setNodeColor(Color.WHITE);
        }
        string1Nodes.get(curAnimationIdx / m).setNodeColor(Color.WHEAT);
        string2Nodes.get(curAnimationIdx % m).setNodeColor(Color.WHEAT);

        curNode.get(curAnimationIdx).setNodeColor(Color.ORANGE);
        if (curAnimationIdx > 0) {
            curNode.get(curAnimationIdx - 1).setNodeColor(Color.WHITE);
            if (prevNodeCase1.get(curAnimationIdx - 1) != null)
                prevNodeCase1.get(curAnimationIdx - 1).setNodeColor(Color.WHITE);
            if (prevNodeCase2.get(curAnimationIdx - 1) != null)
                prevNodeCase2.get(curAnimationIdx - 1).setNodeColor(Color.WHITE);
            if (prevNodeCase3.get(curAnimationIdx - 1) != null)
                prevNodeCase3.get(curAnimationIdx - 1).setNodeColor(Color.WHITE);
        }

        if (prevNodeCase1.get(curAnimationIdx) != null) {
            curNode.get(curAnimationIdx).setElement(prevNodeCase1.get(curAnimationIdx).getElement() + 1);
            prevNodeCase1.get(curAnimationIdx).setNodeColor(Color.YELLOW);
            VisualPage.getAnimationPane().getChildren().remove(dpText);
            dpText.setText(
                    "string1[i] = string2[j]\n" + "so, dp[i][j] = dp[i-1][j-1] + 1 = " +
                            prevNodeCase1.get(curAnimationIdx).getElement() + " + 1 = " +
                            curNode.get(curAnimationIdx).getElement());
            VisualPage.getAnimationPane().getChildren().addAll(dpText);
        } else {
            prevNodeCase2.get(curAnimationIdx).setNodeColor(Color.YELLOW);
            prevNodeCase3.get(curAnimationIdx).setNodeColor(Color.YELLOW);
            int c2 = prevNodeCase2.get(curAnimationIdx).getElement();
            int c3 = prevNodeCase3.get(curAnimationIdx).getElement();
            int c = Math.max(c2, c3);
            curNode.get(curAnimationIdx).setElement(c);
            VisualPage.getAnimationPane().getChildren().remove(dpText);
            dpText.setText(
                    "string1[i] != string2[i]" + "\nso, dp[i][j] = max(dp[i-1][j], dp[i][j-1])"
                            + " = max(" + c2 + ", " + c3 + ") = " + c);
            VisualPage.getAnimationPane().getChildren().addAll(dpText);
        }

    }

    void revCurIdx() {

    }

    void reconstruct() {
        lcsNodes.clear();
        dpText.setText("");
        int x = 200, y = 200;
        int s = table.getLast().getLast().getElement();
        lcsIdx = s-1;
        for(int i = 0; i < s; ++i ) {
            lcsNodes.add(new ItemNode("", x, y));
            x += 40;
        }       
        VisualPage.getAnimationPane().getChildren().addAll(lcsNodes);
        curAnimationIdx = -1;
        string1Nodes.getLast().setNodeColor(Color.WHEAT);
        string2Nodes.getLast().setNodeColor(Color.WHEAT);
        timeline = new Timeline(new KeyFrame(Duration.seconds(2), er -> {
            if (curAnimationIdx < curReconNode.size() - 1) {
                playCurReconIdx();
            } else {
                for(ItemNode it : string1Nodes) it.setNodeColor(Color.WHITE); 
                for(ItemNode it : string2Nodes) it.setNodeColor(Color.WHITE); 
                for(List<ItemNode> li : table) for(ItemNode it : li) it.setNodeColor(Color.WHITE);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.playFromStart();
    }

    void playCurReconIdx() {
        
        if(curAnimationIdx >= 0) {
            curReconNode.get(curAnimationIdx).setNodeColor(Color.WHITE);
            if(curReconPrev1Node.get(curAnimationIdx) != null)
             curReconPrev1Node.get(curAnimationIdx).setNodeColor(Color.WHITE);
            if(curReconPrev2Node.get(curAnimationIdx) != null)
             curReconPrev2Node.get(curAnimationIdx).setNodeColor(Color.WHITE);
        }

        curAnimationIdx++;

        curReconNode.get(curAnimationIdx).setNodeColor(Color.ORANGE);
        if(curReconPrev1Node.get(curAnimationIdx) != null) 
         curReconPrev1Node.get(curAnimationIdx).setNodeColor(Color.YELLOW);
        if(curReconPrev2Node.get(curAnimationIdx) != null)
         curReconPrev2Node.get(curAnimationIdx).setNodeColor(Color.YELLOW);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {
            if(curReconStr1Node.get(curAnimationIdx) != null && curReconStr2Node.get(curAnimationIdx) != null) {
                lcsNodes.get(lcsIdx).setText( String.valueOf(lcs.charAt(lcsIdx))); lcsIdx--;

            }

            if(curReconStr1Node.get(curAnimationIdx) != null){
                for(ItemNode x : string1Nodes) x.setNodeColor(Color.WHITE);
                curReconStr1Node.get(curAnimationIdx).setNodeColor(Color.WHEAT);
            }
            if(curReconStr2Node.get(curAnimationIdx) != null) {
                for(ItemNode x : string2Nodes) x.setNodeColor(Color.WHITE);
                curReconStr2Node.get(curAnimationIdx).setNodeColor(Color.WHEAT);    
            }
        });
        pause.play();
    }

    private void drawTable() {
        int tableX = 80;
        int tableY = 400; // -40 for headers

        for (int i = 0; i <= m; ++i) {
            headerRow.add(new ItemNode(i, tableX + i * 40, tableY - 40));
            headerRow.getLast().highlight();
        }
        for (int i = 0; i <= n; ++i) {
            rowHeaders.add(new ItemNode(i, tableX - 40, tableY + i * 40));
            rowHeaders.getLast().highlight();
        }

        VisualPage.getAnimationPane().getChildren().addAll(headerRow);
        VisualPage.getAnimationPane().getChildren().addAll(rowHeaders);

        for (int i = 0; i <= n; ++i) {
            table.add(new ArrayList<ItemNode>());

            for (int j = 0; j <= m; ++j) {
                table.get(i).add(new ItemNode(0, tableX + j * 40, tableY + i * 40));
            }
            VisualPage.getAnimationPane().getChildren().addAll(table.get(i));
        }
    }

    private void getSteps() {
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; ++i) {
            for (int j = 1; j <= m; ++j) {
                curNode.add(table.get(i).get(j));

                if (input1.charAt(i - 1) == input2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    prevNodeCase1.add(table.get(i - 1).get(j - 1));
                    prevNodeCase2.add(null);
                    prevNodeCase3.add(null);
                } else {
                    prevNodeCase1.add(null);
                    prevNodeCase2.add(table.get(i - 1).get(j));
                    prevNodeCase3.add(table.get(i).get(j - 1));
                    if (dp[i - 1][j] > dp[i][j - 1]) {
                        dp[i][j] = dp[i - 1][j];

                    } else {
                        dp[i][j] = dp[i][j - 1];

                    }

                }
            }
        }

        lcs = new String();
        int i = n, j = m; 
        while(i > 0 && j > 0) { 
            curReconNode.add(table.get(i).get(j));
            if(input1.charAt(i-1) == input2.charAt(j-1)) {
                lcs = input1.charAt(i-1)+ lcs;    
                System.out.println(lcs);
                i--; j--; 
                if(i >= 0) curReconStr1Node.add(string1Nodes.get(i));
                else curReconStr1Node.add(null);
                curReconStr2Node.add((j >= 0 ? string2Nodes.get(j) : null));
                curReconPrev1Node.add(null);
                curReconPrev2Node.add(null);
            }
            else {
                curReconPrev1Node.add(table.get(i-1).get(j));
                curReconPrev2Node.add(table.get(i).get(j-1));
                if(dp[i-1][j] > dp[i][j-1]) {
                    i--;
                    curReconStr1Node.add((i >= 0 ? string1Nodes.get(i) : null));
                    curReconStr2Node.add(null);
                }
                else {
                    j--;
                    curReconStr2Node.add((j >= 0 ? string2Nodes.get(j) : null));
                    curReconStr1Node.add(null);
                }
            }
        }
    }

    @Override
    protected void removeLastNode() {

    }

    @Override
    protected void showCode() {

    }

    private void refresh() {
        VisualPage.getAnimationPane().getChildren().clear();
        VisualPage.getAnimationPane().getChildren().addAll(string1Nodes);
        VisualPage.getAnimationPane().getChildren().addAll(string2Nodes);
        // VisualPage.getAnimationPane().getChildren().addAll(labels);
    }

}
