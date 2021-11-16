package ir.code4life.willy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import ir.code4life.willy.R;
import ir.code4life.willy.database.models.PinWithMedia;
import ir.code4life.willy.http.models.Pin;
import ir.code4life.willy.util.G;
import ir.code4life.willy.util.Size;

public class PinPagerAdapter extends RecyclerView.Adapter<PinPagerAdapter.ViewHolder> {

    private Context context;
    private List<Pin> pins;
    private LayoutInflater inflater;

    public PinPagerAdapter(Context context, List<Pin> pins){
        this.pins = pins;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        CheckBox status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.pin_pager_image);
            status = itemView.findViewById(R.id.pin_pager_status);
        }


        public void bind(Pin pin){
            try {
                Picasso.get().load(pin.getImage_url(Size._1200x)).into(image);
                String text = (pin.downloaded)?"Downloaded":"Not Downloaded";
                status.setChecked(pin.downloaded);
                status.setText(text);
            }catch (Exception ignore){ }
        }
    }
}
