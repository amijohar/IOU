package com.hkucs.splitters.billsplitter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private SignInButton googleSignIn;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleSignInClient;
    private EditText name;
    private FirebaseUser firebaseUser;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        firebaseAuth= FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button)findViewById(R.id.buttonRegister);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        textViewSignIn = (TextView) findViewById(R.id.textViewSignIn);
        name = (EditText) findViewById(R.id.name);
        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
        //googleSignIn = (SignInButton) findViewById(R.id.googleSignIn);

        if(firebaseAuth.getCurrentUser()!=null){

            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        }

        //googleSignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

    }

    private void registerUser()
    {
        String email= editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if(TextUtils.isEmpty(email))
        {
            //email empty
            Toast.makeText(this, "Please enter an email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            //password empty
            Toast.makeText(this, "Please enter a password",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        Log.d("myTag", email);


        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            System.out.println("kahe hamein pareshan kar rahe ho bhaiya");
                            // Sign in success, update UI with the signed-in user's information
                            userProfile();

                        } else {
                            // If sign in fails, display a message to the user.
                            FirebaseAuthException e = (FirebaseAuthException )task.getException();
                            Toast.makeText(MainActivity.this, "Failed Registration: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // ...
                    }
                });




    }
    private void userProfile(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if(firebaseUser!=null){
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name.getText().toString().trim()).build();

            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("Testing","User profile updated");

                            }
                        }
                    });

            while(firebaseUser.getDisplayName()==null){
                //System.out.println("wait...............................................................");
            }

            System.out.println("CHASKA CHASKA");
            user = firebaseUser.getDisplayName();
            new MainActivity.HttpAsyncTask().execute("http://i.cs.hku.hk/~tmehra/tutorial.php?action=add");
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));


        }

    }

    @Override
    public void onClick(View view) {

        if(view == buttonRegister)
            registerUser();

        if(view == textViewSignIn)
        {
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

    }

    //googleSignIn

//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }


    public String POST(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);
            System.out.println("HERE..................................................................................................................................................................................................................................................."+ user);
            String json = "";
//            usernames[0]="random";
//            usernames[1]="random1";
//            usernames[2]="random2";
//            usernames.remove(user);
//            JSONArray jsonArray = new JSONArray(usernames);

            // 3. build jsonObject
//            float amount_for_JSON;
//            amount_for_JSON = Float.parseFloat(amount.getText().toString());
//            amount_for_JSON = amount_for_JSON/(usernames.size()+1);

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("username", user);
            //jsonObject.accumulate("username",jsonArray);
            //jsonObject.accumulate("amount", amount_for_JSON);

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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
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
