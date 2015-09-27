package me.kellymckinnon.shirtswap;

/**
 * Object representing a match, i.e. someone whose shirt(s) the user liked that
 * also liked their shirt(s).
 */
public class Match {
    String yourShirtID;
    String yourShirtUrl;
    String theirShirtID;
    String theirShirtUrl;
    User otherUser;

    Match(String yourShirtID, String yourShirtUrl, String theirShirtID, String theirShirtUrl, User otherUser) {
        this.yourShirtID = yourShirtID;
        this.yourShirtUrl = yourShirtUrl;
        this.theirShirtID = theirShirtID;
        this.theirShirtUrl = theirShirtUrl;
        this.otherUser = otherUser;
    }
}
