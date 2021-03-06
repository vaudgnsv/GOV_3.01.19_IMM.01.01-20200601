package org.centerm.land.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.centerm.land.BuildConfig;


public class Preference {

    public static final String KEY_PRIMARY_IP = Preference.class.getName() + "key_primary_ip";
    public static final String KEY_PRIMARY_PORT = Preference.class.getName() + "key_primary_port";

    public static final String KEY_SECONDARY_IP = Preference.class.getName() + "key_secondary_ip";
    public static final String KEY_SECONDARY_PORT = Preference.class.getName() + "key_secondary_port";

    public static final String KEY_TRACE_NO_TMS = Preference.class.getName() +"key_trace_no_tms";
    public static final String KEY_TRACE_NO_EPS = Preference.class.getName() +"key_trace_no_eps";
    public static final String KEY_TRACE_NO_POS = Preference.class.getName() +"key_trace_no_pos";

    public static final String KEY_BATCH_NUMBER_POS = Preference.class.getName() +"key_batch_number_pos"; //mBlockData 60
    public static final String KEY_BATCH_NUMBER_EPS = Preference.class.getName() +"key_batch_number_ems"; //mBlockData 60
    public static final String KEY_BATCH_NUMBER_TMS = Preference.class.getName() +"key_batch_number_tms"; //mBlockData 60

    public static final String KEY_INVOICE_NUMBER_POS = Preference.class.getName() +"key_invoice_number_pos"; //mBlockData 62
    public static final String KEY_INVOICE_NUMBER_EPS = Preference.class.getName() +"key_invoice_number_ems"; //mBlockData 62
    public static final String KEY_INVOICE_NUMBER_TMS = Preference.class.getName() +"key_invoice_number_tms"; //mBlockData 62

    public static final String KEY_TERMINAL_ID_POS = Preference.class.getName() + "key_terminal_id_pos";
    public static final String KEY_TERMINAL_ID_TMS = Preference.class.getName() + "key_terminal_id_tms";
    public static final String KEY_TERMINAL_ID_EPS = Preference.class.getName() + "key_terminal_id_eps";

    public static final String KEY_MERCHANT_ID_POS = Preference.class.getName() + "key_merchant_id_pos";
    public static final String KEY_MERCHANT_ID_TMS = Preference.class.getName() + "key_merchant_id_tms";
    public static final String KEY_MERCHANT_ID_EPS = Preference.class.getName() + "key_merchant_id_eps";

    public static final String KEY_TERMINAL_VERSION = Preference.class.getName() + "key_terminal_version";

    public static final String KEY_MESSAGE_VERSION = Preference.class.getName() + "key_message_version";

    public static final String KEY_TRANSACTION_CODE = Preference.class.getName() + "key_transaction_code";

    public static final String KEY_PARAMETER_VERSION = Preference.class.getName() +"key_parameter_version";

    public static final String KEY_NII_POS = Preference.class.getName() + "key_nii_pos";
    public static final String KEY_NII_TMS = Preference.class.getName() + "key_nii_tms";
    public static final String KEY_NII_EPS = Preference.class.getName() + "key_nii_eps";

    public static final String KEY_TPDU_POS = Preference.class.getName() + "key_tpdu_pos";
    public static final String KEY_TPDU_TMS = Preference.class.getName() + "key_tpdu_tms";
    public static final String KEY_TPDU_EPS = Preference.class.getName() + "key_tpdu_eps";

    public static final String KEY_PIN = Preference.class.getName() + "key_pin";

    public static final String KEY_TAG_1000 = Preference.class.getName() + "key_tag_1000";
    public static final String KEY_TAG_1001 = Preference.class.getName() + "key_tag_1001";
    public static final String KEY_TAG_1002 = Preference.class.getName() + "key_tag_1002";
    public static final String KEY_TAG_1003 = Preference.class.getName() + "key_tag_1003";
    public static final String KEY_TAG_1004 = Preference.class.getName() + "key_tag_1004";
    public static final String KEY_TAG_1005 = Preference.class.getName() + "key_tag_1005";
    public static final String KEY_TAG_1006 = Preference.class.getName() + "key_tag_1006";
    public static final String KEY_TAG_1007 = Preference.class.getName() + "key_tag_1007";

    public static final String KEY_FEE = Preference.class.getName() + "key_fee_eps";

    public static final String KEY_QR_AID = Preference.class.getName() + "key_qr_aid";
    public static final String KEY_QR_BILLER_ID = Preference.class.getName() + "key_qr_biller_id";
    public static final String KEY_QR_MERCHANT_NAME = Preference.class.getName() + "key_qr_merchant_name";
    public static final String KEY_QR_TERMINAL_ID = Preference.class.getName() + "key_qr_terminal_id";
    public static final String KEY_QR_TRACE_NO = Preference.class.getName() + "key_qr_trace_no";
    public static final String KEY_QR_BATCH_NUMBER = Preference.class.getName() +"key_qr_batch_number";
    public static final String KEY_QR_MERCHANT_NAME_THAI = Preference.class.getName() + "key_qr_merchant_name_THAI";

    public static final String KEY_REF_2 = Preference.class.getName() + "key_ref_2";

    public static final String KEY_BILLER_KEY = Preference.class.getName() + "key_biller_key";
    public static final String KEY_QR_PORT = Preference.class.getName() + "key_qr_port";

    public static final String KEY_MERCHANT_1 = Preference.class.getName() + "key_merchant_1";
    public static final String KEY_MERCHANT_2 = Preference.class.getName() + "key_merchant_2";
    public static final String KEY_MERCHANT_3 = Preference.class.getName() + "key_merchant_3";

    public static final String KEY_ADMIN_PASS_WORD = Preference.class.getName() + "key_admin_pass_word";
    public static final String KEY_NORMAL_PASS_WORD = Preference.class.getName() + "key_normal_pass_word";

    public static final String KEY_SALE_VOID_PRINT_ID_POS = Preference.class.getName() + "key_sale_void_print_id_pos";
    public static final String KEY_SALE_VOID_PRINT_ID_EPS = Preference.class.getName() + "key_sale_void_print_id_eps";
    public static final String KEY_SALE_VOID_PRINT_ID_TMS = Preference.class.getName() + "key_sale_void_print_id_tms";

    public static final String KEY_SETTLE_TYPE_POS = Preference.class.getName() + "key_settle_type_pos";
    public static final String KEY_SETTLE_DATE_POS = Preference.class.getName() + "key_settle_date_pos";
    public static final String KEY_SETTLE_TIME_POS = Preference.class.getName() + "key_settle_time_pos";
    public static final String KEY_SETTLE_SALE_COUNT_POS = Preference.class.getName() + "key_settle_sale_count_pos";
    public static final String KEY_SETTLE_SALE_TOTAL_POS = Preference.class.getName() + "key_settle_sale_total_pos";
    public static final String KEY_SETTLE_VOID_COUNT_POS = Preference.class.getName() + "key_settle_void_count_pos";
    public static final String KEY_SETTLE_VOID_TOTAL_POS = Preference.class.getName() + "key_settle_void_total_pos";
    public static final String KEY_SETTLE_BATCH_POS = Preference.class.getName() + "key_settle_batch_pos";
    public static final String KEY_SETTLE_TAX_FEE_SALE_POS = Preference.class.getName() + "key_settle_tax_fee_sale_pos";
    public static final String KEY_SETTLE_TAX_FEE_VOID_POS = Preference.class.getName() + "key_settle_tax_fee_void_pos";

    public static final String KEY_SETTLE_TYPE_EPS = Preference.class.getName() + "key_settle_type_eps";
    public static final String KEY_SETTLE_DATE_EPS = Preference.class.getName() + "key_settle_date_eps";
    public static final String KEY_SETTLE_TIME_EPS = Preference.class.getName() + "key_settle_time_eps";
    public static final String KEY_SETTLE_SALE_COUNT_EPS = Preference.class.getName() + "key_settle_sale_count_eps";
    public static final String KEY_SETTLE_SALE_TOTAL_EPS = Preference.class.getName() + "key_settle_sale_total_eps";
    public static final String KEY_SETTLE_VOID_COUNT_EPS = Preference.class.getName() + "key_settle_void_count_eps";
    public static final String KEY_SETTLE_VOID_TOTAL_EPS = Preference.class.getName() + "key_settle_void_total_eps";
    public static final String KEY_SETTLE_BATCH_EPS = Preference.class.getName() + "key_settle_batch_eps";
    public static final String KEY_SETTLE_TAX_FEE_SALE_EPS = Preference.class.getName() + "key_settle_tax_fee_sale_eps";
    public static final String KEY_SETTLE_TAX_FEE_VOID_EPS = Preference.class.getName() + "key_settle_tax_fee_void_eps";

    public static final String KEY_SETTLE_TYPE_TMS = Preference.class.getName() + "key_settle_type_tms";
    public static final String KEY_SETTLE_DATE_TMS = Preference.class.getName() + "key_settle_date_tms";
    public static final String KEY_SETTLE_TIME_TMS = Preference.class.getName() + "key_settle_time_tms";
    public static final String KEY_SETTLE_SALE_COUNT_TMS = Preference.class.getName() + "key_settle_sale_count_tms";
    public static final String KEY_SETTLE_SALE_TOTAL_TMS = Preference.class.getName() + "key_settle_sale_total_tms";
    public static final String KEY_SETTLE_VOID_COUNT_TMS = Preference.class.getName() + "key_settle_void_count_tms";
    public static final String KEY_SETTLE_VOID_TOTAL_TMS = Preference.class.getName() + "key_settle_void_total_tms";
    public static final String KEY_SETTLE_BATCH_TMS = Preference.class.getName() + "key_settle_batch_tms";

    public static final String KEY_SETTLE_DATE_QR = Preference.class.getName() + "key_settle_date_qr";
    public static final String KEY_SETTLE_TIME_QR = Preference.class.getName() + "key_settle_time_qr";
    public static final String KEY_SETTLE_SALE_COUNT_QR = Preference.class.getName() + "key_settle_sale_count_qr";
    public static final String KEY_SETTLE_SALE_TOTAL_QR = Preference.class.getName() + "key_settle_sale_total_qr";
    public static final String KEY_SETTLE_VOID_COUNT_QR = Preference.class.getName() + "key_settle_void_count_qr";
    public static final String KEY_SETTLE_VOID_TOTAL_QR = Preference.class.getName() + "key_settle_void_total_qr";
    public static final String KEY_SETTLE_BATCH_QR = Preference.class.getName() + "key_settle_batch_qr";

    public static final String KEY_TAX_INVOICE_NO_POS = Preference.class.getName() + "key_tax_invoice_no_pos";
    public static final String KEY_TAX_INVOICE_NO_EPS = Preference.class.getName() + "key_tax_invoice_no_eps";
    public static final String KEY_TAX_ID = Preference.class.getName() + "key_tax_id";

    public static final String KEY_POS_ID = Preference.class.getName() + "key_pos_id";


    public static final String KEY_TRACE_NO_GHC = Preference.class.getName() +"key_trace_no_ghc"; // Paul_20180620

    public static final String KEY_BATCH_NUMBER_GHC = Preference.class.getName() +"key_batch_number_ghc"; // Paul_20180620 //mBlockData 60
    public static final String KEY_INVOICE_NUMBER_GHC = Preference.class.getName() +"key_invoice_number_ghc"; // Paul_20180620 //mBlockData 62
    public static final String KEY_TERMINAL_ID_GHC = Preference.class.getName() + "key_terminal_id_ghc"; // Paul_20180620
    public static final String KEY_MERCHANT_ID_GHC = Preference.class.getName() + "key_merchant_id_ghc";    // Paul_20180620
    public static final String KEY_MESSAGE_GHC_VERSION = Preference.class.getName() + "key_message_ghc_version";
    public static final String KEY_NII_GHC = Preference.class.getName() + "key_nii_ghc";    // Paul_20180620
    public static final String KEY_TPDU_GHC = Preference.class.getName() + "key_tpdu_ghc";  // Paul_20180620

    public static final String KEY_TAG_1000_HC = Preference.class.getName() + "key_tag_1000_hc";
    public static final String KEY_TAG_1001_HC = Preference.class.getName() + "key_tag_1001_hc";
    public static final String KEY_TAG_1002_HC = Preference.class.getName() + "key_tag_1002_hc";
    public static final String KEY_TAG_1003_HC = Preference.class.getName() + "key_tag_1003_hc";
    public static final String KEY_TAG_1004_HC = Preference.class.getName() + "key_tag_1004_hc";
    public static final String KEY_TAG_1005_HC = Preference.class.getName() + "key_tag_1005_hc";
    public static final String KEY_TAG_1006_HC = Preference.class.getName() + "key_tag_1006_hc";
    public static final String KEY_TAG_1007_HC = Preference.class.getName() + "key_tag_1007_hc";

    private static Preference settingPreference = null;
    private final String preferenceName = BuildConfig.APPLICATION_ID + "Setting";
    private SharedPreferences preference = null;
    private Editor editor = null;
    private Context context = null;

    /**
     * Constructor method
     *
     * @param context
     */
    public Preference(Context context) {
        this.context = context;
        int mode = Context.MODE_PRIVATE;
        this.preference = this.context.getSharedPreferences(this.preferenceName, mode);
        this.editor = this.preference.edit();
    }

    /**
     * Factory method
     *
     * @param context
     * @return
     */
    public static Preference getInstance(Context context) {
        if (settingPreference == null) {
            settingPreference = new Preference(context);
        }
        return settingPreference;
    }

    public void setValueString(String key, String value) {
        this.editor.putString(key, value);
        this.editor.commit();
    }

    public String getValueString(String key) {
        return this.preference.getString(key, "");
    }

    public String getValueString(String key, String defaultValue) {
        return this.preference.getString(key, defaultValue);
    }

    public void setValueInt(String key, int value) {
        this.editor.putInt(key, value);
        this.editor.commit();
    }

    public int getValueInt(String key, int defaultValue) {
        return this.preference.getInt(key, defaultValue);
    }


    public int getValueInt(String key) {
        return this.preference.getInt(key, 0);
    }

    public void setValueLong(String key, long value) {
        this.editor.putLong(key, value);
        this.editor.commit();
    }

    public long getValueLong(String key, long defaultValue) {
        return this.preference.getLong(key, defaultValue);
    }

    public long getValueLong(String key) {
        return this.preference.getLong(key, 0);
    }

    public void setValueDouble(String key, double value) {
        this.editor.putFloat(key, (float) value);
        this.editor.commit();
    }

    public double getValueDouble(String key, float defaultValue) {
        float value = this.preference.getFloat(key, defaultValue);
        return value;
    }

    public double getValueDouble(String key) {
        float value = this.preference.getFloat(key, 0.0f);
        return value;
    }

    public void setValueFloat(String key, float value) {
        this.editor.putFloat(key, value);
        this.editor.commit();
    }

    public float getValueFloat(String key, float defaultValue) {
        float value = this.preference.getFloat(key, defaultValue);
        return value;
    }

    public float getValueFloat(String key) {
        float value = this.preference.getFloat(key, 0.0f);
        return value;
    }

    public void setValueBoolean(String key, boolean value) {
        this.editor.putBoolean(key, value);
        this.editor.commit();
    }

    public boolean getValueBoolean(String key) {
        return this.preference.getBoolean(key, false);
    }

    public void clear() {
        this.editor.clear();
        this.editor.commit();
    }

    public void deleteKey(String key) {
        this.editor.remove(key);
        this.editor.commit();
    }

}
