package com.example.quizapp.HomeAndDiscover.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.quizapp.Auth.models.User;
import com.example.quizapp.Auth.repositories.UserRepository;
import com.example.quizapp.HomeAndDiscover.models.Friend;
import com.example.quizapp.HomeAndDiscover.repositories.FriendRepository;
import com.example.quizapp.Quiz.models.Answer;
import com.example.quizapp.Quiz.models.Question;
import com.example.quizapp.Quiz.models.Quiz;
import com.example.quizapp.Quiz.models.Result;
import com.example.quizapp.Quiz.repositories.AnswerRepository;
import com.example.quizapp.Quiz.repositories.QuestionRepository;
import com.example.quizapp.Quiz.repositories.QuizRepository;
import com.example.quizapp.Quiz.repositories.ResultRepository;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeViewModel extends AndroidViewModel {
    public final UserRepository userRepository;
    public final QuizRepository quizRepository;
    public final QuestionRepository questionRepository;
    public final AnswerRepository answerRepository;
    public final ResultRepository resultRepository;
    public final FriendRepository friendRepository;
    private final MutableLiveData<Boolean> initDataDone = new MutableLiveData<>();
    private final MutableLiveData<List<Quiz>> quizListLiveData = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public HomeViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        quizRepository = new QuizRepository(application);
        questionRepository = new QuestionRepository(application);
        answerRepository = new AnswerRepository(application);
        resultRepository = new ResultRepository(application);
        friendRepository = new FriendRepository(application);
    }

    public LiveData<Boolean> isInitDataDone() {
        return initDataDone;
    }

    public LiveData<List<Quiz>> getQuizList() {
        return quizListLiveData;
    }

    public void initData() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Random random = new Random();
                List<User> users = userRepository.getUserList();
                for (int i = 0; i < 100; i++) {
                    int quizId = i + 1;
                    String quizName = "Quiz " + (i + 1);
                    int creatorId = random.nextInt(userRepository.getUserCount()) + 1;
                    String startTime = "2024-06-19 09:00";
                    String endTime = "2024-06-19 10:00";
                    String description = "Description for Quiz " + (i + 1);
                    int isPublic = 1;
                    int timeLimit = 60;
                    String iconImage = "ic_quiz1";
                    String quizCode = "QZ" + (1000 + i + 1);
                    Quiz quiz = new Quiz(quizId, quizName, creatorId, startTime, endTime, description, isPublic, timeLimit, iconImage, quizCode);
                    quizRepository.addQuiz(quiz);

                    for (int j = 0; j < 5; j++) {
                        int questionId = (i * 5) + j + 1;
                        String questionText = "Question " + (j + 1) + " for " + quizName;
                        String questionType = "Multiple Choice";
                        Question question = new Question(questionId, quizId, questionText, questionType);
                        questionRepository.addQuestion(question);

                        for (int k = 0; k < 4; k++) {
                            int answerId = (questionId * 4) + k + 1;
                            String answerText = "Answer " + (k + 1) + " for " + questionText;
                            int isCorrect = (k == 0) ? 1 : 0;
                            Answer answer = new Answer(answerId, questionId, answerText, isCorrect);
                            answerRepository.addAnswer(answer);
                        }
                    }

                    for (User user : users) {
                        int resultId = (i * userRepository.getUserCount()) + user.getUserId() + 1;
                        int userId = random.nextInt(userRepository.getUserCount()) + 1;
                        int score = random.nextInt(101);
                        String completionDate = "2024-06-19";
                        int correctAnswers = random.nextInt(20);
                        int incorrectAnswers = 20 - correctAnswers;
                        Result result = new Result(resultId, userId, quizId, score, completionDate, correctAnswers, incorrectAnswers);
                        resultRepository.addResult(result);
                    }
                }

                for (int i = 0; i < 10; i++) {
                    int friendId = i + 1;
                    int userId = random.nextInt(userRepository.getUserCount()) + 1;
                    int friendUserId = random.nextInt(userRepository.getUserCount()) + 1;
                    Friend friend = new Friend(friendId, userId, friendUserId);
                    friendRepository.addFriend(friend);
                }

                initDataDone.postValue(true);
            }
        });
    }

    public void loadQuizzesForUser(int userId) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                quizRepository.filterQuizzesByUserId(userId);
                List<Quiz> quizList = quizRepository.getQuizList();
                quizListLiveData.postValue(quizList);
            }
        });
    }
}
