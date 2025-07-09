package Controllers;

import java.util.ArrayList;
import java.util.List;

import Pages.VisualPage;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class HomePageController {
    private List<Button> DSBtnList = new ArrayList<>();
    private List<String> DSList = List.of("List","Stack","Queue","Binary Search Tree (BST)", "Graph","Heap");
    private List<String> AlgoList = List.of("Sorting","Linked Lists","Divide and Conquer","Dynamic Programming","Greedy Algorithm","Graph Algorithms");
    public List<Button> DSButtonsCreator(Stage stage){
        for(String ds: DSList){
            DSBtnList.add(createButton(ds,stage));
        }
        return DSBtnList;
    }
    private Button createButton(String s, Stage stage){
        Button btn = new Button(s);
        btn.setStyle(
            "-fx-background-color: lightblue;" +    // background color
            "-fx-border-color: transparent;" +      // no border initially
            "-fx-font-family: 'Arial';" +            // font family
            "-fx-font-size: 16px;" +                 // font size
            "-fx-font-weight: bold;"                 // font weight
        );

        btn.setOnAction(e->changeScene(stage, btn.getText()));
        return btn;
    } 

    private void changeScene(Stage stage, String s){
        stage.getScene().setRoot(VisualPage.getView(stage, s));
    }

    public Accordion algoArchionCreate(){
        Accordion homeAccordion = new Accordion();

        for(String as: AlgoList){
            TitledPane ttlPane = createAlgoTitleBox(as);
            homeAccordion.getPanes().add(ttlPane);
        }
        return homeAccordion;
    }

    private TitledPane createAlgoTitleBox(String s){
        TitledPane algoPane = new TitledPane();
        algoPane.setText(s);
        algoPane.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        algoPane.setContent(new VBox(
            createAlgoButton("Testing")
        ));
        
        return algoPane;
    }

    private Button createAlgoButton(String s){
        Button btn = new Button(s);
        return btn;
    } 

    
}
