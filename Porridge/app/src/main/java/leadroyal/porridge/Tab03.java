package leadroyal.porridge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tab03 extends Fragment implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private GridView mGridView;
    private ImageButton mImg;
    private Button submit;
    private EditText mEditText;
    private String picPath = null;
    private ArrayList<Bitmap> picArray = new ArrayList<>();
    private String[] pathArray = new String[9];
    private GridAdapter mAdapter;
    private AVObject article;
    private AVFile avFile[] = new AVFile[9];
    private TextView typelist;
    private TextView price;

//
//    private ArrayList<SpinnerOption> typelist;
//    private Spinner typeSp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag03, null);
//
//        typelist = new ArrayList<SpinnerOption>();
//        SpinnerOption x;
//        x = new SpinnerOption(1+"", 1 + " 茶器");
//        typelist.add(x);
//        x = new SpinnerOption(2+"", 2 + " 文房");
//        typelist.add(x);
//        x = new SpinnerOption(3+"", 3 + " 包");
//        typelist.add(x);
//        x = new SpinnerOption(4+"", 4 + " 银器");
//        typelist.add(x);
//        x = new SpinnerOption(5+"", 5 + " 禅");
//        typelist.add(x);
//        x = new SpinnerOption(6+"", 6 + " 手镯");
//        typelist.add(x);
//        x = new SpinnerOption(7+"", 7 + " 花器");
//        typelist.add(x);
//        x = new SpinnerOption(8+"", 8 + " 紫砂")
//        typelist.add(x);
//        x = new SpinnerOption(9+"", 9 + " 雕刻")
//        typelist.add(x);
//
//        typeSp = (Spinner)view.findViewById(R.id.type_choose);
//
        initView(view);
        initAdapter();
        return view;
    }

    private void initAdapter() {
        mAdapter = new GridAdapter(getContext(), picArray);
        mGridView.setAdapter(mAdapter);
    }

    private void initView(View v) {
        mGridView = (GridView) v.findViewById(R.id.pic_grid);
        mImg = (ImageButton) v.findViewById(R.id.img_bt);
        mEditText = (EditText) v.findViewById(R.id.article);
        submit = (Button) v.findViewById(R.id.submit_article);
        price = (EditText) v.findViewById(R.id.price);
        typelist = (EditText) v.findViewById(R.id.type_choose);
        price.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        mImg.setOnClickListener(this);
        mGridView.setOnItemLongClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_bt) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(intent, 1);
        } else if (v.getId() == R.id.submit_article) {
            if (!mEditText.getText().toString().trim().isEmpty()) {
                article = new AVObject("Article");
                article.put("content", mEditText.getText().toString());
                article.put("type", typelist.getText().toString());
                article.put("price", "￥"+price.getText().toString());
                String user = AVUser.getCurrentUser().getUsername();
                article.put("user", user);
                mEditText.setText("");
                String s[] = {"pic1", "pic2", "pic3", "pic4", "pic5", "pic6", "pic7", "pic8", "pic9"};
                final File file[] = new File[9];

                try {
                    for (int i = 0; i < picArray.size(); i++) {
                        file[i] = new File(pathArray[i]);
                        if (file[i] != null) {
                            avFile[i] = AVFile.withFile(s[i], file[i]);
                            avFile[i].saveInBackground();
                            article.put(s[i], avFile[i]);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                article.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(getContext(), "发布成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        picArray.clear();
                        //pathArray.clear();
                        mAdapter.notifyDataSetChanged();
                        mEditText.setText("");
                        price.setText("");
                        typelist.setText("");
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            /**
             * 当选择的图片不为空的话，在获取到图片的途径
             */
            Uri uri = data.getData();
            Log.e("TAG", "uri = " + uri);
            try {
                String[] pojo = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().managedQuery(uri, pojo, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = getActivity().getContentResolver();
                    int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);

                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        picPath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        //imageView.setImageBitmap(bitmap);
                        if (picArray.size() < 9) {
                            picArray.add(bitmap);
                            pathArray[picArray.size() - 1] = picPath;
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        alert();
                    }
                } else {
                    alert();
                }

            } catch (Exception e) {
                alert();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        picPath = null;
                    }
                }).create();
        dialog.show();
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        picArray.remove(position);
        //pathArray.remove(position);
        mAdapter.notifyDataSetChanged();
        return true;
    }
}
