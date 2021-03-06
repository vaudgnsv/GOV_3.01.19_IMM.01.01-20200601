package org.centerm.land.utility;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.centerm.land.R;

import java.util.Hashtable;

public class Utility {

    private static Dialog dialogAlert = null;

    public static void customDialogAlert(Context context, String msg, final OnClickCloseImage onClickCloseImage) {
        if (dialogAlert != null) {
            if (dialogAlert.isShowing()) {
                dialogAlert.dismiss();
            }
            dialogAlert = null;
        }
        dialogAlert = new Dialog(context);
        dialogAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAlert.setContentView(R.layout.dialog_custom_alert);
        dialogAlert.setCancelable(false);
        dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAlert.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView msgLabel = dialogAlert.findViewById(R.id.msgLabel);
        ImageView closeImage = dialogAlert.findViewById(R.id.closeImage);
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCloseImage != null) {
                    onClickCloseImage.onClickImage(dialogAlert);
                }
            }
        });
        if (msg != null) {
            msgLabel.setText(msg);
        }
        dialogAlert.show();
    }

    public static void customDialogAlertSuccess(Context context, @Nullable String msg, final OnClickCloseImage onClickCloseImage) {
        final Dialog dialogAlert = new Dialog(context);
        dialogAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAlert.setContentView(R.layout.dialog_custom_success);
        dialogAlert.setCancelable(false);
        dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAlert.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView msgLabel = dialogAlert.findViewById(R.id.msgLabel);
        ImageView closeImage = dialogAlert.findViewById(R.id.closeImage);
        if (msg != null) {
            msgLabel.setText(msg);
        }
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCloseImage != null) {
                    onClickCloseImage.onClickImage(dialogAlert);
                }
            }
        });
        dialogAlert.show();
    }

    public static void customDialogAlertNotConnect(Context context, @Nullable String msg, final OnClickCloseImage onClickCloseImage) {
        final Dialog dialogAlert = new Dialog(context);
        dialogAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAlert.setContentView(R.layout.dialog_custom_alert_not_connect);
        dialogAlert.setCancelable(false);
        dialogAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogAlert.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        TextView msgLabel = dialogAlert.findViewById(R.id.msgLabel);
        Button okBtn = dialogAlert.findViewById(R.id.okBtn);
        ImageView closeImage = dialogAlert.findViewById(R.id.closeImage);
        if (msg != null) {
            msgLabel.setText(msg);
        }
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCloseImage != null) {
                    onClickCloseImage.onClickImage(dialogAlert);
                }
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickCloseImage != null) {
                    onClickCloseImage.onClickImage(dialogAlert);
                }
            }
        });
        dialogAlert.show();
    }

    public static String calNumTraceNo(String trace) {
        String traceNo = "";
        for (int i = trace.length(); i < 6; i++) {
            traceNo += "0";
        }
        return traceNo + trace;
    }

    public static String idValue(String szQr, String szId, String szValue) {
        String szLen = null;
        szLen = String.valueOf(szValue.length());
        if (szLen.length() == 1)
            szLen = "0" + szLen;

        szQr += szId + szLen + szValue;

        return szQr;
    }


    public static Bitmap createQRImage(String url, int QR_WIDTH, int QR_HEIGHT, Context context) {
        try {//??????URL?????????
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //??????????????????????????????????????????
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //????????????????????????????????????????????????????????????????????????
            //??????for????????????????????????????????????
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_HEIGHT + x] = 0xffffffff;
                    }
                }
            }//???????????????????????????????????????ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);

            Bitmap overlay = BitmapFactory.decodeResource(context.getResources(), R.drawable.qr_bot_s);
            //setting bitmap to image view
            bitmap = mergeBitmaps(overlay, bitmap);
            //???????????????ImageView??????
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap mergeBitmaps(Bitmap overlay, Bitmap bitmap) {

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();

        Bitmap combined = Bitmap.createBitmap(width, height, bitmap.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        canvas.drawBitmap(bitmap, new Matrix(), null);

        int centreX = (canvasWidth - overlay.getWidth()) / 2;
        int centreY = (canvasHeight - overlay.getHeight()) / 2;
        canvas.drawBitmap(overlay, centreX, centreY, null);

        return combined;
    }

    public static String getLength62(String slength62) {
        StringBuilder length = new StringBuilder();
        for (int i = slength62.length(); i < 4; i++) {
            length.append("0");
        }
        return length + slength62;
    }

    public static String CheckSumCrcCCITT(String SourceString) {
        String OutString = null;
        int crc = 0xffff;
//        int polynomial = 0xffff;
        int polynomial = 0x1021;

//        byte[] array = HexUtil.hexStringToByte( SourceString );
        byte[] array = SourceString.getBytes();
        for (byte b : array) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }
        crc &= 0xFFFF;
        OutString = Integer.toHexString(crc);
        if (OutString.length() < 4) {
            for (int i = OutString.length(); i < 4; i++) {
                OutString = "0" + OutString;
            }
        }
        System.out.printf("utility:: CheckSumCrcCCITT AAAAAAAAAAAAAAA  %s \n", OutString);
        return OutString;
    }


    // Paul_20180624
    public static int JavaHexDump(byte[] s, int len )
    {
        int i,j, quota=(len/16),remainder=(len%16);
        int ii;
//        byte[] TempBuf = new byte[100+1];
//        byte[] TempBuf1 = new byte[50+1];

        System.out.printf("utility:: Offset  Hex Value                                        Ascii value\n");
        j = 0;
        for(ii=0;ii<quota;ii++)
        {
            System.out.printf("utility:: 0x%04X  ",j);
            for (i=0; i<16; i++){
                System.out.printf("%02X ",s[i+j]);
            }
            System.out.print(" ");
            for (i=0; i<16; i++){
                if((s[i+j] >= 0x20) && (s[i+j] < 0x80)) {
                    System.out.printf("%c",s[i+j]);
                }
                else
                {
                    System.out.printf(".");
                }
            }
            j += 16;
            System.out.printf("\n");
        }
        if (remainder != 0)
        {
            System.out.printf("utility:: 0x%04X  ",j);

            for (i=0; i<remainder; i++){
                System.out.printf("%02X ",s[i+j]);
            }
            for (i=0; i<(16-remainder); i++){
                System.out.printf("   ");
            }
            System.out.printf(" ");
            for (i=0; i<remainder; i++){
                if((s[i+j] >= 0x20) && (s[i+j] < 0x80)) {
                    System.out.printf("%c",s[i+j]);
                }
                else
                {
                    System.out.printf(".");
                }
            }
            for (i=0; i<(16-remainder); i++){
                System.out.printf(" ");
            }
            System.out.printf("\n");
        }
        return 0;
    }

    // Paul_20180624
    public static int BCDtoInt(byte FirstByte,byte SecondByte)
    {
        int Returnint=0;

        Returnint = (((FirstByte & (byte) 0xF0) >> 4) * 1000) + ((FirstByte & (byte) 0x0F) * 100) + (((SecondByte & (byte) 0xF0) >> 4) * 10) + ((SecondByte & (byte) 0x0F));
        return Returnint;
    }

    // Paul_20180624
    public static byte[] IntToBCD(int iLen)
    {
        int i;
        int op;
        int iiLen;
        byte[] dst = new byte[2];

        op = 1000;
        for(i=0;i<2;i++)
        {
            iiLen = iLen/op;
            iLen %= op;
            op /= 10;
            dst[i] = (byte)((iiLen << 4) & (byte)0xF0);

            iiLen = iLen/op;
            iLen %= op;
            op /= 10;
            dst[i] |= (iiLen & 0x0F);
        }
        return dst;
    }

    public interface OnClickCloseImage {
        void onClickImage(Dialog dialog);
    }
}
