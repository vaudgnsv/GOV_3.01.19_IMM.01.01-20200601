package org.centerm.land.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.centerm.land.R;
import org.centerm.land.adapter.VoidAdapter;
import org.centerm.land.bassactivity.SettingToolbarActivity;
import org.centerm.land.database.TransTemp;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class PrintPreviousActivity extends SettingToolbarActivity {

    private EditText invoiceEt = null;
    private ImageView searchInvoiceImage = null;
    private Realm realm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_previous);
        initWidget();
        initBtnExit();
    }

    @Override
    public void initWidget() {
//        super.initWidget();
        invoiceEt = findViewById(R.id.invoiceEt);
        searchInvoiceImage = findViewById(R.id.searchInvoiceImage);

        invoiceEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchDataTransTemp(invoiceEt.getText().toString());
                    return true;
                }
                return false;
            }
        });
        searchInvoiceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchDataTransTemp(invoiceEt.getText().toString());
            }
        });
    }

    private void searchDataTransTemp(String traceNo) {
        RealmResults<TransTemp> transTemp;
        String traceNoAddZero = "";//???????????????????????????????????????????????? 6 ???????????????????????? 0 ????????????????????????
        if (!traceNo.isEmpty()) {
            if (traceNo.length() < 6) {
                for (int i = traceNo.length(); i < 6; i++) {
                    traceNoAddZero += "0";
                }
            }
            traceNoAddZero += traceNo;
//            transTemp = realm.where(TransTemp.class).equalTo("ecr", traceNoAddZero).equalTo("hostTypeCard", typeHost).findAll();
//
//            if (transTemp.size() > 0) {
//
//            }


        } else {
//            setVoidList();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }
}
