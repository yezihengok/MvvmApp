package com.example.mvvmapp.bean;


public class User  {


    private int id;

    private String email;

    private String icon;

    private String password;

    private int type;

    private String username;

    private int coinCount;

    private int rank;


    public int getId() {
        return id;
    }

    public void setId( int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User( int id, String email, String icon, String password, int type, String username, int coinCount, int rank) {
        this.id = id;
        this.email = email;
        this.icon = icon;
        this.password = password;
        this.type = type;
        this.username = username;
        this.coinCount = coinCount;
        this.rank = rank;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", icon='" + icon + '\'' +
                ", password='" + password + '\'' +
                ", type=" + type +
                ", username='" + username + '\'' +
                ", coinCount=" + coinCount +
                ", rank=" + rank +
                '}';
    }
}
