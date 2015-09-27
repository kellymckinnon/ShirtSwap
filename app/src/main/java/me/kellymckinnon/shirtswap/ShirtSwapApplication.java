package me.kellymckinnon.shirtswap;

import android.app.Application;

import com.firebase.client.Firebase;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Fisher on 7/21/15.
 */
public class ShirtSwapApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // parse stuff
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "AIgZniXU55L236JKqoatHBJfA1CR4b5lu89FEDgX", "A1ns2xTtz8v4uHtMEfWGSIAFWJ2b4hynQqbnkXnP");
        ParseFacebookUtils.initialize(this);

        // firebase stuff
        Firebase.setAndroidContext(this);
    }
}
