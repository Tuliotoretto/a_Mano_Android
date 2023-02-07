package dgtic.unam.amano

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dgtic.unam.amano.adapters.GroupAdapter
import dgtic.unam.amano.databinding.FragmentGroupsBinding
import dgtic.unam.amano.firebase.AuthManager
import dgtic.unam.amano.firebase.DatabaseManager
import dgtic.unam.amano.model.Group

class GroupsFragment : Fragment() {

    private var _binding: FragmentGroupsBinding? = null

    private val binding get() = _binding!!

    private var groups: MutableList<Group> = arrayListOf()

    private lateinit var groupAdapter: GroupAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        setUpRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // populate groups
        DatabaseManager.getAllGroups(AuthManager.userId) {
            if (it != null) {
                groups = it.toMutableList()
                groupAdapter = GroupAdapter(groups)
                binding.groupsRecyclerView.adapter = groupAdapter

                this.activity?.runOnUiThread {
                    groupAdapter.notifyItemRangeChanged(1, groups.count())
                    //Toast.makeText(requireContext(),"${it.count()}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_group_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_action_bar_button -> {
                Navigation.findNavController(requireView()).navigate(R.id.addGroupFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpRecyclerView() {
        val recycler = binding.groupsRecyclerView
        recycler.layoutManager = LinearLayoutManager(requireContext())

        groupAdapter = GroupAdapter(groups)

        binding.groupsRecyclerView.adapter = groupAdapter
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}