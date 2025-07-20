package Pages;

import Algorithms.Sorting;
import DataStructures.*;
import Helpers.ItemNode;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VisualPage {
    private static StackPane root;
    private static BorderPane rootPane;
    private static Label headingLabel;
    private static Button backButton;
    private static Button copyButton;
    private static Region titleRegion;
    private static HBox titleBox;
    private static Pane animationPane;
    private static AnchorPane controlPane;
    private static VBox controlBox;
    private static VBox rightBox;
    private static boolean isInit = false;
    private static TabPane codePane;
    @SuppressWarnings("unused")
    private static DSAbstract<ItemNode> ds;

    private static void initialize(Stage stage) {
        isInit = true;
        root = new StackPane();
        root.setPadding(new Insets(10));
        rootPane = new BorderPane();
        // VBox.setMargin(rootPane, new Insets(15, 15, 15, 15));

        headingLabel = new Label("//Heading");
        headingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        copyButton = new Button("Copy Code");
        copyButton.setStyle("-fx-background-color: lightgray;" + // background color
                "-fx-border-color: transparent;" + // no border initially
                "-fx-font-family: 'Arial';" + // font family
                "-fx-font-size: 12px;");
        copyButton.setOnAction(e -> {
            Tab selectedTab = codePane.getSelectionModel().getSelectedItem();
            if (selectedTab != null) {
                // Try to find a TextArea in the tab content
                if (selectedTab.getContent() instanceof VBox vbox) {
                    for (Node node : vbox.getChildren()) {
                        if (node instanceof TextArea area) {
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            ClipboardContent content = new ClipboardContent();
                            content.putString(area.getText());
                            clipboard.setContent(content);
                            break;
                        }
                    }
                }
            }
        });
        backButton = new Button("Return");
        backButton.setStyle("-fx-background-color: lightgray;" + // background color
                "-fx-border-color: transparent;" + // no border initially
                "-fx-font-family: 'Arial';" + // font family
                "-fx-font-size: 12px;");
        backButton.setOnAction(e -> {
            animationPane.getChildren().clear(); // Clear the animation pane
            codePane.getTabs().clear();
            controlBox.getChildren().clear(); // Clear the control box

            // Action to return to the home page

            stage.getScene().setRoot(HomePage.getView(stage));
        });

        titleRegion = new Region();
        HBox.setHgrow(titleRegion, Priority.ALWAYS);

        // TitleBox to show heading and return button
        titleBox = new HBox(10,headingLabel, titleRegion,copyButton, backButton);
        titleBox.prefWidthProperty().bind(rootPane.widthProperty());

        animationPane = new Pane();
        animationPane.setStyle(
                "-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");
        codePane = new TabPane();
        codePane.setStyle(
                "-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");

        rightBox = new VBox(codePane);

        rootPane.setTop(titleBox);
        rootPane.setLeft(animationPane);
        rootPane.setRight(rightBox);

        codePane.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
        codePane.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.6));
        animationPane.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
        animationPane.prefHeightProperty().bind(rootPane.heightProperty());

        controlBox = new VBox();
        controlPane = new AnchorPane();
        controlPane.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
        controlPane.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.35));
        controlBox.setStyle(
                "-fx-background-color: #f5f5f5;" + // light gray background
                        "-fx-padding: 20;" + // inner padding
                        "-fx-border-color: #ccc;" + // light gray border
                        "-fx-border-radius: 8;" + // rounded border
                        "-fx-background-radius: 8;" // rounded background corners
        );
        controlBox.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
        controlBox.prefHeightProperty().bind(rootPane.heightProperty().multiply(0.35));

        AnchorPane.setBottomAnchor(controlBox, 0.0);
        AnchorPane.setRightAnchor(controlBox, 0.0);
        controlPane.getChildren().add(controlBox);
        controlPane.setPickOnBounds(false);
        controlBox.setMouseTransparent(false);

        root.getChildren().addAll(rootPane, controlPane);

    }

    public static Parent getView(Stage stage, String s) {
        if (!isInit)
            initialize(stage);
        headingLabel.setText(s);

        if (s.equals("Stack")) {
            ds = new Stack(200, 200);
        } else if (s.equals("Heap")) {
            ds = new Heap();
        } else if (s.equals("Binary Search Tree (BST)")) {
            ds = new BST();
        } else if (s.equals("Graph")) {
            ds = new Graph();
        } else if (s.equals("List")) {
            ds = new MyList();
        }  else if(s.equals("Queue")) {
            ds = new Queue();
        }

        else if(s.equals("Sorting")){
            new Sorting();
        }

        return root;
    }

    public static Pane getAnimationPane() {
        return animationPane;
    }

    public static VBox getControlBox() {
        return controlBox;
    }

    public static TabPane getCodePane() {
        return codePane;
    }

    // public static TextArea getCodeBox() {
    // return codeBox;
    // }

}
