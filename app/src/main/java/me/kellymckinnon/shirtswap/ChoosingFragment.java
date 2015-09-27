package me.kellymckinnon.shirtswap;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.util.Log;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import com.parse.GetCallback;
import com.parse.FindCallback;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that shows the main view with the swipeable cards.
 */
public class ChoosingFragment extends Fragment implements UserDataSource.UserDataCallbacks {

    private static final String TAG = "ChoosingFragment";

    private CardAdapter mCardAdapter;

    public ChoosingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        UserDataSource.getUnseenUsers(this);

        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) v.findViewById(
                R.id.swipe_view);

        List<Shirt> shirts = new ArrayList<>();
        mCardAdapter = new CardAdapter(getActivity(), shirts);

        flingContainer.setAdapter(mCardAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                //This is the simplest way to delete an object from the adapter
                mCardAdapter.removeFrontItem();
            }

            @Override
            public void onLeftCardExit(Object o) {
                // Do something on the left; you still have access to the original object
                flingContainer.requestLayout();
//                ActionDataSource.saveUserLiked(user.getId());
            }

            @Override
            public void onRightCardExit(Object o) {
                flingContainer.requestLayout();
//                ActionDataSource.saveUserSkipped(user.getId());
                if(o instanceof Shirt) {
                    final User currentUser = UserDataSource.getCurrentUser();
                    final Shirt likedShirt = (Shirt)o;
                    currentUser.addLikedShirt(likedShirt.id);

                    // check for match
                    // if other user has liked one of my shirts then match
                    List<String> otherUserLikedShirts = likedShirt.user.getLikedShirts();
                    List<String> myShirts = currentUser.getShirts();

                    for(String shirtID : myShirts) {
                        final String shirtIDCopy = shirtID;
                        if(otherUserLikedShirts.contains(shirtID)) {
                            // there's a match bitches!!!
                            // get Shirt URL from parse
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Shirt");
                            query.getInBackground(shirtID, new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {
                                    if (e == null) {
                                        ParseFile postImage = object.getParseFile("image");
                                        final ParseObject yourMatch = new ParseObject("Match");

                                        yourMatch.put("yourShirtID", shirtIDCopy);
                                        yourMatch.put("yourShirtURL", Uri.parse(postImage.getUrl()));
                                        yourMatch.put("theirShirtID", likedShirt.id);
                                        yourMatch.put("theirShirtURL", likedShirt.url);
                                        yourMatch.put("otherUserID", likedShirt.user.getId());
                                        yourMatch.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException parseException) {
                                                currentUser.addMatch(yourMatch.getObjectId());
                                            }
                                        });

                                        final ParseObject theirMatch = new ParseObject("Match");
                                        theirMatch.put("yourShirtID", likedShirt.id);
                                        theirMatch.put("yourShirtURL", likedShirt.url);
                                        theirMatch.put("theirShirtID", shirtIDCopy);
                                        theirMatch.put("theirShirtURL", Uri.parse(postImage.getUrl()));
                                        theirMatch.put("otherUserID", currentUser.getId());
                                        theirMatch.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException parseException) {
                                                likedShirt.user.addMatch(theirMatch.getObjectId());
                                            }
                                        });
                                    } else {
                                        // something went wrong
                                    }
                                }
                            });
                        }
                    }
                } else {
                  // Something went wrong...
                }

            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                // TODO: Don't fetch all at once; use this
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        ImageButton yesButton = (ImageButton) v.findViewById(R.id.yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectRight();
            }
        });

        ImageButton noButton = (ImageButton) v.findViewById(R.id.no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectLeft();
            }
        });

        return v;
    }

    @Override
    public void onUsersFetched(List<User> users) {
        final List<Shirt> otherUsersShirts = new ArrayList<>();

        final ArrayList<Shirt> currentUserShirts = new ArrayList<>();
        final User currentUser = UserDataSource.getCurrentUser();

        ParseQuery<ParseObject> query = new ParseQuery<>("Shirt");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> markers, ParseException e) {
                if (e == null) {
                    for (ParseObject po : markers) {
                        User shirtOwner = new User();
                        shirtOwner.setFirstName(po.getString("user"));
                        shirtOwner.setId(po.getString("userID"));

                        ParseFile postImage = po.getParseFile("image");

                        Shirt shirt = new Shirt();
                        shirt.id = po.getObjectId();
                        shirt.user = shirtOwner;
                        shirt.description = po.getString("description");
                        shirt.size = po.getString("size");
                        shirt.tag = po.getString("tag");
                        shirt.url = Uri.parse(postImage.getUrl());

                        if (shirt.user.getId().equals(currentUser.getId())) {
                            currentUserShirts.add(shirt);
                        } else {
                            otherUsersShirts.add(shirt);
                        }
                    }
                    mCardAdapter.addCards(otherUsersShirts);
                    mCardAdapter.notifyDataSetChanged();
                    UserDataSource.setCurrentUserShirts(currentUserShirts);
                } else {
                    Log.e("LOL","done fucked up");
                }
            }
        });
    }
}