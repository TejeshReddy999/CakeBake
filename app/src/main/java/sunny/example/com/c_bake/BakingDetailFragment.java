package sunny.example.com.c_bake;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

/**
 * A fragment representing a single Baking detail screen.
 * This fragment is either contained in a {@link BakingListActivity}
 * in two-pane mode (on tablets) or a {@link BakingDetailActivity}
 * on handsets.
 */
public class BakingDetailFragment extends Fragment {

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static boolean check = false;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    ImageView error_ImageView;
    TextView description;
    String vid="", des,thumbnail;
    Uri mediaUri;
    private long presentPosition = 0;
    private boolean presentAlready = true;
    /**
     * The dummy content this fragment is presenting.
     */

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BakingDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.baking_detail, container, false);
        description = rootView.findViewById(R.id.baking_detail);
        simpleExoPlayerView = rootView.findViewById(R.id.id_player);
        error_ImageView = rootView.findViewById(R.id.id_error_image);
        Activity activity = this.getActivity();
            // vid=getArguments().getString("VID_URL");

        if(!getArguments().getString("VID_URL").isEmpty())
            {
                vid = getArguments().getString("VID_URL");
            }else if(!getArguments().getString("thumbnail").isEmpty())
            {
                vid=thumbnail=getArguments().getString("thumbnail");
                if(getArguments().getString("thumbnail").equalsIgnoreCase("mp4"))
                {
                    Glide.with(this).load(vid).into(error_ImageView);

                }
            }else
            {
                simpleExoPlayerView.setVisibility(rootView.GONE);
                Toast.makeText(getContext(), "no video", Toast.LENGTH_SHORT).show();
            }

            des = getArguments().getString("DESC");

        mediaUri = Uri.parse(vid);
        if (savedInstanceState != null) {
            presentPosition = savedInstanceState.getLong("position");
            presentAlready = savedInstanceState.getBoolean("playState");
        }
        description.setText(des);
        play(mediaUri);
        return rootView;
    }

    private void play(Uri videoUri) {
        if (simpleExoPlayer == null && !String.valueOf(videoUri).isEmpty()) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new
                    AdaptiveTrackSelection.Factory(bandwidthMeter));

            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("ReciepieVideo");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, dataSourceFactory, extractorsFactory, null, null);

            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(presentAlready);
            simpleExoPlayer.seekTo(presentPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        check = true;
        if(vid!="") {
            presentPosition = simpleExoPlayer.getCurrentPosition();
            presentAlready = simpleExoPlayer.getPlayWhenReady();
        }
            outState.putLong("position", presentPosition);
            outState.putBoolean("playState", presentAlready);

    }
    @Override
    public void onDestroy() {
        if (simpleExoPlayer != null)
            simpleExoPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        if (simpleExoPlayer != null)
            simpleExoPlayer.release();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (simpleExoPlayer != null) {
            if (simpleExoPlayer.getCurrentPosition() != simpleExoPlayer.getDuration())
                simpleExoPlayer.release();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (simpleExoPlayer != null) {
                ViewGroup.LayoutParams layoutParams = simpleExoPlayerView.getLayoutParams();
                layoutParams.width = layoutParams.MATCH_PARENT;
                layoutParams.height = layoutParams.MATCH_PARENT;
                simpleExoPlayerView.setLayoutParams(layoutParams);
            }
        }
    }


}
