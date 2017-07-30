package in.awarespot.awarespot.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import in.awarespot.awarespot.R;

public class SeeStories extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 234;
    //a Uri object to store file path
    private Uri selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_stories);
    }
}
