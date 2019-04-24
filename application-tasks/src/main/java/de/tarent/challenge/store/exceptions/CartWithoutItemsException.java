package de.tarent.challenge.store.exceptions;

public class CartWithoutItemsException extends StoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CartWithoutItemsException(String string) {
		super(string);
	}

}
