package com.guce.listnode;

import com.guce.module.ListNode;

/**
 * @Author chengen.gce
 * @DATE 2021/3/17 10:38 下午
 *
 * https://leetcode-cn.com/problems/liang-ge-lian-biao-de-di-yi-ge-gong-gong-jie-dian-lcof/
 */
public class GetIntersectionNode {

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode A = headA ,B = headB;
        while (A != B) {
            A = A != null ? A = A.next : headB;
            B = B != null ? B =B.next : headA ;
        }
        return A;
    }
}
