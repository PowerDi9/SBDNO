-- TABLA EMPRESAS

CREATE TABLE IF NOT EXISTS empresas (
    id_empresa INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE
);

--TABLA TIENDAS

CREATE TABLE IF NOT EXISTS tiendas (
    id_tienda INTEGER PRIMARY KEY AUTOINCREMENT,
    id_empresa INTEGER NOT NULL,
    nombre TEXT NOT NULL,
    FOREIGN KEY(id_empresa) REFERENCES empresas(id_empresa)
);

-- TABLA CLIENTES

CREATE TABLE IF NOT EXISTS clientes (
    id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    telefono TEXT,
    empresa TEXT,
    tienda TEXT
);

-- TABLA VENDEDORES

CREATE TABLE IF NOT EXISTS vendedores (
    id_vendedor INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL
);

-- TABLA GESTORES

CREATE TABLE IF NOT EXISTS trabajadores (
    id_trabajador INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL
);

-- TABLA CAMIONES

CREATE TABLE IF NOT EXISTS camiones (
    id_camion INTEGER PRIMARY KEY AUTOINCREMENT,
    descripcion TEXT
);

-- TABLA ALBARANES

CREATE TABLE IF NOT EXISTS albaranes (
    id_albaran INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha DATE NOT NULL,
    fecha_entrega DATE,
    importe REAL NOT NULL,
    id_cliente INTEGER NOT NULL,
    id_vendedor INTEGER NOT NULL,
    id_trabajador INTEGER,
    id_camion INTEGER,
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente),
    FOREIGN KEY (id_vendedor) REFERENCES vendedores(id_vendedor),
    FOREIGN KEY (id_trabajador) REFERENCES trabajadores(id_trabajador),
    FOREIGN KEY (id_camion) REFERENCES camiones(id_camion)
);

-- INDICES para mejorar el rendimiento de las consultas

CREATE INDEX IF NOT EXISTS idx_albaranes_fecha_entrega ON albaranes(fecha_entrega);
CREATE INDEX IF NOT EXISTS idx_albaranes_cliente ON albaranes(id_cliente);
CREATE INDEX IF NOT EXISTS idx_albaranes_vendedor ON albaranes(id_vendedor);
CREATE INDEX IF NOT EXISTS idx_albaranes_camion ON albaranes(id_camion);
