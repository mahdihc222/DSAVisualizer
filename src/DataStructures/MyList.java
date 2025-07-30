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

    private int lastX, currentY;
    private final int NODEWIDTH=40;
    private int currentIndex;
    private int startingX;
    private Line currentIndexIndicator;
    private final int INDICATORHEIGHT = 50;
    
    public MyList(){
        super();
        startingX = lastX = 50;
        currentY = 100;
        currentIndex = 0;
        initializeControls();
        
        VisualPage.getControlBox().getChildren().addAll(Controls);
        showCode();
        currentIndexIndicator = new Line(lastX,currentY-5,lastX,currentY-5+INDICATORHEIGHT);
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
        if(currentIndexIndicator.getStartX()==lastX) return;
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
        leftArrow.setOnAction(e->moveIndicatorLeft());  
        Button rightArrow = new Button("→");  
        rightArrow.setOnAction(e->moveIndicatorRight());
        HBox arrowBox = new HBox(5, leftArrow,rightArrow);
        VBox rightSideControlBox = new VBox(10,arrowBox);
        HBox totalBox = new HBox(20,pushPopBox,rightSideControlBox);
        Controls.add(totalBox);
    }

    @Override
    protected void showCode(){
        Tab arrayListTab = new Tab("Array List");
        arrayListTab.setContent(getCodeTextArea("ArrayList"));

        Tab linkedListTab = new Tab("Linked List");
        linkedListTab.setContent(getCodeTextArea("LinkedList"));
        VisualPage.getCodePane().getTabs().addAll(arrayListTab, linkedListTab);
        VisualPage.getCodePane().getTabs().forEach(tab-> tab.setClosable(false));

    }

    private void delete(TextField removeField){
        String input = removeField.getText().trim();
        
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                removeNode(value);
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
        dataNodes.get(currentIndex).flash(Color.GREEN);
        currentIndex++;
        for(int i=currentIndex; i<dataNodes.size();i++){
            ItemNode curNode = dataNodes.get(i);
            curNode.setLocation(curNode.getX()+10+NODEWIDTH, curNode.getY());
            curNode.setIndex(i);
        }
        lastX+= 10 + NODEWIDTH;

        currentIndexIndicator.setStartX(currentIndexIndicator.getStartX()+NODEWIDTH+10);
        currentIndexIndicator.setEndX(currentIndexIndicator.getEndX()+NODEWIDTH+10);
        VisualPage.getAnimationPane().getChildren().add(newNode);
    }

    @Override
    protected void removeLastNode() {
        if(currentIndex>0){
            currentIndex--;
            VisualPage.getAnimationPane().getChildren().removeAll(dataNodes);
            dataNodes.get(currentIndex).flash(Color.RED);
            dataNodes.remove(currentIndex);
            for(int i=currentIndex; i<dataNodes.size(); i++){
                ItemNode curNode = dataNodes.get(i);
                curNode.setLocation(curNode.getX()-(10+NODEWIDTH), curNode.getY());
                curNode.setIndex(i);
            }
            lastX-= 10 +NODEWIDTH;
            currentIndexIndicator.setStartX(currentIndexIndicator.getStartX()-(NODEWIDTH+10));
            currentIndexIndicator.setEndX(currentIndexIndicator.getEndX()-(NODEWIDTH+10));
            VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
            
        }
    }

    private void removeNode(int value){
        VisualPage.getAnimationPane().getChildren().removeAll(dataNodes);
        int i;
        for(i=0; i<dataNodes.size(); i++){
            ItemNode curNode = dataNodes.get(i);
            if(curNode.getElement() == value){
                break;
            }
        }
        final int INDEXTOREMOVE = i;
        dataNodes.remove(INDEXTOREMOVE);
        
        for(int j=INDEXTOREMOVE; j<dataNodes.size(); j++){
                ItemNode nextNode = dataNodes.get(j);
                nextNode.setLocation(nextNode.getX()-(10+NODEWIDTH), nextNode.getY());
                nextNode.setIndex(j);
            }

        lastX -= (10 + NODEWIDTH);
        if(currentIndex>=INDEXTOREMOVE){ 
            currentIndex--;
            currentIndexIndicator.setStartX(currentIndexIndicator.getStartX()-(NODEWIDTH+10));
            currentIndexIndicator.setEndX(currentIndexIndicator.getEndX()-(NODEWIDTH+10));
        }
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
        

    }
    
}
