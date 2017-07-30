package in.awarespot.awarespot.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.UUID;

import in.awarespot.awarespot.FirebaseInfo.FirebaseDataBaseCheck;
import in.awarespot.awarespot.FirebaseInfo.FirebaseInfo;
import in.awarespot.awarespot.Models.StoriesModel;
import in.awarespot.awarespot.R;

public class PostStoryActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static final int PICK_IMAGE_REQUEST = 234;
    //a Uri object to store file path
    private Uri selectedImage;
    EditText editText;
    ProgressDialog dialog;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_story);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        ScreenDesign();
        pickUP();

        editText = (EditText) findViewById(R.id.status);

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
    public void upload(View v){

        dialog = ProgressDialog.show(PostStoryActivity.this, "",
                "Uploading Image. Just few seconds", true);

        ImageView imageView = (ImageView) findViewById(R.id.imgView);
//        final TextView tv = (TextView) findViewById(R.id.txtview);

        // Creating a reference to the full path of the file. myfileRef now points
        // gs://fir-demo-d7354.appspot.com/myuploadedfile.jpg
        StorageReference myfileRef = mStorageRef.child(UUID.randomUUID().toString() + ".jpg");
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = myfileRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(PostStoryActivity.this, "TASK FAILED", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PostStoryActivity.this, "TASK SUCCEEDED", Toast.LENGTH_SHORT).show();
                Uri downloadUrl =taskSnapshot.getDownloadUrl();
                final String DOWNLOAD_URL = downloadUrl.getPath();

                Log.v("DOWNLOAD URL", DOWNLOAD_URL);

                if(dialog!=null){
                    dialog.dismiss();
                }
                new MaterialDialog.Builder(PostStoryActivity.this)
                        .title("Enter your Lin. Number")
                        .content("please enter your valid register number to validate")
                        .inputType(InputType.TYPE_CLASS_TEXT )
                        .input("", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                // Do something
                                StoriesModel mStoriesModel = new StoriesModel();
                                mStoriesModel.setShopId(input.toString());
                                mStoriesModel.setShopName("abcxyz");
                                mStoriesModel.setCaptionText(editText.getText().toString());
                                mStoriesModel.setImageUrl(DOWNLOAD_URL);
                                mStoriesModel.setTotalViews(0);
                                FirebaseDataBaseCheck.getDatabase().getReference().child(FirebaseInfo.NodeUsing).child(FirebaseInfo.Stories).push().setValue(mStoriesModel);

                            }
                        }).show();



//                tv.setText(DOWNLOAD_URL);
            }
        });
    }

    public void pickUP(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgView);

            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);

                //compress the bitmap
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG,50,stream);

                byte[] byteArray = stream.toByteArray();
                Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                imageView.setImageBitmap(compressedBitmap);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

        parcelFileDescriptor.close();
        return image;
    }


}
