package tw.org.iii.mynetwork2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private UIHandler handler;
    private File donloadPath;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }else{
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            init();
        }else{
            finish();
        }
    }

    private void init(){
        img = findViewById(R.id.img);
        handler = new UIHandler();

        donloadPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Downloading...");
    }


    public void test1(View view) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://10.0.3.2:8088/MyServer/brad01.jsp");
                    HttpURLConnection conn =
                            (HttpURLConnection)url.openConnection();
                    conn.connect();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String line = null;
                    while ( (line = reader.readLine()) != null){
                        Log.v("brad", line);
                    }
                }catch(Exception e){
                    Log.v("brad", e.toString());
                }

            }
        }.start();

    }
    public void test2(View view) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("https://s2.yimg.com/uu/api/res/1.2/HcjRj7fZncmu5SbOQOCAoA--~B/Zmk9dWxjcm9wO2N3PTUxNDtkeD02ODtjaD0zMDY7ZHk9NDU7dz0zOTI7aD0zMDg7Y3I9MTthcHBpZD15dGFjaHlvbg--/https://media.zenfs.com/creatr-images/GLB/2018-03-25/c8faba80-2fd3-11e8-8f9a-ad649e394bc5_-.jpg");
                    HttpURLConnection conn =
                            (HttpURLConnection)url.openConnection();
                    conn.connect();


                    Bitmap bmp = BitmapFactory.decodeStream(conn.getInputStream());

                    Message mesg = new Message();
                    Bundle data = new Bundle();
                    data.putParcelable("bmp", bmp);
                    mesg.setData(data);
                    mesg.what = 1;
                    handler.sendMessage(mesg);
                    //img.setImageBitmap(bmp);

                }catch (Exception e){
                    Log.v("brad", e.toString());
                }
            }
        }.start();


    }

    private class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    Bitmap bmp = (Bitmap)msg.getData().getParcelable("bmp");
                    img.setImageBitmap(bmp);
                    break;
                case 2:
                    progressDialog.dismiss();
                    break;
            }
        }
    }

    public void test3(View view) {
        progressDialog.show();
        new Thread(){
            @Override
            public void run() {
                getPDF();
            }
        }.start();

    }

    private void getPDF(){
        try{
            URL url = new URL("http://pdfmyurl.com/?url=http://www.gamer.com.tw");
            HttpURLConnection conn =
                    (HttpURLConnection)url.openConnection();
            conn.connect();

            FileOutputStream outFile = new FileOutputStream(
                    donloadPath.getAbsolutePath() + "/brad.pdf");
            BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
            byte[] buf = new byte[4096]; int len = 0;
            while ( (len = bin.read(buf)) != -1){
                outFile.write(buf,0, len);
            }
            outFile.flush();
            outFile.close();
            handler.sendEmptyMessage(2);
        }catch (Exception e){
            Log.v("brad", e.toString());
        }



    }


    public void test4(View view) {
    }
}
