package com.cncoding.teazer.ui.fragment.activity;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ProgressRequestBody;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdate;
import com.cncoding.teazer.model.profile.profileupdate.ProfileUpdateRequest;
import com.cncoding.teazer.utilities.SharedPrefs;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.blurry.Blurry;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.SharedPrefs.finishVideoUploadSession;
import static com.cncoding.teazer.utilities.SharedPrefs.getBlurredProfilePic;
import static com.cncoding.teazer.utilities.SharedPrefs.isBlurredProfilePicSaved;

public class EditProfile extends AppCompatActivity implements IPickResult, EasyPermissions.PermissionCallbacks, ProgressRequestBody.UploadCallbacks {

    Context context;
    ImageView bgImage;
    CircularAppCompatImageView profile_image;
    AppBarLayout appBarLayout;
    EditText _username;
    EditText _firstname;
    EditText _lastName;
    EditText _email;
    EditText _mobileNumber;
    EditText _bio;
    String username;
    String firstname;
    String lastname;
    int countrycode;
    String emailId;
    Long mobilenumber;
    int gender;
    String detail;
    @BindView(R.id.male)
    CircularAppCompatImageView male;
    @BindView(R.id.female)
    CircularAppCompatImageView female;

    @BindView(R.id.maletxt)
    ProximaNovaRegularCheckedTextView maletext;
    @BindView(R.id.femaletxt)
    ProximaNovaRegularCheckedTextView femaletxt;

    Button fab;
    ProgressBar simpleProgressBar;
    ScrollView layoutdetail;
    private static final int RC_REQUEST_STORAGE = 1001;
    private static final int LIMIT = 1;
    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final String TAG = "Edit Profile";
    boolean flag = false;
    private String userProfileThumbnail;
    private String userProfileUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusbar));
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        toolbar.setNavigationIcon(R.drawable.ic_previous);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                onBackPressed();

            }
        });
        context = EditProfile.this;
        bgImage = findViewById(R.id.profile_id2);
        simpleProgressBar = findViewById(R.id.simpleProgressBar);
        layoutdetail = findViewById(R.id.layoutdetail);
        _username = findViewById(R.id.username);
        _firstname = findViewById(R.id.firstname);
        profile_image = findViewById(R.id.profile_id);
        _email = findViewById(R.id.email);
        _mobileNumber = findViewById(R.id.mobile);
        _bio = findViewById(R.id.bio);
        fab = findViewById(R.id.fab);
        layoutdetail.setVisibility(View.GONE);
        Intent intent = getIntent();
        username = intent.getStringExtra("UserName");
        firstname = intent.getStringExtra("FirstName");
        lastname = intent.getStringExtra("LastName");
        String mobileno = intent.getStringExtra("MobileNumber");
        userProfileThumbnail = intent.getStringExtra("ProfileThumb");
        userProfileUrl = intent.getStringExtra("ProfileMedia");
        if (mobileno != null) {
            mobilenumber = Long.parseLong(mobileno);
        }
        gender = Integer.parseInt(intent.getStringExtra("Gender"));
        emailId = intent.getStringExtra("EmailId");
        countrycode = Integer.parseInt(intent.getStringExtra("CountryCode"));
        detail = intent.getStringExtra("Detail");
        _username.setText(username);
        _firstname.setText(firstname);
        _bio.setText(detail);
        _email.setText(emailId);
        _mobileNumber.setText(String.valueOf(mobilenumber));
        initProfileImage();

        if (gender == 1) {
            male.setBackgroundResource(R.drawable.ic_male_sel);
            female.setBackgroundResource(R.drawable.ic_female_white);
            maletext.setTextColor(Color.parseColor("#2196F3"));
            femaletxt.setTextColor(Color.parseColor("#333333"));
        } else {
            female.setBackgroundResource(R.drawable.ic_female_sel);
            male.setBackgroundResource(R.drawable.ic_male_white);
            femaletxt.setTextColor(Color.parseColor("#2196F3"));
            maletext.setTextColor(Color.parseColor("#333333"));
        }

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                male.setBackgroundResource(R.drawable.ic_male_sel);
                female.setBackgroundResource(R.drawable.ic_female_white);
                maletext.setTextColor(Color.parseColor("#2196F3"));
                femaletxt.setTextColor(Color.parseColor("#333333"));
                gender = 1;
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                female.setBackgroundResource(R.drawable.ic_female_sel);
                male.setBackgroundResource(R.drawable.ic_male_white);
                femaletxt.setTextColor(Color.parseColor("#2196F3"));
                maletext.setTextColor(Color.parseColor("#333333"));
                gender = 2;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  validate();
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PickImageDialog.build(new PickSetup()).show(EditProfile.this);
            }
        });
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {

            SharedPreferences preferences = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("MYIMAGES", r.getPath());
            editor.apply();
            try {
                File profileImage = new File(r.getPath());
                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), profileImage);
//                String fileExt = profileImage.getAbsolutePath().substring(profileImage.getAbsolutePath().lastIndexOf("."));
                MultipartBody.Part body = MultipartBody.Part.createFormData("media", "profile_image.jpg", reqFile);

                saveDataToDatabase(body);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "oops something went wrong, please try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_REQUEST_STORAGE)
    public void initProfileImage() {
        String perm = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_REQUEST_STORAGE, perm);
        } else {

            if (userProfileThumbnail != null) {
                Picasso.with(context)
                        .load(Uri.parse(userProfileThumbnail))
                        .into(profile_image);
                profileBlur(userProfileUrl);
            }

        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }


    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
    }



    @Override
    protected void onResume() {
        super.onResume();
//        final String pic = "https://aff.bstatic.com/images/hotel/840x460/304/30427979.jpg";
        layoutdetail.setVisibility(View.VISIBLE);
        simpleProgressBar.setVisibility(View.GONE);

    }


    public void ProfileUpdate(ProfileUpdateRequest profileUpdateRequest) {
        simpleProgressBar.setVisibility(View.VISIBLE);
        layoutdetail.setVisibility(View.GONE);

        ApiCallingService.User.updateUserProfiles(profileUpdateRequest, getApplicationContext()).enqueue(new Callback<ProfileUpdate>() {
            @Override
            public void onResponse(Call<ProfileUpdate> call, Response<ProfileUpdate> response) {


                if (response.code() == 200) {

                    try {
                        if (response.body().getStatus()) {
                            Toast.makeText(getApplicationContext(), "Your Profile has been updated", Toast.LENGTH_LONG).show();
                            simpleProgressBar.setVisibility(View.GONE);
                            layoutdetail.setVisibility(View.VISIBLE);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Your Profile has not been updated yet", Toast.LENGTH_LONG).show();
                            simpleProgressBar.setVisibility(View.GONE);
                            layoutdetail.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Something went wrong Please try again", Toast.LENGTH_LONG).show();
                        simpleProgressBar.setVisibility(View.GONE);
                        layoutdetail.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please check your data is correct", Toast.LENGTH_LONG).show();
                    simpleProgressBar.setVisibility(View.GONE);
                    layoutdetail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ProfileUpdate> call, Throwable t) {
                 t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network Issue Please check once again ", Toast.LENGTH_LONG).show();
                simpleProgressBar.setVisibility(View.GONE);
                layoutdetail.setVisibility(View.VISIBLE);
            }
        });
    }


    public void saveDataToDatabase(MultipartBody.Part body) {
        ApiCallingService.User.updateUserProfileMedia(body, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {

                    SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
                    final String imagePath = prfs.getString("MYIMAGES", "");
                    Glide.with(EditProfile.this)
                            .load(imagePath)
                            .into(profile_image);

                    SharedPrefs.deleteSavedBlurredProfilePic(getApplicationContext());
                    new SetBackgroundBlurPic(EditProfile.this, true).execute(imagePath);
                    simpleProgressBar.setVisibility(View.GONE);
                    layoutdetail.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                simpleProgressBar.setVisibility(View.GONE);
                layoutdetail.setVisibility(View.VISIBLE);
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onProgressUpdate(int percentage) {
    }

    @Override
    public void onUploadError(Throwable throwable) {
    }

    @Override
    public void onUploadFinish() {
        finishVideoUploadSession(this);
    }


    @AfterPermissionGranted(RC_REQUEST_STORAGE)
    public void profileBlur(String pic) {

        String perm = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        if (!EasyPermissions.hasPermissions(this, perm)) {
            EasyPermissions.requestPermissions(this, getString(R.string.rationale_storage),
                    RC_REQUEST_STORAGE, perm);
        } else {
            simpleProgressBar.setVisibility(View.VISIBLE);
            layoutdetail.setVisibility(View.GONE);

            new SetBackgroundBlurPic(this, false).execute(pic);

            simpleProgressBar.setVisibility(View.GONE);
            layoutdetail.setVisibility(View.VISIBLE);
        }

    }

    public static class SetBackgroundBlurPic extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<EditProfile> reference;
        private final boolean isLocalImage;
        private boolean isSavedLocally;

        SetBackgroundBlurPic(EditProfile context, boolean isLocalImage) {
            reference = new WeakReference<>(context);
            this.isLocalImage = isLocalImage;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            if (isBlurredProfilePicSaved(reference.get().getApplicationContext())) {
                File file = new File(getBlurredProfilePic(reference.get().getApplicationContext()));
                isSavedLocally = true;
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            } else {
                try {
                    Bitmap bitmap;
                    if (isLocalImage) {
                        bitmap = BitmapFactory.decodeFile(strings[0], new BitmapFactory.Options());
                        isSavedLocally = false;
                        return bitmap;
                    } else {
                        final URL url = new URL(strings[0]);
                        bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        isSavedLocally = false;
                        return bitmap;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        private String saveBlurredPic(Bitmap bitmapImage){
            ContextWrapper cw = new ContextWrapper(reference.get().getApplicationContext());
            File directory = cw.getDir("dp", Context.MODE_PRIVATE);
            File imagePath = new File(directory,"profile.png");

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(imagePath);

                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imagePath.getAbsolutePath();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            reference.get().simpleProgressBar.setVisibility(View.GONE);
            reference.get().layoutdetail.setVisibility(View.VISIBLE);

            try {
                if (isSavedLocally) {
                    reference.get().bgImage.setImageBitmap(bitmap);
                } else {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    while (width > 500 && height > 500) {
                        width = width / 2;
                        height = height / 2;
                    }
                    Blurry.with(reference.get())
                            .async(new Blurry.ImageComposer.ImageComposerListener() {
                                @Override
                                public void onImageReady(BitmapDrawable drawable) {
                                    SharedPrefs.saveBlurredProfilePic(reference.get().context, saveBlurredPic(drawable.getBitmap()));
                                    reference.get().bgImage.setImageDrawable(drawable);
                                }
                            })
                            .from(Bitmap.createScaledBitmap(bitmap, width, height, false))
                            .into(reference.get().bgImage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            reference.get().simpleProgressBar.setVisibility(View.GONE);
            reference.get().layoutdetail.setVisibility(View.VISIBLE);
        }
    }

    public void validate() {
        boolean valid = true;
        String usernames = _username.getText().toString();
        String firstname = _firstname.getText().toString();
        String lastnames = "";
        Integer countrycodes = countrycode;
        String  mobilenumber = _mobileNumber.getText().toString();
        String emailid = _email.getText().toString();
        String details = _bio.getText().toString();

        if (usernames.isEmpty()) {
            _username.setError("enter username");
            _username.requestFocus();
            valid = false;
        }
        else {
            _username.setError(null);
        }

        if (firstname.isEmpty()) {
            _firstname.setError("enter your name");
            _firstname.requestFocus();
            valid = false;
        } else {
            _firstname.setError(null);
        }

        if (emailid.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailid).matches() || emailid.isEmpty()) {
            _email.setError("enter a email address");
            _email.requestFocus();
            valid = false;
        } else {
            _email.setError(null);
        }

        if (mobilenumber.isEmpty()|| mobilenumber.length() < 10 || mobilenumber.length() > 10) {
            _mobileNumber.setError("enter valid 10 digit mobile number");
            _mobileNumber.requestFocus();
            valid = false;
        }
        else {
            _mobileNumber.setError(null);
        }

        if (details.isEmpty()) {
            _bio.setError("Pan number is required");
            _bio.requestFocus();
            valid = false;
        } else {
            _bio.setError(null);
        }

        if(valid) {
            ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest(firstname, lastnames, usernames, emailid, Long.parseLong(mobilenumber), countrycodes, gender, details);
            ProfileUpdate(profileUpdateRequest);
        }
    }
}






