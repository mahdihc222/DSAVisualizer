package Helpers;

import javafx.scene.Group;
import javafx.scene.shape.Line;

public class Edge extends Group {
    ItemNode n1, n2;
    Line edge, arrow1, arrow2;
    boolean isDirected;
    double startX, startY, endX, endY;

    public Edge(ItemNode n1, ItemNode n2, boolean isDirected) {
        this.isDirected = isDirected;
        
        if(isDirected){
            addLineWithArrowhead(n1, n2);
        }
        else{
            addLine(n1, n2);
        }   
    }

    public boolean containsNode(ItemNode n) {
        if(isDirected) return n == n1;
        else return n == n1 || n == n2;
    }

    public ItemNode getn1 (){
        return n1;
    }

    public ItemNode getn2 (){
        return n2;
    }

    public boolean connects(ItemNode n1, ItemNode n2) {
        if(isDirected)
        return (this.n1 == n1 && this.n2 == n2);
        else
        return (this.n1 == n1 && this.n2 == n2) || (this.n1 == n2 && this.n2 == n1);
    }

    private void addLine(ItemNode n1, ItemNode n2){
        this.n1 = n1;
        this.n2 = n2;
        double x1 = n1.getX();
        double y1 = n1.getY();
        double x2 = n2.getX();
        double y2 = n2.getY();
        double dx = x2 - x1;
        double dy = y2 - y1;
        double dist = Math.sqrt(dx * dx + dy * dy);
        double r = n1.getNodeRadius();


        startX = x1 + dx * r / dist;
        startY = y1 + dy * r / dist;
        endX = x2 - dx * r / dist;
        endY = y2 - dy * r / dist;
                
        edge = new Line(startX, startY, endX, endY);
        getChildren().add(edge);
    }

    private void addLineWithArrowhead(ItemNode n1, ItemNode n2) {
        addLine(n1, n2);

        double dx = endX - startX;
        double dy = endY - startY;
        double angle = Math.atan2(dy, dx);

        double arrowLength = 15;
        double arrowAngle = Math.toRadians(20);  

        double x1 = endX - arrowLength * Math.cos(angle - arrowAngle);
        double y1 = endY - arrowLength * Math.sin(angle - arrowAngle);

        double x2 = endX - arrowLength * Math.cos(angle + arrowAngle);
        double y2 = endY - arrowLength * Math.sin(angle + arrowAngle);

        arrow1 = new Line(endX, endY, x1, y1);
        arrow2 = new Line(endX, endY, x2, y2);

        getChildren().addAll(arrow1, arrow2); 
    }

    public void redraw() {
        getChildren().clear();
        if(isDirected){
            addLineWithArrowhead(n1, n2);
        }
        else{
            addLine(n1, n2);
        }   
    }
}
