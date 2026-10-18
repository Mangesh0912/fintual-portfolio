package com.portfolio;

import java.math.BigDecimal;
import java.util.Objects;

public record Stock(String symbol, BigDecimal currentPrice) {

	public Stock {
		Objects.requireNonNull(symbol, "symbol");
		Objects.requireNonNull(currentPrice, "currentPrice");
		symbol = symbol.trim();

		if (symbol.isEmpty()) {
			throw new IllegalArgumentException("Symbol must not be blank");
		}

		if (currentPrice.signum() <= 0) {
			throw new IllegalArgumentException("Price must be positive");
		}

	}

}
