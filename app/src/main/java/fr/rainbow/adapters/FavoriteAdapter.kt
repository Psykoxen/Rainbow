package fr.rainbow.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fr.rainbow.DetailedActivity
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
        //val favoriteItem : Favorite = favorites[position]
        //al favoriteItem  differ.currentList[position]
        val favoriteItem = differ.currentList[position]
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
                if (favorite.isGPS) {
                    itemView.ic_location.setVisibility(View.VISIBLE)
                } else {
                    itemView.ic_location.setVisibility(View.GONE)
                }
                updatingTempValue(itemView.city_label2,favorite.name)
                if(favorite.weatherData!= null){
                    updatingBackgroundShape(itemView.fav_section,favorite.weatherData!!.hourly.weathercode[findCurrentSlotHourly(favorite.weatherData)],favorite.weatherData!!.daily.sunset[2])
                    updatingWeatherIc(itemView.weather_icon2,favorite.weatherData!!.hourly.weathercode[findCurrentSlotHourly(favorite.weatherData)],favorite.weatherData!!.daily.sunset[2])

                    updatingTempValue(itemView.temperature_now_value2,favorite.weatherData!!.hourly.temperature_2m.get(
                        findCurrentSlotHourly(favorite.weatherData!!)).toString())
                }
            }
        }

    inner    class ViewHolderBig(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(favorite: Favorite) {
                Log.d("FAVORITE",favorite.toString())
                if (favorite.isGPS) {
                    itemView.ic_location_big.setVisibility(View.VISIBLE)
                } else {
                    itemView.ic_location_big.setVisibility(View.GONE)
                }
                updatingTempValue(itemView.city_label,favorite.name)
                if(favorite.weatherData!=null){
                    updatingBackgroundShape(itemView.main_section,favorite.weatherData!!.hourly.weathercode[findCurrentSlotHourly(favorite.weatherData)],favorite.weatherData!!.daily.sunset[2])
                    updatingWeatherIc(itemView.weather_icon,favorite.weatherData!!.hourly.weathercode[findCurrentSlotHourly(favorite.weatherData)],favorite.weatherData!!.daily.sunset[2])
                    updatingTempValue(itemView.temperature_now_value,favorite.weatherData!!.hourly.temperature_2m.get(
                        findCurrentSlotHourly(favorite.weatherData!!)).toString())
                    updatingTempValue(itemView.tmp_min_value, favorite.weatherData!!.daily.temperature_2m_min[0])
                    updatingTempValue(itemView.tmp_max_value, favorite.weatherData!!.daily.temperature_2m_max[0])
                    itemView.rain_probability.progress = favorite.weatherData!!.hourly.precipitation_probability[findCurrentSlotHourly(favorite.weatherData)]
                }
            }
        }

}