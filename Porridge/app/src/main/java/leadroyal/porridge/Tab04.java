package leadroyal.porridge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

public class Tab04 extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag04, null);
        initView(view);
        return view;
    }



    private void initView(View v) {
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.comment_info);


        rl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), leadroyal.porridge.GeneralMessageList.class);
//                Bundle bundle = new Bundle();
//                bundle.putInt("selectedCategory", position);
//                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

}
