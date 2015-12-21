package leadroyal.porridge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;

public class Tab05 extends Fragment implements View.OnClickListener {

    private Button bt_logout;
    private String[] s = new String[5];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag05, null);

        bt_logout = (Button) view.findViewById(R.id.logout);
        bt_logout.setOnClickListener(this);

        getUserData();

        return view;
    }

    private void getUserData() {
        s[0] = AVUser.getCurrentUser().getUsername();
        s[1] = AVUser.getCurrentUser().getEmail();
        ////累计like数
        AVQuery<AVObject> query = new AVQuery<>("PersonToArticle");
        query.whereEqualTo("user", AVUser.getCurrentUser().getUsername());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                s[2] = i+"";
            }
        });
        ////累计文章数
        AVQuery<AVObject> query2 = new AVQuery<>("Article");
        query.whereEqualTo("user", AVUser.getCurrentUser().getUsername());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                s[3] = i+"";
            }
        });


        ///其他信息，如身高体重肺活量
        ////s[4] = AVUser.getCurrentUser().getString("身高");

    }

    @Override
    public void onClick(View v) {
        AVUser.logOut();
        getActivity().finish();
    }


}
