package DataStructures;

import java.util.LinkedHashMap;

import java.util.Random;
import javax.swing.JOptionPane;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class BST extends DSAbstract<ItemNode> {

    private class TreeNode {
        int element;
        TreeNode left;
        TreeNode right;

        TreeNode(int elem, TreeNode left, TreeNode right) {
            element = elem;
            this.left = left;
            this.right = right;
        }
    }

    TreeNode root;
    private LinkedHashMap<TreeNode, ItemNode> map = new LinkedHashMap<>();
    private final int HSPACINGBETWEENNODES = 200;
    private LinkedHashMap<TreeNode, Line> lineMap = new LinkedHashMap<>();

    public BST() {
        super();
        startingX = 300;
        startingY = 100;
        root = null;
        initializeControls();
        showCode();
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
        VisualPage.getAnimationPane().getChildren().addAll(lineMap.values());
    }

    @Override
    protected void initializeControls() {
        TextField pushField = new TextField();
        pushField.setPromptText("Enter value");
        Button pushButton = new Button("Insert");
        HBox pushRow = new HBox(10, pushField, pushButton);

        TextField removeField = new TextField();
        removeField.setPromptText("Enter value");
        Button removeButton = new Button("Remove");
        HBox popRow = new HBox(10, removeField, removeButton);

        Button generateRandomButton = new Button("Generate Random BST");
        generateRandomButton.setOnAction(e -> generateRandomBST());

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> clear());
        VBox pushPopBox = new VBox(10, pushRow, popRow, generateRandomButton, clearButton);

        TextField searchField = new TextField();
        searchField.setPromptText("Enter value");
        Button searchButton = new Button("Search");
        HBox searchRow = new HBox(10, searchField, searchButton);

        Button maxButton = new Button("Maximum");
        Button minButton = new Button("Minimum");
        Button heightButton = new Button("Height");
        HBox minMaxRow = new HBox(10, maxButton, minButton, heightButton);

        TextField successorField = new TextField();
        successorField.setPromptText("Enter value");
        Button successorButton = new Button("Successor");
        HBox successorRow = new HBox(10, successorField, successorButton);

        TextField predecessorField = new TextField();
        predecessorField.setPromptText("Enter value");
        Button predecessorButton = new Button("Predecessor");
        HBox predecessorRow = new HBox(10, predecessorField, predecessorButton);

        VBox controlBox = new VBox(20, searchRow, minMaxRow, successorRow, predecessorRow);

        HBox totalControl = new HBox(20, pushPopBox, controlBox);

        pushButton.setOnAction(e -> insertValue(pushField));
        pushField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                insertValue(pushField);
        });
        removeButton.setOnAction(e -> removeValue(removeField));
        removeField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                removeValue(removeField);
        });

        searchButton.setOnAction(e -> searchValue(searchField));

        maxButton.setOnAction(e -> showMaximum(root, null));
        minButton.setOnAction(e -> showMinimum(root, null));
        heightButton.setOnAction(e -> findHeight());

        successorButton.setOnAction(e -> successorNode(successorField));
        predecessorButton.setOnAction(e -> predecessorNode(predecessorField));
        Controls.add(totalControl);
    }

    @Override
    protected void showCode() {
        Tab bstTab = new Tab("BST");
        bstTab.setContent(getCodeTextArea("BST"));
        VisualPage.getCodePane().getTabs().add(bstTab);
        VisualPage.getCodePane().getTabs().forEach(tab -> tab.setClosable(false));

    }

    private void insertValue(TextField pushField) {
        String input = pushField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                if (findNodeWithValue(value) == null)
                    addNode(value);
                pushField.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }
    }

    private void removeValue(TextField popField) {
        String input = popField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                removeNode(value);
                popField.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }
    }

    @Override
    protected void clear() {
        super.clear();
        map.clear();
        root = null;
    }

    private void findHeight() {
        Label heightLabel = new Label("Height: ");
        heightLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-name: Arial;");
        heightLabel.setLayoutX(40);
        heightLabel.setLayoutY(VisualPage.getAnimationPane().getHeight() - 100);
        VisualPage.getAnimationPane().getChildren().add(heightLabel);
        int[] height = {0};
        SequentialTransition sq = new SequentialTransition();
        sq.getChildren().add(showHeight(root, 1, height, heightLabel));
        PauseTransition pr = new PauseTransition(Duration.seconds(1));
        pr.setOnFinished(e->{
            VisualPage.getAnimationPane().getChildren().remove(heightLabel);
        });
        sq.getChildren().add(pr);
        sq.play();
    }

    private void searchValue(TextField searchField) {
        String input = searchField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                searchNode(root, value);
                searchField.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }
    }

    private void successorNode(TextField successorField) {
        String input = successorField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                TreeNode node = findNodeWithValue(value);
                if (node == null) {
                    JOptionPane.showMessageDialog(null, "Node with value " + value + " not found", "Warning", 1);
                    return;
                }
                showSuccessor(node);
                successorField.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }
    }

    private void predecessorNode(TextField predecessorField) {
        String input = predecessorField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                TreeNode node = findNodeWithValue(value);
                if (node == null) {
                    JOptionPane.showMessageDialog(null, "Node with value " + value + " not found", "Warning", 1);

                } else {
                    showPredecessor(node);
                }

                predecessorField.clear();
            } catch (NumberFormatException ex) {
                return;
            }
        }
    }

    private TreeNode findParentNode(TreeNode node) {
        if (node == root)
            return null;
        TreeNode current = root;
        while (current != null) {
            if (current.left == node || current.right == node)
                return current;
            else if (current.element > node.element)
                current = current.left;
            else
                current = current.right;
        }
        return null; // Node not found
    }

    private TreeNode findParentNodeForInsertion(TreeNode curr, int val) {
        if (curr.left == null && curr.right == null)
            return curr;
        if (curr.element > val) {
            if (curr.left == null)
                return curr;
            return findParentNodeForInsertion(curr.left, val);
        } else {
            if (curr.right == null)
                return curr;
            return findParentNodeForInsertion(curr.right, val);
        }
    }

    private int getLevelOfParent(TreeNode curr, int val) {
        if (curr.left == null && curr.right == null)
            return 1;
        if (curr.element > val) {
            if (curr.left == null)
                return 1;
            return getLevelOfParent(curr.left, val) + 1;
        } else {
            if (curr.right == null)
                return 1;
            return 1 + getLevelOfParent(curr.right, val);
        }
    }

    @Override
    protected void addNode(int val) {
        TreeNode newNode = new TreeNode(val, null, null);
        if (root == null) {
            root = newNode;
            ItemNode rootItem = new ItemNode(val, startingX, startingY, false);
            map.put(root, rootItem);
            dataNodes.add(rootItem);
            lineMap.put(root, null);
        } else {
            TreeNode parent = findParentNodeForInsertion(root, val);
            int level = getLevelOfParent(root, val);
            double offset = (HSPACINGBETWEENNODES * Math.pow(0.6, level));
            double x = map.get(parent).getX();
            double y = map.get(parent).getY() + 80;
            if (parent.element > val) {
                parent.left = newNode;
                x -= offset;
            } else {
                parent.right = newNode;
                x += offset;
            }
            ItemNode newItem = new ItemNode(val, (int) x, (int) y, false);
            double angle = Math.atan2(y - map.get(parent).getY(), x - map.get(parent).getX());
            double rad = ItemNode.getNodeRadius();
            double lineX1, lineX2, lineY1, lineY2;
            lineX1 = map.get(parent).getX() + rad * Math.cos(angle);
            lineY1 = map.get(parent).getY() + rad * Math.sin(angle);
            lineX2 = x - rad * Math.cos(angle);
            lineY2 = y - rad * Math.sin(angle);
            Line newLine = new Line(lineX1, lineY1, lineX2, lineY2);
            dataNodes.add(newItem);
            map.put(newNode, newItem);
            lineMap.put(newNode, newLine);
        }
        VisualPage.getAnimationPane().getChildren().add(map.get(newNode));
        if (lineMap.get(newNode) != null)
            VisualPage.getAnimationPane().getChildren().add(lineMap.get(newNode));
        map.get(newNode).flash(Color.CADETBLUE);

    }

    private TreeNode findNodeWithValue(int val) {
        TreeNode current = root;
        while (current != null) {
            if (current.element == val)
                return current;
            else if (current.element > val)
                current = current.left;
            else
                current = current.right;
        }
        return null;
    }

    private void searchNode(TreeNode current, int value) {
        if (current == null)
            return;
        PauseTransition pr = new PauseTransition(Duration.millis(1000));
        map.get(current).flash(Color.SKYBLUE);
        pr.setOnFinished(e -> {
            if (current.element > value) {
                searchNode(current.left, value);
            } else if (current.element < value) {
                searchNode(current.right, value);
            } else {
                map.get(current).flash(Color.GREEN);

            }
        });
        pr.play();

    }

    private void showMaximum(TreeNode root, TreeNode optional) {
        if (root == null)
            return;
        PauseTransition pr = new PauseTransition(Duration.millis(1000));
        map.get(root).flash(Color.SKYBLUE);
        pr.setOnFinished(e -> {
            if (root.right == null) {
                map.get(root).flash(Color.GREEN);
                if (optional != null) {
                    map.get(optional).flash(Color.GREEN);
                }
            } else {
                showMaximum(root.right, optional);
            }

        });
        pr.play();
    }

    // optional node is for tracking original node when finiding successor
    private void showMinimum(TreeNode root, TreeNode optional) {
        if (root == null)
            return;
        PauseTransition pr = new PauseTransition(Duration.millis(1000));
        map.get(root).flash(Color.SKYBLUE);
        pr.setOnFinished(e -> {
            if (root.left == null) {
                map.get(root).flash(Color.GREEN);
                if (optional != null) {
                    map.get(optional).flash(Color.GREEN);
                }
            } else {
                showMinimum(root.left, optional);
            }
        });
        pr.play();
    }

    private void showSuccessor(TreeNode node) {
        if (node == null)
            return;
        ItemNode currentItem = map.get(node);
        currentItem.setNodeColor(Color.ORANGE);
        if (node.right != null) {
            showMinimum(node.right, node);
            return;
        } else {
            showSuccessorHelper(findParentNode(node), findParentNode(findParentNode(node)), node);
        }
    }

    private void showSuccessorHelper(TreeNode from, TreeNode current, TreeNode originalNode) {
        if (current == null)
            return;
        map.get(current).flash(Color.SKYBLUE);
        map.get(from).flash(Color.ROYALBLUE);
        PauseTransition pr = new PauseTransition(Duration.seconds(1));

        pr.setOnFinished(e -> {
            if (from == current.right) {
                showSuccessorHelper(current, findParentNode(current), originalNode);
            } else {
                map.get(current).flash(Color.GREEN);
                map.get(originalNode).flash(Color.GREEN);
            }
        });
        pr.play();
    }

    private void showPredecessor(TreeNode node) {
        if (node == null)
            return;
        ItemNode currentItem = map.get(node);
        currentItem.setNodeColor(Color.ORANGE);
        PauseTransition pr = new PauseTransition(Duration.seconds(0.5));
        pr.setOnFinished(e -> {
            if (node.left != null) {
                showMaximum(node.left, node);
                return;
            } else {
                showPredecessorHelper(findParentNode(node), findParentNode(findParentNode(node)), node);
            }
        });
        pr.play();
    }

    private void showPredecessorHelper(TreeNode from, TreeNode current, TreeNode originalNode) {
        if (current == null)
            return;
        map.get(current).flash(Color.SKYBLUE);
        map.get(from).flash(Color.ROYALBLUE);
        PauseTransition pr = new PauseTransition(Duration.seconds(1));

        pr.setOnFinished(e -> {
            if (from == current.left) {
                showPredecessorHelper(current, findParentNode(current), originalNode);
            } else {
                map.get(current).flash(Color.GREEN);
                map.get(originalNode).flash(Color.GREEN);
            }
        });
        pr.play();
    }

    private SequentialTransition showHeight(TreeNode node, int lvl, int[] height,Label heightLabel){
        SequentialTransition sq = new SequentialTransition();
        if (node == null) {
            return sq;
        }
        PauseTransition pr1 = new PauseTransition(Duration.millis(400));
        pr1.setOnFinished(e->{
            map.get(node).setNodeColor(Color.ORANGE);
        });
        sq.getChildren().add(pr1);
        PauseTransition pr2 = new PauseTransition(Duration.millis(100));
        if(lvl>height[0]) height[0]=lvl;
        final int hh = height[0];
        pr2.setOnFinished(e->{
            heightLabel.setText("Height: " + hh);
        });
        sq.getChildren().add(pr2);
        
        if(node.left!=null) sq.getChildren().add(showHeight(node.left, lvl+1, height, heightLabel));
        if(node.right!=null) sq.getChildren().add(showHeight(node.right, lvl+1, height, heightLabel));

        PauseTransition pr3 = new PauseTransition(Duration.millis(400));
        pr3.setOnFinished(e->{
            if(map.get(node)!=null) map.get(node).setNodeColor(Color.WHITE);
        });

        sq.getChildren().add(pr3);
        return sq;
    }

    private void removeNode(int value) {
        TreeNode nodeToDelete = findNodeWithValue(value);
        SequentialTransition master = new SequentialTransition();
        PauseTransition pr = new PauseTransition(Duration.seconds(0.1));
        pr.setOnFinished(e -> {
            map.get(nodeToDelete).setNodeColor(Color.RED);
        });
        master.getChildren().add(pr);
        PauseTransition showColor = new PauseTransition(Duration.seconds(0.2));
        master.getChildren().add(showColor);
        if (nodeToDelete.left == null && nodeToDelete.right == null) {
            master.setOnFinished(e -> {
                VisualPage.getAnimationPane().getChildren().remove(map.get(nodeToDelete));
                VisualPage.getAnimationPane().getChildren().remove(lineMap.get(nodeToDelete));
                dataNodes.remove(map.get(nodeToDelete));
                map.remove(nodeToDelete);
                lineMap.remove(nodeToDelete);
                TreeNode parent = findParentNode(nodeToDelete);
                if (parent.right == nodeToDelete)
                    parent.right = null;
                else
                    parent.left = null;

            });
        } else if (nodeToDelete.right == null) {
            if (nodeToDelete.left.left == null && nodeToDelete.left.right == null) {
                master.getChildren().add(exchangeNodePosition(nodeToDelete, nodeToDelete.left));

            }

            else
                master.getChildren().add(transplant(nodeToDelete, nodeToDelete.left));

            master.setOnFinished(e -> {
                dataNodes.remove(map.get(nodeToDelete));
                map.remove(nodeToDelete);
                lineMap.remove(nodeToDelete);
                if (!(nodeToDelete.left.left == null && nodeToDelete.left.right == null)) {
                    refresh(true);
                } else {
                    refresh(false);
                }
            });

        } else if (nodeToDelete.left == null) {
            if (nodeToDelete.right.left == null && nodeToDelete.right.right == null) {
                master.getChildren().add(exchangeNodePosition(nodeToDelete, nodeToDelete.right));

            } else
                master.getChildren().add(transplant(nodeToDelete, nodeToDelete.right));
            master.setOnFinished(e -> {
                dataNodes.remove(map.get(nodeToDelete));
                map.remove(nodeToDelete);
                lineMap.remove(nodeToDelete);
                if (!(nodeToDelete.right.left == null && nodeToDelete.right.right == null)) {
                    refresh(true);
                } else {
                    refresh(false);
                }

            });
        } else {
            TreeNode curr = nodeToDelete.right;
            PauseTransition pause = new PauseTransition(Duration.seconds(0.1));
            pause.setOnFinished(e -> {
                map.get(curr).setNodeColor(Color.ORANGE);
            });
            master.getChildren().add(pause);

            PauseTransition pr1 = new PauseTransition(Duration.seconds(0.1));
            TreeNode minNode = minimumOfSubtree(curr);
            pr1.setOnFinished(e -> {
                showMinimum(curr.left, null);
            });
            master.getChildren().add(pr1);

            if (findParentNode(minNode) != nodeToDelete) {
                if (minNode.right != null)
                    master.getChildren().add(transplant(minNode, minNode.right));
                PauseTransition breaker = new PauseTransition(Duration.seconds(0.1));
                breaker.setOnFinished(e -> {
                    if (minNode.right == null) {
                        findParentNode(minNode).left = null;
                    }
                    minNode.right = nodeToDelete.right;

                });
                master.getChildren().add(breaker);

                master.getChildren().add(moveNode(minNode,
                        map.get(nodeToDelete).getX() - map.get(minNode).getX(),
                        map.get(nodeToDelete).getY() - map.get(minNode).getY()));
                PauseTransition updaterParent = new PauseTransition(Duration.millis(100));
                updaterParent.setOnFinished(e -> {
                    TreeNode parent = findParentNode(nodeToDelete);
                    if (parent == null) {
                        root = minNode;
                    } else if (parent.left == nodeToDelete) {
                        parent.left = minNode;
                    } else {
                        parent.right = minNode;
                    }
                });
                master.getChildren().add(updaterParent);
            } else {
                master.getChildren().add(transplant(nodeToDelete, minNode));
            }

            PauseTransition pr3 = new PauseTransition(Duration.seconds(0.1));
            pr3.setOnFinished(e -> {
                minNode.left = nodeToDelete.left;
            });
            master.getChildren().add(pr3);
            master.setOnFinished(e -> {
                dataNodes.remove(map.get(nodeToDelete));
                map.remove(nodeToDelete);

                if (lineMap.get(nodeToDelete) != null) {
                    lineMap.put(minNode, lineMap.get(nodeToDelete));
                    lineMap.remove(nodeToDelete);
                }
                refresh(true);
            });
        }

        master.play();
    }

    private void refresh(boolean isFixNeeded) {
        VisualPage.getAnimationPane().getChildren().clear();
        lineMap.clear();
        fixBST(root, 1);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
        VisualPage.getAnimationPane().getChildren().addAll(lineMap.values());
        for (ItemNode itemNode : dataNodes) {
            itemNode.setNodeColor(Color.WHITE);
        }
    }

    private ParallelTransition transplant(TreeNode a, TreeNode b) {
        ParallelTransition prt = new ParallelTransition();
        double dx = map.get(a).getX() - map.get(b).getX();
        double dy = map.get(a).getY() - map.get(b).getY();
        TreeNode parent = findParentNode(a);

        java.util.Stack<TreeNode> stack = new java.util.Stack<>();
        stack.push(b);
        while (!stack.empty()) {
            TreeNode node = stack.pop();
            prt.getChildren().add(moveNode(node, dx, dy));
            if (node != b && lineMap.get(node) != null) {
                prt.getChildren().add(moveLine(lineMap.get(node), dx, dy));
            }
            if (node.right != null)
                stack.push(node.right);
            if (node.left != null)
                stack.push(node.left);

        }
        prt.setOnFinished(e -> {
            VisualPage.getAnimationPane().getChildren().remove(lineMap.get(b));

            lineMap.remove(b);
            lineMap.put(b, lineMap.get(a));
            if (parent == null) {
                root = b;
            } else if (parent.left == a) {
                parent.left = b;
            } else {
                parent.right = b;
            }

        });
        return prt;
    }

    private TranslateTransition moveLine(Line l, double dx, double dy) {
        TranslateTransition tr = new TranslateTransition(Duration.seconds(1), l);
        tr.setByX(dx);
        tr.setByY(dy);
        tr.setOnFinished(e -> {
            l.setTranslateX(0);
            l.setTranslateY(0);
            l.setStartX(l.getStartX() + dx);
            l.setEndX(l.getEndX() + dx);
            l.setStartY(l.getStartY() + dy);
            l.setEndY(l.getEndY() + dy);
        });
        return tr;
    }

    private TranslateTransition moveNode(TreeNode node, double dx, double dy) {

        TranslateTransition tr = new TranslateTransition(Duration.seconds(1), map.get(node));
        tr.setByX(dx);
        tr.setByY(dy);
        tr.setOnFinished(e -> {
            map.get(node).setTranslateX(0);
            map.get(node).setTranslateY(0);
            map.get(node).setLocation(map.get(node).getX() + dx, map.get(node).getY() + dy);
        });
        return tr;

    }

    private TranslateTransition exchangeNodePosition(TreeNode a, TreeNode b) {
        ItemNode aNode = map.get(a);
        ItemNode bNode = map.get(b);
        TreeNode parent = findParentNode(a);

        TranslateTransition tr = new TranslateTransition(Duration.seconds(1), bNode);
        tr.setByX(aNode.getX() - bNode.getX());
        tr.setByY(aNode.getY() - bNode.getY());
        tr.setOnFinished(e -> {
            bNode.setTranslateX(0);
            bNode.setTranslateY(0);
            bNode.setLocation(bNode.getX(), bNode.getY());
            bNode.setLocation(aNode.getX(), aNode.getY());

            VisualPage.getAnimationPane().getChildren().remove(lineMap.get(b));
            VisualPage.getAnimationPane().getChildren().remove(aNode);
            lineMap.remove(b);
            lineMap.put(b, lineMap.get(a));
            if (parent == null) {
                root = b;
            } else if (parent.left == a) {
                parent.left = b;
            } else {
                parent.right = b;
            }
        });
        return tr;

    }

    private TreeNode minimumOfSubtree(TreeNode node) {
        if (node.left == null)
            return node;
        return minimumOfSubtree(node.left);
    }

    private void fixBST(TreeNode node, int lvl) {
        if (node == null)
            return;

        ItemNode parentNode = map.get(node);
        double parX = parentNode.getX();
        double parY = parentNode.getY();
        double currY = parY + 80;
        double currX = parX;
        double offset = HSPACINGBETWEENNODES * Math.pow(0.6, lvl);
        if (node.left != null) {
            ItemNode leftNode = map.get(node.left);
            currX -= offset;
            leftNode.setLocation((int) currX, (int) currY);
            double angle = Math.atan2(currY - parY, currX - parX);
            double rad = ItemNode.getNodeRadius();
            double lineX1, lineX2, lineY1, lineY2;
            lineX1 = parX + rad * Math.cos(angle);
            lineY1 = parY + rad * Math.sin(angle);
            lineX2 = currX - rad * Math.cos(angle);
            lineY2 = currY - rad * Math.sin(angle);
            Line newLine = new Line(lineX1, lineY1, lineX2, lineY2);
            lineMap.put(node.left, newLine);
        }

        currX = parX;
        if (node.right != null) {
            ItemNode rightNode = map.get(node.right);
            currX += offset;
            rightNode.setLocation((int) currX, (int) currY);
            double angle = Math.atan2(currY - parY, currX - parX);
            double rad = ItemNode.getNodeRadius();
            double lineX1, lineX2, lineY1, lineY2;
            lineX1 = parX + rad * Math.cos(angle);
            lineY1 = parY + rad * Math.sin(angle);
            lineX2 = currX - rad * Math.cos(angle);
            lineY2 = currY - rad * Math.sin(angle);
            Line newLine = new Line(lineX1, lineY1, lineX2, lineY2);
            lineMap.put(node.right, newLine);
        }

        if (node.left != null) {
            fixBST(node.left, lvl + 1);
        }
        if (node.right != null) {
            fixBST(node.right, lvl + 1);
        }
    }
    @Override
    protected void removeLastNode() {
        // This function needs no implementation for BST
    }

    private void generateRandomBST() {
        Random r = new Random();

        for (int i = 0; i < 8; i++) {
            addNode(r.nextInt(98) + 1);
        }
    }

}
