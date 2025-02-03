package com.john.freezeapp;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;

public abstract class ToolbarSearchActivity extends ToolbarActivity {

    SearchView searchView;
    private String query = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_menu, menu);
        initSearchMenu(menu);
        return true;
    }

    protected void initSearchMenu(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.toolbar_search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        ToolbarSearchActivity.this.onQueryTextSubmit(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        query = s;
                        ToolbarSearchActivity.this.onQueryTextChange(s);
                        return false;
                    }
                });
                searchView.setOnCloseListener(() -> {
                    onQueryTextClose();
                    return false;
                });

            }
        }
    }

    protected String getQuery() {
        return query;
    }

    protected void onQueryTextSubmit(String query) {

    }

    protected void onQueryTextChange(String query) {

    }

    protected void onQueryTextClose() {

    }

    protected boolean closeToolbarSearch() {
        if (searchView != null && !searchView.isIconified()) {
            searchView.setQuery("", false);
            searchView.setIconified(true);
            return true;
        }
        return false;
    }

    protected boolean isSearchActive() {
        return searchView != null && searchView.isIconified();
    }

    @Override
    protected boolean interceptToolbarBack() {
        if (closeToolbarSearch()) {
            return true;
        }
        return super.interceptToolbarBack();
    }
}
