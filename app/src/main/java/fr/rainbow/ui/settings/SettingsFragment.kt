package fr.rainbow.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.rainbow.MainActivity
import fr.rainbow.R
import fr.rainbow.adapters.FavoriteSettingAdatper
import fr.rainbow.databinding.FragmentSettingsBinding
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.functions.Functions
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    lateinit var favorites:ArrayList<Favorite>
    lateinit var favoritess:MutableList<Favorite>
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val itemTouchHelper by lazy {
        val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(UP or DOWN or START or END, 0) {

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                val adapter = recyclerView.adapter as FavoriteSettingAdatper
                val from = viewHolder.adapterPosition
                val to = target.adapterPosition
                adapter.moveItemInRecyclerViewList(from, to)
                adapter.notifyItemMoved(from, to)

                val favorite = favorites[from]
                favorites.removeAt(from)
                favorites.add(to, favorite)
                context?.let { Functions.writeFile(it,favorites) }

                Log.d("test","$favorites")
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)

                if (actionState == ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.5f
                }

            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1.0f
            }
        }
        ItemTouchHelper(simpleItemTouchCallback)

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val root: View = binding.root
        val radioGroup = root.findViewById<RadioGroup>(R.id.idRGgroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.idRBLight -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                R.id.idRBDark -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
        gpsAddButton.setOnClickListener {
            (activity as MainActivity).addGps()
        }

        favorites = (activity as MainActivity).favorites
        favoritess =  favorites.toMutableList()

        val recycler = favoriteSettingView

        val adapter = context?.let { FavoriteSettingAdatper(it) }
        if (adapter != null) {
            adapter.differ.submitList(favoritess)
        }
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this.context)
        itemTouchHelper.attachToRecyclerView(recycler)





    }
}