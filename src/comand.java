import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class comand {

    // =============================
    // Data Produk
    static class Product {
        String name;
        String description;
        int price;
        String imagePath;
        boolean available;

        public Product(String name, String description, int price, String imagePath, boolean available) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.imagePath = imagePath;
            this.available = available;
        }
    }

    static List<Product> productList = new ArrayList<>();
    static List<Product> wishlist = new ArrayList<>();
    static DefaultTableModel cartTableModel;
    static JLabel totalPriceLabel;
    static JTextField searchField;
    static List<Object[]> cartItems = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            initializeProducts();
            new MainApp();
        });
    }

    private static void initializeProducts() {
        productList.add(new Product("Gundam RX-78-2", "Scale 1/144, High Grade", 300000, "src/images/10.jpg", true));
        productList.add(new Product("Gundam Exia", "Scale 1/100, High Grade", 450000, "src/images/9.jpeg", false));
        productList.add(new Product("Gundam Unicorn", "Scale 1/144, High Grade", 700000, "src/images/8.jpg", true));
        productList.add(new Product("Gundam Aerial", "Scale 1/144, High Grade", 700000, "src/images/1.jpg", true));
        productList.add(new Product("Gundam Wing Zero", "Scale 1/144, High Grade", 700000, "src/images/2.jpg", true));
        productList.add(new Product("Gundam NU", "Scale 1/144, High Grade", 700000, "src/images/3.jpg", true));
        productList.add(new Product("Gundam Barbatos", "Scale 1/144, High Grade", 700000, "src/images/4.jpg", true));
        productList.add(new Product("Gundam Destiny", "Scale 1/144, High Grade", 700000, "src/images/5.jpeg", true));
        productList.add(new Product("Gundam Strike Freedom", "Scale 1/144, High Grade", 700000, "src/images/6.jpeg", true));
        productList.add(new Product("Gundam Justice", "Scale 1/144, High Grade", 700000, "src/images/7.jpg", true));
        productList.add(new Product("Gundam Double X", "Scale 1/144, High Grade", 700000, "src/images/11.jpg", true));
        productList.add(new Product("Gundam God", "Scale 1/144, High Grade", 700000, "src/images/12.jpeg", true));
    }

    // =============================
    // Frame Utama
    static class MainApp extends JFrame {
        JPanel productPanel;

        public MainApp() {
            setTitle("Toko Gundam Online");
            setSize(1200, 700);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            // Header Panel (Logo, Search, Wishlist, Cart)
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(new Color(88, 24, 69)); // Warna gradasi modern

            JLabel logoLabel = new JLabel("TokoGundam", JLabel.CENTER);
            logoLabel.setFont(new Font("Poppins", Font.BOLD, 30)); // Font modern
            logoLabel.setForeground(Color.WHITE);

            // Search Bar
            searchField = new JTextField(30);
            JButton searchButton = new JButton("Search");
            searchButton.addActionListener(e -> searchProduct());
            styleButton(searchButton, new Color(255, 87, 51));

            JPanel searchPanel = new JPanel();
            searchPanel.setBackground(new Color(88, 24, 69));
            searchPanel.add(searchField);
            searchPanel.add(searchButton);

            // Wishlist dan Keranjang
            JButton wishlistButton = new JButton("Wishlist");
            styleButton(wishlistButton, new Color(3, 218, 198));
            wishlistButton.addActionListener(e -> showWishlist());

            JButton cartButton = new JButton("Keranjang");
            styleButton(cartButton, new Color(63, 81, 181));
            cartButton.addActionListener(e -> showCart());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(new Color(88, 24, 69));
            buttonPanel.add(wishlistButton);
            buttonPanel.add(cartButton);

            headerPanel.add(logoLabel, BorderLayout.WEST);
            headerPanel.add(searchPanel, BorderLayout.CENTER);
            headerPanel.add(buttonPanel, BorderLayout.EAST);

            add(headerPanel, BorderLayout.NORTH);

            // Product Panel
            productPanel = new JPanel(new GridLayout(0, 3, 10, 10));
            productPanel.setBackground(new Color(255, 239, 213)); // Background panel produk
            JScrollPane scrollPane = new JScrollPane(productPanel);
            displayProducts(productList);
            add(scrollPane, BorderLayout.CENTER);

            // Menu CRUD
            JMenuBar menuBar = new JMenuBar();
            JMenu productMenu = new JMenu("Produk");

            JMenuItem addProduct = new JMenuItem("Tambah Produk");
            addProduct.addActionListener(e -> addProductDialog());
            productMenu.add(addProduct);

            JMenuItem exitApp = new JMenuItem("Keluar");
            exitApp.addActionListener(e -> System.exit(0));
            productMenu.add(exitApp);

            menuBar.add(productMenu);
            setJMenuBar(menuBar);

            setVisible(true);
        }

        private void displayProducts(List<Product> products) {
            productPanel.removeAll();
            for (Product product : products) {
                JPanel productBox = createProductBox(product);
                productPanel.add(productBox);
            }
            productPanel.revalidate();
            productPanel.repaint();
        }

        private JPanel createProductBox(Product product) {
            JPanel box = new JPanel(new BorderLayout());
            box.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            box.setBackground(Color.WHITE);
            box.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    box.setBackground(new Color(240, 240, 240));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    box.setBackground(Color.WHITE);
                }
            });

            // Gambar Produk
            ImageIcon icon;
            try {
                icon = new ImageIcon(product.imagePath);
            } catch (Exception e) {
                icon = new ImageIcon("src/images/default.jpg"); // Gambar default jika tidak ditemukan
            }

            JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
            box.add(imageLabel, BorderLayout.CENTER);

            JLabel nameLabel = new JLabel(product.name, JLabel.CENTER);
            nameLabel.setFont(new Font("Poppins", Font.BOLD, 14));
            JLabel priceLabel = new JLabel("Rp " + product.price, JLabel.CENTER);

            JButton viewDetails = new JButton("Lihat Detail");
            styleButton(viewDetails, new Color(255, 193, 7));
            viewDetails.addActionListener(e -> showProductDetails(product));

            JButton addToWishlist = new JButton("Wishlist");
            styleButton(addToWishlist, new Color(3, 218, 198));
            addToWishlist.addActionListener(e -> {
                addToWishlist(product);
                JOptionPane.showMessageDialog(this, "Produk ditambahkan ke Wishlist.");
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.WHITE);
            buttonPanel.add(viewDetails);
            buttonPanel.add(addToWishlist);

            box.add(nameLabel, BorderLayout.NORTH);
            box.add(priceLabel, BorderLayout.SOUTH);
            box.add(buttonPanel, BorderLayout.SOUTH);

            return box;
        }

        private void styleButton(JButton button, Color color) {
            button.setBackground(color);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setFont(new Font("Poppins", Font.BOLD, 14));
            button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(color.darker());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    button.setBackground(color);
                }
            });
        }

        private void addProductDialog() {
            JDialog addDialog = new JDialog(this, "Tambah Produk", true);
            addDialog.setSize(400, 400);

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

            formPanel.add(new JLabel("Nama Produk:"));
            JTextField nameField = new JTextField();
            formPanel.add(nameField);

            formPanel.add(new JLabel("Deskripsi:"));
            JTextField descriptionField = new JTextField();
            formPanel.add(descriptionField);

            formPanel.add(new JLabel("Harga:"));
            JTextField priceField = new JTextField();
            formPanel.add(priceField);

            formPanel.add(new JLabel("Gambar (Path):"));
            JTextField imagePathField = new JTextField();
            formPanel.add(imagePathField);

            formPanel.add(new JLabel("Tersedia (true/false):"));
            JTextField availableField = new JTextField();
            formPanel.add(availableField);

            JButton saveButton = new JButton("Simpan");
            styleButton(saveButton, new Color(63, 81, 181));
            saveButton.addActionListener(e -> {
                try {
                    String name = nameField.getText();
                    String description = descriptionField.getText();
                    int price = Integer.parseInt(priceField.getText());
                    String imagePath = imagePathField.getText();
                    boolean available = Boolean.parseBoolean(availableField.getText());

                    productList.add(new Product(name, description, price, imagePath, available));
                    displayProducts(productList);
                    addDialog.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Input tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            addDialog.add(formPanel, BorderLayout.CENTER);
            addDialog.add(saveButton, BorderLayout.SOUTH);
            addDialog.setVisible(true);
        }

        private void searchProduct() {
            String keyword = searchField.getText().trim().toLowerCase();
            List<Product> results = new ArrayList<>();
            for (Product p : productList) {
                if (p.name.toLowerCase().contains(keyword)) {
                    results.add(p);
                }
            }
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Produk tidak tersedia.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                displayProducts(results);
            }
        }

        private void addToWishlist(Product product) {
            if (!wishlist.contains(product)) {
                wishlist.add(product);
            }
        }

        private void showWishlist() {
            if (wishlist.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wishlist kosong.", "Info", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JFrame wishlistFrame = new JFrame("Wishlist");
                wishlistFrame.setSize(600, 400);

                DefaultTableModel wishlistModel = new DefaultTableModel(new String[]{"Nama", "Harga", "Deskripsi"}, 0);
                for (Product product : wishlist) {
                    wishlistModel.addRow(new Object[]{product.name, product.price, product.description});
                }

                JTable wishlistTable = new JTable(wishlistModel);
                JScrollPane scrollPane = new JScrollPane(wishlistTable);

                wishlistFrame.add(scrollPane);
                wishlistFrame.setVisible(true);
            }
        }

        private void showCart() {
            JDialog cartDialog = new JDialog(this, "Keranjang Checkout", true);
            cartDialog.setSize(600, 400);

            JTable cartTable = new JTable(cartTableModel);
            JScrollPane scrollPane = new JScrollPane(cartTable);

            JButton checkoutButton = new JButton("Checkout");
            styleButton(checkoutButton, new Color(76, 175, 80));
            checkoutButton.addActionListener(e -> checkout(cartDialog));

            JPanel bottomPanel = new JPanel(new BorderLayout());
            totalPriceLabel = new JLabel("Total: Rp 0", JLabel.CENTER);
            bottomPanel.add(totalPriceLabel, BorderLayout.NORTH);
            bottomPanel.add(checkoutButton, BorderLayout.SOUTH);

            cartDialog.add(scrollPane, BorderLayout.CENTER);
            cartDialog.add(bottomPanel, BorderLayout.SOUTH);
            cartDialog.setVisible(true);
        }

        private void checkout(JDialog parent) {
            JDialog checkoutDialog = new JDialog(this, "Checkout", true);
            checkoutDialog.setSize(400, 400);

            JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

            formPanel.add(new JLabel("Nama:"));
            JTextField nameField = new JTextField();
            formPanel.add(nameField);

            formPanel.add(new JLabel("Alamat:"));
            JTextField addressField = new JTextField();
            formPanel.add(addressField);

            formPanel.add(new JLabel("Kurir:"));
            JComboBox<String> courierBox = new JComboBox<>(new String[]{"JNE", "TIKI", "POS Indonesia"});
            formPanel.add(courierBox);

            formPanel.add(new JLabel("Metode Pembayaran:"));
            JComboBox<String> paymentBox = new JComboBox<>(new String[]{"Transfer Bank", "Kartu Kredit", "E-Wallet"});
            formPanel.add(paymentBox);

            JButton confirmButton = new JButton("Konfirmasi");
            styleButton(confirmButton, new Color(33, 150, 243));
            confirmButton.addActionListener(e -> {
                String name = nameField.getText();
                String address = addressField.getText();
                String courier = (String) courierBox.getSelectedItem();
                String payment = (String) paymentBox.getSelectedItem();

                int total = calculateTotal(payment);
                JOptionPane.showMessageDialog(this, "Pesanan berhasil! Total: Rp " + total);
                checkoutDialog.dispose();
                parent.dispose();
            });

            checkoutDialog.add(formPanel, BorderLayout.CENTER);
            checkoutDialog.add(confirmButton, BorderLayout.SOUTH);
            checkoutDialog.setVisible(true);
        }

        private int calculateTotal(String paymentMethod) {
            int total = 0;
            int tax = 0;
            int shippingCost = 20000; // Flat rate ongkir

            for (Object[] item : cartItems) {
                total += (int) item[1];
            }

            if (paymentMethod.equals("Kartu Kredit")) {
                tax = (int) (total * 0.1); // 10% pajak untuk kartu kredit
            }

            return total + tax + shippingCost;
        }

        private void showProductDetails(Product product) {
            JDialog detailsDialog = new JDialog(this, "Detail Produk", true);
            detailsDialog.setSize(400, 500);

            JPanel panel = new JPanel(new BorderLayout());

            JLabel imageLabel = new JLabel(new ImageIcon(new ImageIcon(product.imagePath).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
            JLabel nameLabel = new JLabel(product.name, JLabel.CENTER);
            nameLabel.setFont(new Font("Poppins", Font.BOLD, 16));

            JLabel descriptionLabel = new JLabel("Deskripsi: " + product.description, JLabel.CENTER);
            JLabel priceLabel = new JLabel("Harga: Rp " + product.price, JLabel.CENTER);

            JButton addToWishlist = new JButton("Wishlist");
            styleButton(addToWishlist, new Color(3, 218, 198));
            addToWishlist.addActionListener(e -> addToWishlist(product));

            JButton addToCart = new JButton("Tambah ke Keranjang");
            styleButton(addToCart, new Color(255, 193, 7));
            addToCart.addActionListener(e -> {
                addToCart(product);
                detailsDialog.dispose();
            });

            panel.add(imageLabel, BorderLayout.NORTH);
            panel.add(nameLabel, BorderLayout.CENTER);

            JPanel infoPanel = new JPanel(new GridLayout(0, 1));
            infoPanel.add(descriptionLabel);
            infoPanel.add(priceLabel);
            infoPanel.add(addToCart);
            infoPanel.add(addToWishlist);

            panel.add(infoPanel, BorderLayout.SOUTH);

            detailsDialog.add(panel);
            detailsDialog.setVisible(true);
        }

        private void addToCart(Product product) {
            if (cartTableModel == null) {
                cartTableModel = new DefaultTableModel(new String[]{"Nama", "Harga", "Jumlah"}, 0);
            }
            cartTableModel.addRow(new Object[]{product.name, product.price, 1});
            cartItems.add(new Object[]{product.name, product.price, 1});
            JOptionPane.showMessageDialog(this, "Ditambahkan ke Keranjang!");
        }
    }
}