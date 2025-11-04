package ru.ushell.app.data.features.messenger.mappers

data class Chat(
    val username:String,
    val name: String,
    val recipientId: String,
    val countNewMessage: Int,
)

// TODO: убрать и переделать
val ChatList: ArrayList<Chat> = mutableListOf<Chat>() as ArrayList<Chat>

fun getChatPopulation(): ArrayList<Chat>{
    val population = ArrayList<Chat>()
    val someChat = ArrayList<Chat>()

    for (chat in ChatList){
        if(chat.countNewMessage>0){
            population.add(chat)
        }
        else{
            someChat.add(chat)
        }

    }
    population.sortWith(Comparator.comparing { o -> o.countNewMessage })
    population.addAll(someChat)

    return population
}