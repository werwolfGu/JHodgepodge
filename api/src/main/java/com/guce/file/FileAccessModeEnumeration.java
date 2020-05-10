package com.guce.file;

/**
 * @Author chengen.gce
 * @DATE 2020/5/10 3:53 下午
 */
public enum FileAccessModeEnumeration {
    READ("r"), READ_WRITE("rw"), READ_WRITE_SYNCHRONISE("rws");

    private String mode;

    private FileAccessModeEnumeration(String mode)
    {
        this.mode = mode;
    }

    /**
     * @return the mode
     */
    public String getMode()
    {
        return mode;
    }
}
