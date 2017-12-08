package com.hkucs.splitters.billsplitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BillDetails extends AppCompatActivity implements View.OnClickListener {

    Button next;
    EditText amount,name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        next = (Button) findViewById(R.id.next);
        amount = (EditText) findViewById(R.id.amount);
        name = (EditText) findViewById(R.id.name);

        next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view==next){
            final String billAmount = amount.getText().toString().trim();
            //billName = name.getText().toString().trim();

            if(billAmount.length()==0){
                amount.requestFocus();
                amount.setError("Amount cannot be empty");
            }
            else
            {
                finish();
                Intent intent = new Intent(BillDetails.this, AddbillActivity.class);
                intent.putExtra("message", billAmount);
                startActivity(intent);
            }



        }

    }
}
