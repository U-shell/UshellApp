package ru.ushell.app.data.features.messenger.room

import ru.ushell.app.data.features.messenger.MessengerLocalDataSource
import ru.ushell.app.data.features.messenger.room.dao.MessengerDao

class RoomMessengerDataSource(val messengerDao: MessengerDao ): MessengerLocalDataSource {
}