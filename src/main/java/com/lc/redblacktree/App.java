package com.lc.redblacktree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 测试类
 *
 * @author lc
 */
public class App {
    public static void main(String[] args) {
        RedBlackTree tree = new RedBlackTree();
        /**
         * 插入13 6 4 15 3 89 1 0
         */
        tree.insert(13);
        tree.insert(6);
        tree.insert(4);
        tree.insert(15);
        tree.insert(3);
        tree.insert(89);
        tree.insert(1);
        tree.insert(0);
        tree.insert(13);
        tree.insert(8);
        System.out.println(tree.getHeight());
        List<Integer> data = tree.inOrder();
        System.out.println(data.toString());

        /**
         * 广度优先搜索
         */
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(tree.getRoot());
        queue.offer(null);
        while(queue.size()>1){
            TreeNode temp = queue.poll();
            if(temp == null){
                System.out.print("\n");
                queue.offer(null);
                continue;
            }
            if(temp.getLeft() != null){
                queue.offer(temp.getLeft());
            }
            if(temp.getRight() != null){
                queue.offer(temp.getRight());
            }
            System.out.print(temp.toString());
        }
    }
}
