package com.example.chickens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText name, egg_num;
    Spinner type;
    Button add_btn, all_btn, num_btn, next_btn;
    ListView lv_chickenList;
    TextView avg_text;
    String type_text = "";

    DataBaseHelper dataBaseHelper;
    ArrayAdapter chickenArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.nameEditText);
        egg_num = findViewById(R.id.eggNumEditText);
        add_btn = findViewById(R.id.add_btn);
        all_btn = findViewById(R.id.all_btn);
        num_btn = findViewById(R.id.num_btn);
        next_btn = findViewById(R.id.next_btn);
        avg_text = findViewById(R.id.avg_text);
        lv_chickenList = findViewById(R.id.lv_chickenList);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        ShowChickenOnListView(dataBaseHelper);

        avg_text.setText("Середня кількіть яєць яєчних пород " + dataBaseHelper.getAverage());

        type = findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.chicken_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        type.setOnItemSelectedListener(this);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChickenModel chickenModel;
                try{
                    chickenModel = new ChickenModel(-1, name.getText().toString(), Integer.parseInt(egg_num.getText().toString()), type_text);
                    Toast.makeText(MainActivity.this, chickenModel.toString(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    chickenModel = new ChickenModel(-1, "error", 0, "no type");
                }

                dataBaseHelper = new DataBaseHelper(MainActivity.this);
                boolean success = dataBaseHelper.addOne(chickenModel);
                Toast.makeText(MainActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
                ShowChickenOnListView(dataBaseHelper);
                avg_text.setText("Середня кількіть яєць яєчних пород " + dataBaseHelper.getAverage());
            }
        });
        all_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                List<ChickenModel> allChicken = dataBaseHelper.getAll();

                ShowChickenOnListView(dataBaseHelper);
                //Toast.makeText(MainActivity.this, allChicken.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        num_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
                List<ChickenModel> moreChicken = dataBaseHelper.getMoreThen();

                chickenArrayAdapter = new ArrayAdapter<ChickenModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getMoreThen());
                lv_chickenList.setAdapter(chickenArrayAdapter);
            }
        });

        lv_chickenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChickenModel clickedChicken = (ChickenModel) adapterView.getItemAtPosition(i);
                dataBaseHelper.deleteOne(clickedChicken);
                ShowChickenOnListView(dataBaseHelper);
                Toast.makeText(MainActivity.this, "Deleted " + clickedChicken.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    private void ShowChickenOnListView(DataBaseHelper dataBaseHelper) {
        chickenArrayAdapter = new ArrayAdapter<ChickenModel>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.getAll());
        lv_chickenList.setAdapter(chickenArrayAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type_text = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), type_text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}