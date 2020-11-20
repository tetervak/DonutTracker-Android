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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.samples.donuttracker.R
import com.android.samples.donuttracker.databinding.DonutListItemBinding
import com.android.samples.donuttracker.domain.Donut

/**
 * The adapter used by the RecyclerView to display the current list of donuts
 */
class DonutListAdapter(
    private var onEdit: (Donut) -> Unit,
    private var onDelete: (Donut) -> Unit) :
    ListAdapter<Donut, DonutListAdapter.ViewHolder>(DonutDiffCallback()) {

    inner class ViewHolder(
        private val binding: DonutListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(donut: Donut) {
            binding.donut = donut
            binding.thumbnail.setImageResource(R.drawable.donut_with_sprinkles)
            binding.deleteButton.setOnClickListener {
                onDelete(donut)
            }
            binding.root.setOnClickListener {
                onEdit(donut)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DonutListItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DonutDiffCallback : DiffUtil.ItemCallback<Donut>() {
        override fun areItemsTheSame(oldItem: Donut, newItem: Donut): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Donut, newItem: Donut): Boolean {
            return oldItem == newItem
        }
    }
}