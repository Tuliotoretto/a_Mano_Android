package dgtic.unam.amano

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import dgtic.unam.amano.adapters.NewGroupAdapter
import dgtic.unam.amano.databinding.FragmentAddGroupBinding
import dgtic.unam.amano.firebase.AuthManager
import dgtic.unam.amano.firebase.DatabaseManager
import dgtic.unam.amano.model.Group
import dgtic.unam.amano.model.Participant
import java.util.UUID

class AddGroupFragment : Fragment() {

    private var _binding: FragmentAddGroupBinding? = null

    private val binding get() = _binding!!

    private var participants: MutableList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGroupBinding.inflate(inflater, container, false)

        participants.add(
            AuthManager.userName!!
        )


        setUpRecyclerView()
        setUpAddButton()

        return binding.root
    }

    private fun setUpAddButton() {
        binding.floatingActionButton.setOnClickListener {
            // create alert
            val builder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            builder.setTitle("Nuevo Participante")
            val dialogLayoutInflater = inflater.inflate(R.layout.alert_dialog_with_edit_text, null)
            val nameEditText = dialogLayoutInflater.findViewById<EditText>(R.id.name_edit_text)
            builder.setView(dialogLayoutInflater)
            builder.setPositiveButton("Ok") { dialogInterface, i ->
                if (nameEditText.text.isNotEmpty()) {
                    participants.add(nameEditText.text!!.toString())
                    binding.recyclerView.adapter?.notifyItemInserted(participants.size - 1)
                    Toast.makeText(requireContext(), "${nameEditText.text} agregado", Toast.LENGTH_SHORT).show()
                }
            }
            builder.show()
        }
    }

    private fun setUpRecyclerView() {
        val recycler = binding.recyclerView
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val adapter = NewGroupAdapter(participants)
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.create_group_done, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.create_group_action_bar_button -> {
                createNewGroup()
            }
            else -> {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createNewGroup() {
        if (binding.groupNameTextInput.text!!.isEmpty()) return

        val groupName = binding.groupNameTextInput.text!!.toString()

        // create group
        val newGroup = Group(
            id = null,
            ownerID = AuthManager.userId,
            name = groupName,
            debt = 0.0,
            participants = participants.map {
                Participant(
                    UUID.randomUUID().toString(),
                    it,
                    0.0,
                )
            },
            lastUpdated = Timestamp.now()
        )

        DatabaseManager.createGroup(newGroup) {
            if (it) {
                Toast.makeText(requireContext(), "Grupo agregado", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            } else {
                Toast.makeText(requireContext(), "Algo salió mal!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
