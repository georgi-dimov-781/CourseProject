package com.example.courseproject;

public class Contact {
    private int contactId;
    private String name;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String phone1;
    private String phone2;
    private String phone3;

    @SuppressWarnings("All")
    public Contact(int contactId, String name, String address, String city, String province, String postalCode, String phone1, String phone2, String phone3) {
        this.contactId = contactId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
    }

    @SuppressWarnings("All")
    public int getContactId() {
        return contactId;
    }

    @SuppressWarnings("All")
    public String getName() {
        return name;
    }

    @SuppressWarnings("All")
    public String getAddress() {
        return address;
    }

    @SuppressWarnings("All")
    public String getCity() {
        return city;
    }

    @SuppressWarnings("All")
    public String getProvince() {
        return province;
    }

    @SuppressWarnings("All")
    public String getPostalCode() {
        return postalCode;
    }

    @SuppressWarnings("All")
    public String getPhone1() {
        return phone1;
    }

    @SuppressWarnings("All")
    public String getPhone2() {
        return phone2;
    }

    @SuppressWarnings("All")
    public String getPhone3() {
        return phone3;
    }
}
