package dgtic.unam.amano.firebase

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import dgtic.unam.amano.model.Group

object DatabaseManager {

    var groups = mutableListOf<Group>()

    // local methods
    val totalDebt: Double
        get() {
            var debt = 0.0
            groups.map {
                debt += it.debt
            }
            return debt
        }

    val groupNames: List<String>
        get() {
            val names: MutableList<String> = arrayListOf()
            groups.map {
                names.add(it.name)
            }
            return names
        }

    fun getParticipantId(groupID: String, name: String) : String? {
        groups.map { group ->
            if (group.id == groupID) {
                group.participants.map { par ->
                    if (par.name == name) {
                        return par.uuid
                    }
                }
            }
        }
        return null
    }

    // TODO create group

    // TODO get all groups manually
    fun getAllGroups(userid: String, completion: ((groups: List<Group>?) -> Unit)?) {
        val db = Firebase.firestore
        db.collection("groups")
            .whereEqualTo("ownerID", userid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val g = mutableListOf<Group>()
                for (document in querySnapshot.documents) {
                    val group = document.toObject<Group>()
                    if (group != null) {
                        g.add(group)
                    }
                    Log.d("DatabaseManager", group.toString())
                }
                g.sortedWith(compareBy {
                    it.lastUpdated.seconds
                })
                groups = g
                completion?.invoke(groups)
            }
            .addOnFailureListener {
                completion?.invoke(null)
            }
            .addOnCanceledListener {
                completion?.invoke(null)
            }
    }

    fun addExpense(groupID: String,
                   participantId: String,
                   amount: Double,
                   completion: ((done: Boolean) -> Unit)? = null) {

        // update participants locally

        val selectedGroup = groups.first { group ->
            group.id == groupID
        }

        val participantIndex = selectedGroup.participants.indexOfFirst { participant ->
            participant.uuid == participantId
        }

        selectedGroup.debt += amount
        selectedGroup.participants[participantIndex].debt += amount

        selectedGroup.lastUpdated = Timestamp.now()

        // update firebase
        val db = Firebase.firestore
        val docRef = db.collection("groups").document(groupID)
        docRef.set(selectedGroup)
            .addOnSuccessListener {
                Log.d("DatabaseManager", "Gasto agregado con exito!")
                completion?.invoke(true)
            }
            .addOnFailureListener {
                Log.d("DatabaseManager", "Gasto agregado sin exito")
                completion?.invoke(false)
            }
            .addOnCanceledListener {
                Log.d("DatabaseManager", "Gasto agregado sin exito")
                completion?.invoke(false)
            }
    }
}