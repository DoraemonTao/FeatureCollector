package com.example.myfirstapp.util;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileMerge {
    public FileMerge(){

    }

    public static boolean mergeFiles(ArrayList<String> fpaths, String resultPath) {
        if (fpaths == null || fpaths.size() < 1 || TextUtils.isEmpty(resultPath)) {
            return false;
        }
        if (fpaths.size() == 1) {
            return new File(fpaths.get(0)).renameTo(new File(resultPath));
        }

        File[] files = new File[fpaths.size()];
        for (int i = 0; i < fpaths.size(); i ++) {
            files[i] = new File(fpaths.get(i));
            if (TextUtils.isEmpty(fpaths.get(i)) || !files[i].exists() || !files[i].isFile()) {
                return false;
            }
        }

        File resultFile = new File(resultPath);

        try {
            int bufSize = 1024;
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(resultFile));
            byte[] buffer = new byte[bufSize];

            for (int i = 0; i < fpaths.size(); i ++) {
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(files[i]));
                int readcount;
                while ((readcount = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, readcount);
                }
                inputStream.close();
            }
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
