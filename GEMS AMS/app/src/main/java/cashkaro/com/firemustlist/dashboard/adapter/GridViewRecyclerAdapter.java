package cashkaro.com.firemustlist.dashboard.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cashkaro.com.firemustlist.R;
import cashkaro.com.firemustlist.dashboard.customview.CircleView;
import cashkaro.com.firemustlist.dashboard.model.SchoolList;

import static android.content.ContentValues.TAG;


public class GridViewRecyclerAdapter extends RecyclerView.Adapter<GridViewRecyclerAdapter.MyViewHolder> {

    public int selectedPosition = 0;
    private OnItemClickListener onItemClickListener;

    public GridViewRecyclerAdapter(Context context, List<SchoolList> list) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = (OnItemClickListener) context;
    }

    private Context context;
    private List<SchoolList> list;

    public void update(List<SchoolList> list) {
//        this.list = new ArrayList<>();
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();

        Log.e(TAG, "update: Called    ");
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_row, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final SchoolList tabMenu = list.get(position);

        holder.schoolname.setText(tabMenu.getName());

        if (tabMenu.is_selected()) {
            holder.schoolview.setCircleColor(tabMenu.getColor());
            holder.schoolview.setAlpha(1f);
        } else {
            holder.schoolview.setCircleColor(Color.parseColor("#000000"));
            holder.schoolview.setAlpha(.25f);
        }

        Log.e(TAG, "onBindViewHolder: " + tabMenu.is_selected());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tabMenu.is_selected()) {
                    onItemClickListener.remove(tabMenu, position);
//                    tabMenu.setIs_selected(false);
                } else {
                    onItemClickListener.add(tabMenu, position);
//                    tabMenu.setIs_selected(true);
                }

//                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView schoolname;
        private CircleView schoolview;// init the item view's
//        private LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
// get the reference of item view's
            schoolname = (TextView) itemView.findViewById(R.id.schoolname);
            schoolview = (CircleView) itemView.findViewById(R.id.schoolview);
//            linearLayout = (LinearLayout) itemView.findViewById(R.id.tabmenu);


        }
    }

    public interface OnItemClickListener {
        void add(SchoolList itemName, int pos);

        void remove(SchoolList itemName, int pos);

    }
}