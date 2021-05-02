package e.anjul.singh.demojson;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    String result = null;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(new Runnable() {

            @Override
            public void run() {


                try {
                    URL url = new URL("https://fakemyapi.com/api/fake?id=296e44dc-3dc7-44c0-8659-f82cac8a4cd5");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();

                    if (inputStream == null)
                        result = "Data is not found";
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();

                    while ((line = br.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    result = stringBuilder.toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.equalsIgnoreCase("Data is not found")) {
                            Log.i("jSON", "not found");
                            Toast.makeText(MainActivity.this, "Not found", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                Log.i("jSON", jsonObject.toString());
                                textView.setText(jsonObject.toString());
                                downloadAndStoreJson(jsonObject.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }


        });


    }

    private void downloadAndStoreJson(String data){

        byte[] jsonArray = data.getBytes();

        File fileToSaveJson = new File("/storage/emulated/0/demo.json");



        BufferedOutputStream bos;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(fileToSaveJson));
            bos.write(jsonArray);
            bos.flush();
            bos.close();

        } catch (FileNotFoundException e4) {

            e4.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        finally {
            jsonArray=null;
            System.gc();
        }

    }
}