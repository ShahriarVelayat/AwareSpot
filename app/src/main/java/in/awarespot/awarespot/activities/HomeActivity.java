package in.awarespot.awarespot.activities;

import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
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
import in.awarespot.awarespot.Models.AwareSpotModel;
import in.awarespot.awarespot.R;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = " HomeActivity" ;
    private GoogleMap mMap;

    private double latPoint;
    private double lngPoint;
    private Geocoder mGeocoder;

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

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // TODO Auto-generated method stub
                mMap.addMarker(new MarkerOptions().position(point));
                latPoint = point.latitude;
                lngPoint = point.longitude;
                dialogeShow();
            }
        });
    }

    public void dialogeShow(){
        final AwareSpotModel awareSpotModel = new AwareSpotModel();
        new MaterialDialog.Builder(this)
                .title("Enter title")
                .content("one can give any title to there aware spot")
                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)
                .input("Title", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                        awareSpotModel.setLat(latPoint);
                        awareSpotModel.setLng(lngPoint);
                        awareSpotModel.setTitleOfAwareSpot(input.toString());

                        new MaterialDialog.Builder(HomeActivity.this)
                                .title("Enter Content")
                                .content("Brerif of the Awarespot")
                                .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE)
                                .input("Title", "", new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                        // Do something
                                        awareSpotModel.setContentOfAwareSpot(input.toString());
                                        try{
                                            awareSpotModel.setCity(getCityNameByCoordinates(latPoint,lngPoint));
                                            FirebaseDataBaseCheck.getDatabase().getReference().child(FirebaseInfo.NodeUsing).child(FirebaseInfo.AWARE_SPOT).push().setValue(awareSpotModel);
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


        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        MarkerOptions spot = new MarkerOptions().position(sydney).title("Aware-spot" );
        spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.spot));
        mMap.addMarker(spot);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(25.153900, 78.013092))
                .zoom(4)
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
                AwareSpotModel mAwareSpotModel = dataSnapshot.getValue(AwareSpotModel.class);

                try {
                        markOnMap(dataSnapshot.getKey(),mAwareSpotModel.getTitleOfAwareSpot(),mAwareSpotModel.getContentOfAwareSpot(),mAwareSpotModel.getCity(),mAwareSpotModel.getLat(),mAwareSpotModel.getLng());
                }catch (Exception e){
                        Log.d(TAG,""+e);
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
    public void markOnMap(String id , String Title,String Despcription,String city,double lat, double lng){
        LatLng sydney = new LatLng(lat, lng);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        MarkerOptions spot = new MarkerOptions().position(sydney).title("Aware-spot" + city );
        spot.icon(BitmapDescriptorFactory.fromResource(R.drawable.spot));
        spot.snippet("" + Despcription);
        mMap.addMarker(spot);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(25.153900,78.013092))
                .zoom(4)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

}
