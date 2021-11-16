package ir.code4life.willy.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import ir.code4life.willy.R;
import ir.code4life.willy.activities.fragments.PinPagerFragment;
import ir.code4life.willy.activities.fragments.PinsFragment;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.models.PinWithMedia;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.Size;

public class PinRecyclerAdapter extends RecyclerView.Adapter<PinRecyclerAdapter.ViewHolder> {
    private final FragmentActivity context;
    public final List<Pin> list;
    public Integer page =0;

    public PinRecyclerAdapter(FragmentActivity context) {

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
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Pin> pins) {
        list.addAll(pins);
        this.notifyItemRangeChanged(page,pins.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        CheckBox status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.pin_status);
            image = itemView.findViewById(R.id.pin_image);
        }

        public void bind(Integer position) {
            try {
                Pin item = list.get(position);
                Picasso.get().load(item.getImage_url(Size._600x)).into(image);
                String text = (item.downloaded)?"Downloaded":"Not Downloaded";
                status.setChecked(item.downloaded);
                status.setText(text);

                image.setOnClickListener(view -> {
                    PinPagerFragment fragment = PinPagerFragment.newInstance();
                    fragment.setList(list,position);
                    FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment,fragment);
                    transaction.addToBackStack("board");
                    transaction.commit();
                });
            } catch (Exception ignore) { }
        }
    }
}