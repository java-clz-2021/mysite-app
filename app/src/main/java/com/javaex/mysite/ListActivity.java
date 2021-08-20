package com.javaex.mysite;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.javaex.vo.GuestbookVo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ListView lvGuestbookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //ListView를 객체화 한다
        lvGuestbookList = (ListView)findViewById(R.id.lvGuestbookList);



        //데이터를 가져온다(서버로부터) -- 임시
        //List<GuestbookVo> guestbookList = getListFromServer();

        //나가서 일해라 (데이터 가져오기  ---> 화면에 그리기(어댑터))
        ListAsyncTask listAsyncTask = new ListAsyncTask();
        listAsyncTask.execute();



        lvGuestbookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //현재 클릭한 뷰의 리스트의 index 값
                Log.d("javaStudy", "index=" + i);

                // 화면에 있는 값을 읽어온다
                TextView txtContent = (TextView)view.findViewById(R.id.txtContent);
                Log.d("javaStudy", "Content=" + txtContent.getText().toString());

                // 화면에 출력되지 않은 데이터를 가져올때 ---> 리스트의 값을 사용할때
                GuestbookVo guestbookVo = (GuestbookVo) adapterView.getItemAtPosition(i);
                Log.d("javaStudy", "vo=" + guestbookVo.toString());
                Log.d("javaStudy", "vo.regDate=" + guestbookVo.getRegDate());

                //클릭한 아이템의 pk값 을 읽어온다
                int no = guestbookVo.getNo();
                Log.d("javaStudy", "vo.no=" + no);

            }
        });

    }




    //이너 클래스
    public class ListAsyncTask extends AsyncTask<Void, Integer, List<GuestbookVo>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //이너클래스
        @Override
        protected List<GuestbookVo> doInBackground(Void... voids) {
            List<GuestbookVo> guestbookList = null;

            //서버에 연결을 한다
            //요청을한다
            try {
                URL url = new URL("http://192.168.0.62:8088/mysite5/api/guestbook/list");  //url 생성

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  //url 연결
                conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                conn.setRequestMethod("POST"); // 요청방식 POST
                conn.setRequestProperty("Content-Type", "application/json"); //요청시 데이터 형식 json
                conn.setRequestProperty("Accept", "application/json"); //응답시 데이터 형식 json
                conn.setDoOutput(true); //OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
                conn.setDoInput(true); //InputStream으로 서버로 부터 응답을 받겠다는 옵션.

                int resCode = conn.getResponseCode(); // 응답코드 200이 정상
                Log.d("javaStudy", ""+resCode);

                if(resCode == 200){ //정상이면

                    //Stream 을 통해 통신한다
                    //데이타 형식은 json으로 한다.
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                    BufferedReader br = new BufferedReader(isr);

                    String jsonData = "";
                    while(true){
                        String line = br.readLine();
                        if(line == null){
                            break;
                        }
                        jsonData = jsonData + line;
                    }

                    //응답을 받는다  json --> java 객체로 변환( List<GuestbookVo> guestbookList)
                    //json-->자바객체
                    Log.d("javaStudy", "jsonData--> "+jsonData);
                    Gson gson = new Gson();
                    guestbookList = gson.fromJson(jsonData, new TypeToken<List<GuestbookVo>>(){}.getType());


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return guestbookList;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<GuestbookVo> guestbookList) {
            Log.d("javaStudy", "onPostExecute()");
            Log.d("javaStudy", "size--> "+guestbookList.size());
            Log.d("javaStudy", "index(0).name--> "+guestbookList.get(0).getName());


            //어댑터를 생성
            GuestbookListAdapter guestbookListAdapter =
                    new GuestbookListAdapter(getApplicationContext(), R.id.lvGuestbookList, guestbookList);

            //리스트뷰에 어댑터를 세팅한다다
            lvGuestbookList.setAdapter(guestbookListAdapter);


            super.onPostExecute(guestbookList);
        }
    }










    //방명록리스트 정보 만들기 (가상)
    public List<GuestbookVo> getListFromServer(){
        List<GuestbookVo> guestbookList = new ArrayList<GuestbookVo>();

        for(int i=1; i<=50; i++ ){
            GuestbookVo guestbookVo = new GuestbookVo();
            guestbookVo.setNo(i);
            guestbookVo.setName("정우성");
            guestbookVo.setRegDate("2021-08-19-" +  i);
            guestbookVo.setContent(i+"번째 본문입니다.");

            guestbookList.add(guestbookVo);
        }

        return guestbookList;
    }


}