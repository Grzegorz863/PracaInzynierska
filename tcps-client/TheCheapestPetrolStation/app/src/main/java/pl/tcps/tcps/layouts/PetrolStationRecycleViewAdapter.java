package pl.tcps.tcps.layouts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import pl.tcps.tcps.R;
import pl.tcps.tcps.pojo.responses.PetrolStationRecycleViewItem;

public class PetrolStationRecycleViewAdapter extends RecyclerView.Adapter<PetrolStationRecycleViewAdapter.PetrolStationItemViewHolder> {

    private List<PetrolStationRecycleViewItem> petrolStationList;
    private RecyclerView recyclerView;
    private Context context;

    public static class PetrolStationItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvStationName;
        public TextView tvPb95Price;
        public TextView tvPb98Price;
        public TextView tvOnPrice;
        public TextView tvLpgPrice;
        public TextView tvConsortiumName;
        public TextView tvDistance;
        public RatingBar rbRating;

        public PetrolStationItemViewHolder(View view) {
            super(view);
            tvStationName = view.findViewById(R.id.recycleview_station_name);
            tvPb95Price = view.findViewById(R.id.recycleview_pb95_price);
            tvPb98Price = view.findViewById(R.id.recycleview_pb98_price);
            tvOnPrice = view.findViewById(R.id.recycleview_on_price);
            tvLpgPrice = view.findViewById(R.id.recycleview_lpg_price);
            tvConsortiumName = view.findViewById(R.id.recycleview_consortium_name);
            tvDistance = view.findViewById(R.id.recycleview_distance);
            rbRating = view.findViewById(R.id.recycleview_rating_bar);
        }
    }

    public PetrolStationRecycleViewAdapter(List<PetrolStationRecycleViewItem> petrolStationList, RecyclerView recyclerView) {
        this.petrolStationList = petrolStationList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public PetrolStationItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_layout, parent, false);
        this.context = parent.getContext();
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Integer positionToDelete = recyclerView.getChildAdapterPosition(v);
//                petrolStationList.remove(positionToDelete);
//                notifyItemRemoved(positionToDelete);
//            }
//        });

        return new PetrolStationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetrolStationItemViewHolder holder, int position) {
        PetrolStationRecycleViewItem petrolStation = petrolStationList.get(position);

        holder.tvStationName.setText(petrolStation.getStationName());

        String pb95Price = String.format(Locale.ENGLISH,"%.2f", petrolStation.getPriceRecycleViewItem().getPb95Price());
        holder.tvPb95Price.setText(context.getString(R.string.recycleview_pb95_text_value_price_par, pb95Price));

        String pb98Price = String.format(Locale.ENGLISH,"%.2f", petrolStation.getPriceRecycleViewItem().getPb98Price());
        holder.tvPb98Price.setText(context.getString(R.string.recycleview_pb98_text_value_price_par, pb98Price));

        String onPrice = String.format(Locale.ENGLISH,"%.2f", petrolStation.getPriceRecycleViewItem().getOnPrice());
        holder.tvOnPrice.setText(context.getString(R.string.recycleview_on_text_value_price_par, onPrice));

        String lpgPrice = String.format(Locale.ENGLISH,"%.2f", petrolStation.getPriceRecycleViewItem().getLpgPrice());
        holder.tvLpgPrice.setText(context.getString(R.string.recycleview_lpg_text_value_price_par, lpgPrice));

        holder.tvConsortiumName.setText(petrolStation.getConsortiumName());
        
        Double distance = petrolStation.getDistance();
        String distanceTextToShow;
        String distanceText;
        if(distance < 1000) {
            distanceText = String.format(Locale.ENGLISH,"%.2f", distance);
            distanceTextToShow = context.getString(R.string.recycleview_distance, distanceText, "m");
        }else{
            distance = distance/1000;
            distanceText = String.format(Locale.ENGLISH,"%.2f", distance);
            distanceTextToShow = context.getString(R.string.recycleview_distance, distanceText, "km");
        }

        holder.tvDistance.setText(distanceTextToShow);
        holder.rbRating.setRating(petrolStation.getRating().floatValue());

    }

    @Override
    public int getItemCount() {
        return petrolStationList.size();
    }

}
