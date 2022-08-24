package com.udacity.asteroidradar.main.recycler_view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class AsteroidAdapter(val clickListener: OnAsteroidClickListener):
    ListAdapter<Asteroid, AsteroidAdapter.ViewHolder>(AsteroidDiffCallBack()) {
    class ViewHolder(val itemBinding:ListItemAsteroidBinding):RecyclerView.ViewHolder(itemBinding.root){

        fun bind(item: Asteroid, clickListener: OnAsteroidClickListener){
            itemBinding.item=item
            itemBinding.onAsteroidClickListener= clickListener
            itemBinding.image.setImageResource(
                if(item.isPotentiallyHazardous) R.drawable.ic_status_potentially_hazardous
                else R.drawable.ic_status_normal
            )
            itemBinding.executePendingBindings()
        }
        companion object{
            fun from(parent: ViewGroup):ViewHolder {
                return ViewHolder(
                    DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_asteroid,
                    parent,
                    false
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item= getItem(position)
        holder.bind(item,clickListener)
    }

    interface OnAsteroidClickListener{
        fun onItemClick(asteroid: Asteroid)
    }
}

class AsteroidDiffCallBack:DiffUtil.ItemCallback<Asteroid>(){
    override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem.id==newItem.id
    }

    override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
        return oldItem==newItem
    }

}