package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainAdapter extends FirebaseRecyclerAdapter<MainModel,MainAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MainModel> options) {
        super(options);
    }

    @Override // กำหนดค่าของรายการใน ViewHolder ของ RecyclerView
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull MainModel model) {
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice());

        // กำหนดข้อความชื่อและราคาสำหรับแสดงใน TextView
        Glide.with(holder.img.getContext())
                .load(model.getKurl())
                .placeholder(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark)
                .circleCrop()
                .error(com.firebase.ui.database.R.drawable.common_google_signin_btn_icon_dark_normal)
                .into(holder.img);
        // กำหนดการทำงานเมื่อปุ่มแก้ไขถูกคลิก
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override // สร้าง DialogPlus เพื่อแสดงหน้าต่างแก้ไขข้อมูล
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1200)
                        .create();

                //dialogPlus.show();
                // ดึง View ออกมาจาก DialogPlus
                View view = dialogPlus.getHolderView();
                // กำหนดข้อมูลใน EditText ในหน้าต่างแก้ไข
                EditText name = view.findViewById(R.id.txtlocationname);
                EditText price = view.findViewById(R.id.txtprice);
                EditText kurl = view.findViewById(R.id.txtkurl);

                Button btnUpdate = view.findViewById(R.id.btnUpdate);
                // กำหนดข้อมูลใน EditText ด้วยข้อมูลของรายการที่ถูกคลิก
                name.setText(model.getName());
                price.setText(model.getPrice());
                kurl.setText(model.getKurl());
                // แสดงหน้าต่างแก้ไขข้อมูล
                dialogPlus.show();
                // กำหนดการทำงานเมื่อปุ่มอัปเดตถูกคลิก
                btnUpdate.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // สร้าง Map เพื่อเก็บข้อมูลที่อัปเดต
                        Map<String,Object> map = new HashMap<>();
                        map.put("name",name.getText().toString());
                        map.put("price",price.getText().toString());
                        map.put("kurl",kurl.getText().toString());
                        // อัปเดตข้อมูลใน Firebase Realtime Database
                        FirebaseDatabase.getInstance().getReference().child("kanom")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override // แสดงข้อความเมื่ออัปเดตข้อมูลสำเร็จและปิดหน้าต่างแก้ไขข้อมูล
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.name.getContext(), "Data Update Successfully", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }

                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override // แสดงข้อความเมื่อเกิดข้อผิดพลาดในการอัปเดตข้อมูลและปิดหน้าต่างแก้ไขข้อมูล
                                    public void onFailure(Exception e) {
                                        Toast.makeText(holder.name.getContext(), "Error Update", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }

                                });

                    }

                });


            }
        });
        // กำหนดการทำงานเมื่อปุ่ม "ลบ" ถูกคลิกใน ViewHolder ของ RecyclerView
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override // สร้าง AlertDialog เพื่อยืนยันการลบข้อมูล  อเลิ้ต
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are You Sure?");
                builder.setMessage("Deleted");
                // กำหนดปุ่ม "ลบ" เพื่อลบข้อมูลใน Firebase Realtime Database
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("kanom")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                // กำหนดปุ่ม "ยกเลิก" เพื่อยกเลิกการลบข้อมูล
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override // แสดงข้อความเมื่อยกเลิกการลบข้อมูล
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.name.getContext(),"Canceled",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show(); // แสดง AlertDialog

            }
        });

    }

    @NonNull
    @Override   // สร้าง ViewHolder ด้วยการเตรียมเลเอาท์ของรายการใน RecyclerView
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,parent,false);
        return new myViewHolder(view);
    }
    // คลาส ViewHolder ที่ใช้เพื่อเก็บ View แต่ละรายการใน RecyclerView
    class myViewHolder extends RecyclerView.ViewHolder{

        CircleImageView img;
        TextView name,price;

        Button btnEdit,btnDelete;


        public myViewHolder(@NonNull View itemView) {
            super(itemView); // กำหนดความสัมพันธ์ของ View ใน ViewHolder
            img = (CircleImageView)itemView.findViewById(R.id.img1);
            name = (TextView)itemView.findViewById(R.id.nametext);
            price = (TextView)itemView.findViewById(R.id.pricetext);


            btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button)itemView.findViewById(R.id.btnDelete);

        }
    }
}
