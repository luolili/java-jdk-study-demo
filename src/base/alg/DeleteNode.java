package base.alg;

/**
 * 输入: head = [4,5,1,9], val = 5
 * 输出: [4,1,9]
 * 解释: 给定你链表中值为 5 的第二个节点，那么在调用了你的函数之后，该链表应变为 4 -> 1 -> 9.
 */
public class DeleteNode {

    public ListNode deleteNode(ListNode head, int val) {
        if (head == null) {
            return null;
        }
        if (head.val == val) {
            return head.next;
        }
        ListNode curNode = head;
        while (curNode != null && curNode.next != null) {
            if (curNode.next.val == val) {
                curNode.next = curNode.next.next;
            }
            // 遍历剩下的节点
            curNode = curNode.next;
        }
        return head;
    }
}
