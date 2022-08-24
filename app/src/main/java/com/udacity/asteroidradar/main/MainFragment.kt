package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.main.recycler_view.AsteroidAdapter
import java.util.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val factory=Factory(requireActivity().application)
        ViewModelProvider(this,factory).get(MainViewModel::class.java)
    }
    private var list:MutableList<Asteroid> = mutableListOf()
    private var adapter:AsteroidAdapter?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        adapter=AsteroidAdapter(object :AsteroidAdapter.OnAsteroidClickListener{
            override fun onItemClick(asteroid: Asteroid) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
            }

        })
        initViews(adapter!!,binding)
        return binding.root
    }

    private fun subscribeToLiveData(adapter: AsteroidAdapter, binding: FragmentMainBinding) {
        viewModel.asteroidList.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
        viewModel.nasaImage.observe(viewLifecycleOwner){
                Picasso.with(requireContext())
                .load(it.url)
                .into(binding.activityMainImageOfTheDay)
            binding.activityMainImageOfTheDay.contentDescription=it.title
        }
    }

    private fun initViews(adapter: AsteroidAdapter, binding: FragmentMainBinding) {
        adapter.submitList(viewModel.asteroidList.value)
        binding.asteroidRecycler.adapter= adapter
        subscribeToLiveData(adapter,binding)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        list= mutableListOf()
        if (item.itemId==R.id.show_all_menu){
            //all data
            list= viewModel.asteroidList.value?.toMutableList()!!
        }else if(item.itemId==R.id.show_rent_menu){
            //today
            viewModel.asteroidList.value?.forEach{
                if (it.closeApproachDate==viewModel.startDate()){
                    list.add(it)
                }
            }
        }else if (item.itemId==R.id.show_buy_menu){
            //week
            val endIndex=viewModel.startDate().lastIndex
            val startIndex=endIndex-1
            val startDate=viewModel.startDate().substring(startIndex,endIndex).toInt()
            viewModel.asteroidList.value?.forEach{
                val date=it.closeApproachDate.substring(startIndex,endIndex).toInt()
                if (date>=startDate){
                    list.add(it)
                }
            }
        }
        adapter!!.submitList(list)
        return true
    }
}
