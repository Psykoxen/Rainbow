package fr.rainbow.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.R
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter(private val favorites : ArrayList<Favorite>, private val context: Context)
    : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return(ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_favorite, parent, false)
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteItem : Favorite = favorites[position]

        holder.bind(favoriteItem)

        /**holder.itemView.setOnClickListener {
            Log.d("DEBUGAD", "click")
        }**/
    }

    override fun getItemCount(): Int = favorites.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorites: Favorite) {
            itemView.city_label2.text = "BJR"
            itemView.temperature_now_value2.text = "SLT" //TODO
            itemView.weather_icon2.setImageResource(R.drawable.ic_weather_code_0) //TODO
        }
    }
}