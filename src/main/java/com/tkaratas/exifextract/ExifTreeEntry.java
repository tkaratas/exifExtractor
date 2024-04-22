package com.tkaratas.exifextract;

public class ExifTreeEntry {
    private String tag = null;
    private String val = null;

    public ExifTreeEntry(){}
    public ExifTreeEntry(String tag, String val){
        this.val = val;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getVal() {
        return val;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
