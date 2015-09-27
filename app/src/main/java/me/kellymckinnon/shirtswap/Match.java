package me.kellymckinnon.shirtswap;

/**
 * Object representing a match, i.e. someone whose shirt(s) the user liked that
 * also liked their shirt(s).
 */
public class Match {
    String yourShirtUrl;
    String theirShirtUrl;
    User otherUser;

    Match(String yourShirtUrl, String theirShirtUrl, User otherUser) {
        this.yourShirtUrl = yourShirtUrl;
        this.theirShirtUrl = theirShirtUrl;
        this.otherUser = otherUser;
    }
}
