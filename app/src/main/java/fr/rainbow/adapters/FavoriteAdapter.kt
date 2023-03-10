package fr.rainbow.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.R
import fr.rainbow.ui.detailed.DetailedActivity
import kotlinx.android.synthetic.main.item_favorite.view.*
import kotlinx.android.synthetic.main.item_favorite_big.view.*

class FavoriteAdapter(private val favorites : ArrayList<Favorite>, private val context: Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("test", viewType.toString())
        if(viewType==1){
            return(ViewHolderBig(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_favorite_big, parent, false)
            ))
        }else{
            return(ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_favorite, parent, false)
            ))
        }

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val favoriteItem : Favorite = favorites[position]
        if (getItemViewType(position)==1){
            (holder as ViewHolderBig).bind(favoriteItem)
        }else{
            (holder as ViewHolder).bind(favoriteItem)
        }



        holder.itemView.setOnClickListener {
            val detailedIntent = Intent(context, DetailedActivity::class.java)
            detailedIntent.putExtra("latitude", favoriteItem.latitude.toString())
            detailedIntent.putExtra("longitude", favoriteItem.longitude.toString())
            detailedIntent.putExtra("name",favoriteItem.name)
            startActivity(context,detailedIntent,null)
        }
    }

    override fun getItemCount(): Int = favorites.size

    override fun getItemViewType(position: Int): Int {
        return if(favorites.get(position).isBig){
            1
        }else{
            0
        }
    }

    inner    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(favorite: Favorite) {
                itemView.city_label2.text = favorite.name
                // le reste du remplissage est en asynchrone
            }
        }

    inner    class ViewHolderBig(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(favorite: Favorite) {
                itemView.city_label.text = favorite.name
                // le reste du remplissage est en asynchrone
            }
        }

}