package com.salahtawqit.coffee.helpers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.salahtawqit.coffee.helpers.ListAdapter.ItemClickListener

/**
 * Basic RecyclerView list adapter.
 *
 * @param layoutResId [Int]. The resource ID of the viewHolder layout XML file.
 * @param textViewResId [Int]. The resource ID of the textView inside the provided layout.
 * @param dataList [List]<[String]>. The list of strings to be displayed by the adapter.
 * @param inflater [LayoutInflater]. The layoutInflater of a viewLifecycleOwner, required to inflate
 * @param listener [ItemClickListener]. The listener for each item click.
 * the layout identified by [layoutResId].
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class ListAdapter(
    private val layoutResId: Int,
    private val textViewResId: Int,
    private val dataList: List<String>,
    private val inflater: LayoutInflater,
    private val listener: ItemClickListener
    ): RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(textViewResId)

        init {
            itemView.setOnClickListener {
                /**
                 * Call [ItemClickListener]'s [ItemClickListener.onItemClick].
                 */
                view -> listener.onItemClick(view, adapterPosition)
            }
        }
    }

    interface ItemClickListener {
        /**
         * Called when a [ListAdapter]'s item is clicked.
         */
        fun onItemClick(view: View, position: Int)
    }

    /**
     * Called when RecyclerView needs a new [ListViewHolder] of the given type to represent
     * an item.
     *
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     *
     *
     * The new ViewHolder will be used to display items of the adapter using
     * [.onBindViewHolder]. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary [View.findViewById] calls.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     * @see .getItemViewType
     * @see .onBindViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        // Inflate the view from layout.
        val view = inflater.inflate(layoutResId, parent, false)

        // Return ListViewHolder instance that holds the currently generated view.
        return ListViewHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [ListViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [android.widget.ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [ListViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     * Overriding [onBindViewHolder] instead of Adapter can handle efficient partial bind.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.textView.text = dataList[position]
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return dataList.size
    }
}