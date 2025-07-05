# 🛒 E-commerce System in Java

This is a simple object-oriented **E-commerce system** implemented in Java as part of the Fawry Quantum Internship Challenge. It supports product management, cart operations, and checkout functionalities.

## 🚀 Features

- ✅ Define products with name, price, and quantity  
- 🧀 Support for expirable items (e.g., Cheese, Biscuits)  
- 📦 Handle shippable and non-shippable items  
- 📉 Cart with quantity validation and out-of-stock checks  
- 💸 Checkout with:
  - Subtotal
  - Shipping fees
  - Final amount
  - Updated customer balance  
- 📤 Integrated `ShippingService` for shippable products  

## 🧠 Concepts Used

- OOP principles (abstraction, inheritance, interfaces)  
- Exception handling  
- Streams and Lists in Java  
- Interface implementation (`ShippableItem`)  
- Abstract class for reusable product logic  

## 💻 Technologies

- Java 8+  
- Git & GitHub  

## 📦 Sample Output

```text
** Shipment notice **
2x Cheese 400.0g
1x Biscuits 700.0g
Total package weight 1.1kg

** Checkout receipt **
2x Cheese 200.0
1x Biscuits 150.0
1x Scratch Card 50.0
----------------------
Subtotal 400.0
Shipping 30.0
Amount 430.0
Remaining Balance 9570.0
