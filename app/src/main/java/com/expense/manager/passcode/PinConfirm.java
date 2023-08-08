package com.expense.manager.passcode;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.expense.manager.R;
import com.expense.manager.Utile.CustomDialog;
import com.expense.manager.smith.BaseActivity;

public class PinConfirm extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText etConfirmPassword;
    String h = "";
    int i = 0;
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

    public void afterTextChanged(Editable editable) {
    }

    public void beforeTextChanged(CharSequence charSequence, int i2, int i22, int i3) {
    }

    @Override
    public void changeUserAccount() {

    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_pinconfirm);
        findViewById();
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
        this.etConfirmPassword = editText;
        editText.addTextChangedListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn0:
                this.h += 0;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.h);
                return;
            case R.id.btn1:
                this.h += 1;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.h);
                return;
            case R.id.btn2:
                this.h += 2;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.h);
                return;
            case R.id.btn3:
                this.h += 3;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.h);
                return;
            case R.id.btn4:
                this.h += 4;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.h);
                return;
            case R.id.btn5:
                this.h += 5;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.h);
                return;
            case R.id.btn6:
                this.h += 6;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.h);
                return;
            case R.id.btn7:
                this.h += 7;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.h);
                return;
            case R.id.btn8:
                this.h += 8;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.h);
                return;
            case R.id.btn9:
                this.h += 9;
                this.etConfirmPassword.setText("");
                this.etConfirmPassword.setText(this.h);
                return;
            default:
                return;
        }
    }

    public void onTextChanged(CharSequence charSequence, int i2, int i22, int i3) {
        int length = this.etConfirmPassword.length();
        this.i = length;
        if (length == 4) {
            if (getIntent().getStringExtra("pin").equals(this.etConfirmPassword.getText().toString())) {
//                startActivity(new Intent(this, EmailConfirm.class));
                CustomDialog.getInstance().SecurityDialog(PinConfirm.this, "confirmscreen", getString(R.string.select_security_question));
                return;
            }
            CustomDialog.getInstance().OtherDialog(PinConfirm.this, getString(R.string.pass_notmatch), getString(R.string.pass_notmatch_info));

        }
    }
}
