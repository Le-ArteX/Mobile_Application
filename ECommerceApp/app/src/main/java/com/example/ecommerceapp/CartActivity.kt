package com.example.ecommerceapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceapp.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: ProductAdapter

    companion object {
        var cartItems: MutableList<Product> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        setupRecyclerView()
        updateTotal()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter { product ->
            // Remove from cart
            product.inCart = false
            cartItems.remove(product)
            updateTotal()
            adapter.submitList(cartItems.toList()) // Use toList to trigger DiffUtil
            if (cartItems.isEmpty()) {
                finish() // Or show empty state
            }
        }
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCart.adapter = adapter
        adapter.submitList(cartItems.toList())
    }

    private fun updateTotal() {
        val total = cartItems.sumOf { it.price }
        binding.tvTotalPrice.text = "$${String.format("%.2f", total)}"
    }
}
