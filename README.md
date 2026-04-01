# Auto-Repair-Shop-Management-System
# Auto Repair Shop Management System

A comprehensive JavaFX desktop application for managing automotive repair shop operations, including customer management, vehicle tracking, repair orders, inventory control, appointments, and reporting.

## Features

### User Roles & Authentication
- **Admin**: Full system access with user management and reporting
- **Mechanic**: View assigned repairs, update status, manage parts
- **Receptionist**: Handle customers, vehicles, appointments, and repairs
- **Customer**: View personal vehicles and repair history

### Core Modules

#### Customer Management
- Add, edit, and delete customer profiles
- Search customers by name, phone, or email
- Email and phone number validation
- View customer vehicles and service history

#### Vehicle Management
- Track vehicles with license plate, VIN, make, model, year, and mileage
- Link vehicles to customers
- Cascading delete with associated records
- Unique VIN and license plate validation

#### Repair Orders
- Create and manage repair jobs
- Assign mechanics to repairs
- Track repair status: Pending, In Progress, Completed, Canceled
- Add parts to repairs with automatic inventory deduction on completion
- Calculate total cost (labor + parts)
- Payment status tracking

#### Inventory Management
- Track parts with quantities, prices, and reorder thresholds
- Visual low-stock highlighting
- Automatic stock updates when repairs are completed
- Prevent part usage when insufficient stock exists

#### Appointments
- Schedule customer appointments
- Prevent past dates
- Status tracking: Scheduled, Confirmed, Canceled, Completed

#### Reports
- Monthly revenue reports
- Repair status summaries
- Inventory status reports
- Customer service history
- Mechanic performance metrics
- Export reports to text files

### Dashboard Views

#### Admin Dashboard
- Operational statistics (customers, active repairs, low stock, today's appointments)
- Staff count (mechanics and receptionists)
- Financial metrics (parts cost, net income)
- Pie charts for repair status and inventory status

#### Mechanic Dashboard
- Assigned repairs list
- Update repair status
- View parts needed for active repairs
- Edit repair descriptions

#### Receptionist Dashboard
- Today's appointments view
- Recent repairs list
- Full customer and vehicle management

#### Customer Dashboard
- Personal vehicle cards
- Repair history table with costs

## Technology Stack

- **Java 17+**
- **JavaFX** for UI
- **In-memory data storage** with ObservableList collections
- **CSS** for styling

## Installation

### Prerequisites
- JDK 17 or higher
- JavaFX SDK (if not bundled with JDK)

### Running the Application

1. Clone the repository:
```bash
git clone https://github.com/yourusername/auto-repair-shop-system.git
cd auto-repair-shop-system
```

2. Compile and run:
```bash
javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml AutomotiveRepairSystem.java
java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml AutomotiveRepairSystem
```

## Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | AdminPass1 |
| Mechanic | mechanic1 | MechPass1 |
| Receptionist | reception1 | RecepPass1 |
| Customer | Use customer email | Use customer phone number |

**Customer example:**
- Email: `bereket@gmail.com`
- Password: `+251956743456`

## Data Validation

- **Phone numbers**: Format: `+251[79]XXXXXXXX` or `0[79]XXXXXXXX`
- **Emails**: Must be `@gmail.com` addresses
- **VIN**: 17-character alphanumeric
- **Years**: Between 1900 and next year
- **Passwords**: Minimum 8 characters, at least one uppercase letter and one number



## Screenshots

### Login Screen
Modern login interface with role selection and background image rotation.

### Admin Dashboard
Comprehensive overview with statistics, financial metrics, and status charts.

### Repair Management
Full CRUD operations with part selection and inventory tracking.

## Key Features Explained

### Inventory Integration
When a repair is marked as "Completed", parts are automatically deducted from inventory. If sufficient stock is unavailable, completion is blocked. If a completed repair is reverted, parts are returned to stock.

### Cascading Deletes
- Deleting a customer checks for associated vehicles/appointments
- Deleting a vehicle removes all associated repairs and appointments
- Mechanics with active assignments cannot be deleted
- Users cannot delete their own account or the last admin

### Reporting
All reports can be generated for custom date ranges and exported to text files for record-keeping.

## Future Enhancements

- [ ] Database integration (MySQL/PostgreSQL)
- [ ] Email notifications for appointments
- [ ] PDF report generation
- [ ] Barcode scanning for parts
- [ ] Service package management
- [ ] Technician scheduling calendar
- [ ] Mobile companion app

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Author

**Behailu Yifru** - *Initial work*

---

*Built as a comprehensive solution for automotive repair shop management.*
