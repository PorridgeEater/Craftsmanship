package leadroyal.porridge;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by LeadroyaL on 2015/12/4.
 */
public class GridAdapter extends BaseAdapter {

    private List<Bitmap> data;
    private Context context;
    private LayoutInflater mInflater;

    public GridAdapter(Context context, List<Bitmap> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
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
        convertView = mInflater.inflate(R.layout.grid_img_layout, null);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.imgView = (ImageView) convertView.findViewById(R.id.picpic);
        viewHolder.imgView.setImageBitmap(data.get(position));
        return convertView;
    }

    static class ViewHolder {
        public ImageView imgView;
    }
}
