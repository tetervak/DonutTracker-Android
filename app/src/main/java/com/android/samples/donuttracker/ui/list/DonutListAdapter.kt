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
import com.android.samples.donuttracker.databinding.DonutItemBinding
import com.android.samples.donuttracker.database.DonutEntity

/**
 * The adapter used by the RecyclerView to display the current list of donuts
 */
class DonutListAdapter(
    private var onEdit: (DonutEntity) -> Unit,
    private var onDelete: (DonutEntity) -> Unit) :
    ListAdapter<DonutEntity, DonutListAdapter.ViewHolder>(DonutDiffCallback()) {

    class ViewHolder(
        private val binding: DonutItemBinding,
        private var onEdit: (DonutEntity) -> Unit,
        private var onDelete: (DonutEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        private var donutId: Long = -1
        private var nameView = binding.name
        private var description = binding.description
        private var thumbnail = binding.thumbnail
        private var rating = binding.rating
        private var donut: DonutEntity? = null

        fun bind(donut: DonutEntity) {
            donutId = donut.id
            nameView.text = donut.name
            description.text = donut.description
            rating.text = donut.rating.toString()
            thumbnail.setImageResource(R.drawable.donut_with_sprinkles)
            this.donut = donut
            binding.deleteButton.setOnClickListener {
                onDelete(donut)
            }
            binding.root.setOnClickListener {
                onEdit(donut)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            DonutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onEdit,
            onDelete
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DonutDiffCallback : DiffUtil.ItemCallback<DonutEntity>() {
        override fun areItemsTheSame(oldItem: DonutEntity, newItem: DonutEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DonutEntity, newItem: DonutEntity): Boolean {
            return oldItem == newItem
        }
    }


}