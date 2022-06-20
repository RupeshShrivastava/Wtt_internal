Feature: Delivery Order of allocation type preferred

  Scenario: Create a delivery order with allocation type as "preferred" and invalid consultation
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order with allocation type preferred and invalid consultation
    Then : i see delivery up order of allocation type preferred created

  Scenario: Create a delivery order with allocation type as "preferred" and other entity consultation id
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order with allocation type preferred and other person consultation_id consultation
    Then : i see delivery up order of allocation type preferred created

  Scenario: Create a delivery order with allocation type as "preferred" and with merchant_location
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order with allocation type preferred and with merchant_location
    Then : i see delivery up order of allocation type preferred created

  Scenario: Create a delivery order with allocation type as "preferred" and with merchant_location and consultation
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order with allocation type preferred and with merchant_location and consultation
    Then : i see delivery up order of allocation type preferred created

  Scenario: Create a delivery order of type delayed_instant with allocation type as "preferred" inventory check as "true" and merchant location as "true"
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant with allocation type preferred and IC true and ML true
    Then : i see delivery up order of allocation type preferred created

  Scenario: Create a delivery order of type instant with allocation type as "preferred" inventory check as "true" and merchant location as "true"
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order of type instant with allocation type preferred and IC true and ML true
    Then : i see delivery up order of allocation type preferred created

  Scenario: Create a delivery order of type delayed_instant with allocation type as "preferred" inventory check as "true" and merchant location as "false"
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant allocation type as preferred and IC true and ML false
    Then : i see delivery up order of allocation type preferred created

  Scenario: Create a delivery order of type instant with allocation type as "preferred" inventory check as "true" and merchant location as "false"
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order of type instant allocation type as preferred and IC true and ML false
    Then : i see delivery up order of allocation type preferred created

  Scenario: Create a delivery order of type delayed_instant with allocation type as "preferred" inventory check as "false" and merchant location as "true"
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant with allocation type as preferred and IC false and ML true
    Then : i see delivery up order of allocation type preferred created

  Scenario: Create a delivery order of type instant with allocation type as "preferred" inventory check as "false" and merchant location as "true"
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order of type instant with allocation type as preferred and IC false and ML true
    Then : i see delivery up order of allocation type preferred created


  Scenario: Create a delivery order of type delayed_instant with allocation type as "preferred" inventory check as "false" and merchant location as "false"
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order of type delayed_instant with allocation type as preferred and IC false and ML false
    Then : i see delivery up order of allocation type preferred created

  Scenario: Create a delivery order of type instant with allocation type as "preferred" inventory check as "false" and merchant location as "false"
    Given : delivery order preferred allocation type and inventory check enabled
    When : create a delivery order of type instant with allocation type as preferred and IC false and ML false
    Then : i see delivery up order of allocation type preferred created

  