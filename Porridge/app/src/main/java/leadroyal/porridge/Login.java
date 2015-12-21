package leadroyal.porridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button login;
    private TextView user, psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        AVAnalytics.trackAppOpened(getIntent());
        initView();
        initEvent();
    }

    private void initEvent() {
        login.setOnClickListener(this);
    }

    private void initView() {
        login = (Button) findViewById(R.id.sign_in_button);
        user = (TextView) findViewById(R.id.user);
        psw = (TextView) findViewById(R.id.password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Toast.makeText(getApplicationContext(), "登陆中...", Toast.LENGTH_SHORT).show();
                if (!user.getText().toString().isEmpty() && !user.getText().toString().trim().isEmpty())
                    AVUser.logInInBackground(user.getText().toString(), psw.getText().toString(), new LogInCallback() {
                        public void done(AVUser user, AVException e) {
                            if (e==null && user!=null) {
                                //登录成功
                                Toast.makeText(getApplicationContext(), "登陆成功!", Toast.LENGTH_SHORT).show();
                                show_main_screen();
                            } else {
                                // 登录失败
                                Toast.makeText(getApplicationContext(), "登陆失败,请检查账户或网络!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                break;
        }
    }

    private void show_main_screen() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }
}
