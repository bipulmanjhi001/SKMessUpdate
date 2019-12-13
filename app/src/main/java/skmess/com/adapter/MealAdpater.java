package skmess.com.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import skmess.com.R;

public class MealAdpater extends BaseAdapter {

    private Context mContext;
    ArrayList<MealModel> mylist = new ArrayList<MealModel>();

    public MealAdpater(ArrayList<MealModel> itemArray, Context mContext) {
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
        private ImageView breakfast,lunch,dinner;
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
                convertView = inflator.inflate(R.layout.meal_details_list, null);
                view.date = (TextView) convertView.findViewById(R.id.date_select);
                view.price = (TextView) convertView.findViewById(R.id.price_total);
                view.breakfast = (ImageView) convertView.findViewById(R.id.img_bf);
                view.lunch = (ImageView) convertView.findViewById(R.id.img_l);
                view.dinner = (ImageView) convertView.findViewById(R.id.img_d);

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
            if(mylist.get(position).getBreakfast().equals("0")){
                try {
                    Glide.with(mContext).load(R.drawable.ic_clear)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(view.breakfast);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }else {
                try {
                    Glide.with(mContext).load(R.drawable.ic_check_mark)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(view.breakfast);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
            if(mylist.get(position).getLunch().equals("0")){
                try {
                    Glide.with(mContext).load(R.drawable.ic_clear)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(view.lunch);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }else {
                try {
                    Glide.with(mContext).load(R.drawable.ic_check_mark)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(view.lunch);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }

            }
            if(mylist.get(position).getDinner().equals("0")){
                try {
                    Glide.with(mContext).load(R.drawable.ic_clear)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(view.dinner);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }else {
                try {
                    Glide.with(mContext).load(R.drawable.ic_check_mark)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(view.dinner);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}