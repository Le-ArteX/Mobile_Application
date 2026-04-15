package com.example.ecommerceapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerceapp.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import java.util.Collections

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter
    private val allProducts = mutableListOf<Product>()
    private var filteredProducts = mutableListOf<Product>()
    private val cartItems = mutableListOf<Product>()
    private var currentCategory = "All"
    private var searchQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        setupSampleData()
        setupRecyclerView()
        setupCategoryFilter()
        
        loadProducts()
    }

    private fun loadProducts() {
        adapter.isLoading = true
        adapter.notifyDataSetChanged()
        
        Handler(Looper.getMainLooper()).postDelayed({
            adapter.isLoading = false
            updateList()
        }, 2000)
    }

    private fun setupSampleData() {
        allProducts.clear()
        allProducts.addAll(listOf(
            Product(1, "Smartphone X", 799.99, 4.5f, "Electronics", android.R.drawable.ic_menu_call),
            Product(2, "Cotton T-Shirt", 19.99, 4.0f, "Clothing", android.R.drawable.ic_menu_gallery),
            Product(3, "Android Programming", 49.99, 4.8f, "Books", android.R.drawable.ic_menu_edit),
            Product(4, "Organic Apples", 5.99, 4.2f, "Food", android.R.drawable.ic_menu_day),
            Product(5, "Lego Set", 89.99, 4.9f, "Toys", android.R.drawable.ic_menu_camera),
            Product(6, "Laptop Pro", 1299.99, 4.7f, "Electronics", android.R.drawable.ic_menu_slideshow),
            Product(7, "Jeans", 39.99, 4.3f, "Clothing", android.R.drawable.ic_menu_view),
            Product(8, "Cooking Master", 29.99, 4.1f, "Books", android.R.drawable.ic_menu_myplaces)
        ))
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter { product ->
            product.inCart = !product.inCart
            if (product.inCart) {
                cartItems.add(product)
                Snackbar.make(binding.root, getString(R.string.added_to_cart, product.name), Snackbar.LENGTH_SHORT).show()
            } else {
                cartItems.remove(product)
            }
            val index = filteredProducts.indexOf(product)
            if (index != -1) {
                adapter.notifyItemChanged(index)
            }
            invalidateOptionsMenu()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                if (adapter.isLoading) return false
                val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                if (fromPos != RecyclerView.NO_POSITION && toPos != RecyclerView.NO_POSITION && 
                    fromPos < filteredProducts.size && toPos < filteredProducts.size) {
                    Collections.swap(filteredProducts, fromPos, toPos)
                    adapter.notifyItemMoved(fromPos, toPos)
                }
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (adapter.isLoading) return
                val position = viewHolder.adapterPosition
                if (position == RecyclerView.NO_POSITION || position >= filteredProducts.size) return
                
                val deletedProduct = filteredProducts[position]
                
                filteredProducts.removeAt(position)
                allProducts.remove(deletedProduct)
                adapter.notifyItemRemoved(position)

                Snackbar.make(binding.recyclerView, getString(R.string.product_deleted), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.undo)) {
                        filteredProducts.add(position, deletedProduct)
                        allProducts.add(deletedProduct)
                        adapter.notifyItemInserted(position)
                        updateList() // Ensure consistency
                    }.show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setupCategoryFilter() {
        binding.categoryChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            currentCategory = chip?.text?.toString() ?: "All"
            updateList()
        }
    }

    private fun updateList() {
        if (adapter.isLoading) return

        filteredProducts = if (currentCategory == "All") {
            allProducts.toMutableList()
        } else {
            allProducts.filter { it.category == currentCategory }.toMutableList()
        }

        if (searchQuery.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { 
                it.name.contains(searchQuery, ignoreCase = true) 
            }.toMutableList()
        }

        adapter.submitList(filteredProducts.toList())
        
        if (filteredProducts.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                searchQuery = newText ?: ""
                updateList()
                return true
            }
        })

        val cartItem = menu.findItem(R.id.action_cart)
        val actionView = cartItem.actionView
        val badge = actionView?.findViewById<TextView>(R.id.cart_badge)
        
        if (cartItems.isNotEmpty()) {
            badge?.visibility = View.VISIBLE
            badge?.text = cartItems.size.toString()
        } else {
            badge?.visibility = View.GONE
        }
        
        actionView?.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            CartActivity.cartItems = cartItems
            startActivity(intent)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_view_toggle -> {
                adapter.isGridView = !adapter.isGridView
                binding.recyclerView.layoutManager = if (adapter.isGridView) {
                    item.setIcon(android.R.drawable.ic_menu_sort_by_size)
                    GridLayoutManager(this, 2)
                } else {
                    item.setIcon(android.R.drawable.ic_menu_agenda)
                    LinearLayoutManager(this)
                }
                adapter.notifyDataSetChanged()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        allProducts.forEach { product ->
            product.inCart = cartItems.contains(product)
        }
        adapter.notifyDataSetChanged()
        invalidateOptionsMenu()
    }
}
