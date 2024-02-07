package view.swing;

import java.awt.*;

public class ViewConstants {
    public static final String APP_NAME = "ITCandidateEvaluator";
    public static final int WINDOW_WIDTH_PIXELS = 1280;
    public static final int WINDOW_HEIGHT_PIXELS = 720;
    public static final Font FONT_LARGE = new Font(Font.DIALOG, Font.PLAIN, 26);
    public static final Font FONT_STAGE_INFO = new Font(Font.DIALOG, Font.PLAIN, 16);
    public static final String[] SLIDER_VALUE_DESCRIPTION = {"Unacceptable", "Poor", "Not too good", "Average", "Quite good", "Impressive", "Amazing!"};
    public static final String PRESETS_HOWTO = "Adjust sliders accordingly to their importance.\n" +
            "Higher slider value represents higher multiplier for its item score.\n" +
            "If a slider is set to '0' - its item will not occur during the process.\n" +
            "You can choose one of default presets or create your own - adjust sliders and press 'Save Presets' button.\n" +
            "Press 'Start' button when you're ready, set the name for the process and start the evaluation.\n";

    public static final String RESUME_STAGE_INFO =
            "<html><b>This stage's purpose is to evaluate candidate's background - whether it's beneficial for the applied position.</b><br>" +
            "( It should be performed before the actual interview. )<br>" +
            "<br>" +
            "At first go through the candidate's resume (and application letter if attached). Consider the following:<br>" +
            "- does it look neat and well prepared?<br>" +
            "- are there spelling or grammar errors?<br>" +
            "- does it contain all the necessary information?<br>" +
            "- is it too long or too short?<br>" +
            "- is it a default template or is it customized for the offer/company?<br>" +
            "- does it look appealing for you as a recruiter?<br>" +
            "<br>" +
            "<br>" +
            "Then you can check candidate's social media and attached links for website profiles or blogs.<br>" +
            "- does the candidate's profile look interesting? Are you excited for the interview or the exact opposite?<br>" +
            "- did you find any disturbing information about the candidate?<br>" +
            "- do you think this candidate would fit well to the team?<br>" +
            "<br></html>";
    public static final String LANGUAGE_STAGE_INFO =
            "<html><b>This stage's purpose is to evaluate candidate's English language proficiency - speaking, vocabulary and grammar.</b><br>" +
            "<br>" +
            "Not everyone is a foreign language teacher, so it might be difficult for the recruiter to asses language skills correctly.<br>" +
            "The interviewers will have to evaluate this skill according to theirs by comparison with the candidate's.<br>" +
            "<br>" +
            "To make it easier let it take a form of a small talk starting with topics like:<br>" +
            "<i>- How's the candidate's day? What have they been up to?</i><br>" +
            "<i>- How's the search for job going so far?</i><br>" +
            "<i>- What are they looking for in their next position?</i><br>" +
            "<br>" +
            "It would be best if the interviewer took some points from the candidate's resume to talk about:<br>" +
            "<i>- I found in resume that you were ... Can you tell me more about this?</i><br>" +
            "<i>- You said you are stress resilient - what do you mean by that?</i><br>" +
            "<i>- You mentioned you love travelling - what's your most memorable trip?</i><br>" +
            "<br>" +
            "You may as well talk about previous work experiences - the topic for the next stage so you can save some time doing that step.<br>" +
            "<br>" +
            "At the end think about the most important: <b>Was the communication in foreign language successful?</b><br>" +
            "Did you hear any mistakes? Is the candidate's proficiency better than yours?<br></html>";

    public static final String EXPERIENCE_STAGE_INFO =
            "<html><b>This stage's purpose is to evaluate candidate's former work experience - will it help in the new position?</b><br>" +
            "<br>" +
            "There are different criteria for this item, depending on the candidate's seniority level:<br>" +
            "- If they apply for the regular or senior position, only IT-related experience should be relevant.<br>" +
            "- If the candidate is fresh after college and hasn't worked yet - consider their education.<br>" +
            "- If the candidate is a career-shifter, think if their former job would benefit the team.<br>" +
            "<br>" +
            "<b>For an instance:</b><br>" +
            "The candidate is a college graduate majored at Software Development - set the experience above average. <br>" +
            "The candidate is a college graduate majored at Philosophy - set the experience to average. <br>" +
            "<br>" +
            "The candidate is a senior with 10 years of work in IT - set the experience to very high. <br>" +
            "The candidate is a senior with 7 years of work in IT - set the experience to high. <br>" +
            "<br>" +
            "The candidate has abandoned their career as a banker - set the experience to average. <br>" +
            "The candidate has abandoned their career as a banker - set the experience to high if your company is a fintech. <br>" +
            "<br>" +
            "The candidate has been an illegal cock fights manager - set the experience to low (but not the lowest - it's still management! ;) <br>" +
            "<br>" +
            "<b>Talk with the candidate, discuss details of former employment, ask about quitting and future plans.</b><br></html>";
    public static final String PROJECTS_STAGE_INFO =
            "<html><b>This stage's purpose is to evaluate candidate's passion, knowledge and engagement in the field.</b><br>" +
            "<br>" +
            "Not every senior is the same, not every junior has similar skills. Some people keep working on one and the same project<br>" +
            "for years. This makes them razor sharp... only in this very field. Others have vast experience in all possible segments, without<br>" +
            " mastering any. It's up for the recruiter to choose which of them the company needs at the moment.<br>" +
            "<br>" +
            "This step is to talk about the interesting projects candidates have participated in. <b>You may also use it to discuss<br>" +
            "the 'homework' - task given for the candidates, completing which brought them to this very interview.</b><br>" +
            "<br>" +
            "Speaking about junior position - you've got to go through the candidate's GitHub/GitLab repositories checking their work:<br>" +
            "- are they active programmers?<br>" +
            "- do they have many or few different repositories?<br>" +
            "- did they take on any particularly interesting projects?<br>" +
            "- do they have any experience in team programming?<br>" +
            "- what's their code quality?<br>" +
            "- how many technologies have they used?<br>" +
            "- do they use unit/integration tests?<br>" +
            "<br>" +
            "On the other hand, senior's best work might be protected with various NDA-s - but you can still talk about<br>" +
            "the most inspiring projects they have been involved with, without mentioning details.<br>" +
            "<br>" +
            "Prepare a list of technologies important for your company and discuss them with the candidate.<br></html>";
    public static final String LIVE_CODING_STAGE_INFO =
            "<html><b>This stage's purpose is to evaluate candidate's coding style under the pressure of time.</b><br>" +
            "<br>" +
            "Very few people enjoy live coding stage so you need to be thoughtful (unless stress simulation is what you want).<br>" +
            "Writing code in a notepad without using IDE, search engines or AI can be painful even for an experienced senior engineer.<br>" +
            "That's why I'd advise to reduce this stage to the minimum - instead of wasting time looking how the candidate is building<br>" +
            "their code, just ask them how would they do this particular task. Step by step.<br>" +
            "<br>" +
            "If you do want to perform a typical live coding phase anyways - use the menu below to look for some interesting tasks.<br>" +
            "These example tasks are stored in the 'livecodingtasks.txt' file - you may update it if you like.<br>" +
            "<br>" +
            "<b>During this step evaluate candidate's way of thinking rather than their ability to memorize stuff.</b><br></html>";
    public static final String QUESTIONS_STAGE_INFO =
            "<html><b>This stage's purpose is to evaluate candidate's knowledge in the field.</b><br>" +
            "<br>" +
            "Unlike the other stages - this one can have multiple questions scored individually. If you want to ask another question<br>" +
            "without moving to the next stage - press the 'Next question' button instead 'Continue'. You can do it as many times as you want,<br>" +
            "the final score in this stage will be the average of all acquired points.<br>" +
            "<br>" +
            "You can use prepared questions - choose file with particular subject and then you'll see related questions list. Pick one and<br>" +
            "give it to the candidate. Txt files with these questions are stored in '/questions' directory. Add more or delete if you desire.<br>" +
            "<br>" +
            "If you decide to ask own question - type its name in the 'Question:' text field - it will be visible in the final feedback.<br>" +
            "<br>" +
            "Tip. Same questions can be used for different seniority levels - only expectations can vary.<br></html>";
    public static final String SALARY_STAGE_INFO =
            "<html><b>This stage's purpose is to evaluate candidate's financial expectations.</b><br>" +
            "<br>" +
            "At this moment you should know the candidate better - what's their experience, knowledge, strong and weak spots.<br>" +
            "It's a perfect time to talk about finances and other benefits from working at the applied position.<br>" +
            "<br>" +
            "The candidate can be very skilled but if salary expectations exceed the company limits, all this won't matter.<br>" +
            "Sometimes it might be more efficient to hire someone less experienced and use some of the budget to train them.<br>" +
            "<br>" +
            "Input the minimal and maximal salary you can offer for this position in the text fields below. Ask the candidate how much would they<br>" +
            "would like to earn and move the slider to match that amount.<br>" +
            "<br>" +
            "'Total score' and 'Total score including salary' will be two different results, presented separately. <br></html>";
    public static final String SOFT_SKILLS_STAGE_INFO =
            "<html><b>This stage's purpose is to evaluate candidate's personality - will they fit well in the team?</b><br>" +
            "( It may be performed after the interview. )<br>" +
            "<br>" +
            "Past all the steps you should know what kind of a person is the candidate you've just interviewed.<br>" +
            "Being a good worker doesn't only mean to do your job well, the following features are as much important (if not more):<br>" +
            "- COMMUNICATION SKILLS<br>" +
            "- RESPONSIBILITY<br>" +
            "- CREATIVITY<br>" +
            "- 'GOOD ENERGY'<br>" +
            "<br>" +
            "Remember: <u>you don't necessarily want to hire the best candidate.</u><br>" +
            "<b>You want to get someone who does their work done, doesn't make a mess and is a team player.</b><br>" +
            "<br>" +
            "If you have difficulty setting the slider, ask yourself the question:<br>" +
            "<i>'How much would I like if I had to sit desk by desk with this person for the next year?'</i><br>" +
            "<br>" +
            "The score from this stage is a multiplier which will affect the entire candidate's result.<br></html>";

}
