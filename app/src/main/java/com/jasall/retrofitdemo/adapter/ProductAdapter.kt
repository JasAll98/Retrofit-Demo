package com.jasall.retrofitdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jasall.retrofitdemo.R
import com.jasall.retrofitdemo.databinding.ListItemBinding
import com.jasall.retrofitdemo.retrofit.Product
import com.squareup.picasso.Picasso

class ProductAdapter : ListAdapter<Product, ProductAdapter.Holder>(Compar()) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ListItemBinding.bind(view)

        fun bind(product: Product) = with(binding) {
            titleTV.text = product.title
            descriptionTV.text = product.description
            Picasso.get().load(product.thumbnail).into(listIV)
        }
    }

    class Compar : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}