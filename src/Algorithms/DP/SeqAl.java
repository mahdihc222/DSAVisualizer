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
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class SeqAl extends DSAbstract<ItemNode> {
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
    List<ItemNode> curReconPrev3Node = new ArrayList<>();
    List<Integer> setVal = new ArrayList<>();
    List<Integer> c1List = new ArrayList<>();
    List<Integer> c2List = new ArrayList<>();
    List<Integer> c3List = new ArrayList<>();
    List<ItemNode> seqAlNodes1 = new ArrayList<>();
    List<ItemNode> seqAlNodes2 = new ArrayList<>();
    String seqAl1 = new String();
    String seqAl2 = new String();
    int[][] dp;

    int matchVal, mismPen, gapPen;

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
    int seqAlIdx = -1;

    public SeqAl() {
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

        TextField matchValField = new TextField();
        matchValField.setPromptText("Enter Match Value");
        TextField mismPenField = new TextField();
        mismPenField.setPromptText("Enter mismatch penalty");
        TextField gapPenField = new TextField();
        gapPenField.setPromptText("Enter gap penalty");
        TextField stringField1 = new TextField();
        stringField1.setPromptText("Enter first string");
        TextField stringField2 = new TextField();
        stringField2.setPromptText("Enter second string");
        Button startButton = new Button("Start");
        Button resumeButton = new Button("Resume");
        Button pauseButton = new Button("Pause");
        // Button prevButton = new Button("Previous");
        Button nextButton = new Button("Next");
        ComboBox<String> speedBox = new ComboBox<>();

        HBox animContRow = new HBox(10);
        animContRow.getChildren().addAll(startButton, pauseButton, resumeButton, nextButton, speedBox);

        startButton.setOnAction(e -> {
            input1 = stringField1.getText().trim();
            input2 = stringField2.getText().trim();
            String matchValStr = matchValField.getText().trim();
            String mismPenStr = mismPenField.getText().trim();
            String gapPenStr = gapPenField.getText().trim();
            if (!input1.isEmpty() && !input2.isEmpty() && !matchValStr.isEmpty() && !mismPenStr.isEmpty() 
                    && !gapPenStr.isEmpty()) {

            try {
                matchVal = Integer.parseInt(matchValStr);
                mismPen = Integer.parseInt(mismPenStr);
                gapPen = Integer.parseInt(gapPenStr);
                matchValField.clear();
                mismPenField.clear();
                gapPenField.clear();
            } catch (NumberFormatException ex) {
                return;
            }

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

        // prevButton.setOnAction(e -> {
        //     if (animIsPaused && !dpTableDone)
        //         revCurIdx();
        // });

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
        Controls.add(matchValField);
        Controls.add(mismPenField);
        Controls.add(gapPenField);

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



        VisualPage.getAnimationPane().getChildren().remove(dpText);
        dpText.setText(
            "Case 1: " + c1List.get(curAnimationIdx) + "\nCase 2: " + c2List.get(curAnimationIdx) +
            "\nCase 3: " + c3List.get(curAnimationIdx) + "\ndp[i][j] = " + setVal.get(curAnimationIdx)
        );
        VisualPage.getAnimationPane().getChildren().addAll(dpText);


        curNode.get(curAnimationIdx).setElement(setVal.get(curAnimationIdx));
        prevNodeCase1.get(curAnimationIdx).setNodeColor(Color.YELLOW);
        prevNodeCase2.get(curAnimationIdx).setNodeColor(Color.YELLOW);
        prevNodeCase3.get(curAnimationIdx).setNodeColor(Color.YELLOW);

    }

    void revCurIdx() {

    }

    void reconstruct() {
        seqAlNodes1.clear();
        seqAlNodes2.clear();
        dpText.setText("");
        int x = 200, y = 200;
        int s = seqAl1.length();
        seqAlIdx = s-1; System.out.println("seqalidx " + seqAlIdx);
        for(int i = 0; i < s; ++i ) {
            seqAlNodes1.add(new ItemNode("", x, y)); seqAlNodes1.getLast().setVisible(false);
            seqAlNodes2.add(new ItemNode("", x, y + 50)); seqAlNodes2.getLast().setVisible(false);
            x += 40;
        }       
        VisualPage.getAnimationPane().getChildren().addAll(seqAlNodes1);
        VisualPage.getAnimationPane().getChildren().addAll(seqAlNodes2);
        curAnimationIdx = -1;
        string1Nodes.getLast().setNodeColor(Color.WHEAT);
        string2Nodes.getLast().setNodeColor(Color.WHEAT);
        System.out.println("curreconnodesize " + curReconNode.size());
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
             if(curReconPrev3Node.get(curAnimationIdx) != null)
             curReconPrev3Node.get(curAnimationIdx).setNodeColor(Color.WHITE);
        }

        curAnimationIdx++;

        curReconNode.get(curAnimationIdx).setNodeColor(Color.ORANGE);
        if(curReconPrev1Node.get(curAnimationIdx) != null) 
         curReconPrev1Node.get(curAnimationIdx).setNodeColor(Color.YELLOW);
        if(curReconPrev2Node.get(curAnimationIdx) != null)
         curReconPrev2Node.get(curAnimationIdx).setNodeColor(Color.YELLOW);
         if(curReconPrev3Node.get(curAnimationIdx) != null)
         curReconPrev3Node.get(curAnimationIdx).setNodeColor(Color.YELLOW);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> {

                seqAlNodes1.get(seqAlIdx).setText( String.valueOf(seqAl1.charAt(seqAlIdx))); 
                seqAlNodes1.get(seqAlIdx).setVisible(true);
                seqAlNodes2.get(seqAlIdx).setText( String.valueOf(seqAl2.charAt(seqAlIdx)));
                seqAlNodes2.get(seqAlIdx).setVisible(true);
                seqAlIdx--;

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
        dp = new int[n + 1][m + 1];
        
        for (int i = 1; i <= n; ++i) {
            for (int j = 1; j <= m; ++j) {
                curNode.add(table.get(i).get(j));

                int c1 = dp[i-1][j-1] + ((input1.charAt(i-1) == input2.charAt(j-1)) ? matchVal : mismPen);
                int c2 = dp[i-1][j] + gapPen;
                int c3 = dp[i][j-1] + gapPen;
                int mx = Math.max(c1, c2);
                mx = Math.max(mx, c3);
                setVal.add(mx);
                c1List.add(c1); c2List.add(c2); c3List.add(c3);

                prevNodeCase1.add(table.get(i - 1).get(j - 1));
                prevNodeCase2.add(table.get(i - 1).get(j));
                prevNodeCase3.add(table.get(i).get(j - 1));

                dp[i][j] = mx;
            }
        }

        seqAl1 = new String();
        seqAl2 = new String();
        int i = n, j = m; 

while(i > 0 || j > 0) {

        curReconNode.add(table.get(i).get(j));
        if(i > 0 && j > 0) curReconPrev1Node.add(table.get(i - 1).get(j - 1));
        else curReconPrev1Node.add(null);
        if(i > 0) curReconPrev2Node.add(table.get(i - 1).get(j));
        else curReconPrev2Node.add(null);
        if(i > 0 && j > 0) curReconPrev3Node.add(table.get(i).get(j - 1));
        else curReconPrev3Node.add(null);

    if(i > 0 && j > 0) {
        

        int c1 = dp[i-1][j-1] + ((input1.charAt(i-1) == input2.charAt(j-1)) ? matchVal : mismPen);
        int c2 = dp[i-1][j] + gapPen;
        int c3 = dp[i][j-1] + gapPen;

        if(dp[i][j] == c1) {
            seqAl1 = input1.charAt(i-1) + seqAl1;
            seqAl2 = input2.charAt(j-1) + seqAl2;
            i--; j--;
            curReconStr1Node.add(i >= 0 ? string1Nodes.get(i) : null);
            curReconStr2Node.add(j >= 0 ? string2Nodes.get(j) : null);
        }
        else if(dp[i][j] == c2) {
            seqAl1 = input1.charAt(i-1) + seqAl1;
            seqAl2 = "-" + seqAl2;
            i--;
            curReconStr1Node.add(i >= 0 ? string1Nodes.get(i) : null);
            curReconStr2Node.add(null);
        }
        else {
            seqAl1 = "-" + seqAl1;
            seqAl2 = input2.charAt(j-1) + seqAl2;
            j--;
            curReconStr1Node.add(null);
            curReconStr2Node.add(j >= 0 ? string2Nodes.get(j) : null);
        }
    }


    else if(i > 0) {
        seqAl1 = input1.charAt(i-1) + seqAl1;
        seqAl2 = "-" + seqAl2;
        i--;
        curReconStr1Node.add(i >= 0 ? string1Nodes.get(i) : null);
        curReconStr2Node.add(null);
    }


    else if(j > 0) {
        seqAl1 = "-" + seqAl1;
        seqAl2 = input2.charAt(j-1) + seqAl2;
        j--;
        curReconStr1Node.add(null);
        curReconStr2Node.add(j >= 0 ? string2Nodes.get(j) : null);
    }

    System.out.println(seqAl1);
    System.out.println(seqAl2);
}

    }

    @Override
    protected void removeLastNode() {

    }

    @Override
    protected void showCode() {
        Tab seqAltab = new Tab("SeqAl");
        seqAltab.setContent(getCodeTextArea("SeqAl"));
        VisualPage.getCodePane().getTabs().add(seqAltab);
        VisualPage.getCodePane().getTabs().forEach(tab-> tab.setClosable(false));
    }

    private void refresh() {
        VisualPage.getAnimationPane().getChildren().clear();
        VisualPage.getAnimationPane().getChildren().addAll(string1Nodes);
        VisualPage.getAnimationPane().getChildren().addAll(string2Nodes);
        // VisualPage.getAnimationPane().getChildren().addAll(labels);
    }

}
