@finance @billing
Feature: US15 - SaaS Subscriptions and Digital Wallet Management
  As a business customer
  I want to manage monthly SaaS tiers and digital wallet balance
  So that platform telemetry ingestion and automated alerts remain active.

  Scenario: Recharging digital wallet with credit card token
    When the user authorizes a wallet recharge of 150.00 USD
    Then the wallet balance should increment by 150.00 USD
    And an invoice record should be generated with status "PAID"
