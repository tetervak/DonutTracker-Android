/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.samples.donuttracker.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.android.samples.donuttracker.MainViewModel
import com.android.samples.donuttracker.databinding.DonutListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment containing the RecyclerView which shows the current list of donuts being tracked.
 */
@AndroidEntryPoint
class DonutListFragment : Fragment() {

    private val listViewModel: DonutListViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private val adapter = DonutListAdapter(
        onEdit = { donut ->
            findNavController().navigate(
                DonutListFragmentDirections.actionListToEntry(donut.id)
            )
        },
        onDelete = { donut ->
            mainViewModel.delete(donut)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DonutListFragmentBinding.inflate(inflater, container, false)

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(divider)
        binding.recyclerView.adapter = adapter

        binding.fab.setOnClickListener { fabView ->
            fabView.findNavController().navigate(
                DonutListFragmentDirections.actionListToEntry(null)
            )
        }

        listViewModel.donuts.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        return binding.root
    }
}
