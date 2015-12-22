package leadroyal.porridge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFollowResponse;
import com.avos.avoscloud.AVFriendship;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;

import org.w3c.dom.Text;

import java.util.List;

public class Tab05 extends Fragment implements View.OnClickListener {

    private Button bt_logout;
    private String[] s = new String[7];
    private TextView p_user;
    private TextView p_info;
    private TextView p_post;
    private TextView p_like;
    private TextView p_comment;
    private TextView p_fan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag05, null);

        p_user = (TextView)view.findViewById(R.id.userID);
        p_info = (TextView)view.findViewById(R.id.personInfo);
        p_post = (TextView)view.findViewById(R.id.post_num);
        p_like = (TextView)view.findViewById(R.id.like_num);
        p_comment = (TextView)view.findViewById(R.id.comment_num);
        p_fan = (TextView)view.findViewById(R.id.fan_num);

        bt_logout = (Button) view.findViewById(R.id.logout);
        bt_logout.setOnClickListener(this);

        getUserData();

        if (s[0] != null)
            p_user.setText(s[0]);
        else if (s[1] != null)
            p_user.setText(s[1]);

        p_info.setText(s[2]);

        p_post.setText(s[3]);
        p_like.setText(s[4]);
        p_comment.setText(s[5]);
        p_fan.setText(s[6]);

        return view;
    }

    private void getUserData() {
        AVUser usr = AVUser.getCurrentUser();
        s[0] = usr.getUsername();
        s[1] = usr.getEmail();
        s[2] = usr.getString("userintro");

        ////累计文章数
        AVQuery<AVObject> query = new AVQuery<>("Article");
        query.whereEqualTo("user", s[0]);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, AVException e) {
                if (e == null)
                    s[3] = "" + count;
                else {
                    s[3] = "" + 0;
                }
            }
        });
//        Toast.makeText(getContext(), "文章数"+s[3], Toast.LENGTH_SHORT).show();

        ////累计like数
        AVQuery<AVObject> query2 = new AVQuery<>("PersonToArticle");
        query2.whereEqualTo("user", usr);
        query2.countInBackground(new CountCallback() {
            @Override
            public void done(int count, AVException e) {
                if (e == null)
                    s[4] = "" + count;
                else {
                    s[4] = "" + 0;
                }
            }
        });


        ////累计comment
        AVQuery<AVObject> query3 = new AVQuery<>("PersonToArticle");
        query3.whereEqualTo("user", s[0]);
        query3.countInBackground(new CountCallback() {
            @Override
            public void done(int count, AVException e) {
                if ( e == null )
                    s[5] = ""+count;
                else {
                    s[5] = ""+0;
                }
            }
        });

        ////累计粉丝
        AVQuery<AVObject> query4 = new AVQuery<>("PersonToPerson");
        query4.whereEqualTo("Followee", usr);
        query4.countInBackground(new CountCallback() {
            @Override
            public void done(int count, AVException e) {
                if ( e==null )
                    s[6] = ""+count;
                else {
                    s[6] = ""+0;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        AVUser.logOut();
        getActivity().finish();
    }


}
