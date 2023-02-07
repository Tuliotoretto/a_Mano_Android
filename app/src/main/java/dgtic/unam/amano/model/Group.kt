package dgtic.unam.amano.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import java.util.*

data class Group (
    @DocumentId var id: String? = null,
    val ownerID: String = "",
    var name: String = "",
    var debt: Double = 0.0,
    var participants: List<Participant> = emptyList(),
    var lastUpdated: Timestamp = Timestamp(Date())
    )

data class Participant (
    val uuid: String = "",
    var name: String = "",
    var debt: Double = 0.0
    )