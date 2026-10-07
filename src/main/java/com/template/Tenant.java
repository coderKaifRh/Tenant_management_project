package com.template;
public class Tenant extends TAKUser {
    public Tenant(int userId, String username) { super(userId, username); }

    public double calculatePayableAmount(double amount) { return amount + amount; }
}