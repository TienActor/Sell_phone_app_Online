package com.example.mobile_app.Model;

import java.io.Serializable;

public class OrderItem implements Serializable {
        private Product product;
        private int quantity;

        public OrderItem(Product product, int quantity) {
                this.product = product;
                this.quantity = quantity;
        }

        public Product getProduct() {
                return product;
        }

        public void setProduct(Product product) {
                this.product = product;
        }

        public int getQuantity() {
                return quantity;
        }

        public void setQuantity(int quantity) {
                this.quantity = quantity;
        }
}
