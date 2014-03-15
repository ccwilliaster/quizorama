SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `c_cs108_aspanu` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
SHOW WARNINGS;
USE `c_cs108_aspanu` ;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`users` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`users` (
  `userID` INT NOT NULL AUTO_INCREMENT,
  `userName` VARCHAR(255) NULL,
  `dateJoined` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `password` VARCHAR(255) NULL,
  `cookie` VARCHAR(64) NULL,
  PRIMARY KEY (`userID`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`quizzes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`quizzes` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`quizzes` (
  `quizID` INT NOT NULL AUTO_INCREMENT,
  `quizName` VARCHAR(255) NULL,
  `quizCreation` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `quizCreatorUserID` INT NOT NULL,
  `singlePage?` TINYINT(1) NULL,
  `randomOrder?` TINYINT(1) NULL,
  `immediateCorrection?` TINYINT(1) NULL,
  `practiceMode?` TINYINT(1) NULL,
  PRIMARY KEY (`quizID`),
  CONSTRAINT `fk_quizzes_users1`
    FOREIGN KEY (`quizCreatorUserID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_quizzes_users1_idx` ON `c_cs108_aspanu`.`quizzes` (`quizCreatorUserID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`questionTypes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`questionTypes` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`questionTypes` (
  `questionTypeID` INT NOT NULL AUTO_INCREMENT,
  `questionTypeName` VARCHAR(255) NULL,
  `questionTypeDescription` VARCHAR(255) NULL,
  PRIMARY KEY (`questionTypeID`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`quizQuestions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`quizQuestions` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`quizQuestions` (
  `questionID` INT NOT NULL AUTO_INCREMENT,
  `quizID` INT NOT NULL,
  `questionTypeID` INT NOT NULL,
  `question` VARCHAR(8000) NULL,
  `questionNumber` INT NULL,
  PRIMARY KEY (`questionID`),
  CONSTRAINT `fk_quizQuestions_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `c_cs108_aspanu`.`quizzes` (`quizID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizQuestions_questionTypes1`
    FOREIGN KEY (`questionTypeID`)
    REFERENCES `c_cs108_aspanu`.`questionTypes` (`questionTypeID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_quizQuestions_quizzes1_idx` ON `c_cs108_aspanu`.`quizQuestions` (`quizID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_quizQuestions_questionTypes1_idx` ON `c_cs108_aspanu`.`quizQuestions` (`questionTypeID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`quizAnswers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`quizAnswers` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`quizAnswers` (
  `answerID` INT NOT NULL AUTO_INCREMENT,
  `questionID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `answer` VARCHAR(8000) NULL,
  PRIMARY KEY (`answerID`),
  CONSTRAINT `fk_quizAnswers_quizQuestions1`
    FOREIGN KEY (`questionID`)
    REFERENCES `c_cs108_aspanu`.`quizQuestions` (`questionID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizAnswers_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `c_cs108_aspanu`.`quizzes` (`quizID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_quizAnswers_quizQuestions1_idx` ON `c_cs108_aspanu`.`quizAnswers` (`questionID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_quizAnswers_quizzes1_idx` ON `c_cs108_aspanu`.`quizAnswers` (`quizID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`scores`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`scores` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`scores` (
  `scoreID` INT NOT NULL AUTO_INCREMENT,
  `userID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `score` INT NULL,
  PRIMARY KEY (`scoreID`),
  CONSTRAINT `fk_scores_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_scores_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `c_cs108_aspanu`.`quizzes` (`quizID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_scores_users1_idx` ON `c_cs108_aspanu`.`scores` (`userID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_scores_quizzes1_idx` ON `c_cs108_aspanu`.`scores` (`quizID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`friendships`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`friendships` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`friendships` (
  `friendshipID` INT NOT NULL AUTO_INCREMENT,
  `userID` INT NOT NULL COMMENT 'When inserting into this table, both friends should be included in the userID column (i.e. one friendship between user 1 and user 5 will create two rows: \"1, 5\" and \"5, 1\"',
  `frienduserID` INT NOT NULL,
  PRIMARY KEY (`friendshipID`),
  CONSTRAINT `fk_friendships_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_friendships_users2`
    FOREIGN KEY (`frienduserID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_friendships_users1_idx` ON `c_cs108_aspanu`.`friendships` (`userID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_friendships_users2_idx` ON `c_cs108_aspanu`.`friendships` (`frienduserID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`categories` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`categories` (
  `categoryID` INT NOT NULL AUTO_INCREMENT,
  `categoryName` VARCHAR(255) NULL,
  PRIMARY KEY (`categoryID`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`quizCategories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`quizCategories` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`quizCategories` (
  `quizCategoryID` INT NOT NULL AUTO_INCREMENT,
  `quizID` INT NOT NULL,
  `categoryID` INT NOT NULL,
  PRIMARY KEY (`quizCategoryID`),
  CONSTRAINT `fk_quizCategories_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `c_cs108_aspanu`.`quizzes` (`quizID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizCategories_categories1`
    FOREIGN KEY (`categoryID`)
    REFERENCES `c_cs108_aspanu`.`categories` (`categoryID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_quizCategories_quizzes1_idx` ON `c_cs108_aspanu`.`quizCategories` (`quizID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_quizCategories_categories1_idx` ON `c_cs108_aspanu`.`quizCategories` (`categoryID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`tags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`tags` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`tags` (
  `tagID` INT NOT NULL AUTO_INCREMENT,
  `tagName` VARCHAR(255) NULL,
  PRIMARY KEY (`tagID`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`quizTags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`quizTags` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`quizTags` (
  `quizTagID` INT NOT NULL AUTO_INCREMENT,
  `quizID` INT NOT NULL,
  `tagID` INT NOT NULL,
  PRIMARY KEY (`quizTagID`),
  CONSTRAINT `fk_quizTags_tags1`
    FOREIGN KEY (`tagID`)
    REFERENCES `c_cs108_aspanu`.`tags` (`tagID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizTags_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `c_cs108_aspanu`.`quizzes` (`quizID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_quizTags_tags1_idx` ON `c_cs108_aspanu`.`quizTags` (`tagID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_quizTags_quizzes1_idx` ON `c_cs108_aspanu`.`quizTags` (`quizID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`quizHistory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`quizHistory` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`quizHistory` (
  `quizHistoryID` INT NOT NULL AUTO_INCREMENT,
  `quizID` INT NOT NULL,
  `userID` INT NOT NULL,
  `dateTaken` DATETIME NULL,
  `score` INT NULL,
  `completed?` TINYINT(1) NULL,
  `timeStamp` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`quizHistoryID`),
  CONSTRAINT `fk_quizHistory_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `c_cs108_aspanu`.`quizzes` (`quizID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizHistory_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_quizHistory_quizzes1_idx` ON `c_cs108_aspanu`.`quizHistory` (`quizID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_quizHistory_users1_idx` ON `c_cs108_aspanu`.`quizHistory` (`userID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`messages`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`message` ;
DROP TABLE IF EXISTS `c_cs108_aspanu`.`messages` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`messages` (
  `messageID` INT NOT NULL AUTO_INCREMENT,
  `messageType` TINYINT NULL,
  `toUserID` INT NOT NULL,
  `fromUserID` INT NOT NULL,
  `subject` VARCHAR(255) NULL,
  `content` VARCHAR(8000) NULL,
  `date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `messageRead` TINYINT(1) NULL,
  `toUserDeleted` TINYINT(1) NULL,
  `fromUserDeleted` TINYINT(1) NULL,
  PRIMARY KEY (`messageID`),
  CONSTRAINT `fk_mailMessage_users`
    FOREIGN KEY (`toUserID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_mailMessage_users1`
    FOREIGN KEY (`fromUserID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_mailMessage_users_idx` ON `c_cs108_aspanu`.`messages` (`toUserID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_mailMessage_users1_idx` ON `c_cs108_aspanu`.`messages` (`fromUserID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`achievements`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`achievements` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`achievements` (
  `achievementID` INT NOT NULL AUTO_INCREMENT,
  `achievementName` VARCHAR(255) NULL,
  `achievementDescription` VARCHAR(8000) NULL,
  PRIMARY KEY (`achievementID`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`userAchievements`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`userAchievements` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`userAchievements` (
  `userAchievementID` INT NOT NULL AUTO_INCREMENT,
  `userID` INT NOT NULL,
  `achievementID` INT NOT NULL,
  `dateAchieved` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userAchievementID`),
  CONSTRAINT `fk_userAchievements_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userAchievements_achievements1`
    FOREIGN KEY (`achievementID`)
    REFERENCES `c_cs108_aspanu`.`achievements` (`achievementID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_userAchievements_users1_idx` ON `c_cs108_aspanu`.`userAchievements` (`userID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_userAchievements_achievements1_idx` ON `c_cs108_aspanu`.`userAchievements` (`achievementID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`userQuizRatings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`userQuizRatings` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`userQuizRatings` (
  `ratingID` INT NOT NULL AUTO_INCREMENT,
  `userID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `ratingValue` TINYINT NULL,
  `ratingReview` VARCHAR(255) NULL,
  PRIMARY KEY (`ratingID`),
  CONSTRAINT `fk_userQuizRatings_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userQuizRatings_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `c_cs108_aspanu`.`quizzes` (`quizID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_userQuizRatings_users1_idx` ON `c_cs108_aspanu`.`userQuizRatings` (`userID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_userQuizRatings_quizzes1_idx` ON `c_cs108_aspanu`.`userQuizRatings` (`quizID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`quizQuestionHistory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`quizQuestionHistory` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`quizQuestionHistory` (
  `quizQuestionHistoryID` INT NOT NULL AUTO_INCREMENT,
  `userID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `questionID` INT NOT NULL,
  `score` INT NULL,
  `timeTaken` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `timeStamp` DATETIME NULL,
  PRIMARY KEY (`quizQuestionHistoryID`),
  CONSTRAINT `fk_quizQuestionHistory_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizQuestionHistory_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `c_cs108_aspanu`.`quizzes` (`quizID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizQuestionHistory_quizQuestions1`
    FOREIGN KEY (`questionID`)
    REFERENCES `c_cs108_aspanu`.`quizQuestions` (`questionID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_quizQuestionHistory_users1_idx` ON `c_cs108_aspanu`.`quizQuestionHistory` (`userID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_quizQuestionHistory_quizzes1_idx` ON `c_cs108_aspanu`.`quizQuestionHistory` (`quizID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_quizQuestionHistory_quizQuestions1_idx` ON `c_cs108_aspanu`.`quizQuestionHistory` (`questionID` ASC);

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`types`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`types` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`types` (
  `typeID` INT NOT NULL AUTO_INCREMENT,
  `typeName` VARCHAR(10) NULL,
  `typeDescription` VARCHAR(255) NULL,
  PRIMARY KEY (`typeID`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `c_cs108_aspanu`.`userTypes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `c_cs108_aspanu`.`userTypes` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `c_cs108_aspanu`.`userTypes` (
  `userTypeID` INT NOT NULL AUTO_INCREMENT,
  `userID` INT NOT NULL,
  `typeID` INT NOT NULL,
  PRIMARY KEY (`userTypeID`),
  CONSTRAINT `fk_userTypes_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `c_cs108_aspanu`.`users` (`userID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userTypes_types1`
    FOREIGN KEY (`typeID`)
    REFERENCES `c_cs108_aspanu`.`types` (`typeID`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_userTypes_users1_idx` ON `c_cs108_aspanu`.`userTypes` (`userID` ASC);

SHOW WARNINGS;
CREATE INDEX `fk_userTypes_types1_idx` ON `c_cs108_aspanu`.`userTypes` (`typeID` ASC);

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
