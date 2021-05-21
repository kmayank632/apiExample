package com.example.apipractice.basemodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class BaseCommonAdapter<T : BaseViewModel>(
    private val items: List<T> = ArrayList(), private val itemClickListener: BaseItemClickListener? = null
) : RecyclerView.Adapter<BaseCommonAdapter.ViewHolder<T>>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        holder.bind(items[position], itemClickListener)
    }

    /**
     * @return Item View Type
     */
    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    /**
     * @return Item Count
     */
    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder<T : BaseViewModel>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: ViewDataBinding? = null

        fun bind(value: T, itemClickListener: BaseItemClickListener?) {
            value.clickListener = itemClickListener
            binding?.setVariable(BR.data, value)
        }

        init {
            binding = DataBindingUtil.bind(itemView)
        }
    }

    /**
     * If need to update the last item layout
     * in case of last item bottom margin in Pagination
     * */
    private var isUpdateLastItem = false

    fun setLastItemUpdate(value: Boolean) {
        this.isUpdateLastItem = value
    }

    /**
     * Sets items to the adapter and notifies that data set has been changed.
     *
     * @param items items to set to the adapter
     */
    fun setItems(items: List<T>?) {
        setItems(items, true)
    }

    /**
     * Sets items to the adapter and notifies that data set has been changed.
     * Typically this method should be use with `notifyChanges = false` in
     * case you are using DiffUtil
     * [DiffUtil] in order to delegate it do all the updating job.
     *
     * @param items         items to set to the adapter
     * @param notifyChanges pass in `true` to call notifiDatasetChanged
     * [RecyclerView.Adapter.notifyDataSetChanged] or `false` otherwise
     * @throws IllegalArgumentException in case of setting `null` items
     */
    @Throws(IllegalArgumentException::class)
    fun setItems(items: List<T>?, notifyChanges: Boolean) {
        requireNotNull(items) { "Cannot set `null` item to the Recycler adapter" }

        if (items is ArrayList)
            this.items.apply {
                clear()
                this@BaseCommonAdapter.addAll(items)
            }.also {
                if (notifyChanges) {
                    notifyDataSetChanged()
                }
            }
    }

    /**
     * Updates items list.
     * Typically to be used for the implementation of DiffUtil [DiffUtil]
     *
     * @param newItems new items
     */
    fun updateItems(newItems: List<T>?) {
        setItems(newItems, false)
    }

    /**
     * Updates items with use of DiffUtil callback [DiffUtil.Callback]
     *
     * @param newItems     new items
     * @param diffCallback DiffUtil callback
     */
    fun updateItems(newItems: List<T>?, diffCallback: DiffUtil.Callback) {
        val result = DiffUtil.calculateDiff(diffCallback, false)
        setItems(newItems, false)
        result.dispatchUpdatesTo(this)
    }

    /**
     * Returns all items from the data set held by the adapter.
     *
     * @return All of items in this adapter.
     */
    fun getItems(): List<T> {
        return items
    }

    /**
     * Returns an items from the data set at a certain position.
     *
     * @return All of items in this adapter.
     */
    fun getItem(position: Int): T? {
        return if (position >= items.size) {
            null
        } else {
            items[position]
        }
    }

    /**
     * Adds item to the end of the data set.
     * Notifies that item has been inserted.
     *
     * @param item item which has to be added to the adapter.
     */
    fun add(item: T?) {
        requireNotNull(item) { "Cannot add null item to the Recycler adapter" }

        if (items is ArrayList)
            items.apply {
                add(item)
            }.let {

                val lastItemPosition = it.size - 1

                if (isUpdateLastItem && lastItemPosition > 0) {
                    notifyItemChanged(lastItemPosition - 1)
                }

                notifyItemInserted(lastItemPosition)
            }
    }

    /**
     * Adds item to the beginning of the data set.
     * Notifies that item has been inserted.
     *
     * @param index position which item will be added
     * @param item item which has to be added to the adapter.
     */
    fun add(index: Int, item: T?) {
        requireNotNull(item) { "Cannot add null item to the Recycler adapter" }

        if (items is ArrayList)
            items.apply {
                add(index, item)
            }.let {

                if (isUpdateLastItem && index > 0) {
                    notifyItemChanged(index - 1)
                }

                notifyItemInserted(index)
            }
    }

    /**
     * Adds item to the beginning of the data set.
     * Notifies that item has been inserted.
     *
     * @param item item which has to be added to the adapter.
     */
    fun addToBeginning(item: T?) {
        requireNotNull(item) { "Cannot add null item to the Recycler adapter" }

        if (items is ArrayList)
            items.apply {
                this.add(0, item)
            }.let {
                notifyItemInserted(0)
            }
    }

    /**
     * Adds list of items to the end of the adapter's data set.
     * Notifies that item has been inserted.
     *
     * @param items items which has to be added to the adapter.
     */
    open fun addAll(items: List<T>?) {
        requireNotNull(items) { "Cannot add `null` items to the Recycler adapter" }

        if (this.items is ArrayList)
            this.items.apply {
                addAll(items)
            }.let {

                val newItemPosition = it.size - items.size

                if (isUpdateLastItem && newItemPosition > 0) {
                    notifyItemChanged(newItemPosition - 1)
                }

                notifyItemRangeInserted(newItemPosition, items.size)
            }
    }

    /**
     * Clears all the items in the adapter.
     */
    fun clear() {
        if (items is ArrayList)
            items.apply {
                clear()
            }.let {
                notifyDataSetChanged()
            }
    }

    /**
     * Removes an item from the adapter.
     * Notifies that item has been removed.
     *
     * @param item to be removed
     */
    fun remove(item: T) {
        if (items is ArrayList)
            items.apply {
                val position = indexOf(item)
                if (position > -1) {
                    removeAt(position)
                    notifyItemRemoved(position)

                    if (isUpdateLastItem && (itemCount > 0)) {
                        notifyItemChanged(itemCount - 1)
                    }
                }
            }
    }

    /**
     * Returns whether adapter is empty or not.
     *
     * @return `true` if adapter is empty or `false` otherwise
     */
    val isEmpty: Boolean
        get() = itemCount == 0
}