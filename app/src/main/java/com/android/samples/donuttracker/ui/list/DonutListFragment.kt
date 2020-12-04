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
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.samples.donuttracker.R
import com.android.samples.donuttracker.databinding.DonutListFragmentBinding
import com.android.samples.donuttracker.domain.Donut
import com.android.samples.donuttracker.ui.entry.DonutEntryDialog
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment containing the RecyclerView which shows the current list of donuts being tracked.
 */
@AndroidEntryPoint
class DonutListFragment : Fragment() {

    companion object{
        private const val DONUT_ENTRY_REQUEST: Int = 1
    }

    private val viewModel: DonutListViewModel by viewModels()

    private val adapter = DonutListAdapter(
        onEdit = { donut ->
            findNavController().navigate(
                DonutListFragmentDirections.actionListToEntry(DONUT_ENTRY_REQUEST, donut)
            )
        },
        onDelete = { donut ->
            viewModel.delete(donut)
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DonutListFragmentBinding.inflate(inflater, container, false)

        binding.recyclerView.adapter = adapter

        viewModel.donuts.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener { fabView ->
            fabView.findNavController().navigate(
                DonutListFragmentDirections.actionListToEntry(DONUT_ENTRY_REQUEST, Donut(null))
            )
        }

        DonutEntryDialog.setResultListener(this, R.id.list_fragment){ result ->
            if(result?.requestCode == DONUT_ENTRY_REQUEST){
                viewModel.saveDonut(result.donut)
            }
        }

        return binding.root
    }
}
