Feature: Delivery Order of allocation type system

  Scenario: Create a delivery order of type delayed_instant with allocation type as "system" inventory check as "true" and merchant location as "true"
    Given : delivery order system allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant with allocation type system and IC true and ML true
    Then : i see delivery up order of allocation type system created created

  Scenario: Create a delivery order of type instant with allocation type as "system" inventory check as "true" and merchant location as "true"
    Given : delivery order system allocation type and inventory check enabled
    When : create a delivery order of type instant with allocation type system and IC true and ML true
    Then : i see delivery up order of allocation type system created created

  Scenario: Create a delivery order of type delayed_instant with allocation type as "system" inventory check as "true" and merchant location as "false"
    Given : delivery order system allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant allocation type as system and IC true and ML false
    Then : i see delivery up order of allocation type system created created

  Scenario: Create a delivery order of type instant with allocation type as "system" inventory check as "true" and merchant location as "false"
    Given : delivery order system allocation type and inventory check enabled
    When : create a delivery order of type instant allocation type as system and IC true and ML false
    Then : i see delivery up order of allocation type system created created

  Scenario: Create a delivery order of type delayed_instant with allocation type as "system" inventory check as "false" and merchant location as "true"
    Given : delivery order system allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant with allocation type as system and IC false and ML true
    Then : i see delivery up order of allocation type system created created

  Scenario: Create a delivery order of type instant with allocation type as "system" inventory check as "false" and merchant location as "true"
    Given : delivery order system allocation type and inventory check enabled
    When : create a delivery order of type instant with allocation type as system and IC false and ML true
    Then : i see delivery up order of allocation type system created created


  Scenario: Create a delivery order of type delayed_instant with allocation type as "system" inventory check as "false" and merchant location as "false"
    Given : delivery order system allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant with allocation type as system and IC false and ML false
    Then : i see delivery up order of allocation type system created created

  Scenario: Create a delivery order of type instant with allocation type as "system" inventory check as "false" and merchant location as "false"
    Given : delivery order system allocation type and inventory check enabled
    When : create a delivery order of type instant with allocation type as system and IC false and ML false
    Then : i see delivery up order of allocation type system created created

