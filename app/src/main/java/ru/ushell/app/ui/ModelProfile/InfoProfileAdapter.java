package ru.ushell.app.ui.ModelProfile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.ushell.app.R;

public class InfoProfileAdapter extends RecyclerView.Adapter<InfoProfileViewHolder> {
    private final Context context;
    private final List<DataClass> dataClassList;

    public InfoProfileAdapter( Context context, List<DataClass> dataClassList){
        this.context = context;
        this.dataClassList = dataClassList;

    }

    @NonNull
    @Override
    public InfoProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_info_item, parent,false);

        return new InfoProfileViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull InfoProfileViewHolder holder, int position) {

        holder.recImage.setImageResource(dataClassList.get(position).getDataImage());
        holder.recTitle.setText(dataClassList.get(position).getDataTitle());
//        holder.detailDes.setText(dataClassList.get(position).getDataDesc());

        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onClic");
                Intent intent = new Intent(context, DetailProfile.class);
                intent.putExtra("Image",dataClassList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("Titel",dataClassList.get(holder.getAdapterPosition()).getDataTitle());
//                intent.putExtra("Desc", dataClassList.get(holder.getAdapterPosition()).getDataDesc());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataClassList.size();
    }
}
