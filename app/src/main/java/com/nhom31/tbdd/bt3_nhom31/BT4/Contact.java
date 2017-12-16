package com.nhom31.tbdd.bt3_nhom31.BT4;

import java.util.Arrays;
import java.util.List;


public class Contact {
    private String displayName;
    private Phone[] listPhone;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Phone[] getListPhone() {
        return listPhone;
    }

    public void setListPhone(Phone[] listPhone) {
        this.listPhone = listPhone;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "displayName='" + displayName + '\'' +
                ", listPhone=" + Arrays.toString(listPhone) +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Contact)) {
            return false;
        }

        Contact that = (Contact) obj;
        if(this.getDisplayName().equals(that.getDisplayName())){
            if(this.getListPhone().length == that.getListPhone().length){
                if(checkSame(this, that)){
                    return true;
                }
            }
        }

        return super.equals(obj);
    }

    private boolean checkSame(Contact a, Contact b) {
        int counter = 0;
        List<Phone> arr1 = Arrays.asList(a.getListPhone());
        List<Phone> arr2 = Arrays.asList(b.getListPhone());
        for(Phone phone : arr2){
            if(arr1.contains(phone)){
                counter++;
            }
        }

        if(counter == arr1.size()){
            return true;
        }
        return false;
    }
}
