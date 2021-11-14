package ir.code4life.willy.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import ir.code4life.willy.R;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.models.PinWithMedia;
import ir.code4life.willy.http.models.Pin;

public class PinRecyclerAdapter extends RecyclerView.Adapter<PinRecyclerAdapter.ViewHolder> {
    private final Context context;
    private final List<PinWithMedia> list;

    public PinRecyclerAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pin_item, parent, false);
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

    public void updateList(List<PinWithMedia> pins) {
        list.clear();
        list.addAll(pins);
        this.notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        CheckBox status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.pin_status);
            image = itemView.findViewById(R.id.pin_image);
        }

        public void Set(PinWithMedia pin) {
            try {
                Picasso.get().load(pin.media.url).into(image);
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }
    }
}