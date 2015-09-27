package me.kellymckinnon.shirtswap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public MatchesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onClick(View v) {
        int itemPosition = rv.getChildPosition(v);
        Match item = mMatches.get(itemPosition);
        User user = item.otherUser;
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(ChatActivity.USER_EXTRA, user);
        startActivity(intent);
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
        matchIds.add("p0qSFSKRvF");
        matchIds.add("nRKiKv462m");
        matchIds.add("p0qSFSKRvF");
        UserDataSource.getUsersIn(matchIds, this);
    }

    @Override
    public void onUsersFetched(List<User> users) {
        mMatches.clear();

        Match match = new Match("id1", "http://3.bp.blogspot.com/-zfZdPShvmg8/UzmBivGcysI/AAAAAAAAJhM/lcjSD0fmMG0/s1600/WP_001911.jpg", "id2", "http://i.ebayimg.com/00/s/NzAwWDcwMA==/z/P6sAAOSwHnFVuBKS/$_35.JPG", users.get(0)); // TODO USE IMAGES
        Match match1 = new Match("id3", "http://files.parsetfss.com/de628155-4892-411c-957c-022880994bef/tfss-43c5f9a6-7a9b-4a98-b8f2-baf0fcccb92e-shirtImage.jpeg", "id4", "http://files.parsetfss.com/de628155-4892-411c-957c-022880994bef/tfss-a56beb00-1098-4672-88f3-32a187e7dfed-shirtImage.jpeg", users.get(1)); // TODO USE IMAGES
        mMatches.add(match);
        mMatches.add(match1);

        mAdapter.notifyDataSetChanged();
    }
}
