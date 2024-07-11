package ru.ushell.app.ui.ModelTimeTable;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.ushell.app.R;
public class MessageViewHolder extends RecyclerView.ViewHolder {
    public final TextView message;
    public final View parentView;

    public MessageViewHolder(@NonNull View itemView) {
        super(itemView);
        parentView = itemView.findViewById(R.id.cardView1);
        message = itemView.findViewById(R.id.messagetext);

    }
}
