package com.udacity.asteroidradar.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapters.ARClickListener
import com.udacity.asteroidradar.adapters.ARListAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.domain.Asteroid

class MainFragment : Fragment() {
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application))[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        val adapter = ARListAdapter(ARClickListener { asteroidObj ->

        })
        binding.asteroidRecycler.adapter = adapter
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.asteroidList.observe(viewLifecycleOwner,object : Observer<List<Asteroid>>{
//            override fun onChanged(t: List<Asteroid>?) {
//
//            }
//
//        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
