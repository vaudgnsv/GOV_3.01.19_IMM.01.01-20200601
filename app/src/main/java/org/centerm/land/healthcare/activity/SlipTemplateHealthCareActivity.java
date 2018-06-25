package org.centerm.land.healthcare.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.centerm.smartpos.aidl.printer.AidlPrinter;
import com.centerm.smartpos.aidl.printer.AidlPrinterStateChangeListener;
import com.centerm.smartpos.aidl.sys.AidlDeviceManager;
import com.centerm.smartpos.constant.Constant;

import org.centerm.land.CardManager;
import org.centerm.land.MainApplication;
import org.centerm.land.R;
import org.centerm.land.activity.CalculatePriceActivity;
import org.centerm.land.activity.MenuServiceActivity;
import org.centerm.land.bassactivity.SettingToolbarActivity;
import org.centerm.land.healthcare.database.HealthCareDB;
import org.centerm.land.helper.CardPrefix;
import org.centerm.land.utility.Preference;

import java.text.DecimalFormat;

import io.realm.Realm;

public class SlipTemplateHealthCareActivity extends SettingToolbarActivity implements View.OnClickListener {

    private final String TAG = "SlipHelthCare";

    private Realm realm = null;

    private CountDownTimer timer = null;

    /**
     * Slip
     */
    private ImageView bankImage = null;
    private ImageView bank1Image = null;
    private TextView merchantName1Label = null;
    private TextView merchantName2Label = null;
    private TextView merchantName3Label = null;
    private TextView tidLabel = null;
    private TextView midLabel = null;
    private TextView traceLabel = null;
    private TextView systrcLabel = null;
    private TextView batchLabel = null;
    private TextView refNoLabel = null;
    private TextView dateLabel = null;
    private TextView timeLabel = null;
    private TextView typeLabel = null;
    private TextView typeCardLabel = null;
    private TextView cardNoLabel = null;
    private TextView apprCodeLabel = null;
    private TextView comCodeLabel = null;
    private TextView amtThbLabel = null;
    private TextView feeThbLabel = null;
    private TextView totThbLabel = null;
    private TextView ref1Label = null;
    private TextView ref2Label = null;
    private TextView ref3Label = null;
    private RelativeLayout ref1RelativeLayout = null;
    private RelativeLayout ref2RelativeLayout = null;
    private RelativeLayout ref3RelativeLayout = null;

    private TextView taxIdLayout = null;
    private TextView taxAbbLayout = null;
    private TextView traceTaxLayout = null;
    private TextView batchTaxLayout = null;
    private TextView dateTaxLayout = null;
    private TextView timeTaxLayout = null;
    private TextView feeTaxLayout = null;

    private TextView appLabel = null;
    private TextView tcLabel = null;
    private TextView aidLabel = null;
    private TextView nameEmvCardLabel = null;

    private FrameLayout appFrameLabel = null;
    private FrameLayout tcFrameLayout = null;
    private FrameLayout aidFrameLayout = null;
    private LinearLayout taxLinearLayout = null;
    private TextView copyLabel = null;
    private TextView typeInputCardLabel = null;
    /**
     * Slip Auto
     */
    private ImageView bankImageAuto = null;
    private ImageView bank1ImageAuto = null;
    private TextView merchantName1LabelAuto = null;
    private TextView merchantName2LabelAuto = null;
    private TextView merchantName3LabelAuto = null;
    private TextView tidLabelAuto = null;
    private TextView midLabelAuto = null;
    private TextView traceLabelAuto = null;
    private TextView systrcLabelAuto = null;
    private TextView batchLabelAuto = null;
    private TextView refNoLabelAuto = null;
    private TextView dateLabelAuto = null;
    private TextView timeLabelAuto = null;
    private TextView typeLabelAuto = null;
    private TextView typeCardLabelAuto = null;
    private TextView cardNoLabelAuto = null;
    private TextView apprCodeLabelAuto = null;
    private TextView comCodeLabelAuto = null;
    private TextView amtThbLabelAuto = null;
    private TextView feeThbLabelAuto = null;
    private TextView totThbLabelAuto = null;
    private TextView ref1LabelAuto = null;
    private TextView ref2LabelAuto = null;
    private TextView ref3LabelAuto = null;
    private RelativeLayout ref1RelativeLayoutAuto = null;
    private RelativeLayout ref2RelativeLayoutAuto = null;
    private RelativeLayout ref3RelativeLayoutAuto = null;
    private TextView taxIdLayoutAuto = null;
    private TextView taxAbbLayoutAuto = null;
    private TextView traceTaxLayoutAuto = null;
    private TextView batchTaxLayoutAuto = null;
    private TextView dateTaxLayoutAuto = null;
    private TextView timeTaxLayoutAuto = null;
    private TextView feeTaxLayoutAuto = null;

    private TextView appLabelAuto = null;
    private TextView tcLabelAuto = null;
    private TextView aidLabelAuto = null;
    private TextView nameEmvCardLabelAuto = null;


    private FrameLayout appFrameLabelAuto = null;
    private FrameLayout tcFrameLayoutAuto = null;
    private FrameLayout aidFrameLayoutAuto = null;
    private LinearLayout taxLinearLayoutAuto = null;
    private TextView copyLabelAuto = null;

    private TextView typeInputCardLabelAuto = null;

    private Button printBtn;
    private AidlPrinter printDev = null;
    private AidlDeviceManager manager = null;
    private NestedScrollView slipNestedScrollView;
    private NestedScrollView slipNestedScrollViewAuto;
    private LinearLayout slipLinearLayout;
    private LinearLayout slipLinearLayoutAuto;
    private CardManager cardManager = null;

    private int saleId;
    private String typeSlip;
    private View printFirst;

    private boolean statusOutScress = false;

    private AidlPrinterStateChangeListener.Stub callBackPrint = null;
    private Dialog dialogOutOfPaper;
    private Button okBtn;

    private Bitmap bitmapOld = null;
    private TextView msgLabel;
    private View texView;
    private FrameLayout comCodeFragmentAuto;
    private FrameLayout comCodeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slip_template_helth_care);
        initData();
        initWidget();
//        initBtnExit();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            saleId = bundle.getInt(CalculateHelthCareActivity.KEY_CALCULATE_ID_HGC);
            typeSlip = bundle.getString(CalculatePriceActivity.KEY_TYPE_SALE_OR_VOID);
        }
    }

    @Override
    public void initWidget() {
//        super.initWidget();
        realm = Realm.getDefaultInstance();
        cardManager = MainApplication.getCardManager();
        cardManager.abortPBOCProcess();
        printDev = cardManager.getInstancesPrint();
        customDialogOutOfPaper();

        printBtn = findViewById(R.id.printBtn);
        slipNestedScrollView = findViewById(R.id.slipNestedScrollView);
        slipLinearLayout = findViewById(R.id.slipLinearLayout);
        bankImage = findViewById(R.id.bankImage);
        bank1Image = findViewById(R.id.bank1Image);
        merchantName1Label = findViewById(R.id.merchantName1Label);
        if (!Preference.getInstance(this).getValueString(Preference.KEY_MERCHANT_1).isEmpty())
            merchantName1Label.setText(Preference.getInstance(this).getValueString(Preference.KEY_MERCHANT_1));

        merchantName2Label = findViewById(R.id.merchantName2Label);
        if (!Preference.getInstance(this).getValueString(Preference.KEY_MERCHANT_2).isEmpty())
            merchantName2Label.setText(Preference.getInstance(this).getValueString(Preference.KEY_MERCHANT_2));

        merchantName3Label = findViewById(R.id.merchantName3Label);
        if (!Preference.getInstance(this).getValueString(Preference.KEY_MERCHANT_3).isEmpty())
            merchantName3Label.setText(Preference.getInstance(this).getValueString(Preference.KEY_MERCHANT_3));

        tidLabel = findViewById(R.id.tidLabel);
        midLabel = findViewById(R.id.midLabel);
        traceLabel = findViewById(R.id.traceLabel);
        systrcLabel = findViewById(R.id.systrcLabel);
        batchLabel = findViewById(R.id.batchLabel);
        refNoLabel = findViewById(R.id.refNoLabel);
        dateLabel = findViewById(R.id.dateLabel);
        timeLabel = findViewById(R.id.timeLabel);
        typeLabel = findViewById(R.id.typeLabel);
        typeCardLabel = findViewById(R.id.typeCardLabel);
        cardNoLabel = findViewById(R.id.cardNoLabel);
        apprCodeLabel = findViewById(R.id.apprCodeLabel);
        comCodeLabel = findViewById(R.id.comCodeLabel);
        amtThbLabel = findViewById(R.id.amtThbLabel);
        feeThbLabel = findViewById(R.id.feeThbLabel);
        totThbLabel = findViewById(R.id.totThbLabel);
        ref1Label = findViewById(R.id.ref1Label);
        ref2Label = findViewById(R.id.ref2Label);
        ref3Label = findViewById(R.id.ref3Label);
        ref1RelativeLayout = findViewById(R.id.ref1RelativeLayout);
        ref2RelativeLayout = findViewById(R.id.ref2RelativeLayout);
        ref3RelativeLayout = findViewById(R.id.ref3RelativeLayout);

        taxIdLayout = findViewById(R.id.taxIdLabel);
        taxAbbLayout = findViewById(R.id.taxAbbLabel);
        traceTaxLayout = findViewById(R.id.traceTaxLabel);
        batchTaxLayout = findViewById(R.id.batchTaxLabel);
        dateTaxLayout = findViewById(R.id.dateTaxLabel);
        timeTaxLayout = findViewById(R.id.timeTaxLabel);
        feeTaxLayout = findViewById(R.id.feeTaxLabel);

        appLabel = findViewById(R.id.appLabel);
        tcLabel = findViewById(R.id.tcLabel);
        aidLabel = findViewById(R.id.aidLabel);
        nameEmvCardLabel = findViewById(R.id.nameEmvCardLabel);

        appFrameLabel = findViewById(R.id.appFrameLabel);
        tcFrameLayout = findViewById(R.id.tcFrameLayout);
        aidFrameLayout = findViewById(R.id.aidFrameLayout);
        taxLinearLayout = findViewById(R.id.taxLinearLayout);
        copyLabel = findViewById(R.id.copyLabel);
        typeInputCardLabel = findViewById(R.id.typeInputCardLabel);
        comCodeFragment = findViewById(R.id.comCodeFragment);

        printBtn.setOnClickListener(this);
        printBtn.setEnabled(true);
        selectHealthCareSALE();
    }

    private void selectHealthCareSALE() {
            HealthCareDB healthCareDB = realm.where(HealthCareDB.class).equalTo("id", saleId).findFirst();
            Log.d(TAG, "selectSALE: " + healthCareDB.getCardNumber());
            setHealthDataView(healthCareDB);
//            setHealthDataViewAuto(healthCareDB);
    }

    private void setHealthDataView(HealthCareDB item) {

//        Preference.getInstance(SlipTemplateHealthCareActivity.this).setValueInt(Preference.KEY_SALE_VOID_PRINT_ID_TMS, item.getId());
//        Preference.getInstance(SlipTemplateHealthCareActivity.this).setValueString(Preference.KEY_SETTLE_TYPE_TMS, typeSlip);
        DecimalFormat decimalFormatShow = new DecimalFormat("#,##0.00");
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        tidLabel.setText(item.getTid());
        midLabel.setText(item.getMid());
        traceLabel.setText(item.getTraceNo());
        systrcLabel.setText(item.getTraceNo());
        Log.d(TAG, "setDataView getTraceNo: " + item.getTraceNo());
        batchLabel.setText(CardPrefix.calLen(Preference.getInstance(this).getValueString(Preference.KEY_BATCH_NUMBER_GHC), 6));
        batchTaxLayout.setText(CardPrefix.calLen(Preference.getInstance(this).getValueString(Preference.KEY_BATCH_NUMBER_GHC), 6));

        refNoLabel.setText(item.getRefNo());

        if (item.getStatusVoid().equals("Y")) {
            typeLabel.setText("VOID");
            amtThbLabel.setText(getString(R.string.slip_pattern_amount,decimalFormatShow.format(Double.valueOf(item.getAmount()))));
        } else {
            typeLabel.setText("SALE");
            amtThbLabel.setText(getString(R.string.slip_pattern_amount_void,decimalFormatShow.format(Double.valueOf(item.getAmount()))));
        }
        typeCardLabel.setText(CardPrefix.getTypeCardName(item.getCardNumber()));
        cardNoLabel.setText(item.getCardNumber());
        apprCodeLabel.setText(item.getApprCode());

        comCodeFragment.setVisibility(View.GONE);

    }

    /*private void setHealthDataViewAuto(HealthCareDB item) {
        DecimalFormat decimalFormatShow = new DecimalFormat("#,##0.00");
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        tidLabelAuto.setText(item.getTid());
        midLabelAuto.setText(item.getMid());
        traceLabelAuto.setText(item.getEcr());
        systrcLabelAuto.setText(item.getTraceNo());
        batchLabelAuto.setText(CardPrefix.calLen(Preference.getInstance(this).getValueString(Preference.KEY_BATCH_NUMBER_GHC), 6));
        batchTaxLayoutAuto.setText(CardPrefix.calLen(Preference.getInstance(this).getValueString(Preference.KEY_BATCH_NUMBER_GHC), 6));
        refNoLabelAuto.setText(item.getRefNo());

//        timeLabelAuto.setText(item.getTransTime());

        if (!item.getHostTypeCard().equals("TMS")) {
            comCodeFragmentAuto.setVisibility(View.GONE);
            taxIdLayoutAuto.setText(Preference.getInstance(SlipTemplateHealthCareActivity.this).getValueString(Preference.KEY_TAX_ID));
            taxAbbLayoutAuto.setText(item.getTaxAbb());
            traceTaxLayoutAuto.setText(item.getEcr());
//            dateTaxLayoutAuto.setText(item.getTransDate());
            String day = item.getTransDate().substring(6, 8);
            String mount = item.getTransDate().substring(4, 6);
            String year = item.getTransDate().substring(2, 4);
            Date date = new Date();
            if (!item.getHostTypeCard().equalsIgnoreCase("POS")) {
                dateLabelAuto.setText(new SimpleDateFormat("dd/MM/yy").format(date));
                timeLabelAuto.setText(new SimpleDateFormat("HH:mm:ss").format(date));
                dateTaxLayoutAuto.setText(new SimpleDateFormat("dd/MM/yy").format(date));
                timeTaxLayoutAuto.setText(new SimpleDateFormat("HH:mm:ss").format(date));
            } else {
                dateLabelAuto.setText(day + "/" + mount + "/" + year);
                timeLabelAuto.setText(item.getTransTime());
                dateTaxLayoutAuto.setText(day + "/" + mount + "/" + year);
                timeTaxLayoutAuto.setText(item.getTransTime());
            }

            *//*dateTaxLayoutAuto.setText(day + "/" + mount + "/" + year);
            timeTaxLayoutAuto.setText(item.getTransTime());*//*

            if (item.getEmvAppLabel().isEmpty()) {
                appLabelAuto.setText(item.getEmvAppLabel());
            } else {
                appFrameLabelAuto.setVisibility(View.GONE);
            }
            if (item.getEmvTc().isEmpty()) {
                tcLabelAuto.setText(item.getEmvTc());
            } else {
                tcFrameLayoutAuto.setVisibility(View.GONE);
            }
            if (item.getEmvAid().isEmpty()) {
                aidLabelAuto.setText(item.getEmvAid());
            } else {
                aidFrameLayoutAuto.setVisibility(View.GONE);
            }
        } else {
            comCodeFragmentAuto.setVisibility(View.VISIBLE);
            Date date = new Date();
            *//*String day = item.getTransDate().substring(6, 8);
            String mount = item.getTransDate().substring(4, 6);
            String year = item.getTransDate().substring(2, 4);
            dateLabelAuto.setText(day + "/" + mount + "/" + year);*//*
            dateLabelAuto.setText(new SimpleDateFormat("dd/MM/yy").format(date));
            timeLabelAuto.setText(new SimpleDateFormat("HH:mm:ss").format(date));

            *//*dateTaxLayoutAuto.setText(day + "/" + mount + "/" + year);
            timeTaxLayoutAuto.setText(item.getTransTime());*//*

            texView.setVisibility(View.GONE);
            appFrameLabelAuto.setVisibility(View.GONE);
            tcFrameLayoutAuto.setVisibility(View.GONE);
            aidFrameLayoutAuto.setVisibility(View.GONE);
            taxLinearLayoutAuto.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 20, 0, 100);
            copyLabelAuto.setLayoutParams(lp);
            copyLabelAuto.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        if (item.getEmvNameCardHolder() != null) {
            nameEmvCardLabelAuto.setText(item.getEmvNameCardHolder().trim());
        }
        if (item.getTransType().equals("I")) {
            typeInputCardLabelAuto.setText("C");
        } else {
            typeInputCardLabelAuto.setText("S");
        }
//        typeLabelAuto.setText(item.getTransStat());
        if (item.getVoidFlag().equals("Y")) {
            typeLabelAuto.setText("VOID");
        } else {
            typeLabelAuto.setText("SALE");
        }
        typeCardLabelAuto.setText(CardPrefix.getTypeCardName(item.getCardNo()));
        String cutCardStart = item.getCardNo().substring(0, 6);
        String cutCardEnd = item.getCardNo().substring(12, item.getCardNo().length());
        String cardNo = cutCardStart + "XXXXXX" + cutCardEnd;

        cardNoLabelAuto.setText(cardNo.substring(0, 4) + " " + cardNo.substring(4, 8) + " " + cardNo.substring(8, 12) + " " + cardNo.substring(12, 16));
        apprCodeLabelAuto.setText(item.getApprvCode());
//        comCodeLabelAuto.setText(item.getComCode());
        if (typeSlip.equalsIgnoreCase(CalculatePriceActivity.TypeSale)) {
            feeTaxLayoutAuto.setText(getString(R.string.slip_pattern_amount, item.getFee()));
            amtThbLabelAuto.setText(getString(R.string.slip_pattern_amount, decimalFormatShow.format(Double.valueOf(item.getAmount()))));
            Log.d(TAG, "setDataView if : " + item.getAmount() + " Fee : " + item.getFee());
            if (item.getHostTypeCard().equals("TMS")) {
                if (!item.getEmciFree().isEmpty()) {
                    feeThbLabelAuto.setText(getString(R.string.slip_pattern_amount, decimalFormat.format(Float.valueOf(item.getEmciFree()))));
                    float fee = Float.parseFloat(decimalFormat.format(Float.valueOf(item.getEmciFree())));
                    float amount = Float.parseFloat(decimalFormat.format(Float.valueOf(item.getAmount())));
                    totThbLabelAuto.setText(getString(R.string.slip_pattern_amount, decimalFormatShow.format((float) (amount + fee))));
                } else {
                    feeThbLabelAuto.setText(getString(R.string.slip_pattern_amount, "0.00"));
                    totThbLabelAuto.setText(getString(R.string.slip_pattern_amount, "0.00"));
                }
            } else if (item.getFee() != null) {
                feeThbLabelAuto.setText(getString(R.string.slip_pattern_amount, decimalFormat.format(Double.valueOf(item.getFee()))));
                double fee = Double.parseDouble(decimalFormat.format(Double.valueOf(item.getFee())));
                double amount = Double.parseDouble(decimalFormat.format(Double.valueOf(item.getAmount())));
                totThbLabelAuto.setText(getString(R.string.slip_pattern_amount, decimalFormatShow.format((float) (amount + fee))));
            } else {
                feeThbLabelAuto.setText(getString(R.string.slip_pattern_amount, "0.00"));
                totThbLabelAuto.setText(getString(R.string.slip_pattern_amount, "0.00"));
            }
        } else {
            feeTaxLayoutAuto.setText(getString(R.string.slip_pattern_amount_void, item.getFee()));
            amtThbLabelAuto.setText(getString(R.string.slip_pattern_amount_void, decimalFormatShow.format(Float.valueOf(item.getAmount()))));
            Log.d(TAG, "setDataView Else : " + item.getAmount() + " Fee : " + item.getFee());
            if (item.getHostTypeCard().equals("TMS")) {
                if (!item.getEmciFree().isEmpty()) {
                    feeThbLabelAuto.setText(getString(R.string.slip_pattern_amount_void, decimalFormat.format(Float.valueOf(item.getEmciFree()))));
                    float fee = Float.parseFloat(decimalFormat.format(Float.valueOf(item.getEmciFree())));
                    float amount = Float.parseFloat(decimalFormat.format(Float.valueOf(item.getAmount())));
                    totThbLabelAuto.setText(getString(R.string.slip_pattern_amount_void, decimalFormatShow.format((float) (amount + fee))));
                } else {
                    feeThbLabelAuto.setText(getString(R.string.slip_pattern_amount_void, "0.00"));
                    totThbLabelAuto.setText(getString(R.string.slip_pattern_amount_void, "0.00"));
                }
            } else {
                if (item.getFee() != null) {
                    feeThbLabelAuto.setText(getString(R.string.slip_pattern_amount_void, decimalFormatShow.format(Float.valueOf(item.getFee()))));
                    float fee = Float.parseFloat(decimalFormat.format(Float.valueOf(item.getFee())));
                    float amount = Float.parseFloat(decimalFormat.format(Float.valueOf(item.getAmount())));
                    totThbLabelAuto.setText(getString(R.string.slip_pattern_amount_void, decimalFormatShow.format((float) (amount + fee))));
                } else {
                    feeThbLabelAuto.setText(getString(R.string.slip_pattern_amount_void, "0.00"));
                    totThbLabelAuto.setText(getString(R.string.slip_pattern_amount_void, "0.00"));
                }
            }
        }
        String valueParameterEnable = Preference.getInstance(SlipTemplateHealthCareActivity.this).getValueString(Preference.KEY_TAG_1000);
        if (valueParameterEnable.substring(0, 1).equalsIgnoreCase("3")) {
            comCodeLabelAuto.setVisibility(View.VISIBLE);
            comCodeLabelAuto.setText(item.getComCode());
        } else if (valueParameterEnable.substring(0, 1).equalsIgnoreCase("4")) {
            comCodeLabelAuto.setVisibility(View.VISIBLE);
            comCodeLabelAuto.setText(item.getComCode());
        } else if (valueParameterEnable.substring(0, 1).equalsIgnoreCase("2")) {
            comCodeLabelAuto.setVisibility(View.GONE);
            comCodeLabelAuto.setText(item.getComCode());
        }
        if (valueParameterEnable.substring(1, 2).equalsIgnoreCase("3")) {
            ref1RelativeLayoutAuto.setVisibility(View.VISIBLE);
            ref1LabelAuto.setText(item.getRef1());
        } else if (valueParameterEnable.substring(1, 2).equalsIgnoreCase("4")) {
            ref1RelativeLayoutAuto.setVisibility(View.VISIBLE);
            ref1LabelAuto.setText(item.getRef1());
        } else if (valueParameterEnable.substring(1, 2).equalsIgnoreCase("2")) {
            ref1RelativeLayoutAuto.setVisibility(View.GONE);
            ref1LabelAuto.setText(item.getRef1());
        }
        if (valueParameterEnable.substring(2, 3).equalsIgnoreCase("3")) {
            ref2RelativeLayoutAuto.setVisibility(View.VISIBLE);
            ref2LabelAuto.setText(item.getRef2());
        } else if (valueParameterEnable.substring(2, 3).equalsIgnoreCase("4")) {
            ref2RelativeLayoutAuto.setVisibility(View.VISIBLE);
            ref2LabelAuto.setText(item.getRef2());
        } else if (valueParameterEnable.substring(2, 3).equalsIgnoreCase("2")) {
            ref2RelativeLayoutAuto.setVisibility(View.GONE);
            ref2LabelAuto.setText(item.getRef2());
        }
        if (valueParameterEnable.substring(3, 4).equalsIgnoreCase("3")) {
            ref3RelativeLayoutAuto.setVisibility(View.VISIBLE);
            ref3LabelAuto.setText(item.getRef3());
        } else if (valueParameterEnable.substring(3, 4).equalsIgnoreCase("4")) {
            ref3RelativeLayoutAuto.setVisibility(View.VISIBLE);
            ref3LabelAuto.setText(item.getRef3());
        } else if (valueParameterEnable.substring(3, 4).equalsIgnoreCase("2")) {
            ref3RelativeLayoutAuto.setVisibility(View.GONE);
            ref3LabelAuto.setText(item.getRef3());
        }

        setMeasure();

        new CountDownTimer(1500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                doPrinting(getBitmapFromView(slipLinearLayoutAuto));
                autoPrint();
            }
        }.start();
    }*/

    private void setMeasure() {
        printFirst.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        printFirst.layout(0, 0, printFirst.getMeasuredWidth(), printFirst.getMeasuredHeight());
    }

    private void autoPrint() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                statusOutScress = true;
                doPrinting(getBitmapFromView(slipLinearLayout));
            }
        };
        timer.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (realm.isClosed()) {
            realm = Realm.getDefaultInstance();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        realm.close();
    }

    public void doPrinting(Bitmap slip) {
        bitmapOld = slip;
        new Thread() {
            @Override
            public void run() {
                try {
                    printDev.initPrinter();
                    //This interface is used for set the gray level of the printing
                    //MIN is 0,Max is 4
                    //The level bigger, the speed of print is smaller
                    printDev.setPrinterGray(2);
                    printDev.printBmpFast(bitmapOld, Constant.ALIGN.CENTER, new AidlPrinterStateChangeListener.Stub() {
                        @Override
                        public void onPrintFinish() throws RemoteException {
                            Log.d(TAG, "onPrintFinish: ");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    printBtn.setEnabled(true);
                                }
                            });
                            if (statusOutScress) {
                                Intent intent = new Intent(SlipTemplateHealthCareActivity.this, MenuServiceActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0, 0);
                            } else {
                                if (timer != null) {
                                    timer.cancel();
                                    timer.start();
                                }
                            }
                        }

                        @Override
                        public void onPrintError(int i) throws RemoteException {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "onPrintError: ");
                                    msgLabel.setText("เกิดข้อผิดพลาด");
                                    dialogOutOfPaper.show();
                                }
                            });

                        }

                        @Override
                        public void onPrintOutOfPaper() throws RemoteException {
                            Log.d(TAG, "onPrintOutOfPaper: ");
                            if (!statusOutScress) {
                                timer.cancel();
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    dialogOutOfPaper.show();
                                }
                            });
                        }
                    });
//                    int ret = printDev.printBarCodeSync("asdasd");
//                    Log.d(TAG, "after call printData ret = " + ret);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    @Override
    public void onClick(View v) {
        if (v == printBtn) {
            statusOutScress = true;
            printBtn.setEnabled(false);
            doPrinting(getBitmapFromView(slipLinearLayout));
            if (timer != null) {
                timer.cancel();
            }
        }
    }

    private void customDialogOutOfPaper() {
        dialogOutOfPaper = new Dialog(this);
        dialogOutOfPaper.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOutOfPaper.setContentView(R.layout.dialog_custom_printer);
        dialogOutOfPaper.setCancelable(false);
        dialogOutOfPaper.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogOutOfPaper.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        okBtn = dialogOutOfPaper.findViewById(R.id.okBtn);
        msgLabel = dialogOutOfPaper.findViewById(R.id.msgLabel);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPrinting(bitmapOld);
                dialogOutOfPaper.dismiss();
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

}
