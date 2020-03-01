package base.alg;

import java.util.LinkedList;

public class MaxDepth {
    /**
     * 迭代
     *
     * @param root
     * @return
     */
    public int maxDepthV2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        LinkedList<TreeNode> q = new LinkedList<>();
        q.add(root);
        int level = 0;
        while (!q.isEmpty()) {
            int nodeNum = q.size();
            for (int i = 0; i < nodeNum; i++) {
                TreeNode node = q.poll();
                if (node.left != null) {
                    q.add(node.left);
                }
                if (node.right != null) {
                    q.add(node.right);
                }
            }
            level++;
        }
        return level;
    }

    /**
     * 递归
     *
     * @param root
     * @return
     */
    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int maxLeftDepth = maxDepth(root.left);
        int maxRightDepth = maxDepth(root.right);
        return Math.max(maxLeftDepth, maxRightDepth) + 1;
    }

    //一行代码
    public int maxDepthV3(TreeNode root) {
        return (root == null) ? 0 : Math.max(maxDepthV3(root.left), maxDepthV3(root.right)) + 1;
    }

}
