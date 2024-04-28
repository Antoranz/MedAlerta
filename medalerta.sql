-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 28-04-2024 a las 21:51:08
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

DELIMITER $$
--
-- Procedimientos
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `ComprobarCita` (IN `nueva_fecha_hora` DATETIME, IN `nueva_duracion` INT, IN `dniDoctor` VARCHAR(20))   BEGIN
    DECLARE nueva_fecha_fin DATETIME;

    -- Calcula la fecha de finalización de la nueva cita sumando la duración
    SET nueva_fecha_fin = ADDTIME(nueva_fecha_hora, SEC_TO_TIME(nueva_duracion * 60));

    -- Devuelve las citas coincidentes en el intervalo de tiempo
    SELECT *
    FROM citas
    WHERE doctor_dni = dniDoctor
    AND (
        -- Verifica si la nueva cita se superpone con alguna cita existente
        (nueva_fecha_hora BETWEEN fecha_hora AND ADDTIME(fecha_hora, SEC_TO_TIME(duracion * 60)))
        OR(nueva_fecha_fin BETWEEN fecha_hora AND ADDTIME(fecha_hora, SEC_TO_TIME(duracion * 60)))
        OR(nueva_fecha_hora <= fecha_hora AND nueva_fecha_fin >= ADDTIME(fecha_hora, SEC_TO_TIME(duracion * 60)))
    );
END$$

DELIMITER ;

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
('08366085L', '49249480V');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `citas`
--

CREATE TABLE `citas` (
  `id` int(11) NOT NULL,
  `fecha_hora` datetime DEFAULT NULL,
  `duracion` int(11) DEFAULT NULL,
  `doctor_dni` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `paciente_dni` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_spanish_ci;

--
-- Volcado de datos para la tabla `citas`
--

INSERT INTO `citas` (`id`, `fecha_hora`, `duracion`, `doctor_dni`, `paciente_dni`) VALUES
(24, '2024-04-30 15:30:00', 20, '08366085L', '49249480V'),
(25, '2024-04-30 17:30:00', 20, '08366085L', '49249480V'),
(26, '2024-04-30 18:30:00', 12, '08366085L', '49249480V'),
(27, '2024-04-30 20:30:00', 12, '08366085L', '49249480V'),
(29, '2024-05-14 05:32:00', 12, '08366085L', '49249480V'),
(30, '2024-04-30 19:30:00', 15, '08366085L', '49249480V'),
(31, '2024-04-30 16:00:00', 15, '08366085L', '49249480V'),
(36, '2024-04-30 09:26:00', 10, '08366085L', '49249480V');

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
('08366085L', 'sergiosan112@gmail.com', '42fe64654aaa1c13db56c624f436d5b56dae74155ba69bbc03c7338896edbe12', 'Sergio', 'Sánchez Frantora', '2000-01-08', 'Calle vicente camaron 44', '06700', '616859670', '');

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
('08366085L', 'Eczema'),
('08366085L', 'Ansiedad');

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

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `notificaciones`
--

CREATE TABLE `notificaciones` (
  `id` int(11) NOT NULL,
  `tipo` varchar(255) DEFAULT NULL,
  `motivo` varchar(255) NOT NULL,
  `fecha_hora` datetime DEFAULT NULL,
  `doctor_dni` varchar(20) DEFAULT NULL,
  `paciente_dni` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
('frantora@ucm.es', 'Antoranz', '06700', '49249480V', 'Calle las rosas', '2001-04-19', 'Francisc0o Javier', '616859670', 'f3981b64446c3bcca20de6cd1dda123b9ab1796cf163c9e1b162fe3080180520', '');

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
('otRYJ4fULDFXKTgUfyU8obSFuAR3ouQv', 1714409867, '{\"cookie\":{\"originalMaxAge\":null,\"expires\":null,\"httpOnly\":true,\"path\":\"/\"},\"currentUser\":{\"dni\":\"08366085L\",\"email\":\"sergiosan112@gmail.com\",\"nombre\":\"Sergio\",\"apellidos\":\"Sánchez Frantora\",\"fecha_nacimiento\":\"2000-01-08\",\"domicilio\":\"Calle vicente camaron 44\",\"codigo_postal\":\"06700\",\"numero_telefono\":\"616859670\",\"imagen\":\"\",\"validado\":true}}'),
('4OJyHmjr5SGg2ef5_yizGMOQZ3KQE9m2', 1714420228, '{\"cookie\":{\"originalMaxAge\":null,\"expires\":null,\"httpOnly\":true,\"path\":\"/\"},\"currentUser\":{\"dni\":\"08366085L\",\"email\":\"sergiosan112@gmail.com\",\"nombre\":\"Sergio\",\"apellidos\":\"Sánchez Frantora\",\"fecha_nacimiento\":\"2000-01-08\",\"domicilio\":\"Calle vicente camaron 44\",\"codigo_postal\":\"06700\",\"numero_telefono\":\"616859670\",\"imagen\":\"\",\"validado\":true}}');

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
(17, '49249480V', '08366085L', 'javi tiene fiebre'),
(18, '49249480V', '08366085L', 'prueba2'),
(19, '49249480V', '08366085L', 'prueba 4'),
(20, '49249480V', '08366085L', 'paracetamol');

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
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `citas`
--
ALTER TABLE `citas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
