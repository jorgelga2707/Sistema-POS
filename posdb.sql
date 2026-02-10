DROP DATABASE IF EXISTS posdb;
CREATE DATABASE posdb;
USE posdb;

CREATE TABLE IF NOT EXISTS usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY UNIQUE,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  rol VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS productos (
  id INT AUTO_INCREMENT PRIMARY KEY UNIQUE,
  codigo VARCHAR(50) NOT NULL UNIQUE,
  nombre VARCHAR(150),
  descripcion TEXT,
  categoria VARCHAR(100),
  precio DECIMAL(10,2),
  stock INT
);

CREATE TABLE IF NOT EXISTS ventas (
  id INT AUTO_INCREMENT PRIMARY KEY UNIQUE,
  fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
  total DECIMAL(10,2),
  id_Usuario INT, 
  FOREIGN KEY (id_Usuario) REFERENCES usuarios(id) 
);

CREATE TABLE IF NOT EXISTS detalle_venta (
  id INT AUTO_INCREMENT PRIMARY KEY ,
  venta_id INT,
  producto_id INT,
  cantidad INT,
  subtotal DECIMAL(10,2),
  FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
  FOREIGN KEY (producto_id) REFERENCES productos(id)
);

INSERT INTO usuarios (username, password, rol) VALUES
('admin','admin123','Administrador'),
('ventas','vendedor123','Vendedor');

INSERT INTO productos (codigo, nombre, descripcion, categoria, precio, stock) VALUES

('A001','Arroz Costeño 1kg','Arroz blanco extra','Abarrotes',4.20,30),
('A002','Azúcar Rubia 1kg','Azúcar rubia','Abarrotes',3.80,25),
('A003','Fideos Don Vittorio','Pasta 500g','Abarrotes',3.50,20),
('A004','Aceite Primor 1L','Aceite vegetal','Abarrotes',9.50,18),
('A005','Lentejas 500g','Lentejas secas','Abarrotes',3.20,22),

('B001','Coca Cola 600ml','Gaseosa 600 ml','Bebidas',3.50,50),
('B002','Inca Kola 1L','Gaseosa 1 litro','Bebidas',5.00,40),
('B003','Agua San Luis 625ml','Agua sin gas','Bebidas',2.00,60),
('B004','Fanta Naranja 500ml','Gaseosa sabor naranja','Bebidas',3.00,45),
('B005','Powerade','Bebida rehidratante','Bebidas',4.50,30),

('P001','Pan Integral','Bolsa 400g','Panadería',2.00,30),
('P002','Pan Francés','Unidad','Panadería',0.30,100),
('P003','Pan de Molde','Bolsa grande','Panadería',6.50,20),
('P004','Queque','Queque casero','Panadería',4.00,15),
('P005','Galletas de Avena','Paquete','Panadería',3.00,25),

('S001','Papas Lays','Papas fritas','Snacks',2.50,40),
('S002','Chizitos','Snack de maíz','Snacks',1.50,50),
('S003','Chocolate Sublime','Chocolate con maní','Snacks',3.00,35),
('S004','Galletas Oreo','Paquete','Snacks',4.20,28),
('S005','Maní Salado','Bolsa 200g','Snacks',2.80,30),

('C001','Shampoo Head & Shoulders','Anticaspa 375ml','Cuidado Personal',18.00,15),
('C002','Pasta Dental Colgate','Anticaries','Cuidado Personal',6.50,25),
('C003','Jabón Protex','Protección antibacterial','Cuidado Personal',3.50,40),
('C004','Desodorante Rexona','48h protección','Cuidado Personal',12.00,20),
('C005','Papel Higiénico Elite','Paquete x4','Cuidado Personal',7.50,30),

('LIM001','Detergente Ariel 1kg','Detergente en polvo','Limpieza',14.00,20),
('LIM002','Lejía Clorox','Lejía 1L','Limpieza',4.00,30),
('LIM003','Lavavajilla Sapolio','Limón 900ml','Limpieza',5.50,25),
('LIM004','Limpiavidrios','Spray multiuso','Limpieza',6.00,18),
('LIM005','Esponja Scotch-Brite','Uso doméstico','Limpieza',2.50,40);

select*from productos;
select*from usuarios; 

 