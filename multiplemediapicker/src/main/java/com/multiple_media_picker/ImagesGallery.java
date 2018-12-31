package com.multiple_media_picker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.multiple_media_picker.Adapters.MediaAdapter;

import java.util.ArrayList;
import java.util.List;

public class ImagesGallery extends AppCompatActivity {
    public String title;
    public int maxSelection;
    private RecyclerView recyclerView;
    private final String[] projection = new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA};
    private final String[] projection2 = new String[]{MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
    public ArrayList<String> imagesSelected = new ArrayList<>();
    public List<Boolean> selected = new ArrayList<>();
    private List<String> mediaList = new ArrayList<>();
    private MediaAdapter mAdapter;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = false;
    private int mPage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            onBackPressed();
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_images_gallery);
            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
           setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.arrow_back);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                    // returnResult();
                }
            });
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    returnResult();
                }
            });
            title = getIntent().getExtras().getString("title");
            maxSelection = getIntent().getExtras().getInt("maxSelection");
            List<String> selectedList = (List<String>) getIntent().getSerializableExtra("selectedList");
            imagesSelected.addAll(selectedList);
            if (maxSelection == 0) maxSelection = Integer.MAX_VALUE;
            if (imagesSelected.size() != 0) {
                setTitle(String.valueOf(imagesSelected.size()));
            } else {
                setTitle(title);
            }
            populateRecyclerView();
            mPage = 0;
            getAllShownImagesPath(this, mPage);
            //  getPicBuckets();
            //  new LoadAsync().execute();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) //check for scroll down
                    {
                        GridLayoutManager mLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                        if (!loading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                loading = true;
                                mPage = mPage + 1;
                                getAllShownImagesPath(ImagesGallery.this, mPage);
                                //  getPicBuckets();

                            }
                        }
                    }
                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //  returnResult();
    }

   /* *//*
     * Get all folders of images
     * *//*
    public void getPicBuckets() {
        Cursor cursor = getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                        null, null, MediaStore.Images.Media.DATE_ADDED + " ASC LIMIT 20 OFFSET 20");
        ArrayList<String> bucketNamesTEMP = new ArrayList<>(cursor.getCount());
        HashSet<String> albumSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
                }
                String album = cursor.getString(cursor.getColumnIndex(projection[0]));
                if (!albumSet.contains(album)) {
                    bucketNamesTEMP.add(album);
                    albumSet.add(album);
                    getPictures(album);
                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();
    }

    *//*
     * Get all images of particular folder
     * *//*
    public void getPictures(String bucket) {
        Cursor cursor = getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection2,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{bucket}, MediaStore.Images.Media.DATE_ADDED + " ASC LIMIT 20 OFFSET 20");
        HashSet<String> albumSet = new HashSet<>();
        File file;
        if (cursor.moveToLast()) {
            do {
                if (Thread.interrupted()) {
                    return;
                }
                String path = cursor.getString(cursor.getColumnIndex(projection2[1]));
                file = new File(path);
                if (file.exists() && !albumSet.contains(path)) {
                    albumSet.add(path);
                    if (imagesSelected.size() > 0) {
                        if (imagesSelected.contains(path))
                            selected.add(true);
                        else
                            selected.add(false);
                    } else
                        selected.add(false);
                    mediaList.add(path);

                }
            } while (cursor.moveToPrevious());
        }
        cursor.close();
    }
*/

    /*
     * Show all images on recyclerview
     * */
    private void populateRecyclerView() {
        mAdapter = new MediaAdapter(mediaList, selected, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (selected.get(position).equals(false) && imagesSelected.size() < maxSelection) {
                    imagesSelected.add(mediaList.get(position));
                    selected.set(position, !selected.get(position));
                    mAdapter.notifyItemChanged(position);
                } else if (selected.get(position).equals(true)) {
                    if (imagesSelected.indexOf(mediaList.get(position)) != -1) {
                        imagesSelected.remove(imagesSelected.indexOf(mediaList.get(position)));
                        selected.set(position, !selected.get(position));
                        mAdapter.notifyItemChanged(position);
                    }
                } else {
                    Toast.makeText(ImagesGallery.this, "you can select only " + maxSelection, Toast.LENGTH_SHORT).show();
                }
                if (imagesSelected.size() != 0) {
                    setTitle(String.valueOf(imagesSelected.size()));
                } else {
                    setTitle(title);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    /**
     * Getting All Images Path.
     *
     * @param activity the activity
     * @param i
     */
    private void getAllShownImagesPath(Activity activity, int i) {
        Uri uri;
        Cursor cursor;
        int column_index_data/*, column_index_folder_name*/;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, MediaStore.Images.Media.DATE_ADDED + " DESC LIMIT 50 OFFSET " + ((i * 20) + 20));

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        //  column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            if (imagesSelected.size() > 0) {
                if (imagesSelected.contains(absolutePathOfImage))
                    selected.add(true);
                else
                    selected.add(false);
            } else
                selected.add(false);
            listOfAllImages.add(absolutePathOfImage);
        }
        if (listOfAllImages.size() != 0) {
            loading = false;
            mediaList.addAll(listOfAllImages);
            if (listOfAllImages.size() != 0)
                mAdapter.notifyItemInserted(listOfAllImages.size() - 50);
            else
                mAdapter.notifyDataSetChanged();
        }
        cursor.close();
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

/*

    public class LoadAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            getPicBuckets();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();
        }
    }

*/

    /*
     * Return selected images
     * */
    private void returnResult() {
        Intent returnIntent = new Intent();
        returnIntent.putStringArrayListExtra("result", imagesSelected);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
