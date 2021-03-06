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
package com.android.samples.donuttracker.ui.entry

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.samples.donuttracker.MainViewModel
import com.android.samples.donuttracker.databinding.DonutEntryDialogBinding
import com.android.samples.donuttracker.domain.Donut
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * This dialog allows the user to enter information about a donut, either creating a new
 * entry or updating an existing one.
 */
@AndroidEntryPoint
class DonutEntryDialog : BottomSheetDialogFragment() {

    companion object {
        private const val TAG = "DonutEntryDialog"
    }

    private val safeArgs: DonutEntryDialogArgs by navArgs()
    private val entryViewModel: DonutEntryViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DonutEntryDialogBinding.inflate(inflater, container, false)

        Log.d(TAG, "onCreateView: donutId = " + safeArgs.donutId)
        entryViewModel.loadData(safeArgs.donutId)

        entryViewModel.donut.observe(viewLifecycleOwner) { donut ->
            binding.donut = donut
        }

        binding.saveButton.setOnClickListener {
            mainViewModel.save(
                Donut(
                    id = safeArgs.donutId,
                    name = binding.name.text.toString(),
                    description =  binding.description.text.toString(),
                    rating = binding.ratingBar.rating
                )
            )
            dismiss()
        }

        // User clicked the Cancel button; just exit the dialog without saving the data
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }
}
