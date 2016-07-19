package com.codenote.tikal.example.layouts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codenote.tikal.example.R;
import com.codenote.tikal.example.data.Movie;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {
    private static final String EXTRA_DATA = "extra_data";
    private static final String IMAGE_BASE_URL_SMALL = "http://image.tmdb.org/t/p/w185";
    @Bind(R.id.image)
    public ImageView mImageView;
    @Bind(R.id.title)
    public TextView mTitle;
    @Bind(R.id.text1)
    public TextView mText1;
    @Bind(R.id.text2)
    public TextView mText2;
    @Bind(R.id.text3)
    public TextView mText3;
    @Bind(R.id.text4)
    public TextView mText4;
    @Bind(R.id.overview)
    public TextView mOverview;
    private Movie mMovie;

    public static DetailsFragment newInstance(Movie movie) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_DATA, movie);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(EXTRA_DATA);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMovie != null) {
            Picasso.with(getActivity())
                    .load(IMAGE_BASE_URL_SMALL + mMovie.poster_path)
                    .fit().centerCrop()

                    .into(mImageView);
            mTitle.setText(mMovie.title);
            mText1.setText(mMovie.release_date);
            mText2.setText(mMovie.vote_average + "/10");

//        mText3.setText(mMovie.);
//        mText4.setText(mMovie.);
            mOverview.setText(mMovie.overview);
        }
    }
}
