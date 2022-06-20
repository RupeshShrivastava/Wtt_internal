Feature: Create Order

  Scenario: Create a order and confirm a order
    Given : access token and user agent
    When : create a order
    And : confirm a order by cash
    Then : i see order created and confirmed

 """ Scenario: Create a order and confirm a order
    Given : access token and user agent
    When : create a order
    And : confirm a order by wallet
    Then : i see order created and confirmed"""

  Scenario: Create a order , confirm a order and cancel
    Given : access token and user agent
    When : create a order
    And : confirm a order by cash
    And :cancel the order
    Then : i see order created and confirmed

 """ Scenario: Create a order , confirm a order and cancel
    Given : access token and user agent
    When : create a order
    And : confirm a order by wallet
    And :cancel the order
    Then : i see order created and confirmed"""

  Scenario: Create a order and abandon a order
    Given : access token and user agent
    When : create a order
    And : abandon order
    Then : i see order created and confirmed

 Scenario: Create a order and apply coupon
    Given : access token and user agent
    When : create a order
    And : apply coupon
   Then : i see order created and confirmed



