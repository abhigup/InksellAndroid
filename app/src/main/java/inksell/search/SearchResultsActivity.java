package inksell.search;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import Constants.AppData;
import adapters.RVAdapter;
import butterknife.InjectView;
import inksell.common.BaseActionBarActivity;
import inksell.inksell.R;
import models.CategoryEntity;
import models.LocationEntity;
import models.PostSummaryEntity;
import models.SearchEntity;
import services.InksellCallback;
import services.RestClient;
import utilities.ConfigurationManager;
import utilities.EmptyTemplateHelper;
import utilities.NavigationHelper;
import utilities.ResponseStatus;
import utilities.Utility;

public class SearchResultsActivity extends BaseActionBarActivity {

    ArrayAdapter locationAdapter;
    ArrayAdapter categoryAdapter;

    private RVAdapter rvAdapter;

    private List<PostSummaryEntity> postSummaryList;

    EmptyTemplateHelper emptyTemplateHelper;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.search_spncategory)
    Spinner spnCategory;

    @InjectView(R.id.search_spnlocation)
    Spinner spnLocation;

    @InjectView(R.id.search_recycler_view)
    RecyclerView rv;

    @InjectView(R.id.loading_spinner)
    ProgressBar progressBar;

    @InjectView(R.id.layout_error_tryAgain)
    RelativeLayout layoutErrorTryAgain;

    @InjectView(R.id.tryAgainButton)
    Button tryAgainButton;

    String query;

    @Override
    protected void initDataAndLayout() {
        populateLocations(AppData.UserData.CopmanyId);

        handleIntent(getIntent());
    }

    @Override
    protected void initActivity() {

        emptyTemplateHelper = new EmptyTemplateHelper(this);
        emptyTemplateHelper.setEmptyTemplate(R.drawable.sad, R.string.emptySearchResults);

        progressBar.setVisibility(View.GONE);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryListener().onQueryTextSubmit(query);
            }
        });

        // Set a Toolbar to replace the ActionBar.
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        locationAdapter = new ArrayAdapter(this, R.layout.spinner_item);
        spnLocation.setAdapter(locationAdapter);

        categoryAdapter = new ArrayAdapter(this, R.layout.spinner_item);
        spnCategory.setAdapter(categoryAdapter);

        categoryAdapter.addAll(Utility.getCategoryList());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);


    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_search_results;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_search_results, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager)getSystemService(ConfigurationManager.CurrentActivityContext.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(queryListener());

        searchView.setIconifiedByDefault(false);
        searchView.onActionViewExpanded();
        return true;
    }

    private SearchView.OnQueryTextListener queryListener() {
        return new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                return true;

            }

            @Override
            public boolean onQueryTextSubmit(String q) {
                // this is your adapter that will be filtered
                query = q;
                if(locationAdapter.getCount()>0) {
                    emptyTemplateHelper.setLayoutVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                    RestClient.post().searchTextV2(AppData.UserData.CopmanyId, ((LocationEntity) spnLocation.getSelectedItem()).locationId, ((CategoryEntity) spnCategory.getSelectedItem()).type.ordinal(), AppData.UserGuid, query).enqueue(searchCallback());
                }
                else
                {
                    Utility.ShowInfoDialog("No locations selected. Please try again later");
                }

                Utility.hideSoftKeyboard(SearchResultsActivity.this);
                return true;
            }
        };
    }

    private InksellCallback<List<SearchEntity>> searchCallback() {
        return new InksellCallback<List<SearchEntity>>() {
            @Override
            public void onSuccess(List<SearchEntity> searchEntities) {

                progressBar.setVisibility(View.GONE);
                layoutErrorTryAgain.setVisibility(View.GONE);

                postSummaryList = searchEntityToPostSummary(searchEntities);
                if(searchEntities.isEmpty()) {
                    emptyTemplateHelper.setLayoutVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
                else {
                    if(rvAdapter==null) {
                        rvAdapter = new RVAdapter(postSummaryList, NavigationHelper.cardViewClickListener(rv, postSummaryList, getParent()));
                        rvAdapter.setIsSearchPosts(true);
                        rv.setAdapter(rvAdapter);
                    }
                    else {
                        rvAdapter.Update(postSummaryList, NavigationHelper.cardViewClickListener(rv, postSummaryList, getParent()));
                    }
                    emptyTemplateHelper.setLayoutVisibility(View.GONE);
                    rv.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(ResponseStatus responseStatus)
            {
                progressBar.setVisibility(View.GONE);
                layoutErrorTryAgain.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
        };
    }

    private List<PostSummaryEntity> searchEntityToPostSummary(List<SearchEntity> searchEntities)
    {
        List<PostSummaryEntity> postSummaryEntities = new ArrayList<>();
        for(int i=0; i<searchEntities.size(); i++)
        {
            PostSummaryEntity postSummaryEntity = new PostSummaryEntity();
            postSummaryEntity.categoryid = searchEntities.get(i).categoryId;
            postSummaryEntity.Postdate = searchEntities.get(i).dateTime;
            postSummaryEntity.PostId = searchEntities.get(i).postId;
            postSummaryEntity.Title = searchEntities.get(i).title;
            postSummaryEntity.PostedBy = searchEntities.get(i).userName;
            postSummaryEntities.add(postSummaryEntity);
        }
        return postSummaryEntities;
    }

    private void populateLocations(int companyId) {

        RestClient.get().getLocations(companyId).enqueue(new InksellCallback<List<LocationEntity>>() {
            @Override
            public void onSuccess(List<LocationEntity> locationEntities) {
                locationAdapter.addAll((locationEntities));
                for(int i=0;i<locationEntities.size();i++)
                {
                    if(locationEntities.get(i).locationId==AppData.UserData.LocationId)
                    {
                        spnLocation.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
