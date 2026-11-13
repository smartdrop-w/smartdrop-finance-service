@finance @wallet @payments
Feature: US16 - Digital Wallet Recharge and Balance Operations
  As an account holder
  I want to maintain a digital prepaid wallet balance
  So that micro-transactions, logistics dispatch, and on-demand water replenishment are paid seamlessly.

  Background:
    Given the SmartDrop finance service is operational
    And the user has ID 4001

  Scenario: Recharging digital wallet with positive amount
    When the user recharges the wallet with 150.00 USD for "Recarga Billetera Digital"
    Then the wallet balance should be at least 150.00 USD
    And the transaction history should contain an entry with positive amount 150.00 USD

  Scenario: Withdrawing funds from digital wallet with sufficient balance
    Given the user wallet has an available balance of 200.00 USD
    When the user withdraws 75.00 USD from the wallet for "Pago Servicio Tecnico"
    Then the wallet balance should be 125.00 USD

  Scenario: Preventing withdrawal when balance is insufficient
    Given the user wallet has an available balance of 50.00 USD
    When the user attempts to withdraw 300.00 USD from the wallet
    Then the transaction should fail with an insufficient funds error
