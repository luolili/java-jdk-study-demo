package base.alg;

import java.util.LinkedList;
import java.util.Queue;

public class IsSymmetric {

    public boolean isSymmetric(TreeNode root) {
        return isMirror(root, root);

    }

    public boolean isMirrorV2(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        ((LinkedList<TreeNode>) q).add(root);
        ((LinkedList<TreeNode>) q).add(root);
        while (!q.isEmpty()) {
            TreeNode t1 = q.poll();
            TreeNode t2 = q.poll();
            if (t1 == null && t2 == null) {
                continue;
            }
            if (t1 == null || t2 == null) {
                return false;
            }
            if (t1.value != t2.value) {
                return false;
            }
            // 队列的连续2个元素是对称的/相等的/一样的
            ((LinkedList<TreeNode>) q).add(t1.left);
            ((LinkedList<TreeNode>) q).add(t2.right);
            ((LinkedList<TreeNode>) q).add(t1.right);
            ((LinkedList<TreeNode>) q).add(t2.left);
        }
        return true;
    }


    public boolean isMirror(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        return (p.value == q.value && isMirror(p.right, q.left) && isMirror(p.left, q.right));
    }


}
