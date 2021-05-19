package com.guce;

/**
 * @Author chengen.gce
 * @DATE 2021/3/9 1:57 下午
 */
public class InstrumentTest {

    public static void main(String[] args) {
        String code = String.format(
                // decorate to TTL wrapper,
                // and then set AutoWrapper attachment/Tag
                "    $%d = %s.get($%1$d, false, true);"
                        + "\n    com.alibaba.ttl.spi.TtlAttachmentsDelegate.setAutoWrapperAttachment($%1$d);",
                1,  "guce");

        System.out.println(code);
    }
}
