package Helpers;

import javafx.scene.paint.Color;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

//Dont use
public class ListNode extends Group{
    private Rectangle rect;
    private Text text;
    private Text index;

    public ListNode(int elem,int nodeX, int nodeY, int ind){
        rect = new Rectangle(nodeX, nodeY, 20,20);
        rect.setFill(Color.WHITE);
        rect.setStroke(Color.BLACK);
        text = new Text(String.valueOf(elem));
        text.setFont(Font.font("Arial",12));
        text.setX(rect.getX()+rect.getWidth()/2 - text.getLayoutBounds().getWidth()/2);
        text.setY(rect.getY()+rect.getHeight()/2+text.getLayoutBounds().getHeight()/4);

        index = new Text(String.valueOf(ind));
        index.setX(text.getX());
        index.setY(rect.getY()+rect.getHeight()+15);
        index.setFill(Color.GRAY);
        index.setFont(Font.font("Arial",12));

        this.getChildren().addAll(rect,text,index);
        
    }

    public int getElement(){ return Integer.parseInt(text.getText());}
    public void setElement(Integer x){
        text.setText(x.toString());
        
    }

}
