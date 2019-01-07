package fr.halas.loginhalas;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.r0adkll.slidr.model.SlidrPosition;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.halas.loginhalas.Filter.AffichageAdapter;
import fr.halas.loginhalas.Filter.Filters;
import fr.halas.loginhalas.Filter.MainActivityViewModel;


public class ViewActivity extends AppCompatActivity implements
        FilterDialogFragment.FilterListener,
        AffichageAdapter.OnAffichageSelectedListener {



    //Animation
    private SlidrInterface slidr;

    //FireBase
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseFirestore mFirestore;
    private Query mQuery;


    private FilterDialogFragment mFilterDialog;
    private AffichageAdapter mAdapter;

    private MainActivityViewModel mViewModel;


    @BindView(R.id.text_current_search)
    TextView mCurrentSearchView;

    @BindView(R.id.text_current_sort_by)
    TextView mCurrentSortByView;

    @BindView(R.id.listView)
    RecyclerView ListView;

    @BindView(R.id.addAffichage)
    FloatingActionButton adding;

    @BindView(R.id.progressBar)
    ProgressBar barPro;

    @BindView(R.id.TextNoData)
    TextView NoDataText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        ButterKnife.bind(this);
        barPro.setVisibility(View.VISIBLE);

        // View model
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Firestore
        mFirestore = FirebaseFirestore.getInstance();

        // Get ${LIMIT} Affichages
        mQuery = mFirestore.collection("affichages")
                .orderBy("Date", Query.Direction.DESCENDING)
                .limit(50);

        // RecyclerView
        mAdapter = new AffichageAdapter(mQuery, this,this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    barPro.setVisibility(View.GONE);
                    ListView.setVisibility(View.GONE);
                    NoDataText.setVisibility(View.VISIBLE);

                } else {
                    barPro.setVisibility(View.GONE);
                    ListView.setVisibility(View.VISIBLE);
                    NoDataText.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        ListView.setLayoutManager(new LinearLayoutManager(this));
        ListView.setAdapter(mAdapter);

        // Filter Dialog
        mFilterDialog = new FilterDialogFragment();


        adding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewActivity.this, AddingActivity.class));
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });

        SlidrConfig config = new SlidrConfig.Builder()

                .primaryColor(getResources().getColor(R.color.colorPrimary))
                .secondaryColor(getResources().getColor(R.color.colorPrimaryDark))
                .position(SlidrPosition.LEFT)
                .sensitivity(1f)
                .scrimColor(Color.BLACK)
                .scrimStartAlpha(0.8f)
                .scrimEndAlpha(0f)
                .velocityThreshold(2400)
                .distanceThreshold(0.25f)
                .edge(true)
                .edgeSize(0.18f) // The % of the screen that counts as the edge, default 18%
                .build();

        slidr = Slidr.attach(this, config);



    }

    @OnClick(R.id.filter_bar)
    public void onFilterClicked() {
        // Show the dialog containing filter options
        mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
    }


    @OnClick(R.id.button_clear_filter)
    public void onClearFilterClicked() {
        mFilterDialog.resetFilters();

        onFilter(Filters.getDefault());
    }


    @Override
    protected void onStart() {
        super.onStart();

        onFilter(mViewModel.getFilters());

        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }


    @Override
    public void onFilter(Filters filters) {

        Query query = db.collection("affichages");

        // Year (equality filter)
        if (filters.hasyear()) {
            query = query.whereEqualTo("Year", filters.getyear());
        }

        // Section (equality filter)
        if (filters.hassection()) {
            query = query.whereEqualTo("Section", filters.getsection());
        }

        // Groupe (equality filter)
        if (filters.hasgroupe()) {
            query = query.whereEqualTo("Groupe", filters.getgroupe());
        }

        // Sort by (orderBy with direction)
        if (filters.hasSortBy()) {
            query = query.orderBy(filters.getSortBy(), filters.getSortDirection());
        }

        // Limit items
        query = query.limit(50);

        // Update the query
        mAdapter.setQuery(query);

        // Set header
        mCurrentSearchView.setText(Html.fromHtml(filters.getSearchDescription(this)));
        mCurrentSortByView.setText(filters.getOrderDescription(this));

        // Save filters
        mViewModel.setFilters(filters);
    }


    @Override
    public void onAffichageSelected(DocumentSnapshot affichage) {

        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("AffichageID", affichage.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
    }

}


