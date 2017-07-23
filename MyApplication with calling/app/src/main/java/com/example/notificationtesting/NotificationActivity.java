package com.example.notificationtesting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView rv;
    RVAdapter adapter;
    private Paint p = new Paint();
    public notificationreceived noti_rev;
    List<String> notilist= new ArrayList<String>();
    Boolean ring=false;
    Boolean callReceived=false;
    Boolean idle = false;


    List<notificationreceived> datalist= new ArrayList<>();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.newnotification:
                    String amg="users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Notifications/";
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(amg);
                    //contactList = ref.getKey();
                    ref.addValueEventListener(new ValueEventListener() {

                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //  notilist.clear();
                            datalist.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                // notilist.add(String.valueOf(ds.getKey()));
                                noti_rev = ds.getValue(notificationreceived.class);
                                if(noti_rev.status.equals("new"))
                                datalist.add(new notificationreceived(noti_rev.Date,noti_rev.Message,noti_rev.status,noti_rev.Time,noti_rev.link));

                            }
                            displaylist(datalist);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                    return true;
                case R.id.inprogressnotifications:
                    String amg1="users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Notifications/";
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference(amg1);
                    //contactList = ref.getKey();
                    ref1.addValueEventListener(new ValueEventListener() {

                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //  notilist.clear();
                            datalist.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                // notilist.add(String.valueOf(ds.getKey()));
                                noti_rev = ds.getValue(notificationreceived.class);
                                if(noti_rev.status.equals("inprogress"))
                                    datalist.add(new notificationreceived(noti_rev.Date,noti_rev.Message,noti_rev.status,noti_rev.Time,noti_rev.link));

                            }
                            displaylist(datalist);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                    return true;
                case R.id.closednotifications:
                    String amg2="users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Notifications/";
                    DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference(amg2);
                    //contactList = ref.getKey();
                    ref2.addValueEventListener(new ValueEventListener() {

                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //  notilist.clear();
                            datalist.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren())
                            {
                                // notilist.add(String.valueOf(ds.getKey()));
                                noti_rev = ds.getValue(notificationreceived.class);
                                if(noti_rev.status.equals("closed"))
                                    datalist.add(new notificationreceived(noti_rev.Date,noti_rev.Message,noti_rev.status,noti_rev.Time,noti_rev.link));

                            }
                            displaylist(datalist);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);

        rv=(RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);



        final FirebaseDatabase fdatabase = FirebaseDatabase.getInstance();
        String amg="users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Notifications/";
        // Toast.makeText(getApplicationContext(),amg,Toast.LENGTH_SHORT).show();
        DatabaseReference ref = fdatabase.getReference(amg);
        //contactList = ref.getKey();
        ref.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
              //  notilist.clear();
                datalist.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                   // notilist.add(String.valueOf(ds.getKey()));
                    noti_rev = ds.getValue(notificationreceived.class);
                    datalist.add(new notificationreceived(noti_rev.Date,noti_rev.Message,noti_rev.status,noti_rev.Time,noti_rev.link));

                }
               displaylist(datalist);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    //adapter.removeItem(position);
                } else {
                    //removeView();
                    //edit_position = position;
                    //alertDialog.setTitle("Edit Country");
                    //et_country.setText(countries.get(position));
                   // alertDialog.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_call_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                       // c.drawBitmap(icon,null,icon_dest,p);
                        if(icon != null) c.drawBitmap(icon,null,icon_dest,p);
                        if(dX>250) dX=0;

                    } else if(dX < 0) {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_black_24dp);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);

                        if(icon != null)
                            c.drawBitmap(icon,null,icon_dest,p);
                        if(dX<-250) dX=0;

                    }

                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }
    public void displaylist(List<notificationreceived> incoming)
    {
        adapter = new RVAdapter(incoming);
        rv.setAdapter(adapter);
        //initSwipe();
    }



        public boolean onSupportNavigateUp(){


        finish();
        return true;
    }
}
