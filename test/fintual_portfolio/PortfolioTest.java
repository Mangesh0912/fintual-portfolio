package fintual_portfolio;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.portfolio.Portfolio;
import com.portfolio.Stock;

public class PortfolioTest {
	
	public static BigDecimal n(String value) {
		 return new BigDecimal(value);
	}
	
	private static void check(boolean condition, String message) {
		if(!condition) {
			throw new AssertionError(message);
		}
	} 
	
	public static void main(String[] args) {
		var prices = List.of(new Stock("META", n("100")), new Stock("AAPL", n("100")));
		var target = Map.of("META", n("0.4"), "AAPL", n("0.6"));
		var p = new Portfolio(Map.of("META", n("6"), "AAPL", n("4")), target);
		var trades = p.rebalance(prices);
		
		check(trades.size() == 2, "Two trades required");
		check(trades.get(0).symbol().equals("AAPL") && trades.get(0).side() == Portfolio.Side.BUY, "Buy AAPL");
		check(trades.get(1).side() == Portfolio.Side.SELL, "Sell META");
		
		
		
		
		System.out.println(prices);
	}

}
