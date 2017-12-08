package com.hkucs.splitters.billsplitter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth firebaseAuth;
    private TextView userEmail;
    private Button buttonLogout;
    private Button infoIOU;
    private TextView display;
    private FirebaseUser user;
    Button settle;
    Button pay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        firebaseAuth= FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null) {

            finish();
            startActivity(new Intent(this, LoginActivity.class));

        }


        /*FirebaseUser*/ user=firebaseAuth.getCurrentUser();
        userEmail = (TextView) findViewById(R.id.userEmail);
        while(user.getDisplayName()==null){
            System.out.println("wait...............................................................");
        };
        userEmail.setText("Welcome "+user.getDisplayName());
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        infoIOU = (Button) findViewById(R.id.infoIOU);
        buttonLogout.setOnClickListener(this);
        infoIOU.setOnClickListener(this);
        display = (TextView) findViewById(R.id.display);
        display.setMovementMethod(new ScrollingMovementMethod());
        pay = (Button) findViewById(R.id.pay);
        pay.setOnClickListener(this);
        settle = (Button) findViewById(R.id.settle2);
        settle.setOnClickListener(this);
        connect();


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
    public void parse_JSON_String_and_Switch_Activity(String JSONString) {
//        ArrayList<String> role = new ArrayList<String>();
//        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> name = new ArrayList<String>();
        ArrayList<String> amount = new ArrayList<String>();
        try {
            System.out.println(JSONString + "..................................................................................................................");
            JSONObject jsonObject = new JSONObject(JSONString);

            JSONArray jsonArray = jsonObject.optJSONArray("list");

            for (int i=0; i<jsonArray.length(); i++)
            {
                String name2;
                String amount2;
                JSONObject jsonObject1;
                jsonObject1 = jsonArray.getJSONObject(i);
                name2 = jsonObject1.getString("name");
                amount2 = jsonObject1.getString("value");
                name.add(name2);
                amount.add(amount2);
            }

            //System.out.println("NAME: " + name.get(0) + ", value: " + amount.get(0) +  "..................................................................................................................");

            for (int i=0; i<name.size(); i++) {
                float borrowed;
                String lent = "Lent to";
                borrowed = Float.parseFloat(amount.get(i));
                if (borrowed>0)
                {
//                    lent = "Borrowed";
//                    borrowed= borrowed*(-1);
                    display.append(lent + "  |  " + name.get(i) + ", value: " + borrowed+"\n");
                }

            }
            for (int i=0; i<name.size(); i++) {
                float borrowed;
                String lent = "Lent to";
                borrowed = Float.parseFloat(amount.get(i));
                if (borrowed<0)
                {
                    lent = "Borrowed";
                    borrowed= borrowed*(-1);
                    display.append(lent + "  |  " + name.get(i) + ", value: " + borrowed+"\n");
                }
            }
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
    @Override
    public void onClick(View view) {
        if(view == buttonLogout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
        if(view == infoIOU){
            finish();
            startActivity(new Intent(this,BillDetails.class));
        }
        if(view == settle){
            finish();
            startActivity(new Intent(this,settleup.class));
        }
        if(view == pay){
            //startActivity(new Intent(this, PaymentActivity.class));
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadUrl("http://wallet.google.com");
        }

    }
    protected void alert(String title, String mymessage){
        new AlertDialog.Builder(this)
                .setMessage(mymessage)
                .setTitle(title)
                .setCancelable(true)
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton){}
                        }
                )
                .show();
    }
    public void connect(){
        final ProgressDialog pdialog = new ProgressDialog(this);
        pdialog.setCancelable(false);
        pdialog.setMessage("Connecting ...");
        pdialog.show();
        final String url = "http://i.cs.hku.hk/~tmehra/tutorial.php?" + ("action=display")+(user.getDisplayName().isEmpty()?"":"&username=" + android.net.Uri.encode(user.getDisplayName(), "UTF-8"));
        System.out.println(url+"................................................................................................................................................................................................................................");
        AsyncTask<String, Void, String> task = new AsyncTask<String,
                Void, String>() {
            boolean success;
            String jsonString;
            @Override
            protected String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                success = true;
                pdialog.setMessage("Before ...");
                pdialog.show();
                jsonString = getJsonPage(url);
                if (jsonString.equals("Fail to login"))
                    success = false;
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                if (success) {
                    parse_JSON_String_and_Switch_Activity(jsonString);
                } else {
                    alert( "Error", "Fail to connect" );
                }
                pdialog.hide();
            }
        }.execute("");
    }

    public void onBackPressed() {
        // your code.
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
}
