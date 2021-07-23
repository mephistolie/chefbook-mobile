package com.cactusknights.chefbook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.adapters.ShoppingAdapter
import com.cactusknights.chefbook.databinding.FragmentRecyclerViewBinding
import com.cactusknights.chefbook.viewmodels.UserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class ShoppingListFragment: Fragment(), ShoppingAdapter.ItemClickListener {

    private val viewModel by activityViewModels<UserViewModel>()
    val shoppingList: ArrayList<String> = arrayListOf()
    private val excludedList: ArrayList<String> = arrayListOf()
    val shoppingAdapter = ShoppingAdapter(shoppingList, this)
    private val ingredientTouchHelper = ItemTouchHelper(ItemsCallback())

    lateinit var binding: FragmentRecyclerViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvContent.layoutManager = LinearLayoutManager(context)
        binding.rvContent.adapter = shoppingAdapter
        ingredientTouchHelper.attachToRecyclerView(binding.rvContent)
    }

    override fun onStart() {
        lifecycleScope.launch { checkForUpdates() }
        super.onStart()
    }

    private suspend fun checkForUpdates() {
        viewModel.listenToShoppingList().collect { newShoppingList: ArrayList<String> ->
            shoppingList.addAll(newShoppingList.filter { it !in shoppingList})
            shoppingAdapter.notifyDataSetChanged()
            binding.textEmptyList.visibility = if (shoppingList.size > 0) View.GONE else View.VISIBLE
        }
    }

    override fun onPause() {
        val oldShoppingList = viewModel.getShoppingList()
        if (oldShoppingList.size == shoppingList.size) {
            for (item in shoppingList) {
                if (item !in oldShoppingList) {
                    viewModel.setShoppingList(shoppingList)
                    break
                }
            }
        } else { viewModel.setShoppingList(shoppingList) }
        super.onPause()
    }

    inner class ItemsCallback: ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {

            val from = viewHolder.adapterPosition
            val to = target.adapterPosition
            Collections.swap(shoppingList, from, to)
            shoppingAdapter.notifyItemMoved(from, to)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            excludedList.add(shoppingList[viewHolder.adapterPosition])
            shoppingList.removeAt(viewHolder.adapterPosition)
            shoppingAdapter.notifyItemRemoved(viewHolder.adapterPosition)
            binding.textEmptyList.visibility = if (shoppingList.size > 0) View.GONE else View.VISIBLE
        }

    }

    override fun onItemClick(position: Int) {
        if (position < shoppingList.size) {
            excludedList.add(shoppingList[position])
            shoppingList.removeAt(position)
            shoppingAdapter.notifyItemRemoved(position)
            binding.textEmptyList.visibility = if (shoppingList.size > 0) View.GONE else View.VISIBLE
        }
    }
}