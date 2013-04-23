package com.gs.bean;

/**
 * Created with IntelliJ IDEA.
 * User: kensen
 * Date: 4/17/13
 * Time: 1:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class NumberVoiceBean {
    private int iNumber = 0;
    private String onesPlace = "";
    private String tensPlace = "";

    public int getNumber() {
        return iNumber;
    }

    public void setNumber(int iNumber) {
        this.iNumber = iNumber;
    }

    public String getOnesPlace() {
        return onesPlace;
    }

    public void setOnesPlace(String onesPlace) {
        this.onesPlace = onesPlace;
    }

    public String getTensPlace() {
        return tensPlace;
    }

    public void setTensPlace(String tensPlace) {
        this.tensPlace = tensPlace;
    }

    @Override
    public String toString() {
        return "NumberVoiceBean{" +
                "iNumber=" + iNumber +
                ", onesPlace='" + onesPlace + '\'' +
                ", tensPlace='" + tensPlace + '\'' +
                '}';
    }
}
