package com.portfolio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Portfolio portfolio = new Portfolio(Map.of("META", new BigDecimal("6"), "AAPL", new BigDecimal("4")),
				Map.of("META", new BigDecimal("0.4"), "AAPL", new BigDecimal("0.6")));

		System.out.println(portfolio);

		var prices = List.of(new Stock("META", new BigDecimal("100")), new Stock("AAPL", new BigDecimal("100")));
		
		System.out.println(prices);
		
		for(var trade: portfolio.rebalance(prices)) {
			System.out.printf("%s %s %s shares @ %s (estimated value %s)%n",
                    trade.side(), trade.symbol(), trade.qty().stripTrailingZeros().toPlainString(),
                    trade.price().toPlainString(), trade.estimatedValue().stripTrailingZeros().toPlainString());
		}

	}

}
