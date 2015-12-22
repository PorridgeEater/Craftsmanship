package leadroyal.porridge;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVOSCloud;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout mLinear1, mLinear2, mLinear3, mLinear4, mLinear5;
    private ImageView imb1, imb2, imb3, imb4, imb5;
    private Fragment mTab1, mTab2, mTab3, mTab4, mTab5;
    private TextView bottom1, bottom2, bottom3, bottom4, bottom5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lead);
        AVOSCloud.initialize(this, "NJckDG2TbCs7RpTBhEe9UOFx", "R0jCmrsjRQllegzWzaRt2noo");

        initView();
        initEvent();

        mLinear1.performClick();
    }

    private void initEvent() {
        mLinear1.setOnClickListener(this);
        mLinear2.setOnClickListener(this);
        mLinear3.setOnClickListener(this);
        mLinear4.setOnClickListener(this);
        mLinear5.setOnClickListener(this);
    }

    private void initView() {
        mLinear1 = (LinearLayout) findViewById(R.id.id_tab01);
        mLinear2 = (LinearLayout) findViewById(R.id.id_tab02);
        mLinear3 = (LinearLayout) findViewById(R.id.id_tab03);
        mLinear4 = (LinearLayout) findViewById(R.id.id_tab04);
        mLinear5 = (LinearLayout) findViewById(R.id.id_tab05);
        imb1 = (ImageView) findViewById(R.id.id_tab01_img);
        imb2 = (ImageView) findViewById(R.id.id_tab02_img);
        imb3 = (ImageView) findViewById(R.id.id_tab03_img);
        imb4 = (ImageView) findViewById(R.id.id_tab04_img);
        imb5 = (ImageView) findViewById(R.id.id_tab05_img);
        bottom1 = (TextView) findViewById(R.id.bottom_tv1);
        bottom2 = (TextView) findViewById(R.id.bottom_tv2);
        bottom3 = (TextView) findViewById(R.id.bottom_tv3);
        bottom4 = (TextView) findViewById(R.id.bottom_tv4);
        bottom5 = (TextView) findViewById(R.id.bottom_tv5);
    }


    @Override
    public void onClick(View v) {

        resetImages();
        resetText();
        switch (v.getId()) {
            case R.id.id_tab01:
                setSelect(1);
                break;
            case R.id.id_tab02:
                setSelect(2);
                break;
            case R.id.id_tab03:
                setSelect(3);
                break;
            case R.id.id_tab04:
                setSelect(4);
                break;
            case R.id.id_tab05:
                setSelect(5);
            default:
                break;
        }

    }

    private void resetText() {
        bottom1.setTextColor(Color.BLACK);
        bottom2.setTextColor(Color.BLACK);
        bottom3.setTextColor(Color.BLACK);
        bottom4.setTextColor(Color.BLACK);
        bottom5.setTextColor(Color.BLACK);
    }

    private void resetImages() {
        imb1.setImageResource(R.drawable.tab01_normal);
        imb2.setImageResource(R.drawable.tab02_normal);
        imb3.setImageResource(R.drawable.tab03_normal);
        imb4.setImageResource(R.drawable.tab04_normal);
        imb5.setImageResource(R.drawable.tab05_normal);
    }

    private void setSelect(int select) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);

        switch (select) {
            case 1:
                bottom1.setTextColor(getResources().getColor(R.color.red));
                if (mTab1 == null) {
                    mTab1 = new Tab01();
                    transaction.add(R.id.id_content, mTab1);
                } else {
                    transaction.show(mTab1);
                }
                imb1.setImageResource(R.drawable.tab01_pressed);
                break;
            case 2:
                bottom2.setTextColor(getResources().getColor(R.color.red));
                if (mTab2 == null) {
                    mTab2 = new Tab02();
                    transaction.add(R.id.id_content, mTab2);
                } else {
                    transaction.show(mTab2);

                }
                imb2.setImageResource(R.drawable.tab02_pressed);
                break;
            case 3:
                bottom3.setTextColor(getResources().getColor(R.color.red));
                if (mTab3 == null) {
                    mTab3 = new Tab03();
                    transaction.add(R.id.id_content, mTab3);
                } else {
                    transaction.show(mTab3);
                }
                imb3.setImageResource(R.drawable.tab03_pressed);
                break;
            case 4:
                bottom4.setTextColor(getResources().getColor(R.color.red));
                if (mTab4 == null) {
                    mTab4 = new Tab04();
                    transaction.add(R.id.id_content, mTab4);
                } else {
                    transaction.show(mTab4);
                }
                imb4.setImageResource(R.drawable.tab04_pressed);
                break;
            case 5:
                bottom5.setTextColor(getResources().getColor(R.color.red));
                if (mTab5 == null) {
                    mTab5 = new Tab05();
                    transaction.add(R.id.id_content, mTab5);
                } else {
                    transaction.show(mTab5);
                }
                imb5.setImageResource(R.drawable.tab05_pressed);
            default:
                break;
        }
        transaction.commit();
    }


    private void hideFragment(FragmentTransaction transaction) {
        if (mTab1 != null) {
            transaction.hide(mTab1);
        }
        if (mTab2 != null) {
            transaction.hide(mTab2);
        }
        if (mTab3 != null) {
            transaction.hide(mTab3);
        }
        if (mTab4 != null) {
            transaction.hide(mTab4);
        }
        if (mTab5 != null) {
            transaction.hide(mTab5);
        }
    }
}
