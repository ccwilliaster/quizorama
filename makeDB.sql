SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `quizSite` ;
CREATE SCHEMA IF NOT EXISTS `quizSite` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `quizSite` ;

-- -----------------------------------------------------
-- Table `quizSite`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`users` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`users` (
  `userID` INT NOT NULL,
  `userName` VARCHAR(255) NULL,
  `dateJoined` DATETIME NULL,
  `password` VARCHAR(64) NULL,
  `salt` VARCHAR(64) NULL,
  `cookie` VARCHAR(64) NULL,
  PRIMARY KEY (`userID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizSite`.`quizzes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`quizzes` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`quizzes` (
  `quizID` INT NOT NULL,
  `quizName` VARCHAR(255) NULL,
  `quizCreation` DATETIME NULL,
  `quizCreatoruserID` INT NOT NULL,
  `singlePage?` TINYINT(1) NULL,
  `randomOrder?` TINYINT(1) NULL,
  `immediateCorrection?` TINYINT(1) NULL,
  `practiceMode?` TINYINT(1) NULL,
  PRIMARY KEY (`quizID`),
  CONSTRAINT `fk_quizzes_users1`
    FOREIGN KEY (`quizCreatoruserID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_quizzes_users1_idx` ON `quizSite`.`quizzes` (`quizCreatoruserID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`questionTypes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`questionTypes` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`questionTypes` (
  `questionTypeID` INT NOT NULL,
  `questionTypeName` VARCHAR(255) NULL,
  `questionTypeDescription` VARCHAR(255) NULL,
  PRIMARY KEY (`questionTypeID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizSite`.`quizQuestions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`quizQuestions` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`quizQuestions` (
  `questionID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `questionTypeID` INT NOT NULL,
  `question` VARCHAR(8000) NULL,
  `questionNumber` INT NULL,
  PRIMARY KEY (`questionID`),
  CONSTRAINT `fk_quizQuestions_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `quizSite`.`quizzes` (`quizID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizQuestions_questionTypes1`
    FOREIGN KEY (`questionTypeID`)
    REFERENCES `quizSite`.`questionTypes` (`questionTypeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_quizQuestions_quizzes1_idx` ON `quizSite`.`quizQuestions` (`quizID` ASC);

CREATE INDEX `fk_quizQuestions_questionTypes1_idx` ON `quizSite`.`quizQuestions` (`questionTypeID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`quizAnswers`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`quizAnswers` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`quizAnswers` (
  `answerID` INT NOT NULL,
  `questionID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `answer` VARCHAR(8000) NULL,
  PRIMARY KEY (`answerID`),
  CONSTRAINT `fk_quizAnswers_quizQuestions1`
    FOREIGN KEY (`questionID`)
    REFERENCES `quizSite`.`quizQuestions` (`questionID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizAnswers_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `quizSite`.`quizzes` (`quizID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_quizAnswers_quizQuestions1_idx` ON `quizSite`.`quizAnswers` (`questionID` ASC);

CREATE INDEX `fk_quizAnswers_quizzes1_idx` ON `quizSite`.`quizAnswers` (`quizID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`scores`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`scores` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`scores` (
  `scoreID` INT NOT NULL,
  `userID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `score` INT NULL,
  PRIMARY KEY (`scoreID`),
  CONSTRAINT `fk_scores_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_scores_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `quizSite`.`quizzes` (`quizID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_scores_users1_idx` ON `quizSite`.`scores` (`userID` ASC);

CREATE INDEX `fk_scores_quizzes1_idx` ON `quizSite`.`scores` (`quizID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`friendships`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`friendships` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`friendships` (
  `friendshipID` INT NOT NULL,
  `userID` INT NOT NULL COMMENT 'When inserting into this table, both friends should be included in the userID column (i.e. one friendship between user 1 and user 5 will create two rows: \"1, 5\" and \"5, 1\"',
  `frienduserID` INT NOT NULL,
  PRIMARY KEY (`friendshipID`),
  CONSTRAINT `fk_friendships_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_friendships_users2`
    FOREIGN KEY (`frienduserID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_friendships_users1_idx` ON `quizSite`.`friendships` (`userID` ASC);

CREATE INDEX `fk_friendships_users2_idx` ON `quizSite`.`friendships` (`frienduserID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`categories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`categories` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`categories` (
  `categoryID` INT NOT NULL,
  `categoryName` VARCHAR(255) NULL,
  PRIMARY KEY (`categoryID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizSite`.`quizCategories`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`quizCategories` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`quizCategories` (
  `quizCategoryID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `categoryID` INT NOT NULL,
  PRIMARY KEY (`quizCategoryID`),
  CONSTRAINT `fk_quizCategories_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `quizSite`.`quizzes` (`quizID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizCategories_categories1`
    FOREIGN KEY (`categoryID`)
    REFERENCES `quizSite`.`categories` (`categoryID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_quizCategories_quizzes1_idx` ON `quizSite`.`quizCategories` (`quizID` ASC);

CREATE INDEX `fk_quizCategories_categories1_idx` ON `quizSite`.`quizCategories` (`categoryID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`tags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`tags` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`tags` (
  `tagID` INT NOT NULL,
  `tagName` VARCHAR(255) NULL,
  PRIMARY KEY (`tagID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizSite`.`quizTags`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`quizTags` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`quizTags` (
  `quizTagID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `tagID` INT NOT NULL,
  PRIMARY KEY (`quizTagID`),
  CONSTRAINT `fk_quizTags_tags1`
    FOREIGN KEY (`tagID`)
    REFERENCES `quizSite`.`tags` (`tagID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizTags_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `quizSite`.`quizzes` (`quizID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_quizTags_tags1_idx` ON `quizSite`.`quizTags` (`tagID` ASC);

CREATE INDEX `fk_quizTags_quizzes1_idx` ON `quizSite`.`quizTags` (`quizID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`quizHistory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`quizHistory` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`quizHistory` (
  `quizHistoryID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `userID` INT NOT NULL,
  `dateTaken` DATETIME NULL,
  `score` INT NULL,
  `completed?` TINYINT(1) NULL,
  `timeStamp` DATETIME NULL,
  PRIMARY KEY (`quizHistoryID`),
  CONSTRAINT `fk_quizHistory_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `quizSite`.`quizzes` (`quizID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizHistory_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_quizHistory_quizzes1_idx` ON `quizSite`.`quizHistory` (`quizID` ASC);

CREATE INDEX `fk_quizHistory_users1_idx` ON `quizSite`.`quizHistory` (`userID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`message`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`message` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`message` (
  `messageID` INT NOT NULL,
  `messageType` TINYINT NULL,
  `toUserID` INT NOT NULL,
  `fromUserID` INT NULL,
  `subject` VARCHAR(255) NULL,
  `content` VARCHAR(8000) NULL,
  `date` DATETIME NULL,
  `messageRead` TINYINT(1) NULL,
  PRIMARY KEY (`messageID`),
  CONSTRAINT `fk_mailMessage_users`
    FOREIGN KEY (`toUserID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_mailMessage_users1`
    FOREIGN KEY (`fromUserID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_mailMessage_users_idx` ON `quizSite`.`message` (`toUserID` ASC);

CREATE INDEX `fk_mailMessage_users1_idx` ON `quizSite`.`message` (`fromUserID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`achievements`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`achievements` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`achievements` (
  `achievementID` INT NOT NULL,
  `achievementName` VARCHAR(255) NULL,
  `achievementDescription` VARCHAR(8000) NULL,
  PRIMARY KEY (`achievementID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizSite`.`userAchievements`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`userAchievements` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`userAchievements` (
  `userAchievementID` INT NOT NULL,
  `userID` INT NOT NULL,
  `achievementID` INT NOT NULL,
  `dateAchieved` DATETIME NULL,
  PRIMARY KEY (`userAchievementID`),
  CONSTRAINT `fk_userAchievements_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userAchievements_achievements1`
    FOREIGN KEY (`achievementID`)
    REFERENCES `quizSite`.`achievements` (`achievementID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_userAchievements_users1_idx` ON `quizSite`.`userAchievements` (`userID` ASC);

CREATE INDEX `fk_userAchievements_achievements1_idx` ON `quizSite`.`userAchievements` (`achievementID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`userQuizRatings`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`userQuizRatings` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`userQuizRatings` (
  `ratingID` INT NOT NULL,
  `userID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `ratingValue` TINYINT NULL,
  `ratingReview` VARCHAR(255) NULL,
  PRIMARY KEY (`ratingID`),
  CONSTRAINT `fk_userQuizRatings_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userQuizRatings_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `quizSite`.`quizzes` (`quizID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_userQuizRatings_users1_idx` ON `quizSite`.`userQuizRatings` (`userID` ASC);

CREATE INDEX `fk_userQuizRatings_quizzes1_idx` ON `quizSite`.`userQuizRatings` (`quizID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`quizQuestionHistory`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`quizQuestionHistory` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`quizQuestionHistory` (
  `quizQuestionHistoryID` VARCHAR(45) NOT NULL,
  `userID` INT NOT NULL,
  `quizID` INT NOT NULL,
  `questionID` INT NOT NULL,
  `score` INT NULL,
  `timeTaken` DATETIME NULL,
  `timeStamp` DATETIME NULL,
  PRIMARY KEY (`quizQuestionHistoryID`),
  CONSTRAINT `fk_quizQuestionHistory_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizQuestionHistory_quizzes1`
    FOREIGN KEY (`quizID`)
    REFERENCES `quizSite`.`quizzes` (`quizID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_quizQuestionHistory_quizQuestions1`
    FOREIGN KEY (`questionID`)
    REFERENCES `quizSite`.`quizQuestions` (`questionID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_quizQuestionHistory_users1_idx` ON `quizSite`.`quizQuestionHistory` (`userID` ASC);

CREATE INDEX `fk_quizQuestionHistory_quizzes1_idx` ON `quizSite`.`quizQuestionHistory` (`quizID` ASC);

CREATE INDEX `fk_quizQuestionHistory_quizQuestions1_idx` ON `quizSite`.`quizQuestionHistory` (`questionID` ASC);


-- -----------------------------------------------------
-- Table `quizSite`.`types`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`types` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`types` (
  `typeID` INT NOT NULL,
  `typeName` VARCHAR(10) NULL,
  `typeDescription` VARCHAR(255) NULL,
  PRIMARY KEY (`typeID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizSite`.`userTypes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `quizSite`.`userTypes` ;

CREATE TABLE IF NOT EXISTS `quizSite`.`userTypes` (
  `userTypeID` INT NOT NULL,
  `userID` INT NOT NULL,
  `typeID` INT NOT NULL,
  PRIMARY KEY (`userTypeID`),
  CONSTRAINT `fk_userTypes_users1`
    FOREIGN KEY (`userID`)
    REFERENCES `quizSite`.`users` (`userID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_userTypes_types1`
    FOREIGN KEY (`typeID`)
    REFERENCES `quizSite`.`types` (`typeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_userTypes_users1_idx` ON `quizSite`.`userTypes` (`userID` ASC);

CREATE INDEX `fk_userTypes_types1_idx` ON `quizSite`.`userTypes` (`typeID` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
