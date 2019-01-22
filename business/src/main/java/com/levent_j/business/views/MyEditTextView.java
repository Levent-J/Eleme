package com.levent_j.business.views;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.levent_j.business.R;

/**
 * @auther : levent_j on 2018/3/10.
 * @desc :
 */

public class MyEditTextView extends RelativeLayout{
    private TextView mTitle;
    private EditText mContent;
    private ICheckRule mCheckRule;

    public MyEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_my_editor,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTitle = findViewById(R.id.tv_edit_title);
        mContent = findViewById(R.id.et_edit_content);
    }

    public void setInputTitle(String title){
        mTitle.setText(title);
    }

    public String getContent(){
        return mContent.getText().toString().trim();
    }

    public void setCheckRule(ICheckRule checkRule){
        this.mCheckRule = checkRule;
    }

    public void setInputType(int type){
        mContent.setInputType(type);
    }

    public void setContent(String s){
        mContent.setText(s);
    }

    //检测输入是否合法
    public boolean checkInputContent(){
        if (mCheckRule==null){
            return true;
        }
        String content = mContent.getText().toString().trim();
        return mCheckRule.check(content);
    }

    public interface ICheckRule{
        boolean check(String s);
    }


}
