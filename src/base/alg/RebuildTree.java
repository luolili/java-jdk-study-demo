package base.alg;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据二叉树的前序和中序遍历得到二叉树
 */
public class RebuildTree {

    private Map<Integer, Integer> map;
    private int[] preorder;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        int preLen = preorder.length;
        int inLen = inorder.length;
        // 数据不合法
        if (preLen != inLen) {
            return null;
        }
        this.preorder = preorder;
        map = new HashMap<>(inLen);
        for (int i = 0; i < inLen; i++) {

            map.put(inorder[i], i);
        }
        return buildTree(0, preLen - 1, 0, inLen - 1);

    }

    /**
     * @param preLeft  前序遍历的最左边节点的索引
     * @param preRight 前序遍历的最右边节点的索引
     * @param inLeft
     * @param inRight
     * @return
     */
    public TreeNode buildTree(int preLeft, int preRight, int inLeft, int inRight) {
        if (preLeft > preRight || inLeft > inRight) {
            return null;
        }
        int rootVal = preorder[preLeft];
        TreeNode root = new TreeNode(rootVal);
        int rootIndex = map.get(rootVal);
        root.left = buildTree(preLeft + 1, preLeft + rootIndex - inLeft, inLeft, rootIndex - 1);
        root.right = buildTree(preLeft + rootIndex - inLeft, preRight, rootIndex + 1, inRight);
        return root;

    }
}
