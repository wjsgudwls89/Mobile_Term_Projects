package com.quirodev.sac;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * Created by hj on 2018. 6. 6..
 */

public class HowToUseActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.howtouse);
        textView = (TextView)findViewById(R.id.howtouse);
        textView.setMovementMethod(new ScrollingMovementMethod());

        textView.setText("HOW TO USE \n" +
                "\n"+
                "1.APP USAGE \n"+
                "\n"+
                "APP USAGE 는 당신의 앱 사용량을 보여줍니다.\n" +
                "당신은 기간에 따라서 어떠한 앱을 몇시간 사용하였는지 볼수 있습니다.\n" +
                "\n"+
                "2.SCREEN LOCK \n" +
                "\n"+
                "SCREEN LOCK 은 화면 잠금기능을 제공합니다.\n" +
                "당신이 설정한 시간만큼 화면은 잠기게 되고 강제로 종료할시에는\n" +
                "2시간동안 자동으로 잠기게 되니 설정시 주의 하시기 바랍니다\n" +
                "\n"+
                "3.RANKING\n" +
                "\n"+
                "RANKING 은 당신의 오늘 하루 까지 사용한 사용시간을 등록합니다\n" +
                "각 사용자들은 얼마나 핸드폰을 사용했고, 당신이 몇위인지 확인할수 있습니다\n" +
                "\n"+
                "4.SET ALARM \n" +
                "\n"+
                "SET ALARM 기능은 당신이 오늘 핸드폰을 사용할 시간을 정해놓으면\n" +
                "그 시간이 지난뒤에 알림을 해주는 서비스 입니다\n" +
                "연동한 사용자가 있을시 연동한 사용자에게도 같은 알림이 보내집니다"+
                "핸드폰을 정해진 시간만큼 사용하고 싶을때 유용합니다\n" +
                "\n"+
                "5.LINK\n" +
                "\n"+
                "LINK 기능은 당신이 연동하고 싶은 상대의 이름을 등록하면\n" +
                "연동된 사용자가 얼마나 핸드폰을 사용 했는지 시간을 실시간으로 알수있습니다\n" +
                "그리고 원하는 메세지를 보낼 수 있는 푸시알림 기능을 제공합니다\n");
    }
}
