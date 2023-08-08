package com.expense.manager.passcode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.expense.manager.R;
import com.expense.manager.Utile.Preferences;
import com.expense.manager.smith.HomeActivity;
import com.expense.manager.smith.MyDatabaseHandler;
import com.expense.manager.Model.bean.AccountBean;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class EmailConfirm extends AppCompatActivity implements View.OnClickListener {
    private Button btnContinue;
    private EditText etAccEmail;
    List<AccountBean> h = new ArrayList();
    MyDatabaseHandler i;

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_emailconfirm);
        this.etAccEmail = (EditText) findViewById(R.id.etAccountEmail);
        Button button = (Button) findViewById(R.id.btn_continue);
        this.btnContinue = button;
        button.setOnClickListener(this);
        this.i = new MyDatabaseHandler(this);
    }

    public void onClick(View view) {
        if (view.getId() != R.id.btn_continue) {
            return;
        }
        if (!checkmail(this.etAccEmail.getText().toString())) {
            Toast.makeText(this, "Enter valid email.", 0).show();
            return;
        }
        List<AccountBean> accountList = this.i.getAccountList();
        this.h = accountList;
        if (accountList.get(0).getEmail().equals(this.etAccEmail.getText().toString())) {
            Preferences.getInstance().setFirstTimeLaunch(true);
            Preferences.getInstance().setLoginStatus(true);
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }
        Toast.makeText(this, "Email not match with before enter email", 0).show();
    }

    private boolean checkmail(String str) {
        if (str == null || str.equalsIgnoreCase("")) {
            return false;
        }
        return Pattern.compile(".+@.+\\.[a-z]+").matcher(str).matches();
    }
}
