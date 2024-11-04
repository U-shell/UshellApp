package ru.ushell.app.api.request.chat;

import static ru.ushell.app.models.modelChat.chat.Chat.ChatList;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.net.HttpURLConnection;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.ushell.app.api.API;
import ru.ushell.app.api.RetrofitService;
import ru.ushell.app.api.body.chat.BodyRequestUserDetails;
import ru.ushell.app.api.response.chat.ResponseAllUserChat;
import ru.ushell.app.api.response.chat.ResponseInfoUserChat;

import ru.ushell.app.models.User;
import ru.ushell.app.models.modelChat.chat.Chat;

public class RequestUserChat {

    public interface UserChatCallback{
        void onUserChatReceived(String infoUserData);
    }

    public static void getCurrentUser(String username, UserChatCallback userChatCallback){

        Gson gson = new Gson();
        String json = gson.toJson(new BodyRequestUserDetails().setUsername(username));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);

        RetrofitService retrofitService = new RetrofitService();

        API api =  retrofitService.getRetrofit().create(API.class);
        Call<ResponseInfoUserChat> infoUserChatResponseCall  = api.getInfoUserChat(requestBody);

        infoUserChatResponseCall.enqueue(new Callback<ResponseInfoUserChat>() {
            @Override
            public void onResponse(@NonNull Call<ResponseInfoUserChat> call, @NonNull Response<ResponseInfoUserChat> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {
                    ResponseInfoUserChat responseInfoUserChat = response.body();

                    userChatCallback.onUserChatReceived(responseInfoUserChat.getId());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseInfoUserChat> call, @NonNull Throwable t) {

            }
        });
    }

    public static void getAllUser(){
        RetrofitService retrofitService = new RetrofitService();

        API api =  retrofitService.getRetrofit().create(API.class);

        Call<ResponseAllUserChat> allUsersChatResponseCall = api.getAllUserSummaries(
                new BodyRequestUserDetails().setUsername(User.getUsername())
        );

        allUsersChatResponseCall.enqueue(new Callback<ResponseAllUserChat>() {
            @Override
            public void onResponse(@NonNull Call<ResponseAllUserChat> call, @NonNull Response<ResponseAllUserChat> response) {
                if (response.isSuccessful() && response.body() != null && response.code() == HttpURLConnection.HTTP_OK) {
                    ResponseAllUserChat responseInfoUserChat = response.body();
                    for(ResponseInfoUserChat i: responseInfoUserChat.getList()){

                        RequestMessageChat.getCountNewMessage(User.getKeyIdUserChat(), i.getId(),
                                infoMessageData -> {
                                    ChatList.add(new Chat(
                                            i.getUsername(),
                                            i.getName(),
                                            i.getId(),
                                            infoMessageData
                                    ));
                                }
                        );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseAllUserChat> call, @NonNull Throwable t) {

            }
        });
    }
}
