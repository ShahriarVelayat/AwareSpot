package in.awarespot.awarespot.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import in.awarespot.awarespot.FirebaseInfo.FirebaseDataBaseCheck;
import in.awarespot.awarespot.FirebaseInfo.FirebaseInfo;
import in.awarespot.awarespot.Models.UtilityModel;
import in.awarespot.awarespot.R;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = " HomeActivity" ;
    private GoogleMap mMap;

    private double latPoint;
    private double lngPoint;
    private Geocoder mGeocoder;
    private TextView mProfileText;
    LatLng latlng;
    int i=0;
    private TextView search;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ScreenDesign();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGeocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
    }


    public void initUiElement(){
        mProfileText = (TextView) findViewById(R.id.profile);
        search = (TextView) findViewById(R.id.search);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {



//                mMap.addMarker(new MarkerOptions().position(point));
//                latPoint = point.latitude;
//                lngPoint = point.longitude;
                //dialogeShow();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAutocompleteActivity();
            }
        });
        mProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,UserProfileActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
            }
        });
    }

    public void dialogeShow(){
        final UtilityModel utilityModel = new UtilityModel();
        new MaterialDialog.Builder(this)
                .title("Enter title")
                .content("one can give any title to there aware spot")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)
                .input("Title", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        utilityModel.setLat(latPoint);
                        utilityModel.setLng(lngPoint);
                        utilityModel.setTitleOfUtility(input.toString());

                        new MaterialDialog.Builder(HomeActivity.this)
                                .title("Enter Content")
                                .content("Breif of the Awarespot")
                                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)
                                .input("Title", "", new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                        // Do something
                                        utilityModel.setContentOfUtility(input.toString());
                                        try{
                                            utilityModel.setCity(getCityNameByCoordinates(latPoint,lngPoint));
                                            FirebaseDataBaseCheck.getDatabase().getReference().child(FirebaseInfo.NodeUsing).child(FirebaseInfo.AWARE_SPOT).push().setValue(utilityModel);
                                        }catch(Exception e){
                                            Log.d(TAG,""+e);
                                        }

                                    }
                                }).show();

                    }
                }).show();
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        initUiElement();

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        getAwareSpots();

//        LatLng sydney = new LatLng(-34, 151);
////        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        MarkerOptions spot = new MarkerOptions().position(sydney).title("Aware-spot" );
//        spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.spot));
//        mMap.addMarker(spot);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(22.3038945, 70.80215989999999))
                .zoom(10)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private String getCityNameByCoordinates(double lat, double lon) throws IOException {

        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {
            return addresses.get(0).getLocality();

        }
        return null;
    }

    public void getAwareSpots(){
        mMap.clear();

        FirebaseDataBaseCheck.getDatabase().getReference().child(FirebaseInfo.NodeUsing).child(FirebaseInfo.AWARE_SPOT).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                i++;
                try {
                    UtilityModel mUtilityModel = dataSnapshot.getValue(UtilityModel.class);
                    mUtilityModel.setUid(dataSnapshot.getKey());
                        markOnMap(mUtilityModel);
                }catch (Exception e){
                        Log.d("log_error", i+ " "+e);
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

    public void markOnMap(UtilityModel mUtilityModel){
        LatLng sydney = new LatLng(mUtilityModel.lat, mUtilityModel.lng);

        MarkerOptions spot = new MarkerOptions().position(sydney).title(mUtilityModel.getTitleOfUtility());
        if(mUtilityModel.getTag1().toLowerCase().equals("hospital")){
            spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.hospital));
        }else if(mUtilityModel.getTag1().toLowerCase().equals("governmentoffice")){
            spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.government));
        }else if(mUtilityModel.getTag1().toLowerCase().equals("transportation")){
            if(mUtilityModel.getTag2().toLowerCase().equals("airport")){
                spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.airplane));
            }else if(mUtilityModel.getTag2().toLowerCase().equals("busstation")){
                spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus));
            }else if(mUtilityModel.getTag2().toLowerCase().equals("railwaystation")){
                spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.subway));
            }
        }else if(mUtilityModel.getTag1().toLowerCase().equals("meadow")){
            spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.parks));
        }
        else if(mUtilityModel.getTag1().toLowerCase().equals("bathroom")){
            spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.toilet));
        }
        else if(mUtilityModel.getTag1().toLowerCase().equals("exhibition")){
            spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.circus));
        }
        else {
            spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.spot));
        }
        spot.snippet("" + mUtilityModel.contentOfUtility);
        mMap.addMarker(spot);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mUtilityModel.lat,mUtilityModel.lng))
                .zoom(13)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void searchCity(double latPoint, double lngPoint){
        latlng = new LatLng(latPoint,lngPoint);
        mMap.addMarker(new MarkerOptions().position(latlng));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latPoint,lngPoint))
                .zoom(17)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void openAutocompleteActivity() {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(HomeActivity.this);
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(HomeActivity.this, message, Toast.LENGTH_SHORT).show();
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

                searchCity(place.getLatLng().latitude,place.getLatLng().longitude);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.profile : profile();
                break;
            case R.id.logout : ;
                break;
            default:
                Toast.makeText(this,"No actionFound",Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void profile(){
        Intent intent = new Intent( HomeActivity.this,UserProfileActivity.class);
        startActivity(intent);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }

    public void story(View view){
        Intent intent = new Intent( HomeActivity.this,storyActivity.class);
        startActivity(intent);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
    }

    public void underProcess(View view){
        Toast.makeText(HomeActivity.this,"Stories for nearby stores in Under Development",Toast.LENGTH_SHORT).show();
    }


}
