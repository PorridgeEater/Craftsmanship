package leadroyal.porridge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;


public class Tab01 extends Fragment{

    private GridView mGridView;
    private NoticeAdapter mAdapter;
    private ArrayList<NoticeEntity> mDataArray = new ArrayList<>();
    private DisplayImageOptions options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag01, null);

        initView(view);
        initAdapter();
        //new ImageLoaderConfiguration();
        refresh();
        return view;
    }


    private void initAdapter() {
        mAdapter = new NoticeAdapter(getContext(), mDataArray);
        mGridView.setAdapter(mAdapter);

    }

    private void initView(View v) {
        mGridView = (GridView) v.findViewById(R.id.notice_grid);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d("LeadroyaL", position + "");
                Intent intent = new Intent();
                intent.setClass(getActivity(), leadroyal.porridge.NoticeDetail.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("newsAVO", mDataArray.get(position).getAVO());
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });
    }

    private void refresh() {
//        List<AVObject> folloees;
//
//        AVQuery<AVObject> query = new AVQuery<>("PersonToPerson");
//        query.whereEqualTo("follower", AVUser.getCurrentUser());
//        query.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//                if (e == null) {
//                    folloees = list;
//                }
//            }
//        });

        final List<String> idArray = new ArrayList<>();
        AVQuery<AVObject> query_before = new AVQuery<>("PersonToArticle");
        query_before.whereEqualTo("user", AVUser.getCurrentUser());
        query_before.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if ( e == null ) {
                    idArray.clear();
                    for ( int i=0; i<list.size(); i++ ) {
                        idArray.add(list.get(i).getAVObject("Article").getObjectId());
                    }

                    AVQuery<AVObject> query;
                    query = new AVQuery<AVObject>("Article");
                    query.orderByDescending("createdAt" );
                    query.whereEqualTo("objectId", idArray.get(0));

                    query.findInBackground(
                            new FindCallback<AVObject>() {
                                public void done(List<AVObject> list, AVException e) {
                                    if (e == null) {
                                        Toast.makeText(getActivity(), "tttttt"+list.size(), Toast.LENGTH_SHORT).show();

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
            }
        });

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
            ImageLoaderConfiguration mConfig = new ImageLoaderConfiguration.Builder(getContext()).build();

            convertView = inflater.inflate(R.layout.notice_layout, parent, false);
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.picpic);
            viewHolder.txt = (TextView) convertView.findViewById(R.id.txttxt);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);

            //ImageLoader.getInstance().displayImage(data.get(position).getImageSrc(0), viewHolder.imgView, options, animateFirstListener);
            if (data.get(position).getImageSrc(0) != null) {
                ImageLoader mLoader = ImageLoader.getInstance();
                mLoader.init(mConfig);
                mLoader.getInstance().displayImage(data.get(position).getImageSrc(0), viewHolder.imgView, options, null);
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