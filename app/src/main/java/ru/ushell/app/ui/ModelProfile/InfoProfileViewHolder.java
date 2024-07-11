package ru.ushell.app.ui.ModelProfile;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import ru.ushell.app.R;

public class InfoProfileViewHolder extends RecyclerView.ViewHolder {

    ImageView recImage;
    TextView recTitle, detailTitle, detailDes;
    CardView recCard;

    public InfoProfileViewHolder(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recTitle = itemView.findViewById(R.id.recTitle);
        detailTitle = itemView.findViewById(R.id.detailTitle);
        detailDes = itemView.findViewById(R.id.detailDes);
        recCard = itemView.findViewById(R.id.recCard);
    }
}
