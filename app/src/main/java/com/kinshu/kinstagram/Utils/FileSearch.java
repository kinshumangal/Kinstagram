package com.kinshu.kinstagram.Utils;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {

    /**
     * Search a directory and return a list of all **directories** contained inside
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectoryPaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        assert listfiles != null;
        for (File listfile : listfiles) {
            if (listfile.isDirectory()) {
                pathArray.add(listfile.getAbsolutePath());
            }
        }
        return pathArray;
    }

    /**
     * Search a directory and return a list of all **files** contained inside
     * @param directory
     * @return
     */
    public static ArrayList<String> getFilePaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        for(int i = 0; i < listfiles.length; i++){
            if(listfiles[i].isFile()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }

    private static boolean isImageFile(File file) {
        String[] imageExtensions = {"jpg", "jpeg", "png", "gif"}; // Add more extensions if needed
        String fileName = file.getName().toLowerCase();

        for (String extension : imageExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }

        return false;
    }
    
}
