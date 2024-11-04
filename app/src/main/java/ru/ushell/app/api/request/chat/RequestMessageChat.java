package ru.ushell.app.api.request.chat;

import static java.util.Collections.reverse;

import androidx.annotation.NonNull;

import java.net.HttpURLConnection;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ushell.app.api.API;
import ru.ushell.app.api.RetrofitService;
import ru.ushell.app.api.body.BodyRequestSingIn;
import ru.ushell.app.api.body.chat.BodyRequestMessageChat;
import ru.ushell.app.api.response.ResponseTimeTable;
import ru.ushell.app.api.response.chat.ResponseMessageChat;
import ru.ushell.app.models.modelChat.message.Message;
import ru.ushell.app.models.modelChat.message.MessageList;


public class RequestMessageChat {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
    public interface MessageChatCallback{
        void onMessageChatReceived(Integer infoMessageData);
    }

    // получение количество новых сообшений
    public static void getCountNewMessage(String senderId, String recipientId, MessageChatCallback messageChatCallback){

        BodyRequestMessageChat bodyRequestMessageChat = new BodyRequestMessageChat()
                .setSenderId(senderId)
                .setRecipientId(recipientId);

        RetrofitService retrofitService = new RetrofitService();
        API api = retrofitService.getRetrofit().create(API.class);

        Call<String> countNewMessage = api.getCountNewMessage(bodyRequestMessageChat);

        countNewMessage.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {
                    messageChatCallback.onMessageChatReceived(Integer.parseInt(response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {

            }
        });
    }

    // получение сообшений из чата
    public static void getMessageChat(String senderId, String recipientId){
        BodyRequestMessageChat bodyRequestMessageChat = new BodyRequestMessageChat()
                .setSenderId(senderId)
                .setRecipientId(recipientId);

        RetrofitService retrofitService = new RetrofitService();
        API api = retrofitService.getRetrofit().create(API.class);

        Call<List<ResponseMessageChat>> countNewMessage = api.getMessageChat(bodyRequestMessageChat);

        countNewMessage.enqueue(new Callback<List<ResponseMessageChat>>() {
            @Override
            public void onResponse(@NonNull Call<List<ResponseMessageChat>> call, @NonNull Response<List<ResponseMessageChat>> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {
                    //TODO для ускорения убрать модуль обработки данных
                    MessageList.setChannelName(recipientId);
                    List<Message> messages = new ArrayList<>();
                    List<ResponseMessageChat> responseMessageChatList = response.body();
                    for(ResponseMessageChat responseMessageChat: responseMessageChatList){
                        messages.add(
                            new Message(
                                senderId.equals(responseMessageChat.getSenderId()),
                                responseMessageChat.getMessage(),
                                OffsetDateTime.parse(responseMessageChat.getTimestamp(), formatter)
                            )
                        );
                    }
                    reverse(messages);

                    MessageList.setMessageList(messages);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ResponseMessageChat>> call, @NonNull Throwable t) {

            }
        });

    }

    //отправка о прочтении
    public static void getNoiseMessage(){}
}
