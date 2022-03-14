package sunny.example.com.c_bake;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * An activity representing a single Baking detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link BakingListActivity}.
 */
public class BakingDetailActivity extends AppCompatActivity {

    Steps steps;
    BakingDetailFragment frag;
    String video, desc,thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_detail);
        video = getIntent().getStringExtra("VID_URL");
        desc = getIntent().getStringExtra("DESC");
        thumbnail=getIntent().getStringExtra("thumbnail");
        setTitle(getIntent().getStringExtra("shodec"));
        if (savedInstanceState != null) {
            frag = (BakingDetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, "FRAG");
        } else {
            frag = new BakingDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("VID_URL", video);
            bundle.putString("DESC", desc);
            bundle.putString("thumbnail",thumbnail);
            frag.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.baking_detail_container, frag)
                    .commit();
        }
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("STEPS", getIntent().getParcelableExtra("STEPS"));
        getSupportFragmentManager().putFragment(outState, "FRAG", frag);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        steps = savedInstanceState.getParcelable("STEPS");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, BakingListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
