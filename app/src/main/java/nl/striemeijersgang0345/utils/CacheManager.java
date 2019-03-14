package nl.striemeijersgang0345.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import androidx.core.content.FileProvider;

public class CacheManager {

    private static final long MAX_SIZE = 10485760L; // 10MB

    private CacheManager() {

    }

    public static void clearCache(Context context) {

        Runnable r = () -> {
            File cacheDir = context.getExternalCacheDir();
            assert cacheDir != null;
            long size = getDirSize(cacheDir);
            Log.d("CacheManager/clearCache", "Cache size is " + Long.toString(size) + " bytes");

            if (size > MAX_SIZE) {
                cleanDir(cacheDir, size - MAX_SIZE);
            }
        };
        r.run();
    }

    public static Uri getFileUri(Context mContext, String path, String resourceName) throws IOException {
        clearCache(mContext);
        // Check if SD is mounted
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File file = copyToFile(mContext, path, resourceName);
            return FileProvider.getUriForFile(mContext,
                        mContext.getApplicationContext().getPackageName() + ".provider",
                        file);
        } else
            throw new IOException();
    }

    private static File copyToFile(Context mContext, String file, String resourceName) {

            File outputFile = new File(mContext.getExternalCacheDir(), resourceName);
            if(!outputFile.exists()) {
                Runnable r = () -> {
                    try {
                        InputStream in = mContext.getAssets().open(file);
                        FileOutputStream out;
                        out = new FileOutputStream(outputFile);
                        byte[] buff = new byte[1024];
                        int read;
                        try {
                            while ((read = in.read(buff)) > 0) {
                                out.write(buff, 0, read);
                            }
                        } finally {
                            in.close();
                            out.flush();
                            out.close();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
                r.run();
            }
            return outputFile;
    }

    static byte[] retrieveData(Context context, String name) throws IOException {

        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, name);

        if (!file.exists()) {
            // Data doesn't exist
            return null;
        }

        byte[] data = new byte[(int) file.length()];
        try (FileInputStream is = new FileInputStream(file)) {
            is.read(data);
        }

        return data;
    }

    private static void cleanDir(File dir, long bytes) {

        long bytesDeleted = 0;
        File[] files = dir.listFiles();

        for (File file : files) {
            bytesDeleted += file.length();
            file.delete();

            if (bytesDeleted >= bytes) {
                break;
            }
        }
    }

    private static long getDirSize(File dir) {

        long size = 0;
        File[] files = dir.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                size += file.length();
            }
        }

        return size;
    }
}