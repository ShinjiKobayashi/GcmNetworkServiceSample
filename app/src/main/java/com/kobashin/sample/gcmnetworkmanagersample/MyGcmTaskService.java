package com.kobashin.sample.gcmnetworkmanagersample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;

public class MyGcmTaskService extends GcmTaskService {
    public MyGcmTaskService() {
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        // called on main thread
        // アプリケーションが削除されたり、アップデートされた場合は全てのタスクは削除される
        // それらの再登録の為に利用するメソッド
        // 初回起動時には呼ばれない。
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        // jobの実体を書くところ
        // WakeLockを握って実行されるが、3分でタイムアウトするので注意

        String tag = taskParams.getTag();
        if (tag != null) {
            Log.i("koba", "tag : " + tag);
            switch (tag){
                case MainActivity.TAG_ONE_OFF:
                    Log.i("koba", "one off job");
                    break;
                case MainActivity.TAG_PERIODIC:
                    Log.i("koba", "periodic job");
                    break;
                default:
                    break;
            }

        }
        // returnは以下のいずれかを返す
        // GcmNetworkManager.RESULT_SUCCESS 正常終了
        // RESULT_RESCHEDULE Error終了。リスケジュールする
        // RESULT_FAILURE Error終了。
        return GcmNetworkManager.RESULT_SUCCESS;
    }

}
