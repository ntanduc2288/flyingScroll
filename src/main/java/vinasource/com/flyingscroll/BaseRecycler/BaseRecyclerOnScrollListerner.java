package vinasource.com.flyingscroll.BaseRecycler;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;


/**
 * Created by user on 12/15/15.
 */
public final class BaseRecyclerOnScrollListerner<T extends BaseRecyclerAdapter> extends RecyclerView.OnScrollListener {

    T baseRecyclerViewAdapter;
    boolean isTouching;
    RecyclerView.OnScrollListener onScrollListener;

    public interface IScrollToBottomListener {
        void onScrollToBottom();
    }

    public IScrollToBottomListener scrollToBottomListener;

    public BaseRecyclerOnScrollListerner(T baseRecyclerViewAdapter, RecyclerView.OnScrollListener onScrollListener, IScrollToBottomListener scrollToBottomListener){
        this.baseRecyclerViewAdapter = baseRecyclerViewAdapter;
        this.scrollToBottomListener = scrollToBottomListener;
        this.onScrollListener = onScrollListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        checkScrollToBottom(recyclerView);
        if(onScrollListener != null)
            onScrollListener.onScrolled(recyclerView, dx, dy);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (RecyclerView.SCROLL_STATE_SETTLING == newState) {
            baseRecyclerViewAdapter.setFlying(true);
            isTouching = true;
        } else {
            baseRecyclerViewAdapter.setFlying(false);

            if (isTouching) {
                isTouching = false;
                loadDataOnCurrentView(recyclerView);
            }

        }

        if(onScrollListener != null)
            onScrollListener.onScrollStateChanged(recyclerView, newState);
    }

    private void loadDataOnCurrentView(RecyclerView recyclerView){
        int childCount = recyclerView.getLayoutManager().getChildCount();
        int firstItem = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int index = 0;
        RecyclerView.ViewHolder viewHolder;
        for (int i = 0; i < childCount; i++) {
            index = firstItem + i;

            View childView = recyclerView.getLayoutManager().getChildAt(i);
            if(childView != null){
                viewHolder = recyclerView.getChildViewHolder(childView);
                baseRecyclerViewAdapter.loadData(viewHolder, index);
            }
        }
    }

    private void checkScrollToBottom(RecyclerView recyclerView){

        int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
        int totalItemCount = recyclerView.getLayoutManager().getItemCount();
        int pastVisiblesItems = 0;

        if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
            pastVisiblesItems = ((GridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        }else if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        }else if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager){ //Have not test it yet :D
            pastVisiblesItems = ((StaggeredGridLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPositions(null)[0];
        }

        boolean shouldNotifyScrollToBottom = scrollToBottomListener != null &&
                ((visibleItemCount + pastVisiblesItems) >= totalItemCount);

        if(shouldNotifyScrollToBottom){
            scrollToBottomListener.onScrollToBottom();
        }
    }
}
