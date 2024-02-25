<h1>ITCandidateEvaluator</h1>

<h2>The problem</h2>

Years 2023 and 2024 brought a massive popularity for IT-related jobs. Suddenly everyone wanted to become a programmer.
At the same time covid-related bubble has popped which resulted in cutting employment among many companies, especially these working in IT branches.
The outcome was predictable - every new job offer is getting hundreds or thousands resumes.
Even using automatic filters such as various ATS-es, HR recruiters are left with dozens of valid candidates, where each seems to be perfect for the position.
To find the best amongst them hours and hours have to be spent on interviews, mostly with the assistance of a qualified technical expert.
(Whose hourly rate is high, and they'd rather be doing something more productive that time)
<b>The evaluation process of candidates should be more automatic and standardized to be quick, effective and fair.</b>

<h2>The solution</h2>

Having the last sentence in mind I have developed ITCandidateEvaluator - a tool which can be used before, during and after the interviews.
This app will help you organize all your recruitment processes and their applicants. It will also guide you through the interview process, so it's the same for each candidate.
You can modify the steps of the recruitment as you wish to adjust them to your needs.

<h2>Features</h2>

- the app is using two types of database (chosen at launch): local files and MySQL Database
- user can start a new recruitment process and pick steps of evaluation which will occur during the interview
- these steps are: Resume & Social Media, English Language, Work Experience, Own Projects, Live Coding, Technical Questions, Salary Expectations, Soft Skills
- user can set the importance for each of these steps - high importance equals higher score multiplier
- presets with default importance for different types of candidates are prepared and stored as JSON files, but user can quickly make more
- once a recruitment process is created, user can add candidates - either manually or automatically by parsing their resume files' names
- evaluating a candidate is simple: go through the previously selected stages, ask questions and set a score slider accordingly to the candidate's responses
- live coding tasks and technical questions are stored in local files and can be easily edited to match the type of the recruitment (e.g. programming language)
- salary score is a global multiplier which will affect only cost/value ratio of the candidate
- soft skills score is a global modifier which can give up to bonus 50% (positive or negative) points to the candidate's score
- candidates can be sorted by their name, date of joining the recruitment, total score and cost/value ratio
- once a candidate gets evaluated, the interviewer can generate a quick feedback with the results to email this back to the candidate. Feedback may look like below:

<pre>
Thank you for participation in our recruitment process!
Here are your results:
Resume and social media evaluation stage: 94%
English language assessment stage: 62%
Previous work experience: 77%
Own projects: 54%
Live coding stage: 19%
Technical questions stage: 58%
You have been asked the following questions:
-Why is multiple inheritance isn’t allowed... : 37%.
-What are method signatures? : 50%.
-Can you override private or static... : 92%.
-What is a default method in... : 100%.
-Each try block must be followed... : 12%.

Your final score is: 58%
You ranked: 1 among 1 candidates interviewed.

FOR RECRUITER ONLY!
The candidate joined the process on 2024-02-24
The candidate got evaluated on 2024-02-24
Evaluation time: 2 minutes 37 seconds
Salary expected: 6008
Salary expectations: 8% less than average.
Value/cost ratio: 63%
Soft skills: 83%
Additional notes: former restaurant manager
</pre>


<h2>Development</h2>
The app consists of 38 classes (including interfaces and enums) grouped in MVC scheme. Each class has a basic yet descriptive JavaDoc documentation.
I used old but good Swing libraries for GUI - wanted to make something more than just an API, to create a fully functional solution. But I am not a frontend guy (yet?).
All important actions are being logged with log4j - to the console and app.log file.
All public methods have unit tests (JUnit and Mockito).
You can follow the development process on Trello: https://trello.com/b/qXfFUV59/itcandidateevaluator
Each steps of development process: idea, frontend, backend, database and testing are briefly described on my blog (Polish language):
- https://wwsj.xyu.pl/wwsj/uncategorized/itcandidateevaluator-idea/
- https://wwsj.xyu.pl/wwsj/uncategorized/itcandidateevaluator-frontend/
- https://wwsj.xyu.pl/wwsj/uncategorized/itcandidateevaluator-backend/


