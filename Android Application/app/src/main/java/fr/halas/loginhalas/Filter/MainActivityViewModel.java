package fr.halas.loginhalas.Filter;

import android.arch.lifecycle.ViewModel;

/**
 * ViewModel for {@link fr.halas.loginhalas.ViewActivity}.
 */

public class MainActivityViewModel extends ViewModel {

    private Filters mFilters;

    public MainActivityViewModel() {

        mFilters = Filters.getDefault();
    }

    public Filters getFilters() {
        return mFilters;
    }

    public void setFilters(Filters mFilters) {
        this.mFilters = mFilters;
    }
}
