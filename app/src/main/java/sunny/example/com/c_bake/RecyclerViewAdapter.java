package sunny.example.com.c_bake;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dell on 6/4/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    Context mitemContext;
    ArrayList<Item> mitemData;
    public static String name = "";
    AdapterView.OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(Context itemContext, ArrayList<Item> itemData) {
        this.mitemContext = itemContext;
        this.mitemData = itemData;
    }

    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mitemContext);
        view = inflater.inflate(R.layout.items_card, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.MyViewHolder holder, int position) {
        final Item item = mitemData.get(position);
        name = item.getName();
        holder.itemText.setText(item.getName());
        holder.itemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailIntent = new Intent(mitemContext, BakingListActivity.class);
                detailIntent.putParcelableArrayListExtra("STEPS_KEY", item.getSteps());
                detailIntent.putParcelableArrayListExtra("INGRE_KEY", (ArrayList<? extends Parcelable>) item.getIngredients());
                detailIntent.putExtra("name",item.getName());
                v.getContext().startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mitemData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.items_first);
        }
    }
}
