package fr.rainbow.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.rainbow.R
import fr.rainbow.dataclasses.Favorite
import kotlinx.android.synthetic.main.favorite_item_setting.view.*


class FavoriteSettingAdatper (private val context: Context)
    : RecyclerView.Adapter<FavoriteSettingAdatper.ViewHolder>() {

    private val differCallBack  = object : DiffUtil.ItemCallback<Favorite>()
    {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return  oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return  oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return(ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.favorite_item_setting, parent, false)
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fav = differ.currentList[position]
        holder.bind(fav)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it(fav)
            }
        }
    }
    fun moveItemInRecyclerViewList(from: Int, to: Int) {

        val list = differ.currentList.toMutableList()
        val fromLocation = list[from]
        list.removeAt(from)
        if (to < from) {
            //+1 because it start from 0 on the upside. otherwise it will not change the locations accordingly
            list.add(to + 1 , fromLocation)
        } else {
            //-1 because it start from length + 1 on the down side. otherwise it will not change the locations accordingly
            list.add(to - 1, fromLocation)
        }
        differ.submitList(list)
    }

    private var onItemClickListener: ((Favorite) -> Unit)? = null


    override fun getItemCount(): Int = differ.currentList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(fav: Favorite) {
            itemView.favorite_name.text = fav.name
        }

    }







}
