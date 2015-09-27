package me.kellymckinnon.shirtswap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends Fragment implements ActionDataSource.ActionDataCallbacks, me.kellymckinnon.shirtswap.UserDataSource.UserDataCallbacks, View.OnClickListener {

    private static final String TAG = "MatchesFragment";
    private MatchAdapter mAdapter;
    private List<Match> mMatches;
    private RecyclerView rv;

    @Override
    public void onClick(View v) {
        int itemPosition = rv.getChildPosition(v);
        Match item = mMatches.get(itemPosition);
        User user = item.otherUser;
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(ChatActivity.USER_EXTRA, user);
        startActivity(intent);
    }

    public MatchesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionDataSource.getMatches(this);

        View v = inflater.inflate(R.layout.fragment_matches, container, false);

        rv = (RecyclerView) v.findViewById(R.id.matches_list);
        rv.setHasFixedSize(true);

        mMatches = new ArrayList<Match>();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        mAdapter = new MatchAdapter(mMatches, getActivity(), this);
        rv.setAdapter(mAdapter);

        return v;
    }


    @Override
    public void onFetchedMatches(List<String> matchIds) {
        matchIds.add("dAPWnehkUo");
        matchIds.add("nRKiKv462m");
        matchIds.add("p0qSFSKRvF");
        UserDataSource.getUsersIn(matchIds, this);
    }

    @Override
    public void onUsersFetched(List<User> users) {
        mMatches.clear();
        for (User user : users) {
            Match match = new Match("http://dummyimage.com/600x400/000/fff", "http://dummyimage.com/600x400/123/fff", user); // TODO USE IMAGES
            mMatches.add(match);
        }

        mAdapter.notifyDataSetChanged();
    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        User user = mMatches.get(position).otherUser;
//        Intent intent = new Intent(getActivity(), ChatActivity.class);
//        intent.putExtra(ChatActivity.USER_EXTRA, user);
//        startActivity(intent);
//    }
}
