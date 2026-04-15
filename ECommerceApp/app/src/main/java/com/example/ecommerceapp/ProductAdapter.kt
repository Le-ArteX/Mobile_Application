package com.example.ecommerceapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val onAddToCartClick: (Product) -> Unit
) : ListAdapter<Product, RecyclerView.ViewHolder>(ProductDiffCallback()) {

    var isGridView: Boolean = false
    var isLoading: Boolean = false

    enum class ViewType {
        LIST, GRID, SKELETON
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            isLoading -> ViewType.SKELETON.ordinal
            isGridView -> ViewType.GRID.ordinal
            else -> ViewType.LIST.ordinal
        }
    }

    override fun getItemCount(): Int {
        return if (isLoading) 3 else super.getItemCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ViewType.SKELETON.ordinal -> {
                SkeletonViewHolder(inflater.inflate(R.layout.item_skeleton, parent, false))
            }
            ViewType.GRID.ordinal -> {
                ProductViewHolder(inflater.inflate(R.layout.item_product_grid, parent, false), true)
            }
            else -> {
                ProductViewHolder(inflater.inflate(R.layout.item_product_list, parent, false), false)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProductViewHolder && !isLoading) {
            holder.bind(getItem(position), onAddToCartClick)
        }
    }

    class ProductViewHolder(itemView: View, private val isGrid: Boolean) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        private val btnAddToCart: View = itemView.findViewById(R.id.btnAddToCart)

        private val productCategory: TextView? = itemView.findViewById(R.id.productCategory)
        private val productRating: RatingBar? = itemView.findViewById(R.id.productRating)

        fun bind(product: Product, onAddToCartClick: (Product) -> Unit) {
            productName.text = product.name
            productPrice.text = itemView.context.getString(R.string.price_format, product.price)
            productImage.setImageResource(product.imageRes)
            
            if (!isGrid) {
                productCategory?.text = product.category
                productRating?.rating = product.rating
                if (btnAddToCart is Button) {
                    btnAddToCart.text = itemView.context.getString(
                        if (product.inCart) R.string.in_cart else R.string.add_to_cart
                    )
                }
            } else {
                if (btnAddToCart is ImageButton) {
                    btnAddToCart.setImageResource(
                        if (product.inCart) android.R.drawable.checkbox_on_background else android.R.drawable.ic_input_add
                    )
                }
            }

            btnAddToCart.setOnClickListener {
                onAddToCartClick(product)
            }
        }
    }

    class SkeletonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean = oldItem == newItem
    }
}
