package GUI;

public class OrderService {
    
    private ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void processOrder(Order order) throws Exception {
        
        productRepository.beginTransaction();
        
        try {
            // Loop through each order line in the order
            for (OrderLine line : order.getOrderLines()) {
                // Retrieve the current stock level for the product
                int currentStock = productRepository.getStockForProduct(line.getProductId());

                // Calculate the new stock level
                int newStock = currentStock - line.getQuantity();

                // Check if there is sufficient stock to fulfill the order
                if (newStock >= 0) {
                    // Update the product's stock in the database
                    productRepository.updateProductStock(line.getProductId(), newStock);
                } else {
                    // If there isn't enough stock, throw an exception
                    throw new Exception("Not enough stock for product ID: " + line.getProductId());
                }
            }
            // If all goes well, commit the transaction
            productRepository.commitTransaction();
        } catch (Exception e) {
            // If there is any exception, rollback the transaction
            productRepository.rollbackTransaction();
            // Rethrow the exception to be handled by the caller
            throw e;
        }
    }
}

