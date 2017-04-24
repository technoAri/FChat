package fchat.mychat.com.fchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    TextView time, senderName;
    ImageView backButton, menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_chatbox);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        senderName = (TextView) findViewById(R.id.messegeName);
        menu = (ImageView) findViewById(R.id.menu);
        backButton = (ImageView) findViewById(R.id.back);
        //time = (TextView) findViewById(R.id.time_display);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://fchat-e3655.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://fchat-e3655.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
                messageArea.setText("");
            }
        });
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox( message, 1);
                }
                else{
                    //UserDetails.chatWith + ":-\n" +
                    addMessageBox(message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ChatActivity.this, menu);
                popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId() == R.id.logout) {
                            SharedPreferences preferences = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
                            preferences.edit().remove("LoggedInState").commit();
                            Intent intent = new Intent(ChatActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
//                        else if (item.getItemId() == R.id.logout) {
//                            SharedPreferences preferences = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
//                            preferences.edit().remove("state").commit();
//                            finish();
//                        }

                        return true;
                    }
                });
                popup.show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void addMessageBox(String message, int type){

//        long timeInMillis = System.currentTimeMillis();
//        Calendar cal1 = Calendar.getInstance();
//        cal1.setTimeInMillis(timeInMillis);
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "dd/MM/yyyy hh:mm:ss a");
//        String dateforrow = dateFormat.format(cal1.getTime()).toString();

        senderName.setText(UserDetails.chatWith);
        TextView textView = new TextView(ChatActivity.this);
        TextView textViewTime = new TextView((ChatActivity.this));
        textView.setText(message);
        textView.setTextSize(18);
        textView.setPadding(10, 10, 10, 10);
        textView.setPaddingRelative(10, 10, 10, 10);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);
        //textViewTime.setText(dateforrow);
        //textViewTime.setLayoutParams(lp);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.rounded_corner1);
            lp.gravity = Gravity.END;
            textView.setTextColor(Color.WHITE);
//            textViewTime.setText(dateforrow);
//            textViewTime.setLayoutParams(lp);
        }
        else{
            textView.setBackgroundResource(R.drawable.rounded_corner2);
            lp.gravity = Gravity.START;
//            textViewTime.setText(dateforrow);
//            textViewTime.setLayoutParams(lp);
        }

        layout.addView(textView);

        scrollView.fullScroll(View.FOCUS_DOWN);

    }
}
