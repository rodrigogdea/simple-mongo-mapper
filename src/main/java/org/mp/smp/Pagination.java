package org.mp.smp;

import java.util.List;

public interface Pagination<E> {

	public List<E> getPage();

	public int getAmountOfPages();

	public void moveToNextPage();

	public void moveToPreviousPage();

	public int getCurrentPageNumber();

	public void goToPage(int pageNumber);

	public abstract long getCount();

}