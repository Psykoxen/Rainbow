package fr.rainbow.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.rainbow.R
import fr.rainbow.dataclasses.Favorite
import fr.rainbow.functions.Functions
import fr.rainbow.functions.Functions.findCurrentSlotHourly
import fr.rainbow.functions.Functions.updatingBackgroundShape
import fr.rainbow.functions.Functions.updatingTempValue
import fr.rainbow.functions.Functions.updatingWeatherIc
import kotlinx.android.synthetic.main.item_favorite.view.*
import kotlinx.android.synthetic.main.item_favorite_big.view.*

class FavoriteAdapter(private val favorites : ArrayList<Favorite>, private val context: Context,private var onItemClicked: ((favorite: Favorite) -> Unit))
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType==1){
            (ViewHolderBig(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_favorite_big, parent, false)
            ))
        }else{
            (ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_favorite, parent, false)
            ))
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val favoriteItem : Favorite = favorites[position]
        if (getItemViewType(position)==1){
            (holder as ViewHolderBig).bind(favoriteItem)
            holder.itemView.ic_less.setOnClickListener {
                favoriteItem.isBig = false
                Functions.writeFile(context,favorites)
                notifyDataSetChanged()
            }
        }else{
            (holder as ViewHolder).bind(favoriteItem)

            holder.itemView.ic_more.setOnClickListener{
                favoriteItem.isBig = true
                Functions.writeFile(context,favorites)
                notifyDataSetChanged()
            }
        }
        holder.itemView.setOnClickListener {
            onItemClicked(favoriteItem)
        }
        holder.itemView.setOnLongClickListener {
            onItemClickListener?.let {
                it(favoriteItem)
            }
            true
        }



    }

    fun moveItemInRecyclerViewList(from: Int, to: Int) {

        val list = favorites

        val fromLocation = list[from]
        list.removeAt(from)
        if (to < from) {
            list.add(to + 1 , fromLocation)
        } else {
            list.add(to - 1, fromLocation)
        }

    }

    private var onItemClickListener: ((Favorite) -> Unit)? = null

    override fun getItemCount(): Int = favorites.size

    override fun getItemViewType(position: Int): Int {
        return if(favorites[position].isBig){
            1
        }else{
            0
        }
    }

    inner    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(favorite: Favorite) {
                if (favorite.isGPS) {
                    itemView.ic_location.visibility = View.VISIBLE
                } else {
                    itemView.ic_location.visibility = View.GONE
                }
                updatingTempValue(itemView.city_label2,favorite.name)
                if(favorite.weatherData!= null) {
                    if ( favorite.datetime!=null) {
                        updatingBackgroundShape(
                            itemView.fav_section,
                            favorite.weatherData!!.hourly.weathercode[findCurrentSlotHourly(favorite)],
                            favorite.weatherData!!.daily.sunset[2],
                            favorite.weatherData!!.daily.sunrise[2],
                            favorite.datetime!!.date_time
                        )
                        updatingWeatherIc(
                            itemView.weather_icon2,
                            favorite.weatherData!!.hourly.weathercode[findCurrentSlotHourly(favorite)],
                            favorite.weatherData!!.daily.sunset[2],
                            favorite.weatherData!!.daily.sunrise[2],
                            favorite.datetime!!.date_time
                        )
                        updatingTempValue(itemView.temperature_now_value2,favorite.weatherData!!.hourly.temperature_2m.get(
                            findCurrentSlotHourly(favorite!!)).toString())
                    }

                }
            }
        }

    inner    class ViewHolderBig(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(favorite: Favorite) {
                if (favorite.isGPS) {
                    itemView.ic_location_big.visibility = View.VISIBLE
                } else {
                    itemView.ic_location_big.visibility = View.GONE
                }
                updatingTempValue(itemView.city_label,favorite.name)
                if(favorite.weatherData!=null){
                    if ( favorite.datetime!=null) {
                        updatingBackgroundShape(
                            itemView.main_section,
                            favorite.weatherData!!.hourly.weathercode[findCurrentSlotHourly(favorite)],
                            favorite.weatherData!!.daily.sunset[2],
                            favorite.weatherData!!.daily.sunrise[2],
                            favorite.datetime!!.date_time
                        )
                        updatingWeatherIc(
                            itemView.weather_icon,
                            favorite.weatherData!!.hourly.weathercode[findCurrentSlotHourly(favorite)],
                            favorite.weatherData!!.daily.sunset[2],
                            favorite.weatherData!!.daily.sunrise[2],
                            favorite.datetime!!.date_time
                        )
                        itemView.rain_probability.progress = favorite.weatherData!!.hourly.precipitation_probability[findCurrentSlotHourly(favorite)]

                    }
                    updatingTempValue(itemView.temperature_now_value,favorite.weatherData!!.hourly.temperature_2m.get(
                        findCurrentSlotHourly(favorite!!)).toString())
                    updatingTempValue(itemView.tmp_min_value, favorite.weatherData!!.daily.temperature_2m_min[0])
                    updatingTempValue(itemView.tmp_max_value, favorite.weatherData!!.daily.temperature_2m_max[0])
                    }
            }
        }

}