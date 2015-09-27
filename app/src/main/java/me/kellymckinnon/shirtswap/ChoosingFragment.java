package me.kellymckinnon.shirtswap;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import me.kellymckinnon.shirtswap.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChoosingFragment extends Fragment implements me.kellymckinnon.shirtswap.UserDataSource.UserDataCallbacks, CardStackContainer.SwipeCallbacks {

    private static final String TAG = "ChoosingFragment";

    private CardStackContainer mCardStack;
    private List<me.kellymckinnon.shirtswap.User> mUsers;
    private CardAdapter mCardAdapter;

    public ChoosingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        mCardStack = (CardStackContainer) v.findViewById(R.id.card_stack);

        UserDataSource.getUnseenUsers(this);

        mUsers = new ArrayList<>();
        mCardAdapter = new CardAdapter(getActivity(), mUsers);
        mCardStack.setCardAdapter(mCardAdapter);
        mCardStack.setSwipeCallbacks(this);

        ImageButton yesButton = (ImageButton) v.findViewById(R.id.yes_button);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardStack.swipeRight();
            }
        });

        ImageButton noButton = (ImageButton) v.findViewById(R.id.no_button);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardStack.swipeLeft();
            }
        });

        return v;
    }

    @Override
    public void onUsersFetched(List<User> users) {
        mUsers.addAll(users);
        mCardAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSwipedRight(User user) {
        ActionDataSource.saveUserLiked(user.getId());
    }

    @Override
    public void onSwipedLeft(User user) {
        ActionDataSource.saveUserSkipped(user.getId());
    }
}
