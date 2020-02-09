package base.alg;

public class TreeTraversal {
    public static void main(String[] args) {
        //先生成 完全二叉树
        TreeNode[] treeNodes = new TreeNode[10];
        for (int i = 0; i < 10; i++) {
            treeNodes[i] = new TreeNode(i);

        }
        for (int i = 0; i < 10; i++) {
            if (i * 2 + 1 < 10) {
                treeNodes[i].left = new TreeNode(i * 2 + 1);
            }
            if (i * 2 + 2 < 10) {
                treeNodes[i].right = new TreeNode(i * 2 + 2);
            }

        }
    }

    public static void preOrderRe(TreeNode tn) {
        System.out.println(tn.value);

        TreeNode leftTree
                = tn.left;
        if (leftTree != null) {
            preOrderRe(leftTree);
        }

        TreeNode rightTree
                = tn.right;
        if (rightTree != null) {
            preOrderRe(rightTree);
        }
    }

    public static void inOrderRe(TreeNode root) {
        TreeNode leftTree = root.left;
        TreeNode rightTree = root.right;

        if (leftTree != null) {
            inOrderRe(leftTree);
        }
        System.out.println(root.value);
        if (rightTree != null) {
            inOrderRe(rightTree);
            new Object();
        }
    }
}
