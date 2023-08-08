package com.expense.manager.passcode;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.expense.manager.R;
import com.expense.manager.Utile.CustomDialog;
import com.expense.manager.Utile.Preferences;
import com.expense.manager.smith.BaseActivity;
import com.expense.manager.smith.HomeActivity;
import com.expense.manager.smith.MyDatabaseHandler;
import com.expense.manager.Model.bean.AccountBean;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText etPasswordLogin;
    MyDatabaseHandler h;
    String i = "";
    private ImageButton ivBtnEight;
    private ImageButton ivBtnFive;
    private ImageButton ivBtnFour;
    private ImageButton ivBtnNine;
    private ImageButton ivBtnOne;
    private ImageButton ivBtnSeven;
    private ImageButton ivBtnSix;
    private ImageButton ivBtnThree;
    private ImageButton ivBtnTwo;
    private ImageButton ivBtnZero;
    int j = 0;
    List<AccountBean> k;
    private TextView tvForgot;
    private TextView tvReset;

    public LoginActivity() {
        new ArrayList();
    }

    public void afterTextChanged(Editable editable) {
    }

    public void beforeTextChanged(CharSequence charSequence, int i2, int i22, int i3) {
    }

    @Override
    public void changeUserAccount() {

    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_login);
        findViewById();
        MyDatabaseHandler myDatabaseHandler = new MyDatabaseHandler(this);
        this.h = myDatabaseHandler;
        this.k = myDatabaseHandler.getAccountList();
        Preferences instance = Preferences.getInstance();
        Preferences.getInstance().getClass();
        instance.getPrefValue("pref_pin");
    }

    private void findViewById() {
        this.ivBtnOne = (ImageButton) findViewById(R.id.btn1);
        this.ivBtnTwo = (ImageButton) findViewById(R.id.btn2);
        this.ivBtnThree = (ImageButton) findViewById(R.id.btn3);
        this.ivBtnFour = (ImageButton) findViewById(R.id.btn4);
        this.ivBtnFive = (ImageButton) findViewById(R.id.btn5);
        this.ivBtnSix = (ImageButton) findViewById(R.id.btn6);
        this.ivBtnSeven = (ImageButton) findViewById(R.id.btn7);
        this.ivBtnEight = (ImageButton) findViewById(R.id.btn8);
        this.ivBtnNine = (ImageButton) findViewById(R.id.btn9);
        this.ivBtnZero = (ImageButton) findViewById(R.id.btn0);
        this.ivBtnOne.setOnClickListener(this);
        this.ivBtnTwo.setOnClickListener(this);
        this.ivBtnThree.setOnClickListener(this);
        this.ivBtnFour.setOnClickListener(this);
        this.ivBtnFive.setOnClickListener(this);
        this.ivBtnSix.setOnClickListener(this);
        this.ivBtnSeven.setOnClickListener(this);
        this.ivBtnEight.setOnClickListener(this);
        this.ivBtnNine.setOnClickListener(this);
        this.ivBtnZero.setOnClickListener(this);
        EditText editText = (EditText) findViewById(R.id.et_password);
        this.etPasswordLogin = editText;
        editText.addTextChangedListener(this);
        TextView textView = (TextView) findViewById(R.id.tv_forgot);
        this.tvForgot = textView;
        textView.setOnClickListener(this);
        TextView textView2 = (TextView) findViewById(R.id.tv_reset);
        this.tvReset = textView2;
        textView2.setOnClickListener(this);
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.tv_reset) {
            switch (id) {
                case R.id.btn0:
                    this.i += 0;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.i);
                    return;
                case R.id.btn1:
                    this.i += 1;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.i);
                    return;
                case R.id.btn2:
                    this.i += 2;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.i);
                    return;
                case R.id.btn3:
                    this.i += 3;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.i);
                    return;
                case R.id.btn4:
                    this.i += 4;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.i);
                    return;
                case R.id.btn5:
                    this.i += 5;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.i);
                    return;
                case R.id.btn6:
                    this.i += 6;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.i);
                    return;
                case R.id.btn7:
                    this.i += 7;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.i);
                    return;
                case R.id.btn8:
                    this.i += 8;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.i);
                    return;
                case R.id.btn9:
                    this.i += 9;
                    this.etPasswordLogin.setText("");
                    this.etPasswordLogin.setText(this.i);
                    return;
                case R.id.tv_forgot:
                    CustomDialog.getInstance().SecurityDialog(LoginActivity.this, "loginscreen", getString(R.string.select_security_question));
                    return;
                default:
                    return;
            }
        } else {
            this.i = "";
            this.etPasswordLogin.setText("");
        }
    }

    public void onTextChanged(CharSequence charSequence, int i2, int i22, int i3) {
        int length = this.etPasswordLogin.length();
        this.j = length;
        if (length == 4) {
            Preferences instance = Preferences.getInstance();
            Preferences.getInstance().getClass();
            if (instance.getPrefValue("pref_pin").equals(this.etPasswordLogin.getText().toString())) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return;
            }
            this.etPasswordLogin.setText("");
            this.i = "";
            CustomDialog.getInstance().ErrorDialog(this,  getString(R.string.wrong_pass), getString(R.string.wrong_pass_info));
        }
    }

    public boolean onKeyDown(int i2, KeyEvent keyEvent) {
        if (i2 == 4 && Build.VERSION.SDK_INT >= 16) {
            finishAffinity();
        }
        return super.onKeyDown(i2, keyEvent);
    }
}
