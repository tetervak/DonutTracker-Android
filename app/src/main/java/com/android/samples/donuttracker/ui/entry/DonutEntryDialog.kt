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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.samples.donuttracker.databinding.DonutEntryDialogBinding
import com.android.samples.donuttracker.domain.Donut
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable

/**
 * This dialog allows the user to enter information about a donut, either creating a new
 * entry or updating an existing one.
 */
@AndroidEntryPoint
class DonutEntryDialog : BottomSheetDialogFragment() {

    data class DonutEntryResult(
        val requestCode: Int,
        val donut: Donut,
        var processed: Boolean = false
    ) : Serializable

    companion object {

        private const val TAG = "DonutEntryDialog"
        const val DONUT_ENTRY_RESULT = "donut_entry_result"

        fun setResultListener(
            fragment: Fragment,
            fragmentId: Int,
            onResult: (DonutEntryResult) -> Unit
        ) {
            val navController = fragment.findNavController()
            val navBackStackEntry = navController.getBackStackEntry(fragmentId)
            val handle = navBackStackEntry.savedStateHandle
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME
                    && handle.contains(DONUT_ENTRY_RESULT)
                ) {
                    val result: DonutEntryResult? = handle.get(DONUT_ENTRY_RESULT);
                    if(result != null) {
                        if(!result.processed) {
                            onResult(result)
                            result.processed = true
                        }
                    }
                }
            }
            navBackStackEntry.lifecycle.addObserver(observer)
            fragment.viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            })
        }

    }

    private val safeArgs: DonutEntryDialogArgs by navArgs()

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        navController = findNavController()

        val binding = DonutEntryDialogBinding.inflate(inflater, container, false)

        val donut = safeArgs.donut
        Log.d(TAG, "onCreateView: donut = $donut")
        binding.name.setText(donut.name)
        binding.description.setText(donut.description)
        binding.ratingBar.rating = donut.rating.toFloat()

        // When the user clicks the Done button, use the data here to either update
        // an existing item or create a new one
        binding.doneButton.setOnClickListener {
            donut.name = binding.name.text.toString()
            donut.description = binding.description.text.toString()
            donut.rating = binding.ratingBar.rating.toInt()
            setDonutEntryResult(donut)
            dismiss()
        }

        // User clicked the Cancel button; just exit the dialog without saving the data
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    private fun setDonutEntryResult(donut: Donut) {
        val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
        savedStateHandle?.set(DONUT_ENTRY_RESULT, DonutEntryResult(safeArgs.requestCode, donut))
    }
}
