package model;

public class Voucher {

    private String voucherName;
    private String voucherType;

    public String getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public Voucher[] getVouchers() {
        return new Voucher[0];
    }
}
