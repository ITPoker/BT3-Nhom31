package com.nhom31.tbdd.bt3_nhom31.BT4;

public class Phone {
    private String phoneNumber;
    private String typePhone;
    private String nameLabel;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTypePhone() {
        return typePhone;
    }

    public String getNameLabel() {
        return nameLabel;
    }

    public void setNameLabel(String nameLabel) {
        this.nameLabel = nameLabel;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTypePhone(String typePhone) {
        this.typePhone = typePhone;
    }

    @Override
    public String toString() {
        return "ListPhone{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", typePhone='" + typePhone + '\'' +
                ", nameLabel='" + nameLabel + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Phone)) {
            return false;
        }

        Phone that = (Phone) obj;
        if((this.getTypePhone().equals(that.getTypePhone())) && (this.getPhoneNumber().equals(that.getPhoneNumber()))){
            return true;
        }

        return super.equals(obj);
    }
}
