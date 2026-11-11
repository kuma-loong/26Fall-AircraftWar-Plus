package edu.hitsz.aircraftwarplus;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

/** Android 应用入口；初始化阶段仅提供启动验证页面。 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView welcome = new TextView(this);
        welcome.setGravity(Gravity.CENTER);
        welcome.setTextSize(24);
        welcome.setText("AircraftWar Plus\n游戏迁移开发中");
        setContentView(welcome);
    }
}
