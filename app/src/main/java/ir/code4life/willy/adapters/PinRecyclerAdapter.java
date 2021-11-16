package ir.code4life.willy.adapters;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import ir.code4life.willy.R;
import ir.code4life.willy.activities.fragments.PinPagerFragment;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.Size;

public class PinRecyclerAdapter extends RecyclerView.Adapter<PinRecyclerAdapter.ViewHolder> {
    private final FragmentActivity context;
    public final List<Pin> list;
    public Integer page = 0;
    private Board board;

    public PinRecyclerAdapter(FragmentActivity context) {

        this.context = context;
        this.list = new ArrayList<>();

    }

    public void setBoard(Board board){
        this.board = board;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pin_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<Pin> pins) {
        list.addAll(pins);
        this.notifyItemRangeChanged(page * 20,pins.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        CheckBox status;
        Integer position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.pin_status);
            image = itemView.findViewById(R.id.pin_image);
        }

        public void bind(Pin item,Integer position) {
            try {
                this.position = position;
                Picasso.get().load(item.getImage_url(Size._600x)).into(image);
                boolean downloaded = item.local_path != null;
                String text = (downloaded)?"Downloaded":"Not Downloaded";
                status.setChecked(downloaded);
                status.setText(text);

                image.setOnClickListener(view -> {
                    PinPagerFragment fragment = PinPagerFragment.newInstance();
                    fragment.setList(list,position);
                    fragment.setBoard(board);
                    FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment,fragment);
                    transaction.addToBackStack("board");
                    transaction.commit();
                });

                image.setOnLongClickListener(view -> {
                    PopupMenu menu = new PopupMenu(context,view, Gravity.TOP|Gravity.LEFT);
                    menu.inflate(R.menu.context_menu);
                    menu.show();
                    menu.setOnMenuItemClickListener(menuItem -> {
                        Toast.makeText(context, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        if(menuItem.getItemId() == R.id.context_download){
                            // TODO
                        }else if(menuItem.getItemId() == R.id.context_setwallpaper){
                            // TODO
                        }
                        return true;
                    });
                    return true;
                });

            } catch (Exception ignore) { }
        }
    }
}