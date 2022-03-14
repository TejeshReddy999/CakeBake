package sunny.example.com.c_bake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Baking. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BakingDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BakingListActivity extends AppCompatActivity {

    public static String widget_text = "";
    ArrayList<Steps> as = new ArrayList<>();
    List<Ingredients> ai = new ArrayList<>();
    TextView ingridents;
    String ingr = "";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    public static String getDetails() {
        return widget_text;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_list);
        ingridents = findViewById(R.id.id_ingridents);
        as = getIntent().getParcelableArrayListExtra("STEPS_KEY");
        ai = getIntent().getParcelableArrayListExtra("INGRE_KEY");
        setTitle(getIntent().getStringExtra("name"));
        for (int i = 0; i < ai.size(); i++) {

            String st = (i + 1) + "." + ai.get(i).getQuantity() + "\t" + ai.get(i).getMeasure() + "\t" + ai.get(i).getIngredient() + "\n";
            ingr = ingr + st;
        }
        ingridents.setText(ingr);

        if (findViewById(R.id.baking_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.baking_list);
        recyclerView.setFocusable(false);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.widget_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        widget_text = "";
        if (item_id == R.id.widget_bar) {
            Toast.makeText(this, "Your Recipe ingredients to widget", Toast.LENGTH_SHORT).show();
            for (int i = 0; i < ai.size(); i++) {

                String st = (i + 1) + "." + ai.get(i).getQuantity() + "\t" + ai.get(i).getMeasure() + "\t" + ai.get(i).getIngredient() + "\n";
                widget_text = widget_text + st;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, as, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final BakingListActivity mParentActivity;
        private final List<Steps> mValues;
        private final boolean mTwoPane;

        SimpleItemRecyclerViewAdapter(BakingListActivity parent,
                                      List<Steps> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.baking_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mContentView.setText(mValues.get(position).getShortDescription());
            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String vidURL = mValues.get(position).getVideoURL();
                    String desc = mValues.get(position).getDescription();
                    String thumbnail=mValues.get(position).getThumbnailURL();
                    if (mTwoPane) {
                        Bundle bundle = new Bundle();
                        bundle.putString("VID_URL", vidURL);
                        bundle.putString("DESC", desc);
                        bundle.putString("thumbnail",thumbnail);
                        bundle.putString("shodec",mValues.get(position).getShortDescription());
                        BakingDetailFragment fragment = new BakingDetailFragment();
                        fragment.setArguments(bundle);
                        mParentActivity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.baking_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, BakingDetailActivity.class);
                        intent.putExtra("STEPS", mValues.get(position));
                        intent.putExtra("VID_URL", vidURL);
                        intent.putExtra("DESC", desc);
                        intent.putExtra("thumbnail",thumbnail);
                        intent.putExtra("shodec",mValues.get(position).getShortDescription());
                        context.startActivity(intent);

                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
}
