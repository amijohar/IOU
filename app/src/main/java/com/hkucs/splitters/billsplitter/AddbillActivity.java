package com.hkucs.splitters.billsplitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AddbillActivity extends AppCompatActivity implements View.OnClickListener {
//
//    TextView tvIsConnected;
//    EditText etName,etCountry,etTwitter;
//    Button btnPost;
    Button add_another;
    Button home;
    Button btn_add;
    EditText name;
    EditText amount;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String user;
    String message;
    ArrayAdapter<String> adapter;
    //Switch ipaid;
    Spinner spinner;
    //private static String[] usernames = new String[3];
    private ArrayList<String> usernames = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbill);
        add_another = (Button) findViewById(R.id.add_another);
        home = (Button) findViewById(R.id.home);
        btn_add = (Button) findViewById(R.id.btn_add);
        name = (EditText) findViewById(R.id.name);
        amount = (EditText) findViewById(R.id.amount);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        user = firebaseUser.getDisplayName();
        usernames.add(user);

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("message");

        //spinner = (Spinner) findViewById(R.id.spinner);
        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,items);


//        setContentView(R.layout.activity_main);

        // get reference to the views
//        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);
//        etName = (EditText) findViewById(R.id.etName);
//        etCountry = (EditText) findViewById(R.id.etCountry);
//        etTwitter = (EditText) findViewById(R.id.etTwitter);
//        btnPost = (Button) findViewById(R.id.btnPost);

        // check if you are connected or not
//        if(isConnected()){
//            tvIsConnected.setBackgroundColor(0xFF00CC00);
//            tvIsConnected.setText("You are conncted");
//        }
//        else{
//            tvIsConnected.setText("You are NOT conncted");
//        }
        add_another.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        home.setOnClickListener(this);

        //new HttpAsyncTask().execute("http://i.cs.hku.hk/~tmehra/tutorial.php?action=split");
//        new HttpAsyncTask().execute("localhost:8080/getthis");
        // add click listener to Button "POST"
//        btnPost.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,usernames);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void add_name_list()
    {
        usernames.add(name.getText().toString());
        name.setText("");

    }

    public void onClick(View view) {

        if (view == add_another)
        {
            add_name_list();
        }

        if(view==home)
        {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
//        if(view == btn_add)
//        {
//            usernames.add(amount.getText().toString());
//            //startActivity(new Intent(getApplicationContext(), BillFinalise.class));
//            Intent intent = new Intent(this, BillFinalise.class);
//            intent.putStringArrayListExtra("key", usernames);
//            startActivity(intent);
//        }
        if(view == btn_add)
        {
            //add_name_list();
            new HttpAsyncTask().execute("http://i.cs.hku.hk/~tmehra/tutorial.php?action=split");
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }
    }

    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            System.out.println("HERE...................................................................................................................................................................................................................................................");
            String json = "";
//            usernames[0]="random";
//            usernames[1]="random1";
//            usernames[2]="random2";
            usernames.remove(user);
            JSONArray jsonArray = new JSONArray(usernames);

            // 3. build jsonObject
            float amount_for_JSON;
            amount_for_JSON = Float.parseFloat(message);
            amount_for_JSON = amount_for_JSON/(usernames.size()+1);

            JSONObject jsonObject = new JSONObject();
            json = jsonObject.toString();
            jsonObject.accumulate("name", user);
            System.out.println(json+".............................................................z");
            jsonObject.accumulate("username",jsonArray);
            jsonObject.accumulate("amount", amount_for_JSON);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            System.out.println(json+".............................................................z");
            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

//            person = new Person();
//            person.setName(etName.getText().toString());
//            person.setCountry(etCountry.getText().toString());
//            person.setTwitter(etTwitter.getText().toString());

            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        inputStream.close();
        return result;
    }

    public void onBackPressed() {
        // your code.
        finish();
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }
}