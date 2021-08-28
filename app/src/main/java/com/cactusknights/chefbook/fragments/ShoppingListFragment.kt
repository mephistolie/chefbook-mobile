package com.cactusknights.chefbook.fragments

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.adapters.ShoppingAdapter
import com.cactusknights.chefbook.databinding.FragmentShoppingListBinding
import com.cactusknights.chefbook.viewmodels.UserViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*


class ShoppingListFragment: Fragment(), ShoppingAdapter.ItemClickListener {

    private val viewModel by activityViewModels<UserViewModel>()
    val shoppingList: ArrayList<String> = arrayListOf()
    private val excludedList: ArrayList<String> = arrayListOf()
    val shoppingAdapter = ShoppingAdapter(shoppingList, this)
    private val ingredientTouchHelper = ItemTouchHelper(ItemsCallback())

    lateinit var binding: FragmentShoppingListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvContent.layoutManager = LinearLayoutManager(context)
        binding.rvContent.adapter = shoppingAdapter
        ingredientTouchHelper.attachToRecyclerView(binding.rvContent)

        binding.inputItem.doOnTextChanged { text, _, _, _ ->
            binding.btnAddItem.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        binding.btnAddItem.setOnClickListener {
            val itemText = binding.inputItem.text.toString()
            if (itemText.isNotEmpty()) {
                shoppingList.add(itemText)
                shoppingAdapter.notifyItemInserted(shoppingList.size-1)
                binding.inputItem.setText("")
            }
        }
    }

    override fun onStart() {
        lifecycleScope.launch { checkForUpdates() }
        super.onStart()
    }

    private suspend fun checkForUpdates() {
        viewModel.listenToShoppingList().collect { newShoppingList: ArrayList<String> ->
            shoppingList.addAll(newShoppingList.filter { it !in shoppingList})
            shoppingAdapter.notifyDataSetChanged()
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
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            val topY = viewHolder.itemView.top + dY
            val bottomY = topY + viewHolder.itemView.height
            if (topY > 0 && bottomY < recyclerView.height) { super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive) }
        }
    }

    override fun onItemClick(position: Int) {
        if (position < shoppingList.size) {
            excludedList.add(shoppingList[position])
            shoppingList.removeAt(position)
            shoppingAdapter.notifyItemRemoved(position)
        }
    }
}