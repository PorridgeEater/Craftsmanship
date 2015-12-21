package leadroyal.porridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestEmailVerifyCallback;
import com.avos.avoscloud.SignUpCallback;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private Button register;
    private TextView user, psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        AVAnalytics.trackAppOpened(getIntent());
        initView();
        initEvent();
    }

    private void initView() {
        register = (Button) findViewById(R.id.register_button);
        user = (TextView) findViewById(R.id.user);
        psw = (TextView) findViewById(R.id.password);
    }

    private void initEvent() {
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        register_it();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void register_it() {
        String s;
        AVUser new_user = new AVUser();
        s = user.getText().toString();
        if (s.contains("@"))
            new_user.setEmail(s);
        new_user.setUsername(s);
        s = psw.getText().toString();
        new_user.setPassword(s);
        new_user.signUpInBackground(new SignUpCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // successfully
                    Toast.makeText(getApplicationContext(), "注册成功!", Toast.LENGTH_SHORT).show();
                    AVUser.requestEmailVerfiyInBackground(AVUser.getCurrentUser().getEmail(), new RequestEmailVerifyCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null)
                                Toast.makeText(getApplicationContext(), "邮件已经发送", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    // failed
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent();
                intent.setClass(Register.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
