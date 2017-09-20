package com.example.asif047.mr_informer;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.asif047.mr_informer.ImageUpload.ApiClient;
import com.example.asif047.mr_informer.ImageUpload.ApiInterface;
import com.example.asif047.mr_informer.ImageUpload.ImageClass;
import com.example.asif047.mr_informer.helper.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InformActivity extends AppCompatActivity implements View.OnClickListener{


    String BASE_URL = "https://asif047mrinformer.000webhostapp.com/";

    EditText edt_phone, edt_message,edt_image;
    Button btn_insert, btn_display;


    //new starts
    private Button chooseBtn,uploadBtn;
    private ImageView img;
    private static final int IMG_REQUEST=777;

    private Bitmap bitmap;

  private ImageClass imageClass;


    //new 2 starts

    private String email,latitude,longitude,address,city,country,date_time;

    //new 2 ends



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inform);



        edt_phone = (EditText) findViewById(R.id.phoneEditText);
        edt_message = (EditText) findViewById(R.id.messageEditText);
        edt_image = (EditText) findViewById(R.id.imageEditText);


        btn_insert = (Button) findViewById(R.id.insert);


        //new 3 starts

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        latitude=intent.getStringExtra("latitude");
        longitude=intent.getStringExtra("longitude");
        address=intent.getStringExtra("address");
        city=intent.getStringExtra("city");
        country=intent.getStringExtra("country");
        date_time=intent.getStringExtra("date_time");

        //new 3 ends




        //new starts

        chooseBtn= (Button) findViewById(R.id.choose_bn);
        uploadBtn= (Button) findViewById(R.id.upload_bn);
        img= (ImageView) findViewById(R.id.imageview);

        chooseBtn.setOnClickListener(this);
        uploadBtn.setOnClickListener(this);

        uploadBtn.setEnabled(false);


        //new ends



        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //new starts
                if(edt_phone.getText().toString().isEmpty()) edt_phone.setError("This field is required");
                if(edt_message.getText().toString().isEmpty()) edt_message.setError("This field is required");
                //new ends

                if(!edt_phone.getText().toString().isEmpty()&&!edt_message.getText().toString().isEmpty())
                insert_data();
            }
        });


    }







    //new starts
    public void insert_data() {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL) //Setting the Root URL
                .build();

        AppConfig.insert api = adapter.create(AppConfig.insert.class);



        api.insertData(
                edt_phone.getText().toString(),
                email,
                edt_message.getText().toString(),
                latitude,
                longitude,
                address,
                city,
               country,
                edt_image.getText().toString()+"_"+date_time,
                date_time,
                new retrofit.Callback<retrofit.client.Response>() {
                    @Override
                    public void success(retrofit.client.Response result, retrofit.client.Response response) {

                        try {

                            BufferedReader reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                            String resp;
                            resp = reader.readLine();
                            Log.d("success", "" + resp);

                            JSONObject jObj = new JSONObject(resp);
                            int success = jObj.getInt("success");

                            if(success == 1){
                                Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_SHORT).show();
                            } else{
                                Toast.makeText(getApplicationContext(), "Insertion Failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (IOException e) {
                            Log.d("Exception", e.toString());
                        } catch (JSONException e) {
                            Log.d("JsonException", e.toString());
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(InformActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {


        switch (view.getId())
        {
            case R.id.choose_bn:
                selectImage();
                break;
            case R.id.upload_bn:

                uploadImage();
                break;
        }


    }




    //new starts

    private void uploadImage()
    {
        String Image=imageToString();
        String Title=edt_image.getText()+"_"+date_time;
        ApiInterface apiInterface= ApiClient.getApiClient().create(ApiInterface.class);
        Call<ImageClass> call=apiInterface.UploadImage(Title,Image);
        //Toast.makeText(InformActivity.this,Image,Toast.LENGTH_LONG).show();
        call.enqueue(new retrofit2.Callback<ImageClass>() {
            @Override
            public void onResponse(Call<ImageClass> call, retrofit2.Response<ImageClass> response) {
                //Toast.makeText(MainActivity.this,"Hello "+imageClass.getResponse(),Toast.LENGTH_LONG).show();
                imageClass=response.body();
                Toast.makeText(InformActivity.this,"Server Response: "+imageClass.getResponse(),Toast.LENGTH_LONG).show();
                img.setVisibility(View.GONE);
                // image_title.setVisibility(View.GONE);
                chooseBtn.setEnabled(true);
                uploadBtn.setEnabled(false);
                //image_title.setText("");
            }

            @Override
            public void onFailure(Call<ImageClass> call, Throwable t) {
                //Toast.makeText(MainActivity.this,"Server Response: "+t.getMessage().toString(),Toast.LENGTH_LONG).show();
                Log.e("Server Response: ", t.getMessage().toString());
                Log.e("Server Response: ", t.getStackTrace().toString());
            }
        });

    }



    private void selectImage()
    {




        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==IMG_REQUEST&&resultCode==RESULT_OK&&data!=null)
        {
            Uri path=data.getData();

            try
            {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                edt_image.setVisibility(View.VISIBLE);
                chooseBtn.setEnabled(false);
                uploadBtn.setEnabled(true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }


    private String imageToString()
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgByte=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }


    //new ends

    //new ends







}
