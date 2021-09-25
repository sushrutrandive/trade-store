package com.db.trade;

import java.util.Optional;

public interface Consumer<T> {
	
	public Optional<T> consume();

}
