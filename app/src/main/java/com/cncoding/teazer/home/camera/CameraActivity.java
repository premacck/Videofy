/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cncoding.teazer.home.camera;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cncoding.teazer.BaseBottomBarActivity;
import com.cncoding.teazer.R;
import com.cncoding.teazer.home.camera.CameraFragment.OnCameraFragmentInteractionListener;
import com.cncoding.teazer.home.camera.UploadFragment.OnUploadFragmentInteractionListener;
import com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesAdapter;
import com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesAdapter.NearbyPlacesInteractionListener;
import com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesList;
import com.cncoding.teazer.home.camera.nearbyPlaces.NearbyPlacesList.OnNearbyPlacesListInteractionListener;
import com.cncoding.teazer.home.camera.nearbyPlaces.SelectedPlace;
import com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment;
import com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment.TagsAndCategoriesInteractionListener;
import com.cncoding.teazer.utilities.Pojos;
import com.cncoding.teazer.utilities.Pojos.Post.PostDetails;
import com.cncoding.teazer.utilities.Pojos.UploadParams;
import com.cncoding.teazer.videoTrim.TrimmerActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.R.anim.fade_in;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.cncoding.teazer.R.anim.float_up;
import static com.cncoding.teazer.R.anim.sink_down;
import static com.cncoding.teazer.home.camera.CameraFragment.ACTION_SHOW_GALLERY;
import static com.cncoding.teazer.home.camera.CameraFragment.ACTION_START_UPLOAD_FRAGMENT;
import static com.cncoding.teazer.home.camera.UploadFragment.REACTION_UPLOAD;
import static com.cncoding.teazer.home.camera.UploadFragment.TAG_CATEGORIES_FRAGMENT;
import static com.cncoding.teazer.home.camera.UploadFragment.TAG_NEARBY_PLACES;
import static com.cncoding.teazer.home.camera.UploadFragment.TAG_NULL_NEARBY_PLACES;
import static com.cncoding.teazer.home.camera.UploadFragment.TAG_TAGS_FRAGMENT;
import static com.cncoding.teazer.home.camera.UploadFragment.VIDEO_UPLOAD;
import static com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment.ACTION_CATEGORIES_FRAGMENT;
import static com.cncoding.teazer.tagsAndCategories.TagsAndCategoryFragment.ACTION_TAGS_FRAGMENT;
import static com.cncoding.teazer.utilities.ViewUtils.IS_REACTION;
import static com.cncoding.teazer.utilities.ViewUtils.POST_DETAILS;
import static com.cncoding.teazer.utilities.ViewUtils.performReactionUpload;
import static com.cncoding.teazer.utilities.ViewUtils.performVideoUpload;
import static com.cncoding.teazer.utilities.ViewUtils.updateMediaStoreDatabase;
import static com.cncoding.teazer.videoTrim.TrimmerActivity.VIDEO_TRIM_REQUEST_CODE;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.ANCHORED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.COLLAPSED;
import static com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState.EXPANDED;

public class CameraActivity extends AppCompatActivity
        implements OnCameraFragmentInteractionListener, OnUploadFragmentInteractionListener,
        TagsAndCategoriesInteractionListener, OnNearbyPlacesListInteractionListener,
        NearbyPlacesInteractionListener, OnConnectionFailedListener {

    private static final int REQUEST_CODE_STORAGE_PERMISSIONS = 101;
    private static final String TAG_UPLOAD_FRAGMENT = "uploadFragment";

    @BindView(R.id.sliding_layout) SlidingUpPanelLayout slidingUpPanelLayout;
    @BindView(R.id.video_gallery_container) RecyclerView recyclerView;
    @BindView(R.id.sliding_panel_arrow) AppCompatImageView slidingPanelArrow;
    @BindView(R.id.up_btn) AppCompatImageView upBtn;

    private FragmentManager fragmentManager;
    private GoogleApiClient googleApiClient;
    private SlidingUpPanelLayout.PanelSlideListener panelSlideListener;
    private ArrayList<Videos> videosList;

    private boolean isReaction = false;
    private PostDetails postDetails;
    private CameraFragment cameraFragment;
    private UploadFragment uploadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectAll()
//                .permitDiskWrites()
//                .penaltyLog()
////                .penaltyDialog()
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
////                .penaltyLog()
//                .build());
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        fragmentManager = getSupportFragmentManager();
        videosList = new ArrayList<>();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(new VideoGalleryAdapter(this, videosList));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isReaction = bundle.getBoolean(IS_REACTION);
            postDetails = bundle.getParcelable(POST_DETAILS);
        }

        slidingUpPanelLayout.setOverlayed(true);
        slidingUpPanelLayout.setScrollableView(recyclerView);

        cameraFragment = CameraFragment.newInstance(isReaction);
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, cameraFragment)
                    .commit();
        }

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSIONS);
        }
        else
            prepareVideoGallery();

        if (googleApiClient!= null && !googleApiClient.isConnected())
            googleApiClient.connect();
    }

    private void prepareVideoGallery() {
        if (panelSlideListener != null) {
            slidingUpPanelLayout.removePanelSlideListener(panelSlideListener);
            panelSlideListener = null;
        }
        panelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {
                if (newState == COLLAPSED) {
                    slidingPanelArrow.setImageResource(R.drawable.ic_up);
                }
                else if (newState == EXPANDED) {
                    slidingPanelArrow.setImageResource(R.drawable.ic_down);
                }
                else if (newState == ANCHORED) {
                    slidingPanelArrow.setImageResource(R.drawable.ic_drag_handle);
                }
            }
        };
        slidingUpPanelLayout.addPanelSlideListener(panelSlideListener);

        new GetVideoGalleryData(this).execute();
    }

    private void startVideoUploadFragment() {
        slidingUpPanelLayout.setPanelState(COLLAPSED);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(float_up, sink_down, fade_in, sink_down)
                        .replace(R.id.uploading_container, uploadFragment, TAG_UPLOAD_FRAGMENT)
                        .addToBackStack(TAG_UPLOAD_FRAGMENT)
                        .commitAllowingStateLoss();
                cameraFragment.closePreviewSession();
            }
        }, 300);
    }

    @Override
    public void onCameraInteraction(int action, UploadParams uploadParams) {
        switch (action) {
            case ACTION_START_UPLOAD_FRAGMENT:
//                SEND BROADCAST TO UPDATE THE VIDEO IN MEDIASTORE DATABASE.
                updateMediaStoreDatabase(this, uploadParams.getVideoPath());
                uploadFragment = UploadFragment.newInstance(uploadParams.getVideoPath(), isReaction, false);
                startVideoUploadFragment();
                break;
            case ACTION_SHOW_GALLERY:
                slidingUpPanelLayout.setPanelState(ANCHORED);
                break;
            default:
                break;
        }
    }

    public void onVideoGalleryAdapterInteraction(String videoPath) {
        if (new File(videoPath).exists()) {
                if (getVideoDuration(videoPath) < 60) {
                    uploadFragment = UploadFragment.newInstance(videoPath, isReaction, true);
                    startVideoUploadFragment();
                }
                else
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("path", videoPath);
                    bundle.putInt("MAX_DURATION", (int)getVideoDuration(videoPath));
                    Intent intent = new Intent(this,TrimmerActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent,VIDEO_TRIM_REQUEST_CODE);
                }
        } else
            Toast.makeText(this, "Cannot find this file", Toast.LENGTH_SHORT).show();
    }

    private long getVideoDuration(String videoFile) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoFile);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInSec = Long.parseLong(time)/1000;

        retriever.release();
        return timeInSec;
    }


    @Override
    public void onNearbyPlacesAdapterInteraction(final SelectedPlace selectedPlace) {
        fragmentManager.popBackStack();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uploadFragment.onNearbyPlacesAdapterInteraction(selectedPlace);
            }
        }, 100);
    }

    @Override
    public void onNearbyPlacesListInteraction(int action) {
        uploadFragment.onNearbyPlacesListInteraction(action);
    }

    @Override
    public void onPlaceClick(ArrayList<NearbyPlacesAdapter.PlaceAutocomplete> mResultList, int position) {
        if(mResultList!=null){
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);

                /*
                  Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(googleApiClient, placeId);
                placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(@NonNull PlaceBuffer places) {
                        if(places.getCount()==1){
                            //Do the things here on Click.....
                            uploadFragment.onNearbyPlacesAdapterInteraction(new SelectedPlace(
                                    places.get(0).getName().toString(),
                                    places.get(0).getLatLng().latitude,
                                    places.get(0).getLatLng().longitude
                            ));
                            fragmentManager.popBackStack();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            catch (Exception e){
                Log.e("onPlaceClick()", e.getMessage());
            }

        }
    }

    @Override
    public void onTagsAndCategoriesInteraction(final String action, final String resultToShow, final String resultToSend, final int count) {
        fragmentManager.popBackStack();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uploadFragment.onTagsAndCategoriesInteraction(action, resultToShow, resultToSend, count);
            }
        }, 100);
    }

    @Override
    public void onUploadInteraction(String tag, ArrayList<HashMap<String, String>> googlePlaces, String selectedData) {
        if (tag != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (tag) {
                case TAG_CATEGORIES_FRAGMENT:
                    fragmentTransaction.replace(R.id.uploading_container,
                            TagsAndCategoryFragment.newInstance(ACTION_CATEGORIES_FRAGMENT, selectedData), tag);
                    break;
                case TAG_TAGS_FRAGMENT:
                    fragmentTransaction.replace(R.id.uploading_container,
                            TagsAndCategoryFragment.newInstance(ACTION_TAGS_FRAGMENT, selectedData), tag);
                    break;
                case TAG_NEARBY_PLACES:
                    fragmentTransaction.replace(R.id.uploading_container,
                            NearbyPlacesList.newInstance(googlePlaces), tag);
                    break;
                case TAG_NULL_NEARBY_PLACES:
                    fragmentTransaction.replace(R.id.uploading_container,
                            NearbyPlacesList.newInstance(null), tag);
                    break;
                default:
                    break;
            }
            fragmentTransaction.addToBackStack(tag);
            fragmentTransaction.commit();
        } else {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void performUpload(int whichUpload, boolean isGallery, String videoPath, String title, String location,
                              double latitude, double longitude, String selectedTagsToSend, String selectedCategoriesToSend) {
        switch (whichUpload) {
            case VIDEO_UPLOAD:
                performVideoUpload(this,
                        new Pojos.UploadParams(
                                isGallery,
                                videoPath,
                                title,
                                location,
                                latitude,
                                longitude,
                                selectedTagsToSend,
                                selectedCategoriesToSend,
                                postDetails));
                break;
            case REACTION_UPLOAD:
                performReactionUpload(this,
                        new Pojos.UploadParams(
                                isGallery,
                                videoPath,
                                title,
                                location,
                                latitude,
                                longitude,
                                postDetails));
                break;
            default:
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private static class GetVideoGalleryData extends AsyncTask<Void, Void, Void> {

        private WeakReference<CameraActivity> reference;

        GetVideoGalleryData(CameraActivity context) {
            reference = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            reference.get().videosList.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Uri uri;
            Cursor cursor;
            int columnIndexData;
//                int columnId;
//                int columnFolderName;
            int thumbnailData;
            uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

            String[] projection = {
                    MediaStore.MediaColumns.DATA,
//                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
//                        MediaStore.Video.Media._ID,
                    MediaStore.Video.Thumbnails.DATA
            };
            final String orderByDateTaken = MediaStore.Video.Media.DATE_TAKEN;

            cursor = reference.get().getContentResolver().query(uri, projection, null, null,
                    orderByDateTaken + " DESC");
            if (cursor != null) {
                columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//                    columnFolderName = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
//                    columnId = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
                thumbnailData = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

                while (cursor.moveToNext()) {
                    reference.get().videosList.add(new Videos(
                            cursor.getString(columnIndexData),              //Video path
                            cursor.getString(thumbnailData)                 //Thumbnail
                    ));
                }
                cursor.close();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            reference.get().recyclerView.getAdapter().notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSIONS) {
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                prepareVideoGallery();
            }
        }
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            uploadFragment = null;
            cameraFragment = null;
            videosList.clear();
            videosList = null;
//        gridLayoutManager.removeAndRecycleAllViews(recycler);
            recyclerView.removeAllViews();
            if (panelSlideListener != null) {
                slidingUpPanelLayout.removePanelSlideListener(panelSlideListener);
                panelSlideListener = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VIDEO_TRIM_REQUEST_CODE:
                if (data != null) {
                    String videoPath = data.getStringExtra("trimmed_path");
                    uploadFragment = UploadFragment.newInstance(videoPath, false, true);
                    startVideoUploadFragment();
                }
                break;
            default:
                break;
        }
    }

    public void updateBackButton(@DrawableRes int resId) {
        if (resId != -1) {
            upBtn.setVisibility(VISIBLE);
            upBtn.setImageResource(resId);
        } else upBtn.setVisibility(GONE);
    }

    @OnClick(R.id.up_btn) public void retakeVideo() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if (slidingUpPanelLayout.getPanelState() == ANCHORED ||
                slidingUpPanelLayout.getPanelState() == EXPANDED) {
            slidingUpPanelLayout.setPanelState(COLLAPSED);
        }
        else if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
        else {
            if (!isReaction) {
                startActivity(new Intent(this, BaseBottomBarActivity.class));
                finish();
            } else super.onBackPressed();
        }
        if (fragmentManager.getBackStackEntryCount() == 1)
            cameraFragment.startPreview();
    }
}