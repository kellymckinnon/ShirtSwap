package me.kellymckinnon.shirtswap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MatchesFragment extends Fragment implements ActionDataSource.ActionDataCallbacks, me.kellymckinnon.shirtswap.UserDataSource.UserDataCallbacks, AdapterView.OnItemClickListener {

    private static final String TAG = "MatchesFragment";
    private MatchesAdapter mAdapter;
    private ArrayList<me.kellymckinnon.shirtswap.User> mUsers;

    public MatchesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionDataSource.getMatches(this);

        View v = inflater.inflate(R.layout.fragment_matches, container, false);

        ListView matchesListView = (ListView) v.findViewById(R.id.matches_list);
        mUsers = new ArrayList<>();
        mAdapter = new MatchesAdapter(mUsers);
        matchesListView.setAdapter(mAdapter);
        matchesListView.setOnItemClickListener(this);
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
        mUsers.clear();
        mUsers.addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = mUsers.get(position);
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(ChatActivity.USER_EXTRA, user);
        startActivity(intent);
    }

    public class MatchesAdapter extends ArrayAdapter<me.kellymckinnon.shirtswap.User> {

        MatchesAdapter(List<User> users) {
            super(MatchesFragment.this.getActivity(), android.R.layout.simple_list_item_1, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView v = (TextView) super.getView(position, convertView, parent);
            v.setText(getItem(position).getFirstName());
            return v;
        }
    }
}
