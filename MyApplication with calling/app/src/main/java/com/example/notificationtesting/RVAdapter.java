package com.example.notificationtesting;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    Context con;

    public List<notificationreceived> notirev;
    String phonenumber = "";
    String message = "";
    String dateandtime = "";
    NotificationActivity nv;
    Boolean ring=false;
    Boolean callReceived=false;
    Boolean idle = false;
    String link;

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        RVAdapter rva = new RVAdapter();
        public CardView cv;
        TextView phonenumber;
        TextView message;
        TextView date;
        Button call;

        //List<notificationreceived> compclass1;
        PersonViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            phonenumber = (TextView) itemView.findViewById(R.id.phonenumber);
            phonenumber.setSingleLine(true);
            call = (Button) itemView.findViewById(R.id.call);
            date = (TextView) itemView.findViewById(R.id.date);
            //message = (TextView)itemView.findViewById(R.id.message);
            //nv = new notificationreceived();
            //this.compclass1 = PersonViewHolder.this.rva.compclass;
        }

    }

    public RVAdapter(Context context) {
        this.con = context;
    }

    public RVAdapter(List<notificationreceived> persons) {
        this.notirev = persons;
    }

    RVAdapter() {

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PersonViewHolder personViewHolder, final int i) {

        JSONObject jsonobj;//=new JSONObject();
        try {
            jsonobj = new JSONObject(notirev.get(i).Message);
        } catch (Exception e) {
            jsonobj = new JSONObject();
        }


        if (jsonobj.has("phone"))
            try {
                phonenumber = (jsonobj.getString("phone"));
                message = (jsonobj.getString("message"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        personViewHolder.phonenumber.setText(phonenumber);
        //personViewHolder.message.setText(message);
        dateandtime = notirev.get(i).Date + " (" + notirev.get(i).Time + ")";

        personViewHolder.date.setText(dateandtime);

        if (notirev.get(i).status.equals("new"))
            personViewHolder.cv.setCardBackgroundColor(Color.parseColor("#f5c700"));

        else if (notirev.get(i).status.equals("inprogress"))
        {
            personViewHolder.cv.setCardBackgroundColor(Color.parseColor("#ff6600"));
            personViewHolder.call.setText("Close");
        }

        else if (notirev.get(i).status.equals("closed"))
        {
            personViewHolder.cv.setCardBackgroundColor(Color.parseColor("#c12552"));
            //personViewHolder.call.setVisibility(View.GONE);
        }

        final int t=i;

        personViewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(personViewHolder.call.getText().equals("Close"))
                {
                    DatabaseReference ref_notification = FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Notifications/"+notirev.get(t).link+"/");
                    //DatabaseReference ref1 = ref_notification.push();
                    ref_notification.child("status").setValue("closed");
                }
                else {
                    Log.i("check", "sghjh");
                    // String number = "7777777777";
                    link = notirev.get(t).link;
                    TelephonyManager TelephonyMgr = (TelephonyManager) v.getContext().getSystemService(Context.TELEPHONY_SERVICE);
                    TelephonyMgr.listen(new TeleListener(),
                            PhoneStateListener.LISTEN_CALL_STATE);
                    Uri call = Uri.parse("tel:" + phonenumber);
                    Intent surf = new Intent(Intent.ACTION_CALL, call);
                    v.getContext().startActivity(surf);


                    // Intent intent = new Intent(Intent.ACTION_CALL);

                    //intent.setData(Uri.parse(phonenumber));
                    if (ActivityCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                }

            }
        });

        /*personViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                builder1.setTitle(phonenumber);
                builder1.setMessage(message+System.getProperty("line.separator")+System.getProperty("line.separator")+ Html.fromHtml("<b>"+dateandtime+"</b>"));
                builder1.setCancelable(false);
                builder1.setPositiveButton("Call",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                    alert11.show();
            }
        });*/

    }

    class TeleListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);

            switch (state) {

                case TelephonyManager.CALL_STATE_IDLE:

                    DatabaseReference ref_notification = FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Notifications/"+link+"/");
                    //DatabaseReference ref1 = ref_notification.push();
                    ref_notification.child("status").setValue("closed");

                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK:
                    DatabaseReference ref_notification1 = FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Notifications/"+link+"/");
                    //DatabaseReference ref1 = ref_notification.push();
                    ref_notification1.child("status").setValue("inprogress");
                    callReceived = true;

                    break;

                case TelephonyManager.CALL_STATE_RINGING:
                    ring = true;
                    DatabaseReference ref_notification3 = FirebaseDatabase.getInstance().getReference("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Notifications/"+link+"/");
                    //DatabaseReference ref1 = ref_notification.push();
                    ref_notification3.child("status").setValue("inprogress");
                    break;

                default:
                    break;
            }
        }

    }
    @Override
    public int getItemCount() {
        return notirev.size();
    }
}
