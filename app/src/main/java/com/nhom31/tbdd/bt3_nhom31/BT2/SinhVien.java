package com.nhom31.tbdd.bt3_nhom31.BT2;

public class SinhVien {
    String mssv;
    String toan;
    String ly;
    String hoa;

    public SinhVien(String mssv, String toan, String ly, String hoa) {
        this.mssv = mssv;
        this.toan = toan;
        this.ly = ly;
        this.hoa = hoa;
    }

    public String getMssv() {
        return mssv;
    }

    public void setMssv(String mssv) {
        this.mssv = mssv;
    }

    public String getToan() {
        return toan;
    }

    public void setToan(String toan) {
        this.toan = toan;
    }

    public String getLy() {
        return ly;
    }

    public void setLy(String ly) {
        this.ly = ly;
    }

    public String getHoa() {
        return hoa;
    }

    public void setHoa(String hoa) {
        this.hoa = hoa;
    }

    @Override
    public String toString() {
        return " Điểm thi:\nToán: " + this.getToan() + "\nLý: "
                + this.getLy() + "\nHóa: " + this.getHoa();
    }
}
