package org.centerm.land.healthcare.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.centerm.land.R;
import org.centerm.land.healthcare.baseavtivity.BaseToolbarActivity;

public class MedicalTreatmentActivity extends BaseToolbarActivity implements View.OnClickListener {

    public static final String KEY_STATUS_SALE = MedicalTreatmentActivity.class.getName() + "_key_status_sale";

    public static final String KEY_ID_FOREIGNER_NUMBER = MedicalTreatmentActivity.class.getName() +"_key_id_foreigner_number";

    public static final String KEY_TYPE_FAMILY = "11";
    public static final String KEY_TYPE_MIN_SEVEN = "12";
    public static final String KEY_TYPE_FOREIGNER= "13";
    public static final String KEY_TYPE_NO_CARD = "14";


    private CardView personOutCardView;
    private CardView kidneyCardView;
    private CardView cancerPatientsCardView;
    /**
     * Dialog Family
     */
    private Dialog dialogMenuFamily;
    private Button familyBtn;
    private Button minSevenBtn;
    private Button foreignerBtn;
    private Button noCardBtn;
    private ImageView closeImage;

    private String statusSale;
    private Dialog dialogForeigner;
    private TextView dialogTitleLabel;
    private EditText userInputDialogEt;
    private Button okBtn;
    private Button cancelBtn;
    private int pos;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_treatment);
        initWidget();
        initWidgetToolbar();
        setTitleToolbar("ใช้สิทธืรักษาพยาบาล");
        customDialogMenuFamily();
        customDialogKeyInForeigner();
    }

    @Override
    public void initWidget() {
//        super.initWidget();
        personOutCardView = findViewById(R.id.personOutCardView);
        kidneyCardView = findViewById(R.id.kidneyCardView);
        cancerPatientsCardView = findViewById(R.id.cancerPatientsCardView);
        personOutCardView.setOnClickListener(this);
        kidneyCardView.setOnClickListener(this);
        cancerPatientsCardView.setOnClickListener(this);

    }

    private void customDialogMenuFamily() {
        dialogMenuFamily = new Dialog(MedicalTreatmentActivity.this);
        dialogMenuFamily.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogMenuFamily.setCancelable(true);
        dialogMenuFamily.setContentView(R.layout.dialog_custom_family);
        dialogMenuFamily.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogMenuFamily.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        familyBtn = dialogMenuFamily.findViewById(R.id.familyBtn);
        minSevenBtn = dialogMenuFamily.findViewById(R.id.minSevenBtn);
        foreignerBtn = dialogMenuFamily.findViewById(R.id.foreignerBtn);
        noCardBtn = dialogMenuFamily.findViewById(R.id.noCardBtn);
        closeImage = dialogMenuFamily.findViewById(R.id.closeImage);

        familyBtn.setOnClickListener(this);
        minSevenBtn.setOnClickListener(this);
        foreignerBtn.setOnClickListener(this);
        noCardBtn.setOnClickListener(this);
        closeImage.setOnClickListener(this);
    }
    private void customDialogKeyInForeigner() {
        dialogForeigner = new Dialog(MedicalTreatmentActivity.this);
        dialogForeigner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogForeigner.setCancelable(true);
        dialogForeigner.setContentView(R.layout.dialog_key_in_foreigner);
        dialogForeigner.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialogTitleLabel = dialogForeigner.findViewById(R.id.dialogTitleLabel);
        userInputDialogEt = dialogForeigner.findViewById(R.id.userInputDialogEt);
        userInputDialogEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 1) {
                    if (statusSale.equalsIgnoreCase("13")) {
                        userInputDialogEt.setText("B");
                        int pos = userInputDialogEt.getText().length();
                        userInputDialogEt.setSelection(pos);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        okBtn = dialogForeigner.findViewById(R.id.okBtn);
        cancelBtn = dialogForeigner.findViewById(R.id.cancelBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidingKeyboard(userInputDialogEt);
                Intent intent = new Intent(MedicalTreatmentActivity.this,CalculateHelthCareActivity.class);
                intent.putExtra(KEY_STATUS_SALE,statusSale);
                intent.putExtra(KEY_ID_FOREIGNER_NUMBER,userInputDialogEt.getText().toString());
                startActivity(intent);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidingKeyboard(userInputDialogEt);
                dialogForeigner.dismiss();
            }
        });
    }

    private void hidingKeyboard(EditText editText ) {
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void intentStartActivity() {
        Intent intent = new Intent(MedicalTreatmentActivity.this,IDActivity.class);
        intent.putExtra(KEY_STATUS_SALE,statusSale);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personOutCardView:
                statusSale = "1";
                dialogMenuFamily.show();
                break;
            case R.id.kidneyCardView:
                statusSale = "2";
//                dialogMenuFamily.show();
                break;
            case R.id.cancerPatientsCardView:
                statusSale = "3";
//                dialogMenuFamily.show();
                break;
            /**
             * Click ฺButton Dialog
             */
            case R.id.familyBtn:
                statusSale += "1";
                intentStartActivity();
                break;
            case R.id.minSevenBtn:
                statusSale += "2";
                intentStartActivity();
                break;
            case R.id.foreignerBtn:
                statusSale += "3";
                userInputDialogEt.setText("B");
                dialogTitleLabel.setText("สิทธิบุคคลต่างชาติ");
                pos = userInputDialogEt.getText().length();
                userInputDialogEt.setSelection(pos);
                userInputDialogEt.requestFocus();
                imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                dialogForeigner.show();
                break;
            case R.id.noCardBtn:
                statusSale += "4";
                dialogTitleLabel.setText("สิทธิบุคคลต่างชาติ");
                pos = userInputDialogEt.getText().length();
                userInputDialogEt.setSelection(pos);
                userInputDialogEt.requestFocus();
                imm = (InputMethodManager)   getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                dialogForeigner.show();
                break;
            case R.id.closeImage:
                dialogMenuFamily.dismiss();
                break;
        }
    }
}
