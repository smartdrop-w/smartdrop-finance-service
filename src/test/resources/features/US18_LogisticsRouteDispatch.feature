@planning @logistics @routes
Feature: US18 - Logistics Route Planning and Carrier Dispatch Lifecycle
  As a logistics coordinator
  I want to calculate delivery routes, assign authorized carriers, and track dispatch status
  So that water tanker replenishment and urgent hardware parts arrive promptly.

  Background:
    Given the SmartDrop logistics planning service is operational
    And an active carrier exists named "Cisterna Express Lima" with contact phone "+51998877665"

  Scenario: Calculating and scheduling a replenishment route
    When the dispatcher schedules a route from "Planta Tratamiento Sur" to "Cerveceria Valle Sagrado"
    Then the route should be created with estimated distance and time
    And the initial route status should be "SCHEDULED"

  Scenario: Assigning carrier to scheduled route
    Given a scheduled route from "Almacen Central" to "Restaurante Marino"
    When the dispatcher assigns carrier "Cisterna Express Lima" to the route
    Then the route should be linked with the carrier ID

  Scenario: Transitioning route status during dispatch lifecycle
    Given a scheduled route from "Almacen Central" to "Restaurante Marino"
    When the carrier updates the route status to "IN_TRANSIT"
    Then the route status should become "IN_TRANSIT"
    When the carrier updates the route status to "COMPLETED"
    Then the route status should become "COMPLETED"
