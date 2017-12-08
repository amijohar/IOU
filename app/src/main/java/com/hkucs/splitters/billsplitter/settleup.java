package com.hkucs.splitters.billsplitter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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


public class settleup extends AppCompatActivity implements View.OnClickListener {
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
    ArrayAdapter<String> adapter;
    //Switch ipaid;
    Spinner spinner;
    String username;
    private ArrayList<String> usernames = new ArrayList<String>();
    EditText user1;
    float amount1;
    int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        p=2;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settleup);
        add_another = (Button) findViewById(R.id.add_another);
        //home = (Button) findViewById(R.id.home);
        btn_add = (Button) findViewById(R.id.btn_add);
       // name = (EditText) findViewById(R.id.name);
        amount = (EditText) findViewById(R.id.amount);

        user1 = (EditText) findViewById(R.id.user1);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        user = firebaseUser.getDisplayName();


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
        //add_another.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        //home.setOnClickListener(this);

        //new HttpAsyncTask().execute("http://i.cs.hku.hk/~tmehra/tutorial.php?action=split");
//        new HttpAsyncTask().execute("localhost:8080/getthis");
        // add click listener to Button "POST"
//        btnPost.setOnClickListener(this);
//        spinner = (Spinner) findViewById(R.id.spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,usernames);
//        spinner.setAdapter(adapter);

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                user = adapterView.getItemAtPosition(i).toString();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

    public void add_name_list()
    {
        //usernames.add(name.getText().toString());
        //name.setText("");

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

            username = user1.getText().toString();
            usernames.add(user);
            amount1 = Float.parseFloat(amount.getText().toString().trim());
            new HttpAsyncTask().execute("http://i.cs.hku.hk/~tmehra/tutorial.php?action=settle_up");
            System.out.println("Tryong to get json");
//
//            System.out.println(jsonString+"........................................................................................................................");
//            parse_JSON_String_and_Switch_Activity(jsonString);

//            connect();
//            if (p==1) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//            }
//            else if (p==0)
//            {
//                Toast.makeText(getBaseContext(), "User does not exist!", Toast.LENGTH_LONG).show();
//            }
        }



    }
    public void parse_JSON_String_and_Switch_Activity(String JSONString) {
//        ArrayList<String> role = new ArrayList<String>();
//        ArrayList<String> name = new ArrayList<String>();f
        System.out.println("Entereing /..............................................");
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> amount = new ArrayList<String>();
        try {
            System.out.println(JSONString + "..................................................................................................................");
            JSONObject jsonObject = new JSONObject(JSONString);
            p = Integer.parseInt(jsonObject.getString("result"));
            System.out.println(p+"value............................");
            //System.out.println("NAME: " + name.get(0) + ", value: " + amount.get(0) +  "..................................................................................................................");


            //             System.out.println(JSONString);
//            JSONObject rootJSONObj = new JSONObject(JSONString);
//
//            String name2= rootJSONObj.getString("values");
//            String value2 = rootJSONObj.getString("New");
//            name.add(name2);
//            number.add(value2);
//
//            System.out.println("........................................................." + name.get(0) + "<-value New->"+number.get(0));
//            JSONObject rootJSONObj = new JSONObject(JSONString);
//            JSONArray jsonArray = rootJSONObj.optJSONArray("name");
//
//            String name1;
//            String value1;
//
//            for (int i=0; i<jsonArray.length(); i++)
//            {
//                name1 = jsonArray.getString(i);
//                name.add(name1);
//                System.out.println(name1 + "......................................................................................................................................................................................................................................");
//            }
//            String instructor = rootJSONObj.getString("teacher_1");
//            role.add("Instructor");
//            name.add(instructor);
//            String teaching_assistant = rootJSONObj.getString("teacher_2");
//            role.add("Teaching Assistant");
//            name.add(teaching_assistant);
//            JSONArray jsonArray = rootJSONObj.optJSONArray("students");
//            for (int i=0; i<jsonArray.length(); ++i) {
//                String studentName = jsonArray.getString(i);
//                role.add("Student " + (i+1));
//                name.add(studentName);
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        Intent intent = new Intent(getBaseContext(), PersonListActivity.class);
        //intent.putStringArrayListExtra("Roles", role);
        //intent.putStringArrayListExtra("Names", name);
        //startActivity(intent);
    }
    public String getJsonPage(String url) {
        HttpURLConnection conn_object = null;
        final int HTML_BUFFER_SIZE = 2*1024*1024;
        char htmlBuffer[] = new char[HTML_BUFFER_SIZE];
        try {
            System.out.println("hit from profile page.... " + url+"..................................................................................................................................");
            URL url_object = new URL(url);
            conn_object = (HttpURLConnection) url_object.openConnection();
            conn_object.setInstanceFollowRedirects(true);
            BufferedReader reader_list = new BufferedReader
                    (new InputStreamReader(conn_object.getInputStream()));
            String HTMLSource = ReadBufferedHTML(reader_list, htmlBuffer,
                    HTML_BUFFER_SIZE);
            reader_list.close();
            return HTMLSource;
        } catch (Exception e) {
            return "Fail to login";
        } finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            if (conn_object != null) {
                conn_object.disconnect();
            }
        }
    }
    public String ReadBufferedHTML(BufferedReader reader,
                                   char [] htmlBuffer, int bufSz) throws java.io.IOException {
        htmlBuffer[0] = '\0';
        int offset = 0;
        do {
            int cnt = reader.read(htmlBuffer, offset, bufSz - offset);
            if (cnt > 0) {
                offset += cnt;
            } else {
                break;
            }
        } while (true);
        return new String(htmlBuffer);
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
            //usernames.remove(user);
            JSONArray jsonArray = new JSONArray(usernames);

            // 3. build jsonObject
            float amount_for_JSON;
            //amount_for_JSON = Float.parseFloat(amount.getText().toString());
            //amount_for_JSON = amount_for_JSON/(usernames.size()+1);

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("name", username);
            jsonObject.accumulate("username",jsonArray);
            jsonObject.accumulate("amount", amount1);

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
    public void connect(){
        final ProgressDialog pdialog = new ProgressDialog(this);
        pdialog.setCancelable(false);
//        pdialog.setMessage("Connecting ...");
//        pdialog.show();
        final String url = "http://i.cs.hku.hk/~tmehra/tutorial.php?action=settle_up2";
        System.out.println(url+"................................................................................................................................................................................................................................");
        AsyncTask<String, Void, String> task = new AsyncTask<String,Void, String>() {
            boolean success;
            String jsonString;
            protected String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                success = true;
//                pdialog.setMessage("Before ...");
//                pdialog.show();
                jsonString = getJsonPage(url);
                System.out.println("My json:"+jsonString);
                if (jsonString.equals("Fail to login"))
                    success = false;
                return null;
            }

            protected void onPostExecute(String result) {
                if (success) {
                    parse_JSON_String_and_Switch_Activity(jsonString);
                    System.out.println(jsonString);//+"...........................................jsexon");
                } else {
                    System.out.println("qwertyuiopoiuytrewqwertyuiopoiuytrewqwertyuiopoiuytrewqß");
                    //alert( "Error", "Fail to connect" );
                }
                pdialog.hide();
            }
        }.execute("");
    }
    public void onBackPressed() {
        // your code.
        finish();
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }
}