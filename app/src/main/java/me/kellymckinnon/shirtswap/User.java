package me.kellymckinnon.shirtswap;

import com.parse.ParseUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String mFirstName;
    private String mPictureURL;
    private String mId;
    private String mFacebookId;
    private List<String> mShirts = new ArrayList<String>();
    private List<String> mLikedShirts = new ArrayList<String>();
    private List<String> mMatches = new ArrayList<String>();

    public String getLargePictureURL() {
        return "https://graph.facebook.com/v2.3/" + mFacebookId + "/picture?type=large";
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getPictureURL() {
        return mPictureURL;
    }

    public void setPictureURL(String pictureURL) {
        mPictureURL = pictureURL;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getFacebookId() {
        return mFacebookId;
    }

    public void setFacebookId(String facebookId) {
        mFacebookId = facebookId;
    }

    public List<String> getShirts() {
        return mShirts;
    }

    public void setShirts(List<Object> shirtsList) {
        if (shirtsList != null)
            for (Object shirt : shirtsList)
                if (shirt instanceof String)
                    mShirts.add((String) shirt);
    }

    public void addShirt(String shirtID) {
        mShirts.add(shirtID);
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("shirts", mShirts);
        currentUser.saveInBackground();
    }

    public List<String> getLikedShirts() {
        return mLikedShirts;
    }

    public void setLikedShirts(List<Object> likedShirtsList) {
        if (likedShirtsList != null)
            for (Object shirt : likedShirtsList)
                if (shirt instanceof String)
                    mLikedShirts.add((String) shirt);
    }

    public void addLikedShirt(String likedShirtID) {
        mLikedShirts.add(likedShirtID);
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("likedShirts", mLikedShirts);
        currentUser.saveInBackground();
    }

    public List<String> getMatches() {
        return mMatches;
    }

    public void setMatches(List<Object> matchesList) {
        if (matchesList != null)
            for (Object match : matchesList)
                if (match instanceof String)
                    mMatches.add((String) match);
    }

    public void addMatch(String matchID) {
        mMatches.add(matchID);
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("matches", mMatches);
        currentUser.saveInBackground();
    }
}
