package me.kellymckinnon.shirtswap;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.gc.materialdesign.views.ButtonRectangle;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kelly on 9/27/15.
 */
public class UploadDialogFragment extends android.support.v4.app.DialogFragment {

    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;

    EditText shirtDesc;
    EditText shirtTag;
    Spinner shirtSize;
    ImageView image;
    ButtonRectangle uploadButton;
    Fragment parentFragment;
    private byte[] imageBytes;

    @Override
    // ANDROID GODS I AM SORRY FOR MY SINS #SupportLibraryBugsAreFun
    public void setTargetFragment(Fragment fragment, int requestCode) {
        super.setTargetFragment(fragment, requestCode);
        parentFragment = fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_upload_dialog, container, false);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        shirtSize = (Spinner) v.findViewById(R.id.shirt_size);
        String[] sizes = new String[]{"SMALL", "MEDIUM", "LARGE", "EXTRA LARGE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, sizes);
        shirtSize.setAdapter(adapter);

        shirtDesc = (EditText) v.findViewById(R.id.shirt_description);
        shirtTag = (EditText) v.findViewById(R.id.shirt_tag);
        image = (ImageView) v.findViewById(R.id.upload_image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });

        uploadButton = (ButtonRectangle) v.findViewById(R.id.upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadShirt();
            }
        });

        return v;
    }

    private void selectImage(View v) {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0: // Take photo
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        parentFragment.startActivityForResult(intent, REQUEST_CAMERA);
                        return;
                    case 1: // Gallery
                        // TODO: FIX THIS, ITS BROKEN
                        return;
//                        Intent intent2 = new Intent();
//                        intent2.setType("image/*");
//                        intent2.setAction(Intent.ACTION_GET_CONTENT);
//                        intent2.putExtra("return-data", true);
//                        startActivityForResult(Intent.createChooser(intent2,
//                                "Complete action using"), SELECT_FILE);

//                            break;
                    case 2: // Cancel
                        dialog.dismiss();
                        break;
                }
            }
        });
        builder.show();
    }

    private void uploadShirt() {
        final ParseObject newShirt = new ParseObject("Shirt");

        User currentUser = UserDataSource.getCurrentUser();
        final String username = (currentUser != null) ? currentUser.getFirstName() : "Jane";
        final String userID = (currentUser != null) ? currentUser.getId() : "Doe";

        final ParseFile imgFile = new ParseFile("shirtImage.jpeg", imageBytes);
        imgFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                newShirt.put("image", imgFile);
                newShirt.put("user", username);
                newShirt.put("userID", userID);
                newShirt.put("tag", shirtTag.getText().toString());
                newShirt.put("description", shirtDesc.getText().toString());
                newShirt.put("size", shirtSize.getSelectedItem().toString());
                newShirt.saveInBackground();

                ArrayList<Shirt> oldShirts = UserDataSource.getCurrentUserShirts();
                Shirt shirt = new Shirt();
                shirt.description = shirtDesc.getText().toString();
                shirt.size = shirtSize.getSelectedItem().toString();
                shirt.url = new Uri.Builder().path(imgFile.getUrl()).build();

                oldShirts.add(shirt);
                UserDataSource.setCurrentUserShirts(oldShirts);
                ((SellingFragment) parentFragment).notifyDataChanged();

                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                imageBytes = bytes.toByteArray();
                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image.setImageBitmap(thumbnail);
            } else if (requestCode == SELECT_FILE) {
                Bitmap bitmap = data.getExtras().getParcelable("data");
                image.setImageBitmap(bitmap);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                imageBytes = stream.toByteArray();
            }
            uploadButton.setEnabled(true);
        }
    }

    private Bitmap getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        // Convert file path into bitmap image using below line.
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        return bitmap;
    }

}
