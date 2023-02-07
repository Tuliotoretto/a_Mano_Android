package dgtic.unam.amano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import dgtic.unam.amano.databinding.FragmentAddBinding
import dgtic.unam.amano.firebase.AuthManager
import dgtic.unam.amano.firebase.DatabaseManager

class AddFragment : Fragment() {

    enum class WhoPaid {
        Me, Other
    }

    private var _binding: FragmentAddBinding? = null

    private val binding get() = _binding!!

    private var selectedGroupName: String? = null
    private var selectedGroupId: String? = null
    private var selectedParticipant: String? = null

    private var whoPaid: WhoPaid = WhoPaid.Me

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)

        setUpGroupSpinner()
        setUpToggleButtons()
        setUpParticipantsSpinner()
        setUpDoneButton()

        return binding.root
    }

    private fun setUpDoneButton() {
        binding.doneButton.setOnClickListener {
            binding.doneButton.isEnabled = false
            if (selectedGroupName == null || selectedGroupId == null || selectedParticipant == null || binding.editTextAmount.text.isEmpty()) {
                binding.doneButton.isEnabled = true
                return@setOnClickListener
            }

            val participantId = DatabaseManager.getParticipantId(selectedGroupId!!,selectedParticipant!!)

            var amount = binding.editTextAmount.text.toString().toDouble()
            if (whoPaid == WhoPaid.Other) {
                amount *= -1
            }

            DatabaseManager.addExpense(selectedGroupId!!,participantId!!, amount) {
                binding.doneButton.isEnabled = true
                if (it) {
                    clearData()
                    Toast.makeText(requireContext(),"gasto agregado!",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearData() {
        binding.editTextAmount.text.clear()
    }

    private fun setUpParticipantsSpinner() {
        if (selectedGroupName == null) {
            binding.participantSpinner.isEnabled = false
            return
        }
        binding.participantSpinner.isEnabled = true

        val selectedGroup = DatabaseManager.groups.first {
            it.name == selectedGroupName
        }

        val participants = selectedGroup.participants.map { it.name }

        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, participants)
        binding.participantSpinner.adapter = arrayAdapter

        binding.participantSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedParticipant = participants[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedParticipant = null
            }
        }
    }

    private fun setUpToggleButtons() {
        binding.toggleButtonGroup.check(R.id.button_i_paid)
        binding.toggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            when (checkedId) {
                R.id.button_i_paid -> whoPaid = WhoPaid.Me
                R.id.button_other_paid -> whoPaid = WhoPaid.Other
            }
        }
    }

    private fun setUpGroupSpinner() {
         val groupNames = DatabaseManager.groups.map {
             it.name
         }
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, groupNames)
        binding.groupSpinner.adapter = arrayAdapter

        binding.groupSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedGroupName = groupNames[p2]
                selectedGroupId = DatabaseManager.groups[p2].id
                setUpParticipantsSpinner()
                //Toast.makeText(requireContext(), selectedGroup!!, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                selectedGroupName = null
                selectedGroupId = null
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}