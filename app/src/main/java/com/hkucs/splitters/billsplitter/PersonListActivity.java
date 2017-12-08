package com.hkucs.splitters.billsplitter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class PersonListActivity extends ListActivity {
    ArrayList< Map<String, Object> > list = new ArrayList< Map<String,
            Object> >();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        ArrayList<String> roles = intent.getStringArrayListExtra("Roles");
        ArrayList<String> names = intent.getStringArrayListExtra("Names");
        for( int i = 0; i < roles.size(); i++ ){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put( "Role", roles.get(i) );
            map.put( "Name", names.get(i) );
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter( this, list,
                R.layout.activity_person_list,
                new String[]{"Role","Name"},
                new int[]{R.id.role, R.id.name});
        setListAdapter(adapter);
    }
}
