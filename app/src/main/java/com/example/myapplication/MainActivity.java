package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    MainAdapter mainAdapter;

    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // กำหนด FirebaseRecyclerOptions สำหรับการแสดงข้อมูลใน RecyclerView  รีไซ้เคอร์
        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("kanom"), MainModel.class)
                        .build();
        // สร้าง Adapter และกำหนดข้อมูลและการตั้งค่า RecyclerView
        mainAdapter = new MainAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        // กำหนดการทำงานเมื่อกดปุ่ม Floating Action Button
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override // เรียกเมธอดเพื่อเปิดหน้า AddActivity เพื่อเพิ่มข้อมูลใหม่
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddActivity.class));
            }
        });

    }


    @Override // เริ่มการตรวจสอบการเปลี่ยนแปลงใน FirebaseRecyclerAdapter เมื่อ Activity เริ่มทำงาน
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override // หยุดการตรวจสอบการเปลี่ยนแปลงใน FirebaseRecyclerAdapter เมื่อ Activity หยุดทำงาน
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search,menu); //สร้างเมนูค้นหาโดยเรียกใช้ไฟล์เมนูที่กำหนดไว้ใน R.menu.search
        MenuItem item = menu.findItem(R.id.search);  // หา MenuItem ที่เป็นส่วนของการค้นหา
        SearchView searchView = (SearchView)item.getActionView(); // ดึง SearchView ออกมาจาก MenuItem

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){ // กำหนดการทำงานของ SearchView เมื่อมีการค้นหา


            @Override
            public boolean onQueryTextSubmit(String query) {
                txtsearch(query); // เรียกเมธอด txtsearch() เพื่อค้นหาข้อมูลเมื่อกดปุ่มค้นหา
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                txtsearch(query); // เรียกเมธอด txtsearch() เพื่อค้นหาข้อมูลเมื่อมีการเปลี่ยนแปลงข้อความในช่องค้นหา
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

    private void txtsearch(String str){ // สร้าง FirebaseRecyclerOptions เพื่อกำหนดค่าในการค้นหาข้อมูลใน Firebase Realtime Database

        FirebaseRecyclerOptions<MainModel> options =
                new FirebaseRecyclerOptions.Builder<MainModel>()
                        .setQuery(
                                FirebaseDatabase.getInstance().getReference()
                                        .child("kanom")
                                        .orderByChild("name")   // เรียงลำดับตามชื่อ
                                        .startAt(str)// เริ่มค้นหาที่ชื่อของรายการตรงกับคำค้นหา
                                        .endAt(str+"\uf8ff"),  // สิ้นสุดการค้นหาที่ชื่อของรายการตรงกับคำค้นหา
                                MainModel.class)
                        .build();

        mainAdapter = new MainAdapter(options); // สร้าง Adapter ใหม่จาก FirebaseRecyclerOptions ที่กำหนดขึ้น
        mainAdapter.startListening(); // เริ่มการตรวจสอบการเปลี่ยนแปลงของ Adapter
        recyclerView.setAdapter(mainAdapter);  // กำหนด Adapter ให้กับ RecyclerView เพื่อแสดงผลข้อมูลใหม่

    }
}
