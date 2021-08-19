package com.salahtawqit.coffee.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.salahtawqit.coffee.R

/**
 * List item that has an icon, title, description and a valueView field for something important.
 *
 * Based on the styleable attributes, each individual elements of the layout will be visible and
 * hidden. For example, if the title isn't provided, it'll be hidden.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class ListItem(
    context: Context,
    attrs: AttributeSet
    ): LinearLayout(context, attrs) {

    // Used in companion object.
    var valueView: TextView

    init {
        inflate(context, R.layout.template_list_item, this)

        // Cache the views.
        val icon = findViewById<ImageView>(R.id.list_icon)
        val title = findViewById<TextView>(R.id.list_title)
        valueView = findViewById(R.id.list_value)
        val description = findViewById<TextView>(R.id.list_description)

        // Obtain the custom styleable.
        val styleable = context.obtainStyledAttributes(attrs, R.styleable.ListItem)

        // Set values from styleable attributes and show the elements.
        styleable.getDrawable(R.styleable.ListItem_listIcon)?.let {
            icon.setImageDrawable(it)
            icon.visibility = View.VISIBLE
        }

        styleable.getString(R.styleable.ListItem_listTitle)?.let {
            title.text = it
            title.visibility = View.VISIBLE
        }

        styleable.getString(R.styleable.ListItem_listDescription)?.let {
            description.text = it
            description.visibility = View.VISIBLE
        }

        // Recycle for the changes to appear.
        styleable.recycle()
    }

    companion object {
        @JvmStatic @BindingAdapter("listValue")
        fun setListValue(view: ListItem, value: String?) {
            value?.let {
                view.valueView.text = it
                view.valueView.visibility = View.VISIBLE
            }
        }
    }
}