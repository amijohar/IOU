package com.hkucs.splitters.billsplitter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by sandhyashukla on 16/11/17.
 */


public class InformatioIOU extends AppCompatActivity implements View.OnClickListener {
    EditText name;
    ArrayList<EditText> name2 = new ArrayList<EditText>();
    EditText amount;
    //EditText username;
    String username;
    CheckBox checkBox;
    FirebaseAuth firebaseAuth;

    Button btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informatio_iou);
        name = (EditText)findViewById(R.id.name);
        amount = (EditText)findViewById(R.id.amount);
        //username = (EditText)findViewById(R.id.username);
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user1 = firebaseAuth.getCurrentUser();
        //String user3 = firebaseAuth.getCurrentUser().toString();
        username = user1.getDisplayName();
        System.out.println(username  + ".........................................................................................................");
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        System.out.println("hi im here.....................................................................................................................");

        btn_add = (Button)findViewById(R.id.btn_add);
        // Register the Login button to click listener
        // Whenever the button is clicked, onClick is called
        btn_add.setOnClickListener(this);
        CookieHandler.setDefault(new CookieManager());
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.btn_add) {
            String name2 = name.getText().toString();
            String amount2 = amount.getText().toString();
            String username2 = username;//.getText().toString();
            String isBorrowed="";
            isBorrowed = "borrowed";

            if (checkBox.isChecked())
            {

                System.out.println("BALLE.....................................................................................................................");
                amount2 = "-"+amount2;
            }
            connect(username2, name2, amount2, isBorrowed);
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
    public String getJsonPage(String url) {
        HttpURLConnection conn_object = null;
        final int HTML_BUFFER_SIZE = 2*1024*1024;
        char htmlBuffer[] = new char[HTML_BUFFER_SIZE];
        try {
            System.out.println(url+"..................................................................................................................................");
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
    public void parse_JSON_String_and_Switch_Activity(String JSONString) {
        ArrayList<String> role = new ArrayList<String>();
        ArrayList<String> name = new ArrayList<String>();
        try {
            JSONObject rootJSONObj = new JSONObject(JSONString);
            String instructor = rootJSONObj.getString("teacher_1");
            role.add("Instructor");
            name.add(instructor);
            String teaching_assistant = rootJSONObj.getString("teacher_2");
            role.add("Teaching Assistant");
            name.add(teaching_assistant);
            JSONArray jsonArray = rootJSONObj.optJSONArray("students");
            for (int i=0; i<jsonArray.length(); ++i) {
                String studentName = jsonArray.getString(i);
                role.add("Student " + (i+1));
                name.add(studentName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
        intent.putStringArrayListExtra("Roles", role);
        intent.putStringArrayListExtra("Names", name);
        startActivity(intent);
    }
    public void connect(final String username, final String name, final String amount, final String isBorrowed){
        final ProgressDialog pdialog = new ProgressDialog(this);
        pdialog.setCancelable(false);
        pdialog.setMessage("Connecting ...");
        pdialog.show();
        final String url = "http://i.cs.hku.hk/~tmehra/tutorial.php"
                + (name.isEmpty() ? "" : "?"+(isBorrowed.isEmpty() ? "" : "action=" + android.net.Uri.encode(isBorrowed, "UTF-8"))+"&username="
                + android.net.Uri.encode(username, "UTF-8"))+(amount.isEmpty() ? "" : "&name="
                + android.net.Uri.encode(name, "UTF-8")) +(amount.isEmpty() ? "" : "&amount="
                + android.net.Uri.encode(amount, "UTF-8"));
        AsyncTask<String, Void, String> task = new AsyncTask<String,
                Void, String>() {
            boolean success;
            String jsonString;
            @Override
            protected String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                success = true;
                pdialog.setMessage("Loading ...");
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

}
