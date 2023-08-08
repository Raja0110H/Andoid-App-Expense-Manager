package com.expense.manager.Utile;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.expense.manager.R;
import com.expense.manager.smith.HomeActivity;
import com.expense.manager.passcode.PinEnterActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomDialog {
    private static CustomDialog INSTANCE = new CustomDialog();
    AlertDialog a;

    public interface DismissListenerWithStatus {
        void onDismissed(String str);
    }

    public static CustomDialog getInstance() {
        return INSTANCE;
    }

    public void ErrorDialog(Context context, String s1, String s2) {
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setView(view);

        Dialog dialog = builder.create();


        TextView sub_title = view.findViewById(R.id.sub_title);
        TextView main_title = view.findViewById(R.id.main_title);

        TextView btn_ok = view.findViewById(R.id.btn_ok);

        main_title.setText(s1);
        sub_title.setText(s2);

        dialog.show();


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.setCancelable(true);

        dialog.show();
    }

    public void OtherDialog(Activity context, String s1, String s2) {

        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setView(view);

        Dialog dialog = builder.create();


        TextView sub_title = view.findViewById(R.id.sub_title);
        TextView main_title = view.findViewById(R.id.main_title);

        TextView btn_ok = view.findViewById(R.id.btn_ok);
        dialog.setCanceledOnTouchOutside(false);
        main_title.setText(s1);
        sub_title.setText(s2);

        dialog.show();


        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, PinEnterActivity.class));
                context.finish();

            }
        });
        dialog.setCancelable(true);

        dialog.show();


    }

    public void SecurityDialog(Activity context, String s1, String s2) {


        final int[] mQuestionIndex = {0};
        View view = LayoutInflater.from(context).inflate(R.layout.sequrityquestion, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setView(view);

        Dialog dialog = builder.create();

        Spinner mSpinner = view.findViewById(R.id.sub_spiner);
        List myList = new ArrayList();
        Collections.addAll(myList, context.getResources().getStringArray(R.array.security));
        ArrayAdapter<String> gameKindArray = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, myList);
        gameKindArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(gameKindArray);


        EditText mEditText = view.findViewById(R.id.ans_security);

        TextView main_title = view.findViewById(R.id.main_title);
        TextView btn_cancel = view.findViewById(R.id.btn_cancel);
        TextView btn_ok = view.findViewById(R.id.btn_ok);
        dialog.setCanceledOnTouchOutside(false);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mQuestionIndex[0] = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        main_title.setText(s2);
        dialog.show();


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        if (s1.equals("loginscreen")) {
            mSpinner.setSelection(LocaleHelper.getSequrity_question(context));
        } else {
        }
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditText.getText().toString().length() > 0) {

                    if (s1.equals("loginscreen")) {
                        if (mQuestionIndex[0] != 0) {
//                            mSpinner.setSelection(LocaleHelper.getSequrity_question(context));
                            if (mQuestionIndex[0] == LocaleHelper.getSequrity_question(context) && mEditText.getText().toString().equalsIgnoreCase(LocaleHelper.getSequrityAnswer(context))) {
                                LocaleHelper.setIsPinSet(context, false);
                                context.startActivity(new Intent(context, HomeActivity.class));
                                Toast.makeText(context, R.string.sequrity_ans_ok, Toast.LENGTH_SHORT).show();

                                context.finish();
                                return;
                            } else {
                                Toast.makeText(context, R.string.ans_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, R.string.select_security_question, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (mQuestionIndex[0] != 0) {
                            LocaleHelper.setIsPinSet(context, true);

                            LocaleHelper.setSequrity_question(context, mQuestionIndex[0]);
                            LocaleHelper.setSequrityAnswer(context, mEditText.getText().toString());
                            context.startActivity(new Intent(context, HomeActivity.class));
                            Toast.makeText(context, R.string.your_ans_saved, Toast.LENGTH_SHORT).show();

                            context.finish();
                            return;
                        }else {
                            Toast.makeText(context, R.string.select_security_question, Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(context, context.getResources().getString(R.string.enter_your_ans), Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialog.setCancelable(true);

        dialog.show();


    }
}
