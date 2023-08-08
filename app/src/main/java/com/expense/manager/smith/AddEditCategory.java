package com.expense.manager.smith;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.cardview.widget.CardView;

import com.expense.manager.R;
import com.expense.manager.Model.bean.AccountBean;
import com.expense.manager.Model.bean.CategoryBean;
import com.expense.manager.widget.CircleImageView;
import java.util.List;

public class AddEditCategory extends BaseActivity implements View.OnClickListener {
    private List<AccountBean> accountBean;
    private CardView cardSpentLimit;
    /* access modifiers changed from: private */
    public CircleImageView catColor;
    /* access modifiers changed from: private */
    public EditText catName;
    /* access modifiers changed from: private */
    public CategoryBean categoryBean;
    /* access modifiers changed from: private */
    public EditText edtLimit;
    /* access modifiers changed from: private */
    public Spinner spinner1;
    private TextView tvCancel;
    private TextView tvDone;

    public void changeUserAccount() {
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onCreate(Bundle bundle) {
        this.k = false;
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_manage_category);
//        AdAdmob adAdmob = new AdAdmob(this);
//        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.bannerAd), this);
//        adAdmob.FullscreenAd(this);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.app_name) + "</font>"));
        this.catColor = (CircleImageView) findViewById(R.id.cat_color);
        this.catName = (EditText) findViewById(R.id.cat_name);
        this.edtLimit = (EditText) findViewById(R.id.edt_limit);
        this.spinner1 = (Spinner) findViewById(R.id.spinner1);
        this.tvDone = (TextView) findViewById(R.id.btn_add_category);
        this.tvCancel = (TextView) findViewById(R.id.btn_cancle);
        this.cardSpentLimit = (CardView) findViewById(R.id.card_spentlimit);
        String currentAccount = MyUtils.getCurrentAccount(this.h);
        this.accountBean = this.j.getAccountList();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, this.accountBean);
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinner1.setAdapter(arrayAdapter);
        this.spinner1.setSelection(Integer.parseInt(currentAccount) - 1);
        this.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.catColor.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int i = extras.getInt(TAG.CATEGORY, 1);
            int i2 = extras.getInt(TAG.DATA, 0);
            if (i2 != 0) {
                MyDatabaseHandler myDatabaseHandler = this.j;
                CategoryBean categoryModelById = myDatabaseHandler.getCategoryModelById(i, "" + i2);
                this.categoryBean = categoryModelById;
                categoryModelById.setDrag_id(this.j.getDragIdfromCategoty(i2, categoryModelById.getAccountRef()));
            } else {
                this.categoryBean = new CategoryBean();
                String[] strArr = MyUtils.colorcode;
                double length = (double) strArr.length;
                double random = Math.random();
                Double.isNaN(length);
                String str = strArr[(int) (length * random)];
                this.categoryBean.setCategoryGroup(i);
                this.categoryBean.setColor(str);
                this.categoryBean.setDrag_id(this.j.getCategoryCount(MyDatabaseHandler.Category.TABLE_NAME, i, MyUtils.getCurrentAccount(this.h)));
            }
            if (i == 1) {
                this.cardSpentLimit.setVisibility(View.VISIBLE);
            }
        }
        this.catColor.setColorFilter(Color.parseColor(this.categoryBean.getColor()));
        this.catColor.setBorderColor(Color.parseColor(this.categoryBean.getColor()));
        this.catColor.setCircleBackgroundColor(Color.parseColor(this.categoryBean.getColor()));
        this.catName.setText(this.categoryBean.getName());
        this.edtLimit.setText(this.categoryBean.getCategoryLimit());
        n();
        this.tvDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String obj = AddEditCategory.this.catName.getText().toString();
                String obj2 = AddEditCategory.this.edtLimit.getText().toString();
                if (TextUtils.isEmpty(obj)) {
                    AddEditCategory addEditCategory = AddEditCategory.this;
                    Toast.makeText(addEditCategory.h, addEditCategory.getString(R.string.msg_please_enter_category_name), 0).show();
                    return;
                }
                if (obj2.isEmpty()) {
                    obj2 = "0";
                }
                AddEditCategory.this.categoryBean.setName(obj);
                AddEditCategory.this.categoryBean.setCategoryLimit(obj2);
                AddEditCategory.this.categoryBean.setAccountRef(AddEditCategory.this.i.getId());
                AddEditCategory addEditCategory2 = AddEditCategory.this;
                addEditCategory2.j.addUpdateCategoryData(addEditCategory2.categoryBean);
                String currentAccount2 = MyUtils.getCurrentAccount(AddEditCategory.this.h);
                String obj3 = AddEditCategory.this.spinner1.getSelectedItem().toString();
                AccountBean accountModelByName = AddEditCategory.this.j.getAccountModelByName(obj3);
                if (!AddEditCategory.this.j.getAccountModelById(currentAccount2).getName().equals(obj3) && accountModelByName.getName().equals(obj3)) {
                    Bundle extras2 = AddEditCategory.this.getIntent().getExtras();
                    int i3 = extras2.getInt(TAG.CATEGORY, 1);
                    int categoryCount = AddEditCategory.this.j.getCategoryCount(MyDatabaseHandler.Category.TABLE_NAME, i3, accountModelByName.getId());
                    int i4 = extras2.getInt(TAG.DATA, 0);
                    AddEditCategory addEditCategory3 = AddEditCategory.this;
                    addEditCategory3.j.addUpdateCategoryByDrod_IdtoOthersMove(addEditCategory3.categoryBean.getDrag_id(), i3, currentAccount2);
                    AddEditCategory.this.j.UpdateMoveCategory(String.valueOf(i4), accountModelByName.getId());
                    AddEditCategory.this.j.UpdateManagerAccount_id(i4, accountModelByName.getId());
                    AddEditCategory.this.j.UpdateRecirringAccount_id(i4, accountModelByName.getId());
                    AddEditCategory.this.j.UpdateDragIdMoveCategory(String.valueOf(i4), categoryCount);
                }
                AddEditCategory.this.finish();
            }
        });
        this.tvCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddEditCategory.this.deleteCategory();
            }
        });
    }

    public void onClick(View view) {
        if (view.getId() == R.id.cat_color) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View inflate = getLayoutInflater().inflate(R.layout.dialog_color_piker, (ViewGroup) null);
            builder.setView(inflate);
            builder.setCancelable(true);
            builder.setTitle("Select Color");
            ColorPikerAdapter colorPikerAdapter = new ColorPikerAdapter(this.h, MyUtils.colorcode);
            GridView gridView = (GridView) inflate.findViewById(R.id.color_list);
            final AlertDialog create = builder.create();
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                    AddEditCategory.this.catColor.setColorFilter(Color.parseColor(MyUtils.colorcode[i]));
                    AddEditCategory.this.catColor.setBorderColor(Color.parseColor(MyUtils.colorcode[i]));
                    AddEditCategory.this.catColor.setCircleBackgroundColor(Color.parseColor(MyUtils.colorcode[i]));
                    create.dismiss();
                    AddEditCategory.this.categoryBean.setColor(MyUtils.colorcode[i]);
                }
            });
            gridView.setAdapter(colorPikerAdapter);
            create.show();
        }
    }

    public void deleteCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener r1 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case -2:
                        AddEditCategory.this.finish();
                        return;
                    case -1:
                        int drag_id = AddEditCategory.this.categoryBean.getDrag_id();
                        int categoryGroup = AddEditCategory.this.categoryBean.getCategoryGroup();
                        String accountRef = AddEditCategory.this.categoryBean.getAccountRef();
                        AddEditCategory addEditCategory = AddEditCategory.this;
                        addEditCategory.j.deleteCategoryData(addEditCategory.categoryBean);
                        AddEditCategory.this.j.UpdatedeleteCategoryData(drag_id, categoryGroup, accountRef);
                        AddEditCategory.this.finish();
                        return;
                    default:
                        return;
                }
            }
        };
        builder.setMessage("It will delete all relevant data\nAre you sure to delete?").setPositiveButton(getString(R.string.action_yes), r1).setNegativeButton(getString(R.string.action_no), r1);
        builder.show();
    }

    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
    }
}
