data class ChatMessage(
    val text: String = "",
    val sender: String = "",  // This is the UID of the sender (user or doctor)
    val timestamp: Long = 0L
)
