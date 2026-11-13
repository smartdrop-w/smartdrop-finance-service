@finance @subscriptions @billing
Feature: US15 - SaaS Subscriptions Lifecycle and Auto-Renew Configuration
  As a commercial brewery or residential subscriber
  I want to select SaaS subscription tiers and configure automatic renewal
  So that my facility enjoys uninterrupted telemetry ingestion and advanced analytics.

  Background:
    Given the SmartDrop finance service is operational
    And an active subscription plan exists titled "Plan Cervecero Pro" with price 149.99 USD and duration 30 days
    And the subscriber has user ID 4001

  Scenario: Creating a new monthly SaaS subscription
    When the subscriber creates a subscription for "Plan Cervecero Pro" with auto-renew enabled
    Then the subscription should be created with status "PENDING"
    And the subscription auto-renew setting should be true

  Scenario: Toggling subscription auto-renewal
    Given an existing subscription with auto-renew enabled
    When the subscriber toggles auto-renew to false
    Then the subscription auto-renew setting should be false

  Scenario: Updating subscription status to cancelled
    Given an existing subscription with auto-renew enabled
    When the administrator updates the subscription status to "CANCELLED"
    Then the subscription status should be "CANCELLED"
