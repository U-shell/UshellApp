package ru.ushell.app.data.features.messanger.room

import ru.ushell.app.data.features.messanger.MessengerLocalDataSource
import ru.ushell.app.data.features.messanger.room.dao.MessengerDao

class RoomMessengerDataSource(val messengerDao: MessengerDao ): MessengerLocalDataSource {
}