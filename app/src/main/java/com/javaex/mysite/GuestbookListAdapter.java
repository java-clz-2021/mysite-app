package com.javaex.mysite;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.javaex.vo.GuestbookVo;

import java.util.List;

public class GuestbookListAdapter extends ArrayAdapter<GuestbookVo> {

    private TextView txtNo;
    private TextView txtName;
    private TextView txtRegDate;
    private TextView txtContent;


    //생성자
    public GuestbookListAdapter(Context context, int resource, List<GuestbookVo> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("javaStudy", "position=" + position);


        View view = convertView;

        if(view == null) { //만들어놓은 view 없다  만들어야 한다
            //레이아웃 뻥튀기 기계 받아오기
            LayoutInflater layoutInflater = (LayoutInflater)LayoutInflater.from(getContext());

            //레이아웃 뻥튀기기
            view =layoutInflater.inflate(R.layout.activity_list_item, null);

            Log.d("javaStudy", "틀(뻥튀기)을 새로 만듬");

        }

        //1개의 방명록 데이터가 있다.
        //1개의 데이터 처리(xml 데이터 매칭)
        txtNo = view.findViewById(R.id.txtNo);
        txtName = view.findViewById(R.id.txtName);
        txtRegDate = view.findViewById(R.id.txtRegDate);
        txtContent = view.findViewById(R.id.txtContent);

        //데이터 가져오기(1개데이터)  --> 부모쪽에 전체 리스트가 있다
        GuestbookVo guestbooVo = super.getItem(position);
        txtNo.setText(""+guestbooVo.getNo());
        txtName.setText(guestbooVo.getName());
        txtRegDate.setText(guestbooVo.getRegDate());
        txtContent.setText(guestbooVo.getContent());

        return view;
    }
}
