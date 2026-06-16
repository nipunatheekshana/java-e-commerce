# E-Commerce Management System (Group 03)

A Java console application that implements the **Online E-Commerce Management
System** described in the project proposal (course SWT12031 – Practical for
Object Oriented Programming). It demonstrates core OOP principles —
**abstraction, encapsulation, inheritance and polymorphism** — across a
modular customer / product / cart / order / admin design.

You do **not** need Java installed locally — everything compiles and runs
inside Docker.

---

## Quick start (Docker)

```bash
# easiest: build + run in one go
./run.sh
```

or manually:

```bash
docker build -t ecommerce-mgmt .
docker run --rm -it ecommerce-mgmt
```

or with Docker Compose:

```bash
docker compose run --rm ecommerce
```

> The `-it` flags (and `stdin_open`/`tty` in compose) are required because the
> app is interactive and reads menu choices from standard input.

### Demo logins

| Role     | Email           | Password   |
|----------|-----------------|------------|
| Customer | `demo@shop.com` | `demo123`  |
| Admin    | `admin@shop.com`| `admin123` |

New customers can also register from the main menu.

---

## What you can do

**Customer**: register / login, browse & search products, browse by category,
add / update / remove cart items, checkout & place an order, view order
history, cancel an order, update profile.

**Admin**: add / update / delete products, view all customers, view all
orders, and generate a sales report.

---

## Project structure

```
src/main/java/com/ecommerce/
├── Main.java                 # entry point
├── model/                    # domain classes (proposed class structure)
│   ├── User.java   (abstract) ── Customer.java, Admin.java
│   ├── Product.java, Category.java (enum)
│   ├── ShoppingCart.java, CartItem.java
│   └── Order.java, OrderItem.java, OrderStatus.java (enum)
├── service/                  # business logic + in-memory data store
│   ├── ProductService.java   # product module
│   ├── UserService.java      # customer + admin module
│   ├── OrderService.java     # order module (ID generation, stock, cancel)
│   └── ReportService.java    # sales / order report
└── ui/
    └── ConsoleUI.java        # console user interface
```

This maps directly onto the proposal's architecture: **UI → Business Logic →
(Customer / Product / Cart / Order modules) → Admin module → Data Storage
Layer**. Data is held in memory; the services are written so a database-backed
store could replace them without touching the UI.

---

## Running without Docker (optional)

If you later install a JDK (21+):

```bash
mkdir -p out
find src/main/java -name "*.java" > sources.txt
javac -d out @sources.txt
java -cp out com.ecommerce.Main
```
