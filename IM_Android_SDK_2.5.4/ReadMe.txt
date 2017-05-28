1. ������������֪ͨ�������ض���service�����Լ��ɵ�ʱ�������AndroidManifest.xml��<application> </application>�������������[�Ѹ��¹�1.8�汾���õģ�Ҳ�鷳�ĳ����µ�����]��

        <!--  ��Ϣ�շ�service -->
         <service
            android:name="com.tencent.qalsdk.service.QalService"
            android:exported="false" 
            android:process=":QALSERVICE" >  
        </service> 
		<!--  ��Ϣ�շ�����service -->
        <service  
            android:name="com.tencent.qalsdk.service.QalAssistService"  
            android:exported="false"
            android:process=":QALSERVICE" >
         </service> 
        <!--  ������Ϣ�㲥������ -->
         <receiver
            android:name="com.tencent.qalsdk.QALBroadcastReceiver"
            android:exported="false">
            <intent-filter>
                <action android:name="com.tencent.qalsdk.broadcast.qal" />
            </intent-filter>
        </receiver>
        <!--  ϵͳ��Ϣ�㲥������ -->
        <receiver 
            android:name="com.tencent.qalsdk.core.NetConnInfoCenter"  android:process=":QALSERVICE">  
 		    <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
            </intent-filter>
           <intent-filter>
                <action android:name="android.intent.action.TIME_SET" />
            </intent-filter>
            <intent-filter>
                <action android:name="android.intent.action.TIMEZONE_CHANGED" />
            </intent-filter>
        </receiver>    


2. �����Ҫ��������֪ͨ����Ҫ��application��onCreate�е���TIMManager�е�setOfflinePushListener�ӿ�ע����������֪ͨ�ص�������������Բο���Ӧ��javadoc�������£�
   ����!! �ر�ע�⣺onCreate������߼������жϵ�ǰ�����Ƿ��������̣���ʼ������ع�������ֻ��������ʵ�֡�!!����

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("MyApplication", "app oncreate");
        if(MsfSdkUtils.isMainProcess(this)) {
            Log.d("MyApplication", "main process");
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    Log.e("MyApplication", "recv offline push");
                    notification.doNotify(getApplicationContext(), R.drawable.ic_launcher);
                }
            });
        }
    }
}






�ο��ĵ���

http://www.qcloud.com/doc/product/269/%E6%A6%82%E8%BF%B0%EF%BC%88Android%20SDK%EF%BC%89
