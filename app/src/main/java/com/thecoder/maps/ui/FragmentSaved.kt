package com.thecoder.maps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.thecoder.maps.R
import com.thecoder.maps.adapters.SavedAdapter
import com.thecoder.maps.database.Romeo
import com.thecoder.maps.databinding.FragmentSavedBinding
import com.thecoder.maps.models.MySavedItem
import com.thecoder.maps.models.SavedViewModel

import kotlin.collections.ArrayList

class FragmentSaved:Fragment() {




    lateinit var viewmodel:SavedViewModel
    lateinit var binding: FragmentSavedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding= FragmentSavedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO use UI

        viewmodel= ViewModelProvider(this)[SavedViewModel::class.java]

        val livedata=viewmodel.getlocations()




        val adapter=SavedAdapter(requireContext(), ArrayList() ,object :SavedAdapter.ClickListener{
            override fun onItemClicked(item: MySavedItem) {

                // let navigate to map with this location
                val controller=findNavController()

                // bundle to send item
                val bundle=Bundle()
                bundle.putSerializable("item",item)
                controller.navigate(R.id.to_maps,bundle)
            }

        })

        binding.recyclerview.adapter=adapter

        livedata.observe(viewLifecycleOwner){


            val list=it.reversed()
            adapter.updateData(list as ArrayList<MySavedItem>)
            if(it.isNotEmpty()){
                binding.tvNoItems.visibility=View.GONE
            }
        }


    }


}