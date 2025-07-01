package Helpers;
import javafx.scene.paint.Color;
import javafx.animation.PauseTransition;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

//ItemNode class that will hold and display data members
//Dont use other nodes, use this node class and specify type in constructor
public class ItemNode extends Group {
    private Shape boundary;
    private Text text;
    private Text index;

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

    //this constructor is used to construct circular nodes
    public ItemNode(int elem,int centerX, int centerY, boolean isRectangle){
        this.isRectangle = isRectangle;
        boundary = new Circle(centerX, centerY, NODERADIUS);
        boundary.setFill(Color.WHITE);
        boundary.setStroke(Color.BLACK);

        text = new Text(String.valueOf(elem));
        text.setFont(Font.font(FONTNAME,TEXTSIZE));
        updateTextPosition();

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

    public int getX(){
        if(boundary instanceof Circle) return (int)((Circle)boundary).getCenterX();
        if(boundary instanceof Rectangle) return (int)((Rectangle)boundary).getX();
        return -1;
    }
    public int getY(){
        if(boundary instanceof Circle) return (int)((Circle)boundary).getCenterY();
        if(boundary instanceof Rectangle) return (int)((Rectangle)boundary).getY();
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

}
