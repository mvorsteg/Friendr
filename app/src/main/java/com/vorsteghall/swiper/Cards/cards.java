package com.vorsteghall.swiper.Cards;

public class cards {
    private String userId;
    private String name;
    private String profileImageUrl;
    private String bio;

    public cards(String userId, String name, String profileImageUrl, String bio){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.bio = bio;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
