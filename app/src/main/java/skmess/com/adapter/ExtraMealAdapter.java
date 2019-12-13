package skmess.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import skmess.com.R;

public class ExtraMealAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<ExtraMealModel> mylist = new ArrayList<ExtraMealModel>();

    public ExtraMealAdapter(ArrayList<ExtraMealModel> itemArray, Context mContext) {
        super();
        this.mContext = mContext;
        mylist = itemArray;
    }

    @Override
    public int getCount() {
        return mylist.size();
    }

    @Override
    public String getItem(int position) {
        return mylist.get(position).toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        private TextView date,price;
        private TextView breakfast,lunch,dinner;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder view;
        LayoutInflater inflator = null;
        if (convertView == null) {
            view = new ViewHolder();
            try {

                inflator = ((Activity) mContext).getLayoutInflater();
                convertView = inflator.inflate(R.layout.extramealdetails, null);
                view.date = (TextView) convertView.findViewById(R.id.date_select2);
                view.price = (TextView) convertView.findViewById(R.id.price_total2);
                view.breakfast = (TextView) convertView.findViewById(R.id.img_bf2);
                view.lunch = (TextView) convertView.findViewById(R.id.img_l2);
                view.dinner = (TextView) convertView.findViewById(R.id.img_d2);

                convertView.setTag(view);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } else {
            view = (ViewHolder) convertView.getTag();
        }
        try {
            view.date.setText(mylist.get(position).getDate());
            view.price.setText(mylist.get(position).getPrice());

            view.breakfast.setText(mylist.get(position).getBreakfast());
            view.lunch.setText(mylist.get(position).getLunch());
            view.dinner.setText(mylist.get(position).getDinner());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}