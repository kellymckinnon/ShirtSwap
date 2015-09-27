package me.kellymckinnon.shirtswap;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ChoosingFragment extends Fragment implements UserDataSource.UserDataCallbacks {

    private static final String TAG = "ChoosingFragment";

    private List<Shirt> mShirts;
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

        mShirts = new ArrayList<>();
        mCardAdapter = new CardAdapter(getActivity(), mShirts);

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
                Toast.makeText(getActivity(), "Left!", Toast.LENGTH_SHORT).show();
                flingContainer.requestLayout();
//                ActionDataSource.saveUserLiked(user.getId());
            }

            @Override
            public void onRightCardExit(Object o) {
                Toast.makeText(getActivity(), "Right!", Toast.LENGTH_SHORT).show();
                flingContainer.requestLayout();
//                ActionDataSource.saveUserSkipped(user.getId());
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {
                //FIXME: This is called twice for some reason (maybe only on genymotion?) when
                // the adapter is empty at the start
//                new LoadUsersTask().execute();
                // Actually load new shit in here from Parse
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
        int i = 0; // just for testing
        for (User user : users) {
            Shirt shirt = new Shirt();
            shirt.user = user;
            shirt.description = String.valueOf(i);
            mShirts.add(shirt);
            i++;
        }

        mCardAdapter.notifyDataSetChanged();
    }
}