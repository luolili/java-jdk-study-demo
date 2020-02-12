package base.alg;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class InorderTraveral {

    public List<Integer> inorderTraveral(TreeNode root) {
        List<Integer> tree = new ArrayList<>();
        buildTree(root, tree);
        return tree;
    }

    public void buildTreeV2(TreeNode root, List<Integer> res) {
        Stack<TreeNode> tree = new Stack<>();
        TreeNode cur = root;
        while (cur != null || !tree.isEmpty()) {
            while (cur != null) {
                tree.push(cur);
                cur = cur.left;
            }
            cur = tree.pop();
            res.add(cur.value);
            cur = cur.right;

        }
    }

    public void buildTree(TreeNode root, List<Integer> res) {
        if (root != null) {
            if (root.left != null) {
                buildTree(root.left, res);
            }
            res.add(root.value);
            if (root.right != null) {
                buildTree(root.right, res);
            }
        }
    }
}
