package me.kellymckinnon.shirtswap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        Button b = (Button) v.findViewById(R.id.upload);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadShirt();
            }
        });

        return v;
    }

    private void uploadShirt() {
        // DO SHIT HERE
    }


}
