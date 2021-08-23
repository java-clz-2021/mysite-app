package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.javaex.vo.GuestbookVo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReadActivity extends AppCompatActivity {

    //필드
    private TextView txtNo;
    private TextView txtName;
    private TextView txtRegDate;
    private TextView txtContent;
    private Button btnGoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        //툴바 관련
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //위젯 객체화 하기
        txtNo = (TextView)findViewById(R.id.txtNo);
        txtName = (TextView)findViewById(R.id.txtName);
        txtRegDate = (TextView)findViewById(R.id.txtRegDate);
        txtContent = (TextView)findViewById(R.id.txtContent);
        btnGoList = (Button)findViewById(R.id.btnGoList);




        //이전 액티비에서 보내준 값을 꺼내온다  no         *name
        Intent intent = getIntent();
        int no = intent.getExtras().getInt("no");
        String name = intent.getExtras().getString("name");

        Log.d("javaStudy", "intent로 받은 no --> " + no);
        Log.d("javaStudy", "intent로 받은 name --> " + name);

        int no2 = 100;
        int no3 = 500;

        //서버(스프링)로 (29)번 정보를 요청한다.
        //가져온정보를 출력한다
        // -------------------------> 출장보낸다(AsyncTask)
        ReadAsyncTask readAsyncTask = new ReadAsyncTask();
        readAsyncTask.execute(no);
        //readAsyncTask.execute(no, no2, no3);


        ///////////////////////////////////////////////////////////
        //버튼 클릭이벤트
        //////////////////////////////////////////////////////////
        btnGoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("javaStudy", "리스트로 이동 버튼 클릭");
                finish();
            }
        });




    }

    //이너클래스
    public class ReadAsyncTask extends AsyncTask<Integer , Integer, GuestbookVo>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GuestbookVo doInBackground(Integer... noArray) {
            Log.d("javaStudy", "doInBackground()");
            Log.d("javaStudy", "no-->" + noArray[0]);
            //Log.d("javaStudy", "no2-->" + noArray[1]);
            //Log.d("javaStudy", "no3-->" + noArray[2]);

            //Gson 메모리에 올리기 (요청 응답 모두 사용)
            Gson gson = new Gson();

            //보내는 GuestbookVo
            GuestbookVo requestVo = null;

            //받는 GuestbookVo
            GuestbookVo responseVo = null;

            /////////////////////////////////////////////////////////
            //요청 관리 업무
            ////////////////////////////////////////////////////////

            //요청 준비
            int no = noArray[0];
            requestVo = new GuestbookVo();
            requestVo.setNo(no);

            //vo --> json 변경
            String requestJson = gson.toJson(requestVo);
            Log.d("javaStudy", "requestJson-->" + requestJson);

            //요청 --> requestBody에 requestJson 넣어서 요청한다.
            try {
                URL url = new URL("http://192.168.0.223:8088/mysite5/api/guestbook/read");  //url 생성

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  //url 연결
                conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST"); // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); //요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); //응답시 데이터 형식 json
                conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                //보내는 스트림 OutputStream(json ---> requestBody }
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                BufferedWriter bw = new BufferedWriter(osw);


                //쓰기(보내기)
                bw.write(requestJson);
                bw.flush();

                int resCode = conn.getResponseCode(); // 응답코드 200이 정상
                Log.d("javaStudy", "resCode-->" + resCode);

                if(resCode == 200){ //정상이면

                    //받는 스트림 InputStram(responseBody(json) --> 자바객체)
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);


                    //Stream 을 통해 읽는다
                    // 데이타 형식은 json으로 한다.
                    String responseJson = "";
                    while(true){
                        String line = br.readLine();
                        if(line == null ){
                            break;
                        }
                        responseJson = responseJson + line;
                    }

                    //json문자열 확인
                    Log.d("javaStudy", "responseJson-->" + responseJson);

                    //json --> GuestbookVo
                    responseVo = gson.fromJson(responseJson, GuestbookVo.class);
                    Log.d("javaStudy", "responseVo-->" + responseVo);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return responseVo;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(GuestbookVo guestbookVo) {
            super.onPostExecute(guestbookVo);
            Log.d("javaStudy", "onPostExecute()");
            Log.d("javaStudy", "guestbookVo-->" + guestbookVo);

            //화면에 출력
            txtNo.setText(""+guestbookVo.getNo());
            txtName.setText(guestbookVo.getName());
            txtRegDate.setText(guestbookVo.getRegDate());
            txtContent.setText(guestbookVo.getContent());
            
        }
    }

}
