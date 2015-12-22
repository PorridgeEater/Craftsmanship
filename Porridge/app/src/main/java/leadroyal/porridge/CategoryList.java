package leadroyal.porridge;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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

public class CategoryList extends Activity implements View.OnClickListener {

    private Button refressh_bt;
    private GridView mGridView;
    private NoticeAdapter mAdapter;
    private ArrayList<NoticeEntity> mDataArray = new ArrayList<>();
    private DisplayImageOptions options;

    private int selectedCategory;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        View view = getLayoutInflater().inflate(R.layout.frag01, null);


        setContentView(view);

//        ActionBar actionbar = getActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        selectedCategory = bundle.getInt("selectedCategory");


        initView(view);
        initAdapter();
        //new ImageLoaderConfiguration();

        refressh_bt.performClick();
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case ActionBar.DISPLAY_HOME_AS_UP:
//                this.finish();
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

    private void initAdapter() {
        mAdapter = new NoticeAdapter(this.getApplicationContext(), mDataArray);
        mGridView.setAdapter(mAdapter);

    }

    private void initView(View v) {
        mGridView = (GridView) v.findViewById(R.id.notice_grid);
        refressh_bt = (Button) v.findViewById(R.id.refresh_bt);

        refressh_bt.setOnClickListener(this);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), leadroyal.porridge.NoticeDetail.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("newsAVO", mDataArray.get(position).getAVO());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        refresh();
    }

    private void refresh() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("Article");
        query.orderByDescending("createdAt");
        query.whereEqualTo("type", CategoryHelper.getNameWithIndex(selectedCategory));
        query.findInBackground(
                new FindCallback<AVObject>() {
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            mDataArray.clear();
                            String s[] = {"pic1", "pic2", "pic3", "pic4", "pic5", "pic6", "pic7", "pic8", "pic9"};
                            for (int i = 0; i < list.size(); i++) {
                                //Log.d("LeadroyaL-->", list.get(i).getAVFile("pic1").getUrl());
                                NoticeEntity entity = new NoticeEntity();
                                //entity.setPrice();
                                for (int j = 0; j < 9; j++)
                                    if (list.get(i).getAVFile(s[j]) != null)
                                        entity.setImageSrc(list.get(i).getAVFile(s[j]).getUrl(), j);
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

    private class NoticeAdapter extends BaseAdapter {

        private List<NoticeEntity> data;
        private Context context;
        private LayoutInflater mInflater;
        //private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
        private LayoutInflater inflater;
        private DisplayImageOptions options;


        public NoticeAdapter(Context context, List<NoticeEntity> data) {
            inflater = LayoutInflater.from(context);
            this.data = data;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_stub)
                    .showImageForEmptyUri(R.drawable.ic_empty)
                    .showImageOnFail(R.drawable.ic_error)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
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
            ImageLoaderConfiguration mConfig = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();

            convertView = inflater.inflate(R.layout.notice_layout, parent, false);
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.picpic);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.txttxt);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);

            //ImageLoader.getInstance().displayImage(data.get(position).getImageSrc(0), viewHolder.imgView, options, animateFirstListener);
            if (data.get(position).getImageSrc(0) != null) {
                ImageLoader mLoader = ImageLoader.getInstance();
                mLoader.init(mConfig);
                mLoader.getInstance()
                        .displayImage(data.get(position).getImageSrc(0), viewHolder.imgView, options, null);
                viewHolder.txt.setText(data.get(position).getTxt());
                viewHolder.price.setText("￥" + data.get(position).getPrice());
            }
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView imgView;
        public TextView txt;
        public TextView price;
    }

}
