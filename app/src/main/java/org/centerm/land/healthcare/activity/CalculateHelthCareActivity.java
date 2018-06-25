package org.centerm.land.healthcare.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.centerm.land.CardManager;
import org.centerm.land.MainApplication;
import org.centerm.land.R;
import org.centerm.land.core.BlockCalculateUtil;
import org.centerm.land.healthcare.baseavtivity.BaseHealthCardActivity;
import org.centerm.land.healthcare.database.HealthCareDB;
import org.centerm.land.healthcare.model.CardId;
import org.centerm.land.helper.CardPrefix;
import org.centerm.land.utility.Preference;
import org.centerm.land.utility.Utility;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;

public class CalculateHelthCareActivity extends BaseHealthCardActivity implements View.OnClickListener {

    private final String TAG = "CalculatePriceActivity";

    public static final String KEY_CALCULATE_ID_HGC = CalculateHelthCareActivity.class.getName() + "_key_calcuate_id_hgc";

    private Realm realm;

    private TextView idCardLabel = null;
    private FrameLayout sevenClickFrameLayout = null;
    private FrameLayout eightClickFrameLayout = null;
    private FrameLayout nineClickFrameLayout = null;
    private FrameLayout fourClickFrameLayout = null;
    private FrameLayout fiveClickFrameLayout = null;
    private FrameLayout sixClickFrameLayout = null;
    private FrameLayout oneClickFrameLayout = null;
    private FrameLayout twoClickFrameLayout = null;
    private FrameLayout threeClickFrameLayout = null;
    private FrameLayout zeroClickFrameLayout = null;
    private FrameLayout dotClickFrameLayout = null;

    private FrameLayout exitClickFrameLayout = null;
    private FrameLayout deleteClickFrameLayout = null;
    private FrameLayout sureClickFrameLayout = null;
    private TextView priceLabel = null;

    private String numberPrice = "";

    private CardId cardId = null;

    private CardManager cardManager;
    private Dialog dialogWaiting;
    private String[] mBlockDataSend;
    private String TPDU;
    private String TERMINAL_ID;
    private String MERCHANT_NUMBER;
    private String invoiceNumber;
    private String statusSale;

    private String idForeigner=null;        // Paul_20180622

    private int saleId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_helth);
        realm = Realm.getDefaultInstance();
        cardManager = MainApplication.getCardManager();
        initData();
        initWidget();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getParcelable(IDActivity.KEY_CARD_ID_DATA) != null) {
                cardId = bundle.getParcelable(IDActivity.KEY_CARD_ID_DATA);
                Log.d(TAG, "initData: cardId : " + cardId.getIdCard() + "\n cardId : " + cardId.getThName());
            }
            if (bundle.getString(MedicalTreatmentActivity.KEY_STATUS_SALE) != null) {
                statusSale = bundle.getString(MedicalTreatmentActivity.KEY_STATUS_SALE);
                Log.d(TAG, "statusSale: " + statusSale);
                idForeigner = bundle.getString(MedicalTreatmentActivity.KEY_ID_FOREIGNER_NUMBER);
            }
        }
    }

    public void initWidget() {

        idCardLabel = findViewById(R.id.idCardLabel);

        oneClickFrameLayout = findViewById(R.id.oneClickFrameLayout);
        twoClickFrameLayout = findViewById(R.id.twoClickFrameLayout);
        threeClickFrameLayout = findViewById(R.id.threeClickFrameLayout);
        fourClickFrameLayout = findViewById(R.id.fourClickFrameLayout);
        fiveClickFrameLayout = findViewById(R.id.fiveClickFrameLayout);
        sixClickFrameLayout = findViewById(R.id.sixClickFrameLayout);
        sevenClickFrameLayout = findViewById(R.id.sevenClickFrameLayout);
        eightClickFrameLayout = findViewById(R.id.eightClickFrameLayout);
        nineClickFrameLayout = findViewById(R.id.nineClickFrameLayout);
        zeroClickFrameLayout = findViewById(R.id.zeroClickFrameLayout);
        dotClickFrameLayout = findViewById(R.id.dotClickFrameLayout);

        deleteClickFrameLayout = findViewById(R.id.deleteClickFrameLayout);
        sureClickFrameLayout = findViewById(R.id.sureClickFrameLayout);
        exitClickFrameLayout = findViewById(R.id.exitClickFrameLayout);
        priceLabel = findViewById(R.id.priceLabel);
        switch (statusSale) {
            case MedicalTreatmentActivity.KEY_TYPE_FAMILY:
                if (cardId != null) {
                    idCardLabel.setText(cardId.getIdCard());
                }
                break;
            case MedicalTreatmentActivity.KEY_TYPE_MIN_SEVEN:

                break;

            case MedicalTreatmentActivity.KEY_TYPE_FOREIGNER:
                idCardLabel.setText(idForeigner);
                break;

            case MedicalTreatmentActivity.KEY_TYPE_NO_CARD:
                idCardLabel.setText(idForeigner);
                break;
        }

        oneClickFrameLayout.setOnClickListener(this);
        twoClickFrameLayout.setOnClickListener(this);
        threeClickFrameLayout.setOnClickListener(this);
        fourClickFrameLayout.setOnClickListener(this);
        fiveClickFrameLayout.setOnClickListener(this);
        sixClickFrameLayout.setOnClickListener(this);
        sevenClickFrameLayout.setOnClickListener(this);
        eightClickFrameLayout.setOnClickListener(this);
        nineClickFrameLayout.setOnClickListener(this);
        zeroClickFrameLayout.setOnClickListener(this);
        dotClickFrameLayout.setOnClickListener(this);

        deleteClickFrameLayout.setOnClickListener(this);
        sureClickFrameLayout.setOnClickListener(this);
        exitClickFrameLayout.setOnClickListener(this);
        customDialogWaiting();

    }

    private void customDialogWaiting() {
        dialogWaiting = new Dialog(this);
        dialogWaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWaiting.setContentView(R.layout.dialog_custom_load_process);
        dialogWaiting.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogWaiting.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        ImageView waitingImage = dialogWaiting.findViewById(R.id.waitingImage);
        AnimationDrawable animationDrawable = (AnimationDrawable) waitingImage.getBackground();
        animationDrawable.start();
    }

    private void checkNumberPrice() {
        if (numberPrice.equalsIgnoreCase("0.00")) {
            numberPrice = "";
        }
    }

    private void submitAmount() {
        Log.d(TAG, "submitAmount: " + numberPrice);
        if (!priceLabel.getText().toString().equalsIgnoreCase("0.00") &&
                !priceLabel.getText().toString().equalsIgnoreCase("0") &&
                !priceLabel.getText().toString().equalsIgnoreCase("0.") &&
                !priceLabel.getText().toString().equalsIgnoreCase("0.0")) {
            switch (statusSale) {
                case MedicalTreatmentActivity.KEY_TYPE_FAMILY:
                    sendDataSaleFamily(priceLabel.getText().toString());
                    break;
                case MedicalTreatmentActivity.KEY_TYPE_MIN_SEVEN:

                    break;
                case MedicalTreatmentActivity.KEY_TYPE_FOREIGNER:
                    Log.d(TAG, "submitAmount: 13 ");
                    sendDataSaleForeigner(priceLabel.getText().toString());
                    break;
                case MedicalTreatmentActivity.KEY_TYPE_NO_CARD:
                    sendDataSaleNoCard(priceLabel.getText().toString());
                    break;
            }
//            sendDataSaleTest(priceLabel.getText().toString());
        } else {
            Utility.customDialogAlert(CalculateHelthCareActivity.this, "กรุณาใส่จำนวนเงิน", new Utility.OnClickCloseImage() {
                @Override
                public void onClickImage(Dialog dialog) {
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Click number: " + numberPrice);
        String[] splitter = null;
        Log.d(TAG, "onClick: " + numberPrice.contains("."));
        if (numberPrice.length() < 8) {
            if (!numberPrice.contains(".")) {
                Log.d(TAG, "if Main : ");
                if (!numberPrice.isEmpty())
                    if (numberPrice.substring(0, 1).equalsIgnoreCase("0"))
                        numberPrice = "";
                clickCal(v);
            } else {
                Log.d(TAG, "onClick: ");
                Log.d(TAG, "else Main : ");
                splitter = numberPrice.split("\\.");
                if (splitter.length > 1) {
                    Log.d(TAG, "if Sub : ");
                    if (splitter[1].length() > 1) {
                        Log.d(TAG, "splitter[1].length() > 1: ");
                        if (v == exitClickFrameLayout) {
                            cardManager.abortPBOCProcess();
                            finish();
                        } else if (v == deleteClickFrameLayout) {
                            if (!numberPrice.equalsIgnoreCase("0.00")) {
                                Log.d(TAG, "onClick: numberPrice.equalsIgnoreCase(\"0.00\") ");
                                if (numberPrice.length() == 0) {
                                    Log.d(TAG, "onClick: numberPrice.length() If == 0 ");
                                    numberPrice = "0.00";
                                } else {
                                    numberPrice = numberPrice.substring(0, numberPrice.length() - 1);
                                    if (numberPrice.length() == 0) {
                                        Log.d(TAG, "onClick: numberPrice.length() Else == 0 ");
                                        numberPrice = "0.00";
                                    }
                                }
                            } else {
                                if (!numberPrice.isEmpty()) {
                                    numberPrice = numberPrice.substring(0, numberPrice.length() - 1);
                                }
                            }
                        } else if (v == sureClickFrameLayout) {
                            submitAmount();
                        }
                    } else {
                        if (!numberPrice.isEmpty())
                            /*if (numberPrice.substring(0, 1).equalsIgnoreCase("0"))
                                numberPrice = "";*/
                            clickCal(v);
                    }
                } else {

                    Log.d(TAG, "splitter[1].length() > 1 Else: ");

                    if (!numberPrice.isEmpty())
                        /*if (numberPrice.substring(0, 1).equalsIgnoreCase("0"))
                            numberPrice = "";*/
                        Log.d(TAG, "else Sub : " + splitter.length);
                    Log.d(TAG, "else Sub : " + splitter[splitter.length - 1]);
                    clickCal(v);
                }
            }
        } else {
            if (v == exitClickFrameLayout) {
                cardManager.abortPBOCProcess();
                finish();
            } else if (v == deleteClickFrameLayout) {
                numberPrice = numberPrice.substring(0, numberPrice.length() - 1);
                if (numberPrice.length() == 0) {
                    numberPrice = "";
                    priceLabel.setText("0.00");
                }
            } else if (v == sureClickFrameLayout) {
                submitAmount();
            }
        }

        if (!numberPrice.isEmpty()) {
            priceLabel.setText(numberPrice);
        }
    }

    private void clickCal(View v) {

        if (v == oneClickFrameLayout) {
            checkNumberPrice();
            numberPrice += "1";
        } else if (v == twoClickFrameLayout) {
            checkNumberPrice();
            numberPrice += "2";
        } else if (v == threeClickFrameLayout) {
            checkNumberPrice();
            numberPrice += "3";
        } else if (v == fourClickFrameLayout) {
            checkNumberPrice();
            numberPrice += "4";
        } else if (v == fiveClickFrameLayout) {
            checkNumberPrice();
            numberPrice += "5";
        } else if (v == sixClickFrameLayout) {
            checkNumberPrice();
            numberPrice += "6";
        } else if (v == sevenClickFrameLayout) {
            checkNumberPrice();
            numberPrice += "7";
        } else if (v == eightClickFrameLayout) {
            checkNumberPrice();
            numberPrice += "8";
        } else if (v == nineClickFrameLayout) {
            checkNumberPrice();
            numberPrice += "9";
        } else if (v == zeroClickFrameLayout) {
            checkNumberPrice();
            numberPrice += "0";
        } else if (v == dotClickFrameLayout) {
            checkNumberPrice();
            if (!numberPrice.isEmpty()) {
                if (!numberPrice.contains(".")) {
                    numberPrice += ".";
                }
            } else {
                numberPrice += "0.";
            }
        } else if (v == exitClickFrameLayout) {
            cardManager.abortPBOCProcess();
            finish();
        } else if (v == deleteClickFrameLayout) {
            if (!priceLabel.getText().toString().equalsIgnoreCase("0.00")) {
                Log.d(TAG, "clickCal y: " + numberPrice);
                if (numberPrice.isEmpty()) {
                    Log.d(TAG, "clickCal u: " + numberPrice);
                    numberPrice = "";
                    priceLabel.setText("0.00");
                } else {
                    try {
                        Log.d(TAG, "clickCal 1: " + numberPrice);
                        numberPrice = numberPrice.substring(0, numberPrice.length() - 1);
                        Log.d(TAG, "clickCal 1: " + numberPrice);
                        if (numberPrice.equalsIgnoreCase("") || numberPrice == null) {
                            Log.d(TAG, "clickCal: if");
                            numberPrice = "";
                            priceLabel.setText("0.00");
                        } else {
                            Log.d(TAG, "clickCal: else");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } else if (v == sureClickFrameLayout) {
            submitAmount();
        }
    }


    @Override
    protected void connectTimeOut() {
        Utility.customDialogAlert(this, "ไม่สามารถเชื่อต่อได้", new Utility.OnClickCloseImage() {
            @Override
            public void onClickImage(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void transactionTimeOut() {
        Utility.customDialogAlert(this, "ไม่สามารถเชื่อต่อได้", new Utility.OnClickCloseImage() {
            @Override
            public void onClickImage(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void received(String[] data) {
        switch (statusSale) {
            case MedicalTreatmentActivity.KEY_TYPE_FAMILY:
                Date dateTime = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy");
                String second = data[12 - 1].substring(4, 6);
                String minute = data[12 - 1].substring(2, 4);
                String hour = data[12 - 1].substring(0, 2);
                String mount = data[13 - 1].substring(0, 2);
                String date = data[13 - 1].substring(2, 4);
                String EMCI_ID = "";
                /*if (!data[63 - 1].isEmpty()) {
                    EMCI_ID = data[63 - 1].substring(44, 44 + 18);
                    String EMCI_Fee = data[63 - 1].substring(62, 62 + 20);
                    Log.d(TAG, "EMCI_ID: " + EMCI_ID + " \n EMCI FEE : " + EMCI_Fee);
                }*/
                if (cardId.getIdCard().trim().replace(" ", "") != null) {
                    saveDataSale(dateFormat.format(dateTime) + mount + date,
                            String.valueOf(hour + minute + second),
                            cardId.getIdCard().trim().replace(" ", ""),
                            data[38 - 1], CardPrefix.calLen(BlockCalculateUtil.hexToString(data[37 - 1]), 12));
                } else {
                    saveDataSale(dateFormat.format(dateTime) + mount + date,
                            String.valueOf(hour + minute + second),
                            idForeigner,
                            data[38 - 1], CardPrefix.calLen(BlockCalculateUtil.hexToString(data[37 - 1]), 12));
                }
                receivedDataFamily(data[39 - 1]);
                break;
            case MedicalTreatmentActivity.KEY_TYPE_MIN_SEVEN:
                break;
            case MedicalTreatmentActivity.KEY_TYPE_FOREIGNER:
                Date dateTime3 = new Date();
                DateFormat dateFormat3 = new SimpleDateFormat("yyyy");
                String second3 = data[12 - 1].substring(4, 6);
                String minute3 = data[12 - 1].substring(2, 4);
                String hour3 = data[12 - 1].substring(0, 2);
                String mount3 = data[13 - 1].substring(0, 2);
                String date3 = data[13 - 1].substring(2, 4);
                String EMCI_ID3 = "";
                /*if (!data[63 - 1].isEmpty()) {
                    EMCI_ID = data[63 - 1].substring(44, 44 + 18);
                    String EMCI_Fee = data[63 - 1].substring(62, 62 + 20);
                    Log.d(TAG, "EMCI_ID: " + EMCI_ID + " \n EMCI FEE : " + EMCI_Fee);
                }*/
/*
                if (cardId.getIdCard().trim().replace(" ", "") != null) {
                    saveDataSale(dateFormat3.format(dateTime3) + mount3 + date3,
                            String.valueOf(hour3 + minute3 + second3),
                            cardId.getIdCard().trim().replace(" ", ""),
                            data[38 - 1], CardPrefix.calLen(BlockCalculateUtil.hexToString(data[37 - 1]), 12));
                } else {
*/
                    saveDataSale(dateFormat3.format(dateTime3) + mount3 + date3,
                            String.valueOf(hour3 + minute3 + second3),
                            idForeigner,
                            data[38 - 1], CardPrefix.calLen(BlockCalculateUtil.hexToString(data[37 - 1]), 12));
//                }
                receivedDataFamily(data[39 - 1]);
                break;
            case MedicalTreatmentActivity.KEY_TYPE_NO_CARD:
                receivedDataFamily(data[39 - 1]);
                break;
        }
    }


    @Override
    protected void error(String error) {
        Utility.customDialogAlert(this, "ไม่สามารถเชื่อต่อได้ " + error, new Utility.OnClickCloseImage() {
            @Override
            public void onClickImage(Dialog dialog) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void other() {
        Utility.customDialogAlert(this, "ไม่สามารถเชื่อต่อได้", new Utility.OnClickCloseImage() {
            @Override
            public void onClickImage(Dialog dialog) {
                dialog.dismiss();
            }
        });
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

    /**
     * Zone requestData
     */
    private void sendDataSaleFamily(String amount) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        TERMINAL_ID = CardPrefix.getTerminalId(CalculateHelthCareActivity.this, "GHC");
        MERCHANT_NUMBER = CardPrefix.getMerchantId(CalculateHelthCareActivity.this, "GHC");
//        invoiceNumber = CardPrefix.getInvoice(CalculateHelthCareActivity.this, "GHC");
        invoiceNumber = CardPrefix.getInvoice(CalculateHelthCareActivity.this, "GHC");
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        String amountReal = decimalFormat.format(Double.valueOf(amount));
        mBlockDataSend = new String[64];
        String cardNumber = "000" + cardId.getIdCard().trim().replace(" ", "");
        if ((cardNumber.length() % 2) != 0) {
            Log.d(TAG, "cardNumber: If");
            mBlockDataSend[2 - 1] = cardNumber.length() + cardNumber + "0";
        } else {
            Log.d(TAG, "cardNumber: Else");
            mBlockDataSend[2 - 1] = cardNumber.length() + cardNumber;
        }
//        mBlockDataSend[2 - 1] = "160003101100015314";
        mBlockDataSend[3 - 1] = "005000";
        mBlockDataSend[4 - 1] = BlockCalculateUtil.getAmount(amountReal);
        mBlockDataSend[11 - 1] = CardPrefix.calLen(CardPrefix.geTraceId(CalculateHelthCareActivity.this, "GHC"), 6);
        Preference.getInstance(CalculateHelthCareActivity.this).setValueString(Preference.KEY_TRACE_NO_GHC, String.valueOf(Integer.valueOf(Preference.getInstance(CalculateHelthCareActivity.this).getValueString(Preference.KEY_TRACE_NO_GHC)) + 1));
        mBlockDataSend[22 - 1] = "0022";
        mBlockDataSend[24 - 1] = "0444";
        mBlockDataSend[25 - 1] = "05";
        String cardNo = "000" + cardId.getIdCard().replace(" ", "") + "D22102200460000010006";
        if ((cardNo.length() % 2) != 0) {
            mBlockDataSend[35 - 1] = cardNo.length() + cardNo + "0";
        } else {
            mBlockDataSend[35 - 1] = cardNo.length() + cardNo;
        }
//        mBlockDataSend[35 - 1] = "37" + cardNo ;
        mBlockDataSend[41 - 1] = BlockCalculateUtil.getHexString(TERMINAL_ID);
        mBlockDataSend[42 - 1] = BlockCalculateUtil.getHexString(MERCHANT_NUMBER);
        mBlockDataSend[52 - 1] = "87F1594650284713";
        mBlockDataSend[62 - 1] = getLength62(String.valueOf(Utility.calNumTraceNo(invoiceNumber).length())) + BlockCalculateUtil.getHexString(Utility.calNumTraceNo(invoiceNumber));
        Preference.getInstance(CalculateHelthCareActivity.this).setValueString(Preference.KEY_INVOICE_NUMBER_GHC, String.valueOf(Integer.valueOf(Preference.getInstance(CalculateHelthCareActivity.this).getValueString(Preference.KEY_INVOICE_NUMBER_GHC)) + 1));
        String m63 = "0000034000000001000880150000019200000000HCG13814                                                    B                                                 M1                                                " + dateFormat.format(date) + "                        ";
        mBlockDataSend[63 - 1] = Utility.getLength62(String.valueOf(m63.length())) + BlockCalculateUtil.getHexString(m63);
        TPDU = CardPrefix.getTPDU(CalculateHelthCareActivity.this, "GHC");
        packageAndSend(TPDU, "0200", mBlockDataSend);
    }

    private void sendDataSaleForeigner(String amount) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        TERMINAL_ID = CardPrefix.getTerminalId(CalculateHelthCareActivity.this, "GHC");
        MERCHANT_NUMBER = CardPrefix.getMerchantId(CalculateHelthCareActivity.this, "GHC");
        invoiceNumber = CardPrefix.getInvoice(CalculateHelthCareActivity.this, "GHC");
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        String amountReal = decimalFormat.format(Double.valueOf(amount));
        mBlockDataSend = new String[64];
        String cardNumber = "9000" + idForeigner.replace("B", "");
        if ((cardNumber.length() % 2) != 0) {
            Log.d(TAG, "cardNumber: If");
            mBlockDataSend[2 - 1] = cardNumber.length() + cardNumber + "0";
        } else {
            Log.d(TAG, "cardNumber: Else");
            mBlockDataSend[2 - 1] = cardNumber.length() + cardNumber;
        }
//        mBlockDataSend[2 - 1] = "160003101100015314";
        mBlockDataSend[3 - 1] = "005000";
        mBlockDataSend[4 - 1] = BlockCalculateUtil.getAmount(amountReal);
        mBlockDataSend[11 - 1] = CardPrefix.calLen(CardPrefix.geTraceId(CalculateHelthCareActivity.this, "GHC"), 6);

        mBlockDataSend[22 - 1] = "0022";
        mBlockDataSend[24 - 1] = "0444";
        mBlockDataSend[25 - 1] = "05";
        String cardNo = "9000" + idForeigner.replace("B", "") + "D22102200460000010006";
        if ((cardNo.length() % 2) != 0) {
            mBlockDataSend[35 - 1] = cardNo.length() + cardNo + "0";
        } else {
            mBlockDataSend[35 - 1] = cardNo.length() + cardNo;
        }
//        mBlockDataSend[35 - 1] = "37" + cardNo ;
        mBlockDataSend[41 - 1] = BlockCalculateUtil.getHexString(TERMINAL_ID);
        mBlockDataSend[42 - 1] = BlockCalculateUtil.getHexString(MERCHANT_NUMBER);
        mBlockDataSend[52 - 1] = "87F1594650284713";
        mBlockDataSend[62 - 1] = getLength62(String.valueOf(Utility.calNumTraceNo(invoiceNumber).length())) + BlockCalculateUtil.getHexString(Utility.calNumTraceNo(invoiceNumber));
        String m63 = "0000034000000001000880150000019200000000HCG13814                                                    B                                                 M3                                                " + dateFormat.format(date) + "                        ";
        mBlockDataSend[63 - 1] = Utility.getLength62(String.valueOf(m63.length())) + BlockCalculateUtil.getHexString(m63);
        TPDU = CardPrefix.getTPDU(CalculateHelthCareActivity.this, "GHC");
        packageAndSend(TPDU, "0200", mBlockDataSend);
    }

    private void sendDataSaleNoCard(String amount) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        TERMINAL_ID = CardPrefix.getTerminalId(CalculateHelthCareActivity.this, "GHC");
        MERCHANT_NUMBER = CardPrefix.getMerchantId(CalculateHelthCareActivity.this, "GHC");
        invoiceNumber = CardPrefix.getInvoice(CalculateHelthCareActivity.this, "GHC");
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        String amountReal = decimalFormat.format(Double.valueOf(amount));
        mBlockDataSend = new String[64];
        String cardNumber = "000" + idForeigner;
        if ((cardNumber.length() % 2) != 0) {
            Log.d(TAG, "cardNumber: If");
            mBlockDataSend[2 - 1] = cardNumber.length() + cardNumber + "0";
        } else {
            Log.d(TAG, "cardNumber: Else");
            mBlockDataSend[2 - 1] = cardNumber.length() + cardNumber;
        }
//        mBlockDataSend[2 - 1] = "160003101100015314";
        mBlockDataSend[3 - 1] = "005000";
        mBlockDataSend[4 - 1] = BlockCalculateUtil.getAmount(amountReal);
        mBlockDataSend[11 - 1] = CardPrefix.calLen(CardPrefix.geTraceId(CalculateHelthCareActivity.this, "GHC"), 6);
        Preference.getInstance(CalculateHelthCareActivity.this).setValueString(Preference.KEY_TRACE_NO_GHC, String.valueOf(Integer.valueOf(Preference.getInstance(CalculateHelthCareActivity.this).getValueString(Preference.KEY_TRACE_NO_GHC)) + 1));
        mBlockDataSend[22 - 1] = "0022";
        mBlockDataSend[24 - 1] = "0444";
        mBlockDataSend[25 - 1] = "05";
        String cardNo = "000" + idForeigner + "D22102200460000010006";
        if ((cardNo.length() % 2) != 0) {
            mBlockDataSend[35 - 1] = cardNo.length() + cardNo + "0";
        } else {
            mBlockDataSend[35 - 1] = cardNo.length() + cardNo;
        }
//        mBlockDataSend[35 - 1] = "37" + cardNo ;
        mBlockDataSend[41 - 1] = BlockCalculateUtil.getHexString(TERMINAL_ID);
        mBlockDataSend[42 - 1] = BlockCalculateUtil.getHexString(MERCHANT_NUMBER);
        mBlockDataSend[52 - 1] = "87F1594650284713";
        mBlockDataSend[62 - 1] = getLength62(String.valueOf(Utility.calNumTraceNo(invoiceNumber).length())) + BlockCalculateUtil.getHexString(Utility.calNumTraceNo(invoiceNumber));
        String m63 = "0000034000000001000880150000019200000000HCG13814                                                    B                                                 M4                                                " + dateFormat.format(date) + "                        ";
        mBlockDataSend[63 - 1] = Utility.getLength62(String.valueOf(m63.length())) + BlockCalculateUtil.getHexString(m63);
        TPDU = CardPrefix.getTPDU(CalculateHelthCareActivity.this, "GHC");
        packageAndSend(TPDU, "0200", mBlockDataSend);
    }

    /**
     * Zone ReceivedData Action
     */

    private void receivedDataFamily(String data) {
        String m39 = BlockCalculateUtil.hexToString(data);
        if (m39.equalsIgnoreCase("00")) {

            Utility.customDialogAlertSuccess(this, null, new Utility.OnClickCloseImage() {
                @Override
                public void onClickImage(Dialog dialog) {
                    dialog.dismiss();
                    Intent intent = new Intent(CalculateHelthCareActivity.this, SlipTemplateHealthCareActivity.class);
                    intent.putExtra(KEY_CALCULATE_ID_HGC,saleId);
                    startActivity(intent);
                }
            });
        } else {
            Utility.customDialogAlert(this, "ไม่สามารถทำรายการได้  " + m39, new Utility.OnClickCloseImage() {
                @Override
                public void onClickImage(Dialog dialog) {
                    dialog.dismiss();
                }
            });
        }
    }


    /**
     * Zone Save Database
     */
    private void saveDataSale(final String date,
                              final String time,
                              final String cardNumber,
                              final String appCode,
                              final String refNo) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                String tid = Preference.getInstance(CalculateHelthCareActivity.this).getValueString(Preference.KEY_TERMINAL_ID_GHC);
                String mid = Preference.getInstance(CalculateHelthCareActivity.this).getValueString(Preference.KEY_MERCHANT_ID_GHC);
                String batch = Preference.getInstance(CalculateHelthCareActivity.this).getValueString(Preference.KEY_BATCH_NUMBER_GHC);
                String invoice = Preference.getInstance(CalculateHelthCareActivity.this).getValueString(Preference.KEY_INVOICE_NUMBER_GHC);

                DecimalFormat decimalFormat = new DecimalFormat("###0.00");
                Number currentId = realm.where(HealthCareDB.class).max("id");
                int nextId;
                if (currentId == null) {
                    nextId = 1;
                    saleId = nextId;
                } else {
                    nextId = currentId.intValue() + 1;
                    saleId = nextId;
                }
                HealthCareDB healthCareDB = realm.createObject(HealthCareDB.class, nextId);
                healthCareDB.setTraceNo(CardPrefix.calLen(CardPrefix.geTraceId(CalculateHelthCareActivity.this, "GHC"), 6));
                Preference.getInstance(CalculateHelthCareActivity.this).setValueString(Preference.KEY_TRACE_NO_GHC, String.valueOf(Integer.valueOf(Preference.getInstance(CalculateHelthCareActivity.this).getValueString(Preference.KEY_TRACE_NO_GHC)) + 1));
                healthCareDB.setAppid(String.valueOf(nextId));
                healthCareDB.setTid(Utility.calNumTraceNo(tid));
                healthCareDB.setMid(Utility.calNumTraceNo(mid));
                healthCareDB.setBatch(Utility.calNumTraceNo(batch));
                healthCareDB.setInvoice(Utility.calNumTraceNo(invoice));
                healthCareDB.setConditionCode(mBlockDataSend[25 - 1]);
                healthCareDB.setDe63Sale(mBlockDataSend[63 - 1]);
                healthCareDB.setDate(date);
                healthCareDB.setTime(time);
                healthCareDB.setComCode("");
                healthCareDB.setCardNumber(cardNumber);
                if (cardId != null) {
                    healthCareDB.setIdCard(cardId.getIdCard().replace(" ", ""));
                    healthCareDB.setThName(cardId.getThName());
                    healthCareDB.setEngFName(cardId.getEngFName());
                    healthCareDB.setEngLName(cardId.getEngLName());
                    healthCareDB.setEngBirth(cardId.getEngBirth());
                    healthCareDB.setThBirth(cardId.getThBirth());
                    healthCareDB.setAddress(cardId.getAddress());
                    healthCareDB.setEngIssue(cardId.getThIssue());
                    healthCareDB.setThIssue(cardId.getThName());
                    healthCareDB.setEngExpire(cardId.getThExpire());
                    healthCareDB.setThExpire(cardId.getThName());
                    healthCareDB.setReligion(cardId.getReligion());
                    healthCareDB.setXphoto(String.valueOf(cardId.getXphoto()));
                }
                healthCareDB.setApprCode(appCode);
                healthCareDB.setRefNo(refNo);
                healthCareDB.setTypeSale(statusSale);
                healthCareDB.setStatusVoid("N");
                healthCareDB.setAmount(decimalFormat.format(Double.valueOf(priceLabel.getText().toString())));
                realm.insert(healthCareDB);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(CalculateHelthCareActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                saleId = 0;
                Log.d(TAG, "onError: " + error.getMessage());
                Toast.makeText(CalculateHelthCareActivity.this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
