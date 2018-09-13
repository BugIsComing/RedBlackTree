package com.lc.redblacktree;

import java.util.List;
import java.util.Scanner;

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
        tree.insert(8);
        tree.insert(17);
        tree.insert(1);
        tree.insert(11);
        tree.insert(15);
        tree.insert(25);
        tree.insert(6);
        tree.insert(22);
        tree.insert(27);
        System.out.println(tree.getHeight());
        List<Integer> data = tree.inOrder();
        System.out.println(data.toString());
        tree.BFSPrint();
        Scanner in = new Scanner(System.in);
        int va;
        do {
            System.out.println("\n************************************");
            va = in.nextInt();
            tree.delete(va);
            tree.BFSPrint();
        } while (true);
    }
}
