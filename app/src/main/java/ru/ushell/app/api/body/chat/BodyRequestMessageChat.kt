package ru.ushell.app.api.body.chat

class BodyRequestMessageChat {

    private var senderId: String? = null
    private var recipientId: String? = null

    fun setSenderId(senderId: String?): BodyRequestMessageChat {
        this.senderId = senderId
        return this
    }

    fun setRecipientId(recipientId: String?): BodyRequestMessageChat {
        this.recipientId = recipientId
        return this
    }

}