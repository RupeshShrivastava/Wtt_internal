Feature: Pickup Order

  Scenario: Create a pickup order with default allocation type
    Given : system allocation type and inventory check enabled
    When : create a pickup order
    Then : i see pick up order failed with 400 bad request

  Scenario: Create a pickup order with allocation type as "system"
    Given : system allocation type and inventory check enabled
    When : create a pickup order with allocation type as system
    Then : i see pick up order failed with 400 bad request

  Scenario: Create a pickup order with allocation type as "preferred"
    Given : system allocation type and inventory check enabled
    When : create a pickup order with allocation type as preferred
    Then : i see pick up order failed with 400 bad request

  Scenario: Create a pickup order with allocation type as "provided" with no merchant location
    Given : system allocation type and inventory check enabled
    When : create a pickup order with allocation type as provided
    Then : i see pick up order failed with 400 bad request

  Scenario: Create a pickup order with allocation type as "provided" with empty merchant location
    Given : system allocation type and inventory check enabled
    When : create a pickup order with allocation type as provided with empty merchant location
    Then : i see pick up order failed with 400 bad request

  Scenario: Create a pickup order with allocation type as "provided" with null merchant location
    Given : system allocation type and inventory check enabled
    When : create a pickup order with allocation type as provided with null merchant location
    Then : i see pick up order failed with 400 bad request

  Scenario: Create a pickup order with allocation type as "provided" with invalid merchant location
    Given : system allocation type and inventory check enabled
    When : create a pickup order with allocation type as provided with invalid merchant location
    Then : i see pick up order created

  Scenario: Create a pickup order with allocation type as "provided" inventory check as "true" and merchant location as "true"
    Given : system allocation type and inventory check enabled
    When : create a pickup order with allocation type provided and IC true and ML true
    Then : i see pick up order created

  Scenario: Create a pickup order with allocation type as "provided" inventory check as "true" and merchant location as "false"
    Given : system allocation type and inventory check enabled
    When : create a pickup order allocation type as provided and IC true and ML false
    Then : i see pick up order created

  Scenario: Create a pickup order with allocation type as "provided" inventory check as "false" and merchant location as "true"
    Given : system allocation type and inventory check enabled
    When : create a pickup order with allocation type as provided and IC false and ML true
    Then : i see pick up order created

  Scenario: Create a pickup order with allocation type as "provided" inventory check as "false" and merchant location as "false"
    Given : system allocation type and inventory check enabled
    When : create a pickup order with allocation type as provided and IC false and ML false
    Then : i see pick up order created

