package com.cactusknights.chefbook.screens.main.fragments.shoppinglist

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
import com.cactusknights.chefbook.common.Utils.forceSubmitList
import com.cactusknights.chefbook.databinding.FragmentShoppingListBinding
import com.cactusknights.chefbook.models.Purchase
import com.cactusknights.chefbook.models.ShoppingList
import com.cactusknights.chefbook.screens.main.fragments.shoppinglist.adapters.PurchasesAdapter
import com.cactusknights.chefbook.screens.main.fragments.shoppinglist.models.ShoppingListScreenEvent
import com.cactusknights.chefbook.screens.main.fragments.shoppinglist.models.ShoppingListScreenState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingListFragment: Fragment() {

    private val viewModel : ShoppingListFragmentViewModel by viewModels()
    private val shoppingAdapter = PurchasesAdapter { index: Int -> viewModel.obtainEvent(ShoppingListScreenEvent.ChangePurchasedStatus(index)) }
    private val itemTouchHelper = ItemTouchHelper(ItemsCallback())

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!

    private var shoppingList = ShoppingList(arrayListOf())

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
            if (itemText.isNotEmpty()) {
                viewModel.obtainEvent(ShoppingListScreenEvent.AddPurchase(Purchase(name = itemText)))
                binding.inputItem.setText("")
            }
        }
        binding.btnDeletePurchased.setOnClickListener { viewModel.obtainEvent(ShoppingListScreenEvent.DeletePurchased) }

        viewLifecycleOwner.lifecycleScope.launch { viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.shoppingListState.collect { state -> render(state) }
        } }
    }

    private fun render(state: ShoppingListScreenState) {
        if (state is ShoppingListScreenState.ShoppingListUpdated) {
            shoppingList = state.shoppingList
            shoppingAdapter.differ.forceSubmitList(state.shoppingList.purchases.sortedBy { it.isPurchased })
            val currentBinding = _binding
            currentBinding?.btnDeletePurchased?.postDelayed(
                {
                    currentBinding.btnDeletePurchased.visibility =
                        if (state.shoppingList.purchases.any { it.isPurchased }) View.VISIBLE else View.GONE
                }, 0
            )
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
            if (shoppingList.purchases[from].isPurchased || shoppingList.purchases[to].isPurchased) return false
            viewModel.obtainEvent(ShoppingListScreenEvent.MovePurchase(from, to))
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewModel.obtainEvent(ShoppingListScreenEvent.DeletePurchase(viewHolder.adapterPosition))
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
            val topY = viewHolder.itemView.top + dY
            val bottomY = topY + viewHolder.itemView.height
            if (topY > -50 && bottomY < recyclerView.height || dY == 0.0F) { super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive) }
        }
    }

    override fun onPause() {
        viewModel.obtainEvent(ShoppingListScreenEvent.ConfirmInput)
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}