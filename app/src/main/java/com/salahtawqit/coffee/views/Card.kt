package com.salahtawqit.coffee.views

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.salahtawqit.coffee.R

/**
 * Cards that contain an icon, a title, and a simple description.
 *
 * @since v1.0
 * @author Devshot devshot.coffee@gmail.com
 */
class Card(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    init {
        inflate(context, R.layout.template_card, this)

        // Cache the views.
        val icon = findViewById<ImageView>(R.id.card_icon)
        val title = findViewById<TextView>(R.id.card_title)
        val description = findViewById<TextView>(R.id.card_description)

        // Obtain the custom styleable.
        val cardAttrs = context.obtainStyledAttributes(attrs, R.styleable.Card)

        // Set values from styleable attributes.
        title.text = cardAttrs.getString(R.styleable.Card_cardTitle)
        icon.setImageDrawable(cardAttrs.getDrawable(R.styleable.Card_cardIcon))
        description.text = cardAttrs.getString(R.styleable.Card_cardDescription)
        icon.contentDescription = cardAttrs.getString(R.styleable.Card_cardTitle)

        // Recycle for the changes to appear.
        cardAttrs.recycle()
    }
}