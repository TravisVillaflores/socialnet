package org.example.socialnet;

import java.util.HashSet;
import java.util.Set;

public class UserProfile {
    private String name;
    private String status;
    private String favoriteQuote;
    private String imagePath;
    private Set<String> friends;

    public UserProfile(String name) {
        this.name = name;
        this.status = "";
        this.favoriteQuote = "";
        this.imagePath = "";
        this.friends = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setFavoriteQuote(String favoriteQuote) {
        this.favoriteQuote = favoriteQuote;
    }

    public String getFavoriteQuote() {
        return favoriteQuote;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Set<String> getFriends() {
        return friends;
    }

    public void addFriend(String friendName) {
        friends.add(friendName);
    }

    public void removeFriend(String friendName) {
        friends.remove(friendName);
    }
}
