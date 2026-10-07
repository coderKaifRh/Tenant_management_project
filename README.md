# 🏢 TAK Limited - Tenant Management System

A full-featured desktop application built with **JavaFX** and **MySQL** for managing flat rentals, tenant bookings, and moving/transportation services. Managed using standard **Apache Maven**.

---

## 📋 Features

- 👤 **Role-based Authentication:** Separate portals and dashboards for **Owners** and **Tenants**.
- 🏠 **Flat Listings:** Owners can add flats with location, dimensions, rent, room specifications, and multiple photos.
- 🔍 **Search & Filter:** Tenants can search available flats based on location, budget, and size.
- 📑 **Booking System:** Tenants can instantly book available flats.
- 🚚 **Transportation & Relocation Service:** Integrated booking for moving trucks and manpower with instant cost estimation.
- 🗄️ **Persistent Database:** Backed by MySQL relational database.

---

## 🛠️ Prerequisites

Before running the application, make sure you have the following installed on your machine:

1. **Java Development Kit (JDK):** JDK 21 or higher (JDK 25 recommended).
2. **Apache Maven:** Maven 3.8+ (bundled with IntelliJ IDEA or standalone).
3. **MySQL Server:** MySQL 8.0+ running on port `3306`.

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/coderKaifRh/Tenant_management_project.git
cd Tenant_management_project
```

### 2. Set Up the Database
Open MySQL Workbench, phpMyAdmin, or MySQL Command Line Client, and run the included [`schema.sql`](schema.sql) file:

```sql
SOURCE schema.sql;
```
Or execute:
```bash
mysql -u root -p < schema.sql
```
This will create the `tak_limited` database along with all required tables (`users`, `owner_info`, `tenant_info`, `flats`, `flat_images`, `bookings`, `transport`).

### 3. Configure Database Credentials
Open `src/main/java/com/template/DBConnection.java`:
```java
private static final String DB_URL = System.getProperty("db.url", "jdbc:mysql://localhost:3306/tak_limited?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true");
private static final String DB_USER = System.getProperty("db.user", "root");
private static final String DB_PASSWORD = System.getProperty("db.password", "YOUR_MYSQL_PASSWORD");
```
Replace `YOUR_MYSQL_PASSWORD` with your local MySQL root password, OR pass it as a JVM argument when running:
`-Ddb.password=your_password`

---

## ▶️ Running the Application

### Option A: Using IntelliJ IDEA (1-Click Run ▶️)
1. Open IntelliJ IDEA and select **Open**.
2. Select the cloned `Tenant_management_project` folder (or open `pom.xml`).
3. IntelliJ will automatically detect Maven and download all dependencies (JavaFX 25, MySQL connector) in the background.
4. **"Run Application"** is pre-configured at the top toolbar!
5. **Just click the green Run button (▶️)** — the app will build and launch immediately!

### Option B: Using Terminal / Maven Wrapper (No Maven Install Required)
If you don't even have Maven installed, use the included Maven Wrapper:

- **Windows:**
  ```cmd
  mvnw.cmd javafx:run
  ```
- **macOS / Linux:**
  ```bash
  ./mvnw javafx:run
  ```

---

## 📁 Project Structure

```text
Tenant_management_project/
├── pom.xml                               # Maven project dependencies & plugins
├── schema.sql                            # Complete database schema
├── README.md                             # Project documentation
└── src/
    └── main/
        ├── java/
        │   └── com/template/
        │       ├── Main.java             # JavaFX Application Entry
        │       ├── Launcher.java         # Main launcher
        │       ├── DBConnection.java     # Database connector
        │       ├── LoginController.java
        │       ├── SignupController.java
        │       ├── OwnerController.java
        │       ├── TenantController.java
        │       ├── ResultsController.java
        │       ├── TransportController.java
        │       └── ...
        └── resources/
            └── com/template/
                ├── LoginUI.fxml          # Login screen
                ├── SignupUI.fxml         # Registration screen
                ├── OwnerDashboard.fxml   # Owner portal
                ├── TenantSearchUI.fxml   # Tenant search
                ├── ResultsUI.fxml        # Results & booking
                └── TransportUI.fxml      # Moving service UI
```

---

## 👥 Authors
- **Kaif Rahman** ([@coderKaifRh](https://github.com/coderKaifRh))