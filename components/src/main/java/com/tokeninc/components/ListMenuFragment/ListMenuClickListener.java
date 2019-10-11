package com.tokeninc.components.ListMenuFragment;

/**
 * The click listener interface which has to be implemented by the object which is passed as second parameter to
 * {@code ListMenuFragment.newInstance(List<IListMenuItem> menuItemList, ListMenuClickListener listener)} factory method.
 */
public interface ListMenuClickListener{
    /**
     * @param position: Order of tapped list item in the list menu
     * @param item: This is the instance of your list object's generic class which implements {@link IListMenuItem} interface.
     */
    void onItemClick(int position, IListMenuItem item);
}
