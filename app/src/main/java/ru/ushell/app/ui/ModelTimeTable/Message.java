package ru.ushell.app.ui.ModelTimeTable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.ushell.app.R;


/**
 * Вспомогательный файл , котороый отправляет сообшее об отсутсвии пары
 */


public class Message extends RecyclerView.Adapter<MessageViewHolder> {
    Context context;
    String Text;

    public Message(Context context, String Text) {
        this.context = context;
        this.Text = Text;
    }

    // дизайн панели
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View messageItems = LayoutInflater.from(context).inflate(R.layout.message, parent, false);

        return new MessageViewHolder(messageItems);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.message.setText(Text);
    }


    @Override
    public int getItemCount() {
        return 1;
    }

}
