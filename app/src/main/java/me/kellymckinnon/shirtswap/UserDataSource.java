package me.kellymckinnon.shirtswap;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class UserDataSource {

    private static User sCurrentUser;
    private static ArrayList<Shirt> sCurrentUserShirts;

    private static final String COLUMN_FIRST_NAME = "firstName";
    private static final String COLUMN_PICTURE_URL = "pictureURL";
    private static final String COLUMN_FACEBOOK_ID = "facebookId";
    private static final String COLUMN_ID = "objectId";
    private static final String COLUMN_SHIRTS = "shirts";
    private static final String COLUMN_LIKED_SHIRTS = "likedShirts";
    private static final String COLUMN_MATCHES = "matches";

    public static User getCurrentUser() {
        if(sCurrentUser == null && ParseUser.getCurrentUser() != null) {
            sCurrentUser = parseUserToUser(ParseUser.getCurrentUser());
        }

        return sCurrentUser;
    }

    public static void setCurrentUserShirts(ArrayList<Shirt> shirts) {
        sCurrentUserShirts = shirts;
    }

    public static ArrayList<Shirt> getCurrentUserShirts() {
        return sCurrentUserShirts;
    }

    public static void getUnseenUsers(final UserDataCallbacks callbacks) {
        ParseQuery<ParseObject> seenUsersQuery = new ParseQuery<ParseObject>(ActionDataSource.TABLE_NAME);
        seenUsersQuery.whereEqualTo(ActionDataSource.COLUMN_BY_USER, ParseUser.getCurrentUser().getObjectId());
        seenUsersQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(e == null) {
                    List<String> ids = new ArrayList<String>();
                    for(ParseObject parseObject : list) {
                        ids.add(parseObject.getString(ActionDataSource.COLUMN_TO_USER));
                    }
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereNotEqualTo("objectId", getCurrentUser().getId());
                    query.whereNotContainedIn("objectId", ids);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> parseUsers, ParseException e) {
                            formatCallback(parseUsers, e, callbacks);
                        }
                    });
                }
            }
        });
    }

    private static void formatCallback(List<ParseUser> parseUsers, ParseException e, UserDataCallbacks callbacks) {
        if(e == null) {
            List<User> users = new ArrayList<>();
            for(ParseUser parseUser : parseUsers) {
                User user = parseUserToUser(parseUser);
                users.add(user);
            }
            if(callbacks != null) {
                callbacks.onUsersFetched(users);
            }
        }
    }

    public static void getUsersIn(List<String> ids, final UserDataCallbacks callbacks) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn(COLUMN_ID, ids);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                formatCallback(list, e, callbacks);
            }
        });
    }

    private static User parseUserToUser(ParseUser parseUser) {
        User user = new User();
        user.setFirstName(parseUser.getString(COLUMN_FIRST_NAME));
        user.setPictureURL(parseUser.getString(COLUMN_PICTURE_URL));
        user.setId(parseUser.getObjectId());
        user.setFacebookId(parseUser.getString(COLUMN_FACEBOOK_ID));
        user.setShirts(parseUser.getList(COLUMN_SHIRTS));
        user.setLikedShirts(parseUser.getList(COLUMN_LIKED_SHIRTS));
        user.setMatches(parseUser.getList(COLUMN_MATCHES));
        return user;
    }

    public interface UserDataCallbacks {
        void onUsersFetched(List<User> users);
    }
}
