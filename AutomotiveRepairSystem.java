
package AutomotiveRepairSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AutomotiveRepairSystem extends Application {

    private static final String CSS_STYLES = """
        .root {
            -fx-background-color: aqua;
        }
        .header {
            -fx-font-size: 24px;
            -fx-font-weight: bold;
            -fx-text-fill: #2c3e50;
            -fx-padding: 10 0 20 0;
        }
        .table-view {
            -fx-background-color: white;
            -fx-border-color: #bdc3c7;
            -fx-border-radius: 5;
        }
        .table-row-cell:odd {
            -fx-background-color: #f9f9f9;
        }
        .table-row-cell:selected {
            -fx-background-color: #3498db;
            -fx-text-fill: white;
        }
        .low-stock-row {
            -fx-background-color: #F8D7DA; /* Light Red */
        }
        .low-stock-row:odd {
            -fx-background-color: #F8D7DA; /* Consistent light red for odd rows too */
        }
        .low-stock-row:selected {
            -fx-background-color: #e74c3c; /* Darker red on selection */
            -fx-text-fill: white;
        }
        .card {
            -fx-background-color: green;
            -fx-background-radius: 10;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 0);
            -fx-padding: 20;
        }
        .login-container {
            -fx-background-color: buleviolet;
            -fx-background-radius: 15;
            -fx-padding: 30;
        }
        .dashboard-card {
            -fx-background-color: linear-gradient(to bottom right, #3498db, #2c3e50);
            -fx-background-radius: 10;
            -fx-padding: 15;
            -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0);
        }
        .dashboard-card-title {
            -fx-text-fill: white;
            -fx-font-size: 14px;
        }
        .dashboard-card-value {
            -fx-text-fill: white;
            -fx-font-size: 24px;
            -fx-font-weight: bold;
        }
        .low-stock-warning {
            -fx-text-fill: #c61313;
            -fx-font-weight: bold;
            -fx-font-size: 14px;
        }
        .in-stock {
            -fx-text-fill: #2ecc71;
            -fx-font-weight: bold;
            -fx-font-size: 14px;
        }
        .tab-pane {
            -fx-tab-min-width: 120px;
        }
        .tab {
            -fx-background-color: #ecf0f1;
            -fx-background-radius: 5 5 0 0;
        }
        .tab:selected {
            -fx-background-color: #3498db;
            -fx-text-fill: white;
        }
        .toolbar {
            -fx-background-color: #ecf0f1;
            -fx-padding: 10;
            -fx-spacing: 10;
            -fx-background-radius: 0 0 5 5;
        }
        .search-field {
            -fx-prompt-text-fill: #95a5a6;
            -fx-background-radius: 5;
            -fx-border-color: #bdc3c7;
            -fx-border-radius: 5;
        }
        .error-label {
            -fx-text-fill: #e74c3c;
            -fx-font-size: 12px;
        }
    """;

    // Data Models
    public static class User {
        private final int id;
        private final String username;
        private final String password;
        private final String role;
        private final String fullName;

        public User(int id, String username, String password, String role, String fullName) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.role = role;
            this.fullName = fullName;
        }

        public int getId() { return id; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public String getRole() { return role; }
        public String getFullName() { return fullName; }
    }

    public static class Customer {
        private final int id;
        private String name;
        private String phone;
        private String email;
        private String address;

        public Customer(int id, String name, String phone, String email, String address) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.address = address;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getEmail() { return email; }
        public String getAddress() { return address; }

        public void setName(String name) { this.name = name; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setEmail(String email) { this.email = email; }
        public void setAddress(String address) { this.address = address; }
    }

    public static class Vehicle {
        private final int id;
        private int customerId;
        private String licensePlate;
        private String make;
        private String model;
        private int year;
        private String vin;
        private int mileage;

        public Vehicle(int id, int customerId, String licensePlate, String make,
                       String model, int year, String vin, int mileage) {
            this.id = id;
            this.customerId = customerId;
            this.licensePlate = licensePlate;
            this.make = make;
            this.model = model;
            this.year = year;
            this.vin = vin;
            this.mileage = mileage;
        }

        public int getId() { return id; }
        public int getCustomerId() { return customerId; }
        public String getLicensePlate() { return licensePlate; }
        public String getMake() { return make; }
        public String getModel() { return model; }
        public int getYear() { return year; }
        public String getVin() { return vin; }
        public int getMileage() { return mileage; }

        public void setCustomerId(int customerId) { this.customerId = customerId; }
        public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
        public void setMake(String make) { this.make = make; }
        public void setModel(String model) { this.model = model; }
        public void setYear(int year) { this.year = year; }
        public void setVin(String vin) { this.vin = vin; }
        public void setMileage(int mileage) { this.mileage = mileage; }
    }

    public static class Repair {
        private final int id;
        private int vehicleId;
        private int mechanicId;
        private String description;
        private String status;
        private LocalDate startDate;
        private LocalDate endDate;
        private double laborCost;
        private String paymentStatus;
        private List<PartUsage> partsUsed;

        public Repair(int id, int vehicleId, int mechanicId, String description,
                      String status, LocalDate startDate, LocalDate endDate,
                      double laborCost, String paymentStatus, List<PartUsage> partsUsed) {
            this.id = id;
            this.vehicleId = vehicleId;
            this.mechanicId = mechanicId;
            this.description = description;
            this.status = status;
            this.startDate = startDate;
            this.endDate = endDate;
            this.laborCost = laborCost;
            this.paymentStatus = paymentStatus;
            this.partsUsed = partsUsed != null ? partsUsed : new ArrayList<>();
        }

        public int getId() { return id; }
        public int getVehicleId() { return vehicleId; }
        public int getMechanicId() { return mechanicId; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
        public double getLaborCost() { return laborCost; }
        public String getPaymentStatus() { return paymentStatus; }
        public List<PartUsage> getPartsUsed() { return partsUsed; }
        public double getTotalCost() {
            double partsCost = partsUsed.stream().mapToDouble(pu -> pu.getQuantity() * pu.getPart().getPrice()).sum();
            return laborCost + partsCost;
        }

        public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
        public void setMechanicId(int mechanicId) { this.mechanicId = mechanicId; }
        public void setDescription(String description) { this.description = description; }
        public void setStatus(String status) { this.status = status; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public void setLaborCost(double laborCost) { this.laborCost = laborCost; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
        public void setPartsUsed(List<PartUsage> partsUsed) { this.partsUsed = partsUsed; }
    }

    public static class PartUsage {
        private InventoryItem part;
        private int quantity;

        public PartUsage(InventoryItem part, int quantity) {
            this.part = part;
            this.quantity = quantity;
        }

        public InventoryItem getPart() { return part; }
        public int getQuantity() { return quantity; }
        public double getSubtotal() { return part.getPrice() * quantity; }

        public void setPart(InventoryItem part) { this.part = part; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }

    public static class InventoryItem {
        private final int id;
        private String name;
        private String category;
        private final SimpleIntegerProperty quantity;
        private double price;
        private String barcode;
        private int reorderThreshold;

        public InventoryItem(int id, String name, String category, int quantity,
                             double price, String barcode, int reorderThreshold) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.quantity = new SimpleIntegerProperty(quantity);
            this.price = price;
            this.barcode = barcode;
            this.reorderThreshold = reorderThreshold;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public int getQuantity() { return quantity.get(); }
        public SimpleIntegerProperty quantityProperty() { return quantity; }
        public double getPrice() { return price; }
        public String getBarcode() { return barcode; }
        public int getReorderThreshold() { return reorderThreshold; }
        public boolean needsReorder() { return getQuantity() <= reorderThreshold; }

        public void setName(String name) { this.name = name; }
        public void setCategory(String category) { this.category = category; }
        public void setQuantity(int quantity) { this.quantity.set(quantity); }
        public void setPrice(double price) { this.price = price; }
        public void setBarcode(String barcode) { this.barcode = barcode; }
        public void setReorderThreshold(int reorderThreshold) { this.reorderThreshold = reorderThreshold; }

        @Override
        public String toString() {
            return name + " (Stock: " + getQuantity() + ")";
        }
    }

    public static class Appointment {
        private final int id;
        private int customerId;
        private int vehicleId;
        private LocalDate date;
        private String timeSlot;
        private String serviceType;
        private String status;

        public Appointment(int id, int customerId, int vehicleId, LocalDate date,
                           String timeSlot, String serviceType, String status) {
            this.id = id;
            this.customerId = customerId;
            this.vehicleId = vehicleId;
            this.date = date;
            this.timeSlot = timeSlot;
            this.serviceType = serviceType;
            this.status = status;
        }

        public int getId() { return id; }
        public int getCustomerId() { return customerId; }
        public int getVehicleId() { return vehicleId; }
        public LocalDate getDate() { return date; }
        public String getTimeSlot() { return timeSlot; }
        public String getServiceType() { return serviceType; }
        public String getStatus() { return status; }

        public void setCustomerId(int customerId) { this.customerId = customerId; }
        public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
        public void setDate(LocalDate date) { this.date = date; }
        public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }
        public void setServiceType(String serviceType) { this.serviceType = serviceType; }
        public void setStatus(String status) { this.status = status; }
    }

    // In-memory data storage
    private static class DataStore {
        private static int customerIdCounter = 4;
        private static int vehicleIdCounter = 4;
        private static int repairIdCounter = 3;
        private static int inventoryIdCounter = 5;
        private static int appointmentIdCounter = 3;
        private static int userIdCounter = 3;

        public static final ObservableList<User> users = FXCollections.observableArrayList(
            new User(1, "admin", "AdminPass1", "Admin", "Behailu Yifru"),
            new User(2, "mechanic1", "MechPass1", "Mechanic", "Dawit Mulugeta"),
            new User(3, "reception1", "RecepPass1", "Receptionist", "Sara Getahun")
        );

        public static final ObservableList<Customer> customers = FXCollections.observableArrayList(
            new Customer(1, "Bereket S/Mariam", "+251956743456", "bereket@gmail.com", "Debresina"),
            new Customer(2, "Eyob Begashawu", "+251988746570", "eyob@gmail.com", "Sheno"),
            new Customer(3, "Werku Ayitenewu", "+251976465611", "werku@gmail.com", "Gojam"),
            new Customer(4, "Tsion Alemu", "+251789475444", "tsion@gmail.com", "Addis abeba")
        );

        public static final ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(
            new Vehicle(1, 1, "Et123", "Toyota", "Camry", 2018, "JT2BF22K6W0123456", 45000),
            new Vehicle(2, 2, "AM789", "Honda", "Accord", 2020, "1HGCM82633A123456", 22000),
            new Vehicle(3, 3, "AM456", "Ford", "F-150", 2019, "1FTFW1ET0KFA12345", 35000),
            new Vehicle(4, 4, "Et789", "Chevrolet", "Malibu", 2021, "1G1ZD5ST6MF123456", 15000)
        );

        public static final ObservableList<InventoryItem> inventory = FXCollections.observableArrayList(
            new InventoryItem(1, "Oil Filter", "Filters", 15, 750.00, "OF12345", 5),
            new InventoryItem(2, "Brake Pads", "Brakes", 8, 2500.00, "BP67890", 3),
            new InventoryItem(3, "Air Filter", "Filters", 3, 900.00, "AF24680", 5),
            new InventoryItem(4, "Spark Plug", "Engine", 20, 450.00, "SP13579", 10),
            new InventoryItem(5, "Battery", "Electrical", 2, 5000.00, "BT78901", 2)
        );

        public static final ObservableList<Repair> repairs = FXCollections.observableArrayList(
            new Repair(1, 1, 2, "Oil change and tire rotation", "Completed",
                LocalDate.now().minusDays(5), LocalDate.now().minusDays(3), 1500.00, "Paid",
                new ArrayList<>(List.of(new PartUsage(inventory.get(0), 1), new PartUsage(inventory.get(2), 1)))),
            new Repair(2, 2, 2, "Brake pad replacement", "In Progress",
                LocalDate.now().minusDays(2), null, 2000.00, "Unpaid",
                new ArrayList<>(List.of(new PartUsage(inventory.get(1), 1), new PartUsage(inventory.get(3), 4)))),
            new Repair(3, 3, 2, "Battery replacement", "Pending",
                LocalDate.now().plusDays(1), null, 1000.00, "Unpaid",
                new ArrayList<>(List.of(new PartUsage(inventory.get(4), 1))))
        );

        public static final ObservableList<Appointment> appointments = FXCollections.observableArrayList(
            new Appointment(1, 1, 1, LocalDate.now().plusDays(2), "09:00-11:00", "Oil Change", "Scheduled"),
            new Appointment(2, 2, 2, LocalDate.now().plusDays(3), "13:00-15:00", "Brake Inspection", "Confirmed"),
            new Appointment(3, 3, 3, LocalDate.now().plusDays(1), "10:00-12:00", "Battery Check", "Pending")
        );

        public static int getNextCustomerId() { return ++customerIdCounter; }
        public static int getNextVehicleId() { return ++vehicleIdCounter; }
        public static int getNextRepairId() { return ++repairIdCounter; }
        public static int getNextInventoryId() { return ++inventoryIdCounter; }
        public static int getNextAppointmentId() { return ++appointmentIdCounter; }
        public static int getNextUserId() { return ++userIdCounter; }
    }

    // Current user session
    private User currentUser;
    private Customer currentCustomer; // For customer logins
    private Map<String, Image> backgroundImages = new HashMap<>();
    private Stage primaryStageRef;

    private static final Pattern PHONE_PATTERN = Pattern.compile("^(\\+251[79]\\d{8}|0[79]\\d{8})$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
    private static final Pattern VIN_PATTERN = Pattern.compile("^[a-zA-Z0-9]{17}$");


    @Override
    public void start(Stage primaryStage) {
        this.primaryStageRef = primaryStage;
        loadBackgroundImages();

        Scene scene = new Scene(createLoginScreen(), 1000, 650);
        scene.getStylesheets().add("data:text/css;base64," + Base64.getEncoder().encodeToString(CSS_STYLES.getBytes()));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Auto Repair Shop Management System");
        primaryStage.show();
    }

    private void loadBackgroundImages() {
        try {
            backgroundImages.put("skykings", new Image("file:skykings.jpg"));
            backgroundImages.put("kingstare", new Image("file:kingstare.jpg"));
            backgroundImages.put("wideuse", new Image("file:wideuse.jpg"));
            backgroundImages.put("extrause", new Image("file:extrause.jpg"));
            backgroundImages.put("enterthis", new Image("file:enterthis.jpg"));
        } catch (Exception e) {
            System.err.println("Error loading background images: " + e.getMessage());
        }
    }

    private ImageView getRandomBackground() {
        if (backgroundImages.isEmpty()) {
            return null;
        }
        List<String> keys = new ArrayList<>(backgroundImages.keySet());
        String randomKey = keys.get(new Random().nextInt(keys.size()));
        Image image = backgroundImages.get(randomKey);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1000);
        imageView.setFitHeight(650);
        imageView.setOpacity(0.8);
        return imageView;
    }

    private BorderPane createLoginScreen() {
        BorderPane root = new BorderPane();

        ImageView background = getRandomBackground();
        if (background != null) {
            root.getChildren().add(background);
        } else {
            root.setStyle("-fx-background-color: linear-gradient(to bottom right, #3498db, #2c3e50);");
        }

        VBox loginForm = new VBox(20);
        loginForm.setAlignment(Pos.CENTER);
        loginForm.getStyleClass().add("login-container");
        loginForm.setMaxWidth(400);

        Label title = new Label("Auto Repair Shop");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username / Email for Customer");
        usernameField.getStyleClass().add("search-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password / Phone # for Customer");
        passwordField.getStyleClass().add("search-field");

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Admin", "Mechanic", "Receptionist", "Customer");
        roleCombo.setPromptText("Select Role");
        roleCombo.getStyleClass().add("search-field");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        loginButton.setDefaultButton(true);

        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");

        VBox formFields = new VBox(10);
        formFields.getChildren().addAll(
            new Label("Username/Email:"), usernameField,
            new Label("Password/Phone:"), passwordField,
            new Label("Role:"), roleCombo,
            loginButton, errorLabel
        );

        loginForm.getChildren().addAll(title, formFields);
        root.setCenter(loginForm);

        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleCombo.getValue();

            if (username.isEmpty() || password.isEmpty() || role == null) {
                errorLabel.setText("All fields are required");
                return;
            }

            if ("Customer".equals(role)) {
                Optional<Customer> matchedCustomer = DataStore.customers.stream()
                    .filter(c -> c.getEmail().equalsIgnoreCase(username))
                    .findFirst();

                if (matchedCustomer.isPresent()) {
                    Customer customer = matchedCustomer.get();
                    if (customer.getPhone().equals(password)) {
                        this.currentCustomer = customer;
                        this.currentUser = new User(customer.getId(), customer.getEmail(), "", "Customer", customer.getName());
                        Stage stage = (Stage) loginButton.getScene().getWindow();
                        refreshDashboard(stage);
                    } else {
                        errorLabel.setText("Invalid credentials. Use your full phone number as password.");
                    }
                } else {
                    errorLabel.setText("Invalid credentials or role mismatch");
                }
            } else {
                Optional<User> matchedUser = DataStore.users.stream()
                    .filter(u -> u.getUsername().equals(username) &&
                        u.getPassword().equals(password) &&
                        u.getRole().equals(role))
                    .findFirst();

                if (matchedUser.isPresent()) {
                    currentUser = matchedUser.get();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    refreshDashboard(stage);
                } else {
                    errorLabel.setText("Invalid credentials or role mismatch");
                }
            }
        });
        return root;
    }

    private void refreshDashboard(Stage stage) {
        if (currentUser == null) {
            stage.setScene(new Scene(createLoginScreen(), 1000, 650));
            return;
        }

        switch (currentUser.getRole()) {
            case "Admin":
                stage.setScene(new Scene(createAdminDashboard(), 1200, 800));
                break;
            case "Mechanic":
                stage.setScene(new Scene(createMechanicDashboard(), 1200, 800));
                break;
            case "Receptionist":
                stage.setScene(new Scene(createReceptionistDashboard(), 1200, 800));
                break;
            case "Customer":
                stage.setScene(new Scene(createCustomerDashboard(currentCustomer), 1200, 800));
                break;
        }
        stage.setTitle(currentUser.getRole() + " Dashboard - " + currentUser.getFullName());
    }

    private BorderPane createCustomerDashboard(Customer customer) {
        BorderPane root = new BorderPane();
        root.setTop(createTopBar());

        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.TOP_CENTER);
        
        Label title = new Label("Your Vehicle and Repair Status");
        title.getStyleClass().add("header");
        
        Label vehiclesLabel = new Label("Your Vehicles");
        vehiclesLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ObservableList<Vehicle> myVehicles = DataStore.vehicles.stream()
            .filter(v -> v.getCustomerId() == customer.getId())
            .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        
        VBox vehiclesVBox = new VBox(10);
        if (myVehicles.isEmpty()) {
            vehiclesVBox.getChildren().add(new Label("You have no registered vehicles."));
        } else {
            for (Vehicle vehicle : myVehicles) {
                vehiclesVBox.getChildren().add(createVehicleCard(vehicle));
            }
        }
        
        Label repairsLabel = new Label("Your Repair History");
        repairsLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        List<Integer> myVehicleIds = myVehicles.stream().map(Vehicle::getId).collect(Collectors.toList());
        
        ObservableList<Repair> myRepairs = DataStore.repairs.stream()
            .filter(r -> myVehicleIds.contains(r.getVehicleId()))
            .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
            
        TableView<Repair> repairsTable = new TableView<>(myRepairs);
        repairsTable.getStyleClass().add("table-view");

        TableColumn<Repair, Integer> repairIdCol = new TableColumn<>("Repair ID");
        repairIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Repair, String> repairDescCol = new TableColumn<>("Description");
        repairDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Repair, String> repairStatusCol = new TableColumn<>("Status");
        repairStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Repair, LocalDate> startDateCol = new TableColumn<>("Start Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Repair, LocalDate> endDateCol = new TableColumn<>("End Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        
        TableColumn<Repair, Double> totalCostCol = new TableColumn<>("Total Cost (ETB)");
        totalCostCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalCost()).asObject());
        totalCostCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("ETB %,.2f", price));
                }
            }
        });
        
        repairsTable.getColumns().addAll(repairIdCol, repairDescCol, repairStatusCol, startDateCol, endDateCol, totalCostCol);
        
        ScrollPane scrollPane = new ScrollPane(new VBox(20, title, vehiclesLabel, vehiclesVBox, repairsLabel, repairsTable));
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(10));
        
        root.setCenter(scrollPane);
        
        return root;
    }

    private Node createVehicleCard(Vehicle vehicle) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(5);
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: #f0f8ff; -fx-border-color: #d3d3d3; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;");

        grid.add(new Label("License Plate:"), 0, 0);
        Label plateLabel = new Label(vehicle.getLicensePlate());
        plateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        grid.add(plateLabel, 1, 0);

        grid.add(new Label("Make & Model:"), 0, 1);
        grid.add(new Label(vehicle.getMake() + " " + vehicle.getModel()), 1, 1);

        grid.add(new Label("Year:"), 0, 2);
        grid.add(new Label(String.valueOf(vehicle.getYear())), 1, 2);
        return grid;
    }

    private BorderPane createAdminDashboard() {
        BorderPane root = new BorderPane();
        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("tab-pane");

        Tab dashboardTab = new Tab("Dashboard");
        dashboardTab.setContent(createAdminDashboardView());
        dashboardTab.setClosable(false);
        Tab customersTab = new Tab("Customers");
        customersTab.setContent(createCustomersView());
        customersTab.setClosable(false);
        Tab vehiclesTab = new Tab("Vehicles");
        vehiclesTab.setContent(createVehiclesView());
        vehiclesTab.setClosable(false);
        Tab repairsTab = new Tab("Repairs");
        repairsTab.setContent(createRepairsView());
        repairsTab.setClosable(false);
        Tab inventoryTab = new Tab("Inventory");
        inventoryTab.setContent(createInventoryView());
        inventoryTab.setClosable(false);
        Tab appointmentsTab = new Tab("Appointments");
        appointmentsTab.setContent(createAppointmentsView());
        appointmentsTab.setClosable(false);
        Tab reportsTab = new Tab("Reports");
        reportsTab.setContent(createReportsView());
        reportsTab.setClosable(false);
        Tab usersTab = new Tab("Users");
        usersTab.setContent(createUsersView());
        usersTab.setClosable(false);

        tabs.getTabs().addAll(dashboardTab, customersTab, vehiclesTab, repairsTab,
            inventoryTab, appointmentsTab, reportsTab, usersTab);
        root.setCenter(tabs);
        root.setTop(createTopBar());
        root.setBottom(createStatusBar());

        return root;
    }

    private BorderPane createMechanicDashboard() {
        BorderPane root = new BorderPane();
        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("tab-pane");

        Tab dashboardTab = new Tab("Dashboard");
        dashboardTab.setContent(createMechanicDashboardView());
        dashboardTab.setClosable(false);
        Tab repairsTab = new Tab("My Repairs");
        repairsTab.setContent(createMechanicRepairsView());
        repairsTab.setClosable(false);
        Tab inventoryTab = new Tab("Inventory");
        inventoryTab.setContent(createInventoryView()); // Will be made read-only inside the method
        inventoryTab.setClosable(false);

        tabs.getTabs().addAll(dashboardTab, repairsTab, inventoryTab);
        root.setCenter(tabs);
        root.setTop(createTopBar());
        root.setBottom(createStatusBar());

        return root;
    }

    private BorderPane createReceptionistDashboard() {
        BorderPane root = new BorderPane();
        TabPane tabs = new TabPane();
        tabs.getStyleClass().add("tab-pane");

        Tab dashboardTab = new Tab("Dashboard");
        dashboardTab.setContent(createReceptionistDashboardView());
        dashboardTab.setClosable(false);
        Tab customersTab = new Tab("Customers");
        customersTab.setContent(createCustomersView());
        customersTab.setClosable(false);
        Tab vehiclesTab = new Tab("Vehicles");
        vehiclesTab.setContent(createVehiclesView());
        vehiclesTab.setClosable(false);
        Tab repairsTab = new Tab("Repairs");
        repairsTab.setContent(createRepairsView()); // Will have restricted controls
        repairsTab.setClosable(false);
        Tab appointmentsTab = new Tab("Appointments");
        appointmentsTab.setContent(createAppointmentsView());
        appointmentsTab.setClosable(false);

        tabs.getTabs().addAll(dashboardTab, customersTab, vehiclesTab, repairsTab, appointmentsTab);
        root.setCenter(tabs);
        root.setTop(createTopBar());
        root.setBottom(createStatusBar());

        return root;
    }

    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setPadding(new Insets(15));
        topBar.setStyle("-fx-background-color: #2c3e50;");

        Label welcomeLabel = new Label("Welcome, " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        welcomeLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button refreshButton = new Button("Refresh");
        refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        refreshButton.setOnMouseEntered(e -> refreshButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        refreshButton.setOnMouseExited(e -> refreshButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        refreshButton.setOnAction(e -> {
            refreshDashboard(primaryStageRef);
            showAlert("Refresh", "Dashboard has been refreshed.");
        });
        if ("Customer".equals(currentUser.getRole())) {
            refreshButton.setVisible(false);
        }

        Button logoutButton = new Button("Logout");
        logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        logoutButton.setOnMouseEntered(e -> logoutButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        logoutButton.setOnMouseExited(e -> logoutButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        logoutButton.setOnAction(e -> {
            currentUser = null;
            currentCustomer = null;
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(createLoginScreen(), 1000, 650));
            stage.setTitle("Auto Repair Shop Management System");
        });

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(welcomeLabel, spacer, refreshButton, logoutButton);
        return topBar;
    }

    private VBox createAdminDashboardView() {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new Insets(20));
        Label title = new Label("Admin Dashboard Overview");
        title.getStyleClass().add("header");

        // --- Row 1: Operational Stats ---
        HBox summaryCards = new HBox(15);
        summaryCards.setAlignment(Pos.CENTER);
        VBox customersCard = createDashboardCard("Active Customers", String.valueOf(DataStore.customers.size()), "#3498db", "kingstare");
        VBox repairsCard = createDashboardCard("Active Repairs",
            String.valueOf(DataStore.repairs.stream().filter(r -> !r.getStatus().equals("Completed")).count()), "#e74c3c", "wideuse");
        VBox inventoryCard = createDashboardCard("Low Stock Items",
            String.valueOf(DataStore.inventory.stream().filter(InventoryItem::needsReorder).count()), "#f39c12", "extrause");
        VBox appointmentsCard = createDashboardCard("Today's Appointments",
            String.valueOf(DataStore.appointments.stream().filter(a -> a.getDate().equals(LocalDate.now())).count()), "#2ecc71", "enterthis");
        summaryCards.getChildren().addAll(customersCard, repairsCard, inventoryCard, appointmentsCard);

        // --- Row 2: Financial & Staff Stats (NEW) ---
        HBox financialAndStaffCards = new HBox(15);
        financialAndStaffCards.setAlignment(Pos.CENTER);

        // Calculations for new cards
        long numMechanics = DataStore.users.stream().filter(u -> "Mechanic".equals(u.getRole())).count();
        long numReceptionists = DataStore.users.stream().filter(u -> "Receptionist".equals(u.getRole())).count();
        
        double totalPartsCost = DataStore.repairs.stream()
            .flatMap(r -> r.getPartsUsed().stream())
            .mapToDouble(pu -> pu.getQuantity() * pu.getPart().getPrice())
            .sum();

        double totalRevenueFromPaid = DataStore.repairs.stream()
            .filter(r -> "Paid".equals(r.getPaymentStatus()))
            .mapToDouble(Repair::getTotalCost)
            .sum();
        double costOfPartsInPaid = DataStore.repairs.stream()
            .filter(r -> "Paid".equals(r.getPaymentStatus()))
            .flatMap(r -> r.getPartsUsed().stream())
            .mapToDouble(pu -> pu.getQuantity() * pu.getPart().getPrice())
            .sum();
        double netIncome = totalRevenueFromPaid - costOfPartsInPaid;

        VBox mechanicsCard = createDashboardCard("Mechanics On Staff", String.valueOf(numMechanics), "#9b59b6", "skykings");
        VBox receptionistsCard = createDashboardCard("Receptionists On Staff", String.valueOf(numReceptionists), "#1abc9c", "kingstare");
        VBox totalCostCard = createDashboardCard("Total Cost (Parts Used)", String.format("ETB %,.2f", totalPartsCost), "#e67e22", "wideuse");
        VBox netIncomeCard = createDashboardCard("Net Income (from Paid)", String.format("ETB %,.2f", netIncome), "#27ae60", "extrause");
        
        financialAndStaffCards.getChildren().addAll(mechanicsCard, receptionistsCard, totalCostCard, netIncomeCard);

        // --- Charts ---
        HBox charts = new HBox(15);
        charts.setAlignment(Pos.CENTER);
        
        // Repair Status Chart
        VBox statusChartBox = new VBox(5);
        statusChartBox.setAlignment(Pos.CENTER);
        Label statusLabel = new Label("Repair Status");
        statusLabel.setStyle("-fx-font-weight: bold;");
        PieChart statusChart = createRepairStatusChart();
        statusChartBox.getChildren().addAll(statusLabel, statusChart);
        
        // Inventory Status Chart
        VBox inventoryChartBox = new VBox(5);
        inventoryChartBox.setAlignment(Pos.CENTER);
        Label inventoryLabel = new Label("Inventory Status");
        inventoryLabel.setStyle("-fx-font-weight: bold;");
        PieChart inventoryChart = createInventoryChart();
        inventoryChartBox.getChildren().addAll(inventoryLabel, inventoryChart);
        
        charts.getChildren().addAll(statusChartBox, inventoryChartBox);
        
        dashboard.getChildren().addAll(title, summaryCards, financialAndStaffCards, charts);
        return dashboard;
    }

    private VBox createDashboardCard(String title, String valueText, String color, String bgImage) {
        StackPane cardStack = new StackPane();
        VBox cardContent = new VBox(10);
        cardContent.setAlignment(Pos.CENTER);
        cardContent.setPadding(new Insets(15));
        cardContent.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 10;");
        cardContent.setPrefSize(200, 120);

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("dashboard-card-title");
        Label valueLabel = new Label(valueText);
        valueLabel.getStyleClass().add("dashboard-card-value");
        cardContent.getChildren().addAll(titleLabel, valueLabel);
        
        cardStack.getChildren().add(cardContent);
        
        if (backgroundImages.containsKey(bgImage)) {
            ImageView bg = new ImageView(backgroundImages.get(bgImage));
            bg.setFitWidth(200);
            bg.setFitHeight(120);
            bg.setOpacity(0.2);
            bg.setMouseTransparent(true);
            cardStack.getChildren().add(bg);
        }

        // Return a VBox to maintain original method signature and layout compatibility
        VBox wrapper = new VBox(cardStack);
        wrapper.setPrefSize(200, 120);
        return wrapper;
    }

    private VBox createMechanicDashboardView() {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new Insets(20));
        Label title = new Label("Mechanic Dashboard");
        title.getStyleClass().add("header");
        Label assignedLabel = new Label("Your Assigned Repairs");
        assignedLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        ObservableList<Repair> myRepairs = DataStore.repairs.stream()
            .filter(r -> r.getMechanicId() == currentUser.getId())
            .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        TableView<Repair> repairsTable = new TableView<>(myRepairs);
        repairsTable.getStyleClass().add("table-view");
        TableColumn<Repair, Integer> idCol = new TableColumn<>("Repair ID");
        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        TableColumn<Repair, Integer> vehicleCol = new TableColumn<>("Vehicle ID");
        vehicleCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getVehicleId()).asObject());
        TableColumn<Repair, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        TableColumn<Repair, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        TableColumn<Repair, LocalDate> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStartDate()));
        repairsTable.getColumns().addAll(idCol, vehicleCol, descCol, statusCol, startCol);
        Button updateStatusButton = new Button("Update Repair Status");
        updateStatusButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        updateStatusButton.setOnMouseEntered(e -> updateStatusButton.setStyle("-fx-background-color: #d35400; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        updateStatusButton.setOnMouseExited(e -> updateStatusButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        updateStatusButton.setOnAction(e -> {
            Repair selectedRepair = repairsTable.getSelectionModel().getSelectedItem();
            if (selectedRepair == null) {
                showAlert("No Selection", "Please select a repair to update its status.");
                return;
            }
            showUpdateRepairStatusDialog(selectedRepair, repairsTable);
        });
        Label partsLabel = new Label("Parts Needed for Your Repairs");
        partsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        List<PartUsage> partsNeeded = myRepairs.stream()
            .filter(r -> !r.getStatus().equals("Completed"))
            .flatMap(r -> r.getPartsUsed().stream())
            .collect(Collectors.toList());
        TableView<PartUsage> partsTable = new TableView<>(FXCollections.observableArrayList(partsNeeded));
        partsTable.getStyleClass().add("table-view");
        TableColumn<PartUsage, String> partNameCol = new TableColumn<>("Part Name");
        partNameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPart().getName()));
        TableColumn<PartUsage, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()).asObject());
        partsTable.getColumns().addAll(partNameCol, quantityCol);
        dashboard.getChildren().addAll(title, assignedLabel, updateStatusButton, repairsTable, partsLabel, partsTable);
        return dashboard;
    }

    private VBox createMechanicRepairsView() {
        VBox view = new VBox(20);
        view.setPadding(new Insets(20));
        Label title = new Label("My Repairs");
        title.getStyleClass().add("header");
        
        ObservableList<Repair> myRepairs = DataStore.repairs.stream()
            .filter(r -> r.getMechanicId() == currentUser.getId())
            .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        TableView<Repair> repairsTable = new TableView<>(myRepairs);
        repairsTable.getStyleClass().add("table-view");
        
        TableColumn<Repair, Integer> idCol = new TableColumn<>("Repair ID");
        idCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        TableColumn<Repair, Integer> vehicleCol = new TableColumn<>("Vehicle ID");
        vehicleCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getVehicleId()).asObject());
        TableColumn<Repair, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        TableColumn<Repair, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        TableColumn<Repair, LocalDate> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStartDate()));
        TableColumn<Repair, LocalDate> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getEndDate()));
        repairsTable.getColumns().addAll(idCol, vehicleCol, descCol, statusCol, startCol, endCol);
        
        Button updateStatusButton = new Button("Update Status");
        updateStatusButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        updateStatusButton.setOnMouseEntered(e -> updateStatusButton.setStyle("-fx-background-color: #d35400; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        updateStatusButton.setOnMouseExited(e -> updateStatusButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        
        Button editButton = new Button("Edit Repair Details");
        editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        
        updateStatusButton.setOnAction(e -> {
            Repair selectedRepair = repairsTable.getSelectionModel().getSelectedItem();
            if (selectedRepair == null) {
                showAlert("No Selection", "Please select a repair to update its status.");
                return;
            }
            showUpdateRepairStatusDialog(selectedRepair, repairsTable);
        });

        editButton.setOnAction(e -> {
            Repair selected = repairsTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select a repair to edit.");
                return;
            }
            if ("Completed".equals(selected.getStatus()) || "Canceled".equals(selected.getStatus())) {
                showAlert("Action Not Allowed", "You cannot edit a repair that is already 'Completed' or 'Canceled'.");
                return;
            }
            Optional<Repair> result = showRepairDialog(selected);
            result.ifPresent(editedRepair -> {
                selected.setDescription(editedRepair.getDescription());
                selected.setPartsUsed(editedRepair.getPartsUsed());
                repairsTable.refresh();
                showAlert("Success", "Repair details updated.");
            });
        });

        HBox buttonBar = new HBox(10, updateStatusButton, editButton);
        view.getChildren().addAll(title, buttonBar, repairsTable);
        return view;
    }

    private VBox createReceptionistDashboardView() {
        VBox dashboard = new VBox(20);
        dashboard.setPadding(new Insets(20));
        Label title = new Label("Receptionist Dashboard");
        title.getStyleClass().add("header");
        Label todayLabel = new Label("Today's Appointments");
        todayLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        ObservableList<Appointment> todaysAppointments = DataStore.appointments.stream()
            .filter(a -> a.getDate().equals(LocalDate.now()))
            .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        TableView<Appointment> appointmentsTable = new TableView<>(todaysAppointments);
        appointmentsTable.getStyleClass().add("table-view");
        TableColumn<Appointment, Integer> customerCol = new TableColumn<>("Customer ID");
        customerCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCustomerId()).asObject());
        TableColumn<Appointment, String> timeCol = new TableColumn<>("Time Slot");
        timeCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTimeSlot()));
        TableColumn<Appointment, String> serviceCol = new TableColumn<>("Service");
        serviceCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getServiceType()));
        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        appointmentsTable.getColumns().addAll(customerCol, timeCol, serviceCol, statusCol);
        Label recentLabel = new Label("Recent Repairs");
        recentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        ObservableList<Repair> recentRepairs = DataStore.repairs.stream()
            .sorted((r1, r2) -> r2.getStartDate().compareTo(r1.getStartDate()))
            .limit(5)
            .collect(FXCollections::observableArrayList, ObservableList::add, ObservableList::addAll);
        TableView<Repair> repairsTable = new TableView<>(recentRepairs);
        repairsTable.getStyleClass().add("table-view");
        TableColumn<Repair, Integer> vehicleIdCol = new TableColumn<>("Vehicle ID");
        vehicleIdCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getVehicleId()).asObject());
        TableColumn<Repair, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDescription()));
        TableColumn<Repair, String> statusCol2 = new TableColumn<>("Status");
        statusCol2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        repairsTable.getColumns().addAll(vehicleIdCol, descCol, statusCol2);
        dashboard.getChildren().addAll(title, todayLabel, appointmentsTable, recentLabel, repairsTable);
        return dashboard;
    }

    private Node createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5, 12, 5, 12));
        statusBar.setStyle("-fx-background-color: #ecf0f1; -fx-border-color: #bdc3c7; -fx-border-width: 1 0 0 0;");
        
        Label totalCustomersLabel = new Label();
        // Bind the label text to the size of the customer list for automatic updates
        totalCustomersLabel.textProperty().bind(Bindings.concat("Total Customers: ", Bindings.size(DataStore.customers)));
        totalCustomersLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #2c3e50;");
        
        statusBar.getChildren().add(totalCustomersLabel);
        return statusBar;
    }

    private PieChart createRepairStatusChart() {
        long pending = DataStore.repairs.stream().filter(r -> r.getStatus().equals("Pending")).count();
        long inProgress = DataStore.repairs.stream().filter(r -> r.getStatus().equals("In Progress")).count();
        long completed = DataStore.repairs.stream().filter(r -> r.getStatus().equals("Completed")).count();
        PieChart chart = new PieChart();
        chart.setTitle("Repair Status");
        chart.setPrefSize(400, 250);
        chart.getData().add(new PieChart.Data("Pending", pending));
        chart.getData().add(new PieChart.Data("In Progress", inProgress));
        chart.getData().add(new PieChart.Data("Completed", completed));
        return chart;
    }

    private PieChart createInventoryChart() {
        long lowStock = DataStore.inventory.stream().filter(InventoryItem::needsReorder).count();
        long inStock = DataStore.inventory.size() - lowStock;
        PieChart chart = new PieChart();
        chart.setTitle("Inventory Status");
        chart.setPrefSize(400, 250);
        chart.getData().add(new PieChart.Data("Low Stock", lowStock));
        chart.getData().add(new PieChart.Data("In Stock", inStock));
        return chart;
    }

    private BorderPane createCustomersView() {
        BorderPane root = new BorderPane();
        VBox customerListVBox = new VBox(10);
        customerListVBox.setPadding(new Insets(10));

        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");

        TextField searchField = new TextField();
        searchField.setPromptText("Search customers by name, phone, or email...");
        searchField.setPrefWidth(400);
        searchField.getStyleClass().add("search-field");

        Button addButton = new Button("Add Customer");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        
        toolbar.getChildren().addAll(searchField, addButton);
        root.setTop(toolbar);

        ScrollPane scrollPane = new ScrollPane(customerListVBox);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);

        refreshCustomerCards(customerListVBox, DataStore.customers);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            ObservableList<Customer> customersToDisplay;
            if (newVal == null || newVal.isEmpty()) {
                customersToDisplay = DataStore.customers;
            } else {
                ObservableList<Customer> filtered = FXCollections.observableArrayList();
                String lowerKeyword = newVal.toLowerCase();
                for (Customer c : DataStore.customers) {
                    if (c.getName().toLowerCase().contains(lowerKeyword) || c.getPhone().contains(lowerKeyword) || c.getEmail().toLowerCase().contains(lowerKeyword)) {
                        filtered.add(c);
                    }
                }
                customersToDisplay = filtered;
            }
            refreshCustomerCards(customerListVBox, customersToDisplay);
        });

        addButton.setOnAction(e -> {
            Optional<Customer> result = showCustomerDialog(null);
            result.ifPresent(customer -> {
                Customer newCustomer = new Customer(
                    DataStore.getNextCustomerId(),
                    customer.getName(),
                    customer.getPhone(),
                    customer.getEmail(),
                    customer.getAddress()
                );
                DataStore.customers.add(newCustomer);
                searchField.clear(); 
                refreshCustomerCards(customerListVBox, DataStore.customers);
            });
        });

        return root;
    }

    private void refreshCustomerCards(VBox container, ObservableList<Customer> customers) {
        container.getChildren().clear();
        if (customers.isEmpty()) {
            Label emptyLabel = new Label("No customers found.");
            emptyLabel.setStyle("-fx-font-style: italic; -fx-padding: 20;");
            container.getChildren().add(emptyLabel);
        } else {
            for (Customer customer : customers) {
                container.getChildren().add(createCustomerCard(customer));
            }
        }
    }

    private Node createCustomerCard(Customer customer) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.setPadding(new Insets(15));
        grid.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #bdc3c7; -fx-border-width: 1; -fx-background-radius: 5; -fx-border-radius: 5;");

        Label nameLabel = new Label(customer.getName());
        nameLabel.setStyle("-fx-font-weight: bold;");
        Label phoneLabel = new Label(customer.getPhone());
        Label emailLabel = new Label(customer.getEmail());
        Label addressLabel = new Label(customer.getAddress());

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameLabel, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneLabel, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailLabel, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressLabel, 1, 3);

        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        editButton.setOnAction(e -> {
            Optional<Customer> result = showCustomerDialog(customer);
            result.ifPresent(editedData -> {
                customer.setName(editedData.getName());
                customer.setPhone(editedData.getPhone());
                customer.setEmail(editedData.getEmail());
                customer.setAddress(editedData.getAddress());
                
                nameLabel.setText(customer.getName());
                phoneLabel.setText(customer.getPhone());
                emailLabel.setText(customer.getEmail());
                addressLabel.setText(customer.getAddress());
            });
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            boolean hasVehicles = DataStore.vehicles.stream().anyMatch(v -> v.getCustomerId() == customer.getId());
            boolean hasAppointments = DataStore.appointments.stream().anyMatch(a -> a.getCustomerId() == customer.getId());
            if (hasVehicles || hasAppointments) {
                showAlert("Deletion Error", "Cannot delete customer " + customer.getName() + ".\nThey have associated vehicles or appointments. Please remove them first.");
                return;
            }

            if (confirmAction("Delete Customer", "Are you sure you want to delete " + customer.getName() + "?")) {
                DataStore.customers.remove(customer);
                ((VBox) grid.getParent()).getChildren().remove(grid);
            }
        });

        HBox buttonBox = new HBox(10, editButton, deleteButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        grid.add(buttonBox, 1, 4);
        GridPane.setHalignment(buttonBox, HPos.RIGHT);

        return grid;
    }

    private BorderPane createVehiclesView() {
        BorderPane root = new BorderPane();
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");

        TextField searchField = new TextField();
        searchField.setPromptText("Search vehicles...");
        searchField.setPrefWidth(300);
        searchField.getStyleClass().add("search-field");

        Button addButton = new Button("Add Vehicle");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button editButton = new Button("Edit Vehicle");
        editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button deleteButton = new Button("Delete Vehicle");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        toolbar.getChildren().addAll(searchField, addButton, editButton, deleteButton);
        root.setTop(toolbar);

        TableView<Vehicle> table = new TableView<>();
        table.setItems(DataStore.vehicles);
        table.getStyleClass().add("table-view");

        TableColumn<Vehicle, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Vehicle, Integer> customerCol = new TableColumn<>("Customer ID");
        customerCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCustomerId()).asObject());
        TableColumn<Vehicle, String> plateCol = new TableColumn<>("License Plate");
        plateCol.setCellValueFactory(new PropertyValueFactory<>("licensePlate"));
        TableColumn<Vehicle, String> makeCol = new TableColumn<>("Make");
        makeCol.setCellValueFactory(new PropertyValueFactory<>("make"));
        TableColumn<Vehicle, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        TableColumn<Vehicle, Integer> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        TableColumn<Vehicle, String> vinCol = new TableColumn<>("VIN");
        vinCol.setCellValueFactory(new PropertyValueFactory<>("vin"));
        TableColumn<Vehicle, Integer> mileageCol = new TableColumn<>("Mileage");
        mileageCol.setCellValueFactory(new PropertyValueFactory<>("mileage"));
        table.getColumns().addAll(idCol, customerCol, plateCol, makeCol, modelCol, yearCol, vinCol, mileageCol);
        root.setCenter(table);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                table.setItems(DataStore.vehicles);
                return;
            }
            ObservableList<Vehicle> filtered = FXCollections.observableArrayList();
            String lowerKeyword = newVal.toLowerCase();
            for (Vehicle v : DataStore.vehicles) {
                if (v.getLicensePlate().toLowerCase().contains(lowerKeyword) ||
                    v.getMake().toLowerCase().contains(lowerKeyword) ||
                    v.getModel().toLowerCase().contains(lowerKeyword) ||
                    String.valueOf(v.getYear()).contains(lowerKeyword) ||
                    v.getVin().toLowerCase().contains(lowerKeyword)) {
                    filtered.add(v);
                }
            }
            table.setItems(filtered);
        });

        addButton.setOnAction(e -> {
            Optional<Vehicle> result = showVehicleDialog(null);
            result.ifPresent(vehicle -> {
                Vehicle newVehicle = new Vehicle(
                    DataStore.getNextVehicleId(),
                    vehicle.getCustomerId(),
                    vehicle.getLicensePlate(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getVin(),
                    vehicle.getMileage()
                );
                DataStore.vehicles.add(newVehicle);
                table.setItems(DataStore.vehicles);
            });
        });

        editButton.setOnAction(e -> {
            Vehicle selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select a vehicle to edit");
                return;
            }
            Optional<Vehicle> result = showVehicleDialog(selected);
            result.ifPresent(vehicle -> {
                selected.setCustomerId(vehicle.getCustomerId());
                selected.setLicensePlate(vehicle.getLicensePlate());
                selected.setMake(vehicle.getMake());
                selected.setModel(vehicle.getModel());
                selected.setYear(vehicle.getYear());
                selected.setVin(vehicle.getVin());
                selected.setMileage(vehicle.getMileage());
                table.refresh();
            });
        });

        deleteButton.setOnAction(e -> {
            Vehicle selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select a vehicle to delete");
                return;
            }

            boolean hasActiveRepairs = DataStore.repairs.stream()
                .anyMatch(r -> r.getVehicleId() == selected.getId() && "In Progress".equals(r.getStatus()));

            if (hasActiveRepairs) {
                showAlert("Deletion Error", "Cannot delete vehicle " + selected.getLicensePlate() + ".\nIt has an 'In Progress' repair. Please complete or cancel it first.");
                return;
            }

            String confirmationMessage = "Are you sure you want to delete vehicle " + selected.getLicensePlate() + "?\n\n" +
                                       "WARNING: All associated appointments and non-active repairs for this vehicle will also be permanently deleted.";

            if (!confirmAction("Confirm Vehicle Deletion", confirmationMessage)) {
                return;
            }
            
            // Perform cascade delete
            DataStore.appointments.removeIf(a -> a.getVehicleId() == selected.getId());
            DataStore.repairs.removeIf(r -> r.getVehicleId() == selected.getId());
            
            // Delete the vehicle itself
            DataStore.vehicles.remove(selected);
            table.setItems(DataStore.vehicles);
            showAlert("Success", "Vehicle " + selected.getLicensePlate() + " and all its associated records have been deleted.");
        });


        return root;
    }

    private BorderPane createRepairsView() {
        BorderPane root = new BorderPane();
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");

        TextField searchField = new TextField();
        searchField.setPromptText("Search repairs...");
        searchField.setPrefWidth(300);
        searchField.getStyleClass().add("search-field");

        Button addButton = new Button("Add Repair");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button editButton = new Button("Edit Repair");
        editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button deleteButton = new Button("Delete Repair");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button updateStatusButton = new Button("Update Status");
        updateStatusButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        updateStatusButton.setOnMouseEntered(e -> updateStatusButton.setStyle("-fx-background-color: #d35400; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        updateStatusButton.setOnMouseExited(e -> updateStatusButton.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button assignMechanicButton = new Button("Assign Mechanic");
        assignMechanicButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        assignMechanicButton.setOnMouseEntered(e -> assignMechanicButton.setStyle("-fx-background-color: #8e44ad; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        assignMechanicButton.setOnMouseExited(e -> assignMechanicButton.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        toolbar.getChildren().addAll(searchField, addButton, editButton, deleteButton, updateStatusButton, assignMechanicButton);

        // --- Role-Based Access Control ---
        if (!"Admin".equals(currentUser.getRole())) {
            // Only Admins can delete repairs and assign mechanics
            toolbar.getChildren().removeAll(deleteButton, assignMechanicButton);
        }
        root.setTop(toolbar);

        TableView<Repair> table = new TableView<>();
        table.setItems(DataStore.repairs);
        table.getStyleClass().add("table-view");

        TableColumn<Repair, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Repair, Integer> vehicleIdCol = new TableColumn<>("Vehicle ID");
        vehicleIdCol.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        TableColumn<Repair, Integer> mechanicIdCol = new TableColumn<>("Mechanic ID");
        mechanicIdCol.setCellValueFactory(new PropertyValueFactory<>("mechanicId"));
        TableColumn<Repair, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Repair, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        TableColumn<Repair, LocalDate> startDateCol = new TableColumn<>("Start Date");
        startDateCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        TableColumn<Repair, LocalDate> endDateCol = new TableColumn<>("End Date");
        endDateCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        
        TableColumn<Repair, Double> laborCostCol = new TableColumn<>("Labor Cost (ETB)");
        laborCostCol.setCellValueFactory(new PropertyValueFactory<>("laborCost"));
        laborCostCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("ETB %,.2f", price));
                }
            }
        });

        TableColumn<Repair, String> paymentStatusCol = new TableColumn<>("Payment Status");
        paymentStatusCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        
        TableColumn<Repair, Double> totalCostCol = new TableColumn<>("Total Cost (ETB)");
        totalCostCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalCost()).asObject());
        totalCostCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("ETB %,.2f", price));
                }
            }
        });

        table.getColumns().addAll(idCol, vehicleIdCol, mechanicIdCol, descriptionCol, statusCol,
            startDateCol, endDateCol, laborCostCol, paymentStatusCol, totalCostCol);
        root.setCenter(table);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                table.setItems(DataStore.repairs);
                return;
            }
            ObservableList<Repair> filtered = FXCollections.observableArrayList();
            String lowerKeyword = newVal.toLowerCase();
            for (Repair r : DataStore.repairs) {
                if (r.getDescription().toLowerCase().contains(lowerKeyword) ||
                    r.getStatus().toLowerCase().contains(lowerKeyword) ||
                    DataStore.users.stream().filter(u -> u.getId() == r.getMechanicId()).findFirst().map(u -> u.getFullName().toLowerCase().contains(lowerKeyword)).orElse(false) ||
                    DataStore.vehicles.stream().filter(v -> v.getId() == r.getVehicleId()).findFirst().map(v -> v.getLicensePlate().toLowerCase().contains(lowerKeyword)).orElse(false)) {
                    filtered.add(r);
                }
            }
            table.setItems(filtered);
        });

        addButton.setOnAction(e -> {
            Optional<Repair> result = showRepairDialog(null);
            result.ifPresent(repair -> {
                Repair newRepair = new Repair(
                    DataStore.getNextRepairId(),
                    repair.getVehicleId(),
                    repair.getMechanicId(),
                    repair.getDescription(),
                    repair.getStatus(),
                    repair.getStartDate(),
                    repair.getEndDate(),
                    repair.getLaborCost(),
                    repair.getPaymentStatus(),
                    repair.getPartsUsed()
                );
                DataStore.repairs.add(newRepair);
                table.setItems(DataStore.repairs);
            });
        });

        editButton.setOnAction(e -> {
            Repair selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select a repair to edit");
                return;
            }
            Optional<Repair> result = showRepairDialog(selected);
            result.ifPresent(repair -> {
                selected.setVehicleId(repair.getVehicleId());
                selected.setMechanicId(repair.getMechanicId());
                selected.setDescription(repair.getDescription());
                selected.setStatus(repair.getStatus());
                selected.setStartDate(repair.getStartDate());
                selected.setEndDate(repair.getEndDate());
                selected.setLaborCost(repair.getLaborCost());
                selected.setPaymentStatus(repair.getPaymentStatus());
                selected.setPartsUsed(repair.getPartsUsed());
                table.refresh();
            });
        });

        deleteButton.setOnAction(e -> {
            Repair selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select a repair to delete");
                return;
            }
            // LOGIC CHANGE: Only allow deleting 'Pending' repairs.
            if (!"Pending".equals(selected.getStatus())) {
                showAlert("Deletion Not Allowed", "Only repairs with 'Pending' status can be deleted.\n" +
                                                  "For other repairs, consider changing the status to 'Canceled'.");
                return;
            }
            if (!confirmAction("Delete Repair", "Are you sure you want to delete PENDING repair ID " + selected.getId() + "?")) {
                return;
            }
            DataStore.repairs.remove(selected);
            table.setItems(DataStore.repairs);
        });

        updateStatusButton.setOnAction(e -> {
            Repair selectedRepair = table.getSelectionModel().getSelectedItem();
            if (selectedRepair == null) {
                showAlert("No Selection", "Please select a repair to update its status.");
                return;
            }
            showUpdateRepairStatusDialog(selectedRepair, table);
        });

        assignMechanicButton.setOnAction(e -> {
            Repair selectedRepair = table.getSelectionModel().getSelectedItem();
            if (selectedRepair == null) {
                showAlert("No Selection", "Please select a repair to assign a mechanic.");
                return;
            }
            showAssignMechanicDialog(selectedRepair, table);
        });

        return root;
    }

    private void showAssignMechanicDialog(Repair repair, TableView<Repair> repairsTable) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle("Assign Mechanic");
        dialog.setHeaderText("Assign mechanic to Repair ID: " + repair.getId());

        ButtonType assignButtonType = new ButtonType("Assign", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(assignButtonType, ButtonType.CANCEL);

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        Label currentMechanicLabel = new Label("Current Mechanic: " +
            (repair.getMechanicId() > 0 ?
                DataStore.users.stream().filter(u -> u.getId() == repair.getMechanicId()).findFirst().map(User::getFullName).orElse("None") :
                "None"));

        ComboBox<User> mechanicCombo = new ComboBox<>(DataStore.users.stream()
            .filter(u -> u.getRole().equals("Mechanic"))
            .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        mechanicCombo.setPromptText("Select Mechanic");
        mechanicCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(User u) {
                return u.getFullName() + " (ID: " + u.getId() + ")";
            }
            @Override
            public User fromString(String string) { return null; }
        });

        content.getChildren().addAll(currentMechanicLabel, new Label("New Mechanic:"), mechanicCombo);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == assignButtonType) {
                return mechanicCombo.getValue();
            }
            return null;
        });

        Optional<User> result = dialog.showAndWait();
        result.ifPresent(mechanic -> {
            repair.setMechanicId(mechanic.getId());
            repairsTable.refresh();
            showAlert("Success", "Mechanic " + mechanic.getFullName() + " assigned to Repair ID " + repair.getId());
        });
    }

    private void showUpdateRepairStatusDialog(Repair repair, TableView<Repair> repairsTable) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update Repair Status");
        dialog.setHeaderText("Update status for Repair ID: " + repair.getId());

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        Label currentStatusLabel = new Label("Current Status: " + repair.getStatus());
        ComboBox<String> statusComboBox = new ComboBox<>(FXCollections.observableArrayList("Pending", "In Progress", "Completed", "Canceled"));
        statusComboBox.setPromptText("Select New Status");
        statusComboBox.setValue(repair.getStatus());

        content.getChildren().addAll(currentStatusLabel, new Label("New Status:"), statusComboBox);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return statusComboBox.getValue();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(newStatus -> {
            String oldStatus = repair.getStatus();
            if (newStatus.equals(oldStatus)) {
                showAlert("No Change", "Status is already " + newStatus);
                return;
            }

            // LOGIC CHANGE: Handle inventory updates in both directions
            boolean inventoryChanged = false;

            // Case 1: Moving TO 'Completed' from a non-completed state
            if ("Completed".equals(newStatus) && !"Completed".equals(oldStatus)) {
                List<String> insufficientParts = new ArrayList<>();
                for (PartUsage pu : repair.getPartsUsed()) {
                    if (pu.getPart().getQuantity() < pu.getQuantity()) {
                        insufficientParts.add(String.format("%s (Required: %d, Available: %d)",
                            pu.getPart().getName(), pu.getQuantity(), pu.getPart().getQuantity()));
                    }
                }

                if (!insufficientParts.isEmpty()) {
                    showAlert("Cannot Complete Repair", "Insufficient stock for the following parts:\n" +
                        String.join("\n", insufficientParts));
                    return; // Abort status change
                }

                // Debit the inventory
                for (PartUsage pu : repair.getPartsUsed()) {
                    InventoryItem item = pu.getPart();
                    item.setQuantity(item.getQuantity() - pu.getQuantity());
                }
                showAlert("Inventory Updated", "Parts for Repair ID " + repair.getId() + " have been deducted from inventory.");
                inventoryChanged = true;
            }
            // Case 2: Moving FROM 'Completed' to a non-completed state
            else if (!"Completed".equals(newStatus) && "Completed".equals(oldStatus)) {
                // Credit the inventory (return parts to stock)
                for (PartUsage pu : repair.getPartsUsed()) {
                    InventoryItem item = pu.getPart();
                    item.setQuantity(item.getQuantity() + pu.getQuantity());
                }
                showAlert("Inventory Restocked", "Parts for Repair ID " + repair.getId() + " have been returned to inventory.");
                inventoryChanged = true;
            }

            // Finally, update the repair object's status and dates
            repair.setStatus(newStatus);
            if ("Completed".equals(newStatus)) {
                repair.setEndDate(LocalDate.now());
            } else {
                repair.setEndDate(null);
            }
            
            if (!inventoryChanged) {
                 showAlert("Success", "Repair ID " + repair.getId() + " status updated to " + newStatus);
            }

            repairsTable.refresh();
        });
    }


    private BorderPane createInventoryView() {
        BorderPane root = new BorderPane();
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");

        TextField searchField = new TextField();
        searchField.setPromptText("Search inventory...");
        searchField.setPrefWidth(300);
        searchField.getStyleClass().add("search-field");

        Button addButton = new Button("Add Item");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button editButton = new Button("Edit Item");
        editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button deleteButton = new Button("Delete Item");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        toolbar.getChildren().addAll(searchField, addButton, editButton, deleteButton);

        // --- Role-Based Access Control ---
        if ("Mechanic".equals(currentUser.getRole())) {
            // Mechanics get read-only access. Hide management buttons.
            toolbar.getChildren().removeAll(addButton, editButton, deleteButton);
        }
        root.setTop(toolbar);

        TableView<InventoryItem> table = new TableView<>();
        table.setItems(DataStore.inventory);
        table.getStyleClass().add("table-view");

        // === START OF MODIFICATION: Direct styling for low stock rows ===
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(InventoryItem item, boolean empty) {
                super.updateItem(item, empty);
                
                // This implementation uses direct styling instead of CSS classes, as requested.
                if (item == null || empty) {
                    // Ensure empty rows have no special background.
                    // Setting style to null allows the base CSS for the row to be applied.
                    setStyle(null);
                } else {
                    // For non-empty rows, decide the color.
                    if (item.needsReorder()) {
                        // Apply a red background directly for low stock items.
                        // This fulfills the request to insert the color in this tab, not via CSS classes.
                        setStyle("-fx-background-color: #F8D7DA;");
                    } else {
                        // For items not low on stock, remove any inline style.
                        // This allows the default odd/even row styling from the CSS to work.
                        setStyle(null);
                    }
                }
            }
        });
        // === END OF MODIFICATION ===

        TableColumn<InventoryItem, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<InventoryItem, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<InventoryItem, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        TableColumn<InventoryItem, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setCellValueFactory(data -> data.getValue().quantityProperty().asObject());
        
        TableColumn<InventoryItem, Double> priceCol = new TableColumn<>("Price (ETB)");
        priceCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        priceCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("ETB %,.2f", price));
                }
            }
        });

        TableColumn<InventoryItem, String> barcodeCol = new TableColumn<>("Barcode");
        barcodeCol.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        TableColumn<InventoryItem, Integer> reorderThresholdCol = new TableColumn<>("Reorder Threshold");
        reorderThresholdCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getReorderThreshold()).asObject());

        table.getColumns().addAll(idCol, nameCol, categoryCol, quantityCol, priceCol, barcodeCol, reorderThresholdCol);
        root.setCenter(table);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                table.setItems(DataStore.inventory);
                return;
            }
            ObservableList<InventoryItem> filtered = FXCollections.observableArrayList();
            String lowerKeyword = newVal.toLowerCase();
            for (InventoryItem item : DataStore.inventory) {
                if (item.getName().toLowerCase().contains(lowerKeyword) ||
                    item.getCategory().toLowerCase().contains(lowerKeyword) ||
                    item.getBarcode().toLowerCase().contains(lowerKeyword)) {
                    filtered.add(item);
                }
            }
            table.setItems(filtered);
        });

        addButton.setOnAction(e -> {
            Optional<InventoryItem> result = showInventoryItemDialog(null);
            result.ifPresent(item -> {
                InventoryItem newItem = new InventoryItem(
                    DataStore.getNextInventoryId(),
                    item.getName(),
                    item.getCategory(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getBarcode(),
                    item.getReorderThreshold()
                );
                DataStore.inventory.add(newItem);
                table.setItems(DataStore.inventory);
            });
        });

        editButton.setOnAction(e -> {
            InventoryItem selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select an item to edit");
                return;
            }
            Optional<InventoryItem> result = showInventoryItemDialog(selected);
            result.ifPresent(item -> {
                selected.setName(item.getName());
                selected.setCategory(item.getCategory());
                selected.setQuantity(item.getQuantity());
                selected.setPrice(item.getPrice());
                selected.setBarcode(item.getBarcode());
                selected.setReorderThreshold(item.getReorderThreshold());
                table.refresh();
            });
        });

        deleteButton.setOnAction(e -> {
            InventoryItem selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select an item to delete");
                return;
            }
            
            boolean isInUse = DataStore.repairs.stream()
                .filter(r -> !r.getStatus().equals("Completed") && !r.getStatus().equals("Canceled"))
                .anyMatch(r -> r.getPartsUsed().stream().anyMatch(pu -> pu.getPart().getId() == selected.getId()));
            if (isInUse) {
                showAlert("Deletion Error", "Cannot delete " + selected.getName() + ".\nIt is currently allocated to an active repair.");
                return;
            }

            if (!confirmAction("Delete Item", "Are you sure you want to delete " + selected.getName() + "?")) {
                return;
            }
            DataStore.inventory.remove(selected);
            table.setItems(DataStore.inventory);
        });

        return root;
    }

    private BorderPane createAppointmentsView() {
        BorderPane root = new BorderPane();
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");

        TextField searchField = new TextField();
        searchField.setPromptText("Search appointments...");
        searchField.setPrefWidth(300);
        searchField.getStyleClass().add("search-field");

        Button addButton = new Button("Add Appointment");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button editButton = new Button("Edit Appointment");
        editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button deleteButton = new Button("Delete Appointment");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        toolbar.getChildren().addAll(searchField, addButton, editButton, deleteButton);
        root.setTop(toolbar);

        TableView<Appointment> table = new TableView<>();
        table.setItems(DataStore.appointments);
        table.getStyleClass().add("table-view");

        TableColumn<Appointment, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Appointment, Integer> customerIdCol = new TableColumn<>("Customer ID");
        customerIdCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCustomerId()).asObject());
        TableColumn<Appointment, Integer> vehicleIdCol = new TableColumn<>("Vehicle ID");
        vehicleIdCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getVehicleId()).asObject());
        TableColumn<Appointment, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Appointment, String> timeSlotCol = new TableColumn<>("Time Slot");
        timeSlotCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        TableColumn<Appointment, String> serviceTypeCol = new TableColumn<>("Service Type");
        serviceTypeCol.setCellValueFactory(new PropertyValueFactory<>("serviceType"));
        TableColumn<Appointment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        table.getColumns().addAll(idCol, customerIdCol, vehicleIdCol, dateCol, timeSlotCol, serviceTypeCol, statusCol);
        root.setCenter(table);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                table.setItems(DataStore.appointments);
                return;
            }
            ObservableList<Appointment> filtered = FXCollections.observableArrayList();
            String lowerKeyword = newVal.toLowerCase();
            for (Appointment a : DataStore.appointments) {
                if (a.getServiceType().toLowerCase().contains(lowerKeyword) ||
                    a.getStatus().toLowerCase().contains(lowerKeyword) ||
                    String.valueOf(a.getDate()).contains(lowerKeyword) ||
                    DataStore.customers.stream().filter(c -> c.getId() == a.getCustomerId()).findFirst().map(c -> c.getName().toLowerCase().contains(lowerKeyword)).orElse(false) ||
                    DataStore.vehicles.stream().filter(v -> v.getId() == a.getVehicleId()).findFirst().map(v -> v.getLicensePlate().toLowerCase().contains(lowerKeyword)).orElse(false)) {
                    filtered.add(a);
                }
            }
            table.setItems(filtered);
        });

        addButton.setOnAction(e -> {
            Optional<Appointment> result = showAppointmentDialog(null);
            result.ifPresent(appointment -> {
                Appointment newAppointment = new Appointment(
                    DataStore.getNextAppointmentId(),
                    appointment.getCustomerId(),
                    appointment.getVehicleId(),
                    appointment.getDate(),
                    appointment.getTimeSlot(),
                    appointment.getServiceType(),
                    appointment.getStatus()
                );
                DataStore.appointments.add(newAppointment);
                table.setItems(DataStore.appointments);
            });
        });

        editButton.setOnAction(e -> {
            Appointment selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select an appointment to edit");
                return;
            }
            Optional<Appointment> result = showAppointmentDialog(selected);
            result.ifPresent(appointment -> {
                selected.setCustomerId(appointment.getCustomerId());
                selected.setVehicleId(appointment.getVehicleId());
                selected.setDate(appointment.getDate());
                selected.setTimeSlot(appointment.getTimeSlot());
                selected.setServiceType(appointment.getServiceType());
                selected.setStatus(appointment.getStatus());
                table.refresh();
            });
        });

        deleteButton.setOnAction(e -> {
            Appointment selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select an appointment to delete");
                return;
            }
            if (!confirmAction("Delete Appointment", "Are you sure you want to delete appointment ID " + selected.getId() + "?")) {
                return;
            }
            DataStore.appointments.remove(selected);
            table.setItems(DataStore.appointments);
        });

        return root;
    }

    private BorderPane createReportsView() {
        BorderPane root = new BorderPane();
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        Label reportLabel = new Label("Generate Reports");
        reportLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        ComboBox<String> reportTypeCombo = new ComboBox<>();
        reportTypeCombo.getItems().addAll("Monthly Revenue Report", "Repair Status Report", "Inventory Status Report", "Customer Service History", "Mechanic Performance");
        reportTypeCombo.setPromptText("Select Report Type");
        DatePicker startDatePicker = new DatePicker(LocalDate.now().minusMonths(1));
        DatePicker endDatePicker = new DatePicker(LocalDate.now());
        HBox dateRangeBox = new HBox(10, new Label("Start Date:"), startDatePicker, new Label("End Date:"), endDatePicker);
        dateRangeBox.setAlignment(Pos.CENTER_LEFT);
        Button generateButton = new Button("Generate Report");
        Button saveButton = new Button("Save to File");
        saveButton.setDisable(true);
        TextArea reportArea = new TextArea();
        reportArea.setEditable(false);
        reportArea.setWrapText(true);
        reportArea.setPrefHeight(300);
        HBox buttonBox = new HBox(10, generateButton, saveButton);
        buttonBox.setAlignment(Pos.CENTER);

        generateButton.setOnAction(e -> {
            String reportType = reportTypeCombo.getValue();
            if (reportType == null) {
                showAlert("Error", "Please select a report type");
                return;
            }
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();

            if (startDate == null || endDate == null) {
                showAlert("Error", "Please select a date range");
                return;
            }
            if (endDate.isAfter(LocalDate.now())) {
                showAlert("Validation Error", "End date cannot be in the future.");
                return;
            }
            if (startDate.isAfter(endDate)) {
                showAlert("Validation Error", "Start date must be before or the same as the end date.");
                return;
            }

            String report = generateReport(reportType, startDate, endDate);
            reportArea.setText(report);
            saveButton.setDisable(false);
        });

        saveButton.setOnAction(e -> {
            String report = reportArea.getText();
            if (report.isEmpty()) {
                showAlert("Error", "No report to save");
                return;
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Report");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            fileChooser.setInitialFileName("report_" + LocalDate.now() + ".txt");
            File file = fileChooser.showSaveDialog(root.getScene().getWindow());
            if (file != null) {
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(report);
                    showAlert("Success", "Report saved successfully");
                } catch (IOException ex) {
                    showAlert("Error", "Failed to save report: " + ex.getMessage());
                }
            }
        });

        content.getChildren().addAll(reportLabel, reportTypeCombo, dateRangeBox, buttonBox, reportArea);
        root.setCenter(content);
        return root;
    }

    private String generateReport(String reportType, LocalDate startDate, LocalDate endDate) {
        switch (reportType) {
            case "Monthly Revenue Report": return generateRevenueReport(startDate, endDate);
            case "Repair Status Report": return generateRepairStatusReport(startDate, endDate);
            case "Inventory Status Report": return generateInventoryReport();
            case "Customer Service History": return generateCustomerServiceReport(startDate, endDate);
            case "Mechanic Performance": return generateMechanicPerformanceReport(startDate, endDate);
            default: return "Unknown report type";
        }
    }

    private String generateRevenueReport(LocalDate startDate, LocalDate endDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("Monthly Revenue Report\n");
        sb.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
        Map<String, Double> monthlyRevenue = DataStore.repairs.stream()
            .filter(r -> r.getEndDate() != null && !r.getEndDate().isBefore(startDate) && !r.getEndDate().isAfter(endDate))
            .collect(Collectors.groupingBy(r -> r.getEndDate().getMonth().toString() + " " + r.getEndDate().getYear(), Collectors.summingDouble(Repair::getTotalCost)));
        if (monthlyRevenue.isEmpty()) {
            sb.append("No revenue data for the selected period\n");
        } else {
            sb.append(String.format("%-15s %15s\n", "Month", "Revenue"));
            sb.append("---------------------------------\n");
            monthlyRevenue.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> sb.append(String.format("%-15s ETB %,14.2f\n", entry.getKey(), entry.getValue())));
            double total = monthlyRevenue.values().stream().mapToDouble(Double::doubleValue).sum();
            sb.append("---------------------------------\n");
            sb.append(String.format("%-15s ETB %,14.2f\n", "Total", total));
        }
        return sb.toString();
    }

    private String generateRepairStatusReport(LocalDate startDate, LocalDate endDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("Repair Status Report\n");
        sb.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
        Map<String, Long> statusCounts = DataStore.repairs.stream()
            .filter(r -> r.getStartDate() != null && !r.getStartDate().isBefore(startDate) && !r.getStartDate().isAfter(endDate))
            .collect(Collectors.groupingBy(Repair::getStatus, Collectors.counting()));
        sb.append(String.format("%-15s %8s\n", "Status", "Count"));
        sb.append("----------------------\n");
        statusCounts.forEach((status, count) -> sb.append(String.format("%-15s %8d\n", status, count)));
        long total = statusCounts.values().stream().mapToLong(Long::longValue).sum();
        sb.append("----------------------\n");
        sb.append(String.format("%-15s %8d\n", "Total", total));
        return sb.toString();
    }

    private String generateInventoryReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("Inventory Status Report\n");
        sb.append("Generated: ").append(LocalDate.now()).append("\n\n");
        Map<String, List<InventoryItem>> byCategory = DataStore.inventory.stream().collect(Collectors.groupingBy(InventoryItem::getCategory));
        byCategory.forEach((category, items) -> {
            sb.append(category).append(":\n");
            sb.append(String.format("%-20s %8s %15s %8s\n", "Part Name", "Quantity", "Price", "Reorder"));
            sb.append("----------------------------------------------------------\n");
            items.forEach(item -> sb.append(String.format("%-20s %8d ETB %,10.2f %8d\n", item.getName(), item.getQuantity(), item.getPrice(), item.getReorderThreshold())));
            sb.append("\n");
        });
        List<InventoryItem> lowStock = DataStore.inventory.stream().filter(InventoryItem::needsReorder).collect(Collectors.toList());
        if (!lowStock.isEmpty()) {
            sb.append("Low Stock Items:\n");
            lowStock.forEach(item -> sb.append(String.format("%s (Current: %d, Reorder at: %d)\n", item.getName(), item.getQuantity(), item.getReorderThreshold())));
        }
        return sb.toString();
    }

    private String generateCustomerServiceReport(LocalDate startDate, LocalDate endDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("Customer Service History Report\n");
        sb.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
        Map<Customer, List<Repair>> byCustomer = DataStore.repairs.stream()
            .filter(r -> r.getStartDate() != null && !r.getStartDate().isBefore(startDate) && !r.getStartDate().isAfter(endDate))
            .collect(Collectors.groupingBy(r -> {
                Vehicle vehicle = DataStore.vehicles.stream().filter(v -> v.getId() == r.getVehicleId()).findFirst().orElse(null);
                return (vehicle != null) ? DataStore.customers.stream().filter(c -> c.getId() == vehicle.getCustomerId()).findFirst().orElse(null) : null;
            }));
        byCustomer.forEach((customer, repairs) -> {
            if (customer != null) {
                sb.append(customer.getName()).append(" (").append(customer.getPhone()).append(")\n");
                sb.append("Services:\n");
                repairs.forEach(repair -> {
                    Vehicle vehicle = DataStore.vehicles.stream().filter(v -> v.getId() == repair.getVehicleId()).findFirst().orElse(null);
                    sb.append(String.format("- %s: %s (Status: %s, Cost: ETB %.2f)\n",
                        vehicle != null ? vehicle.getMake() + " " + vehicle.getModel() : "Unknown Vehicle",
                        repair.getDescription(), repair.getStatus(), repair.getTotalCost()));
                });
                double total = repairs.stream().mapToDouble(Repair::getTotalCost).sum();
                sb.append(String.format("Total Spent: ETB %.2f\n\n", total));
            }
        });
        return sb.toString();
    }

    private String generateMechanicPerformanceReport(LocalDate startDate, LocalDate endDate) {
        StringBuilder sb = new StringBuilder();
        sb.append("Mechanic Performance Report\n");
        sb.append("Period: ").append(startDate).append(" to ").append(endDate).append("\n\n");
        Map<User, List<Repair>> byMechanic = DataStore.repairs.stream()
            .filter(r -> r.getStartDate() != null && !r.getStartDate().isBefore(startDate) && !r.getStartDate().isAfter(endDate))
            .collect(Collectors.groupingBy(r -> DataStore.users.stream().filter(u -> u.getId() == r.getMechanicId()).findFirst().orElse(null)));
        sb.append(String.format("%-20s %8s %12s %18s\n", "Mechanic", "Jobs", "Total Hours", "Revenue Generated"));
        sb.append("-----------------------------------------------------------------\n");
        byMechanic.forEach((mechanic, repairs) -> {
            if (mechanic != null) {
                long completed = repairs.stream().filter(r -> r.getStatus().equals("Completed")).count();
                double revenue = repairs.stream().mapToDouble(Repair::getTotalCost).sum();
                double hours = repairs.size() * 2; // Simple estimation
                sb.append(String.format("%-20s %8d %12.1f ETB %,16.2f\n", mechanic.getFullName(), completed, hours, revenue));
            }
        });
        return sb.toString();
    }

    private BorderPane createUsersView() {
        BorderPane root = new BorderPane();
        HBox toolbar = new HBox(10);
        toolbar.getStyleClass().add("toolbar");

        TextField searchField = new TextField();
        searchField.setPromptText("Search users...");
        searchField.setPrefWidth(300);
        searchField.getStyleClass().add("search-field");

        Button addButton = new Button("Add User");
        addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        addButton.setOnMouseEntered(e -> addButton.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        addButton.setOnMouseExited(e -> addButton.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button editButton = new Button("Edit User");
        editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        editButton.setOnMouseExited(e -> editButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        Button deleteButton = new Button("Delete User");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-background-radius: 5;"));

        toolbar.getChildren().addAll(searchField, addButton, editButton, deleteButton);
        root.setTop(toolbar);

        TableView<User> table = new TableView<>();
        table.setItems(DataStore.users);
        table.getStyleClass().add("table-view");

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<User, String> fullNameCol = new TableColumn<>("Full Name");
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.getColumns().addAll(idCol, usernameCol, fullNameCol, roleCol);
        root.setCenter(table);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) {
                table.setItems(DataStore.users);
                return;
            }
            ObservableList<User> filtered = FXCollections.observableArrayList();
            String lowerKeyword = newVal.toLowerCase();
            for (User u : DataStore.users) {
                if (u.getUsername().toLowerCase().contains(lowerKeyword) ||
                    u.getFullName().toLowerCase().contains(lowerKeyword) ||
                    u.getRole().toLowerCase().contains(lowerKeyword)) {
                    filtered.add(u);
                }
            }
            table.setItems(filtered);
        });

        addButton.setOnAction(e -> {
            Optional<User> result = showUserDialog(null);
            result.ifPresent(user -> {
                User newUser = new User(
                    DataStore.getNextUserId(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole(),
                    user.getFullName()
                );
                DataStore.users.add(newUser);
                table.setItems(DataStore.users);
            });
        });

        editButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select a user to edit");
                return;
            }
            Optional<User> result = showUserDialog(selected);
            result.ifPresent(user -> {
                int index = DataStore.users.indexOf(selected);
                DataStore.users.set(index, new User(
                    selected.getId(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getRole(),
                    user.getFullName()
                ));
                table.refresh();
            });
        });

        deleteButton.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("No Selection", "Please select a user to delete");
                return;
            }
            
            if (selected.getId() == currentUser.getId()) {
                showAlert("Deletion Error", "You cannot delete your own account.");
                return;
            }
            if ("Admin".equals(selected.getRole()) && DataStore.users.stream().filter(u -> "Admin".equals(u.getRole())).count() <= 1) {
                showAlert("Deletion Error", "Cannot delete the last admin user.");
                return;
            }
            if ("Mechanic".equals(selected.getRole())) {
                boolean isAssigned = DataStore.repairs.stream()
                    .filter(r -> !r.getStatus().equals("Completed") && !r.getStatus().equals("Canceled"))
                    .anyMatch(r -> r.getMechanicId() == selected.getId());
                if (isAssigned) {
                    showAlert("Deletion Error", "Cannot delete mechanic " + selected.getFullName() + ".\nThey are assigned to active repairs.");
                    return;
                }
            }
            
            if (!confirmAction("Delete User", "Are you sure you want to delete user " + selected.getFullName() + "?")) {
                return;
            }
            DataStore.users.remove(selected);
            table.setItems(DataStore.users);
        });

        return root;
    }

    private Optional<Customer> showCustomerDialog(Customer customer) {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle(customer == null ? "Add New Customer" : "Edit Customer");
        dialog.setHeaderText(null);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField phoneField = new TextField();
        phoneField.setPromptText("+251... or 0...");
        TextField emailField = new TextField();
        emailField.setPromptText("user@gmail.com");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        
        Label phoneErrorLabel = new Label();
        phoneErrorLabel.getStyleClass().add("error-label");
        Label emailErrorLabel = new Label();
        emailErrorLabel.getStyleClass().add("error-label");

        int currentId = (customer != null) ? customer.getId() : -1;
        
        if (customer != null) {
            nameField.setText(customer.getName());
            phoneField.setText(customer.getPhone());
            emailField.setText(customer.getEmail());
            addressField.setText(customer.getAddress());
        }

        phoneField.textProperty().addListener((obs, oldVal, newVal) -> 
            phoneErrorLabel.setText(validatePhoneNumber(newVal) ? "" : "Invalid format. Use +251... or 0..."));
        
        emailField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!validateEmail(newVal)) {
                emailErrorLabel.setText("Invalid format. Must be @gmail.com");
            } else if (isCustomerEmailTaken(newVal, currentId)) {
                emailErrorLabel.setText("Email is already in use.");
            } else {
                emailErrorLabel.setText("");
            }
        });

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(phoneErrorLabel, 2, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(emailErrorLabel, 2, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);
        dialog.getDialogPane().setContent(grid);
        
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        BooleanBinding isValid = Bindings.createBooleanBinding(() ->
                !nameField.getText().trim().isEmpty() &&
                validatePhoneNumber(phoneField.getText().trim()) &&
                validateEmail(emailField.getText().trim()) &&
                !isCustomerEmailTaken(emailField.getText().trim(), currentId) &&
                !addressField.getText().trim().isEmpty(),
            nameField.textProperty(), phoneField.textProperty(), emailField.textProperty(), addressField.textProperty()
        );
        saveButton.disableProperty().bind(isValid.not());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Customer(currentId, nameField.getText().trim(),
                    phoneField.getText().trim(), emailField.getText().trim(), addressField.getText().trim());
            }
            return null;
        });
        return dialog.showAndWait();
    }

    private boolean validatePhoneNumber(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    private boolean validateEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    private Optional<Vehicle> showVehicleDialog(Vehicle vehicle) {
        Dialog<Vehicle> dialog = new Dialog<>();
        dialog.setTitle(vehicle == null ? "Add New Vehicle" : "Edit Vehicle");
        dialog.setHeaderText(null);
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        ComboBox<Customer> customerCombo = new ComboBox<>(DataStore.customers);
        customerCombo.setPromptText("Select Customer");
        customerCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Customer c) { return c != null ? c.getName() + " (ID: " + c.getId() + ")" : ""; }
            @Override public Customer fromString(String string) { return null; }
        });
        
        TextField licensePlateField = new TextField();
        licensePlateField.setPromptText("License Plate");
        TextField makeField = new TextField();
        makeField.setPromptText("Make");
        TextField modelField = new TextField();
        modelField.setPromptText("Model");
        TextField yearField = new TextField();
        yearField.setPromptText("Year (e.g., 2020)");
        TextField vinField = new TextField();
        vinField.setPromptText("17-character VIN");
        TextField mileageField = new TextField();
        mileageField.setPromptText("Mileage (e.g., 50000)");

        Label plateErrorLabel = new Label();
        plateErrorLabel.getStyleClass().add("error-label");
        Label vinErrorLabel = new Label();
        vinErrorLabel.getStyleClass().add("error-label");
        Label yearErrorLabel = new Label();
        yearErrorLabel.getStyleClass().add("error-label");
        Label mileageErrorLabel = new Label();
        mileageErrorLabel.getStyleClass().add("error-label");

        int currentId = vehicle != null ? vehicle.getId() : -1;

        if (vehicle != null) {
            customerCombo.getSelectionModel().select(DataStore.customers.stream().filter(c -> c.getId() == vehicle.getCustomerId()).findFirst().orElse(null));
            licensePlateField.setText(vehicle.getLicensePlate());
            makeField.setText(vehicle.getMake());
            modelField.setText(vehicle.getModel());
            yearField.setText(String.valueOf(vehicle.getYear()));
            vinField.setText(vehicle.getVin());
            mileageField.setText(String.valueOf(vehicle.getMileage()));
        }

        // --- Validation Listeners ---
        licensePlateField.textProperty().addListener((obs, ov, nv) -> 
            plateErrorLabel.setText(isVehicleLicensePlateTaken(nv, currentId) ? "License plate already exists." : ""));
        
        vinField.textProperty().addListener((obs, ov, nv) -> {
            if (nv == null || !nv.matches("^[a-zA-Z0-9]*$")) {
                vinErrorLabel.setText("VIN must be alphanumeric.");
            } else if (nv.length() != 17) {
                vinErrorLabel.setText("VIN must be 17 characters.");
            } else if (isVehicleVinTaken(nv, currentId)) {
                vinErrorLabel.setText("VIN already exists.");
            } else {
                vinErrorLabel.setText("");
            }
        });
        
        yearField.textProperty().addListener((obs, ov, nv) -> 
            yearErrorLabel.setText(validateYear(nv) ? "" : "Invalid year."));
        
        mileageField.textProperty().addListener((obs, ov, nv) ->
            mileageErrorLabel.setText(validateNonNegativeInteger(nv) ? "" : "Must be a positive number."));

        // --- Grid Layout ---
        grid.add(new Label("Customer:"), 0, 0);
        grid.add(customerCombo, 1, 0, 2, 1);
        grid.add(new Label("License Plate:"), 0, 1);
        grid.add(licensePlateField, 1, 1);
        grid.add(plateErrorLabel, 2, 1);
        grid.add(new Label("Make:"), 0, 2);
        grid.add(makeField, 1, 2);
        grid.add(new Label("Model:"), 0, 3);
        grid.add(modelField, 1, 3);
        grid.add(new Label("Year:"), 0, 4);
        grid.add(yearField, 1, 4);
        grid.add(yearErrorLabel, 2, 4);
        grid.add(new Label("VIN:"), 0, 5);
        grid.add(vinField, 1, 5);
        grid.add(vinErrorLabel, 2, 5);
        grid.add(new Label("Mileage:"), 0, 6);
        grid.add(mileageField, 1, 6);
        grid.add(mileageErrorLabel, 2, 6);
        
        dialog.getDialogPane().setContent(grid);
        
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        BooleanBinding isValid = Bindings.createBooleanBinding(() ->
                customerCombo.getValue() != null &&
                !licensePlateField.getText().trim().isEmpty() && !isVehicleLicensePlateTaken(licensePlateField.getText(), currentId) &&
                !makeField.getText().trim().isEmpty() &&
                !modelField.getText().trim().isEmpty() &&
                validateYear(yearField.getText()) &&
                vinField.getText().matches(VIN_PATTERN.pattern()) && !isVehicleVinTaken(vinField.getText(), currentId) &&
                validateNonNegativeInteger(mileageField.getText()),
            customerCombo.valueProperty(), licensePlateField.textProperty(), makeField.textProperty(),
            modelField.textProperty(), yearField.textProperty(), vinField.textProperty(), mileageField.textProperty());
        
        saveButton.disableProperty().bind(isValid.not());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Vehicle(currentId,
                    customerCombo.getValue().getId(),
                    licensePlateField.getText().trim(),
                    makeField.getText().trim(),
                    modelField.getText().trim(),
                    Integer.parseInt(yearField.getText()),
                    vinField.getText().trim().toUpperCase(),
                    Integer.parseInt(mileageField.getText())
                );
            }
            return null;
        });
        return dialog.showAndWait();
    }

    private boolean validateYear(String yearStr) {
        if (!validateNonNegativeInteger(yearStr)) return false;
        int year = Integer.parseInt(yearStr);
        return year >= 1900 && year <= LocalDate.now().getYear() + 1;
    }

    private Optional<Repair> showRepairDialog(Repair repair) {
        Dialog<Repair> dialog = new Dialog<>();
        dialog.setTitle(repair == null ? "Add New Repair" : "Edit Repair");
        dialog.setHeaderText(null);
        
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        ComboBox<Vehicle> vehicleCombo = new ComboBox<>(DataStore.vehicles);
        vehicleCombo.setPromptText("Select Vehicle");
        vehicleCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Vehicle v) { return v != null ? v.getLicensePlate() + " - " + v.getMake() + " " + v.getModel() : ""; }
            @Override public Vehicle fromString(String s) { return null; }
        });
        
        ComboBox<User> mechanicCombo = new ComboBox<>(DataStore.users.stream()
            .filter(u -> "Mechanic".equals(u.getRole())).collect(Collectors.toCollection(FXCollections::observableArrayList)));
        mechanicCombo.setPromptText("Select Mechanic");
        mechanicCombo.setConverter(new StringConverter<>() {
            @Override public String toString(User u) { return u != null ? u.getFullName() : ""; }
            @Override public User fromString(String s) { return null; }
        });

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description of repair work");
        ComboBox<String> statusCombo = new ComboBox<>(FXCollections.observableArrayList("Pending", "In Progress", "Completed", "Canceled"));
        statusCombo.setPromptText("Status");
        DatePicker startDatePicker = new DatePicker(LocalDate.now());
        DatePicker endDatePicker = new DatePicker();
        TextField laborCostField = new TextField("0.00");
        laborCostField.setPromptText("Labor Cost (ETB)");
        ComboBox<String> paymentStatusCombo = new ComboBox<>(FXCollections.observableArrayList("Paid", "Unpaid"));

        Label dateErrorLabel = new Label();
        dateErrorLabel.getStyleClass().add("error-label");
        Label laborCostErrorLabel = new Label();
        laborCostErrorLabel.getStyleClass().add("error-label");
        
        // --- Parts Management ---
        ObservableList<PartUsage> tempPartsList = FXCollections.observableArrayList();
        if (repair != null && repair.getPartsUsed() != null) {
            tempPartsList.addAll(repair.getPartsUsed());
        }
        
        TableView<PartUsage> partsTable = new TableView<>(tempPartsList);
        TableColumn<PartUsage, String> partNameCol = new TableColumn<>("Part");
        partNameCol.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getPart().getName()));
        TableColumn<PartUsage, Integer> partQtyCol = new TableColumn<>("Qty");
        partQtyCol.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getQuantity()).asObject());
        
        TableColumn<PartUsage, Double> partSubtotalCol = new TableColumn<>("Subtotal (ETB)");
        partSubtotalCol.setCellValueFactory(p -> new SimpleDoubleProperty(p.getValue().getSubtotal()).asObject());
        partSubtotalCol.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("ETB %,.2f", price));
                }
            }
        });

        partsTable.getColumns().addAll(partNameCol, partQtyCol, partSubtotalCol);
        partsTable.setPrefHeight(150);
        
        Button addPartButton = new Button("Add");
        Button removePartButton = new Button("Remove");
        HBox partButtons = new HBox(10, addPartButton, removePartButton);
        
        addPartButton.setOnAction(e -> showAddPartToRepairDialog().ifPresent(tempPartsList::add));
        removePartButton.setOnAction(e -> {
            PartUsage selected = partsTable.getSelectionModel().getSelectedItem();
            if (selected != null) tempPartsList.remove(selected);
        });
        
        if (repair != null) {
            vehicleCombo.setValue(DataStore.vehicles.stream().filter(v -> v.getId() == repair.getVehicleId()).findFirst().orElse(null));
            mechanicCombo.setValue(DataStore.users.stream().filter(u -> u.getId() == repair.getMechanicId()).findFirst().orElse(null));
            descriptionField.setText(repair.getDescription());
            statusCombo.setValue(repair.getStatus());
            startDatePicker.setValue(repair.getStartDate());
            endDatePicker.setValue(repair.getEndDate());
            laborCostField.setText(String.format("%.2f", repair.getLaborCost()));
            paymentStatusCombo.setValue(repair.getPaymentStatus());
        }

        // --- Validation and Layout ---
        startDatePicker.valueProperty().addListener((obs, ov, nv) -> {
            endDatePicker.setDayCellFactory(d -> new DateCell() {
                @Override public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    setDisable(item.isBefore(startDatePicker.getValue()));
                }
            });
            if (endDatePicker.getValue() != null && endDatePicker.getValue().isBefore(nv)) {
                dateErrorLabel.setText("End date cannot be before start date.");
            } else {
                dateErrorLabel.setText("");
            }
        });
        
        endDatePicker.valueProperty().addListener((obs, ov, nv) -> {
             if (nv != null && nv.isBefore(startDatePicker.getValue())) {
                dateErrorLabel.setText("End date cannot be before start date.");
            } else {
                dateErrorLabel.setText("");
            }
        });
        
        laborCostField.textProperty().addListener((obs, ov, nv) ->
            laborCostErrorLabel.setText(validateNonNegativeDouble(nv) ? "" : "Must be a positive number."));
        
        // LOGIC CHANGE: ROLE-BASED ACCESS for Mechanics
        if ("Mechanic".equals(currentUser.getRole())) {
            vehicleCombo.setDisable(true);
            mechanicCombo.setDisable(true);
            statusCombo.setDisable(true);
            startDatePicker.setDisable(true);
            endDatePicker.setDisable(true);
            laborCostField.setDisable(true);
            paymentStatusCombo.setDisable(true);
            // Mechanics can only edit description and parts
        }


        grid.add(new Label("Vehicle:"), 0, 0); grid.add(vehicleCombo, 1, 0, 2, 1);
        grid.add(new Label("Mechanic:"), 0, 1); grid.add(mechanicCombo, 1, 1, 2, 1);
        grid.add(new Label("Description:"), 0, 2); grid.add(descriptionField, 1, 2, 2, 1);
        grid.add(new Label("Status:"), 0, 3); grid.add(statusCombo, 1, 3, 2, 1);
        grid.add(new Label("Start Date:"), 0, 4); grid.add(startDatePicker, 1, 4, 2, 1);
        grid.add(new Label("End Date:"), 0, 5); grid.add(endDatePicker, 1, 5); grid.add(dateErrorLabel, 2, 5);
        grid.add(new Label("Labor Cost (ETB):"), 0, 6); grid.add(laborCostField, 1, 6); grid.add(laborCostErrorLabel, 2, 6);
        grid.add(new Label("Payment Status:"), 0, 7); grid.add(paymentStatusCombo, 1, 7, 2, 1);
        
        grid.add(new Label("Parts:"), 0, 8);
        grid.add(partsTable, 1, 8, 2, 1);
        grid.add(partButtons, 1, 9);
        
        dialog.getDialogPane().setContent(grid);
        
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        saveButton.disableProperty().bind(
            vehicleCombo.valueProperty().isNull()
            .or(mechanicCombo.valueProperty().isNull())
            .or(descriptionField.textProperty().isEmpty())
            .or(statusCombo.valueProperty().isNull())
            .or(startDatePicker.valueProperty().isNull())
            .or(paymentStatusCombo.valueProperty().isNull())
            .or(Bindings.createBooleanBinding(() -> !validateNonNegativeDouble(laborCostField.getText()), laborCostField.textProperty()))
            .or(Bindings.createBooleanBinding(() -> {
                LocalDate start = startDatePicker.getValue();
                LocalDate end = endDatePicker.getValue();
                return end != null && start != null && end.isBefore(start);
            }, startDatePicker.valueProperty(), endDatePicker.valueProperty()))
        );

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Repair(
                    repair != null ? repair.getId() : -1,
                    vehicleCombo.getValue().getId(),
                    mechanicCombo.getValue().getId(),
                    descriptionField.getText(),
                    statusCombo.getValue(),
                    startDatePicker.getValue(),
                    endDatePicker.getValue(),
                    Double.parseDouble(laborCostField.getText()),
                    paymentStatusCombo.getValue(),
                    new ArrayList<>(tempPartsList)
                );
            }
            return null;
        });
        return dialog.showAndWait();
    }
    
    private Optional<PartUsage> showAddPartToRepairDialog() {
        Dialog<PartUsage> dialog = new Dialog<>();
        dialog.setTitle("Add Part to Repair");
        
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 10, 10, 10));
        
        ComboBox<InventoryItem> partCombo = new ComboBox<>(DataStore.inventory);
        partCombo.setPromptText("Select a part");
        
        // LOGIC CHANGE: Clearer ComboBox choices
        partCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(InventoryItem item) {
                if (item == null) {
                    return null;
                }
                return String.format("%s (Stock: %d) - %s", 
                                     item.getName(), item.getQuantity(), item.getCategory());
            }

            @Override
            public InventoryItem fromString(String s) {
                return null; // Not needed
            }
        });
        partCombo.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(InventoryItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%s (Stock: %d) - %s", 
                                     item.getName(), item.getQuantity(), item.getCategory()));
                }
            }
        });

        TextField quantityField = new TextField("1");
        Label stockLabel = new Label();
        stockLabel.setStyle("-fx-font-style: italic;");
        
        partCombo.valueProperty().addListener((obs, ov, nv) -> {
            if (nv != null) {
                stockLabel.setText("Available: " + nv.getQuantity());
                stockLabel.setStyle(nv.getQuantity() > nv.getReorderThreshold() ? "-fx-text-fill: green;" : "-fx-text-fill: #f39c12;");
                if(nv.getQuantity() == 0){
                    stockLabel.setStyle("-fx-text-fill: red;");
                }
            } else {
                stockLabel.setText("");
            }
        });
        
        grid.add(new Label("Part:"), 0, 0);
        grid.add(partCombo, 1, 0, 2, 1);
        grid.add(new Label("Quantity:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(stockLabel, 2, 1);
        
        dialog.getDialogPane().setContent(grid);
        
        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.disableProperty().bind(partCombo.valueProperty().isNull());
        
        dialog.setResultConverter(btn -> {
            if (btn == addButtonType) {
                InventoryItem selectedPart = partCombo.getValue();
                int quantity;
                try {
                    quantity = Integer.parseInt(quantityField.getText());
                    if (quantity <= 0) {
                        showAlert("Invalid Input", "Quantity must be a positive number.");
                        return null;
                    }
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter a valid number for quantity.");
                    return null;
                }
                
                // STOCK CHECK
                if (selectedPart.getQuantity() < quantity) {
                    showAlert("Insufficient Stock", String.format(
                        "Cannot add %d of %s. Only %d available.",
                        quantity, selectedPart.getName(), selectedPart.getQuantity()));
                    return null; // Block adding the part
                }
                return new PartUsage(selectedPart, quantity);
            }
            return null;
        });
        
        return dialog.showAndWait();
    }

    private boolean validateNonNegativeDouble(String value) {
        try {
            return Double.parseDouble(value) >= 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }
    
    private boolean validateNonNegativeInteger(String value) {
        try {
            return Integer.parseInt(value) >= 0;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    private Optional<InventoryItem> showInventoryItemDialog(InventoryItem item) {
        Dialog<InventoryItem> dialog = new Dialog<>();
        dialog.setTitle(item == null ? "Add New Inventory Item" : "Edit Inventory Item");
        dialog.setHeaderText(null);
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        TextField priceField = new TextField();
        priceField.setPromptText("Price (ETB)");
        TextField barcodeField = new TextField();
        barcodeField.setPromptText("Barcode");
        TextField reorderThresholdField = new TextField();
        reorderThresholdField.setPromptText("Reorder Threshold");

        if (item != null) {
            nameField.setText(item.getName());
            categoryField.setText(item.getCategory());
            quantityField.setText(String.valueOf(item.getQuantity()));
            priceField.setText(String.format("%.2f", item.getPrice()));
            barcodeField.setText(item.getBarcode());
            reorderThresholdField.setText(String.valueOf(item.getReorderThreshold()));
        }

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Category:"), 0, 1);
        grid.add(categoryField, 1, 1);
        grid.add(new Label("Quantity:"), 0, 2);
        grid.add(quantityField, 1, 2);
        grid.add(new Label("Price (ETB):"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(new Label("Barcode:"), 0, 4);
        grid.add(barcodeField, 1, 4);
        grid.add(new Label("Reorder Threshold:"), 0, 5);
        grid.add(reorderThresholdField, 1, 5);
        dialog.getDialogPane().setContent(grid);
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        BooleanBinding isValid = Bindings.createBooleanBinding(() ->
                !nameField.getText().trim().isEmpty() &&
                !categoryField.getText().trim().isEmpty() &&
                validateNonNegativeInteger(quantityField.getText()) &&
                validateNonNegativeDouble(priceField.getText()) &&
                !barcodeField.getText().trim().isEmpty() &&
                validateNonNegativeInteger(reorderThresholdField.getText()),
            nameField.textProperty(), categoryField.textProperty(), quantityField.textProperty(),
            priceField.textProperty(), barcodeField.textProperty(), reorderThresholdField.textProperty());
        saveButton.disableProperty().bind(isValid.not());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new InventoryItem(item != null ? item.getId() : -1,
                    nameField.getText().trim(),
                    categoryField.getText().trim(),
                    Integer.parseInt(quantityField.getText()),
                    Double.parseDouble(priceField.getText()),
                    barcodeField.getText().trim(),
                    Integer.parseInt(reorderThresholdField.getText()));
            }
            return null;
        });
        return dialog.showAndWait();
    }

    private Optional<Appointment> showAppointmentDialog(Appointment appointment) {
        Dialog<Appointment> dialog = new Dialog<>();
        dialog.setTitle(appointment == null ? "Add New Appointment" : "Edit Appointment");
        dialog.setHeaderText(null);
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        ComboBox<Customer> customerCombo = new ComboBox<>(DataStore.customers);
        customerCombo.setPromptText("Select Customer");
        customerCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Customer c) { return c != null ? c.getName() : ""; }
            @Override public Customer fromString(String s) { return null; }
        });
        
        ComboBox<Vehicle> vehicleCombo = new ComboBox<>();
        vehicleCombo.setPromptText("Select Vehicle");
        vehicleCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Vehicle v) { return v != null ? v.getLicensePlate() : ""; }
            @Override public Vehicle fromString(String s) { return null; }
        });
        
        // Filter vehicle list based on selected customer
        customerCombo.valueProperty().addListener((obs, ov, nv) -> {
            if (nv != null) {
                vehicleCombo.setItems(DataStore.vehicles.filtered(v -> v.getCustomerId() == nv.getId()));
            } else {
                vehicleCombo.getItems().clear();
            }
        });

        DatePicker datePicker = new DatePicker(LocalDate.now());
        // Disable past dates
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });
        
        TextField timeSlotField = new TextField();
        timeSlotField.setPromptText("Time Slot (e.g., 09:00-11:00)");
        TextField serviceTypeField = new TextField();
        serviceTypeField.setPromptText("Service Type");
        ComboBox<String> statusCombo = new ComboBox<>(FXCollections.observableArrayList("Scheduled", "Confirmed", "Canceled", "Completed"));
        statusCombo.setPromptText("Status");
        
        if (appointment != null) {
            Customer c = DataStore.customers.stream().filter(cust -> cust.getId() == appointment.getCustomerId()).findFirst().orElse(null);
            customerCombo.setValue(c);
            vehicleCombo.setValue(DataStore.vehicles.stream().filter(v -> v.getId() == appointment.getVehicleId()).findFirst().orElse(null));
            datePicker.setValue(appointment.getDate());
            timeSlotField.setText(appointment.getTimeSlot());
            serviceTypeField.setText(appointment.getServiceType());
            statusCombo.setValue(appointment.getStatus());
        }
        
        grid.add(new Label("Customer:"), 0, 0); grid.add(customerCombo, 1, 0);
        grid.add(new Label("Vehicle:"), 0, 1); grid.add(vehicleCombo, 1, 1);
        grid.add(new Label("Date:"), 0, 2); grid.add(datePicker, 1, 2);
        grid.add(new Label("Time Slot:"), 0, 3); grid.add(timeSlotField, 1, 3);
        grid.add(new Label("Service Type:"), 0, 4); grid.add(serviceTypeField, 1, 4);
        grid.add(new Label("Status:"), 0, 5); grid.add(statusCombo, 1, 5);
        dialog.getDialogPane().setContent(grid);
        
        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        BooleanBinding isValid = Bindings.createBooleanBinding(() ->
                customerCombo.getValue() != null && vehicleCombo.getValue() != null &&
                datePicker.getValue() != null && !timeSlotField.getText().trim().isEmpty() &&
                !serviceTypeField.getText().trim().isEmpty() && statusCombo.getValue() != null,
            customerCombo.valueProperty(), vehicleCombo.valueProperty(), datePicker.valueProperty(),
            timeSlotField.textProperty(), serviceTypeField.textProperty(), statusCombo.valueProperty());
        saveButton.disableProperty().bind(isValid.not());
        
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Appointment(appointment != null ? appointment.getId() : -1,
                    customerCombo.getValue().getId(),
                    vehicleCombo.getValue().getId(),
                    datePicker.getValue(),
                    timeSlotField.getText().trim(),
                    serviceTypeField.getText().trim(),
                    statusCombo.getValue());
            }
            return null;
        });
        return dialog.showAndWait();
    }

    private String getPasswordStrengthError(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty.";
        }
        if (password.length() < 8) {
            return "Password must be at least 8 characters.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain an uppercase letter.";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain a number.";
        }
        return null; // Indicates a valid password
    }

    private Optional<User> showUserDialog(User user) {
        Dialog<User> dialog = new Dialog<>();
        dialog.setTitle(user == null ? "Add New User" : "Edit User");
        dialog.setHeaderText("Enter user details below.");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField();
        TextField fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");
        ComboBox<String> roleCombo = new ComboBox<>(FXCollections.observableArrayList("Admin", "Mechanic", "Receptionist"));
        roleCombo.setPromptText("Select Role");

        Label usernameErrorLabel = new Label();
        usernameErrorLabel.getStyleClass().add("error-label");
        Label passwordErrorLabel = new Label();
        passwordErrorLabel.getStyleClass().add("error-label");

        int currentId = user != null ? user.getId() : -1;

        if (user != null) {
            usernameField.setText(user.getUsername());
            fullNameField.setText(user.getFullName());
            roleCombo.setValue(user.getRole());
            passwordField.setPromptText("Leave blank to keep current password");
        } else {
            passwordField.setPromptText("Enter password");
        }

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(usernameErrorLabel, 2, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(passwordErrorLabel, 2, 1);
        grid.add(new Label("Full Name:"), 0, 2);
        grid.add(fullNameField, 1, 2);
        grid.add(new Label("Role:"), 0, 3);
        grid.add(roleCombo, 1, 3);
        dialog.getDialogPane().setContent(grid);
        
        usernameField.textProperty().addListener((obs, ov, nv) ->
            usernameErrorLabel.setText(isUsernameTaken(nv, currentId) ? "Username is already taken." : ""));

        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (user == null || (newVal != null && !newVal.isEmpty())) {
                passwordErrorLabel.setText(getPasswordStrengthError(newVal));
            } else {
                passwordErrorLabel.setText(""); // Clear error if field is empty in edit mode
            }
        });

        Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
        BooleanBinding isValid = Bindings.createBooleanBinding(() -> {
            boolean passwordOk;
            if (user == null) { // CREATE mode
                passwordOk = getPasswordStrengthError(passwordField.getText()) == null;
            } else { // EDIT mode
                passwordOk = passwordField.getText().isEmpty() || getPasswordStrengthError(passwordField.getText()) == null;
            }
            return !usernameField.getText().trim().isEmpty() &&
                   !isUsernameTaken(usernameField.getText(), currentId) &&
                   !fullNameField.getText().trim().isEmpty() &&
                   roleCombo.getValue() != null &&
                   passwordOk;
        }, usernameField.textProperty(), passwordField.textProperty(), fullNameField.textProperty(), roleCombo.valueProperty());

        saveButton.disableProperty().bind(isValid.not());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String password = passwordField.getText().isEmpty() && user != null ? user.getPassword() : passwordField.getText();
                return new User(currentId, usernameField.getText().trim(), password,
                    roleCombo.getValue(), fullNameField.getText().trim());
            }
            return null;
        });

        return dialog.showAndWait();
    }
    
    // --- UNIQUENESS VALIDATION HELPERS ---
    private boolean isUsernameTaken(String username, int currentUserId) {
        if (username == null || username.trim().isEmpty()) return false;
        return DataStore.users.stream()
            .anyMatch(u -> u.getUsername().equalsIgnoreCase(username.trim()) && u.getId() != currentUserId);
    }
    
    private boolean isCustomerEmailTaken(String email, int currentCustomerId) {
        if (email == null || email.trim().isEmpty()) return false;
        return DataStore.customers.stream()
            .anyMatch(c -> c.getEmail().equalsIgnoreCase(email.trim()) && c.getId() != currentCustomerId);
    }

    private boolean isVehicleVinTaken(String vin, int currentVehicleId) {
        if (vin == null || vin.trim().isEmpty()) return false;
        return DataStore.vehicles.stream()
            .anyMatch(v -> v.getVin().equalsIgnoreCase(vin.trim()) && v.getId() != currentVehicleId);
    }
    
    private boolean isVehicleLicensePlateTaken(String plate, int currentVehicleId) {
        if (plate == null || plate.trim().isEmpty()) return false;
        return DataStore.vehicles.stream()
            .anyMatch(v -> v.getLicensePlate().equalsIgnoreCase(plate.trim()) && v.getId() != currentVehicleId);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean confirmAction(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void updateLowStockLabel(Label label) {
        long lowStockCount = DataStore.inventory.stream().filter(InventoryItem::needsReorder).count();
        if (lowStockCount > 0) {
            label.setText("⚠️ Low stock items: " + lowStockCount);
            label.getStyleClass().add("low-stock-warning");
            label.getStyleClass().remove("in-stock");
        } else {
            label.setText("All items in stock");
            label.getStyleClass().add("in-stock");
            label.getStyleClass().remove("low-stock-warning");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
