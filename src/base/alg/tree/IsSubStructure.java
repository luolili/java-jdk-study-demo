package base.alg.tree;

import base.alg.TreeNode;

public class IsSubStructure {

    public boolean isSubStructure(TreeNode A, TreeNode B) {
        if (A == null || B == null) {
            return false;
        }
        if (A.value == B.value) {
            return isSub(A, B);
        }

        return isSubStructure(A.left, B) || isSubStructure(A.right, B);
    }

    public boolean isSub(TreeNode a, TreeNode b) {
        //b==null, b 树已经遍历玩
        if (b == null) {
            return true;
        }
        // a==null, a 树已经遍历玩
        if (a == null) {
            return false;
        }
        if (a.value == b.value) {
            return isSub(a.left, b.left) && isSub(a.right, b.right);
        }
        return false;
    }
}
