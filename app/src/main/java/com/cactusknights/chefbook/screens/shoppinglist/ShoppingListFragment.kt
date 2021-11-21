package com.cactusknights.chefbook.screens.shoppinglist

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.databinding.FragmentShoppingListBinding
import com.cactusknights.chefbook.models.Selectable
import com.cactusknights.chefbook.screens.shoppinglist.adapters.PurchasesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingListFragment: Fragment() {

    private val viewModel : ShoppingListFragmentViewModel by viewModels()
    private val shoppingAdapter = PurchasesAdapter { index: Int -> viewModel.changePurchasedStatus(index) }
    private val itemTouchHelper = ItemTouchHelper(ItemsCallback())

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvContent.layoutManager = LinearLayoutManager(context)
        binding.rvContent.adapter = shoppingAdapter
        itemTouchHelper.attachToRecyclerView(binding.rvContent)

        binding.inputItem.doOnTextChanged { text, _, _, _ ->
            binding.btnAddItem.visibility = if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        binding.btnAddItem.setOnClickListener {
            val itemText = binding.inputItem.text.toString()
            if (itemText.isNotEmpty() && viewModel.state.value.shoppingList.filter { it.item == itemText }.isEmpty()) {
                viewModel.addPurchase(Selectable(itemText))
            }
            binding.inputItem.setText("")
        }
        binding.btnDeletePurchased.setOnClickListener { viewModel.deletePurchased() }

        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { checkForUpdates() } }
    }

    private suspend fun checkForUpdates() {
        viewModel.getShoppingList()
        viewModel.state.collect { state ->
            binding.btnDeletePurchased.visibility = if (state.shoppingList.any { it.isSelected }) View.VISIBLE else View.INVISIBLE
            val newShoppingList = arrayListOf<Selectable<String>>()
            newShoppingList.addAll(state.shoppingList)
            shoppingAdapter.differ.submitList(newShoppingList)
        }
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
            if (viewModel.state.value.shoppingList[from].isSelected || viewModel.state.value.shoppingList[to].isSelected) return false
            viewModel.movePurchase(from, to)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewModel.removePurchase(viewHolder.adapterPosition)
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            val topY = viewHolder.itemView.top + dY
            val bottomY = topY + viewHolder.itemView.height
            if (topY > -50 && bottomY < recyclerView.height || dY == 0.0F) { super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive) }
        }
    }

    override fun onPause() {
        viewModel.commitChanges()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}