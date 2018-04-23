package com.example.dell.sendbird.groupcchannel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dell.sendbird.R;
import com.example.dell.sendbird.utils.TextUtils;

public class InputUserActivity extends AppCompatActivity {

    EditText userId,nickname;
    Button done,old;
    String userIdVal,nicknameVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_user);
        userId=findViewById(R.id.user_id);
        nickname=findViewById(R.id.nick_name);
        done=findViewById(R.id.done);
        //old=findViewById(R.id.old);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userIdVal=userId.getText().toString();
                nicknameVal=nickname.getText().toString();
//                SharedPreferences.Editor editor = getSharedPreferences("logincredentials", MODE_PRIVATE).edit();
//                editor.putString("userIdpref", userIdVal);
//                editor.putString("nicknamepref", nicknameVal);
//                editor.apply();
//                Log.e("user",""+nicknameVal);
//                Log.e("user",""+userIdVal);
                if(userIdVal==null){
                    /**
                     *   You can Toast a message here that the Username is Empty
                     **/
                    userId.setError( "UserId is required!" );

                }
                Intent intent=new Intent(InputUserActivity.this,GroupChannelActivity.class);
                intent.putExtra("userId",userIdVal);
                intent.putExtra("nickname",nicknameVal);
                startActivity(intent);
            }
        });

//        old.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(InputUserActivity.this,GroupChannelActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}
