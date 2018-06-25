package org.centerm.land.activity.posinterface;

import android.os.RemoteException;
import android.util.Log;

import com.centerm.smartpos.aidl.serialport.AidlSerialPort;
import com.centerm.smartpos.util.HexUtil;


import org.centerm.land.CardManager;
import org.centerm.land.MainApplication;

import java.nio.charset.StandardCharsets;

import static org.centerm.land.utility.Utility.BCDtoInt;
import static org.centerm.land.utility.Utility.IntToBCD;
import static org.centerm.land.utility.Utility.JavaHexDump;


// Make class Paul Start 2018-06-23
public class PosInterfaceActivity {

    String TAG = "utility:: PosInterfaceActivity ";

    AidlSerialPort serialPort1 = null;
    AidlSerialPort serialPort2 = null;

    public static int PosInterfaceExistFlg=0;
    public static String PosInterfaceReceiveData = null;
    public static String PosInterfaceReserve = null;
    public static String PosInterfaceFormatVersion = null;
    public static String PosInterfaceReqResIndicator = null;
    public static String PosInterfaceTransactionCode = null;
    public static String PosInterfaceResponseCode = null;
    public static String PosInterfaceMoreDataIndicator = null;
    public static int PosInterfaceTotalFieldCnt=0;
    public static String[] PosInterfaceFieldType = new String[64];
    public static int[] PosInterfaceFieldLength = new int[64];
    public static String[] PosInterfaceFieldData = new String[64];

    public static int PosInterfaceSnedTotalFieldCnt=0;
    public static String[] PosInterfaceSnedFieldType = new String[64];
    public static int[] PosInterfaceSnedFieldLength = new int[64];
    public static String[] PosInterfaceSnedFieldData = new String[64];

    private CardManager cardManager = null;

    public void PosInterfaceOpen() throws RemoteException {
        cardManager = MainApplication.getCardManager();
        serialPort1 = cardManager.getInstancesSerial1();
        serialPort1.open( 9600 );
        serialPort2 = cardManager.getInstancesSerial2();
        serialPort2.open( 9600 );
    }

    public void POSInterfaceInit() {
        int i;

        PosInterfaceExistFlg = 0;
        PosInterfaceReceiveData = null;
        PosInterfaceReserve = null;
        PosInterfaceFormatVersion = null;
        PosInterfaceReqResIndicator = null;
        PosInterfaceTransactionCode = null;
        PosInterfaceResponseCode = null;
        PosInterfaceMoreDataIndicator = null;
        PosInterfaceTotalFieldCnt = 0;
        for(i=0;i<64;i++)
        {
            PosInterfaceFieldType[i] = null;
            PosInterfaceFieldLength[i] = 0;
            PosInterfaceFieldData[i] = null;
        }
        PosInterfaceSnedTotalFieldCnt = 0;
        for(i=0;i<64;i++)
        {
            PosInterfaceSnedFieldType[i] = null;
            PosInterfaceSnedFieldLength[i] = 0;
            PosInterfaceSnedFieldData[i] = null;
        }
    }

//    @SuppressLint("LongLogTag")
    // return 0 : success
    // return 1 : Format Error
    // return 2 : no matching
    public int CheckPOSInterface(String InputPOSInterfaceData)
    {
        String szRecBuf = null;
        int inSize = 0;
        byte CheckSum = 0;
        byte[] RecBuf = new byte[2048 + 1];
        int i;
        int InLength = 0;
        int PosiCnt = 0;
        byte[] TempBuf = new byte[2048 + 1];
        int rv;

        System.out.printf("utility:: CheckPOSInterface 000 Start \n");

        szRecBuf = InputPOSInterfaceData;
//        szRecBuf = "02003530303030303030303030313031313030301C343000123030303030303031303030301C0311";

        inSize = szRecBuf.length();
        CheckSum = 0;
        RecBuf = HexUtil.hexStringToByte(szRecBuf);
        System.out.printf("utility:: ");
        inSize = inSize / 2;
        for (i = 0; i < inSize; i++) {
            System.out.printf("[%02X]", RecBuf[i]);
        }
        System.out.printf("\n");
//        if (!szRecBuf.substring(0, 2).equalsIgnoreCase("02"))
        if (RecBuf[0] != (byte) 0x02) {
            System.out.printf("utility:: CheckPOSInterface 001 STX Error \n");
            return 1;
        }
        if (inSize < 5) {
            System.out.printf("utility:: CheckPOSInterface 002 total Length Error \n");
            return 1;
        }
        InLength = BCDtoInt(RecBuf[1],RecBuf[2]);
//        System.out.printf("utility:: CheckPOSInterface 003 InLength = %d \n", InLength);
//        InLength = (((RecBuf[1] & (byte) 0xF0) >> 4) * 1000) + ((RecBuf[1] & (byte) 0x0F) * 100) + (((RecBuf[2] & (byte) 0xF0) >> 4) * 10) + ((RecBuf[2] & (byte) 0x0F) * 1);
//        System.out.printf("utility:: inTescoReceiveAnalyse 003 InLength = %d \n", InLength);
        if (InLength > inSize) {
            System.out.printf("utility:: CheckPOSInterface 004 Length Error \n");
            return 1;
        }
        CheckSum = (byte) 0x00;
//        CheckSum = (byte)(CheckSum ^ RecBuf[0]);      // STX
        CheckSum = (byte) (CheckSum ^ RecBuf[1]);
        CheckSum = (byte) (CheckSum ^ RecBuf[2]);
        for (i = 0; i < InLength; i++) {
            CheckSum = (byte) (CheckSum ^ RecBuf[3 + i]);
        }
        if(RecBuf[3 + i] != (byte)0x03)
        {
            System.out.printf("utility:: CheckPOSInterface 005 ETX Error \n");
            return 1;
        }
//        System.out.printf("utility:: ETX [%02X]\n", RecBuf[3 + i]);
        CheckSum = (byte) (CheckSum ^ RecBuf[3 + i]);                  // ETX
//        System.out.printf("utility:: LRC [%02X]\n", RecBuf[3 + i + 1]);
//        System.out.printf("utility:: CheckSum [%02X]\n", CheckSum);
        if(RecBuf[3 + i + 1] != CheckSum)
        {
//            System.out.printf("utility:: CheckPOSInterface 006 LRC Error \n");
//            return 1;
        }
        System.out.printf("utility:: CheckPOSInterface 006 Success \n");

        POSInterfaceInit();


        PosiCnt = 3;
        System.arraycopy(RecBuf, PosiCnt, TempBuf, 0, 10);
        PosInterfaceReserve = new String(TempBuf, 0, 10, StandardCharsets.UTF_8);
        Log.d(TAG,"PosInterfaceReserve = "+PosInterfaceReserve);
        PosiCnt += 10;

        System.out.printf("utility:: PosInterfaceReserve = %s \n", PosInterfaceReserve);
        System.arraycopy(RecBuf, PosiCnt, TempBuf, 0, 1);
        PosInterfaceFormatVersion = new String(TempBuf, 0, 1, StandardCharsets.UTF_8);
        Log.d(TAG,"PosInterfaceFormatVersion = "+PosInterfaceFormatVersion);
        PosiCnt++;

        System.arraycopy(RecBuf, PosiCnt, TempBuf, 0, 1);
        PosInterfaceReqResIndicator = new String(TempBuf, 0, 1, StandardCharsets.UTF_8);
        Log.d(TAG,"PosInterfaceReqResIndicator = "+PosInterfaceReqResIndicator);
        PosiCnt++;

        System.arraycopy(RecBuf, PosiCnt, TempBuf, 0, 2);
        PosInterfaceTransactionCode = new String(TempBuf, 0, 2, StandardCharsets.UTF_8);
        Log.d(TAG,"PosInterfaceTransactionCode = "+PosInterfaceTransactionCode);
        PosiCnt += 2;

        System.arraycopy(RecBuf, PosiCnt, TempBuf, 0, 2);
        PosInterfaceResponseCode = new String(TempBuf, 0, 2, StandardCharsets.UTF_8);
        Log.d(TAG,"PosInterfaceResponseCode = "+PosInterfaceResponseCode);
        PosiCnt += 2;

        System.arraycopy(RecBuf, PosiCnt, TempBuf, 0, 1);
        PosInterfaceMoreDataIndicator = new String(TempBuf, 0, 1, StandardCharsets.UTF_8);
        Log.d(TAG,"PosInterfaceMoreDataIndicator = "+PosInterfaceMoreDataIndicator);
        PosiCnt++;

        if (RecBuf[PosiCnt] != (byte) 0x1C) {
            System.out.printf("utility:: PosInterfaceMoreDataIndicator 007 Field Separator Error \n");
            return 2;
        }
        PosiCnt++;
//        System.out.printf("utility:: FieldSeparator = [%02X] \n", FieldSeparator);
//        System.out.printf("utility:: ");
//        byte[] FieldType = new byte[2];
//        int FieldLength;
//        byte[] FieldData = new byte[1024+1];

//        inSize = PosiCnt-3;
//        System.out.printf("utility:: InLength = %d \n",InLength);
//        System.out.printf("utility:: PosiCnt = %d \n",PosiCnt);

//        byte[] ReceiveBuf=new byte[];
//        System.out.printf("utility:: ");
//        for (i = 0; i < (InLength-PosiCnt); i++) {
//            System.out.printf("[%02X]", RecBuf[i+PosiCnt]);
//        }
//        System.out.printf("\n");

        System.arraycopy(RecBuf, PosiCnt, TempBuf, 0,InLength-PosiCnt+3);
//        PosInterfaceReceiveData = new String(RecBuf,PosiCnt,InLength-PosiCnt-2,StandardCharsets.UTF_8  );

        PosInterfaceReceiveData = HexUtil.bytesToHexString(TempBuf).substring( 0,(InLength-PosiCnt+3)*2 );
        Log.d(TAG,"PosInterfaceReceiveData = "+PosInterfaceReceiveData);
        rv = PosInterfaceFieldPadding();
        if(rv != 0)
        {
            return 2;
        }
        return 0;
    }

    public int PosInterfaceFieldPadding()
    {
        int inSize;
        String szRecBuf;
        byte[] RecBuf = new byte[2048 + 1];
        int i;
        int PosiCnt;
        byte[] TempBuf = new byte[2048 + 1];

        szRecBuf = PosInterfaceReceiveData;
        inSize = szRecBuf.length();
        RecBuf = HexUtil.hexStringToByte(szRecBuf);

        System.out.printf("utility:: ");
        inSize = inSize / 2;
        for (i = 0; i < inSize; i++) {
            System.out.printf("[%02X]", RecBuf[i]);
        }
        System.out.printf("\n");

        PosiCnt = 0;
        PosInterfaceTotalFieldCnt = 0;
        for(i=0;(i<64) && (PosiCnt<inSize);i++)
        {
            System.arraycopy(RecBuf, PosiCnt, TempBuf, 0, 2);
            PosInterfaceFieldType[PosInterfaceTotalFieldCnt] = new String(TempBuf, 0, 2, StandardCharsets.UTF_8);
            Log.d(TAG,"PosInterfaceFieldType = "+PosInterfaceFieldType[PosInterfaceTotalFieldCnt]);
            PosiCnt += 2;
            PosInterfaceFieldLength[PosInterfaceTotalFieldCnt] = BCDtoInt(RecBuf[PosiCnt],RecBuf[PosiCnt+1]);
            PosiCnt += 2;
            System.arraycopy(RecBuf, PosiCnt, TempBuf, 0, PosInterfaceFieldLength[PosInterfaceTotalFieldCnt]);
            PosInterfaceFieldData[PosInterfaceTotalFieldCnt] = new String(TempBuf, 0, PosInterfaceFieldLength[PosInterfaceTotalFieldCnt], StandardCharsets.UTF_8);
            Log.d(TAG,"PosInterfaceFieldData = "+PosInterfaceFieldData[PosInterfaceTotalFieldCnt]);
            PosiCnt += PosInterfaceFieldLength[PosInterfaceTotalFieldCnt];
            if(PosiCnt>inSize)
            {
                System.out.printf("utility:: PosInterfaceFieldPadding 001 Error \n");
                return 1;
            }
            if(RecBuf[PosiCnt] != (byte)0x1C)
            {
                if(PosiCnt == inSize)
                {
                    PosInterfaceTotalFieldCnt++;
                    return 0;
                }
                System.out.printf("utility:: PosInterfaceFieldPadding FieldSeparator 002 Error \n");
                return 1;
            }
            PosiCnt++;
            PosInterfaceTotalFieldCnt++;
        }
        PosInterfaceSendData("06");     // ACK SEND
        return 0;
    }

    public int PosInterfaceSelect()
    {
        try {
            PosInterfaceOpen();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        int rv;
        int i;


//        rv = CheckPOSInterface("02003530303030303030303030313031313030301C343000123030303030303031303030301C0311");
        rv = CheckPOSInterface("02002930303030303030303030313032363030301C363500063030303031301C031A");
        if(rv == 0)
        {
            PosInterfaceExistFlg = 1;
            if(PosInterfaceReqResIndicator.equals("0"))
            {
                for(i=0;i<PosInterfaceTotalFieldCnt;i++)
                {
                    Log.d(TAG,"PosInterfaceFieldType = "+PosInterfaceFieldType[i]);
                    Log.d(TAG,"PosInterfaceFieldData = "+PosInterfaceFieldData[i]);
                }
                switch(PosInterfaceTransactionCode)
                {
                    case "11":      // ผู้ป่วยนอกทั่วไป สิทธิตนเองและครอบครัว
                        break;
                    case "12":      // ผู้ป่วยนอกทั่วไป สิทธิบุตร 0-7 ปี
                        break;
                    case "13":      // ผปู้่ายนอกทวั่ไป สิทธิคู่สมรสต่างชาติ
                        break;
                    case "14":      // ผู้ป่วยนอกทั่วไป ไม่สามารถใช้บัตรได้
                        break;
//                            case "20":      // รายการขาย Card และ QR Cod
//                                break;
                    case "21":      // หน่วยไตเทียม สิทธิตนเองและครอบครัว
                        break;
                    case "22":      // หน่วยไตเทียม สิทธิบุตร 0-7 ปี
                        break;
                    case "23":      // หน่วยไตเทียม สิทธิคู่สมรสต่างชาติ
                        break;
                    case "24":      // หน่วยไตเทียม ไม่สามารถใช้บัตรได้
                        break;
                    case "31":      // หน่วยรังสีผู้เป็นมะเร็ง สิทธิตนเองและครอบครัว
                        break;
                    case "32":      // หน่วยรังสีผู้เป็นมะเร็ง สิทธิบุตร 0-7 ปี
                        break;
                    case "33":      // หน่วยรังสีผู้เป็นมะเร็ง สิทธิคู่สมรสต่างชาติ
                        break;
                    case "34":      // หน่วยรังสีผู้เป็นมะเร็ง ไม่สามารถใช้บัตรได้
                        break;
                    case "26":      // รายการยกเลกิ
                        break;
                    case "50":      // รายการโอนยอด
                        break;
                    case "92":      // รายการพิมพ์สลิปซ ้า
                        break;
//                            case "QR":      // รายการขาย QR Code
//                                break;
//                            case "IQ":      // ตรวจสอบ QR Code
//                                break;
                    default:
                        break;
                }
            }
            else
            {

            }
        }
        PosInterfaceWriteField("01","000      ");   // Approval Code
        String ResponseCode="13";
        ResponseMsgPosInterface(ResponseCode);
        PosInterfaceWriteField("02",ResponseMsgPosInterface(ResponseCode));   // Response Message

        PosInterfaceWriteField("65","      ");   // Invoice Number
        PosInterfaceWriteField("16","40110007");   // Terminal ID
        PosInterfaceWriteField("D1","000000040110000");   // Merchant ID
        PosInterfaceWriteField("03","180622");   // Date YYMMDD
        PosInterfaceWriteField("04","161124");   // Time HHMMSS
        PosInterfaceWriteField("30","000160xxxxxx1678");   // Card No

        PosInterfaceSendMessage(ResponseCode);

        POSInterfaceInit();
        return 0;
    }

    public void PosInterfaceSendMessage(String ResponseCode)
    {
        String SendString=null;
        int TotalLen;
        byte[] SendBuf = new byte[2048+1];
        String TempString=null;
        int i;
        byte CheckSum;

        TotalLen = 0;
        SendBuf[TotalLen++] = 0x02;
        SendBuf[TotalLen++] = 0x00;
        SendBuf[TotalLen++] = 0x00;
        TempString = PosInterfaceReserve;
        System.arraycopy(TempString.getBytes(), 0,SendBuf, TotalLen,  TempString.length());
        TotalLen += TempString.length();

//        JavaHexDump(SendBuf,TotalLen);

        TempString = PosInterfaceFormatVersion;
        System.arraycopy(TempString.getBytes(), 0,SendBuf, TotalLen,  TempString.length());
        TotalLen += TempString.length();
        TempString = "1";   // Response Message
        System.arraycopy(TempString.getBytes(), 0,SendBuf, TotalLen,  TempString.length());
        TotalLen += TempString.length();
        TempString = PosInterfaceTransactionCode;
        System.arraycopy(TempString.getBytes(), 0,SendBuf, TotalLen,  TempString.length());
        TotalLen += TempString.length();

        TempString = ResponseCode;
        System.arraycopy(TempString.getBytes(), 0,SendBuf, TotalLen,  TempString.length());
        TotalLen += TempString.length();

        TempString = "0";   // More Data Indicator
        System.arraycopy(TempString.getBytes(), 0,SendBuf, TotalLen,  TempString.length());
        TotalLen += TempString.length();

        SendBuf[TotalLen++] = (byte)0x1C;   // Field Separator


//        JavaHexDump(SendBuf,TotalLen);

        for(i=0;i<PosInterfaceSnedTotalFieldCnt;i++)
        {
            TempString = PosInterfaceSnedFieldType[i];   // Field Type
            System.arraycopy(TempString.getBytes(), 0,SendBuf, TotalLen,  TempString.length());
            TotalLen += TempString.length();

            System.arraycopy(IntToBCD(PosInterfaceSnedFieldLength[i]), 0,SendBuf, TotalLen,  2);
            TotalLen += 2;

            TempString = PosInterfaceSnedFieldData[i];   // Field Value
            System.arraycopy(TempString.getBytes(), 0,SendBuf, TotalLen,  PosInterfaceSnedFieldLength[i]);
            TotalLen += PosInterfaceSnedFieldLength[i];

            SendBuf[TotalLen++] = (byte)0x1C;   // Field Separator

        }

        System.arraycopy(IntToBCD(TotalLen-3), 0,SendBuf, 1,  2);
        SendBuf[TotalLen++] = (byte)0x03;
//        JavaHexDump(SendBuf,TotalLen);

        CheckSum = (byte) 0x00;
//        CheckSum = (byte)(CheckSum ^ SendBuf[0]);      // STX
        CheckSum = (byte) (CheckSum ^ SendBuf[1]);
        CheckSum = (byte) (CheckSum ^ SendBuf[2]);
        for (i = 3; i < TotalLen; i++) {
            CheckSum = (byte) (CheckSum ^ SendBuf[i]);
        }
//        CheckSum = (byte) (CheckSum ^(byte)0x03);                  // ETX
        SendBuf[TotalLen++] = CheckSum;         // LRC

        JavaHexDump(SendBuf,TotalLen);

        SendString = HexUtil.bytesToHexString(SendBuf).substring(0,TotalLen*2);
        System.out.printf("utility:: SendString = %s \n",SendString);
        /*
        ii = 124;

        TotalLen = IntToBCD(ii);
        SendString = HexUtil.bytesToHexString(TotalLen);
        System.out.printf("utility:: PosInterfaceSendMessage SendString = %s\n",SendString);
//        SendString = "02"+"0000"
*/
        PosInterfaceSendData(SendString);
    }

    public void PosInterfaceWriteField(String FieldTypeString,String FieldValueString)
    {
        PosInterfaceSnedFieldType[PosInterfaceSnedTotalFieldCnt] = FieldTypeString;
        PosInterfaceSnedFieldLength[PosInterfaceSnedTotalFieldCnt] = FieldValueString.length();
        PosInterfaceSnedFieldData[PosInterfaceSnedTotalFieldCnt] = FieldValueString;
        if(PosInterfaceSnedTotalFieldCnt < 64) {
            PosInterfaceSnedTotalFieldCnt++;
        }
    }

    public String PosInterfaceReceiveData()
    {
        byte[] data = new byte[0];
        try {
            data = serialPort1.receiveData(500);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (data == null || data.length < 1) {
            return null;
        } else {
            return new String(data);
        }
    }

    public void PosInterfaceSendData(String SendString)
    {
        int i;

        byte hexData[] = SendString.getBytes();
        hexData = HexUtil.hexStringToByte(SendString);
        if (hexData == null) {
            return;
        }
        try {
//            JavaHexDump( hexData,hexData.length );
//System.out.printf("utility:: PosInterfaceSendData = [%02X]\n",hexData[0]);
            this.serialPort1.sendData(hexData);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public String ResponseMsgPosInterface(String response_code) {
        String szMSG = "Un Define Code";
        switch (response_code) {
            case "00":
                szMSG = "COMPLETE";
                break;
            case "01":
                szMSG = "MSG LEN ERR";
                break;
            case "02":
                szMSG = "FORMAT ERR";
                break;
            case "03":
                szMSG = "TER VER ERR";
                break;
            case "04":
                szMSG = "MSG VER ERR";
                break;
            case "05":
                szMSG = "MAC ERR";
                break;
            case "06":
                szMSG = "TX CODE ERR";
                break;
            case "07":
                szMSG = "TER CER ERR";
                break;
            case "08":
                szMSG = "TID NOT FOUND";
                break;
            case "11":
                szMSG = "CARD NOT FOUND";
                break;
            case "12":
                szMSG = "TX NOT FOUND";
                break;
            case "13":
//                szMSG = "VOID NOT MATCH";
                szMSG = "NOT MATCH";
                break;
            case "14":
                szMSG = "EXCEED AMT";
                break;
            case "15":
                szMSG = "EXCEED USE";
                break;
            case "95":
                szMSG = "TXN CODE ERR";
                break;
            case "96":
                szMSG = "TOP TIMEOUT";
                break;
            case "97":
                szMSG = "TOP FAIL";
                break;
            case "98":
                szMSG = "EXCEED AMT DEPOSIT";
                break;
            case "21":
                szMSG = "SERV TIMEOUT";
                break;
            case "22":
                szMSG = "TOO MANY CONN";
                break;
            case "31":
                szMSG = "DATABASE ERR";
                break;
            case "32":
                szMSG = "EMCI ERR";
                break;
            case "33":
                szMSG = "INVALID BATCH";
                break;
            case "34":
                szMSG = "TID NOT FOUND";
                break;
            case "40":
                szMSG = "EMCI ERR ";
                break;
            case "41":
                szMSG = "EMCI TIMEOUT";
                break;
            case "42":
                szMSG = "EMCI MALFUNC";
                break;
            case "43":
                szMSG = "INCORRECT PIN";
                break;
            case "44":
                szMSG = "INVALID CARD";
                break;
            case "45":
                szMSG = "DO NOT HONOR";
                break;
            case "46":
                szMSG = "PIN EXCEED";
                break;
            case "47":
                szMSG = "TXN NOT PERMIT";
                break;
            case "48":
                szMSG = "CARD EXPIRE";
                break;
            case "49":
                szMSG = "PICKUP CARDr";
                break;
            case "50":
                szMSG = "INVALID TXN";
                break;
            case "51":
                szMSG = "INVALID TIN/PIN";
                break;
            case "52":
                szMSG = "INV CARD CATG";
                break;
            case "69":
                szMSG = "INVALID TID";
                break;
            case "ND":
                szMSG = "TXN CANCEL";
                break;
            case "EN":
                szMSG = "CONNECT FAILED";
                break;
            case "NA":
                szMSG = "NOT AVAILABLE";
                break;
        }
        return szMSG;
    }


}
