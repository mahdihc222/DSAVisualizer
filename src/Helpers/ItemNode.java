package Helpers;
import javafx.scene.paint.Color;

import java.util.Objects;

import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

//ItemNode class that will hold and display data members
//Dont use other nodes, use this node class and specify type in constructor
public class ItemNode extends Group {
    private Shape boundary;
    private Text text;
    private Text index;
    private ItemNode prev=null;
    @SuppressWarnings("unused") //redundant warning, needed later
    private boolean isRectangle = true;

    private final int NODEHEIGHT = 20;
    private final int NODEWIDTH = 20;
    private final int NODERADIUS = 20;
    private final int TEXTSIZE=12; //final int to hold textsize
    private final String FONTNAME = "Arial"; //final string to hold fontname
    //constructor to create rectangular nodes with index
    public ItemNode(int elem, int nodeX, int nodeY, int ind) {
        boundary = new Rectangle(nodeX, nodeY, NODEWIDTH, NODEHEIGHT);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);
        text = new Text(String.valueOf(elem));
        text.setFont(Font.font(FONTNAME, TEXTSIZE));
        updateTextPosition();

        index = new Text(String.valueOf(ind));
        index.setX(text.getX());
        index.setY(getY() + NODEHEIGHT + 15);
        index.setFill(Color.GRAY);
        index.setFont(Font.font(FONTNAME, TEXTSIZE));

        this.getChildren().addAll(boundary, text, index);

    }
    //constructor to create rectangular nodes without index
    public ItemNode(int elem, int nodeX, int nodeY) {
        boundary = new Rectangle(nodeX, nodeY, NODEWIDTH,NODEHEIGHT);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);
        text = new Text(String.valueOf(elem));
        text.setFont(Font.font(FONTNAME, TEXTSIZE));
        updateTextPosition();

        this.getChildren().addAll(boundary, text);
    }

    //this constructor is used to construct circular nodes, which always needs prev link(except root)
    public ItemNode(int elem,int centerX, int centerY, boolean isRectangle, ItemNode prev){
        this.isRectangle = isRectangle;
        this.prev = prev;
        boundary = new Circle(centerX, centerY, NODERADIUS);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);
        
        text = new Text(String.valueOf(elem));
        text.setFont(Font.font(FONTNAME,FontWeight.BOLD,TEXTSIZE));

        updateTextPosition();

        if(this.prev!=null){
            Line connectLine = getConnectingLine();
            this.getChildren().add(connectLine);
        }

        this.getChildren().addAll(boundary,text);
    }

    public ItemNode(int elem,int centerX, int centerY, boolean isRectangle){
        this.isRectangle = isRectangle;
        boundary = new Circle(centerX, centerY, NODERADIUS);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);
        
        text = new Text(String.valueOf(elem));
        text.setFont(Font.font(FONTNAME,FontWeight.BOLD,TEXTSIZE));

        updateTextPosition();

        if(this.prev!=null){
            Line connectLine = getConnectingLine();
            this.getChildren().add(connectLine);
        }

        this.getChildren().addAll(boundary,text);
    }

    //this updateTextPosition places text at proper place
    private void updateTextPosition(){
        if(boundary instanceof Circle){
            text.setX(getX() - text.getLayoutBounds().getWidth()/2);
            text.setY(getY() + text.getLayoutBounds().getHeight()/4);
        }
        else{
            text.setX(getX() + NODEWIDTH / 2 - text.getLayoutBounds().getWidth() / 2);
            text.setY(getY() + NODEHEIGHT / 2 + text.getLayoutBounds().getHeight() / 4);
        }
        
    }

    public int getElement() {
        return Integer.parseInt(text.getText());
    }

    public double getX(){
        if(boundary instanceof Circle) return ((Circle)boundary).getCenterX();
        if(boundary instanceof Rectangle) return ((Rectangle)boundary).getX();
        return -1;
    }
    public double getY(){
        if(boundary instanceof Circle) return ((Circle)boundary).getCenterY();
        if(boundary instanceof Rectangle) return ((Rectangle)boundary).getY();
        return -1;
    }

    //function to compare if current Node is less than the passed node
    public boolean isLessThan(ItemNode other){
        return this.getElement()<other.getElement();
    }


    //function to set or change the location of a node
    //not tested yet
    public void setLocation(int x, int y){
        if(boundary instanceof Circle){
            Circle boun = (Circle) boundary;
            boun.setCenterX(x);
            boun.setCenterY(y);
            
        }
        else{
            Rectangle boun = (Rectangle)boundary;
            boun.setX(x);
            boun.setY(y);
        }
        updateTextPosition();
    }

    //function to exchange only value of two nodes
    //nodes will remain in place
    public void exchangeValueWith(ItemNode other){
        String temp = this.text.getText();
        this.text.setText(other.text.getText());
        other.text.setText(temp);
        this.updateTextPosition();
        other.updateTextPosition();
    }

    //function to set value of a node to x
    // public void setElement(Integer x) {
    //     text.setText(x.toString());

    // }

    public void flash(Color flashColor) {
        boundary.setFill(flashColor);

        PauseTransition pause = new PauseTransition(Duration.seconds(1.0)); // green for 1s
        pause.setOnFinished(e -> boundary.setFill(Color.WHITE));
        pause.play();
    }

    public void flashText(Color flashColor){
        text.setFill(flashColor);
        PauseTransition pause = new PauseTransition(Duration.seconds(2.0)); // green for 1s
        pause.setOnFinished(e -> text.setFill(Color.BLACK));
        pause.play();
    }

    public void setTextColor(Color newColor){
        text.setFill(newColor);
    }
    
    //Returns a Line connecting this node to its previous node.

    private Line getConnectingLine() {
        if (this.prev == null) return null;

        if (boundary instanceof Circle && prev.boundary instanceof Circle) {
            // Calculate direction vector
            double x1 = prev.getX();
            double y1 = prev.getY();
            double x2 = getX();
            double y2 = getY();
            double dx = x2 - x1;
            double dy = y2 - y1;
            double dist = Math.sqrt(dx * dx + dy * dy);
            double r = NODERADIUS;

            if (dist != 0) {
                double startX = x1 + dx * r / dist;
                double startY = y1 + dy * r / dist;
                double endX = x2 - dx * r / dist;
                double endY = y2 - dy * r / dist;
                return new Line(startX, startY, endX, endY);
            } else {
                return new Line(x1, y1, x2, y2);
            }
        } else if (boundary instanceof Rectangle && prev.boundary instanceof Rectangle) {
            // Connect midpoints of right side of prev to left side of current
            Rectangle prevRect = (Rectangle) prev.boundary;
            Rectangle curRect = (Rectangle) boundary;
            double prevMidY = prevRect.getY() + prevRect.getHeight() / 2;
            double curMidY = curRect.getY() + curRect.getHeight() / 2;
            double prevRightX = prevRect.getX() + prevRect.getWidth();
            double curLeftX = curRect.getX();
            return new Line(prevRightX, prevMidY, curLeftX, curMidY);
        } else {
            // Fallback: connect centers
            return new Line(prev.getX(), prev.getY(), getX(), getY());
        }
    }

    public int getNodeRadius() {
        return NODERADIUS;
    }

    public boolean equals(Object other) {
        if(this == other) return true;
        if(other == null || getClass() != other.getClass()) return false;

        int x = Integer.parseInt(text.getText());
        int y = Integer.parseInt(((ItemNode)other).text.getText());
        return x == y;
    }

    public int hashCode() {
        int x = Integer.parseInt(text.getText());
        return Objects.hash(x);
    }

}
