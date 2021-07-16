package com.example.grabtutor.Model;

public class PaymentHistory {

    String refNumber, amount, status, paymentType, date;

    public PaymentHistory() {
    }

    public PaymentHistory(String refNumber, String amount, String status, String paymentType, String date) {
        this.refNumber = refNumber;
        this.amount = amount;
        this.status = status;
        this.paymentType = paymentType;
        this.date = date;
    }


    public String getRefNumber() {
        return refNumber;
    }

    public String getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getDate() {
        return date;
    }
}
