package com.cactusknights.chefbook.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.adapters.ShoppingAdapter
import com.cactusknights.chefbook.viewmodels.UserViewModel
import java.util.*
import kotlin.collections.ArrayList


class ShoppingListFragment: Fragment(), ShoppingAdapter.ItemClickListener {

    private val viewModel by activityViewModels<UserViewModel>()
    val shoppingList: ArrayList<String> = arrayListOf()
    private val excludedList: ArrayList<String> = arrayListOf()
    val shoppingAdapter = ShoppingAdapter(shoppingList, this)

    lateinit var recyclerView: RecyclerView
    lateinit var emptyListTitle: TextView

    private val ingredientTouchHelper = ItemTouchHelper(ItemsCallback())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = shoppingAdapter
        ingredientTouchHelper.attachToRecyclerView(recyclerView)

        emptyListTitle = view.findViewById(R.id.empty_list_title)
    }

    override fun onStart() {
        viewModel.shoppingList.observe(this, { newShoppingList ->
            shoppingList.addAll(newShoppingList.filter { it !in shoppingList})
            shoppingAdapter.notifyDataSetChanged()
            emptyListTitle.visibility = if (shoppingList.size > 0) View.GONE else View.VISIBLE
        })
        super.onStart()
    }

    override fun onPause() {
        val oldShoppingList = viewModel.shoppingList.value
        if (oldShoppingList?.size == shoppingList.size) {
            for (item in shoppingList) {
                if (item !in oldShoppingList) {
                    viewModel.shoppingList = MutableLiveData(shoppingList)
                    break
                }
            }
        } else { viewModel.shoppingList = MutableLiveData(shoppingList) }
        super.onPause()
    }

    inner class ItemsCallback(): ItemTouchHelper.Callback() {
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
            emptyListTitle.visibility = if (shoppingList.size > 0) View.GONE else View.VISIBLE
        }

    }

    override fun onItemClick(position: Int) {
        if (position < shoppingList.size) {
            excludedList.add(shoppingList[position])
            shoppingList.removeAt(position)
            shoppingAdapter.notifyItemRemoved(position)
            emptyListTitle.visibility = if (shoppingList.size > 0) View.GONE else View.VISIBLE
        }
    }
}