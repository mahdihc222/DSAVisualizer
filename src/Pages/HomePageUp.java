package Pages;

import Controllers.HomePageController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HomePageUp {
    private static ScrollPane scrollPane;
    private static GridPane menu;
    private static VBox mainVBox;
    private static HBox centerBox;
    private static Label Heading;
    private static boolean isInit = false;

    private static AnchorPane dpScene;
    private static boolean isDPInit = false;

    private static void initialize(Stage stage) {
        isInit = true;
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        mainVBox = new VBox();
        mainVBox.setPrefWidth(900);
        mainVBox.setSpacing(25);

        centerBox = new HBox(mainVBox);
        HBox.setMargin(mainVBox, new Insets(25, 25, 25, 25));
        centerBox.setAlignment(Pos.CENTER);

        Heading = new Label("Select An Option");
        Heading.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        mainVBox.getChildren().add(Heading);

        menu = new GridPane();
        menu.setHgap(10);
        menu.setVgap(10);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(10, 10, 10, 10));

        menu.add(getGridPaneItem("Graph", "Graph", stage), 0, 0);
        menu.add(getGridPaneItem("Heap", "Heap", stage), 1, 0);
        menu.add(getGridPaneItem("Sorting", "Sorting", stage), 2, 0);
        menu.add(getGridPaneItem("BST", "Binary Search Tree (BST)", stage), 3, 0);

        menu.add(getGridPaneItem("Stack", "Stack", stage), 0, 1);
        menu.add(getGridPaneItem("Queue", "Queue", stage), 1, 1);
        menu.add(getGridPaneItem("DP", "Dynamic Programming", stage), 2, 1);
        menu.add(getGridPaneItem("List", "List", stage), 3, 1);
        mainVBox.getChildren().add(menu);
        scrollPane.setContent(centerBox);

    }

    private static VBox getGridPaneItem(String imageName, String topicName, Stage stage) {
        VBox vbox = new VBox();

        vbox.setStyle(
                "-fx-background-color: #f0f0f0; " + // Normal background
                        "-fx-border-color:rgb(139, 139, 139); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 3; " +
                        "fx-border-insets: 20; " +
                        "-fx-padding: 10;");

        // Hover effect
        vbox.setOnMouseEntered(e -> vbox.setStyle(
                "-fx-background-color:rgb(179, 179, 186); " + // Lighter blue on hover
                        "-fx-border-color:rgb(185, 185, 190); " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 3; " +
                        "-fx-padding: 10; " +
                        "fx-border-insets: 20; " +
                        "-fx-cursor: hand;" // Changes cursor to hand icon
        ));

        vbox.setOnMouseExited(e -> vbox.setStyle(
                "-fx-background-color: #f0f0f0; " + // Revert to normal
                        "-fx-border-color:rgb(139,139,139); " +
                        "-fx-border-width: 1; " +
                        "-fx-border-radius: 3; " +
                        "fx-border-insets: 20; " +
                        "-fx-padding: 10;"));
        Image img = new Image(HomePageUp.class.getResourceAsStream("/Images/" + imageName + ".png"));

        ImageView imgView = new ImageView(img);
        imgView.setPreserveRatio(true);
        // imgView.fitWidthProperty().bind(vbox.widthProperty());
        // imgView.fitHeightProperty().bind(vbox.heightProperty());
        vbox.getChildren().add(imgView);
        Label lb = new Label(topicName);
        lb.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lb.setAlignment(Pos.CENTER);
        vbox.getChildren().add(lb);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefSize(200, 200);
        vbox.setOnMouseClicked(e -> {
            changeScene(stage, topicName);
        });
        return vbox;
    }

    public static Parent getView(Stage stage) {
        if (!isInit)
            initialize(stage);
        return scrollPane;
    }

    private static void changeScene(Stage stage, String s) {
        if (s.equals("Dynamic Programming")) {
            stage.getScene().setRoot(getdpScene(stage));
        } else {
            stage.getScene().setRoot(VisualPage.getView(stage, s));
        }

    }

    private static Parent getdpScene(Stage stage) {
        if (!isDPInit) {
            dpScene = new AnchorPane();
            VBox dpBox = new VBox();
            //dpBox.setAlignment(Pos.CENTER);
            dpBox.setSpacing(100);
            dpBox.setPadding(new Insets(100, 100, 100, 100));
            Label headLabel = new Label("Dynamic Programming Algorithms");
            headLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
            dpBox.getChildren().add(headLabel);
            GridPane dpSelectGrid = new GridPane();
            dpSelectGrid.setHgap(40);

            dpSelectGrid.add(getGridPaneItem("Knapsack", "Knapsack", stage), 0, 0);
            dpSelectGrid.add(getGridPaneItem("LCS", "Longest Common Subsequence", stage), 1, 0);
            dpSelectGrid.add(getGridPaneItem("SeqAl", "Sequence Alignment", stage), 2, 0);
            dpSelectGrid.setAlignment(Pos.CENTER);
            dpBox.getChildren().add(dpSelectGrid);
            dpScene.getChildren().add(dpBox);
            
            AnchorPane.setTopAnchor(dpBox, 0.0);
            //AnchorPane.setBottomAnchor(dpBox, 0.0);
            AnchorPane.setLeftAnchor(dpBox, 0.0);
            AnchorPane.setRightAnchor(dpBox, 0.0);
            Button returnButton = new Button("Return");
            returnButton.setOnAction(e -> {
                stage.getScene().setRoot(HomePageUp.getView(stage));
            });
            returnButton.setLayoutX(headLabel.getLayoutX()+1000);
            returnButton.setLayoutY(headLabel.getLayoutY());
            dpScene.getChildren().add(returnButton);
            isDPInit = true;
        }
        return dpScene;
    }

}
