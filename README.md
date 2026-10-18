# Portfolio rebalancing — Fintual exercise

A small Java solution that calculates purchases and sales needed to move a stock portfolio toward its target allocation. It generates a plan; it does not place trades or mutate the portfolio.

## Requirements and running

JDK 17 or newer. No external libraries, credentials, market-data account, or build tool is required.

From this directory:

```sh
mkdir -p out
javac --release 17 -d out src/*.java test/*.java
java -cp out Main
java -cp out PortfolioTest
```

Expected example output:

```text
BUY AAPL 2 shares @ 100 (estimated value 200)
SELL META 2 shares @ 100 (estimated value 200)
```

## Model and design decisions

- **Stock:** an immutable record with a symbol and positive `currentPrice`. The constructor receives the last available price; the generated `currentPrice()` accessor returns it. The prompt's wording about a price method is ambiguous, so this implementation treats prices as supplied data rather than fetching them. Supply new Stock records to rebalance when prices change.

- **Portfolio:** holdings map symbols to share quantities; allocations map symbols to target weights, e.g. `0.4` means 40%. Using symbols as keys prevents accidental duplicate positions. This represents collections of stock holdings and targets without coupling portfolio ownership to changing quote objects.

- **Latest prices:** `rebalance(Collection<Stock>)` takes a single caller-provided price snapshot. Separating pricing from holdings makes the algorithm reproducible and easy to test. The caller is responsible for price freshness and consistency.

- **Trade:** records BUY/SELL, positive share quantity, reference price, and the absolute desired monetary change (`targetValueChange`). `estimatedValue()` reports quantity times price after rounding. The desired change and estimated value can differ slightly.



## AI disclosure and submission

I used OpenAI Codex as a code review assistant to review the Java implementation, identify potential edge cases, and suggest improvements to code quality and test coverage.