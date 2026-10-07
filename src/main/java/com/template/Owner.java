package com.template;
public class Owner extends TAKUser {
    public Owner(int userId, String username) { super(userId, username); }

    public double calculatePayableAmount(double amount) { return amount * 0.05; }
}