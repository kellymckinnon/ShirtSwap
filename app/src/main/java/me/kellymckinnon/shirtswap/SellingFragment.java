package me.kellymckinnon.shirtswap;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellingFragment extends Fragment {

    public SellingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selling, container, false);

        User user = UserDataSource.getCurrentUser();
//        ArrayList<Shirt> shirts = user.getShirts();

        GridView gridview = (GridView) v.findViewById(R.id.gridview);
//        gridview.setAdapter(new Image(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(),"hello world", Toast.LENGTH_SHORT);
            }
        });

        Button b = (Button) v.findViewById(R.id.list_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadDialogFragment frag = new UploadDialogFragment();
                frag.show(getFragmentManager(), "frag");
                frag.setTargetFragment(SellingFragment.this, 100);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Fragment fragment = getFragmentManager().findFragmentByTag("frag");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
