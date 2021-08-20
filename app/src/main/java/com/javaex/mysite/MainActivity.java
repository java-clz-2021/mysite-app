package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.javaex.vo.GuestbookVo;


public class MainActivity extends AppCompatActivity {
    //필드
    private Button btnWrite;
    private EditText edtName;
    private EditText edtPassword;
    private EditText edtContent;

    //생성자
    //gs
    //일반

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnWrite = (Button)findViewById(R.id.btnWrite);
        edtName = (EditText)findViewById(R.id.edtName);
        edtPassword = (EditText)findViewById(R.id.edtPassword);
        edtContent = (EditText)findViewById(R.id.edtContent);


        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("방명록쓰기");  //xml에서 속성을 찾아서 처리


        //저장버튼을 클릭할때
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("javaStudy", "저장버튼 클릭");

                //방명록데이터를 vo로 만든다
                String name = edtName.getText().toString();
                String password = edtPassword.getText().toString();
                String content = edtContent.getText().toString();

                GuestbookVo guestbookVo = new GuestbookVo(name, password, content);
                Log.d("javaStudy", "vo= " +guestbookVo.toString());

                //서버에 전송하고
                Log.d("javaStudy", "서버전송............ok");

                //리스트 액티비티로 전환
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);

            }
        });




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("javaStudy", "home버튼 클릭");
        Log.d("javaStudy", "item.getItemId()-->"+item.getItemId());
        Log.d("javaStudy", "android.R.id.home-->"+android.R.id.home);
        
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}