package com.lc.redblacktree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lc
 * 红黑树性质
 * 节点是红色或黑色。
 * 根是黑色。
 * 所有叶子都是黑色（叶子是NIL节点）。、每个红色节点必须有两个黑色的子节点。（从每个叶子到根的所有路径上不能有两个连续的红色节点。）
 * 从任一节点到其每个叶子的所有简单路径都包含相同数目的黑色节点。
 */
public class RedBlackTree {
    /**
     * The root of RBTree
     */
    private TreeNode root;

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public RedBlackTree() {
        this.root = null;
    }

    public RedBlackTree(TreeNode root) {
        this.root = root;
    }
    /*********get操作*******/
    /**
     * 根据传入的value获取对应的节点
     *
     * @param value
     * @return
     */
    public TreeNode getNode(int value) {
        if (getRoot() == null) {
            return null;
        }
        return getNodeRecursive(getRoot(), value);
    }

    /**
     * 获取给定节点的祖父节点，前提是该节点一定存在granPa
     *
     * @param current
     * @return
     */
    public TreeNode grandPaNode(TreeNode current) {
        return current.getParent().getParent();
    }

    /**
     * 获取树高度，定义只有一个节点时为高度1
     *
     * @return
     */
    public int getHeight(){
        return getHeight(getRoot());
    }

    private int getHeight(TreeNode current) {
        if (current == null) {
            return 0;
        }
        int leftHeight = getHeight(current.getLeft());
        int rightHeight = getHeight(current.getRight());
        return (leftHeight > rightHeight ? leftHeight : rightHeight) + 1;
    }

    /**
     * 获取给定节点的叔叔节点，前提是该节点一定存在uncle
     *
     * @param current
     * @return
     */
    public TreeNode uncleNode(TreeNode current) {
        TreeNode parent = current.getParent();
        TreeNode grandPa = grandPaNode(current);
        if (grandPa.getLeft() == parent) {
            return grandPa.getRight();
        } else {
            return grandPa.getLeft();
        }
    }

    /**
     * 递归获取值
     *
     * @param current
     * @param value
     * @return
     */
    private TreeNode getNodeRecursive(TreeNode current, final int value) {
        if (current == null) {
            return null;
        }
        if (current.getValue() == value) {
            return current;
        } else if (current.getValue() < value) {
            return getNodeRecursive(current.getRight(), value);
        } else {
            return getNodeRecursive(current.getLeft(), value);
        }
    }

    /********中序遍历获取所有的value*******/
    /**
     * 中序遍历将value放入List，利用红黑树排序
     *
     * @param current
     * @param data
     */
    public List<Integer> inOrder(){
        List<Integer> data = new ArrayList<>();
        inOrder(getRoot(),data);
        return data;
    }
    private void inOrder(TreeNode current, List<Integer> data) {
        if (data == null) {
            data = new ArrayList<Integer>();
        }
        if (current == null) {
            return;
        }
        inOrder(current.getLeft(), data);
        data.add(current.getValue());
        inOrder(current.getRight(), data);
    }

    /******************insert操作********************************/
    /**
     * 插入操作分为两个步骤，首先按照一般的二叉查找树插入，然后再做调整；
     * 特殊情况：如果插入的value已存在则直接返回
     *
     * @param value
     */
    public void insert(final int value) {
        TreeNode current = null;
        if (getRoot() == null) {
            root = new TreeNode(value);
            current = root;
        } else {
            TreeNode temp = root;
            TreeNode parent = null;
            while (temp != null) {
                parent = temp;
                if (temp.getValue() == value) {
                    System.out.println("已存在该Value，无需插入");
                    return;
                } else if (temp.getValue() > value) {
                    temp = temp.getLeft();
                } else {
                    temp = temp.getRight();
                }
            }
            if (parent.getValue() > value) {
                parent.setLeft(new TreeNode(value));
                current = parent.getLeft();
            } else {
                parent.setRight(new TreeNode(value));
                current = parent.getRight();
            }
            /**
             * 设置其父节点
             */
            current.setParent(parent);
        }
        /**
         * 调整颜色或者旋转,如果不调用adjust，就是一般的二叉查找树
         */
        adjust(current);
    }

    /**
     * 调整分为多个case
     *
     * @param current
     */
    public void adjust(TreeNode current) {
        /**
         * case1：插入节点无父节点，只需要将color设置为黑色即可
         */
        if (current.getParent() == null) {
            current.setColor(Color.BLACK);
            return;
        }
        /**
         * case2：如果插入节点的父节点是黑色节点，插入一个红色节点无需做任何调整
         */
        if (current.getParent().getColor() == Color.BLACK) {
            return;
        }
        /**
         * 接下来的所有父节点都是红色的，也就是说current必有祖父节点grandPa，grandPa是黑色，因此current存在叔叔节点uncle
         * uncle节点可能为黑色，也可能是红色
         *
         **/
        /**
         * case3：current的叔叔节点uncle为红色，将parent和uncle设置为黑色，grandpa设置为红色，然后在从grandPa节点开始重新调整
         */
        if (uncleNode(current) != null && uncleNode(current).getColor() == Color.RED) {
            current.getParent().setColor(Color.BLACK);
            uncleNode(current).setColor(Color.BLACK);
            grandPaNode(current).setColor(Color.RED);
            adjust(grandPaNode(current));
            return;
        }
        /**
         * case4：current的叔叔节点uncle为黑色或者为null，在如此条件下需要分如下四个case
         * case4.1：current的parent是grandpa的左节点，current是parent的左节点，则以parent为中心右旋转，并调整颜色
         * case4.2：current的parent是grandpa的左节点，current是parent的右节点，先左旋转，然后右旋转，并调整调整
         * case4.3：current的parent是grandpa的右节点，current是parent的左节点，先右旋转，然后左旋转，并调整调整
         * case4.4：current的parent是grandpa的右节点，current是parent的右节点，则以parent为中心左旋转，并调整颜色
         */
        if (uncleNode(current) == null || uncleNode(current).getColor() == Color.BLACK) {
            /**
             * case4.1
             */
            if (current.getParent() == grandPaNode(current).getLeft() && current == current.getParent().getLeft()) {
                /**
                 * 此处先调色，这样代码更少
                 */
                current.getParent().setColor(Color.BLACK);
                grandPaNode(current).setColor(Color.RED);
                rotateRight(current.getParent());

            } else if (current.getParent() == grandPaNode(current).getLeft() && current == current.getParent().getRight()) {
                /**
                 * case4.2
                 */
                rotateLeft(current);
                rotateRight(current);
                current.setColor(Color.BLACK);
                current.getRight().setColor(Color.RED);
                current.getLeft().setColor(Color.RED);
            } else if (current.getParent() == grandPaNode(current).getRight() && current == current.getParent().getLeft()) {
                /**
                 * case4.3
                 */
                rotateRight(current);
                rotateLeft(current);
                current.setColor(Color.BLACK);
                current.getRight().setColor(Color.RED);
                current.getLeft().setColor(Color.RED);
            } else if (current.getParent() == grandPaNode(current).getRight() && current == current.getParent().getRight()) {
                /**
                 * case4.4
                 */
                current.getParent().setColor(Color.BLACK);
                grandPaNode(current).setColor(Color.RED);
                rotateLeft(current.getParent());
            }

        }
    }

    /**
     * 左旋转,此处的左旋转是指将current作为中心，将current的parent置为current的left，原有的current的left置为parent的right，current会作为其grandpa的一个子节点
     *
     * @param current
     * @return
     */
    public void rotateLeft(TreeNode current) {
        TreeNode parent = current.getParent();
        /**
         * grandpa可能会为null，currentLeft 可能为null
         */
        TreeNode grandpa = parent.getParent();
        TreeNode currentLeft = current.getLeft();

        parent.setRight(currentLeft);
        if (currentLeft != null) {
            currentLeft.setParent(parent);
        }
        current.setLeft(parent);
        parent.setParent(current);

        //这一个判断很重要
        if (getRoot() == parent) {
            root = current;
        }
        current.setParent(grandpa);
        if (grandpa != null) {
            if (grandpa.getRight() == parent) {
                grandpa.setRight(current);
            } else {
                grandpa.setLeft(current);
            }
        }
    }

    /**
     * 右旋转，此处的右旋转是指将current作为中心，将current的parent置为current的right，原有的current的right置为parent的left，current会作为其grandpa的一个子节点
     *
     * @param current
     * @return
     */
    public void rotateRight(TreeNode current) {
        TreeNode parent = current.getParent();
        /**
         * grandpa可能会为null，currentRight 可能为null
         */
        TreeNode grandpa = parent.getParent();
        TreeNode currentRight = current.getRight();
        parent.setLeft(currentRight);
        if (currentRight != null) {
            currentRight.setParent(parent);
        }
        current.setRight(parent);
        parent.setParent(current);

        if (getRoot() == parent) {
            root = current;
        }
        current.setParent(grandpa);
        if (grandpa != null) {
            if (grandpa.getRight() == parent) {
                grandpa.setRight(current);
            } else {
                grandpa.setLeft(current);
            }
        }
    }

}
