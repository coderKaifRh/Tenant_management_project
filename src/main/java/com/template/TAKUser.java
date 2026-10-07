package com.template;
public abstract class TAKUser {
    private int userId;
    private String username;
    private String phone;

    public TAKUser(int userId, String username) { this.userId = userId; this.username = username; }


    public void setPhone(String phone) throws TAKException {
        if (phone == null || !phone.startsWith("+8801") || phone.length() != 14) {
            throw new TAKException("Invalid Phone! Must be like +8801700000000");
        }
        this.phone = phone;
    }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }


    public abstract double calculatePayableAmount(double amount);
}