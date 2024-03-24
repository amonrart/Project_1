package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {


    EditText name,price,kurl;
    Button btnAdd,btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        // กำหนด EditText และ Button
        name = (EditText)findViewById(R.id.txtlocationname);
        price = (EditText)findViewById(R.id.txtprice);
        kurl = (EditText)findViewById(R.id.txtkurl);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        btnBack = (Button)findViewById(R.id.btnBack);


        //ตั้งค่าปุ่ม Add
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  // เรียกใช้เมธอด insertData() เพื่อเพิ่มข้อมูล
                insertData();
                clearAll();
            }
        });
        //ตั้งค่าปุ่ม Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    // เมธอดสำหรับเพิ่มข้อมูลลงใน Firebase Realtime Database
    private void insertData(){
        // สร้าง Map เพื่อเก็บข้อมูลที่ได้รับจาก EditText
        Map<String,Object> map = new HashMap<>();
        map.put("name",name.getText().toString());
        map.put("price",price.getText().toString());
        map.put("kurl",kurl.getText().toString());
        // เพิ่มข้อมูลลงในโหนด "kanom" ใน Firebase Database
        FirebaseDatabase.getInstance().getReference().child("kanom").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override // แสดงข้อความเมื่อเพิ่มข้อมูลสำเร็จ
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddActivity.this,"Data Insert Successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override // แสดงข้อความเมื่อเพิ่มข้อมูลล้มเหลว
                    public void onFailure(Exception e) {
                        Toast.makeText(AddActivity.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });

    }
    // เมธอดสำหรับล้างข้อมูลใน EditText
    private void clearAll(){
        // เซ็ตข้อความใน EditText เป็นค่าว่าง
        name.setText("");
        price.setText("");
        kurl.setText("");


    }



}