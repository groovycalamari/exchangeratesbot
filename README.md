Telegram bot to obtain foreign exchange rates published by the European Central Bank. 

[@ForeignExchangeRatesBot](https://t.me/ForeignExchangeRatesBot)

Commands: 

help - how to use this bot
about - Information about this bot
currencies - List of supported currencies
settings - Set deafults for your base and target currencies


## Build Runtimes

### Google Cloud Function
 
To generate a runtime for Google Cloud HTTP Functions use: 

`./gradlew -Pchatbots.env=googlechathttpfunction build`

### Google Chat Bot deployed to AWS Lambda Java 11 runtime

`./gradlew -Pchatbots.env=googlechat build`

### Telegram Bot deployed to AWS Lambda Java 11 runtime

`./gradlew -Pchatbots.env=telegram build`
