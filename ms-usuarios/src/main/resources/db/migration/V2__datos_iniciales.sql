-- ===== Usuario administrador (contraseña: Admin123) =====
INSERT INTO usuarios (nombre, correo, contrasena, activo, rol)
VALUES ('Administrador', 'admin@gmail.com',
        '$2a$10$mMMCZR5XNl0cPZCatDPBlONjwJmSQwpKy15O8Z/JQ7ZvhsOo1it4y',
        TRUE, 'ADMIN');

-- ===== Categorías (las mismas del select de AdminProductos) =====
INSERT INTO categorias (nombre) VALUES ('Technic');
INSERT INTO categorias (nombre) VALUES ('Star Wars');
INSERT INTO categorias (nombre) VALUES ('MOC');

-- ===== Productos (los 14 de legosData.js) =====
INSERT INTO productos (codigo, nombre, precio, stock, descuento, imagen, categoria_id) VALUES
('ATST-001', 'Caminante AT-ST', 150000, 5, 0, '/img/at-st.png', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('DEST-001', 'Destructor Estelar', 700000, 2, 0, '/img/stardestroyer.png', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('ATAT-001', 'AT-AT Supremacy', 850000, 1, 0, '/img/Fotoat1.jpeg', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('HALC-001', 'Halcón Milenario', 800000, 3, 0, '/img/halconmilenario.png', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-005', 'LEGO® Star Wars Pack De Combate: Emboscada En Mandalore', 29690, 10, 0, 'https://www.tiendacopec.cl/cdn/shop/files/720196-1.jpg?v=1743191792', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-006', 'LEGO® Star Wars Caza Estelar N-1 De Mando Y Grogu', 49490, 8, 0, 'https://www.tiendacopec.cl/cdn/shop/files/720198-1.jpg?v=1743191837&width=1200', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-007', 'Lego Star Wars Hogar De Grogu', 31670, 5, 0, 'https://www.tiendacopec.cl/cdn/shop/files/673419422390-1.jpg?v=1776276054&width=1200', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-008', 'Pack de Combate: Soldado Clon y Droide de Combate', 49990, 12, 20, 'https://www.tiendalego.cl/cdn/shop/products/75372_box_1000x1000.jpg?v=1773684544', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-009', 'Pack de Combate: Sitio de Mandalore', 39990, 15, 0, 'https://www.tiendalego.cl/cdn/shop/files/75449_Box1_v39_851x851.jpg?v=1773686157', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-010', 'Set LEGO Star Wars Tm Caza TIE y Ala-X Fusionados', 239990, 4, 0, 'https://i5.walmartimages.cl/asr/b852af15-b7d4-490e-b1fe-fb24b2dc781d.f07979af1b6845db9b5a4205bcf003fa.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-011', 'Set De Construcción Lego 75412 Star Wars', 31662, 7, 0, 'https://http2.mlstatic.com/D_NQ_NP_2X_789595-MLA114764147006_082026-F.webp', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-012', 'Pack de Combate: Clon Troopers de la 501', 32990, 20, 0, 'https://www.tiendalego.cl/cdn/shop/products/75345_box_851x851.jpg?v=1773072246', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-013', 'Casco de Piloto de AT-RT del Remanente Imperial', 109990, 3, 0, 'https://www.tiendalego.cl/cdn/shop/files/75458_Box1_v39_851x851.jpg?v=1782417013', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-014', 'Busto de Darth Vader', 89990, 2, 20, 'https://www.tiendalego.cl/cdn/shop/files/75439_Box1_v39_851x851.jpg?v=1776799113', (SELECT id FROM categorias WHERE nombre = 'Star Wars'));