package digitalcard.digitalcard.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import digitalcard.digitalcard.Model.Location;
import digitalcard.digitalcard.R;

public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.ViewHolder> {
    private Context context;
    private List<Location> locationList;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, address;
        LinearLayout panel;
        Double latitude, longitude;
        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.location_name);
            address = itemView.findViewById(R.id.location_address);
            panel = itemView.findViewById(R.id.panel);
        }
    }

    public LocationListAdapter(Context context, List<Location> locationList) {
        this.context = context;
        this.locationList = locationList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview_location, parent, false);
        return new LocationListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Location location = locationList.get(position);
        holder.name.setText(location.name);
        holder.address.setText(location.address);
        holder.panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + location.getLatitude() + "," + location.getLongitude()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }
}
