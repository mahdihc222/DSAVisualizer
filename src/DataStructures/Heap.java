package DataStructures;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

// class NodeComparator implements Comparator<CircularNode>{

//     @Override
//     public int compare(CircularNode o1, CircularNode o2) {
//         return o2.getElement()-o1.getElement();
//     }

// }

public class Heap extends DSAbstract<ItemNode> {
    //private PriorityQueue<CircularNode> heap = new PriorityQueue<>(new NodeComparator());
    private int currentX, currentY;
    private Integer index=1;
    private int distance=140;
    public Heap(){
        super();
        currentX =startingX = 250;
        currentY = startingY = 100;
        initializeControls();
        VisualPage.getCodeBox().setText(getCode());
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
    }

    @Override
    public String getCode() {
        return "Not implemented yet";
    }
    @Override
    protected void initializeControls() {
        TextField pushField = new TextField();
        pushField.setPromptText("Enter value");
        Button pushButton = new Button("Insert");
        HBox pushRow = new HBox(10, pushField, pushButton);

        Button popButton = new Button("Remove");
        HBox popRow = new HBox(10, popButton);

        pushButton.setOnAction(e->pushValue(pushField));

        Controls.add(pushRow);
        Controls.add(popRow);
    }
    private void pushValue(TextField pushField){
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
        if(Integer.highestOneBit(index)==index && index>1){
            currentY+=40;
            if(distance>20) distance -=20;
        } 
        if(index>1)
            if(index%2==0) currentX = dataNodes.get(index/2-1).getX()-distance;
            else currentX = dataNodes.get(index/2-1).getX()+distance;
        ItemNode node = new ItemNode(val,currentX, currentY,false);
        index++;
        dataNodes.add(node);
        if(index>2) heapify();
        VisualPage.getAnimationPane().getChildren().setAll(dataNodes);
    }

    private void heapify(){
       int i = index-2; //index of new node
       int parentIndex = (i)/2; //parent of that node
       while(i>0 &&dataNodes.get(i).isLessThan(dataNodes.get(parentIndex))){
            dataNodes.get(i).exchangeValueWith(dataNodes.get(parentIndex));
            i = parentIndex;
            parentIndex = i/2;
       }
    }

    @Override
    protected void removeLastNode(){

    }
    




}
