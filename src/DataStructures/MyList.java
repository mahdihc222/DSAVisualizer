package DataStructures;

import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class MyList extends DSAbstract<ItemNode> {

    private int currentX, currentY;
    private final int NODEWIDTH=40;
    private int currentIndex;
    private int startingX, startingY;
    private int offsetY;
    private Line currentIndexIndicator;
    private final int INDICATORHEIGHT = 50;
    public MyList(){
        super();
        startingX = currentX = 50;
        startingY= currentY = 100;
        currentIndex = 0;
        initializeControls();
        
        VisualPage.getControlBox().getChildren().addAll(Controls);
        showCode();
        currentIndexIndicator = new Line(currentX,currentY-5,currentX,currentY-5+INDICATORHEIGHT);
        currentIndexIndicator.setStroke(Color.RED);
        currentIndexIndicator.setStrokeWidth(3);
        VisualPage.getAnimationPane().getChildren().add(currentIndexIndicator);
    }

    private void moveIndicatorLeft(){
        if(currentIndexIndicator.getStartX()==startingX) return;
        currentIndex--;
        currentIndexIndicator.setStartX(currentIndexIndicator.getStartX()-(NODEWIDTH+10));
        currentIndexIndicator.setEndX(currentIndexIndicator.getEndX()-(NODEWIDTH+10));
    }

    private void moveIndicatorRight(){
        if(currentIndexIndicator.getStartX()==currentX) return;
        currentIndex++;
        currentIndexIndicator.setStartX(currentIndexIndicator.getStartX()+(NODEWIDTH+10));
        currentIndexIndicator.setEndX(currentIndexIndicator.getEndX()+(NODEWIDTH+10));
    }

    

    @Override
    protected void initializeControls() {
        TextField pushField = new TextField();
        pushField.setPromptText("Enter value");
        Button pushButton = new Button("Insert");
        // standardizeButton(pushButton);
        HBox pushRow = new HBox(20, pushField, pushButton);
        pushButton.setOnAction(e->{
            insert(pushField);
        });
        pushField.setOnKeyPressed(e->{
            if(e.getCode()==KeyCode.ENTER){
                insert(pushField);
            }
        });
        TextField removeField = new TextField();
        removeField.setPromptText("Enter value");
        Button removeButton = new Button("Remove");
        // standardizeButton(removeButton);
        removeButton.setOnAction(e->{
            delete(removeField);
        });
        HBox popRow = new HBox(20, removeField, removeButton);

        VBox pushPopBox = new VBox(40,pushRow,popRow);

        Button leftArrow = new Button("←"); 
        // standardizeButton(leftArrow);
        leftArrow.setOnAction(e->moveIndicatorLeft());  // U+2190
        Button rightArrow = new Button("→");  // U+2192
        // standardizeButton(rightArrow);
        rightArrow.setOnAction(e->moveIndicatorRight());
        HBox arrowBox = new HBox(5, leftArrow,rightArrow);

        Button bubbleSortButton = new Button("Bubble Sort");
        Button selectionSortButton = new Button("Selection Sort");
        Button insertionSortButton = new Button("Insertion Sort");
        Button mergeSortButton = new Button("Merge Sort");
        Button quickSortButton = new Button("Quick Sort");

        

        VBox rightSideControlBox = new VBox(10,arrowBox,bubbleSortButton,selectionSortButton,insertionSortButton,mergeSortButton,quickSortButton);

        HBox totalBox = new HBox(20,pushPopBox,rightSideControlBox);


        Controls.add(totalBox);
    }

    @Override
    protected void showCode(){
        Tab arrayListTab = new Tab("Array List");
        //arrayListTab.setContent(getCodeTextArea("ArrayList"));
    }

    private void delete(TextField removeField){
        String input = removeField.getText().trim();
        
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                addNode(value);
                removeField.clear(); 
            } catch (NumberFormatException ex) {
                return;
            }
        }
        else{
            removeLastNode();
        }
    }

    private void insert(TextField pushField){
        String input = pushField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                addNode(value);
                pushField.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }
    }

    @Override
    protected void addNode(int val) {
        ItemNode newNode = new ItemNode(val,(int) currentIndexIndicator.getStartX(), currentY, currentIndex);
        dataNodes.add(currentIndex,newNode);
        currentIndex++;
        for(int i=currentIndex; i<dataNodes.size();i++){
            ItemNode curNode = dataNodes.get(i);
            curNode.setLocation(curNode.getX()+10+NODEWIDTH, curNode.getY());
            curNode.setIndex(i);
        }
        currentX+= 10 + NODEWIDTH;

        currentIndexIndicator.setStartX(currentIndexIndicator.getStartX()+NODEWIDTH+10);
        currentIndexIndicator.setEndX(currentIndexIndicator.getEndX()+NODEWIDTH+10);
        VisualPage.getAnimationPane().getChildren().add(newNode);
    }

    @Override
    protected void removeLastNode() {
        if(currentIndex>0){
            currentIndex--;
            dataNodes.remove(currentIndex);
            for(int i=currentIndex; i<dataNodes.size(); i++){
                ItemNode curNode = dataNodes.get(i);
                curNode.setLocation(curNode.getX()-(10+NODEWIDTH), curNode.getY());
                curNode.setIndex(i);
            }
        
            currentIndexIndicator.setStartX(currentIndexIndicator.getStartX()-(NODEWIDTH+10));
            currentIndexIndicator.setEndX(currentIndexIndicator.getEndX()-(NODEWIDTH+10));
            VisualPage.getAnimationPane().getChildren().remove(currentIndex);
        }
    }

    // private void updateVisuals(){
    //    // VisualPage.getAnimationPane().getChildren().clear();
    //     VisualPage.getAnimationPane().getChildren().setAll(dataNodes);
    //     VisualPage.getAnimationPane().getChildren().add(currentIndexIndicator);
    // }
    
}
