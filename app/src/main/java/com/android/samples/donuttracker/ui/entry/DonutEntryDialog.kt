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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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

    private val entryViewModel: DonutEntryViewModel by viewModels()

    private enum class EditingState {
        NEW_DONUT,
        EXISTING_DONUT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DonutEntryDialogBinding.inflate(inflater, container, false)

        var donut: Donut? = null
        val args: DonutEntryDialogArgs by navArgs()
        val editingState =
            if (args.itemId > 0) EditingState.EXISTING_DONUT
            else EditingState.NEW_DONUT

        // If we arrived here with an itemId of >= 0, then we are editing an existing item
        if (editingState == EditingState.EXISTING_DONUT) {
            // Request to edit an existing item, whose id was passed in as an argument.
            // Retrieve that item and populate the UI with its details
            entryViewModel.get(args.itemId).observe(viewLifecycleOwner) { donutItem ->
                binding.name.setText(donutItem.name)
                binding.description.setText(donutItem.description)
                binding.ratingBar.rating = donutItem.rating.toFloat()
                donut = donutItem
            }
        }

        // When the user clicks the Done button, use the data here to either update
        // an existing item or create a new one
        binding.doneButton.setOnClickListener {
            // Grab these now since the Fragment may go away before the setupNotification
            // lambda below is called
            val context = requireContext().applicationContext
            val navController = findNavController()

            entryViewModel.addData(
                donut?.id ?: 0,
                binding.name.text.toString(),
                binding.description.text.toString(),
                binding.ratingBar.rating.toInt()
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
