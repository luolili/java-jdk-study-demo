package base.alg;

import java.util.ArrayDeque;
import java.util.Deque;

public class ReversePrintLikedList {

    public int[] reversePrint(ListNode head) {
        if (head == null) {
            return null;
        }

        Deque<Integer> q = new ArrayDeque<>();
        ListNode curNode = head;
        while (curNode != null) {
            q.addLast(curNode.val);
            curNode = curNode.next;
        }

        int size = q.size();
        int[] res = new int[size];
        for (int i = 0; i < size; i++) {
            res[i] = q.removeLast();
        }
        return res;
    }

    /**
     * 反转链表
     *
     * @param head
     * @return
     */
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(-1);
        while (head != null) {
            ListNode tmp = head.next;
            //
            head.next = dummy.next;
            dummy.next = head;
            head = tmp;
        }
        return dummy.next;
    }
}
