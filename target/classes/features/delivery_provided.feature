Feature: Delivery Order of allocation type provided

  Scenario: Create a delivery order with allocation type as "provided" with empty merchant location
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order with allocation type as provided with empty merchant location
    Then : i see delivery up order failed with 400 bad request

  Scenario: Create a delivery order with allocation type as "provided" with null merchant location
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order with allocation type as provided with null merchant location
    Then : i see delivery up order failed with 400 bad request

  Scenario: Create a delivery order with allocation type as "provided" with invalid merchant location
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order with allocation type as provided with invalid merchant location
    Then : i see delivery up order of allocation type provided created

  Scenario: Create a delivery order with allocation type as "provided" with valid merchant location
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order with allocation type as provided with valid merchant location
    Then : i see delivery up order of allocation type provided created

  Scenario: Create a delivery order of type delayed_instant with allocation type as "provided" inventory check as "true" and merchant location as "true"
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant with allocation type provided and IC true and ML true
    Then : i see delivery up order of allocation type provided created

  Scenario: Create a delivery order of type instant with allocation type as "provided" inventory check as "true" and merchant location as "true"
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order of type instant with allocation type provided and IC true and ML true
    Then : i see delivery up order of allocation type provided created

  Scenario: Create a delivery order of type delayed_instant with allocation type as "provided" inventory check as "true" and merchant location as "false"
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant allocation type as provided and IC true and ML false
    Then : i see delivery up order of allocation type provided created

  Scenario: Create a delivery order of type instant with allocation type as "provided" inventory check as "true" and merchant location as "false"
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order of type instant allocation type as provided and IC true and ML false
    Then : i see delivery up order of allocation type provided created

  Scenario: Create a delivery order of type delayed_instant with allocation type as "provided" inventory check as "false" and merchant location as "true"
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant with allocation type as provided and IC false and ML true
    Then : i see delivery up order of allocation type provided created

  Scenario: Create a delivery order of type instant with allocation type as "provided" inventory check as "false" and merchant location as "true"
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order of type instant with allocation type as provided and IC false and ML true
    Then : i see delivery up order of allocation type provided created


  Scenario: Create a delivery order of type delayed_instant with allocation type as "provided" inventory check as "false" and merchant location as "false"
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant with allocation type as provided and IC false and ML false
    Then : i see delivery up order of allocation type provided created

  Scenario: Create a delivery order of type instant with allocation type as "provided" inventory check as "false" and merchant location as "false"
    Given : delivery order provided allocation type and inventory check enabled
    When : create a delivery order of type instant with allocation type as provided and IC false and ML false
    Then : i see delivery up order of allocation type provided created
