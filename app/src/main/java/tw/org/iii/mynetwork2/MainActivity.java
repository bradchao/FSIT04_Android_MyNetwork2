package tw.org.iii.mynetwork2;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private UIHandler handler;
    private File donloadPath, sdroot;
    private ProgressDialog progressDialog;
    private EditText editAccount, editPasswd;

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
        sdroot = Environment.getExternalStorageDirectory();

        editAccount = findViewById(R.id.account);
        editPasswd = findViewById(R.id.passwd);

        File approot = new File(sdroot, "/Android/data/" + getPackageName());
        if (!approot.exists()){
            approot.mkdirs();
        }


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

    public void test5(View view) {
        new Thread(){
            @Override
            public void run() {
                //postURL();
                getOpendata();
            }
        }.start();
    }

    private void postURL(){
        try {
            MultipartUtility mu = new MultipartUtility(
                    "http://www.bradchao.com/iii/brad02.php", "",
                    "UTF-8"
            );
            mu.addFormField("account", editAccount.getText().toString());
            mu.addFormField("passwd", editPasswd.getText().toString());
            List<String> ret= mu.finish();
            for (String line : ret){
                Log.v("brad", line);
            }
        }catch(Exception e){
            Log.v("brad", e.toString());
        }
    }


    private void getOpendata(){
        try{
            MultipartUtility mu =
                    new MultipartUtility("http://data.coa.gov.tw/Service/OpenData/ODwsv/ODwsvTravelFood.aspx",
                            "", "UTF-8");
            List<String> ret = mu.finish();
            for (String line : ret){
                Log.v("brad", line);
            }

        }catch (Exception e){

        }
    }

    public void test6(View view) {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                .withHiddenFiles(true)
                .withTitle("Sample title")
                .start();


//        new Thread(){
//            @Override
//            public void run() {
//                try{
//                    File upload =
//                            new File(sdroot.getAbsolutePath() + "/Android/data/" + getPackageName() + "/brad.pdf");
//                    MultipartUtility mu =
//                            new MultipartUtility(
//                                    "http://www.bradchao.com/iii/brad03.php","","UTF-8");
//                    mu.addFilePart("upload", upload);
//                    mu.finish();
//                    Log.v("brad", "Upload OK");
//                }catch(Exception e){
//                    Log.v("brad", e.toString());
//                }
//            }
//        }.start();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
        Log.v("brad", filePath);
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
                    sdroot.getAbsolutePath() + "/Android/data/" + getPackageName() + "/brad.pdf");
            BufferedInputStream bin = new BufferedInputStream(conn.getInputStream());
            byte[] buf = new byte[4096]; int len = 0;
            while ( (len = bin.read(buf)) != -1){
                outFile.write(buf,0, len);
            }
            outFile.flush();
            outFile.close();
            handler.sendEmptyMessage(2);


            File pdffile = new File(donloadPath.getAbsolutePath() + "/brad.pdf");

            if (pdffile.exists()) {

                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(MainActivity.this,
                        BuildConfig.APPLICATION_ID + ".fileProvider", pdffile);
                intent2.setDataAndType(contentUri, "application/vnd.android.package-archive");
                startActivity(intent2);

//                Intent target = new Intent(Intent.ACTION_VIEW);
//                target.setDataAndType(Uri.fromFile(pdffile), "application/pdf");
//                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//                Intent intent = Intent.createChooser(target, "Open File");
//                try {
//                    startActivity(intent);
//                } catch (ActivityNotFoundException e) {
//                    // Instruct the user to install a PDF reader here, or something
//                    Log.v("brad", "pdf reader not found");
//                }
            }else{
                Log.v("brad", "pdf not found");
            }


        }catch (Exception e){
            Log.v("brad", e.toString());
        }



    }


    public void test4(View view) {
        new Thread(){
            @Override
            public void run() {
                checkAccount();
            }
        }.start();

    }

    private void checkAccount() {
        try {
            URL url = new URL("http://www.bradchao.com/iii/brad02.php");
            HttpURLConnection conn =
                    (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            ContentValues data = new ContentValues();
            data.put("account", editAccount.getText().toString());
            data.put("passwd", editPasswd.getText().toString());
            String qs = queryString(data);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(qs);
            writer.flush();
            writer.close();

            conn.connect();
            int rcode = conn.getResponseCode();
            String mesg = conn.getResponseMessage();
            Log.v("brad", rcode + ":" + mesg);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ( (line = reader.readLine()) != null){
                Log.v("brad", line);
            }


        } catch (Exception e) {
            Log.v("brad", e.toString());
        }
    }


    private String queryString(ContentValues data){
        StringBuffer sb = new StringBuffer();
        Set<String> keys = data.keySet();

        try {
            for (String key : keys) {
                sb.append(URLEncoder.encode(key, "UTF-8"));
                sb.append("=");
                sb.append(URLEncoder.encode(data.getAsString(key), "UTF-8"));
                sb.append("&");
            }
            sb.deleteCharAt(sb.length()-1);
            return sb.toString();
        }catch (Exception e){
            return  null;
        }


    }


}
