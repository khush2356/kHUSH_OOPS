
class OutOfStockException extends Exception {

    int shortfall;

    OutOfStockException(int shortfall) {
        this.shortfall = shortfall;
    }
}

class InvalidQuantityException extends Exception {

    InvalidQuantityException(String message) {
        super(message);
    }
}

class Warehouse {

    int stock = 10;

    void issue(String item, int qty)
            throws OutOfStockException, InvalidQuantityException {
        if (qty <= 0) {
            throw new InvalidQuantityException("Invalid quantity!");
        }
        if (qty > stock) {
            throw new OutOfStockException(qty - stock);
        }
        stock = stock - qty;
        System.out.println(item + " issued: " + qty);
        System.out.println("Remaining stock: " + stock);
    }

    public static void main(String[] args) {
        Warehouse w = new Warehouse();
        String[] items = {"Pen", "Book", "Pencil", "Bag"};
        int[] quantities = {3, 10, 0, 5};
        for (int i = 0; i < quantities.length; i++) {
            try {
                w.issue(items[i], quantities[i]);
            } catch (OutOfStockException e) {
                System.out.println(items[i]
                        + ": Out of stock. Shortfall = " + e.shortfall);

            } catch (InvalidQuantityException e) {
                System.out.println(items[i] + ": " + e.getMessage());
            }
            System.out.println("----------------");
        }
    }
}
