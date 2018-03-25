package tw.org.iii.mynetwork2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private UIHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.img);
        handler = new UIHandler();
    }

    public void test1(View view) {
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.bradchao.com");
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

            Bitmap bmp = (Bitmap)msg.getData().getParcelable("bmp");
            img.setImageBitmap(bmp);
        }
    }

    public void test3(View view) {
    }
    public void test4(View view) {
    }
}
