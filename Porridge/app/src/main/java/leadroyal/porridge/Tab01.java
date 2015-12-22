package leadroyal.porridge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.List;

public class Tab01 extends Fragment implements View.OnClickListener {
    private GridView mGridView;
    private NoticeAdapter mAdapter;
    private ArrayList<NoticeEntity> mDataArray = new ArrayList<>();
    private String[] myTitles;

    private DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag01, null);

        initView(view);
        initAdapter();
        //new ImageLoaderConfiguration();
        return view;
    }


    private void initAdapter() {
        myTitles = new String[] {"茶器","文房","包","银器","禅","手镯","花器","紫砂","雕刻"};
        mAdapter = new NoticeAdapter(getContext(), myTitles);

        mGridView.setAdapter(mAdapter);

    }

    private void initView(View v) {
        mGridView = (GridView) v.findViewById(R.id.notice_grid);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), leadroyal.porridge.CategoryList.class);
                Bundle bundle = new Bundle();
                bundle.putInt("selectedCategory", position);
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
        query.findInBackground(
                new FindCallback<AVObject>() {
                    public void done(List<AVObject> list, AVException e) {
                        if (e == null) {
                            mDataArray.clear();
                            String s[] = {"pic1", "pic2", "pic3", "pic4", "pic5", "pic6", "pic7", "pic8", "pic9"};
                            for (int i = 0; i < list.size(); i++) {
                                NoticeEntity entity = new NoticeEntity();
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

        private String[] data;
        private Context context;
        private LayoutInflater mInflater;
        private LayoutInflater inflater;
        private DisplayImageOptions options;

        public NoticeAdapter(Context context, String[] data) {
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
            return this.data.length;
        }

        public Object getItem(int position) {
            return data[position];
        }


        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder = new ViewHolder();
            //ImageLoaderConfiguration mConfig = new ImageLoaderConfiguration.Builder(getContext()).build();

            convertView = inflater.inflate(R.layout.grid_cell, parent, false);
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.ItemImage);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.ItemText);

            //ImageLoader.getInstance().displayImage(data.get(position).getImageSrc(0), viewHolder.imgView, options, animateFirstListener);

            int indentify = getContext().getResources().getIdentifier(getContext().getPackageName() + ":drawable/" + "discover" + (position + 1), null, null);
            if (indentify>0) viewHolder.imgView.setImageResource(indentify);
            viewHolder.txt.setText(myTitles[position]);


            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView imgView;
        public TextView txt;
    }

}
