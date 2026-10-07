# 🏢 TAK Limited - Tenant Management System

A full-featured desktop application built with **JavaFX** and **MySQL** for managing flat rentals, tenant bookings, and moving/transportation services. Managed using standard **Apache Maven**.

---

## 📋 Features

- 👤 **Role-based Authentication:** Separate portals and dashboards for **Owners** and **Tenants**.
- 🏠 **Flat Listings:** Owners can add flats with location, dimensions, rent, room specifications, and multiple photos.
- 🔍 **Search & Filter:** Tenants can search available flats based on location, budget, and size.
- 📑 **Booking System:** Tenants can instantly book available flats.
- 🚚 **Transportation & Relocation Service:** Integrated booking for moving trucks and manpower with instant cost estimation.
- 🗄️ **Dual Database Engine Support:** Automatically connects to MySQL if installed, or falls back to an **Embedded Zero-Setup Database** (no MySQL or server installation required!).

---

## 🛠️ Prerequisites

The **only** thing required to run this project is:
- **Java Development Kit (JDK):** JDK 21 or higher (JDK 21, 23, or 25).
*(No MySQL or IntelliJ IDEA installation is needed!)*

---

## ⚡ Quick Start (No IDE or Database Setup Needed!)

### Step 1: Download or Clone
- **Via Git:**
  ```bash
  git clone https://github.com/coderKaifRh/Tenant_management_project.git
  cd Tenant_management_project
  ```
- **Or via Browser:** Click green **Code** button on GitHub -> **Download ZIP** -> Extract the folder.

### Step 2: Double-Click to Run!
- **Windows:** Just double-click **`run.bat`**
- **macOS / Linux:** Run `./run.sh` in terminal

> **That's it!** The launcher will automatically check Java, download required Maven libraries, set up the embedded database, and launch the UI window.

---

## 🖥️ Alternative Run Options

### Option A: Using IntelliJ IDEA (1-Click Run ▶️)
1. Open IntelliJ IDEA and choose **Open**.
2. Select the `Tenant_management_project` folder.
3. IntelliJ will automatically detect Maven dependencies.
4. Select **"Run Application"** from the top run configuration menu and click the green **Play (▶️)** button.

### Option B: Using Terminal / Maven Wrapper
- **Windows:** `mvnw.cmd javafx:run`
- **macOS / Linux:** `./mvnw javafx:run`

---

## 🗄️ Database Options (Optional)
By default, you **do not** need MySQL. The app will automatically initialize and store data in a persistent local embedded database (`tak_limited_db`).

If you prefer to use **MySQL Server** (optional for power users):
1. Start your local MySQL service (e.g., via XAMPP or MySQL Server).
2. The app will auto-detect port 3306 and create the `tak_limited` database automatically on startup.
3. You can also manually import [`schema.sql`](schema.sql) if needed:
   ```bash
   mysql -u root -p < schema.sql
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