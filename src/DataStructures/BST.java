package DataStructures;

import java.util.LinkedHashMap;
import java.util.Random;
import java.util.function.Consumer;
import javax.swing.JOptionPane;
import Helpers.ItemNode;
import Pages.VisualPage;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BST extends DSAbstract<ItemNode> {

    private class TreeNode {
        int element;
        TreeNode left;
        TreeNode right;
        int x, y;

        TreeNode(int elem, TreeNode left, TreeNode right) {
            element = elem;
            this.left = left;
            this.right = right;
        }
    }

    TreeNode root;
    private LinkedHashMap<TreeNode, ItemNode> map = new LinkedHashMap<>();
    private final int HSPACINGBETWEENNODES = 200;

    public BST() {
        super();
        startingX = 300;
        startingY = 100;
        root = null;
        initializeControls();
        showCode();
        VisualPage.getControlBox().getChildren().addAll(Controls);
        VisualPage.getAnimationPane().getChildren().addAll(dataNodes);
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
        generateRandomButton.setOnAction(e->generateRandomBST());

        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e->clear());
        VBox pushPopBox = new VBox(10, pushRow, popRow,generateRandomButton,clearButton);

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
    protected void showCode(){
        Tab bstTab = new Tab("BST");
        bstTab.setContent(getCodeTextArea("BST"));
        VisualPage.getCodePane().getTabs().add(bstTab);
        VisualPage.getCodePane().getTabs().forEach(tab-> tab.setClosable(false));

    }

    private void insertValue(TextField pushField) {
        String input = pushField.getText().trim();
        if (!input.isEmpty()) {
            try {
                int value = Integer.parseInt(input);
                if(findNodeWithValue(value)==null)
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
    protected void clear(){
        super.clear();
        map.clear();
        root = null;
    }

    private void findHeight() {
        Label heightLabel = new Label("Height: " + 0);
        heightLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-name: Arial;");
        heightLabel.setLayoutX(VisualPage.getAnimationPane().getLayoutX() + 20);
        heightLabel.setLayoutY(VisualPage.getAnimationPane().getHeight() - 100);
        VisualPage.getAnimationPane().getChildren().add(heightLabel);
        showHeight(root, heightLabel, height -> {
            heightLabel.setText("Height: " + height);
        });
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
            ItemNode rootItem = new ItemNode(val, startingX, startingY, false, null);
            map.put(root, rootItem);
            dataNodes.add(rootItem);
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
            ItemNode newItem = new ItemNode(val, (int) x, (int) y, false, map.get(parent));
            dataNodes.add(newItem);
            map.put(newNode, newItem);
        }
        VisualPage.getAnimationPane().getChildren().add(map.get(newNode));
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

    private void showHeight(TreeNode node, Label heightLabel, Consumer<Integer> callback) {
        if (node == null) {
            callback.accept(0); // base case: null has height 0
            return;
        }

        map.get(node).setNodeColor(Color.web("#a5b6d9")); // darken current

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {

            // recurse left
            showHeight(node.left, heightLabel, leftHeight -> {

                PauseTransition pauseRight = new PauseTransition(Duration.seconds(1));
                pauseRight.setOnFinished(e2 -> {

                    // recurse right
                    showHeight(node.right, heightLabel, rightHeight -> {

                        int currentHeight = Math.max(leftHeight, rightHeight) + 1;

                        heightLabel.setText("Height: " + currentHeight);
                        map.get(node).setNodeColor(Color.WHITE); // reset color

                        PauseTransition update = new PauseTransition(Duration.seconds(1));
                        update.setOnFinished(e3 -> {
                            callback.accept(currentHeight); // return value to parent
                        });
                        update.play();
                    });

                });
                pauseRight.play();
            });
        });
        pause.play();
    }

    // Codes for removing a node fromt BST

    private void removeNode(int value) {
        TreeNode nodeToDelete = findNodeWithValue(value);
        if (nodeToDelete == null) {
            JOptionPane.showMessageDialog(null, "Node with value " + value + " not found", "Warning", 1);
            return;
        }

        map.get(nodeToDelete).flash(Color.RED);
        PauseTransition pr = new PauseTransition(Duration.seconds(1));
        pr.setOnFinished(e -> {
            treeDelete(nodeToDelete);
        });
        pr.play();
    }

    private void treeDelete(TreeNode u) {
        if (u.left == null) {
            transplant(u, u.right);
        } else if (u.right == null) {
            transplant(u, u.left);
        } else {
            TreeNode y = treeMinimum(u.right);
            if (findParentNode(y) != u) {
                animateTransplantPreview(y, y.right, () -> {
                    transplant(y, y.right);
                    y.right = u.right;
                    animateTransplantPreview(u, y, () -> {
                        transplant(u, y);
                        y.left = u.left;
                        deleteNodeDirectly(u);
                        repositionTree();
                    });
                });
            } else {
                animateTransplantPreview(u, y, () -> {
                    transplant(u, y);
                    y.left = u.left;
                    deleteNodeDirectly(u);
                    repositionTree();
                });
            }
        }
    }

    private TreeNode treeMinimum(TreeNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void transplant(TreeNode u, TreeNode v) {
        TreeNode parent = findParentNode(u);
        if (parent == null) {
            root = v;
        } else if (u == parent.left) {
            parent.left = v;
        } else {
            parent.right = v;
        }

        if (u.left == null && u.right == null) {
            deleteNodeDirectly(u);
            repositionTree();
        } else if (u.left == null || u.right == null) {
            deleteNodeDirectly(u);
            repositionTree();
        }
    }

    private void animateTransplantPreview(TreeNode u, TreeNode v, Runnable callback) {
        if (v != null) {
            ItemNode replacementItem = new ItemNode(v.element,(int)(VisualPage.getAnimationPane().getWidth() - 100), 50, false, null);
            replacementItem.setLayoutX(VisualPage.getAnimationPane().getWidth() - 100);
            replacementItem.setLayoutY(50);
            replacementItem.flash(Color.YELLOW);

            VisualPage.getAnimationPane().getChildren().add(replacementItem);
            dataNodes.add(replacementItem);

            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
            pause.setOnFinished(e -> {
                VisualPage.getAnimationPane().getChildren().remove(replacementItem);
                dataNodes.remove(replacementItem);
                callback.run();
            });
            pause.play();
        } else {
            callback.run();
        }
    }

    private void deleteNodeDirectly(TreeNode nodeToDelete) {
        ItemNode itemToDelete = map.get(nodeToDelete);
        dataNodes.remove(itemToDelete);
        VisualPage.getAnimationPane().getChildren().remove(itemToDelete);
        map.remove(nodeToDelete);
    }

    private void repositionTree() {
        if (root != null) {
            assignPositions(root, VisualPage.getAnimationPane().getWidth() / 2, 50,
                    VisualPage.getAnimationPane().getWidth() / 4);
            updateVisualPositions();
        }
    }

    private void assignPositions(TreeNode node, double x, double y, double offset) {
        if (node == null)
            return;

        node.x = (int) x;
        node.y = (int) y;

        if (node.left != null) {
            assignPositions(node.left, x - offset, y + 80, offset / 2);
        }
        if (node.right != null) {
            assignPositions(node.right, x + offset, y + 80, offset / 2);
        }
    }

    private void updateVisualPositions() {
        for (TreeNode node : map.keySet()) {
            ItemNode item = map.get(node);
            item.setLayoutX(node.x);
            item.setLayoutY(node.y);
        }
    }

    @Override
    protected void removeLastNode() {
        // This function needs no implementation for BST
    }

    private void generateRandomBST(){
        Random r = new Random();

        for(int i=0; i<8;i++){
            addNode(r.nextInt(98)+1);
        }
    }

}
