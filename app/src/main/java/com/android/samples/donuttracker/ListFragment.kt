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
package com.android.samples.donuttracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.android.samples.donuttracker.databinding.DonutListBinding
import com.android.samples.donuttracker.database.DonutDatabase
import kotlinx.android.synthetic.main.donut_list.*

/**
 * Fragment containing the RecyclerView which shows the current list of donuts being tracked.
 */
class ListFragment : Fragment() {

    private lateinit var listViewModel: ListViewModel

    private val adapter = ListAdapter(
        onEdit = { donut ->
            findNavController().navigate(
                ListFragmentDirections.actionListToEntry(donut.id)
            )
        },
        onDelete = { donut ->
            NotificationManagerCompat.from(requireContext()).cancel(donut.id.toInt())
            listViewModel.delete(donut)
        }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = DonutListBinding.bind(view)
        val donutDao = DonutDatabase.getDatabase(requireContext()).donutDao()
        listViewModel = ViewModelProvider(this, ViewModelFactory(donutDao))
            .get(ListViewModel::class.java)

        listViewModel.donuts.observe(viewLifecycleOwner) { donuts ->
            adapter.submitList(donuts)
        }

        recyclerView.adapter = adapter

        binding.fab.setOnClickListener { fabView ->
            fabView.findNavController().navigate(
                ListFragmentDirections.actionListToEntry()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DonutListBinding.inflate(inflater, container, false).root
    }
}
