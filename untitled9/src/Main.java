import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


interface ShippableItem {
    String getName();
    double getWeight();
}

abstract class Product {
    protected String name;
    protected double price;
    protected int quantity;

    public Product(String name, double price ,int quantity)  {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity(int item_amount) {
        this.quantity -= item_amount;
    }

    public abstract boolean isExpired();
    public abstract boolean requiresShipping();
}

class ExpirableProduct extends Product implements ShippableItem{
    private boolean expire_date ;
    private double weight;

    public ExpirableProduct(String name, double price, int quantity, boolean expired, double weight) {
        super(name, price, quantity);
        this. expire_date = expired;
        this.weight = weight;
    }

    public boolean isExpired() {
        return  expire_date;
    }

    public boolean requiresShipping() {
        return true;
    }

    public double getWeight() {
        return weight;
    }
}

class NonExpirableShippableProduct extends Product implements ShippableItem {
    private double weight;

    public NonExpirableShippableProduct(String name, double price, int quantity, double weight) {
        super(name, price, quantity);
        this.weight = weight;
    }

    public boolean isExpired() {
        return false;
    }

    public boolean requiresShipping() {
        return true;
    }

    public double getWeight() {
        return weight;
    }
}

class NonShippableProduct extends Product {
    public NonShippableProduct(String name, double price, int quantity) {
        super(name, price, quantity);
    }

    public boolean isExpired() {
        return false;
    }

    public boolean requiresShipping() {
        return false;
    }
}

class CartItem {
    Product product;
    int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}

class Cart {
    List<CartItem> items = new ArrayList<>();

    public void add(Product product, int quantity) {
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for " + product.getName());
        }
        if (product.isExpired()) {
            throw new IllegalArgumentException(product.getName() + " is expired.");
        }
        items.add(new CartItem(product, quantity));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getSubtotal() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }

    public List<ShippableItem> getShippableItems() {
        List<ShippableItem> shippables = new ArrayList<>();
        for (CartItem item : items) {
            if (item.getProduct().requiresShipping() && item.getProduct() instanceof ShippableItem) {
                for (int i = 0; i < item.getQuantity(); i++) {
                    shippables.add((ShippableItem) item.getProduct());
                }
            }
        }
        return shippables;
    }
}

class Customer {
    private String name;
    private double balance;

    public Customer(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    public void deductBalance(double amount) {
        balance -= amount;
    }
}

class ShippingService {
    public static void ship(List<ShippableItem> items) {
        if (items.isEmpty()) return;
        System.out.println("** Shipment notice **");
        double totalWeight = 0;
        Map<String, Integer> itemCounts = new HashMap<>();
        for (ShippableItem item : items) {
            totalWeight += item.getWeight();
            itemCounts.put(item.getName(), itemCounts.getOrDefault(item.getName(), 0) + 1);
        }
        for (String name : itemCounts.keySet()) {
            System.out.println(itemCounts.get(name) + "x " + name + " " + items.get(0).getWeight() * itemCounts.get(name) + "g");
        }
        System.out.printf("Total package weight %.1fkg\n", totalWeight / 1000);
    }
}

class ECommerceSystem {
    public static void checkout(Customer customer, Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Error: Cart is empty");
            return;
        }

        double subtotal = cart.getSubtotal();
        double shipping = 30.0;
        double total = subtotal + shipping;

        if (customer.getBalance() < total) {
            System.out.println("Error: Insufficient balance");
            return;
        }

        for (CartItem item : cart.getItems()) {
            item.getProduct().reduceQuantity(item.getQuantity());
        }

        ShippingService.ship(cart.getShippableItems());

        System.out.println("** Checkout receipt **");
        for (CartItem item : cart.getItems()) {
            System.out.printf("%dx %s %.1f\n", item.getQuantity(), item.getProduct().getName(), item.getTotalPrice());
        }
        System.out.println("----------------------");
        System.out.printf("Subtotal %.1f\n", subtotal);
        System.out.printf("Shipping %.1f\n", shipping);
        System.out.printf("Amount %.1f\n", total);

        customer.deductBalance(total);
        System.out.printf("Remaining Balance %.1f\n", customer.getBalance());
    }

    public static void main(String[] args) {
        Product cheese = new ExpirableProduct("Cheese", 100, 5, false, 200);
        Product biscuits = new ExpirableProduct("Biscuits", 150, 3, false, 700);
        Product tv = new NonExpirableShippableProduct("TV", 300, 5, 10000);
        Product scratchCard = new NonShippableProduct("Scratch Card", 50, 10);

        Customer customer = new Customer("Nada", 50000);
        Cart cart = new Cart();

        cart.add(cheese, 2);
        cart.add(biscuits, 1);
        cart.add(scratchCard, 1);

        checkout(customer, cart);
    }
}
