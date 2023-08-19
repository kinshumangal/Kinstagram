package com.kinshu.kinstagram.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class GridImageAdapter extends ArrayAdapter<String> {
    private Context context;
    private LayoutInflater inflater;
    private int layoutResource;
    private String append;
    private ArrayList<String> imgURLs;

    public GridImageAdapter(Context context, LayoutInflater inflater, int layoutResource, String append, ArrayList<String> imgURLs) {
        super(context, layoutResource, imgURLs);
        this.context = context;
        this.inflater = inflater;
        this.layoutResource = layoutResource;
        this.append = append;
        this.imgURLs = imgURLs;
    }
}
