package com.ikramik.eyec;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikramik.eyec.R;

import java.util.List;
import java.io.File;

public class ClothingGridAdapter extends ArrayAdapter<ClothingItem> {

    public ClothingGridAdapter(Context context, List<ClothingItem> items) {
        super(context, R.layout.grid_item_clothing, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.grid_item_clothing, parent, false);
        }

        ClothingItem item = getItem(position);
        if (item == null) {
            return convertView;
        }

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView tvId = convertView.findViewById(R.id.tvId);
        TextView tvColor = convertView.findViewById(R.id.tvColor);

        if (item.getImagePath() != null) {
            File imgFile = new File(item.getImagePath());
            if (imgFile.exists()) {
                // Decode scaled-down image to prevent OutOfMemory crashes
                Bitmap bitmap = decodeSampledBitmapFromFile(item.getImagePath(), 150, 150);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                }
            } else {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        } else {
            imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        tvId.setText("Item " + item.getId());
        tvColor.setText("Color: " + item.getColor());

        return convertView;
    }

    // Helper method to downsample image files efficiently
    private Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize (power of 2 sizing adjustment)
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}