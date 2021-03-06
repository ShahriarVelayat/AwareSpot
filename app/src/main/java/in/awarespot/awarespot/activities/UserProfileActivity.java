package in.awarespot.awarespot.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import in.awarespot.awarespot.FirebaseInfo.FirebaseDataBaseCheck;
import in.awarespot.awarespot.FirebaseInfo.FirebaseInfo;
import in.awarespot.awarespot.Models.NotifyModel;
import in.awarespot.awarespot.R;
import in.awarespot.awarespot.Models.UserModel;

import in.awarespot.awarespot.adapter.CitiesAdapter;
import io.paperdb.Paper;

public class UserProfileActivity extends AppCompatActivity {


    private final static String TAG = "UserProfileActivity";
    private EditText nameEditText;
    private Button doneButton, addNewButton;
    private String uid;
    private ListView cityListView;
    private CitiesAdapter adapter;
    private Geocoder mGeocoder;

    private List<String> citiesKnow = new ArrayList<>();
    private List<String> citiesList = new ArrayList<>();

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ScreenDesign();
        Paper.init(this);
        initUiElements();
        initUiListners();
        getuser();
    }

    public void initUiElements() {
        Paper.init(this);
        nameEditText = (EditText) findViewById(R.id.input_name);
        doneButton = (Button) findViewById(R.id.btn_done);
        addNewButton = (Button) findViewById(R.id.btn_addnew);
        cityListView = (ListView) findViewById(R.id.listViewAndroid) ;

        mGeocoder = new Geocoder(UserProfileActivity.this, Locale.getDefault());
        adapter = new CitiesAdapter(this, citiesKnow);
        cityListView.setAdapter(adapter);
    }

    public void initUiListners() {

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setuserModel();
            }
        });
    }

    public void getuser() {

        FirebaseDataBaseCheck.getDatabase().getReference().child(FirebaseInfo.NodeUsing).child(FirebaseInfo.Notify).child("common").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try{
                    NotifyModel notifyModel = dataSnapshot.getValue(NotifyModel.class);
                    adapter.add("Date : "+ getDate(notifyModel.getTimeStamp())+"\n\nTitle : "+notifyModel.getTitle() + "\n\n Message " + notifyModel.getBody());
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                 Log.d("Notify",e+"");
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        String date = DateFormat.format("dd/MM/yyyy hh:mm a", cal).toString();
        return date;
    }


    public void setuserModel() {
        UserModel userModel = new UserModel();
        userModel.setUid(uid);
        userModel.setNameOfUser(nameEditText.getText().toString());
        userModel.setNotifyToken(FirebaseInstanceId.getInstance().getToken());
        userModel.setCitiesKnown(citiesKnow);

        FirebaseDataBaseCheck.getDatabase().getReference().child(FirebaseInfo.NodeUsing).child(FirebaseInfo.Users).child(uid).setValue(userModel);
        Paper.book().write(FirebaseInfo.Users,userModel);

        for(String city : citiesKnow){
            FirebaseDataBaseCheck.getDatabase().getReference().child(FirebaseInfo.NodeUsing).child(FirebaseInfo.Cities).child(city).child(uid).setValue(userModel.getNotifyToken());
        }
        Intent intent = new Intent(UserProfileActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(UserProfileActivity.this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(UserProfileActivity.this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(UserProfileActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }


    public void ScreenDesign(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            boolean shouldChangeStatusBarTintToDark = true;

            if (shouldChangeStatusBarTintToDark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                // We want to change tint color to white again.
                // You can also record the flags in advance so that you can turn UI back completely if
                // you have set other flags before, such as translucent or full screen.
                decor.setSystemUiVisibility(0);

            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check that the result was from the autocomplete widget.
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                // Get the user's selected place from the Intent.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place Selected: " + place.getLatLng());
                Double lat = place.getLatLng().latitude;
                Double lng = place.getLatLng().longitude;

                try {

                    Log.i(TAG, "City Selected: " + getCityNameByCoordinates(lat, lng));
                    Log.i(TAG, "City Selected: " + getCityNameByCoordinates(lat, lng));

                    citiesKnow.add(getCityNameByCoordinates(lat, lng));
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    Log.i(TAG, "City Selected: " + e);
                }

                // Format the place's details and display them in the TextView.
//                nameEditText.setText(formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()));


                // Display attributions if required.
                CharSequence attributions = place.getAttributions();
                if (!TextUtils.isEmpty(attributions)) {

                } else {

                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e(TAG, "Error: Status = " + status.toString());
            } else if (resultCode == RESULT_CANCELED) {
                // Indicates that the activity closed before a selection was made. For example if
                // the user pressed the back button.
            }
        }
    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }

    private String getCityNameByCoordinates(double lat, double lon) throws IOException {

        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {

            return addresses.get(0).getLocality();

        }
        return null;
    }



}

