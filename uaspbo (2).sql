-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 18, 2025 at 09:21 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `uaspbo`
--

-- --------------------------------------------------------

--
-- Table structure for table `materi`
--

CREATE TABLE `materi` (
  `id` int(11) NOT NULL,
  `title` varchar(200) NOT NULL,
  `description` text DEFAULT NULL,
  `file_path` varchar(500) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `materi`
--

INSERT INTO `materi` (`id`, `title`, `description`, `file_path`, `created_at`, `updated_at`) VALUES
(2, 'tes', 'tes', NULL, '2025-06-16 06:10:23', '2025-06-16 09:49:18'),
(3, 'Bahasa Indonesia', 'Penggunaan Kata Baku dan Tidak Baku', 'C:\\Users\\ASUS\\Downloads\\Sistem rancang web.pdf', '2025-06-16 07:56:24', '2025-06-16 14:28:12'),
(4, 'Matematika', 'Deret Angka', NULL, '2025-06-16 07:57:07', '2025-06-16 09:23:45'),
(5, 'tes', 'tes1', 'C:\\Users\\ASUS\\Documents\\2305181087_Tugas4_DW.pdf', '2025-06-16 09:49:31', '2025-06-16 09:49:31'),
(6, 'AI', '-', 'C:\\Users\\ASUS\\Documents\\klh\\S4\\AI\\3416-7900-1-PB.pdf', '2025-06-16 09:55:52', '2025-06-16 09:55:52'),
(7, 'Logika Pemograman', 'Belajar Tentang Logika dan Alur Pemograman(Flowchart)', 'C:\\Users\\ASUS\\Documents\\cici\\LAMPIRAN BENDAHARA PANITIA CSA\'22.pdf', '2025-06-18 18:58:39', '2025-06-18 18:58:39');

-- --------------------------------------------------------

--
-- Table structure for table `nilai`
--

CREATE TABLE `nilai` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `soal_id` int(11) NOT NULL,
  `score` decimal(5,2) NOT NULL DEFAULT 0.00,
  `answered_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `nilai`
--

INSERT INTO `nilai` (`id`, `user_id`, `soal_id`, `score`, `answered_at`) VALUES
(7, 2, 5, 1.00, '2025-06-18 07:55:23'),
(8, 2, 7, 1.00, '2025-06-18 19:02:22'),
(9, 2, 6, 0.00, '2025-06-18 19:02:29');

-- --------------------------------------------------------

--
-- Table structure for table `soal`
--

CREATE TABLE `soal` (
  `id` int(11) NOT NULL,
  `materi_id` int(11) NOT NULL,
  `question` text NOT NULL,
  `option_a` varchar(500) NOT NULL,
  `option_b` varchar(500) NOT NULL,
  `option_c` varchar(500) NOT NULL,
  `option_d` varchar(500) NOT NULL,
  `correct_option` enum('A','B','C','D') NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `soal`
--

INSERT INTO `soal` (`id`, `materi_id`, `question`, `option_a`, `option_b`, `option_c`, `option_d`, `correct_option`, `created_at`, `updated_at`) VALUES
(5, 3, 'Apa itu ikan mas', 'A. Hewan', 'B. Amfibi', 'C. Reptile', 'D. Unggas', 'B', '2025-06-16 14:12:20', '2025-06-16 14:14:29'),
(6, 7, 'Apa simbol dari awal dan akhir sebuah flowchart', 'trapesium', 'persegi panjang', 'persegi', 'tabung', 'C', '2025-06-18 18:59:45', '2025-06-18 18:59:45'),
(7, 7, 'Apa istilah lain dari diagram alir', 'flowchart', 'use case', 'diagram', 'UML', 'A', '2025-06-18 19:00:35', '2025-06-18 19:00:35');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('admin','student') NOT NULL DEFAULT 'student',
  `profile_picture` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`, `profile_picture`, `created_at`, `updated_at`) VALUES
(1, 'admin', '0192023a7bbd73250516f069df18b500', 'admin', 'C:\\Users\\ASUS\\Pictures\\2024-02-16 20-06-50.png', '2025-06-16 06:10:23', '2025-06-16 16:49:05'),
(2, 'siswa1', '3afa0d81296a4f17d477ec823261b1ec', 'student', NULL, '2025-06-16 06:10:23', '2025-06-16 06:10:23'),
(3, 'siswa2', '123', 'student', 'profile_pics\\AMD_AM5_Socket.png', '2025-06-18 19:01:37', '2025-06-18 19:01:37'),
(4, 'siswa3', '81dc9bdb52d04dc20036dbd8313ed055', 'student', 'profile_pics\\download.png', '2025-06-18 19:14:46', '2025-06-18 19:14:46');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `materi`
--
ALTER TABLE `materi`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `nilai`
--
ALTER TABLE `nilai`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_user_soal` (`user_id`,`soal_id`),
  ADD KEY `soal_id` (`soal_id`);

--
-- Indexes for table `soal`
--
ALTER TABLE `soal`
  ADD PRIMARY KEY (`id`),
  ADD KEY `materi_id` (`materi_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `materi`
--
ALTER TABLE `materi`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `nilai`
--
ALTER TABLE `nilai`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `soal`
--
ALTER TABLE `soal`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `nilai`
--
ALTER TABLE `nilai`
  ADD CONSTRAINT `nilai_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_ibfk_2` FOREIGN KEY (`soal_id`) REFERENCES `soal` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `soal`
--
ALTER TABLE `soal`
  ADD CONSTRAINT `soal_ibfk_1` FOREIGN KEY (`materi_id`) REFERENCES `materi` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
