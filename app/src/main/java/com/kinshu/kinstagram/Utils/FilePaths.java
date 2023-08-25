package com.kinshu.kinstagram.Utils;

import android.os.Environment;

public class FilePaths {

    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String DOWNLOAD = ROOT_DIR + "";
    public String CAMERA = ROOT_DIR + "/DCIM";

    public String FIREBASE_IMAGE_STORAGE = "photos/users/";


}
