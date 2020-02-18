package base.alg;

/**
 * 输入两个递增排序的链表，合并这两个链表并使新链表中的节点仍然是递增排序的。
 */
public class MergeTwoList {
    public ListNode mergeTwoListV2(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        if (l1.val < l2.val) {
            l1.next = mergeTwoListV2(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoListV2(l2.next, l1);
            return l2;
        }
    }

    public ListNode mergeTwoList(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        // 虚拟的首节点，他后面的节点才是合并后的结果
        ListNode dummyNode = new ListNode(0);
        //用新的节点 来指向虚拟的首节点
        ListNode newList = dummyNode;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                dummyNode.next = l1;
                // 遍历下个节点
                l1 = l1.next;
            } else {
                dummyNode.next = l2;
                l2 = l2.next;
            }
            dummyNode = dummyNode.next;
        }
        // 处理 l1 和l2的长度不一样的情况
        if (l1 != null) {
            dummyNode.next = l1;
        }
        if (l2 != null) {
            dummyNode.next = l2;
        }
        return newList.next;
    }
}
