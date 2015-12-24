package leadroyal.porridge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

public class GeneralMessageList extends AppCompatActivity implements View.OnClickListener {
    private NoticeAdapter mAdapter;
    private ArrayList<NoticeEntity> mDataArray = new ArrayList<>();
    private Button refressh_bt;
    private GridView mGridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.general_message_list_layout, null);


        setContentView(view);

        initView(view);
        initAdapter();

        refressh_bt.performClick();


    }


    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),mDataArray.toString(),Toast.LENGTH_LONG);
        Log.v("zyt", mDataArray.toString());
        refresh();
    }

    private String getCurrentUser(){
        //TODO to fix later
        return "fhr";
    }
    private void refresh() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("Comment");
        query.orderByDescending("createdAt");
        query.whereEqualTo("user",getCurrentUser());
        query.findInBackground(
                new FindCallback<AVObject>() {
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            mDataArray.clear();
                            for (int i = 0; i < list.size(); i++) {
                                NoticeEntity entity = new NoticeEntity();
                                entity.setAVO(list.get(i));
                                entity.setTxt(list.get(i).getString("content"));
                                mDataArray.add(entity);
                            }
                            mAdapter.notifyDataSetChanged();
                        } else {
                            LogUtil.log.d("失败", "查询错误: " + e.getMessage());
                        }
                    }
                }
        );
    }



    private void initView(View v){
        mGridView = (GridView) v.findViewById(R.id.notice_grid);
        refressh_bt = (Button) v.findViewById(R.id.refresh_bt);

        refressh_bt.setOnClickListener(this);
    }

    private void initAdapter() {
        mAdapter = new NoticeAdapter(this.getApplicationContext(), mDataArray);
        mGridView.setAdapter(mAdapter);

    }

    private class NoticeAdapter extends BaseAdapter {

        private List<NoticeEntity> data;
        private LayoutInflater inflater;


        public NoticeAdapter(Context context, List<NoticeEntity> data) {
            inflater = LayoutInflater.from(context);
            this.data = data;

        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return data.get(position);
        }


        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.general_message_cell, parent, false);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.txttxt);
            viewHolder.txt.setTextColor(Color.DKGRAY);


            viewHolder.txt.setText("收到评论: "+data.get(position).getAVO().getString("content"));
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView txt;
    }

}
