package com.guce.node;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author chengen.gu
 * @DATE 2020/2/9 2:59 下午
 */

public class ListNode<V> {

    @Getter
    @Setter
    V val;
    @Setter
    @Getter
    ListNode next;
    public ListNode(V val){
        this.val = val;
    }



}
