package dgtic.unam.amano.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dgtic.unam.amano.databinding.ItemParticipantBinding

class NewGroupAdapter(private val groupParticipants: MutableList<String>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    inner class NewGroupViewHolder(binding: ItemParticipantBinding): RecyclerView.ViewHolder(binding.root) {
        private val participantName = binding.participantNameTextView

        fun bind(participant: String) {
            participantName.text = participant
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewGroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewGroupViewHolder) {
            holder.bind(groupParticipants[position])
        }
    }

    override fun getItemCount(): Int {
        return groupParticipants.size
    }
}