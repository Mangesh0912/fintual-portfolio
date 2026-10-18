package com.portfolio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

public class Portfolio {

	private final Map<String, BigDecimal> holdings;
	private final Map<String, BigDecimal> allocations;

	public enum Side {
		BUY, SELL
	};

	public record Trade(String symbol, Side side, BigDecimal qty, BigDecimal price, BigDecimal targetValueChange) {
		public BigDecimal estimatedValue() {
			return qty.multiply(price);
		}
	}

	public Portfolio(Map<String, BigDecimal> holdings, Map<String, BigDecimal> allocations) {
		this.holdings = validatedCopy(holdings, "holding");
		this.allocations = validatedCopy(allocations, "allocations");

		BigDecimal total = this.allocations.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		if (total.compareTo(BigDecimal.ONE) != 0) {
			throw new IllegalArgumentException("Allocation weights must sum to 1");
		}

	}
	
	private static Map<String, BigDecimal> validatedCopy(Map<String, BigDecimal> input, String label) {
		
		Objects.requireNonNull(input,label +" map");
		Map<String, BigDecimal> copy = new HashMap<String, BigDecimal>();
		
		input.forEach((symbol,value) -> {
			 Objects.requireNonNull(symbol, "symbol");
			 Objects.requireNonNull(value, label);
			 
			 if(symbol.isBlank() || !symbol.equals(symbol.trim())) {
	              throw new IllegalArgumentException("Symbols must be nonblank without surrounding spaces");
			 }
			 
			 if(value.signum() < 0) {
	                throw new IllegalArgumentException(label + " must be nonnegative");
			 }
			 copy.put(symbol, value);
		});
		
		
		return Collections.unmodifiableMap(copy);
		
	}

	public List<Trade> rebalance(Collection<Stock> stocks) {

		Objects.requireNonNull(stocks, "stocks");
		Map<String, BigDecimal> prices = new HashMap<String, BigDecimal>();
		System.out.println("Holdings are :" + this.holdings);
		System.out.println("allocations are:" + this.allocations);
		List<Trade> trades = new ArrayList<Trade>();

		for (Stock stock : stocks) {
			Objects.requireNonNull(stock, "stock");

			if (prices.containsKey(stock.symbol())) {
				throw new IllegalArgumentException("Duplicate price:" + stock.symbol());
			}

			prices.put(stock.symbol(), stock.currentPrice());
		}

		System.out.println("Prices are :" + prices);

		Set<String> symbols = new TreeSet<String>(prices.keySet());
		symbols.addAll(allocations.keySet());

		System.out.println("symbols are " + symbols);

		BigDecimal totalValue = BigDecimal.ZERO;

		for (var holdings : this.holdings.entrySet()) {
			totalValue = totalValue.add(holdings.getValue().multiply(prices.get(holdings.getKey())));
		}

		System.out.println("Totalvalue is:" + totalValue);

		for (String symbol : symbols) {

			System.out.println("Currently processing for symbol:" + symbol);

			BigDecimal price = prices.get(symbol);
			BigDecimal currentValue = this.holdings.get(symbol).multiply(price);
			BigDecimal targetValue = totalValue.multiply(this.allocations.get(symbol));
			BigDecimal diff = targetValue.subtract(currentValue);

			if (diff.signum() == 0) {
				continue;
			}

			// Assuming fractional sahres are allowed

			System.out.println("current value is:" + currentValue);
			System.out.println("target value is:" + targetValue);
			System.out.println("difference is:" + diff);

			BigDecimal qty = diff.abs().divide(price, 8, RoundingMode.DOWN);
			System.out.println(qty);

			//public record Trade(String symbol, String side, BigDecimal qty, BigDecimal price, BigDecimal targetValueChange)
			if (qty.signum() > 0) {
               Trade t = new Trade(symbol, diff.signum() > 0 ? Side.BUY: Side.SELL, qty, price, diff.abs());
               trades.add(t);
			}
			
			//break;
		}

		return trades;

	}

}
