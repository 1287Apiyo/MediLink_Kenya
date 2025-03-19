data class Appointment(
    val id: String? = null,
    val doctorName: String = "",
    val appointmentDate: String = "",
    val appointmentTime: String = "",
    val notes: String = "",
    val timestamp: Long = 0L,
    val status: String = ""  // "upcoming" or "past"
)
