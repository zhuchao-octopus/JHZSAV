package com.zhuchao.android.tianpu.data;

/**
 * author: ztz
 * date: 2019/9/18 18:19
 * description:
 */
public class Constants {
   //public static byte[] BluetoothOpen = {0x02, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0E, 0x7E};//蓝牙  K70 //  serialPortUtils.sendBuffer(BluetoothOpen,SizeOf(BluetoothOpen));
    //                                     公司   产品  id    应答  数据长度                       校验和
    //public static byte[] BluetoothClose = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0D, 0x7E};//蓝牙  K50 byte[] BluetoothClose = {0x01, 0x01, 0x05, 0x00, 0x00, 0x02, 0x00, 0x04, 0x00, 0x0D, 0x7E};//蓝牙  K50

    //private byte[] MICEffetive = {0x02, 0x01, 0x04, 0x00, 0x00, 0x02, 0x00, 0x02, 0x00, 0x09, 0x7E};// 效果 1  K70

    //public static byte[] BatteryCharge = {0x01, 0x01, 0x22, 0x00, 0x03, 0x7E};//获取充电状态
    //private byte[] BatteryChargeResult = {0x02, 0x01, 0x22, 0x00, 0x01, 0x01, 0x06, 0x7E};// 1充电 0不冲

    //public static byte[] BatteryInfo = {0x01, 0x01, 0x23, 0x00, 0x03, 0x7E};//获取电量信息 0/100
    //public static byte[] BatteryInfoResult = {0x02, 0x01, 0x23, 0x00,0x01,0x09, 0x06, 0x7E};

}
