-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-02-2024 a las 21:25:59
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `medalerta`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alarma`
--

CREATE TABLE `alarma` (
  `id_alarma` int(11) NOT NULL,
  `id_tratamiento` int(11) DEFAULT NULL,
  `medicamento` varchar(255) DEFAULT NULL,
  `dosis` varchar(50) DEFAULT NULL,
  `hora_primera_toma` time DEFAULT NULL,
  `tomas_al_dia` int(11) DEFAULT NULL,
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alarma`
--

INSERT INTO `alarma` (`id_alarma`, `id_tratamiento`, `medicamento`, `dosis`, `hora_primera_toma`, `tomas_al_dia`, `fecha_inicio`, `fecha_fin`) VALUES
(1, 1, 'Hibuprofeno', '1', '19:52:00', 2, '2024-02-22', '2024-02-29'),
(2, 2, 'Hibuprofeno', '1', '14:26:00', 2, '2024-02-22', '2024-02-24'),
(3, 3, 'Hibuprofeno', '1', '13:31:00', 2, '2024-02-24', '2024-02-25');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asignaciones`
--

CREATE TABLE `asignaciones` (
  `DNIDoctor` varchar(20) NOT NULL,
  `DNIPaciente` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `asignaciones`
--

INSERT INTO `asignaciones` (`DNIDoctor`, `DNIPaciente`) VALUES
('08366085L', '08366085L'),
('08366085L', '71042723R');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `citas`
--

CREATE TABLE `citas` (
  `id` int(11) NOT NULL,
  `fecha_hora` datetime DEFAULT NULL,
  `duracion` int(11) DEFAULT NULL,
  `doctor_dni` varchar(20) DEFAULT NULL,
  `paciente_dni` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `doctores`
--

CREATE TABLE `doctores` (
  `dni` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellidos` varchar(50) NOT NULL,
  `fecha_nacimiento` date NOT NULL,
  `domicilio` varchar(100) NOT NULL,
  `codigo_postal` varchar(10) NOT NULL,
  `numero_telefono` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `doctores`
--

INSERT INTO `doctores` (`dni`, `email`, `password`, `nombre`, `apellidos`, `fecha_nacimiento`, `domicilio`, `codigo_postal`, `numero_telefono`) VALUES
('08366085L', 'frantora@ucm.es', '10fa80f84cd4f98785f5865c0b5da7dbc73d605cbf24d3766bbdb05fd9651672', 'Francisco Javier', 'Antoranz Esteban', '2002-01-02', 'Calle Ocaña', '28047', '618495616');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pacientes`
--

CREATE TABLE `pacientes` (
  `email` varchar(255) DEFAULT NULL,
  `Apellidos` varchar(255) DEFAULT NULL,
  `CodigoPostal` varchar(10) DEFAULT NULL,
  `dni` varchar(20) NOT NULL,
  `Direccion` varchar(255) DEFAULT NULL,
  `FechaDeNacimiento` date DEFAULT NULL,
  `Nombre` varchar(255) DEFAULT NULL,
  `telefono` varchar(15) DEFAULT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pacientes`
--

INSERT INTO `pacientes` (`email`, `Apellidos`, `CodigoPostal`, `dni`, `Direccion`, `FechaDeNacimiento`, `Nombre`, `telefono`, `password`) VALUES
('sergiosan112@gmail.com', 'Sanchez Chamizo ', '28012', '08366085L', 'calle las rosas ', '2000-01-08', 'Sergio ', '616859670', '5a0cbbd4b4d0149dbaf8e880e3c0edd949db943b3b80599fc38dd6ffedfa5a1c'),
('fjavierantoranz@gmail.com', 'Antoranz', '28012', '71042723R', 'calle las rosas ', '2024-01-29', 'javi', '', '7e3e5098c7f185c615c158ccfcf736bd857ed540e4b6da185452e7afe76c6651');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `sessions`
--

CREATE TABLE `sessions` (
  `session_id` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `expires` int(11) UNSIGNED NOT NULL,
  `data` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `sessions`
--

INSERT INTO `sessions` (`session_id`, `expires`, `data`) VALUES
('9rLVleauKHMpoNF_9zv-82C5o8Mx_e_o', 1708951430, '{\"cookie\":{\"originalMaxAge\":null,\"expires\":null,\"httpOnly\":true,\"path\":\"/\"},\"currentUser\":{\"dni\":\"08366085L\",\"email\":\"frantora@ucm.es\",\"password\":\"10fa80f84cd4f98785f5865c0b5da7dbc73d605cbf24d3766bbdb05fd9651672\",\"nombre\":\"Francisco Javier\",\"apellidos\":\"Antoranz Esteban\",\"fecha_nacimiento\":\"2002-01-01T23:00:00.000Z\",\"domicilio\":\"Calle Ocaña\",\"codigo_postal\":\"28047\",\"numero_telefono\":\"618495616\",\"validado\":true}}'),
('kHhXavN1uPs_ytOrF4_NVXJsMxsfwjVh', 1708976750, '{\"cookie\":{\"originalMaxAge\":null,\"expires\":null,\"httpOnly\":true,\"path\":\"/\"},\"currentUser\":{\"dni\":\"08366085L\",\"email\":\"frantora@ucm.es\",\"password\":\"10fa80f84cd4f98785f5865c0b5da7dbc73d605cbf24d3766bbdb05fd9651672\",\"nombre\":\"Francisco Javier\",\"apellidos\":\"Antoranz Esteban\",\"fecha_nacimiento\":\"2002-01-01T23:00:00.000Z\",\"domicilio\":\"Calle Ocaña\",\"codigo_postal\":\"28047\",\"numero_telefono\":\"618495616\",\"validado\":true}}'),
('uefg6Et0vqmiMZWbQkdZrEF8nhqwpfyi', 1708898680, '{\"cookie\":{\"originalMaxAge\":null,\"expires\":null,\"httpOnly\":true,\"path\":\"/\"},\"currentUser\":{\"dni\":\"08366085L\",\"email\":\"frantora@ucm.es\",\"password\":\"10fa80f84cd4f98785f5865c0b5da7dbc73d605cbf24d3766bbdb05fd9651672\",\"nombre\":\"Francisco Javier\",\"apellidos\":\"Antoranz Esteban\",\"fecha_nacimiento\":\"2002-01-01T23:00:00.000Z\",\"domicilio\":\"Calle Ocaña\",\"codigo_postal\":\"28047\",\"numero_telefono\":\"618495616\",\"validado\":true}}');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tratamiento`
--

CREATE TABLE `tratamiento` (
  `id_tratamiento` int(11) NOT NULL,
  `id_paciente` varchar(20) DEFAULT NULL,
  `id_doctor` varchar(20) DEFAULT NULL,
  `diagnostico` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `tratamiento`
--

INSERT INTO `tratamiento` (`id_tratamiento`, `id_paciente`, `id_doctor`, `diagnostico`) VALUES
(1, '71042723R', '08366085L', 'Dolor de Cabeza'),
(2, '08366085L', '08366085L', 'Dolor de cabeza'),
(3, '71042723R', '08366085L', 'Dolor de cojones');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alarma`
--
ALTER TABLE `alarma`
  ADD PRIMARY KEY (`id_alarma`),
  ADD KEY `id_tratamiento` (`id_tratamiento`);

--
-- Indices de la tabla `asignaciones`
--
ALTER TABLE `asignaciones`
  ADD PRIMARY KEY (`DNIDoctor`,`DNIPaciente`);

--
-- Indices de la tabla `citas`
--
ALTER TABLE `citas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `doctor_dni` (`doctor_dni`),
  ADD KEY `paciente_dni` (`paciente_dni`);

--
-- Indices de la tabla `doctores`
--
ALTER TABLE `doctores`
  ADD PRIMARY KEY (`dni`);

--
-- Indices de la tabla `pacientes`
--
ALTER TABLE `pacientes`
  ADD PRIMARY KEY (`dni`);

--
-- Indices de la tabla `sessions`
--
ALTER TABLE `sessions`
  ADD PRIMARY KEY (`session_id`);

--
-- Indices de la tabla `tratamiento`
--
ALTER TABLE `tratamiento`
  ADD PRIMARY KEY (`id_tratamiento`),
  ADD KEY `id_paciente` (`id_paciente`),
  ADD KEY `id_doctor` (`id_doctor`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alarma`
--
ALTER TABLE `alarma`
  MODIFY `id_alarma` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `citas`
--
ALTER TABLE `citas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `tratamiento`
--
ALTER TABLE `tratamiento`
  MODIFY `id_tratamiento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `alarma`
--
ALTER TABLE `alarma`
  ADD CONSTRAINT `alarma_ibfk_1` FOREIGN KEY (`id_tratamiento`) REFERENCES `tratamiento` (`id_tratamiento`);

--
-- Filtros para la tabla `citas`
--
ALTER TABLE `citas`
  ADD CONSTRAINT `citas_ibfk_1` FOREIGN KEY (`doctor_dni`) REFERENCES `doctores` (`dni`),
  ADD CONSTRAINT `citas_ibfk_2` FOREIGN KEY (`paciente_dni`) REFERENCES `pacientes` (`dni`);

--
-- Filtros para la tabla `tratamiento`
--
ALTER TABLE `tratamiento`
  ADD CONSTRAINT `tratamiento_ibfk_1` FOREIGN KEY (`id_paciente`) REFERENCES `pacientes` (`dni`),
  ADD CONSTRAINT `tratamiento_ibfk_2` FOREIGN KEY (`id_doctor`) REFERENCES `doctores` (`dni`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
