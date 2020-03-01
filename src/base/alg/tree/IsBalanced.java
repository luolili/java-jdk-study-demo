package base.alg.tree;

import base.alg.TreeNode;

/**
 * 基于树的高度
 */
public class IsBalanced {

    boolean balanced = true;

    public boolean isBalanced(TreeNode root) {
        height(root);
        return balanced;
    }

    public int height(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int maxLeftDepth = height(root.left);
        int maxRightDepth = height(root.right);
        if (Math.abs(maxLeftDepth - maxRightDepth) > 1) {
            balanced = false;
        }
        return Math.max(maxLeftDepth, maxRightDepth) + 1;
    }
}
