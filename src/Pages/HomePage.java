package Pages;

import Controllers.HomePageController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HomePage {
    private static ScrollPane scrollPane;
    private static VBox mainVBox;
    private static HBox centerBox;
    private static Label DSHeading;
    private static Label AlgoHeading;
    private static boolean isInit=false;
    private static HomePageController controller;
    private static void initialize(Stage stage){
        isInit=true;
        controller = new HomePageController();
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        mainVBox = new VBox();
        mainVBox.setPrefWidth(800);
        mainVBox.setSpacing(10);


        centerBox = new HBox(mainVBox);
        HBox.setMargin(mainVBox, new Insets(40,0,0,0));
        centerBox.setAlignment(Pos.CENTER);


        

        DSHeading = new Label("Data Structures");
        DSHeading.setFont(Font.font("Arial",FontWeight.BOLD, 36));
        mainVBox.getChildren().add(DSHeading);
        mainVBox.getChildren().addAll(controller.DSButtonsCreator(stage));

        AlgoHeading = new Label("Algorithms");
        AlgoHeading.setFont(Font.font("Arial",FontWeight.BOLD, 36));

        
        mainVBox.getChildren().add(AlgoHeading);
        VBox.setMargin(AlgoHeading, new Insets(20,0,0,0));
        mainVBox.getChildren().add(controller.createButton("Sorting",stage));
        mainVBox.getChildren().add(controller.algoArchionCreate(stage));
        scrollPane.setContent(centerBox);
    }

    public static Parent getView(Stage stage){
        if(!isInit) initialize(stage);
        return scrollPane;
    }
}
