package pl.tcps.tcps.layouts;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.tcps.tcps.R;
import pl.tcps.tcps.pojo.PetrolStationToDelete;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<PetrolStationToDelete> petrolStationList;
    private RecyclerView recyclerView;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView description;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.station_name);
            description = view.findViewById(R.id.station_description);
        }
    }

    public MyAdapter(List<PetrolStationToDelete> petrolStationList, RecyclerView recyclerView) {
        this.petrolStationList = petrolStationList;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PetrolStationToDelete petrolStation = petrolStationList.get(position);
        holder.name.setText(petrolStation.getName());
        holder.description.setText(petrolStation.getDescription());
    }

    @Override
    public int getItemCount() {
        return petrolStationList.size();
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_layout, parent,false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer positionToDelete = recyclerView.getChildAdapterPosition(v);
                petrolStationList.remove(positionToDelete);
                notifyItemRemoved(positionToDelete);
            }
        });

        return new MyViewHolder(view);
    }
}
