package ir.code4life.willy.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.code4life.willy.R;
import ir.code4life.willy.database.AppDatabase;
import ir.code4life.willy.database.dao.DownloadDao;
import ir.code4life.willy.database.models.Download;
import ir.code4life.willy.http.models.Board;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.FileSystem;
import ir.code4life.willy.util.G;
import ir.code4life.willy.util.Size;

public class PinPagerAdapter extends RecyclerView.Adapter<PinPagerAdapter.ViewHolder> {

    private final FragmentActivity context;
    private final List<Pin> pins;
    private final DownloadDao downloadDao;
    private final Board board;

    public PinPagerAdapter(FragmentActivity context, List<Pin> pins,Board board){
        this.pins = pins;
        this.context = context;
        AppDatabase database = AppDatabase.getInstance(null);
        downloadDao = database.downloadDao();
        this.board = board;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pager_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(pins.get(position));
    }

    @Override
    public int getItemCount() {
        return pins.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        CheckBox status;
        ImageButton menu_view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.pin_pager_image);
            status = itemView.findViewById(R.id.pin_pager_status);
            menu_view = itemView.findViewById(R.id.pager_context_menu);
        }

        public void bind(Pin pin){
            try {
                Picasso.get().load(pin.getImage_url(Size._1200x)).into(image);
                boolean downloaded = pin.local_path != null;
                String text = (downloaded)?"Downloaded":"Not Downloaded";
                status.setChecked(downloaded);
                status.setText(text);
                setPopupMenu(pin);
            }catch (Exception ignore){ }
        }

        private void setPopupMenu(Pin pin) {
            menu_view.setOnClickListener(view -> {
                PopupMenu menu = new PopupMenu(context,menu_view);
                menu.inflate(R.menu.context_menu);
                menu.show();
                menu.setOnMenuItemClickListener(menuItem -> {
                    if(menuItem.getItemId() == R.id.context_download){
                        G.download(context,pin,board.name,downloadDao);
                    }else if(menuItem.getItemId() == R.id.context_setwallpaper){
                        G.setWallpaper(context,pin,board.name,downloadDao);
                    }
                    return true;
                });
            });
        }
    }
}
