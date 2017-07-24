package com.popalay.cardme.presentation.screens

/*
@BindingAdapter(value = *arrayOf("cardsAdapter", "cardClick", "showImage"), requireAll = false)
fun cardsAdapter(recyclerView: RecyclerView, items: List<Card>?,
                 publisher: PublishRelay<Card>, showImage: ObservableBoolean) {
    if (recyclerView.adapter != null || items == null || items.isEmpty()) return
    LastAdapter(items, BR.item, true)
            .map<Card>(object : ItemType<ItemCardBinding>(R.layout.item_card) {
                override fun onCreate(holder: com.github.nitrico.lastadapter.Holder<ItemCardBinding>) {
                    super.onCreate(holder)
                    holder.binding.publisher = publisher
                    holder.binding.cardView.setWithImage(showImage.get())
                    showImage.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
                        override fun onPropertyChanged(observable: Observable, i: Int) {
                            holder.binding.cardView.setWithImage(showImage.get())
                        }
                    })
                }
            }).into(recyclerView)

}

@BindingAdapter("debtsAdapter")
fun debtsAdapter(recyclerView: RecyclerView, items: List<Debt>?) {
    if (recyclerView.adapter != null || items == null || items.isEmpty()) return
    LastAdapter(items, BR.item, true)
            .map<Debt>(R.layout.item_debt)
            .into(recyclerView)
}

@BindingAdapter(value = *arrayOf("holdersAdapter", "holderClick"), requireAll = false)
fun holdersAdapter(recyclerView: RecyclerView, items: List<Holder>?, listener: ItemClickListener<*>) {
    if (recyclerView.adapter != null || items == null || items.isEmpty()) return
    LastAdapter(items, BR.item, true)
            .map<Holder>(object : ItemType<ItemHolderBinding>(R.layout.item_holder) {
                override fun onCreate(holder: com.github.nitrico.lastadapter.Holder<ItemHolderBinding>) {
                    super.onCreate(holder)
                    holder.binding.listener = listener
                }
            }).into(recyclerView)
}*/
