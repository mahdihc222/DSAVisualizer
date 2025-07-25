package Algorithms.DP;

import java.util.ArrayList;
import java.util.List;

import DataStructures.DSAbstract;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class LCS extends DSAbstract<ItemNode> {
    List<ItemNode> headerRow = new ArrayList<>();
    List<ItemNode> rowHeaders = new ArrayList<>();
    List<List<ItemNode>> table = new ArrayList<>();
    List<ItemNode> curNode = new ArrayList<>();
    List<ItemNode> prevNodeCase1 = new ArrayList<>();
    List<ItemNode> prevNodeCase2 = new ArrayList<>();
    List<ItemNode> prevNodeCase3 = new ArrayList<>();

    int curAnimationIdx = -1;
    Timeline timeline;

    int n, m;
    String input1, input2;

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
        TextField stringField1 = new TextField();
        stringField1.setPromptText("Enter first string");
        TextField stringField2 = new TextField();
        stringField2.setPromptText("Enter second string");
        Button startButton = new Button("Start");
        Button resumeButton = new Button("Resume");
        Button pauseButton = new Button("Pause");
        Button prevButton = new Button("Previous");
        Button nextButton = new Button("Next");

        HBox animContRow = new HBox(10);
        animContRow.getChildren().addAll(startButton, pauseButton, resumeButton, prevButton, nextButton);

        startButton.setOnAction(e -> {
            input1 = stringField1.getText().trim();
            input2 = stringField2.getText().trim();
            if (!input1.isEmpty() && !input2.isEmpty()) {
                n = input1.length();
                m = input2.length();
                drawTable();
                getSteps();
                timeline = new Timeline(new KeyFrame(Duration.seconds(2), er -> {
                    if (curAnimationIdx < curNode.size()) {
                        playCurIdx();

                    } else {
                        timeline.stop(); // end of animation

                    }
                }));
                timeline.setCycleCount(Animation.INDEFINITE);
                timeline.playFromStart();
            }

        });

        Controls.add(stringField1);
        Controls.add(stringField2);
        Controls.add(animContRow);
    }

    void playCurIdx() {
        curAnimationIdx++;
        
    }

    private void drawTable() {
        int tableX = 80;
        int tableY = 400; // -40 for headers

        for (int i = 0; i <= n; ++i) {
            headerRow.add(new ItemNode(i, tableX + i * 40, tableY - 40));
            headerRow.getLast().highlight();
        }
        for (int i = 0; i <= m; ++i) {
            rowHeaders.add(new ItemNode(i, tableX - 40, tableY + i * 40));
            rowHeaders.getLast().highlight();
        }

        VisualPage.getAnimationPane().getChildren().addAll(headerRow);
        VisualPage.getAnimationPane().getChildren().addAll(rowHeaders);

        for (int i = 0; i <= m; ++i) {
            table.add(new ArrayList<ItemNode>());

            for (int j = 0; j <= n; ++j) {
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

                if (input1.charAt(i) == input2.charAt(j)) {
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
    }

    @Override
    protected void removeLastNode() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void showCode() {
        // TODO Auto-generated method stub

    }

}
