package ir.code4life.willy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ir.code4life.willy.R;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.models.Media;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.util.Size;

public class BoardRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final Context context;
    private final List<Board> list;

    public BoardRecyclerAdapter(Context context){
        this.context = context;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.board_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Set(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Board> boards){
        list.clear();
        list.addAll(boards);
        this.notifyDataSetChanged();
    }

}

class ViewHolder extends RecyclerView.ViewHolder {

    ImageView preview_1, preview_2,preview_3;
    TextView title, pins;
    AppDatabase database;
    List<Media> previews;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.board_name);
        pins = itemView.findViewById(R.id.pins_count);
        preview_1 = itemView.findViewById(R.id.image_preview_1);
        preview_2 = itemView.findViewById(R.id.image_preview_2);
        preview_3 = itemView.findViewById(R.id.image_preview_3);
        database = AppDatabase.getInstance(itemView.getContext());
    }

    public void Set(Board board){
        previews = database.mediaDao().getPreviews(Size._150x150.ordinal(), board.id);
        title.setText(board.name);
        try{
            Picasso.get().load(previews.get(0).url).into(preview_1);
            Picasso.get().load(previews.get(1).url).into(preview_2);
            Picasso.get().load(previews.get(2).url).into(preview_3);
        }catch (Exception ignore){}
    }
}
