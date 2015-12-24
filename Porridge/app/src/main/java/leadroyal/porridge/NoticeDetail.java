package leadroyal.porridge;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LeadroyaL on 2015/12/5.
 */
public class NoticeDetail extends AppCompatActivity implements View.OnClickListener {

    private NoticeDetailAdapter mAdapter;
    private NoticeEntity entity;
    private AVObject tp_news;
    private String s[] = {"pic1", "pic2", "pic3", "pic4", "pic5", "pic6", "pic7", "pic8", "pic9"};
    private ArrayList<String> mData = new ArrayList<>();
    private ListViewMeasreHeight mListView;
    private Button like, comment;
    private EditText commentText;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);

        Bundle bundle = getIntent().getExtras();
        tp_news = bundle.getParcelable("newsAVO");
        for (int i = 0; i < 9; i++)
            if (tp_news.getAVFile(s[i]) != null)
                mData.add(tp_news.getAVFile(s[i]).getUrl());
        initView();
        initAdapter();
        initCheckLike();

        refresh_comment();
    }

    private void initCheckLike() {
        AVQuery<AVObject> query = new AVQuery<AVObject>("PersonToArticle");
        query.whereEqualTo("user", AVUser.getCurrentUser());
        query.whereEqualTo("Article", tp_news);
//        query.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//                if (e == null) {
//                    like.setEnabled(false);
//                    like.setText("已经like过");
//                }
//            }
//        });
        query.countInBackground(new CountCallback() {
            public void done(int count, AVException e) {
                if (e == null) {
                    // The count request succeeded. Log the count
                    if (count != 0) {
                        like.setEnabled(false);
                        like.setBackground(getDrawable(R.drawable.heart_selected));
                    }
                } else {
                    // The request failed
                }
            }
        });
    }

    private void initAdapter() {
        mAdapter = new NoticeDetailAdapter(getApplicationContext());
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mListView = (ListViewMeasreHeight) findViewById(R.id.listview);
        mTextView = (TextView) findViewById(R.id.textview);
        like = (Button) findViewById(R.id.like_it);
        comment = (Button) findViewById(R.id.comment_it);
        commentText = (EditText) findViewById(R.id.comment_text);

        like.setOnClickListener(this);
        comment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.like_it:
                like(tp_news);
                break;
            case R.id.comment_it:
                comment();
                break;
        }
    }

    private void comment() {
        String s = commentText.getText().toString();
        if (!s.trim().isEmpty()) {
            AVObject avo = new AVObject("Comment");
            avo.put("Article", tp_news);
            avo.put("user", AVUser.getCurrentUser().getUsername());
            avo.put("content", s);
            avo.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null)
                        refresh_comment();
                    else
                        e.printStackTrace();
                }
            });

        }
    }

    private void refresh_comment() {
        commentText.setText("");
        AVQuery<AVObject> query = new AVQuery<AVObject>("Comment");
        query.whereEqualTo("Article", tp_news);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (list != null) {
                    Log.d("lead---->", "查询到" + list.size() + "个评论");
                    String s = "";
                    for (int i = 0; i < list.size(); i++) {
                        s = s + i + "L   " + list.get(i).getString("user") + '\n' + list.get(i).getString("content") + "\n\n";
                    }
                    mTextView.setText(s);
                }
            }
        });
    }

    private void like(AVObject avo) {
        AVUser current_user = AVUser.getCurrentUser();
        AVObject relation = new AVObject("PersonToArticle");
        relation.put("user", current_user);
        relation.put("Article", avo);
        relation.saveInBackground();
        like.setEnabled(false);
        like.setBackground(getDrawable(R.drawable.heart_selected));
    }

    private class NoticeDetailAdapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater;
        private DisplayImageOptions options;

        public NoticeDetailAdapter(Context context) {
            this.context = context;
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


        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.d("LEADROYAL-->", position + "正在加载");
            final ViewHolder holder;
            inflater = LayoutInflater.from(context);
            View rootView = inflater.inflate(R.layout.fr_image_list, null);
            holder = new ViewHolder();
            holder.imgView = (ImageView) rootView.findViewById(R.id.image);

            ImageLoader.getInstance().displayImage(mData.get(position), holder.imgView, options, null);

            return rootView;
        }
    }

    static class ViewHolder {
        ImageView imgView;
    }
}
