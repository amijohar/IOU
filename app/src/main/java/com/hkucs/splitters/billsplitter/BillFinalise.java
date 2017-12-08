package com.hkucs.splitters.billsplitter;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class BillFinalise extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String user;
    Button add_bill;
    //private ArrayList<String> usernames = new ArrayList<String>();
    ArrayList<String> usernames;
    Float amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_finalise);

        firebaseAuth = FirebaseAuth.getInstance();
        //ipaid = (Switch) findViewById(R.id.ipaid);
        usernames = (ArrayList<String>) getIntent().getStringArrayListExtra("key");
        System.out.println(usernames+".................................................................................");

        System.out.println(usernames.get(usernames.size()-1)+".........................................................................................................................................................");
        amount= Float.parseFloat(usernames.get(usernames.size()-1));
        firebaseUser = firebaseAuth.getCurrentUser();
        user = firebaseUser.getDisplayName();
        usernames.remove(usernames.size()-1);
        usernames.add(user);
        add_bill = (Button) findViewById(R.id.add_bill);


        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,usernames);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                user = adapterView.getItemAtPosition(i).toString();
                usernames.remove(user);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                usernames.remove(user);
            }
        });
        add_bill.setOnClickListener(this);
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
            JSONArray jsonArray = new JSONArray(usernames);

            // 3. build jsonObject
            float amount_for_JSON;
            amount_for_JSON = amount;
            amount_for_JSON = amount_for_JSON/(usernames.size()+1);

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", user);
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

    @Override
    public void onClick(View view) {
        if(view == add_bill)
        {
            new HttpAsyncTask().execute("http://i.cs.hku.hk/~tmehra/tutorial.php?action=split");
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

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


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}

