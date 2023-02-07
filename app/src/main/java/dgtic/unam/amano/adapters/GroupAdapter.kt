package dgtic.unam.amano.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dgtic.unam.amano.databinding.ItemGroupBinding
import dgtic.unam.amano.databinding.ItemGroupHeaderBinding
import dgtic.unam.amano.firebase.AuthManager
import dgtic.unam.amano.firebase.DatabaseManager
import dgtic.unam.amano.firebase.StorageManager
import dgtic.unam.amano.model.Group
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val HEADER_VIEW_TYPE = 1

class GroupAdapter(private val groupItemList: MutableList<Group>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    inner class GroupViewHolder(binding: ItemGroupBinding): RecyclerView.ViewHolder(binding.root) {
        private val itemGroupName = binding.groupNameTextView
        private val itemGroupDate = binding.dateTextView
        private val itemGroupDebt = binding.debtTextView


        fun bind(groupItem: Group) {
            itemGroupName.text = groupItem.name
            itemGroupDebt.text = groupItem.debt.toString()

            // set local date
            val date = groupItem.lastUpdated.toDate()
            val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val formatter = DateTimeFormatter.ofPattern("dd MMMM")
            val formattedDate = localDate.format(formatter)
            itemGroupDate.text = formattedDate
        }
    }

    inner class HeaderViewHolder(binding: ItemGroupHeaderBinding): RecyclerView.ViewHolder(binding.root) {
        private val userName = binding.nameTextView
        private val userDebt = binding.debtTextView
        private val userImage = binding.profileImageView

        fun bind(userName: String, userDebt: Double, imageUri: Uri?) {
            this.userName.text = userName

            this.userDebt.text = userDebt.toString()

            if (imageUri != null) {
                StorageManager.downloadImage(
                    imageUri
                ) { bitmap ->
                    userImage.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0) {
            return HEADER_VIEW_TYPE
        }

        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == HEADER_VIEW_TYPE) {
            val binding = ItemGroupHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return HeaderViewHolder(binding)
        }

        val binding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is GroupViewHolder) {
            val groupItem = groupItemList[position - 1]
            holder.bind(groupItem)
        } else if (holder is HeaderViewHolder) {
            holder.bind(
                AuthManager.userName!!,
                DatabaseManager.totalDebt,
                AuthManager.userPhotoPath
            )
        }
    }

    override fun getItemCount(): Int {
        return groupItemList.size + 1
    }

}