package com.lc.redblacktree;

/**
 * node of RBTree，the value is an integer
 *
 * @author lc
 */
public class TreeNode {
    /**
     * left node
     */
    private TreeNode left;
    /**
     * right node
     */
    private TreeNode right;
    /**
     * parent node
     */
    private TreeNode parent;
    /**
     * the value of a node
     */
    private int value;
    /**
     * the color of a node
     */
    private Color color;

    /**
     * constructor,all of the node will be initialized with the red color
     */
    public TreeNode() {
        this.left = null;
        this.right = null;
        this.parent = null;
        this.color = Color.RED;
    }

    /**
     * constructor
     *
     * @param value
     */
    public TreeNode(int value) {
        this.left = null;
        this.right = null;
        this.parent = null;
        this.value = value;
        this.color = Color.RED;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public TreeNode getParent() {
        return parent;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "value:" + this.value + "parent:" + (parent == null ? "null" : parent.getValue());
    }
}
