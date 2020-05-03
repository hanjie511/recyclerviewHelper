package com.example.hj.myrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewHelper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView.Adapter adapter;
    private static final int normal_view = 1;
    private static final int footer_view = 2;
    public final int isLoading = 1;
    public final int isLoadindComplete = 2;
    public final int isLoadindEnd = 3;
    private int loadState = 2;//默认加载完成

    public RecyclerViewHelper(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getItemCount() {
        return adapter.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        int i = 0;
        if (position + 1 == getItemCount()) {
            i = footer_view;
        } else {
            i = normal_view;
        }
        return i;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == normal_view) {
            return adapter.onCreateViewHolder(parent, normal_view);
        } else {
            adapter.onCreateViewHolder(parent, footer_view);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerviewhelper_footerview, parent, false);
            return new FooterViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (footer_view == getItemViewType(position)) {
            FooterViewHolder footerViewHolder = (FooterViewHolder) holder;
            switch (loadState) {
                case isLoading:
                    footerViewHolder.isLoadingLinear.setVisibility(View.VISIBLE);
                    footerViewHolder.isLoadingEndLinear.setVisibility(View.GONE);
                    break;
                case isLoadindComplete:
                    footerViewHolder.isLoadingLinear.setVisibility(View.GONE);
                    footerViewHolder.isLoadingEndLinear.setVisibility(View.GONE);
                    break;
                case isLoadindEnd:
                    footerViewHolder.isLoadingLinear.setVisibility(View.GONE);
                    footerViewHolder.isLoadingEndLinear.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;

            }
        } else if (normal_view == getItemViewType(position)) {
            adapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 如果当前是footer的位置，那么该item占据2个单元格，正常情况下占据1个单元格
                    return getItemViewType(position) == footer_view ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout isLoadingLinear;
        private LinearLayout isLoadingEndLinear;

        FooterViewHolder(View view) {
            super(view);
            isLoadingLinear = view.findViewById(R.id.isLoading);
            isLoadingEndLinear = view.findViewById(R.id.isEnd);
        }
    }

    public void setLoadingState(int loadingState) {
        this.loadState = loadingState;
        notifyDataSetChanged();
    }

    public abstract static class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {
        boolean isSlidingUp = false;

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int itemCount = layoutManager.getItemCount();//该语句获得的就是该recycleyview中的getItemCount()方法返回的数量
            System.out.println("itemCount:" + itemCount);
            int lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            System.out.println("lastVisiblePosition:" + lastVisiblePosition);
            // 判断是否滑动到了最后一个item，并且是向上滑动
            if(layoutManager instanceof  LinearLayoutManager){
                if ((lastVisiblePosition == itemCount-2||lastVisiblePosition == itemCount-1) && isSlidingUp) {
                    //加载更多
                    loadMore();
                }
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) {
                isSlidingUp = true;
            }
        }

        public abstract void loadMore();
    }
}
