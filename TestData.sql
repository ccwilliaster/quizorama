
-- Create the dimension tables
INSERT INTO types VALUES
(1, "Admin", "An administrator with elevated priviliges. Can remove quizzes."),
(2, "Standard", "A standard user with standard user priviliges. Nothing special.");

INSERT INTO achievements VALUES
(1, "Amateur Author", "You have created a quiz!"),
(2, "Prolific Author", "You have created 5 quizzes!"),
(3, "Prodigious Author", "You have created 10 quizzes!"),
(4, "Quiz Machine", "You have created 20 quizzes!"),
(5, "I am the Greatest", "You achieved the highest score on a quiz!"),
(6, "Practice Makes Perfect", "You used practice mode on a quiz!");

INSERT INTO tags VALUES
(1, "History"),
(2, "Geography"),
(3, "Science"),
(4, "Politics"),
(5, "Question / Answer"),
(6, "Multiple Choice"),
(7, "Image / Answer"),
(8, "Fill in the Blank");

INSERT INTO categories VALUES
(1, "Question / Answer"),
(2, "Multiple Choice"),
(3, "Image / Answer"),
(4, "Fill in the Blank");

INSERT INTO questionTypes VALUES
(1, "Question / Answer", "Single question, single answer. Easy."),
(2, "Fill-In-The-Blank", "Get a line with a blank in it, pick the correct thing to write there."),
(3, "Multiple Choice", "Single question, choose one answer from many."),
(4, "Picture / Response", "Get an image as a question. Respond with the answer."),

INSERT INTO users VALUES 
(1,"testUser",NULL,"",NULL),
(2,"secondUser",NULL,"",NULL),
(3,"chris",NULL,"1000:07abcdc3858e55f2883986ca859723479678362437167ad8295344cce28ee139:4047acd3a1d782da7cc2b742c48f7077a904b7b7c9a52ce5bb4565d8ac2b96586041ea24a0e0fb74b06de4fe649bf1565d3f60f0cd3706c9e784c6ef9ca486ce", NULL);

INSERT INTO userAchievements (userID, achievementID) VALUES
(3, 4),
(3, 5),
(1, 1),
(2, 6);

INSERT INTO userTypes VALUES
(1, 3, 1);

INSERT INTO messages VALUES
(1, 0, 3, 2, "Test Message", "This is a test message. Enjoy!", "2014-02-20 23:59:59", 0, 0, 0),
(2, 0, 2, 3, "Re: Test Message", "Great message", "2014-02-20 23:59:59", 0, 0, 0),
(3, 0, 3, 1, "Reminder", "Message to myself.", "2014-02-20 23:59:59", 0, 0, 0),
(4, 0, 3, 2, "Quiz Me!", "Let's play a quiz together?", "2014-02-20 23:59:59", 0, 0, 0);

INSERT INTO quizzes (quizName, quizCreatorUserID, `singlePage?`, `randomOrder?`, `immediateCorrection?`, `practiceMode?`) 
VALUES 
("Quiz1", 2, 0, 1, 0, 0),
("Quiz2", 1, 1, 1, 0, 0),
("Quiz3", 2, 0, 1, 1, 0),
("Quiz4", 2, 1, 1, 1, 0),
("Quiz5", 1, 0, 1, 0, 1),
("Quiz6", 1, 1, 1, 0, 1),
("Quiz6", 1, 1, 1, 1, 1);

insert into quizzes values (1, "Normal Test", CURRENT_TIMESTAMP, 3, 0, 0, 0, 0);
insert into quizzes values (2, "Random Order Test", CURRENT_TIMESTAMP, 3, 0, 0, 0, 0);
insert into quizzes values (3, "Single Page Test", CURRENT_TIMESTAMP, 3, 1, 0, 0, 0);
insert into quizzes values (4, "Immediate Correction Test", CURRENT_TIMESTAMP, 3, 0, 0, 1, 0);

insert into quizQuestions values (1, 1, 1, "What is 1+1?", 1);
insert into quizQuestions values (2, 1, 4, "http://upload.wikimedia.org/wikipedia/commons/8/86/Africa_%28orthographic_projection%29.svg", 2);
insert into quizQuestions values (3, 2, 1, "What is the name of the First Lady of the United States?", 1);
insert into quizQuestions values (4, 2, 3, "How many continents are there?", 2);
insert into quizQuestions values (5, 3, 1, "What is the name of a group of geese?", 1);
insert into quizQuestions values (6, 3, 1, "What is the capital of the United States?", 2);
insert into quizQuestions values (7, 4, 2, "One of President Lincoln's most famous speeches was the ___________ address", 1);
insert into quizQuestions values (8, 4, 3, "What is the capital of California?", 2);

insert into quizAnswers values (1, 1, 1, "2");
insert into quizAnswers values (2, 2, 1, "Africa");
insert into quizAnswers values (3, 3, 2, "Michelle");
insert into quizAnswers values (4, 3, 2, "Michelle Obama");
insert into quizAnswers values (5, 4, 2, "!4");
insert into quizAnswers values (6, 4, 2, "!6");
insert into quizAnswers values (7, 4, 2, "7");
insert into quizAnswers values (8, 4, 2, "!9");
insert into quizAnswers values (9, 5, 3, "gaggle");
insert into quizAnswers values (10, 6, 3, "DC");
insert into quizAnswers values (11, 6, 3, "D.C.");
insert into quizAnswers values (12, 6, 3, "Washington, DC");
insert into quizAnswers values (13, 6, 3, "Washington, D.C.");
insert into quizAnswers values (14, 6, 3, "District of Columbia");
insert into quizAnswers values (15, 6, 3, "Washington DC");
insert into quizAnswers values (16, 6, 3, "Washington D.C.");
insert into quizAnswers values (17, 6, 3, "Washington");
insert into quizAnswers values (18, 7, 4, "Gettysburg");
insert into quizAnswers values (19, 8, 4, "!San Francisco");
insert into quizAnswers values (20, 8, 4, "!San Diego");
insert into quizAnswers values (21, 8, 4, "Sacramento");

INSERT INTO quizHistory (quizID, userID, score) VALUES
(1, 3, 5),
(1, 3, 10),
(1, 1, 6),
(2, 3, 4),
(2, 1, 9),
(1, 2, 3),
(3, 3, 2);
