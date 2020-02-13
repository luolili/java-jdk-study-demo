package base.alg;

import java.util.*;

/**
 * 广度/宽度优先搜索
 */
public class BreadthFirstSearch {
    public List<List<Integer>> breadthFirstSearchV2(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<List<Integer>>();
        if (root == null) {
            return levels;
        }
        Queue<TreeNode> q = new LinkedList<>();
        ((LinkedList<TreeNode>) q).add(root);
        int level = 0;
        while (!q.isEmpty()) {
            //在加入每一层（从第1层开始）的节点之前，先准备好容量
            levels.add(new ArrayList<>());
            //每一层节点的数量：第0层 1个
            int nodeNumEveryLevel = q.size();
            //不要用 迭代器，否则跑错
            for (int i = 0; i < nodeNumEveryLevel; i++) {
                TreeNode node = q.poll();
                levels.get(level).add(node.value);
                if (node.left != null) {
                    q.add(node.left);
                }
                if (node.right != null) {
                    q.add(node.right);
                }
            }
            level++;

        }
        return levels;
    }

    public List<List<Integer>> breadthFirstSearch(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<List<Integer>>();
        if (root == null) {
            return levels;
        }
        helper(root, 0, levels);
        return levels;
    }


    /**
     * 递归
     *
     * @param root
     * @param level
     * @param levels
     */
    private void helper(TreeNode root, int level, List<List<Integer>> levels) {
        if (level == levels.size()) {
            levels.add(new ArrayList<>());
        }
        //level 初始为0
        levels.get(level).add(root.value);
        // 从左到右 遍历子节点
        if (root.left != null) {
            helper(root.left, level + 1, levels);
        }
        if (root.right != null) {
            helper(root.right, level + 1, levels);
        }
    }
}
