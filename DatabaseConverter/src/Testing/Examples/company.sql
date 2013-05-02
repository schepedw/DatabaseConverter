-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (i686)
--
-- Host: localhost    Database: company
-- ------------------------------------------------------
-- Server version	5.5.31-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Department`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Department` (
  `Dname` int(11) NOT NULL,
  `Dnumber` int(11) NOT NULL,
  `MgrSSN` int(11) NOT NULL,
  `MgrStartDate` int(11) NOT NULL,
  PRIMARY KEY (`Dnumber`),
  UNIQUE KEY `MgrSSN` (`MgrSSN`),
  CONSTRAINT `Department_ibfk_2` FOREIGN KEY (`MgrSSN`) REFERENCES `Employee` (`SSN`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Dependent`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Dependent` (
  `ESSN` int(11) NOT NULL,
  `Dependent_Name` varchar(30) NOT NULL,
  `Sex` varchar(1) NOT NULL,
  `Bdate` int(11) NOT NULL,
  PRIMARY KEY (`ESSN`,`Dependent_Name`),
  CONSTRAINT `Dependent_ibfk_2` FOREIGN KEY (`ESSN`) REFERENCES `Employee` (`SSN`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Dept_Locations`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Dept_Locations` (
  `Dnumber` int(11) NOT NULL,
  `Dlocation` varchar(50) NOT NULL,
  PRIMARY KEY (`Dnumber`,`Dlocation`),
  KEY `Dnumber` (`Dnumber`),
  CONSTRAINT `Dept_Locations_ibfk_2` FOREIGN KEY (`Dnumber`) REFERENCES `Department` (`Dnumber`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Employee`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Employee` (
  `Fname` varchar(20) NOT NULL,
  `Minit` varchar(1) NOT NULL,
  `Lname` varchar(30) NOT NULL,
  `SSN` int(11) NOT NULL,
  `Bdate` int(11) NOT NULL,
  `Address` varchar(50) NOT NULL,
  `Sex` varchar(1) NOT NULL,
  `Salary` int(11) NOT NULL,
  `SuperSSN` int(11) NOT NULL,
  `Dno` int(11) NOT NULL,
  PRIMARY KEY (`SSN`),
  KEY `SuperSSN` (`SuperSSN`),
  KEY `Dno` (`Dno`),
  CONSTRAINT `Employee_ibfk_2` FOREIGN KEY (`Dno`) REFERENCES `Department` (`Dnumber`) ON UPDATE CASCADE,
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Project`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Project` (
  `Pname` varchar(20) NOT NULL,
  `Pnumber` int(11) NOT NULL,
  `Plocation` varchar(20) NOT NULL,
  `Dno` int(11) NOT NULL,
  PRIMARY KEY (`Pnumber`),
  KEY `Dno` (`Dno`),
  CONSTRAINT `Project_ibfk_1` FOREIGN KEY (`Dno`) REFERENCES `Department` (`Dnumber`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Works_On`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Works_On` (
  `ESSN` int(11) NOT NULL,
  `Pno` int(11) NOT NULL,
  `Hours` int(11) NOT NULL,
  PRIMARY KEY (`ESSN`,`Pno`),
  KEY `Pno` (`Pno`),
  CONSTRAINT `Works_On_ibfk_2` FOREIGN KEY (`Pno`) REFERENCES `Project` (`Pnumber`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `Works_On_ibfk_1` FOREIGN KEY (`ESSN`) REFERENCES `Employee` (`SSN`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-01 21:45:29
