package com.little.sample.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.little.picture.PicturePickActivity;
import com.little.picture.glide.GlideUtil;
import com.little.picture.listener.IOnDeleteListener;
import com.little.picture.listener.IOnItemClickListener;
import com.little.picture.util.ImageChooseUtil;
import com.little.picture.util.ImagePreviewUtil;
import com.little.picture.util.ImageUtil;
import com.little.popup.PopupDialog;
import com.little.popup.listener.IOnItemListener;
import com.little.sample.R;
import com.little.sample.adapter.PictureSampleAdapter;
import com.little.sample.util.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PictureSampleActivity extends Activity {

    @InjectView(R.id.activity_picture_sample_single)
    TextView activityPictureSampleSingle;
    @InjectView(R.id.activity_picture_sample_imageView)
    ImageView imageView;
    @InjectView(R.id.activity_picture_sample_gridView)
    GridView activityPictureSampleGridView;
    //多张照片
    private PictureBroadcastReceiver pictureBroadcastReceiver;
    private List<String> chooseImageList = new ArrayList<String>();//选择的照片集合
    private PictureSampleAdapter adapter;
    private int screenWidth = 0;
    private int maxSize = 9;
    //单张照片
    private PopupDialog popupDialog;
    private List<String> optionList = new ArrayList<String>();
    private ImageChooseUtil imageChooseUtil;
    private boolean isCrop = false;//是否裁剪模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_sample);
        ButterKnife.inject(this);
        init();
    }

    @OnClick(R.id.activity_picture_sample_single)
    public void onClick() {
        popupDialog.showListDialog(activityPictureSampleGridView,optionList);
    }

    @Override
    public void onDestroy(){
        this.unregisterReceiver(pictureBroadcastReceiver);
        super.onDestroy();
    }


    public void init() {
//        FrescoUtils.init(this);
        //单张照片
        imageChooseUtil = new ImageChooseUtil(this);
        optionList.add("拍照");
        optionList.add("图库选择");
        optionList.add("拍照--裁剪");
        optionList.add("图库选择--裁剪");
        optionList.add("头像");
        popupDialog = new PopupDialog.Builder(this).dismissBackKey(true).dismissOutside(true).onItemListener(new IOnItemListener() {
            @Override
            public void onItem(int position) {
                switch (position){
                    case 0:
                        imageChooseUtil.doTakePhoto();
                        isCrop = false;
                        break;
                    case 1:
                        imageChooseUtil.doGalleryPhoto();
                        isCrop = false;
                        break;
                    case 2:
                        imageChooseUtil.doTakePhoto();
                        isCrop = true;
                        break;
                    case 3:
                        imageChooseUtil.doGalleryPhoto();
                        isCrop = true;
                        break;
                    case 4:
                        setChooseImagesIntent(PicturePickActivity.PICK_AVATAR);
                        break;
                }
            }
        }).build();

        //多张照片
        screenWidth = ImageUtil.getScreenWidth(this);
        registerBroadcast();
        adapter = new PictureSampleAdapter(this,chooseImageList,screenWidth,maxSize);
        adapter.setOnItemClickListener(iOnItemClickListener);
        activityPictureSampleGridView.setAdapter(adapter);
    }

    private IOnItemClickListener iOnItemClickListener = new IOnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            try{
                if (position==chooseImageList.size()){
                    setChooseImagesIntent(PicturePickActivity.PICK_IMAGE);
                }else {
                    if (chooseImageList.size()>0){
                        ImagePreviewUtil imagesPreviewUtil = new ImagePreviewUtil(PictureSampleActivity.this,activityPictureSampleGridView);
                        imagesPreviewUtil.setOnDeleteListener(new IOnDeleteListener() {
                            @Override
                            public void onDelete(int position) {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        imagesPreviewUtil.showImagePreview(position,chooseImageList);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };


    public void setChooseImagesIntent(int pickType){
        if (chooseImageList.size()<maxSize){
            Intent intent = new Intent(this, PicturePickActivity.class);
            intent.putExtra(PicturePickActivity.PICTURE_PICK_IMAGE,maxSize-chooseImageList.size());
            intent.putExtra(PicturePickActivity.PICTURE_PICK_TYPE,pickType);
            startActivity(intent);
        }else {
            ToastUtil.addToast("" + getString(R.string.picture_max) + maxSize);
        }

    }

    private void registerBroadcast(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PicturePickActivity.PICTURE_PICK_IMAGE);
        pictureBroadcastReceiver = new PictureBroadcastReceiver();
        this.registerReceiver(pictureBroadcastReceiver, intentFilter);
    }

    private class PictureBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(PicturePickActivity.PICTURE_PICK_IMAGE)) {
                ArrayList<String> imageList = intent.getStringArrayListExtra(PicturePickActivity.PICTURE_PICK_IMAGE);
                for(int i=0;i<imageList.size();i++) {
                    chooseImageList.add(imageList.get(i));
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImageChooseUtil.PHOTO_WITH_CAMERA:
                    if (!isCrop){
                        String path = imageChooseUtil.getTakePhotoScaleUrl();//获取拍照压缩路径
                        GlideUtil.getInstance().display(PictureSampleActivity.this,ImageUtil.completeImagePath(path), imageView,ImageUtil.dip2px(PictureSampleActivity.this,80),ImageUtil.dip2px(PictureSampleActivity.this,80));
                    }else {
                        imageChooseUtil.doCropPhoto(imageChooseUtil.getTakeUri());
                    }
                    break;
                case ImageChooseUtil.CHOOSE_PICTURE:
                    if (!isCrop){
                        String galleryPath = imageChooseUtil.getGalleryUrl(data);//获取图库路径
                        GlideUtil.getInstance().display(PictureSampleActivity.this, ImageUtil.completeImagePath(galleryPath), imageView, ImageUtil.dip2px(PictureSampleActivity.this, 80), ImageUtil.dip2px(PictureSampleActivity.this, 80));
                    }else {
                        imageChooseUtil.doCropPhoto(imageChooseUtil.getGalleryUri(data));
                    }
                    break;
                case ImageChooseUtil.PHOTO_PICKED_WITH_CROP:
                    Bitmap headPhoto = imageChooseUtil.getCropBitmap(data);
                    String imagePath = imageChooseUtil.getImagePathFolder() + System.currentTimeMillis() + ".jpg";
                    File dir = new File(imageChooseUtil.getImagePathFolder());
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    boolean saveResult = ImageUtil.saveJPGE_After(headPhoto, 100, imagePath);
                    if (saveResult){
                        GlideUtil.getInstance().display(PictureSampleActivity.this, ImageUtil.completeImagePath(imagePath), imageView, ImageUtil.dip2px(PictureSampleActivity.this, 80), ImageUtil.dip2px(PictureSampleActivity.this, 80));
                    }
                    break;
            }

        }
    }

}
