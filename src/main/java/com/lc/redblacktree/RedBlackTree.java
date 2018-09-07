package com.lc.redblacktree;

import sun.reflect.generics.tree.Tree;

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
    public int getHeight() {
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
     * 获取current的兄弟节点
     *
     * @param current
     * @return
     */
    public TreeNode siblingNode(TreeNode current) {
        if (current == null) {
            return null;
        }
        if (current.getParent() == null) {
            return null;
        }
        if (current.getParent().getLeft() == current) {
            return current.getParent().getRight();
        } else {
            return current.getParent().getLeft();
        }
    }

    /**
     * 获取current子节点
     * 如果左右节点非空，返回left
     *
     * @param current
     * @return
     */
    public TreeNode getChild(TreeNode current) {
        if (current != null) {
            if (current.getLeft() != null) {
                return current.getLeft();
            } else {
                return current.getRight();
            }
        } else {
            return null;
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
     */
    public List<Integer> inOrder() {
        List<Integer> data = new ArrayList<>();
        inOrder(getRoot(), data);
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
        adjust_insert(current);
    }

    /**
     * 调整分为多个case
     *
     * @param current
     */
    public void adjust_insert(TreeNode current) {
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
         * case3：current的叔叔节点uncle为红色，无论current位于parent的左右都满足该case，
         * 将parent和uncle设置为黑色，grandpa设置为红色，然后在从grandPa节点开始重新调整
         */
        if (uncleNode(current) != null && uncleNode(current).getColor() == Color.RED) {
            current.getParent().setColor(Color.BLACK);
            uncleNode(current).setColor(Color.BLACK);
            grandPaNode(current).setColor(Color.RED);
            adjust_insert(grandPaNode(current));
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
    /*********************删除操作*****************************/
    /**
     * 删除任何一个节点，无论其子节点个数为0,1,2，都可以转换为删除一个最多有一个非叶子节点的node
     *
     * @param value
     */
    public void delete(final int value) {
        TreeNode current = getNode(value);
        if (current == null) {
            System.out.println("不存在该value，无法删除");
            return;
        }
        /**
         * curren的左右节点都不为null，则找到current左子树中最大的节点
         */
        if (current.getLeft() != null && current.getRight() != null) {
            TreeNode temp = current.getLeft();
            //左子树最右的节点即为值最大的节点
            while (temp.getRight() != null) {
                temp = temp.getRight();
            }
            current.setValue(temp.getValue());
            current = temp;
        }
        adjust_delete(current);
    }

    /**
     * 删除节点时的调整操作，current最多有一个非叶子节点,如果current没有非叶子节点，可以将任意一个null节点作为其子节点，
     * 因此此处的调整只需要针对current有一个非叶子节点情况
     *
     * @param current
     */
    public void adjust_delete(TreeNode current) {
        if (current == null) {
            return;
        }
        /**
         * case1:如果current为red节点，直接用current的非叶子节点顶替current即可
         * current为red节点，则必有parent节点，但是current的child可能为null
         */
        if (current.getColor() == Color.RED) {
            TreeNode parent = current.getParent();
            TreeNode child = null;
            if (current.getLeft() != null) {
                child = current.getLeft();
            } else {
                child = current.getRight();
            }
            if (parent.getLeft() == current) {
                parent.setLeft(child);
            } else {
                parent.setRight(child);
            }
            if (child != null) {
                child.setParent(parent);
            }
            setNull(current);
            return;
        }
        /**
         * case2:如果current为black，current的child非空且为red，只需要将current的parent指向child，将child设置为black
         * current的parent可能为null
         */
        if (current.getColor() == Color.BLACK && getChild(current) != null && getChild(current).getColor() == Color.RED) {
            TreeNode parent = current.getParent();
            TreeNode child = getChild(current);
            child.setParent(parent);
            child.setColor(Color.BLACK);
            if (parent == null) {
                root = child;
            } else {
                if (parent.getLeft() == current) {
                    parent.setLeft(child);
                } else {
                    parent.setRight(child);
                }
            }
            setNull(current);
            return;
        }
    }


    /*********************左旋转、右旋转***********************/
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

    /**
     * 将给定节点left，right，parent设置为null
     *
     * @param current
     */
    public void setNull(TreeNode current) {
        if (current != null) {
            current.setParent(null);
            current.setRight(null);
            current.setLeft(null);
        }
    }

}
