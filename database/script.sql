-- BUSINESS TABLE

CREATE TABLE IF NOT EXISTS business (
    business_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    percentage REAL NOT NULL
);

-- STORES TABLE

CREATE TABLE IF NOT EXISTS stores (
    store_id INTEGER PRIMARY KEY AUTOINCREMENT,
    business_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    FOREIGN KEY(business_id) REFERENCES business(business_id)
);

-- CLIENTS TABLE

CREATE TABLE IF NOT EXISTS clients (
    client_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    phone TEXT
);

-- SELLERS TABLE

CREATE TABLE IF NOT EXISTS sellers (
    seller_id INTEGER PRIMARY KEY AUTOINCREMENT,
    store_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    FOREIGN KEY(store_id) REFERENCES stores(store_id)
);

-- EMPLOYEE TABLE

CREATE TABLE IF NOT EXISTS employees (
    employee_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    status TEXT
);

-- TRUCKS TABLE

CREATE TABLE IF NOT EXISTS trucks (
    truck_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL UNIQUE,
    description TEXT
);

--EMPLOYEE TO TRUCK TABLE (INTERMEDIATE)
CREATE TABLE IF NOT EXISTS truck_employees (
    truck_id INTEGER NOT NULL,
    employee_id INTEGER NOT NULL,
    PRIMARY KEY(truck_id, employee_id),
    FOREIGN KEY(truck_id) REFERENCES trucks(truck_id),
    FOREIGN KEY(employee_id) REFERENCES employees(employee_id)
);

-- DELIVERY NOTES TABLE

CREATE TABLE IF NOT EXISTS delivery_notes (
    delivery_note_id INTEGER PRIMARY KEY AUTOINCREMENT,
    date DATE NOT NULL,
    delivery_date DATE,
    amount REAL NOT NULL,
    client_id INTEGER NOT NULL,
    seller_id INTEGER NOT NULL,
    business_id INTEGER NOT NULL,
    store_id INTEGER NOT NULL,
    truck_id INTEGER,
    pdf_path TEXT,
    FOREIGN KEY (client_id) REFERENCES clients(client_id),
    FOREIGN KEY (seller_id) REFERENCES sellers(seller_id),
    FOREIGN KEY (truck_id) REFERENCES trucks(truck_id)
);

-- INDEXES

CREATE INDEX IF NOT EXISTS idx_delivery_notes_delivery_date ON delivery_notes(delivery_date);
CREATE INDEX IF NOT EXISTS idx_delivery_notes_client ON delivery_notes(client_id);
CREATE INDEX IF NOT EXISTS idx_delivery_notes_seller ON delivery_notes(seller_id);
CREATE INDEX IF NOT EXISTS idx_delivery_notes_truck ON delivery_notes(truck_id);