package com.tokeninc.sardis.application_template.Entity;

public enum DeviceMode {
    VUK507(0),
    POS(1),
    GIB(2),
    ECR(3),
    NONE(4);

    public final int mode;
    DeviceMode(int mode) {
        this.mode = mode;
    }
}
