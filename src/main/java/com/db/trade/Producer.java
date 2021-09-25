package com.db.trade;


public interface Producer<T> {
	
	public void publish(T t);
	

}
