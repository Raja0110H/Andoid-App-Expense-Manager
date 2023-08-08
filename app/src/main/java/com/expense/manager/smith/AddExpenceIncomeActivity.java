package com.expense.manager.smith;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.expense.manager.R;
import com.expense.manager.Model.bean.CategoryBean;
import com.expense.manager.Model.bean.DataBean;
import com.expense.manager.Model.bean.RecurringBean;
import com.expense.manager.widget.NumberPicker;
import com.jsibbold.zoomage.ZoomageView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
 ;

public class AddExpenceIncomeActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private static final int DATE_DIALOG_ID = 0;
    private static int selectCategory;
    private String Image = "";
    /* access modifiers changed from: private */
    public String Imagepath = "";
    /* access modifiers changed from: private */
    public int PICK_IMAGE = 101;
    private CategoryAdapter adapter;
    private AlertDialog alert;
    /* access modifiers changed from: private */
    public Calendar calendar;
    private int category = 2;
    /* access modifiers changed from: private */
    public DataBean currentDataBean;
    private EditText etAmount;
    private EditText etNote;
    private ImageView imgImage;
    List<CategoryBean> l;
    private LinearLayout layoutCategory;
    private LinearLayout layoutDate;
    private CoordinatorLayout layoutMain;
    private LinearLayout layoutMethod;
    private LinearLayout layoutRecurring;
    private ListView list;
    List<CategoryBean> m;
    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector mScaleGestureDetector;
    /* access modifiers changed from: private */
    public String method = "";
    boolean n;
    int o = 0;
    int p = 0;
    int q = 0;
    private RadioButton rbExpense;
    private RadioButton rbIncome;
    /* access modifiers changed from: private */
    public int recurringDate = 1;
    /* access modifiers changed from: private */
    public int recurringId = 0;
    /* access modifiers changed from: private */
    public int recurringType = -1;
    private RadioGroup rgInEX;
    /* access modifiers changed from: private */
    private String subCategory = "";
    /* access modifiers changed from: private */
    public SwitchCompat tbRecurring;
    private TextView tvCategory;
    private TextView tvDate;
    /* access modifiers changed from: private */
    public TextView tvMethod;
    private TextView tvRecurring;
    private Context mContext = this;

    Ambaliya_MyApplication myApplication;
    private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    private static final int REQUEST_FOR_ALL_INCLUDING_GALARY = 111;

    public void changeUserAccount() {
    }

    public static void verifyStoragePermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void onCreate(Bundle bundle) {
        this.k = false;
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_manage_manager);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setTitle((CharSequence) Html.fromHtml("<font color='#ffffff'>" + getResources().getString(R.string.app_name) + "</font>"));
        this.rgInEX = (RadioGroup) findViewById(R.id.rg_switch);
        this.rbExpense = (RadioButton) findViewById(R.id.rb_expense);
        this.rbIncome = (RadioButton) findViewById(R.id.rb_income);
        this.tvDate = (TextView) findViewById(R.id.tv_date);
        this.etAmount = (EditText) findViewById(R.id.edt_amount);
        this.rbIncome.setTypeface((Typeface) null, 1);
        this.tbRecurring = (SwitchCompat) findViewById(R.id.tbRecurring);
        this.etNote = (EditText) findViewById(R.id.edt_note);
        this.tvCategory = (TextView) findViewById(R.id.tv_category);
        this.tvMethod = (TextView) findViewById(R.id.tv_method);
        this.tvRecurring = (TextView) findViewById(R.id.tv_recurring);
        this.imgImage = (ImageView) findViewById(R.id.img_bill);
        this.layoutCategory = (LinearLayout) findViewById(R.id.layout_category);
        this.layoutMethod = (LinearLayout) findViewById(R.id.layout_method);
        this.layoutDate = (LinearLayout) findViewById(R.id.layout_date);
        this.layoutRecurring = (LinearLayout) findViewById(R.id.layout_recurring);
        this.layoutMain = (CoordinatorLayout) findViewById(R.id.layout_main_incomeexpense);
        this.rgInEX.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint({"ResourceType"})
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (((RadioButton) radioGroup.findViewById(i)) != null && i > -1) {
                    AddExpenceIncomeActivity.this.changeUi();
                }
            }
        });
        myApplication = ((Ambaliya_MyApplication) this.getApplication());



        Calendar instance = Calendar.getInstance();
        this.calendar = instance;
        instance.set(11, 0);
        this.calendar.clear(12);
        this.calendar.clear(13);
        this.calendar.clear(14);
        n();
        this.n = this.j.existsColumnInTable().booleanValue();
        this.m = this.j.getSpeningCategory();
        for (int i = 0; i < this.m.size(); i++) {
            CategoryBean categoryBean = this.m.get(i);
            categoryBean.setCategoryTotal(this.j.getCategoryAmount(categoryBean.getCategoryGroup(), categoryBean.getId()));
        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.category = extras.getInt(TAG.CATEGORY, 1);
            int i2 = extras.getInt(TAG.DATA, 0);
            this.recurringId = extras.getInt(TAG.RECURRING, 0);
            if (i2 != 0) {
                this.currentDataBean = this.j.getManagerModelById(this.category, String.valueOf(i2));
            }
            if (this.recurringId != 0) {
                MyDatabaseHandler myDatabaseHandler = this.j;
                int i3 = this.category;
                RecurringBean recurringModelById = myDatabaseHandler.getRecurringModelById(i3, "" + this.recurringId);
                this.currentDataBean = recurringModelById.convertToDataBean();
                this.recurringDate = Integer.parseInt(recurringModelById.getRecurringDate());
                this.recurringType = Integer.parseInt(recurringModelById.getRecurringType());
            }
            if (i2 == 0 && this.recurringId == 0) {
                findViewById(R.id.layout_recurring).setVisibility(View.VISIBLE);
            }
        }
        updateUI();
        if (this.n) {
            this.l = new ArrayList();
            if (this.category == 1) {
                this.l = this.j.getCategoryList(1);
            } else {
                this.l = this.j.getCategoryList(2);
            }
        } else {
            Toast.makeText(this, "columns not found", 0).show();
            this.j.addcolumn();
        }
        this.layoutDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddExpenceIncomeActivity.this.showDialog(0);
            }
        });
        this.layoutCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddExpenceIncomeActivity.this.selectCategory();
            }
        });
        this.layoutMethod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddExpenceIncomeActivity.this.selectMethods();
            }
        });
        this.imgImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenceIncomeActivity.this);
                builder.setTitle(AddExpenceIncomeActivity.this.getString(R.string.receipts));
                builder.setCancelable(true);
                View inflate = AddExpenceIncomeActivity.this.getLayoutInflater().inflate(R.layout.dialog_image, (ViewGroup) null);
                builder.setView(inflate);
                ZoomageView imageView = (ZoomageView) inflate.findViewById(R.id.my_image);
                if (AddExpenceIncomeActivity.this.Imagepath.equals("")) {
                    imageView.setImageResource(R.drawable.camera);
                } else if (AddExpenceIncomeActivity.this.Imagepath.equals("no image")) {
                    imageView.setImageResource(R.drawable.camera);
                } else {
                    imageView.setImageURI(Uri.parse(AddExpenceIncomeActivity.this.Imagepath));
                }
                builder.setNegativeButton(AddExpenceIncomeActivity.this.getString(R.string.change), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i4) {

                        if (ContextCompat.checkSelfPermission(AddExpenceIncomeActivity.this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
                            AddExpenceIncomeActivity.verifyStoragePermissions(AddExpenceIncomeActivity.this);
                        } else {
                            showOptions();
                        }
                    }
                });
                builder.show();
            }
        });

    }

    public void showOptions() {


        View view = LayoutInflater.from(this).inflate(R.layout.selectiomoption, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddExpenceIncomeActivity.this);
        builder.setView(view);

        Dialog dialog = builder.create();


        CardView cv_camera = view.findViewById(R.id.cv_camera);
        CardView cv_gallery = view.findViewById(R.id.cv_gallery);


        if (!isFinishing() && dialog != null) {
            dialog.show();
        }


        cv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mayRequestForPermission()) {
                    AddExpenceIncomeActivity.this.startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 111);
                }
            }
        });
        cv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mayRequestForPermission()) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    AddExpenceIncomeActivity addExpenceIncomeActivity = AddExpenceIncomeActivity.this;
                    addExpenceIncomeActivity.startActivityForResult(Intent.createChooser(intent, addExpenceIncomeActivity.getString(R.string.select_picture)), AddExpenceIncomeActivity.this.PICK_IMAGE);

                }
            }
        });
        dialog.setCancelable(true);

        dialog.show();
    }

    public void selectMethods() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.h);
        builder.setTitle(getString(R.string.select_method));
        builder.setItems(MyUtils.Methods_TYPES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                String str = MyUtils.Methods_TYPES[i];
                AddExpenceIncomeActivity.this.tvMethod.setText(str);
                String unused = AddExpenceIncomeActivity.this.method = str;
            }
        });
        builder.create().show();
    }

    private void categoryAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = getLayoutInflater().inflate(R.layout.dialog_category_picker, (ViewGroup) null);
        builder.setView(inflate);
        this.l = new ArrayList();
        this.list = (ListView) inflate.findViewById(R.id.color_list);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.select_category));
        if (this.n) {
            this.l = new ArrayList();
            if (this.category == 1) {
                this.l = this.j.getCategoryList(1);
            } else {
                this.l = this.j.getCategoryList(2);
            }
        } else {
            Toast.makeText(this, "columns not found", 0).show();
            this.j.addcolumn();
        }
        this.alert = builder.create();
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, this.l, this.currentDataBean.getSubCategoryId(), this.alert, this.tvCategory);
        this.adapter = categoryAdapter;
        this.list.setAdapter(categoryAdapter);
        CategoryListHelper.getListViewSize(this.list);
    }

    public void selectCategory() {
        categoryAlert();
        this.alert.show();
    }

    public void changeUi() {
        if (this.rbIncome.isChecked()) {
            this.category = 2;
            this.rbExpense.setTextColor(getResources().getColor(R.color.switch_textcolor));
            this.rbIncome.setTextColor(getResources().getColor(R.color.white));
            this.rbIncome.setTypeface((Typeface) null, 1);
            this.rbExpense.setTypeface((Typeface) null, 0);
            return;
        }
        this.category = 1;
        this.rbIncome.setTextColor(getResources().getColor(R.color.switch_textcolor));
        this.rbExpense.setTextColor(getResources().getColor(R.color.white));
        this.rbIncome.setTypeface((Typeface) null, 0);
        this.rbExpense.setTypeface((Typeface) null, 1);
    }

    private void updateUI() {
        DataBean dataBean = this.currentDataBean;
        if (dataBean != null) {
            String replaceAll = dataBean.getAmount().replaceAll(",", "");
            try {
                EditText editText = this.etAmount;
                editText.setText("" + ((int) MyUtils.RoundOff(Double.parseDouble(replaceAll))));
            } catch (NumberFormatException e) {
                this.etAmount.setText("0");
            }
            categoryAlert();
            this.tvMethod.setText(this.currentDataBean.getMethod());
            this.tvCategory.setText(this.currentDataBean.getCategoryModel().getName());
            if (this.category == 2) {
                this.rbIncome.setChecked(true);
            } else {
                this.rbExpense.setChecked(true);
            }
            try {
                this.calendar.setTime(MyUtils.sdfDatabase.parse(this.currentDataBean.getDate()));
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            B();
            this.etNote.setText(this.currentDataBean.getNote());
            if (!TextUtils.isEmpty(this.currentDataBean.getRefRecurring())) {
                this.tbRecurring.setChecked(true);
            }
        } else {
            this.currentDataBean = new DataBean();
        }
        String image = this.currentDataBean.getImage();
        this.Imagepath = image;
        if (image.equals("")) {
            this.imgImage.setImageResource(R.drawable.camera);
        } else if (this.Imagepath.equals("no image")) {
            this.imgImage.setImageResource(R.drawable.camera);
        } else {
            this.imgImage.setImageURI(Uri.parse(this.Imagepath));
        }
        this.tbRecurring.setOnCheckedChangeListener(this);
        B();
        if (this.category == 1) {
            this.rbExpense.setChecked(true);
        } else {
            this.rbIncome.setChecked(true);
        }
    }

    /* access modifiers changed from: protected */
    public void A() {
        NumberPicker numberPicker = new NumberPicker(this);
        final EditText editText = (EditText) numberPicker.findViewById(R.id.timepicker_input);
        new AlertDialog.Builder(this).setTitle(getString(R.string.recurring_date)).setView(numberPicker).setCancelable(false).setPositiveButton(getString(R.string.action_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                int unused = AddExpenceIncomeActivity.this.recurringDate = Integer.parseInt(editText.getText().toString().toString());
                int unused2 = AddExpenceIncomeActivity.this.recurringType = 1;
            }
        }).setNegativeButton(getString(R.string.action_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                int unused = AddExpenceIncomeActivity.this.recurringType = -1;
                AddExpenceIncomeActivity.this.tbRecurring.setChecked(false);
            }
        }).show();
    }

    public void onClick(View view) {
        view.getId();
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public Dialog onCreateDialog(int i, Bundle bundle) {
        if (i == 0) {
            new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datePicker, int i2, int i3, int i4) {
                    if (AddExpenceIncomeActivity.this.recurringId != 0) {
                        int unused = AddExpenceIncomeActivity.this.recurringDate = i4;
                    }
                    AddExpenceIncomeActivity.this.calendar.set(i2, i3, i4);
                    AddExpenceIncomeActivity.this.B();
                }
            }, this.calendar.get(1), this.calendar.get(2), this.calendar.get(5)).show();
        }
        return super.onCreateDialog(i);
    }

    /* access modifiers changed from: protected */
    public void B() {
        this.tvDate.setText(MyUtils.sdfUser.format(this.calendar.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_income_expense, menu);
        Drawable wrap = DrawableCompat.wrap(menu.findItem(R.id.option_done).getIcon());
        DrawableCompat.setTint(wrap, ContextCompat.getColor(this, R.color.white));
        menu.findItem(R.id.option_done).setIcon(wrap);
        Drawable wrap2 = DrawableCompat.wrap(menu.findItem(R.id.option_delete).getIcon());
        DrawableCompat.setTint(wrap2, ContextCompat.getColor(this, R.color.white));
        menu.findItem(R.id.option_delete).setIcon(wrap2);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.option_delete:
                if (this.recurringId != 0) {
                    deleteAlertForRecurringData();
                } else {
                    deleteAlertForManagerData();
                }
                return true;
            case R.id.option_done:
                if (TextUtils.isEmpty(this.etAmount.getText().toString())) {
                    Toast.makeText(this, "Please enter amount", 0).show();
                } else if (this.method.equals("")) {
                    Toast.makeText(this.h, "Please select method type", 0).show();
                } else if (this.tvCategory.getText().toString().equals("Select Category")) {
                    Toast.makeText(this.h, "Please select category", 0).show();
                } else {
                    int parseInt = Integer.parseInt(this.etAmount.getText().toString());
                    if (this.category == 1) {
                        int i = 0;
                        boolean aaaa = true;
                        while (aaaa) {
                            if (i < this.m.size()) {
                                if (this.tvCategory.getText().toString().equals(this.m.get(i).getName())) {
                                    this.o = Integer.parseInt(this.m.get(i).getCategoryTotal());
                                    this.q = Integer.parseInt(this.m.get(i).getId());
                                    this.p = Integer.parseInt(this.m.get(i).getCategoryLimit());
                                    aaaa = false;
                                } else {
                                    i++;
                                }
                            }
                        }
                        int i2 = this.p;
                        if (i2 < Math.max(this.o + parseInt, i2)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle(getString(R.string.over_spent));
                            builder.setMessage(getString(R.string.over_spent_msg));
                            builder.setCancelable(false);
                            builder.setPositiveButton(getString(R.string.change), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    Intent intent = new Intent(AddExpenceIncomeActivity.this.h, AddEditCategory.class);
                                    intent.putExtra(TAG.CATEGORY, 1);
                                    intent.putExtra(TAG.DATA, AddExpenceIncomeActivity.this.q);
                                    AddExpenceIncomeActivity.this.startActivity(intent);
                                }
                            });
                            builder.setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialogInterface, int i2) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.create().show();
                        } else {
                            doneWithManagerData();
                        }
                    } else {
                        doneWithManagerData();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    private void updateAlertForRecurringData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener r1 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    AddExpenceIncomeActivity.this.addUpdateData();
                    AddExpenceIncomeActivity.this.finish();
                }
            }
        };
        builder.setTitle(getString(R.string.be_careful));
        builder.setMessage(getString(R.string.be_careful_msg)).setPositiveButton(getString(R.string.action_yes), r1).setNegativeButton(getString(R.string.action_no), r1);
        builder.show();
    }

    private void doneWithManagerData() {
        String str;
        try {
            String obj = this.etAmount.getText().toString();
            String format = MyUtils.sdfDatabase.format(this.calendar.getTime());
            String obj2 = this.etNote.getText().toString();
            this.method = this.tvMethod.getText().toString();
            this.subCategory = ((CategoryAdapter) this.list.getAdapter()).getSelectedCategoryId();
            try {
                str = "" + Double.parseDouble(obj);
            } catch (NumberFormatException e) {
                str = "0";
            }
            this.currentDataBean.setCategoryId("" + this.category);
            this.currentDataBean.setAmount(str);
            this.currentDataBean.setNote(obj2);
            this.currentDataBean.setDate(format);
            this.currentDataBean.setSubCategoryId(this.subCategory);
            this.currentDataBean.setMethod(this.method);
            this.currentDataBean.setImage(this.Imagepath);
            this.currentDataBean.setAccountRef(MyUtils.getCurrentAccount(this));
            if (this.recurringId != 0) {
                updateAlertForRecurringData();
                return;
            }
            addUpdateData();

            finish();

        } catch (ArrayIndexOutOfBoundsException e2) {
            e2.printStackTrace();
            Toast.makeText(this, "There is no category selected.\n Add some category first.", 0).show();
        }
    }

    private void deleteAlertForManagerData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener r1 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case -2:
                        dialogInterface.dismiss();
                        return;
                    case -1:
                        AddExpenceIncomeActivity addExpenceIncomeActivity = AddExpenceIncomeActivity.this;
                        addExpenceIncomeActivity.j.deleteManagerData(addExpenceIncomeActivity.currentDataBean);
                        AddExpenceIncomeActivity.this.finish();
                        return;
                    default:
                        return;
                }
            }
        };
        builder.setMessage(getString(R.string.confirm_delete)).setPositiveButton(getString(R.string.action_yes), r1).setNegativeButton(getString(R.string.action_no), r1);
        builder.show();
    }

    private void deleteAlertForRecurringData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener r1 = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case -2:
                        AddExpenceIncomeActivity.this.finish();
                        return;
                    case -1:
                        MyDatabaseHandler myDatabaseHandler = AddExpenceIncomeActivity.this.j;
                        myDatabaseHandler.deleteRecurringDataById("" + AddExpenceIncomeActivity.this.recurringId);
                        AddExpenceIncomeActivity.this.finish();
                        return;
                    default:
                        return;
                }
            }
        };
        builder.setTitle(getString(R.string.warning));
        builder.setMessage(getString(R.string.delete_all_future_recurring_transaction)).setPositiveButton(getString(R.string.action_yes), r1).setNegativeButton(getString(R.string.action_no), r1);
        builder.show();
    }

    public void addUpdateData() {
        if (this.recurringId != 0) {
            RecurringBean convertToRecurringBean = this.currentDataBean.convertToRecurringBean();
            convertToRecurringBean.setId("" + this.recurringId);
            convertToRecurringBean.setRecurringDate("" + this.recurringDate);
            convertToRecurringBean.setRecurringType("" + this.recurringType);
            this.j.addUpdateRecurringData(convertToRecurringBean);
            return;
        }
        if (this.recurringType != -1) {
            RecurringBean convertToRecurringBean2 = this.currentDataBean.convertToRecurringBean();
            convertToRecurringBean2.setRecurringDate("" + this.recurringDate);
            convertToRecurringBean2.setRecurringType("" + this.recurringType);
            convertToRecurringBean2.setRecurringLastdate(MyUtils.sdfDatabase.format(MyUtils.getNextUpdateCalender(this.recurringType, this.calendar, this.recurringDate).getTime()));
            this.j.addUpdateRecurringData(convertToRecurringBean2);
        }
        this.j.addUpdateManagerData(this.currentDataBean);
    }

    public void onResume() {
        super.onResume();
        this.m = new ArrayList();
        this.m = this.j.getSpeningCategory();
        for (int i = 0; i < this.m.size(); i++) {
            CategoryBean categoryBean = this.m.get(i);
            categoryBean.setCategoryTotal(this.j.getCategoryAmount(categoryBean.getCategoryGroup(), categoryBean.getId()));
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == this.PICK_IMAGE && i2 == -1 && intent != null) {
            Uri data = intent.getData();
            try {
                UUID randomUUID = UUID.randomUUID();
                this.Imagepath = getFilesDir() + "/bill-" + randomUUID.toString() + ".png";
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data);
                FileOutputStream fileOutputStream = new FileOutputStream(this.Imagepath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                this.imgImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (i == 111 && i2 == -1 && intent != null) {
            intent.getData();
            try {
                UUID randomUUID2 = UUID.randomUUID();
                this.Imagepath = getFilesDir() + "/bill-" + randomUUID2.toString() + ".png";
                Bitmap createScaledBitmap = Bitmap.createScaledBitmap((Bitmap) intent.getExtras().get("data"), 200, 200, true);
                FileOutputStream fileOutputStream2 = new FileOutputStream(this.Imagepath);
                createScaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream2);
                fileOutputStream2.flush();
                fileOutputStream2.close();
                this.imgImage.setImageBitmap(createScaledBitmap);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

//    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
//        super.onRequestPermissionsResult(i, strArr, iArr);
//        if (i != 1) {
//            return;
//        }
//        if (iArr[0] != 0) {
//            AlertDialog create = new AlertDialog.Builder(this).setPositiveButton("Let's Go", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialogInterface, int i2) {
//                    AddExpenceIncomeActivity.verifyStoragePermissions(AddExpenceIncomeActivity.this);
//                }
//            }).create();
//            create.setTitle("Notice!");
//            create.setMessage("Allowing storage permissions is crucial for the app to work. Please grant the permissions.");
//            create.show();
//            return;
//        }
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction("android.intent.action.GET_CONTENT");
//        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), this.PICK_IMAGE);
//    }

    @TargetApi(23)
    public boolean mayRequestForPermission() {


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (mContext.checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && mContext.checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && mContext.checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, READ_EXTERNAL_STORAGE)) {
            //promptStoragePermission();
            showPermissionRationaleSnackBarForALL("To get Invoice Image we Storage Permission");

        } else if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, CAMERA)) {
            showPermissionRationaleSnackBarForALL("To get Invoice Image we Camera permission");
        } else {

            showPermissionRationaleSnackBarForALL("To get Invoice we Need Storage and Camera Permission so please allow in next dialog");
//            ActivityCompat.requestPermissions((Activity) mContext, new String[]{RECORD_AUDIO, CAMERA}, REQUEST_FOR_ALL_INCLUDING_GALARY);
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case REQUEST_FOR_ALL_INCLUDING_GALARY: {


                if (grantResults.length > 0) {

                    if (Arrays.asList(permissions).contains(WRITE_EXTERNAL_STORAGE) && Arrays.asList(permissions).contains(READ_EXTERNAL_STORAGE)) {

                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            //populateImagesFromGallery();

//                            Toast.makeText(mContext, "Storage permission Granted", Toast.LENGTH_SHORT).show();
                        } else {


                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, WRITE_EXTERNAL_STORAGE) && ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, READ_EXTERNAL_STORAGE)) {
                                showPermissionRationaleSnackBarForALL("Storage permission is needed To get Invoice");


                            } else {
                                Toast.makeText(mContext, "Go to settings->Apps->permission and enable Storage permission", Toast.LENGTH_LONG).show();
                                Common_Denial_snackbar("Go to permission and enable Storage permission");
                            }
                        }

                    }
                    if (Arrays.asList(permissions).contains(CAMERA)) {

                        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            //populateImagesFromGallery();

//                            Toast.makeText(mContext, "Camera permission Granted", Toast.LENGTH_SHORT).show();
                        } else {

                            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, CAMERA)) {


                                showPermissionRationaleSnackBarForALL("Camera Permission is needed To get Invoice");
                            } else {
                                Toast.makeText(mContext, "Go to settings->Apps->permission and enable Camera permission", Toast.LENGTH_LONG).show();

                                Common_Denial_snackbar("Go to permission and enable Camera permission");

                            }
                        }
                    }


                }

                break;
            }


        }
    }


    public void showPermissionRationaleSnackBarForALL(String s) {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_permission_screen, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddExpenceIncomeActivity.this);
        builder.setView(view);

        final Dialog dialog = builder.create();
        dialog.setCancelable(false);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_yes = view.findViewById(R.id.tv_yes);
        TextView tv_alert = view.findViewById(R.id.tv_alert);

        tv_title.setText("Permission Needed");
        tv_yes.setText("Ok");
        tv_alert.setText(s);

        CardView btnYes = view.findViewById(R.id.btn_yes);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA},
                        REQUEST_FOR_ALL_INCLUDING_GALARY);

                if (dialog.isShowing() && dialog != null) {
                    dialog.dismiss();
                }

            }
        });

        if (!isFinishing() && dialog != null) {
            dialog.show();
        }


    }

    public void Common_Denial_snackbar(String sss) {


        View view = LayoutInflater.from(this).inflate(R.layout.dialog_permission_screen, null);

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(AddExpenceIncomeActivity.this);
        builder.setView(view);

        final Dialog dialog = builder.create();

        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_yes = view.findViewById(R.id.tv_yes);
        TextView tv_alert = view.findViewById(R.id.tv_alert);

        tv_title.setText("Permission Needed");
        tv_yes.setText("Click Here");
        tv_alert.setText(sss);

        CardView btnYes = view.findViewById(R.id.btn_yes);


        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mContext.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(myAppSettings);

                if (dialog.isShowing() && dialog != null) {
                    dialog.dismiss();
                }

            }
        });

        if (!isFinishing() && dialog != null) {
            dialog.show();
        }


    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        if (z) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.h);
            builder.setTitle(getString(R.string.recurring_type));
            builder.setItems(MyDatabaseHandler.RecurringTable.RECURRING_TYPES, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AddExpenceIncomeActivity.this.z(i);
                }
            });
            builder.create().show();
            return;
        }
        Toast.makeText(this.h, getString(R.string.recurring_delete), 0).show();
        this.recurringType = -1;
    }

    /* access modifiers changed from: protected */
    public void z(int i) {
        if (i == 0) {
            this.recurringType = 0;
        } else if (i == 1) {
            this.recurringType = 2;
        } else if (i == 2) {
            this.recurringType = 1;
        } else if (i == 3) {
            this.recurringType = 3;
        } else if (i == 4) {
            this.recurringType = 4;
        } else if (i == 5) {
            this.recurringType = 5;
        }
        Toast.makeText(this, MyDatabaseHandler.RecurringTable.RECURRING_TYPES[i] + getString(R.string.RecurringCreated), 0).show();
        if (this.recurringType == 1) {
            A();
        }
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
