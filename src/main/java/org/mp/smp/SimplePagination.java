package org.mp.smp;

import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBObject;

public class SimplePagination<E> implements Pagination<E> {

	private String collection;
	private int pageSize;
	private DB mongoDB;
	private MongoMapper mongoMapper;
	private long count;
	private int amountOfPages;
	private int currentPageNumber = 1;
	private DBObject filter;

	public SimplePagination(String collection, DBObject filter, int pageSize,
			MongoMapper mongoMapper, DB mongoDB) {
		this.collection = collection;
		this.filter = filter;
		this.pageSize = pageSize;
		this.mongoMapper = mongoMapper;
		this.mongoDB = mongoDB;
		this.count = this.mongoDB.getCollection(collection).getCount(this.filter);

		if ((this.count % this.pageSize) == 0) {
			this.amountOfPages = (int) (this.count / this.pageSize);
		} else {
			this.amountOfPages = (int) (this.count / this.pageSize) + 1;
		}
	}

	public List<E> getPage() {
		int skip = (currentPageNumber * pageSize) - pageSize;
		return this.mongoMapper.find(collection, filter, skip, pageSize, mongoDB);
	}

	public int getAmountOfPages() {
		return amountOfPages;
	}

	public void moveToNextPage() {
		if (amountOfPages > currentPageNumber) {
			this.currentPageNumber++;
		}
	}

	public void moveToPreviousPage() {
		if (1 < currentPageNumber) {
			this.currentPageNumber--;
		}
	}

	public int getCurrentPageNumber() {
		return currentPageNumber;
	}

	public void goToPage(int pageNumber) {
		if (1 > pageNumber) {
			this.currentPageNumber = 1;
		} else if (amountOfPages < pageNumber) {
			this.currentPageNumber = amountOfPages;
		} else {
			this.currentPageNumber = pageNumber;
		}
	}

	@Override
	public long getCount() {
		return count;
	}
}
