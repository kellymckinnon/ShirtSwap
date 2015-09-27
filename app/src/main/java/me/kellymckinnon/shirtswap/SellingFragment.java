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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;

import com.parse.ParseObject;
import com.parse.ParseFile;
import com.parse.SaveCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellingFragment extends Fragment {

    EditText shirtDesc;
    EditText shirtTag;
    Spinner shirtSize;

    public SellingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_selling, container, false);

        Spinner shirtSizes = (Spinner) v.findViewById(R.id.spShirtSize);
        String[] sizes = new String[]{"S", "M", "L", "XL", "XXL"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sizes);
        shirtSizes.setAdapter(adapter);

        shirtDesc = (EditText) v.findViewById(R.id.tbShirtDesc);
        shirtTag = (EditText) v.findViewById(R.id.tbShirtTag);
        shirtSize = (Spinner) v.findViewById(R.id.spShirtSize);

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
        final ParseObject newShirt = new ParseObject("Shirt");

        final User currentUser = UserDataSource.getCurrentUser();
        String username = (currentUser != null) ? currentUser.getFirstName() : "wtf";
        String userID = (currentUser != null) ? currentUser.getId() : "wtf";

//// This is the logic for saving the image uploaded or taken
//// will probably be moved
//        byte[] imgData = "Temp String".getBytes();
//        ParseFile imgFile = new ParseFile("shirtImage.jpeg", imgData);
//        imgFile.saveInBackground();

//// After the async save happens then we
//        newShirt.put("image", imgFile);

        newShirt.put("user", username);
        newShirt.put("userID", userID);
        newShirt.put("tag", shirtTag.getText().toString());
        newShirt.put("description", shirtDesc.getText().toString());
        newShirt.put("size", shirtSize.getSelectedItem().toString());
        newShirt.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException parseException) {
                currentUser.addShirt(newShirt.getObjectId());
            }
        });

        // we need to somehow show that it was successful
        // maybe segue into prev screen if this is a popup/dialog
    }


}
