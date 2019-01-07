package fr.halas.loginhalas.Filter;

import android.content.Context;
import android.text.TextUtils;

import com.google.firebase.firestore.Query;

import fr.halas.loginhalas.R;

/**
 * Object for passing filters around.
 */
public class Filters {

    private String year = null;
    private String section = null;
    private String groupe = null;
    private String sortBy = null;
    private Query.Direction sortDirection = null;

    public Filters() {}

    public static Filters getDefault() {
        Filters filters = new Filters();
        filters.setSortBy("Date");
        filters.setSortDirection(Query.Direction.DESCENDING);

        return filters;
    }

    public boolean hasyear() {
        return !(TextUtils.isEmpty(year));
    }

    public boolean hassection() {
        return !(TextUtils.isEmpty(section));
    }

    public boolean hasgroupe() {
        return !(TextUtils.isEmpty(groupe));
    }

    public boolean hasSortBy() {
        return !(TextUtils.isEmpty(sortBy));
    }

    public String getyear() {
        return year;
    }

    public void setyear(String year) {
        this.year = year;
    }

    public String getsection() {
        return section;
    }

    public void setsection(String section) {
        this.section = section;
    }

    public String getgroupe() {
        return groupe;
    }

    public void setgroupe(String groupe) {
        this.groupe = groupe;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Query.Direction getSortDirection() {
        return sortDirection;
    } /** DESC OR ASC   For FireDataBase **/

    public void setSortDirection(Query.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSearchDescription(Context context) {
        StringBuilder desc = new StringBuilder();

        if (year == null && section == null && groupe == null) {
            desc.append("<b>");
            desc.append(context.getString(R.string.All));
            desc.append("</b>");
        }

        if (year != null) {
            desc.append("<b>");
            desc.append(year);
            desc.append("</b>");
        }

        if (year != null && section != null) {
            desc.append(" / ");
        }

        if (section != null) {
            desc.append("<b>");
            desc.append("Section " +section);
            desc.append("</b>");
        }

        if (groupe != null) {
            desc.append(" / ");
            desc.append("<b>");
            desc.append("Groupe " +groupe);
            desc.append("</b>");
        }

        return desc.toString();
    } /** TEXT IN THE BAR Example : ' Master 1 / Section 6 for Groupe 3 ' **/

    public String getOrderDescription(Context context) {
        if ("avgGrade" == sortBy) {
            return "sorted by grade";
        } else
            return "sorted by date";
    } /** TEXT IN THE BAR Example : ' Sorted by date'  **/

}
