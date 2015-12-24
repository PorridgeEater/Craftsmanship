package leadroyal.porridge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.avos.avoscloud.AVOSCloud;

import java.util.ArrayList;
import java.util.List;

public class Guide extends Activity implements ViewPager.OnPageChangeListener{
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids = {R.id.iv1, R.id.iv2, R.id.iv3, R.id.iv4, R.id.iv5};
    private Button register_btn;
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide);
        AVOSCloud.initialize(this, "NJckDG2TbCs7RpTBhEe9UOFx", "R0jCmrsjRQllegzWzaRt2noo");
        initViews();
        initDots();
    }

    private void initViews() {
        // 加载额外切换的5个view
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.guide01, null));
        views.add(inflater.inflate(R.layout.guide02, null));
        views.add(inflater.inflate(R.layout.guide03, null));
        views.add(inflater.inflate(R.layout.guide04, null));
        views.add(inflater.inflate(R.layout.guide05, null));

        vpAdapter = new ViewPagerAdapter(views, this);
        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);

        register_btn = (Button) views.get(4).findViewById(R.id.register_btn);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Guide.this, Register.class);
                startActivity(i);
//                finish();
            }
        });

        login_btn = (Button) views.get(4).findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent j = new Intent(Guide.this, MainActivity.class);
                startActivity(j);
//                finish();
            }
        });

        vp.setOnPageChangeListener(this);
    }

    private void initDots() {
        dots = new ImageView[views.size()];
        for ( int i=0; i<views.size(); i++ ) {
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }

    @Override
    public void onPageScrollStateChanged(int args0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        for ( int i=0; i<ids.length; i++ ) {
            if (arg0 == i) {
                dots[i].setImageResource(R.drawable.login_point_selected);
            } else {
                dots[i].setImageResource(R.drawable.login_point);
            }
        }
    }
}
