package Helpers;

import javafx.scene.paint.Color;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Node extends Group{
    private Rectangle rect;
    private Text text;

    public Node(Integer elem,int nodeX, int nodeY){
        rect = new Rectangle(nodeX, nodeY, 20,20);
        
        text = new Text(elem.toString());
        text.setFont(Font.font("Arial",12));
        text.setX(rect.getX()+rect.getWidth()/2 - text.getLayoutBounds().getWidth()/2);
        text.setY(rect.getY()+rect.getHeight()/2 + text.getLayoutBounds().getHeight()/2);
        text.setFill(Color.WHITE);
        this.getChildren().addAll(rect,text);
    }

    public int getElement(){ return Integer.parseInt(text.getText());}
    public void setElement(Integer x){
        text.setText(x.toString());
        
    }

}
