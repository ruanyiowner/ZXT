package tools.file;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import tools.image.AsynImageLoader;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import android.content.*;
import android.graphics.Bitmap;
public class FileUtil {
    private static final String TAG = "FileUtil";

    public static File getCacheFile(String imageUri){
        File cacheFile = null;
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File sdCardDir = Environment.getExternalStorageDirectory();
                String fileName = getFileName(imageUri);
                File dir = new File(sdCardDir.getCanonicalPath()
                        + "/"+AsynImageLoader.CACHE_DIR);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                cacheFile = new File(dir, fileName);
                Log.i(TAG, "exists:" + cacheFile.exists() + ",dir:" + dir + ",file:" + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "getCacheFileError:" + e.getMessage());
        }

        return cacheFile;
    }

    public static String getFileName(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }


    // 保存文件
    public static void SaveFileContent(Context context,String filename,String content)
    {
        //File file = new File(context.getFilesDir(), filename);
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            // e.printStackTrace();
        }
    }
    /**
     * 获取文件文本内容
     * @param filename
     * @return
     */
    public static String GetFileContent(Context context,String filename)
    {
        String str = "";

        try {
            //File urlFile = new File(filename);
            //InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String mimeTypeLine = null ;
            StringBuilder sb = new StringBuilder();
            while ((mimeTypeLine = br.readLine()) != null) {
                //str = str+mimeTypeLine;
                sb.append(mimeTypeLine);
            }
            str = sb.toString();
        } catch (Exception e) {
            // e.printStackTrace();
        }
        return  str;
    }





    public static void writeToSd(String text) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
            File sdFile = new File(sdCardDir, "myfirstsd.txt");

            try {
                FileOutputStream fos = new FileOutputStream(sdFile);
                fos.write(text.getBytes());
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
