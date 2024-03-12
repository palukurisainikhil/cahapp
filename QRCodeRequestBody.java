package com.example.scan2cash.model;

public class QRCodeRequestBody {
    private String token;
    private String mobileNumber;
    private String fundingAccount;
    private String amount;
    private String ATMKioskId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFundingAccount() {
        return fundingAccount;
    }

    public void setFundingAccount(String fundingAccount) {
        this.fundingAccount = fundingAccount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getATMKioskId() {
        return ATMKioskId;
    }

    public void setATMKioskId(String ATMKioskId) {
        this.ATMKioskId = ATMKioskId;
    }

}
