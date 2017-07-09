package com.popalay.cardme.presentation.screens.holders;

public class HoldersPresenter {

/*    @Inject HolderInteractor holderInteractor;
    @Inject NavigationExtrasHolder navigationExtras;

    private boolean openFavoriteHolder;

    public HoldersPresenter() {
        App.Companion.getAppComponent().inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void onFavoriteHolderEvent(FavoriteHolderEvent event) {
        EventBus.getDefault().removeStickyEvent(FavoriteHolderEvent.class);
        openFavoriteHolder = true;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();

        addDisposable(holderInteractor.getHolders()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(holders -> {
                    if (!holders.isEmpty() && openFavoriteHolder) {
                        onHolderClick(holders.get(0));
                        openFavoriteHolder = false;
                    }
                    getViewState().setHolders(holders);
                }, this::handleBaseError));
    }

    public void onHolderClick(Holder holder) {
        navigationExtras.setHolderId(holder.getId());
        getViewState().openHolderDetails();
    }*/
}
