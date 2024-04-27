-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 27-04-2024 a las 16:43:29
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
(23, 16, 'enantyum', '5 petas', '22:42:00', 3, '2024-02-26', '2024-02-27');

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
('08366085L', '71042723R'),
('71042723R', '08366085L');

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

--
-- Volcado de datos para la tabla `citas`
--

INSERT INTO `citas` (`id`, `fecha_hora`, `duracion`, `doctor_dni`, `paciente_dni`) VALUES
(6, '2024-02-28 14:26:00', 30, '08366085L', '08366085L');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `consultas`
--

CREATE TABLE `consultas` (
  `id` int(11) NOT NULL,
  `dni_doctor` varchar(9) NOT NULL,
  `dni_paciente` varchar(9) NOT NULL,
  `titulo` varchar(255) NOT NULL,
  `ultima_fecha` datetime NOT NULL,
  `mensajes_totales` int(11) NOT NULL DEFAULT 0,
  `notificaciones_doctor` int(11) NOT NULL DEFAULT 0,
  `notificaciones_paciente` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `consultas`
--

INSERT INTO `consultas` (`id`, `dni_doctor`, `dni_paciente`, `titulo`, `ultima_fecha`, `mensajes_totales`, `notificaciones_doctor`, `notificaciones_paciente`) VALUES
(1, '', '', '', '0000-00-00 00:00:00', 0, 0, 0),
(2, '08366085L', '08366085L', 'Daltonismo', '2024-03-19 00:00:00', 2, 2, 0),
(7, '71042723R', '08366085L', 'espartaco', '2024-04-11 01:15:26', 0, 0, 0),
(8, '08366085L', '08366085L', 'buenas', '2024-04-10 23:17:27', 0, 0, 0),
(9, '08366085L', '08366085L', 'crack', '2024-04-11 08:52:36', 0, 0, 0);

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
  `numero_telefono` varchar(15) NOT NULL,
  `imagen` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `doctores`
--

INSERT INTO `doctores` (`dni`, `email`, `password`, `nombre`, `apellidos`, `fecha_nacimiento`, `domicilio`, `codigo_postal`, `numero_telefono`, `imagen`) VALUES
('08366085L', 'frantora@ucm.es', '10fa80f84cd4f98785f5865c0b5da7dbc73d605cbf24d3766bbdb05fd9651672', 'Francisco Javier', 'Antoranz Esteban', '2002-01-02', 'Calle Ocaña', '28047', '618495616', ''),
('18366085L', 'carlopen3@ucm.es', 'bf3568b9dcc3ed7da523255bd44e7bbaeaa1ce9343630a80e70cc006159d96fb', 'Carlos3', 'Gimenez Ch', '2007-02-27', 'Calle Guapa', '28001', '654898342', ''),
('71042723R', 'sergis13@ucm.es', 'c22cdd06ab5120cbea143d1b73b262195e00403f8e5421f4bbb65b93dd1e9028', 'Sergio', 'chamizo Sánchez', '2000-02-06', 'Calle vicente camaron 44', '28011', '616859670', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `enfermedades`
--

CREATE TABLE `enfermedades` (
  `doctor_dni` varchar(10) NOT NULL,
  `enfermedad` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `enfermedades`
--

INSERT INTO `enfermedades` (`doctor_dni`, `enfermedad`) VALUES
('18366085L', 'Diabetes'),
('18366085L', 'Hipertensión'),
('18366085L', 'Asma'),
('18366085L', 'Artritis'),
('18366085L', 'Gripe'),
('18366085L', 'Neumonía'),
('18366085L', 'Anemia'),
('18366085L', 'Migraña'),
('18366085L', 'Bronquitis'),
('18366085L', 'Depresión'),
('08366085L', 'daltonismo1'),
('08366085L', 'Daltonismo2'),
('08366085L', 'Diabetes'),
('08366085L', 'Hipertensión'),
('08366085L', 'Asma'),
('08366085L', 'Artritis'),
('08366085L', 'Gripe'),
('08366085L', 'Neumonía'),
('08366085L', 'Anemia'),
('08366085L', 'Migraña'),
('08366085L', 'Bronquitis'),
('08366085L', 'Depresión'),
('08366085L', 'Insuficiencia cardiaca'),
('08366085L', 'Osteoporosis'),
('08366085L', 'Epilepsia'),
('08366085L', 'Alzheimer'),
('08366085L', 'Parkinson'),
('08366085L', 'Esclerosis múltiple'),
('08366085L', 'Colesterol alto'),
('08366085L', 'Enfermedad de Crohn'),
('08366085L', 'Fibromialgia'),
('08366085L', 'Psoriasis'),
('08366085L', 'Alergia'),
('08366085L', 'Cataratas'),
('08366085L', 'Glaucoma'),
('08366085L', 'Hepatitis'),
('08366085L', 'Eczema');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mensajes`
--

CREATE TABLE `mensajes` (
  `id_consulta` int(11) NOT NULL,
  `mensaje` longtext NOT NULL,
  `propietario` int(1) NOT NULL,
  `fecha` datetime NOT NULL,
  `leido_doctor` int(11) NOT NULL,
  `leido_paciente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `mensajes`
--

INSERT INTO `mensajes` (`id_consulta`, `mensaje`, `propietario`, `fecha`, `leido_doctor`, `leido_paciente`) VALUES
(2, 'Sufro de daltonismo (soy paciente)', 1, '2024-03-19 21:15:45', 0, 0),
(2, 'Sufro de daltonismo (soy paciente)', 1, '2024-03-19 21:15:45', 0, 0),
(2, 'hola soy el medico', 0, '2024-03-20 21:15:45', 0, 0),
(2, 'este es un mensaje de prueba largo para ver como queda lo que quiero vale crack', 0, '2024-03-21 21:15:45', 0, 0),
(2, 'hola', 1, '2024-04-11 01:03:03', 0, 0),
(2, 'adios', 0, '2024-04-11 01:03:38', 0, 0),
(7, 'heyyy', 0, '2024-04-11 01:06:08', 0, 0),
(2, 'buenas', 0, '2024-04-11 01:07:26', 0, 0),
(2, 'czczczczdasdasdasd', 0, '2024-04-11 01:57:15', 0, 0),
(2, 'sadfasdf', 1, '2024-04-11 01:57:52', 0, 0),
(2, 'asdfasdf', 1, '2024-04-11 01:57:59', 0, 0),
(2, 'asdasdasd', 1, '2024-04-11 01:58:07', 0, 0),
(2, 'hola buenas', 1, '2024-04-11 01:58:31', 0, 0),
(2, 'holsd', 1, '2024-04-11 07:21:18', 0, 0),
(8, 'hgolasdasdf', 1, '2024-04-11 07:24:12', 0, 0),
(7, 'buenas', 1, '2024-04-11 07:24:50', 0, 0),
(7, 'soy', 1, '2024-04-11 07:24:56', 0, 0),
(7, 'hola buenos dias', 1, '2024-04-11 07:27:39', 0, 0),
(7, '', 1, '2024-04-11 07:27:42', 0, 0),
(7, '', 1, '2024-04-11 07:27:43', 0, 0),
(2, '', 1, '2024-04-11 07:30:25', 0, 0),
(2, '', 1, '2024-04-11 07:30:27', 0, 0),
(2, 'gdnyeg', 1, '2024-04-11 07:30:32', 0, 0),
(2, 'asdfasfasdfasfd', 1, '2024-04-11 07:33:32', 0, 0),
(2, 'a', 1, '2024-04-11 07:36:53', 0, 0),
(2, 'holasdas', 1, '2024-04-11 07:46:10', 0, 0),
(2, 'hola', 1, '2024-04-11 07:47:17', 0, 0),
(2, 'buenos dias', 1, '2024-04-11 07:47:25', 0, 0),
(2, 'adios', 1, '2024-04-11 07:47:36', 0, 0),
(2, 'heyyyyy', 1, '2024-04-11 07:49:35', 0, 0),
(2, 'a', 1, '2024-04-11 07:49:42', 0, 0),
(2, 'hola javi', 1, '2024-04-11 07:50:47', 0, 0),
(2, 'hey', 1, '2024-04-11 07:54:57', 0, 0),
(2, 'asdfhibaosifdbuasdf', 1, '2024-04-11 07:55:14', 0, 0),
(2, 'maaaaadriiiiiiiid', 1, '2024-04-11 07:56:08', 0, 0),
(2, 'alaaaa madriiiiid', 1, '2024-04-11 07:57:13', 0, 0),
(2, 'sfdgsdfg', 1, '2024-04-11 07:57:26', 0, 0),
(2, 'asdfasdfasdfasdf', 1, '2024-04-11 07:57:33', 0, 0),
(2, 'asdfasdfasfasfasdfasf', 1, '2024-04-11 07:57:40', 0, 0),
(2, 'hola buenos dias charck', 1, '2024-04-11 07:57:49', 0, 0),
(2, 'asdasdasdf', 1, '2024-04-11 07:57:57', 0, 0),
(2, 'asdfasfd', 1, '2024-04-11 07:58:04', 0, 0),
(2, '.....', 1, '2024-04-11 07:58:13', 0, 0),
(2, 'mira esto', 1, '2024-04-11 07:58:29', 0, 0),
(2, 'que chuol', 1, '2024-04-11 07:58:33', 0, 0),
(2, 'mira que guapo esto', 1, '2024-04-11 07:59:58', 0, 0),
(2, 'hola', 1, '2024-04-11 08:01:10', 0, 0),
(2, 'mria como funciona tonto', 1, '2024-04-11 08:01:38', 0, 0),
(2, 'me molesta', 1, '2024-04-11 08:02:19', 0, 0),
(2, 'hoal', 1, '2024-04-11 08:04:27', 0, 0),
(2, 'mi viejoooo chamartinnn', 1, '2024-04-11 08:04:56', 0, 0),
(2, 'hola', 1, '2024-04-11 08:05:38', 0, 0),
(2, 'adios', 1, '2024-04-11 08:05:46', 0, 0),
(2, 'asdfasd', 1, '2024-04-11 08:08:12', 0, 0),
(2, 'holiwi', 1, '2024-04-11 08:08:58', 0, 0),
(2, 'hry', 1, '2024-04-11 08:10:00', 0, 0),
(2, 'adios', 1, '2024-04-11 08:10:41', 0, 0),
(2, 'fdgsdgsdfg', 1, '2024-04-11 08:11:26', 0, 0),
(2, 'rfsfgrgsdfe', 1, '2024-04-11 08:15:07', 0, 0),
(8, 'asdfasd', 1, '2024-04-11 08:15:48', 0, 0),
(2, 'holaaaaa', 1, '2024-04-11 08:15:57', 0, 0),
(2, 'dsfg', 1, '2024-04-11 08:16:53', 0, 0),
(8, 'fdfhrtdy', 1, '2024-04-11 08:59:11', 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notificaciones`
--

CREATE TABLE `notificaciones` (
  `id` int(11) NOT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `fecha_hora` datetime DEFAULT NULL,
  `doctor_dni` varchar(20) DEFAULT NULL,
  `paciente_dni` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `notificaciones`
--

INSERT INTO `notificaciones` (`id`, `tipo`, `fecha_hora`, `doctor_dni`, `paciente_dni`) VALUES
(6, 'Cita', '2024-02-28 03:50:00', '08366085L', '08366085L');

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
  `password` varchar(255) NOT NULL,
  `imagen` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pacientes`
--

INSERT INTO `pacientes` (`email`, `Apellidos`, `CodigoPostal`, `dni`, `Direccion`, `FechaDeNacimiento`, `Nombre`, `telefono`, `password`, `imagen`) VALUES
('sergiosan112@gmail.com', 'Sanchez Chamizo ', '28012', '08366085L', 'calle las rosas ', '2000-01-08', 'Sergio ', '616859670', '10fa80f84cd4f98785f5865c0b5da7dbc73d605cbf24d3766bbdb05fd9651672', ''),
('fjavierantoranz@gmail.com', 'Antoranz', '28012', '71042723R', 'calle las rosas ', '2024-01-29', 'javi', '', '7e3e5098c7f185c615c158ccfcf736bd857ed540e4b6da185452e7afe76c6651', '');

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
('gFgTVMx4Z0PJWNepmKbXlOmsTCIkA3eM', 1714315371, '{\"cookie\":{\"originalMaxAge\":null,\"expires\":null,\"httpOnly\":true,\"path\":\"/\"},\"currentUser\":{\"dni\":\"08366085L\",\"email\":\"frantora@ucm.es\",\"nombre\":\"Francisco Javier\",\"apellidos\":\"Antoranz Esteban\",\"fecha_nacimiento\":\"2002-01-02\",\"domicilio\":\"Calle Ocaña\",\"codigo_postal\":\"28047\",\"numero_telefono\":\"618495616\",\"imagen\":\"\",\"validado\":true}}');

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
(16, '08366085L', '08366085L', 'fiebre tochita');

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
-- Indices de la tabla `consultas`
--
ALTER TABLE `consultas`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `doctores`
--
ALTER TABLE `doctores`
  ADD PRIMARY KEY (`dni`);

--
-- Indices de la tabla `notificaciones`
--
ALTER TABLE `notificaciones`
  ADD PRIMARY KEY (`id`);

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
  MODIFY `id_alarma` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de la tabla `citas`
--
ALTER TABLE `citas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `consultas`
--
ALTER TABLE `consultas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `notificaciones`
--
ALTER TABLE `notificaciones`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `tratamiento`
--
ALTER TABLE `tratamiento`
  MODIFY `id_tratamiento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

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
