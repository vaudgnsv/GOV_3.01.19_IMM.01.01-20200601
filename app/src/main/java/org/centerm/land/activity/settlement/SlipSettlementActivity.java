package org.centerm.land.activity.settlement;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.RemoteException;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.centerm.smartpos.aidl.printer.AidlPrinter;
import com.centerm.smartpos.constant.Constant;

import org.centerm.land.CardManager;
import org.centerm.land.MainApplication;
import org.centerm.land.R;
import org.centerm.land.bassactivity.SettingToolbarActivity;
import org.centerm.land.database.TransTemp;
import org.centerm.land.utility.Preference;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class SlipSettlementActivity extends SettingToolbarActivity {

    private final String TAG = "SlipSettlementActivity";
    private NestedScrollView slipNestedScrollView = null;
    private LinearLayout settlementLinearLayout;
    private TextView dateLabel = null;
    private TextView timeLabel = null;
    private TextView midLabel = null;
    private TextView tidLabel = null;
    private TextView batchLabel = null;
    private TextView hostLabel = null;
    private TextView saleCountLabel = null;
    private TextView saleTotalLabel = null;
    private TextView voidSaleCountLabel = null;
    private TextView voidSaleAmountLabel = null;
    private TextView cardCountLabel = null;
    private TextView cardAmountLabel = null;
    private ImageView bank1Image = null;
    private ImageView bankImage = null;

    private Realm realm = null;

    private String typeHost = null;
    private CardManager cardManager = null;
    private AidlPrinter printDev;

    private Dialog dialogAlertLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_slip_settlement);
        cardManager = MainApplication.getCardManager();
        printDev = cardManager.getInstancesPrint();
        initData();
        initWidget();
        initBtnExit();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            typeHost = bundle.getString(MenuSettlementActivity.KEY_TYPE_HOST);
        }
    }

    @Override
    public void initWidget() {
//        super.initWidget();
        slipNestedScrollView = findViewById(R.id.slipNestedScrollView);
        settlementLinearLayout = findViewById(R.id.settlementLinearLayout);
        dateLabel = findViewById(R.id.dateLabel);
        timeLabel = findViewById(R.id.timeLabel);
        midLabel = findViewById(R.id.midLabel);
        tidLabel = findViewById(R.id.tidLabel);
        batchLabel = findViewById(R.id.batchLabel);
        hostLabel = findViewById(R.id.hostLabel);
        saleCountLabel = findViewById(R.id.saleCountLabel);
        saleTotalLabel = findViewById(R.id.saleTotalLabel);
        voidSaleCountLabel = findViewById(R.id.voidSaleCountLabel);
        voidSaleAmountLabel = findViewById(R.id.voidSaleAmountLabel);
        cardCountLabel = findViewById(R.id.cardCountLabel);
        cardAmountLabel = findViewById(R.id.cardAmountLabel);
        bank1Image = findViewById(R.id.bank1Image);
        bankImage = findViewById(R.id.bankImage);
        customDialogAlertLoading();

        setViewSlip();
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: " + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                dialogAlertLoading.dismiss();
                doPrinting(getBitmapFromView(settlementLinearLayout));
                cardManager.deleteTransTemp();
            }
        }.start();

    }

    public void customDialogAlertLoading() {
        dialogAlertLoading = new Dialog(this);
        dialogAlertLoading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAlertLoading.setContentView(R.layout.dialog_custom_alert_loading);
        dialogAlertLoading.setCancelable(false);
        dialogAlertLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAlertLoading.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialogAlertLoading.show();
    }


    private void setViewSlip() {
        if (realm == null) {
            realm = Realm.getDefaultInstance();
        }
        RealmResults<TransTemp> transTemp = realm.where(TransTemp.class).equalTo("voidFlag","N").equalTo("hostTypeCard",typeHost).findAll();
        float amountSale = 0;
        float amountVoid = 0;
        for (int i = 0; i < transTemp.size(); i++) {
            amountSale += Float.valueOf(transTemp.get(i).getAmount());
        }
        RealmResults<TransTemp> transTempVoid = realm.where(TransTemp.class).equalTo("voidFlag","Y").equalTo("hostTypeCard",typeHost).findAll();
        for (int i = 0; i < transTempVoid.size(); i++) {
            amountVoid += Float.valueOf(transTempVoid.get(i).getAmount());
        }
        Date date = new Date();

        voidSaleCountLabel.setText(transTempVoid.size()+"");
        voidSaleAmountLabel.setText(String.format("%.2f",amountVoid));
        saleCountLabel.setText(transTemp.size()+"");
        saleTotalLabel.setText(String.format("%.2f",amountSale));
        cardCountLabel.setText((transTemp.size() + transTempVoid.size()) + "");
        cardAmountLabel.setText(String.format("%.2f",amountVoid + amountSale));
        dateLabel.setText(new SimpleDateFormat("dd/MM/yyyy").format(date));
        timeLabel.setText(new SimpleDateFormat("HH:mm:ss").format(date));
        if (typeHost.equalsIgnoreCase("POS")) {
            hostLabel.setText("OFFUS POS");
            batchLabel.setText(Preference.getInstance(this).getValueString(Preference.KEY_BATCH_NUMBER_POS));
            tidLabel.setText(Preference.getInstance(this).getValueString(Preference.KEY_TERMINAL_ID_POS));
            midLabel.setText(Preference.getInstance(this).getValueString(Preference.KEY_MERCHANT_ID_POS));
        } else if (typeHost.equalsIgnoreCase("EPS")) {
            hostLabel.setText("OFFUS EPS");
            batchLabel.setText(Preference.getInstance(this).getValueString(Preference.KEY_BATCH_NUMBER_EPS));
            tidLabel.setText(Preference.getInstance(this).getValueString(Preference.KEY_TERMINAL_ID_EPS));
            midLabel.setText(Preference.getInstance(this).getValueString(Preference.KEY_MERCHANT_ID_EPS));
        } else {
            hostLabel.setText("KTB ONUS");
            batchLabel.setText(Preference.getInstance(this).getValueString(Preference.KEY_BATCH_NUMBER_TMS));
            tidLabel.setText(Preference.getInstance(this).getValueString(Preference.KEY_TERMINAL_ID_TMS));
            midLabel.setText(Preference.getInstance(this).getValueString(Preference.KEY_MERCHANT_ID_TMS));
        }
        realm.close();
        realm = null;
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

    public void doPrinting(final Bitmap slip) {
        new Thread() {
            @Override
            public void run() {
                try {
                    printDev.initPrinter();
                    int ret = printDev.printBmpFastSync(slip, Constant.ALIGN.CENTER);
//                    int ret = printDev.printBarCodeSync("asdasd");
                    Log.d(TAG, "after call printData ret = " + ret);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}