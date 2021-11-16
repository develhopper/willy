package ir.code4life.willy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.code4life.willy.R;
import ir.code4life.willy.database.models.Download;

public class DownloadRecyclerAdapter extends RecyclerView.Adapter<DownloadRecyclerAdapter.ViewHolder> {

    public Integer page = 0;
    List<Download> list;
    Context context;
    public DownloadRecyclerAdapter(Context context,List<Download> list){
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.download_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Download> list){
        this.list.addAll(list);
        this.notifyItemRangeChanged(page*50,list.size());
    }

    public void setList(List<Download> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView link;
        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            link = itemView.findViewById(R.id.download_link);
            status = itemView.findViewById(R.id.download_status);
        }

        public void bind(Download download){
            link.setText(download.link);
            String text = (download.status)?"Downloaded":"Pending ...";
            status.setText(text);
        }
    }
}
