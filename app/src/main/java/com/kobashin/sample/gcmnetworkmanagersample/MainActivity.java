package com.kobashin.sample.gcmnetworkmanagersample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.kobashin.sample.gcmnetworkmanagersample.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding mainBinding;
    private GcmNetworkManager gcmNetworkManager;

    public static final String TAG_ONE_OFF = "ONE_OFF";
    public static final String TAG_PERIODIC = "PERIODIC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainBinding.setActivity(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        gcmNetworkManager = GcmNetworkManager.getInstance(this);
    }

    public void onClickButton(View v) {
        // 一回限りのJobの実行
        OneoffTask oneoff = new OneoffTask.Builder()
                // どのタスクを実行するかを宣言(必須)
                // タスクはGcmTaskServiceを継承してつくる
                .setService(MyGcmTaskService.class)
                // taskを識別する為のタグを付与(必須)
                .setTag(TAG_ONE_OFF)
                // 実行開始されるWindow幅を指定する[秒](必須)
                // 条件達成後、ここで指定した範囲でタスクが実行される
                .setExecutionWindow(10L, 15L)
                // タスクにパラメータを与える
                //.setExtras(new Bundle())

                // 再起動後にもJobを継続するかどうかを指定する
                // JobSchedulerと同じく、trueを宣言するのであれば BOOT_COMPLETEDが必要
                //.setPersisted(true)

                // Jobの実行に必要なネットワーク形態を設定する
                // Task.NETWORK_STATE_CONNECTED: 従量制でも定額制でも、ネットワークに繋がっていれば
                // Task.NETWORK_STATE_ANY: ネットワークの状態に依存しない
                // NETWORK_TYPE_UNMETERD: 従量制でないネットワーク
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)

                // Deviceが給電状態かどうかを設定する
                //.setRequiresCharging(true)

                // 既存の同タグを持つタスクを上書きするかどうかを指定する
                // 同タグを持つタスクがCompleteしていない場合、新たに発行されたタスクは前のたすくがCompleteされるまで
                // 実行されない。
                // defaultはfalse.
                //.setUpdateCurrent(true)
                .build();

        gcmNetworkManager.schedule(oneoff);
    }

    public void onClickButton2(View v) {
        // 定期実行タスクを作成する
        // Oneoffの方と同じメソッドは書いていない
        PeriodicTask priodic = new PeriodicTask.Builder()
                .setService(MyGcmTaskService.class)
                .setTag(TAG_PERIODIC)
                // インターバルを指定する
                .setPeriod(30L)
                // 前にずれても良い時間を指定する
                // Period = 30, Flex = 10の場合 20 - 30で発火する
                .setFlex(10L)
                .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                .build();

        gcmNetworkManager.schedule(priodic);
    }
}
