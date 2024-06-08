package com.guce.event;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author chengen.gce
 * @DATE 2022/5/11 12:32
 */
@Slf4j
public class ObjectEvent<D> {
    private D data;

    public D getData() {
        return data;
    }
    public D copyOfData() {
        // Copy the data out here. In this case we have a single reference
        // object, so the pass by
        // reference is sufficient. But if we were reusing a byte array,
        // then we
        // would need to copy
        // the actual contents.
        return data;
    }

    public void setData(D data) {
        this.data = data;
    }
}
