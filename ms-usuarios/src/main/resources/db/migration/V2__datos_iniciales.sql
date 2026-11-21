-- Usuario administrador (contraseña: Admin123)
INSERT INTO usuarios (nombre, correo, contrasena, activo, rol)
VALUES ('Administrador', 'admin@gmail.com',
        '$2a$10$mMMCZR5XNl0cPZCatDPBlONjwJmSQwpKy15O8Z/JQ7ZvhsOo1it4y',
        TRUE, 'ADMIN');

-- Categorías (las mismas del select de AdminProductos)
INSERT INTO categorias (nombre) VALUES ('Technic');
INSERT INTO categorias (nombre) VALUES ('Star Wars');
INSERT INTO categorias (nombre) VALUES ('MOC');

-- Productos de Brick Motors (los 14 de legosData.js, con descripción)
INSERT INTO productos (codigo, nombre, descripcion, precio, stock, descuento, imagen, categoria_id) VALUES
('ATST-001', 'Caminante AT-ST', 'Réplica del caminante AT-ST del Imperio, ideal para exhibir en tu colección de Star Wars.', 150000, 5, 0, '/img/at-st.png', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('DEST-001', 'Destructor Estelar', 'Imponente Destructor Estelar para recrear las grandes batallas espaciales de la galaxia.', 700000, 2, 0, '/img/stardestroyer.png', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('ATAT-001', 'AT-AT Supremacy', 'Nuestro proyecto Technic más grande: el AT-AT Supremacy con estructura reforzada.', 850000, 1, 0, '/img/Fotoat1.jpeg', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('HALC-001', 'Halcón Milenario', 'La nave más famosa de la galaxia, lista para exhibir junto al resto de tu flota.', 800000, 3, 0, '/img/halconmilenario.png', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-005', 'LEGO® Star Wars Pack De Combate: Emboscada En Mandalore', 'Pack de combate para armar una escena de emboscada en Mandalore.', 29690, 10, 0, 'https://www.tiendacopec.cl/cdn/shop/files/720196-1.jpg?v=1743191792', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-006', 'LEGO® Star Wars Caza Estelar N-1 De Mando Y Grogu', 'Caza estelar N-1 de Mando y Grogu para sumar a tus naves de Star Wars.', 49490, 8, 0, 'https://www.tiendacopec.cl/cdn/shop/files/720198-1.jpg?v=1743191837&width=1200', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-007', 'Lego Star Wars Hogar De Grogu', 'El hogar de Grogu en versión de construcción, ideal para fans de la serie.', 31670, 5, 0, 'https://www.tiendacopec.cl/cdn/shop/files/673419422390-1.jpg?v=1776276054&width=1200', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-008', 'Pack de Combate: Soldado Clon y Droide de Combate', 'Pack de combate con un soldado clon y un droide de combate para armar tu propia batalla.', 49990, 12, 20, 'https://www.tiendalego.cl/cdn/shop/products/75372_box_1000x1000.jpg?v=1773684544', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-009', 'Pack de Combate: Sitio de Mandalore', 'Pack de combate inspirado en el sitio de Mandalore, perfecto para escenas de batalla.', 39990, 15, 0, 'https://www.tiendalego.cl/cdn/shop/files/75449_Box1_v39_851x851.jpg?v=1773686157', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-010', 'Set LEGO Star Wars Tm Caza TIE y Ala-X Fusionados', 'Set grande que fusiona un caza TIE y un Ala-X en un solo modelo de exhibición.', 239990, 4, 0, 'https://i5.walmartimages.cl/asr/b852af15-b7d4-490e-b1fe-fb24b2dc781d.f07979af1b6845db9b5a4205bcf003fa.jpeg?odnHeight=612&odnWidth=612&odnBg=FFFFFF', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-011', 'Set De Construcción Lego 75412 Star Wars', 'Set de construcción de Star Wars para armar y exhibir en tu vitrina.', 31662, 7, 0, 'https://http2.mlstatic.com/D_NQ_NP_2X_789595-MLA114764147006_082026-F.webp', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-012', 'Pack de Combate: Clon Troopers de la 501', 'Pack de combate con clon troopers de la 501 para completar tu escena de batalla.', 32990, 20, 0, 'https://www.tiendalego.cl/cdn/shop/products/75345_box_851x851.jpg?v=1773072246', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-013', 'Casco de Piloto de AT-RT del Remanente Imperial', 'Casco de piloto de AT-RT del Remanente Imperial, pieza de colección para exhibir.', 109990, 3, 0, 'https://www.tiendalego.cl/cdn/shop/files/75458_Box1_v39_851x851.jpg?v=1782417013', (SELECT id FROM categorias WHERE nombre = 'Star Wars')),
('SW-014', 'Busto de Darth Vader', 'Busto de Darth Vader para exhibir, un clásico para coleccionistas.', 89990, 2, 20, 'https://www.tiendalego.cl/cdn/shop/files/75439_Box1_v39_851x851.jpg?v=1776799113', (SELECT id FROM categorias WHERE nombre = 'Star Wars'));