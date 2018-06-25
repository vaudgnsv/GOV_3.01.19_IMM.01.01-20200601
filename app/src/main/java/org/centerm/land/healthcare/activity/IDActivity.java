package org.centerm.land.healthcare.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.centerm.smartpos.aidl.iccard.AidlICCard;
import com.centerm.smartpos.aidl.sys.AidlDeviceManager;
import com.centerm.smartpos.constant.Constant;
import com.centerm.smartpos.util.HexUtil;

import org.centerm.land.R;
import org.centerm.land.healthcare.baseavtivity.devBase;
import org.centerm.land.healthcare.model.CardId;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by KisadaM on 7/13/2017.
 */

public class IDActivity extends devBase {

    public static final String KEY_CARD_ID_DATA = IDActivity.class.getName() + "_key_card_id_data";

    private AidlICCard iccard = null;
    private String _cmd = "00A4040008";
    private String _thai_id_card = "A000000054480001";
    private String _req_cid = "80b0000402000d";
    //private String _req_thai_name = "80b00011020064";
    //private String _req_eng_name = "80b00075020064";
    //private String _req_gender = "80b000E1020001";
    //private String _req_dob = "80b000D9020008";
    private String _cardno = "A9EF7B30159C2CFCE9E9AC218945213B";
    private String _req_address = "80b01579020064";
    private String _req_issue_expire = "80b00167020012";
    private String _req_full_name = "80b000110200d1";
    private final Charset _UTF8_CHARSET = Charset.forName("TIS-620");
    private List<String> months_eng = Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
    private List<String> months_th = Arrays.asList("ม.ค.", "ก.พ.", "มี.ค.", "เม.ษ.", "พ.ค.", "มิ.ย.", "ก.ค.", "ส.ค.", "ก.ย.", "ต.ค.", "พ.ย.", "ธ.ค.");
    private List<String> religions = Arrays.asList("ไม่นับถือศาสนา", "พุทธ", "อิสลาม", "คริสต์", "พราหมณ์-ฮินดู", "ซิกข์", "ยิว", "เชน", "โซโรอัสเตอร์", "บาไฮ", "ไม่ระบุ");

    private String _id_card;
    private String _thai_name;
    private String _eng_first_name;
    private String _eng_last_name;
    private String _birth_eng;
    private String _birth_th;
    //private String _gender_eng;
    //private String _gender_th;
    private String _address;
    private String _issue_eng;
    private String _issue_th;
    private String _expire_eng;
    private String _expire_th;
    private String _religion;
    private byte[] _photo;

    private TextView idcard;
    private TextView thname;
    private TextView engfname;
    private TextView englname;
    private TextView engbirth;
    private TextView thbirth;
    private TextView address;
    private TextView engissue;
    private TextView thissue;
    private TextView engexpire;
    private TextView thexpire;
    private TextView religion;
    private ImageView xphoto;
    private ProgressDialog mLoading;
    private Handler mHandler = new Handler();
    ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    private boolean isShowImage = false;

    private String statusSale;
    private final String TAG = "IDActivity";
    private Button nextBtn = null;
    private CardId cardId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.setContentView(R.layout.activity_idcard);
        super.onCreate(savedInstanceState);

        initData();
        cardId = new CardId();

        idcard = (TextView) findViewById(R.id.tVidcard);
        idcard.setText("");
        thname = (TextView) findViewById(R.id.tVnameTH);
        thname.setText("");
        engfname = (TextView) findViewById(R.id.tVfirstnameENG);
        engfname.setText("");
        englname = (TextView) findViewById(R.id.tVlastnameENG);
        englname.setText("");
        engbirth = (TextView) findViewById(R.id.tVbirthENG);
        engbirth.setText("");
        thbirth = (TextView) findViewById(R.id.tVbirthTH);
        thbirth.setText("");
        address = (TextView) findViewById(R.id.tVaddress);
        address.setText("");
        engissue = (TextView) findViewById(R.id.tVissueENG);
        engissue.setText("");
        thissue = (TextView) findViewById(R.id.tVissueTH);
        thissue.setText("");
        engexpire = (TextView) findViewById(R.id.tVexpireENG);
        engexpire.setText("");
        thexpire = (TextView) findViewById(R.id.tVexpireTH);
        thexpire.setText("");
        religion = (TextView) findViewById(R.id.tVreligion);
        religion.setText("");
        xphoto = (ImageView) findViewById(R.id.iVphoto);
        xphoto.setImageBitmap(null);
        xphoto.destroyDrawingCache();
        nextBtn = findViewById(R.id.nextBtn);

        mLoading = new ProgressDialog(this);
        mLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoading.setCanceledOnTouchOutside(false);
        mLoading.setMessage("Reading...");
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IDActivity.this, CalculateHelthCareActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(KEY_CARD_ID_DATA,cardId);
                bundle.putString(MedicalTreatmentActivity.KEY_STATUS_SALE,statusSale);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

//		ActionBar actionBar = getSupportActionBar();
//		actionBar.setDisplayShowHomeEnabled(true);
//		actionBar.setIcon(R.drawable.tsslogo72);
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            statusSale = bundle.getString(MedicalTreatmentActivity.KEY_STATUS_SALE);
            Log.d(TAG, "statusSale: " + statusSale);
        }
    }


    private void setDialogShowQuestion() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, android.R.style.Theme_Material_Light_Dialog_Alert);
        alertDialog.setTitle("คุณต้องการแสดงรูป ใช่ หรือ ไม่");
        alertDialog.setCancelable(false);
        alertDialog.setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isShowImage = false;
                try {
                    if (iccard != null) {
                        d();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isShowImage = true;
                try {
                    if (iccard != null) {
                        d();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (iccard != null) {
                scheduledExecutor.shutdown();
                iccard.close();
                iccard = null;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeviceConnected(AidlDeviceManager deviceManager) {
        try {
            this.iccard = AidlICCard.Stub.asInterface(deviceManager.getDevice(Constant.DEVICE_TYPE.DEVICE_TYPE_ICCARD));
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            setDialogShowQuestion();
        }
    }

    public void d() throws InterruptedException, ExecutionException {
        Runnable job = new Runnable() {
            boolean _read = false;

            @Override
            public void run() {
                try {
                    iccard.open();
                    if (iccard.status() == 1) {
                        if (iccard.reset() != null && !_read) {
                            if (iccard.sendAsync(HexUtil.hexStringToByte(_cmd + _thai_id_card)) != null) {
                                _read = true;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoading.show();
                                    }
                                });
                                a(new String(iccard.sendAsync(HexUtil.hexStringToByte(_req_cid)), _UTF8_CHARSET), 0);
                                a(new String(iccard.sendAsync(HexUtil.hexStringToByte(_req_full_name)), _UTF8_CHARSET), 1);
                                a(new String(iccard.sendAsync(HexUtil.hexStringToByte(_req_address)), _UTF8_CHARSET), 2);
                                a(new String(iccard.sendAsync(HexUtil.hexStringToByte(_req_issue_expire)), _UTF8_CHARSET), 3);
                                if (isShowImage)
                                    m();
                                //iccard.close();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mLoading.dismiss();
                                    }
                                });
                            }
                        }
                    } else {
                        _read = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                c();
                                mLoading.dismiss();
                            }
                        });
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            private byte[] r(byte[] data) {
                int index = data.length - 1;
                while ((index > 0) && (data[(index - 1)] == 32)) {
                    index--;
                    if (index == 0) {
                        return null;
                    }
                }
                return Arrays.copyOfRange(data, 0, index);
            }

            private void m() {
                try {
                    ByteArrayOutputStream _a = new ByteArrayOutputStream();
                    for (int i = 0; i < 20; i++) {
                        int xwd;
                        int xof = i * 254 + 379; //379-381
                        xwd = i == 20 ? 38 : 254;

                        String sp2 = e(xof >> 8 & 0xff);
                        String sp3 = e(xof & 0xff);
                        String sp6 = e(xwd & 0xff);

                        byte[] _xx = r(r(iccard.sendAsync(HexUtil.hexStringToByte("80B0" + sp2 + sp3 + "0200" + sp6)))); //0200 - 0201
                        if (_xx != null)
                            _a.write(_xx, 0, _xx.length);
                    }
                    _a.flush();
                    _photo = _a.toByteArray();
                    String _b = Base64.encodeToString(_photo, Base64.DEFAULT);
                    _a.close();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    _photo = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    _photo = null;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap _bm = BitmapFactory.decodeByteArray(_photo, 0, _photo.length);
                        xphoto.setImageBitmap(_bm);
                        cardId.setXphoto(_bm);
                    }
                });
            }

            private void c() {
                idcard.setText("");
                thname.setText("");
                engfname.setText("");
                englname.setText("");
                engbirth.setText("");
                thbirth.setText("");
                address.setText("");
                engissue.setText("");
                thissue.setText("");
                engexpire.setText("");
                thexpire.setText("");
                religion.setText("");
                xphoto.setImageBitmap(null);
                xphoto.destroyDrawingCache();
            }

            private String a(String _val, int _index) {
                String _xx = _val;
                switch (_index) {
                    case 0:
                        if (_xx != null | _xx.length() != 0) {
                            _xx = _val.replaceAll("#", " ");
                            _xx = _xx.substring(0, _xx.length() - 2);
                            char[] achars = _xx.toUpperCase().toCharArray();
                            _id_card = achars[0] + " " + achars[1] + achars[2] + achars[3] + achars[4] + " " + achars[5] + achars[6] + achars[7] + achars[8] + achars[9] + " " + achars[10] + achars[11] + " " + achars[12];
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    idcard.setText(_id_card);

                                    cardId.setIdCard(_id_card);
                                }
                            });
                        }
                        break;
                    case 1:
                        if (_xx != null | _xx.length() != 0) {
                            int _first_space = _val.indexOf(" ");
                            _thai_name = _xx.substring(0, _first_space).replaceAll("#", " ");
                            _xx = _xx.substring(_first_space, _xx.length() - 2);
                            _xx = _xx.trim();
                            _first_space = _xx.indexOf(" ");
                            String _eng_name = _xx.substring(0, _first_space).replaceAll("#", " ");
                            String[] _eng_name_list = _eng_name.split(" ");
                            _eng_first_name = _eng_name_list[0] + " " + _eng_name_list[1];
                            _eng_last_name = _eng_name_list[3];
                            _xx = _xx.substring(_first_space, _xx.length());
                            _xx = _xx.trim();
                            String _year_th = _xx.substring(0, 4);
                            String _year_eng = "" + (Integer.parseInt(_xx.substring(0, 4)) - 543);
                            String _month_eng = months_eng.get(Integer.parseInt(_xx.substring(4, 6)) - 1);
                            String _month_th = months_th.get(Integer.parseInt(_xx.substring(4, 6)) - 1);
                            String _day = "" + Integer.parseInt(_xx.substring(6, 8));
                            _birth_eng = _day + " " + _month_eng + " " + _year_eng;
                            _birth_th = _day + " " + _month_th + " " + _year_th;
							/*if(Integer.parseInt(_xx.substring(8, 9)) == 1) {
								_gender_eng = "Male";
								_gender_th = "ชาย";
							}else{
								_gender_eng = "Female";
								_gender_th = "หญิง";
							}*/
                            //_xx = _thai_name + "\n" + _eng_first_name + "\r\n" + _eng_last_name + "\r\n" + _birth_th + "\n" + _birth_eng + "\n" + _gender_eng + "\r\n" + _gender_th;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    thname.setText(_thai_name);
                                    engfname.setText(_eng_first_name);
                                    englname.setText(_eng_last_name);
                                    thbirth.setText(_birth_th);
                                    engbirth.setText(_birth_eng);

                                    /**
                                     * Set DataCard Id
                                     */
                                    cardId.setThName(_thai_name);
                                    cardId.setEngFName(_eng_first_name);
                                    cardId.setEngLName(_eng_last_name);
                                    cardId.setThBirth(_birth_th);
                                    cardId.setEngBirth(_birth_eng);

                                }
                            });
                        }
                        break;
                    case 2:
                        if (_xx != null | _xx.length() != 0) {
                            _xx = _val.replaceAll("#", " ");
                            _xx = _xx.substring(0, _xx.length() - 2);
                            _xx = _xx.replace("ตำบล", "ต.");
                            _xx = _xx.replace("อำเภอ", "อ.");
                            _xx = _xx.replace("จังหวัด", "จ.");
                            _address = "       " + _xx;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    address.setText(_address);
                                    /**
                                     * Set DataCard Id
                                     */
                                    cardId.setAddress(_address);
                                }
                            });
                        }
                        break;
                    case 3:
                        if (_xx != null | _xx.length() != 0) {
                            _xx = _val.replaceAll("#", " ");
                            _xx = _xx.substring(0, _xx.length() - 2);
                            String _year_th = _xx.substring(0, 4);
                            String _year_eng = "" + (Integer.parseInt(_xx.substring(0, 4)) - 543);
                            String _month_eng = months_eng.get(Integer.parseInt(_xx.substring(4, 6)) - 1);
                            String _month_th = months_th.get(Integer.parseInt(_xx.substring(4, 6)) - 1);
                            String _day = "" + Integer.parseInt(_xx.substring(6, 8));
                            _issue_eng = _day + " " + _month_eng + " " + _year_eng;
                            _issue_th = _day + " " + _month_th + " " + _year_th;

                            _year_th = _xx.substring(8, 12);
                            _year_eng = "" + (Integer.parseInt(_xx.substring(8, 12)) - 543);
                            _month_eng = months_eng.get(Integer.parseInt(_xx.substring(12, 14)) - 1);
                            _month_th = months_th.get(Integer.parseInt(_xx.substring(12, 14)) - 1);
                            _day = "" + Integer.parseInt(_xx.substring(14, 16));
                            _expire_eng = _day + " " + _month_eng + " " + _year_eng;
                            _expire_th = _day + " " + _month_th + " " + _year_th;
                            int _in = Integer.parseInt(_xx.substring(16, 18));
                            _religion = religions.get(_in);
                            if (_in == 99) {
                                _religion = religions.get(10);
                            }
                            _xx = _issue_eng + "\r\n" + _issue_th + "\r\n" + _expire_eng + "\r\n" + _expire_th + "\r\n" + _religion;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    engissue.setText(_issue_eng);
                                    thissue.setText(_issue_th);
                                    engexpire.setText(_expire_eng);
                                    thexpire.setText(_expire_th);
                                    religion.setText(_religion);
                                    /**
                                     * Set DataCard Id
                                     */
                                    cardId.setEngIssue(_issue_eng);
                                    cardId.setThIssue(_issue_th);
                                    cardId.setEngExpire(_expire_eng);
                                    cardId.setThExpire(_expire_th);
                                    cardId.setReligion(_religion);
                                }
                            });
                        }
                        break;
                    default:
                }
                return _xx;
            }
        };

        scheduledExecutor.scheduleAtFixedRate(job, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    private static String e(int value) {
        String hex = Integer.toHexString(value);
        hex = hex.length() % 2 == 1 ? "0" + hex : hex;
        return hex.toUpperCase();
    }

}
