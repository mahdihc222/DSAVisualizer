package Pages;

import java.util.List;

import DataStructures.Array;
import DataStructures.Stack;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class VisualPage {
    private static VBox root;
    private static BorderPane rootPane;
    private static Label headingLabel;
    private static Button backButton;
    private static Region titleRegion;
    private static HBox titleBox;
    private static Pane animationPane;
    private static TextArea codeBox;
    private static VBox rightBox;
    private static boolean isInit = false;
    private static Array tempArr;
    private static  List<Integer> temp = List.of(12,76,43,9);
    private static void initialize(Stage stage){
        isInit = true;
        root = new VBox();
        rootPane = new BorderPane();
        VBox.setMargin(rootPane, new Insets(15));


        headingLabel = new Label(getHeading());
        headingLabel.setFont(Font.font("Arial",FontWeight.BOLD,20));

        backButton = new Button("Return");
        backButton.setStyle( "-fx-background-color: lightgray;" +    // background color
            "-fx-border-color: transparent;" +      // no border initially
            "-fx-font-family: 'Arial';" +            // font family
            "-fx-font-size: 12px;"
            );
        backButton.setOnAction(e->{
            stage.getScene().setRoot(HomePage.getView(stage));
        });

        titleRegion = new Region();
        HBox.setHgrow(titleRegion, Priority.ALWAYS);
        
        //TitleBox to show heading and return button
        titleBox = new HBox(headingLabel,titleRegion,backButton);
        titleBox.prefWidthProperty().bind(rootPane.widthProperty());

        animationPane = new Pane();
        animationPane.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");
        // tempArr = new Array(100,100,temp);
        // tempArr.insert(90);
        // tempArr.removeLast();
        // tempArr.insert(10);
        //tempArr.remove(1);
        // animationPane.getChildren().addAll(tempArr.getVisibleArray());

            
        codeBox = new TextArea("//Code will be shown here");
        codeBox.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 8; -fx-background-radius: 8;");
        codeBox.setFont(Font.font("Consolas",14));
        codeBox.setEditable(false);
        // codeBox.setWrapText(true);
        
        
        rightBox = new VBox(codeBox);

        rootPane.setTop(titleBox);
        rootPane.setLeft(animationPane);
        rootPane.setRight(rightBox);


        codeBox.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
        codeBox.prefHeightProperty().bind(root.heightProperty().multiply(0.6));
        animationPane.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
        animationPane.prefHeightProperty().bind(root.heightProperty());

       
        


        root.getChildren().add(rootPane);        

    }

    private static String getHeading(){
        return "Heading";
    }
    public static Parent getView(Stage stage, String s){
        if(!isInit) initialize(stage);
        headingLabel.setText(s);
        if(s.equals("Stack")) {
            codeBox.setText(Stack.getCode());
            
        }
        return root;
    }
}
