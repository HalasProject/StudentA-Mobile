package fr.halas.loginhalas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.halas.loginhalas.Filter.Filters;

/**
 * Dialog Fragment containing filter form.
 */
public class FilterDialogFragment extends DialogFragment {

    public static final String TAG = "FilterDialog";

    interface FilterListener { void onFilter(Filters filters); }

    private View mRootView;

    @BindView(R.id.spinner_year)
    Spinner myearSpinner;

    @BindView(R.id.spinner_section)
    Spinner mSectionSpinner;

    @BindView(R.id.spinner_groupe)
    Spinner mGroupeSpinner;

    @BindView(R.id.spinner_sort)
    Spinner mSortSpinner;

    private FilterListener mFilterListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.recherche, container, false);
        ButterKnife.bind(this, mRootView);

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterListener) {
            mFilterListener = (FilterListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }


    /** BUTTON SEARCH & CANCALED **/

    @OnClick(R.id.button_search)
    public void onSearchClicked() {
        if (mFilterListener != null) {
            mFilterListener.onFilter(getFilters());
        }

        dismiss(); //FERMER LE POP-UP
    }

    @OnClick(R.id.button_cancel)
    public void onCancelClicked() {
        dismiss(); // FERMER LE POP-UP
    }



    /** GET SELECT DATA Year & Section & Groupe & Sorted **/

    @Nullable
    private String getSelectedyear() {
        String selected = (String) myearSpinner.getSelectedItem();
        if (selected.equals(getString(R.string.SelectGrade))) {
            return null;
        } else {
            return selected;
        }
    }

    @Nullable
    private String getSelectedSection() {
        String selected = (String) mSectionSpinner.getSelectedItem();
        if (selected.equals(getString(R.string.SelectSection))) {
            return null;
        } else {
            return selected;
        }
    }
    @Nullable
    private String getSelectedGroupe() {
        String selected = (String) mGroupeSpinner.getSelectedItem();
        if (selected.equals(getString(R.string.SelectGroupe))) {
           return null;
        } else {
            return selected;
        }
    }

    private String getSelectedSortBy() {
        /**String selected = (String) mSortSpinner.getSelectedItem();
        if (selected.equals("Sort by grade") ) {
            return "Grade";
        } else**/
            return "Date";
    }

    private Query.Direction getSortDirection() {
       /** String selected = (String) mSortSpinner.getSelectedItem();
        if (selected.equals("Sort by grade")) {
            return Query.Direction.ASCENDING;
        } else**/
            return Query.Direction.DESCENDING;
        }

    /** FILTER **/

    public void resetFilters() {
        if (mRootView != null) {
            myearSpinner.setSelection(0);
            mSectionSpinner.setSelection(0);
            mGroupeSpinner.setSelection(0);
            mSortSpinner.setSelection(0);
        }
    }

    public Filters getFilters() {
        Filters filters = new Filters();

        if (mRootView != null) {
            filters.setyear(getSelectedyear());
            filters.setsection(getSelectedSection());
            filters.setgroupe(getSelectedGroupe());
            filters.setSortBy(getSelectedSortBy());
            filters.setSortDirection(getSortDirection());
        }

        return filters;
    }
}
