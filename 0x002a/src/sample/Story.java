package sample;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Story {


    private BinarySearchTree<Question> QuestionSearchTree = null;//For binary search tree
    private Question firstQuestion = null;//For flow of story
    private CStats GameChar = null;
    private Question currQuestion = null;

    public Story(String fileName) throws IOException, IDNotAllowed {
        QuestionSearchTree = new BinarySearchTree<>();
        initializeStory(fileName);
        currQuestion=firstQuestion;
    }

    public void initializeStory(String fileName) throws IOException, IDNotAllowed {

        FileReader readStory = new FileReader("./saved/"+fileName);
        String bufferString;
        boolean firstFlag = true;

        StringBuilder parsedString = new StringBuilder();

        BufferedReader readerStory = new BufferedReader(readStory);

        while((bufferString = readerStory.readLine()) != null){
            parsedString.append(bufferString);
            if(bufferString.contains("(")){
                bufferString = parsedString.toString();
                Question localCurrQuestion = new Question(bufferString.split("\\(")[0]);
                parsedString = new StringBuilder();
                if(!firstFlag)
                QuestionSearchTree.add(localCurrQuestion);
                else{
                    firstQuestion = localCurrQuestion;
                    firstFlag=false;
                }
            }
            else
                parsedString.append("\n");
        }
        connector(firstQuestion);



        return;
    }
    private void connector(Question connection) throws IDNotAllowed {
        if(connection == null) return;
        ArrayList<Answer> answers= connection.GetAnswers();
        int i = 0;
        if(answers.isEmpty())
            return;

            while(i<answers.size()){
            Integer  a = connection.GetAnswers().get(i).nextQuestionID;
            Question searchQuestion = new Question(a);
            answers.get(i).setNextQuestion(QuestionSearchTree.search(searchQuestion));
            connector(answers.get(i).GetNextQuestion());
            ++i;
        }
        connection.setAnswers(answers);
    }

    public void saveStory(){

    }

    public boolean isEnd(){
        return currQuestion.isEnd();
    }
    public void showQuestion(){

        currQuestion.showQuestion();

    }
    public void showQuestionAndAnswer(){
        currQuestion.showQuestionAndAnswer();
    }
    public boolean isAnswerLegal(String Answer){
        boolean flag=false;

        for(int i=0;i<currQuestion.GetAnswers().size();i++){
            if((i+1)==Integer.parseInt(Answer)){
                if(currQuestion.GetAnswers().get(i).hasNextQuestion())//Degisen yer burasi
                    currQuestion = currQuestion.GetAnswers().get(i).GetNextQuestion();
                flag = true;
            }
        }
        return flag;
    }


    // SOON
//    /**
//     * Oyun içerisinde soruların erişim için ön şartları olabiliyor. Bu ön şartların karşılanıp karşılanmadığını kontrol eden method
//     * @param currQuestion Test edilecek soru objesi
//     * @return  Erişilebiliyorsa True, erişilemiyorsa False
//     */
//    public boolean isQuestionReachable(Question currQuestion){
//       return  GameChar.canAccess(currQuestion.getPreRequisite());
//    }


    public void addQuestion(){

    }

    public Question getCurrQuestion() {
        return currQuestion;
    }

    public Question getFirstQuestion() {
        return firstQuestion;
    }

    public Question getByID(int ıd) throws IDNotAllowed {
        Question searchQuestion = new Question(ıd);
        return QuestionSearchTree.search(searchQuestion);
    }
}
