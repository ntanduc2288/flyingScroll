package vinasource.com.flyingscroll.BaseRecycler;

import android.support.v7.widget.RecyclerView;

/**
 * Created by user on 12/15/15.
 */
public abstract class BaseRecyclerAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T>{
    boolean isFlying;
    public void setFlying(boolean isFlying){
        this.isFlying = isFlying;
    }

    public boolean getFlying(){
        return isFlying;
    }
    public abstract void loadData(RecyclerView.ViewHolder viewHolder, int position);
}
