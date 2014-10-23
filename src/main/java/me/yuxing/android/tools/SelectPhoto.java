package me.yuxing.android.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * Created by yuxing on 2014-10-23.
 */
public class SelectPhoto {

    private static final int REQUEST_CODE_TAKE_PHOTO = 100;
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private final Activity mActivity;
    private Fragment mFragment;
    private OnSelectedListener mOnSelectedListener;
    private File mTakePhotoFile;

    public SelectPhoto(Activity activity) {
        mActivity = activity;
    }

    public SelectPhoto(Fragment fragment) {
        mFragment = fragment;
        mActivity = fragment.getActivity();
    }

    public void exec() {
        String[] items = new String[] {
                mActivity.getString(R.string.take_photo),
                mActivity.getString(R.string.select_from_album)};
        new AlertDialog.Builder(mActivity)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            File tempDir = mActivity.getExternalFilesDir("temp");
                            if (tempDir != null) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                mTakePhotoFile = new File(tempDir, System.currentTimeMillis() + ".jpg");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTakePhotoFile));
                                startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                        }
                    }
                })
                .show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Cursor cursor = mActivity.getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                if (mOnSelectedListener != null) {
                    mOnSelectedListener.onSelected(path);
                }
            }
            cursor.close();
        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            if (mTakePhotoFile != null) {
                if (mOnSelectedListener != null) {
                    mOnSelectedListener.onSelected(mTakePhotoFile.getAbsolutePath());
                }
            }
        }
    }

    public void setOnSelectedListener(OnSelectedListener listener) {
        mOnSelectedListener = listener;
    }

    public void crop() {


    }

    private void startActivityForResult(Intent intent, int requestCode) {
        if (mFragment != null) {
            mFragment.startActivityForResult(intent, requestCode);
        } else {
            mActivity.startActivityForResult(intent, requestCode);
        }
    }

    public static interface OnSelectedListener {
        public void onSelected(String path);
    }
}
