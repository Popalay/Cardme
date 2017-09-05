package com.popalay.cardme.presentation.screens

import android.databinding.BindingAdapter
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.support.v7.widget.RecyclerView
import com.github.nitrico.lastadapter.ItemType
import com.github.nitrico.lastadapter.LastAdapter
import com.jakewharton.rxrelay2.Relay
import com.popalay.cardme.BR
import com.popalay.cardme.R
import com.popalay.cardme.domain.model.Card
import com.popalay.cardme.domain.model.Debt
import com.popalay.cardme.domain.model.Holder
import com.popalay.cardme.databinding.ItemCardBinding
import com.popalay.cardme.databinding.ItemHolderBinding

typealias LastAdapterHolder<V> = com.github.nitrico.lastadapter.Holder<V>

@BindingAdapter(value = *arrayOf("cardsAdapter", "cardClick", "showImage"), requireAll = false)
fun RecyclerView.cardsAdapter(items: List<Card>?, publisher: Relay<Card>?, showImage: ObservableBoolean?) {
    if (this.adapter != null || items == null || items.isEmpty()) return
    LastAdapter(items, BR.item)
            .map<Card>(object : ItemType<ItemCardBinding>(R.layout.item_card) {
                override fun onCreate(holder: LastAdapterHolder<ItemCardBinding>) {
                    super.onCreate(holder)
                    holder.binding.publisher = publisher
                    holder.binding.cardView.isWithImage = showImage?.get() ?: false

                    showImage?.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                        override fun onPropertyChanged(observable: Observable, i: Int) {
                            holder.binding.cardView.isWithImage = showImage.get()
                        }
                    })

                }
            }).into(this)
}

@BindingAdapter("debtsAdapter")
fun RecyclerView.debtsAdapter(items: List<Debt>?) {
    if (this.adapter != null || items == null || items.isEmpty()) return
    LastAdapter(items, BR.item)
            .map<Debt>(R.layout.item_debt)
            .into(this)
}

@BindingAdapter(value = *arrayOf("holdersAdapter", "holderClick"), requireAll = false)
fun RecyclerView.holdersAdapter(items: List<Holder>?, publisher: Relay<Holder>?) {
    if (this.adapter != null || items == null || items.isEmpty()) return
    LastAdapter(items, BR.item)
            .map<Holder>(object : ItemType<ItemHolderBinding>(R.layout.item_holder) {
                override fun onCreate(holder: LastAdapterHolder<ItemHolderBinding>) {
                    super.onCreate(holder)
                    holder.binding.publisher = publisher
                }
            })
            .into(this)
}
