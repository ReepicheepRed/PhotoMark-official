package android.gril.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.gril.R;
import android.gril.bean.PhotoUpImageItem;
import android.gril.utils.BitmapUtil;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;


/**
 * Created by zhiPeng.S on 2016/12/1.
 */

public class SelectedAdapter extends RecyclerView.Adapter<SelectedAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = SelectedAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private RecyclerView mRecyclerView;
    private SelectedAdapter.OnItemClickListener onItemClickListener;
    private List<PhotoUpImageItem> data;
    public Context mContext;
    private BitmapFactory.Options options;
    public SelectedAdapter(Context context, List<PhotoUpImageItem> data) {
        inflater = LayoutInflater.from(context);
        mContext = context;
        this.data = data;
//        options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
    }

    public List<PhotoUpImageItem> getData() {
        return data;
    }

    public void setData(List<PhotoUpImageItem> data) {
        this.data = data;
    }

    @Override
    public SelectedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_puzzle_selected, parent, false);
        itemView.setOnClickListener(this);
        return new SelectedAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SelectedAdapter.ViewHolder holder, int position) {
        if(data != null) {
//            int padding = BitmapUtil.dip2px(mContext,16/2.0f);
//            holder.icon.setPadding(padding,padding,padding,padding);
            holder.icon.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.icon.setImageBitmap(BitmapFactory.decodeFile(data.get(position).getImagePath()));
//            holder.cancel.setImageResource(R.drawable.xiangpian_icon_quxiao);
//            int count = holder.parent.getChildCount();
//            for (int i = 0; i < count; i++) {
//                ImageView imageView = (ImageView) holder.parent.getChildAt(i);
//                if(i == 0){
//                    imageView.setPadding(padding,padding,padding,padding);
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    imageView.setImageBitmap(BitmapFactory.decodeFile(data.get(position).getImagePath()));
//                }
//                if(i == 1){
//                    imageView.setImageResource(R.drawable.xiangpian_icon_quxiao);
//                }
//            }
        }
    }

    @Override
    public int getItemCount() {
        return this.data != null ? data.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        ImageView cancel;
        RelativeLayout parent;

        private ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.i_puzzle_selected_iv);
            cancel = (ImageView) itemView.findViewById(R.id.i_puzzle_selected_cancel_iv);
//            parent = (RelativeLayout) itemView.findViewById(R.id.i_puzzle_rl);
        }

    }

    public static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        private Context context;
        public SpacesItemDecoration(Context context,int space) {
            this.context = context;
            this.space = BitmapUtil.dip2px(context,space);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            if (parent.getChildPosition(view) == 0)
                outRect.left =  BitmapUtil.dip2px(context,8);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public void setOnItemClickListener(SelectedAdapter.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        int childAdapterPosition = mRecyclerView.getChildAdapterPosition(v);
        if (onItemClickListener!=null) {
            onItemClickListener.onItemClick(v,childAdapterPosition);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
