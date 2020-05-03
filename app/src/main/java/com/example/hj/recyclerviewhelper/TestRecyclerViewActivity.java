package com.example.hj.recyclerviewhelper;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hj.myrecyclerview.RecyclerViewHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class TestRecyclerViewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private RecyclerViewHelper recyclerViewHelper;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private ArrayList<String> dataList = new ArrayList<String>();
    private SwipeRefreshLayout swipeRefreshLayout;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_recycler_view);
        initView();
        setSupportActionBar(toolbar);
        initLData("linear");
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerview);
        toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dataList.clear();
                initLData("linear");
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initLData(String type) {
        dataList.clear();
        getData();
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(dataList);
        if ("grid".equals(type)) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(TestRecyclerViewActivity.this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(TestRecyclerViewActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
        recyclerViewHelper = new RecyclerViewHelper(myRecyclerViewAdapter);
        recyclerView.setAdapter(recyclerViewHelper);
        recyclerView.addOnScrollListener(new RecyclerViewHelper.RecyclerViewScrollListener() {
            @Override
            public void loadMore() {
                recyclerViewHelper.setLoadingState(recyclerViewHelper.isLoading);
                if (dataList.size() < 12) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getData();
                            recyclerViewHelper.setLoadingState(recyclerViewHelper.isLoadindComplete);
                        }
                    }, 1500);//3秒后执行Runnable中的run方法
                } else {
                    recyclerViewHelper.setLoadingState(recyclerViewHelper.isLoadindEnd);
                }
            }
        });
    }

    private void getData() {
        dataList.add("1");
        dataList.add("2");
        dataList.add("3");
        dataList.add("4");
        dataList.add("5");
        dataList.add("6");
        dataList.add("7");
        dataList.add("8");
        dataList.add("9");
        dataList.add("10");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.linearLayout_menu:
                initLData("linear");
                break;
            case R.id.gridLayout_menu:
                initLData("grid");
                break;
        }
        return true;
    }
}
