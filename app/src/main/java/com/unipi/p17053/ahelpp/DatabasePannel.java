 package com.unipi.p17053.ahelpp;

import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.unipi.p17053.ahelpp.Models.ChallengesModel;
import com.unipi.p17053.ahelpp.Models.IndexElementModel;
import com.unipi.p17053.ahelpp.Models.LessonModel;
import com.unipi.p17053.ahelpp.Models.LeaderboardModel;
import com.unipi.p17053.ahelpp.Models.ProfileModel;
import com.unipi.p17053.ahelpp.Models.QuestionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

 @RequiresApi(api = Build.VERSION_CODES.KITKAT)
 public class DatabasePannel {

    public static FirebaseFirestore globalFirestore;
    public static String clickedFrom = "";
     public static String clickedFromElement = "";
    public static int selected_less_index = 0;
    public static int selected_test_index = 0;
    public static int selected_element_index = 0;
    public static final int notVisited=0;
    public static final int notAnswered=1;
    public static final int answered=2;
    static int tmp;
    static int testAvSum;
    static int testsAttempted;
    public static int currentAv = 0;
    public static int global_users_count=0;
    public static boolean imOnBoard = false;
    public static List<LessonModel> globalLessonsList= new ArrayList<>();
    public static List<IndexElementModel> globalIndexElementsList= new ArrayList<>();
    public static List<ChallengesModel> globalTestList = new ArrayList<>();
     public static List<ChallengesModel> globalAllTestsList = new ArrayList<>();
     public static List<ChallengesModel> globalDoBetterTestList = new ArrayList<>();
    public static List<QuestionModel> globalQuestionList = new ArrayList<>();
    public static List<LeaderboardModel> globalRankUsersList = new ArrayList<>();
    public static List<String> globalBookmrkIDList = new ArrayList<>();
    public static Map<String, Integer> globalLessonTestAverageList = new ArrayMap<>();
    public static List<QuestionModel> globalBookmarksList = new ArrayList<>();
    public static List<String> globalWrongQIDList = new ArrayList<>();
    public static List<String> globalStudiedList = new ArrayList<>();
    public static List<String> globalChallengedList = new ArrayList<>();
    public static ProfileModel profile = new ProfileModel("name",null,0,0);
    public static LeaderboardModel performance = new LeaderboardModel("NULL",0,0);

    public static void createUserData(String email, String name, final MyCompleteListener completeListener) {
        Map<String, Object> userData = new ArrayMap<>();
        userData.put("EMAI_ID", email);
        userData.put("NAME", name);
        userData.put("TOTAL_SCORE", 0);
        userData.put("BOOKMARKS", 0);
        userData.put("WRONGS", 0);

        DocumentReference userDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = globalFirestore.batch();
        batch.set(userDoc, userData);

        DocumentReference userCountdoc = globalFirestore.collection("USERS").document("TOTAL_USERS");
        batch.update(userCountdoc,"COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();

                    }
                });

    }

    public static void getUserData(final MyCompleteListener completeListener)
    {
        globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        profile.setName(documentSnapshot.getString("NAME"));
                        profile.setEmail(documentSnapshot.getString("EMAIL"));
                        performance.setRank(0);
                        performance.setName(documentSnapshot.getString("NAME"));
                        performance.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());

                        if(documentSnapshot.get("BOOKMARKS")!=null)
                        {
                            profile.setBookmarksCount(documentSnapshot.getLong("BOOKMARKS").intValue());
                        }
                        if(documentSnapshot.get("WRONGS")!=null)
                        {
                            profile.setWrongsCount(documentSnapshot.getLong("WRONGS").intValue());
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        completeListener.onFailure();
                    }
                });
    }



    public static void loadMyScores(MyCompleteListener completeListener)
    {
        globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_TOP_SCORES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        for(int i=0;i<globalTestList.size();i++)
                        {
                            int top=0;
                            if(documentSnapshot.get(globalTestList.get(i).getTestId()) != null)
                            {

                                top = documentSnapshot.getLong(globalTestList.get(i).getTestId()).intValue();

                            }


                            globalTestList.get(i).setTopScore(top);
                        }

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });
    }

     public static void loadDoBetterTests(MyCompleteListener completeListener)
     {
         globalDoBetterTestList.clear();
         globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                 .collection("USER_DATA").document("MY_TOP_SCORES")
                 .get()
                 .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {

                         for(int i=0;i<globalAllTestsList.size();i++)
                         {
                             int top=0;
                             if(documentSnapshot.get(globalAllTestsList.get(i).getTestId()) != null)
                             {

                                 top = documentSnapshot.getLong(globalAllTestsList.get(i).getTestId()).intValue();
                                 globalDoBetterTestList.add(new ChallengesModel(
                                         globalAllTestsList.get(i).getTestId(),top,globalAllTestsList.get(i).getTime()
                                 ));


                             }


                         }

                         for (ChallengesModel s : globalDoBetterTestList) {
                             Log.d("FEEDBACK : Load Do Better List", s.getTestId());
                         }
                         completeListener.onSuccess();

                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {

                         completeListener.onFailure();

                     }
                 });
     }

    public static void loadLessons(final MyCompleteListener completeListener) {

        globalLessonsList.clear();

        globalFirestore.collection("LESSONS").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots )
                        {
                            docList.put(doc.getId(),doc);
                        }
                        QueryDocumentSnapshot lessListDoc = docList.get("aeppLessons");

                        long lessCount = lessListDoc.getLong("COUNT");
                        for(int i=1;i<=lessCount;i++)
                        {
                            String lessID = lessListDoc.getString("LESS"+ String.valueOf(i) + "_ID");
                            QueryDocumentSnapshot lessDoc = docList.get(lessID);
                            String lessName  = lessDoc.getString("NAME");
                            String url  = lessDoc.getString("url");
                            int numOfTests = lessDoc.getLong("NO_OF_TESTS").intValue();
                            globalLessonsList.add(new LessonModel(lessID,lessName,numOfTests,url));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

     public static void loadAllTestData(final MyCompleteListener completeListener) {

         globalAllTestsList.clear();
         final boolean[] success = {true};

         for(int i=0;i<globalLessonsList.size();i++)
         {
             int finalI = i;
             globalFirestore.collection("LESSONS").document(globalLessonsList.get(i).getDocID())
                     .collection("TESTS_LIST").document("TEST_INFO")
                     .get()
                     .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                         @Override
                         public void onSuccess(DocumentSnapshot documentSnapshot) {

                             int noOfTests= globalLessonsList.get(finalI).getNumOfTests();
                             Log.d("FEEDBACK noOfTests : ", String.valueOf(noOfTests) );

                             for(int j=1;j<=noOfTests;j++)
                             {

                                 globalAllTestsList.add(new ChallengesModel(
                                         documentSnapshot.getString("TEST"+ String.valueOf(j) + "_ID"),0,documentSnapshot.getLong("TEST"+ String.valueOf(j) + "_TIME").intValue()
                                 ));

                             }



                         }
                     })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             success[0] =false;
                             completeListener.onFailure();
                         }
                     });
         }

         if(success[0] ==true)
         {

             for (ChallengesModel s : globalAllTestsList) {
                 Log.d("FEEDBACK : Load All Tests", s.getTestId());
             }
             completeListener.onSuccess();
         }





     }


    public static void loadTestData(final MyCompleteListener completeListener) {

        globalTestList.clear();
        globalFirestore.collection("LESSONS").document(globalLessonsList.get(selected_less_index).getDocID())
                .collection("TESTS_LIST").document("TEST_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int noOfTests= globalLessonsList.get(selected_less_index).getNumOfTests();
                        Log.d("FEEDBACK noOfTests : ", String.valueOf(noOfTests) );

                        for(int i=1;i<=noOfTests;i++)
                        {

                            globalTestList.add(new ChallengesModel(
                                    documentSnapshot.getString("TEST"+ String.valueOf(i) + "_ID"),0,documentSnapshot.getLong("TEST"+ String.valueOf(i) + "_TIME").intValue()
                            ));

                        }



                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });


    }

     public static void loadElementTestData(final MyCompleteListener completeListener) {

         globalIndexElementsList.clear();

         globalFirestore.collection("INDEX_ELEMENTS").get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                         for(QueryDocumentSnapshot doc : queryDocumentSnapshots )
                         {
                             docList.put(doc.getId(),doc);
                         }
                         QueryDocumentSnapshot elListDoc = docList.get("allElements");

                         long elCount = elListDoc.getLong("COUNT");
                         for(int i=1;i<=elCount;i++)
                         {
                             String elID = elListDoc.getString("ELEMENT"+ String.valueOf(i) + "_ID");
                             QueryDocumentSnapshot elDoc = docList.get(elID);
                             String elRec  = elDoc.getString("recommendationDisc");
                             String elRedirect  = elDoc.getString("redirectTo");
                             globalIndexElementsList.add(new IndexElementModel(elID,elRec,elRedirect));
                         }

                         completeListener.onSuccess();
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         completeListener.onFailure();
                     }
                 });

     }

    public static void loadTestQuestions(final MyCompleteListener completeListener)
    {
        globalQuestionList.clear();
        globalFirestore.collection("TestQuestions")
                .whereEqualTo("LESSON",globalLessonsList.get(selected_less_index).getDocID())
                .whereEqualTo("TEST",globalTestList.get(selected_test_index).getTestId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot doc : queryDocumentSnapshots)
                        {

                            boolean isBookmarked = false;
                            if(globalBookmrkIDList.contains(doc.getId())) isBookmarked=true;

                             globalQuestionList.add(new QuestionModel(
                                     doc.getId(),
                                     doc.getString("QUESTION"),
                                     doc.getString("A"),
                                     doc.getString("B"),
                                     doc.getString("C"),
                                     doc.getLong("ANSWER").intValue(),
                                     -1,
                                     notVisited,
                                     isBookmarked

                             ));
                        }

                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }


     public static void loadBookmarkedTestQuestions(final MyCompleteListener completeListener)
     {
         globalQuestionList.clear();
         globalFirestore.collection("TestQuestions")
                 .get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                         for(DocumentSnapshot doc : queryDocumentSnapshots)
                         {

                             if(globalBookmrkIDList.contains(doc.getId()))
                             {
                                 globalQuestionList.add(new QuestionModel(
                                         doc.getId(),
                                         doc.getString("QUESTION"),
                                         doc.getString("A"),
                                         doc.getString("B"),
                                         doc.getString("C"),
                                         doc.getLong("ANSWER").intValue(),
                                         -1,
                                         notVisited,
                                         true

                                 ));
                             }

                         }

                         completeListener.onSuccess();
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         completeListener.onFailure();
                     }
                 });
     }

     public static void loadWrongTestQuestions(final MyCompleteListener completeListener)
     {
         globalQuestionList.clear();
         globalFirestore.collection("TestQuestions")
                 .get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                         for(DocumentSnapshot doc : queryDocumentSnapshots)
                         {

                             boolean isBookmarked = false;
                             if(globalBookmrkIDList.contains(doc.getId())) isBookmarked=true;

                             if(globalWrongQIDList.contains(doc.getId()))
                             {

                                 globalQuestionList.add(new QuestionModel(
                                         doc.getId(),
                                         doc.getString("QUESTION"),
                                         doc.getString("A"),
                                         doc.getString("B"),
                                         doc.getString("C"),
                                         doc.getLong("ANSWER").intValue(),
                                         -1,
                                         notVisited,
                                         isBookmarked

                                 ));
                             }

                         }

                         completeListener.onSuccess();
                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         completeListener.onFailure();
                     }
                 });
     }

     public static void saveElementResult(final MyCompleteListener completeListener)
     {
         WriteBatch batch = globalFirestore.batch();

         //Bookmarks Count update
         Map<String,Object> bmData = new ArrayMap<>();

         for(int i = 0; i< DatabasePannel.globalBookmrkIDList.size(); i++)
         {
             bmData.put("BM"+String.valueOf(i+1)+"_ID", DatabasePannel.globalBookmrkIDList.get(i));
         }
         DocumentReference bmDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                 .collection("USER_DATA").document("BOOKMARKS");
         batch.set(bmDoc,bmData);

         //Bookmarks question ids update
         DocumentReference userDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());

         Map<String,Object> userData = new ArrayMap<>();
         userData.put("BOOKMARKS", DatabasePannel.globalBookmrkIDList.size());

         batch.update(userDoc,userData);

         

         batch.commit()
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void unused) {


                         completeListener.onSuccess();

                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {

                         completeListener.onFailure();

                     }
                 });


     }

    public static void saveResult(final int score, final MyCompleteListener completeListener)
    {
        WriteBatch batch = globalFirestore.batch();

        //Bookmarks
        Map<String,Object> bmData = new ArrayMap<>();

        for(int i = 0; i< DatabasePannel.globalBookmrkIDList.size(); i++)
        {
            bmData.put("BM"+String.valueOf(i+1)+"_ID", DatabasePannel.globalBookmrkIDList.get(i));
        }

        DocumentReference bmDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("BOOKMARKS");
        batch.set(bmDoc,bmData);

        //Wrongs
        Map<String,Object> wrData = new ArrayMap<>();
        for(int i = 0; i< DatabasePannel.globalWrongQIDList.size(); i++)
        {
            wrData.put("WR"+String.valueOf(i+1)+"_ID", DatabasePannel.globalWrongQIDList.get(i));
        }
        DocumentReference wrDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("WRONGS");
        batch.set(wrDoc,wrData);



        if(score>globalTestList.get(selected_test_index).getTopScore())
        {
            //TOTAL_SCORE + BOOKMARKS field in firestore
            DocumentReference userDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
            int totalScoreAddition = score-globalTestList.get(selected_test_index).getTopScore();

            Map<String,Object> userData = new ArrayMap<>();
            userData.put("TOTAL_SCORE",FieldValue.increment(totalScoreAddition));
            userData.put("BOOKMARKS", DatabasePannel.globalBookmrkIDList.size());
            userData.put("WRONGS", DatabasePannel.globalWrongQIDList.size());

            batch.update(userDoc,userData);

            //USER_DATA >> MY_TOP_SCORES FIELD in firestore
            DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_TOP_SCORES");

            Map<String,Object> testData = new ArrayMap<>();
            testData.put(globalTestList.get(selected_test_index).getTestId(),score);

            batch.set(scoreDoc,testData, SetOptions.merge()); //SetOptions.merge() --> If the document is already created we will just update it
        }
        else
        {
            //TOTAL_SCORE + BOOKMARKS field in firestore
            DocumentReference userDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());

            Map<String,Object> userData = new ArrayMap<>();
            userData.put("BOOKMARKS", DatabasePannel.globalBookmrkIDList.size());
            userData.put("WRONGS", DatabasePannel.globalWrongQIDList.size());

            batch.update(userDoc,userData);
        }


        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(score>globalTestList.get(selected_test_index).getTopScore())
                        {
                            int newScore = performance.getScore()+score-globalTestList.get(selected_test_index).getTopScore();
                            performance.setScore(newScore);
                            globalTestList.get(selected_test_index).setTopScore(score);



                        }

                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });


    }
    public static void init_toSaveNewResult(final MyCompleteListener completeListener) {

        //Helpful path reference
        DocumentReference userDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
        CollectionReference userDataColl = userDoc.collection("USER_DATA");
        DocumentReference currentLessonTestDoc = userDataColl.document(globalTestList.get(selected_test_index).getTestId());

        WriteBatch batch = globalFirestore.batch();

        //Add SCORES_COUNT and AVERAGE fields if they doesn't exist

        userDataColl.document(globalTestList.get(selected_test_index).getTestId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists())
                        {
                            Log.d("FEEDBACK ", "Document exists!");
                            completeListener.onSuccess();
                        }
                        else
                        {
                            Log.d("FEEDBACK ", "Document does not exist!");
                            Map<String, Integer>  newTestScoreBasics = new ArrayMap<>();
                            newTestScoreBasics.put("SCORES_COUNT", 0);
                            newTestScoreBasics.put("AVERAGE", 0);

                            batch.set(currentLessonTestDoc,newTestScoreBasics);


                            batch.commit()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            completeListener.onSuccess();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            completeListener.onFailure();

                                        }
                                    });
                        }

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
        }

        public static void save_NewResult(final int score, final MyCompleteListener completeListener)
      {

        //Helpful path references
        DocumentReference userDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
        CollectionReference userDataColl = userDoc.collection("USER_DATA");

        WriteBatch batch = globalFirestore.batch();

        //Add the score of the new attempt with a recognizable field id of type currentLessonTest_ID and increment the SCORE_COUNT
        userDataColl.document(globalTestList.get(selected_test_index).getTestId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int lesson_scores_count = documentSnapshot.getLong("SCORES_COUNT").intValue();
                        DocumentReference newScoreDoc = userDoc.collection("USER_DATA").document(globalTestList.get(selected_test_index).getTestId());
                        Map<String,Object> newTestData = new ArrayMap<>();
                        int new_lesson_score_pos = lesson_scores_count +1;
                        String id = globalTestList.get(selected_test_index).getTestId()+"_"+new_lesson_score_pos;
                        newTestData.put(id,score);

                        batch.set(newScoreDoc,newTestData,SetOptions.merge());
                        batch.update(newScoreDoc,"SCORES_COUNT", FieldValue.increment(1));

                        batch.commit()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        completeListener.onSuccess();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        completeListener.onFailure();

                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });

      }

    public static void calculate_TestAverage(final MyCompleteListener completeListener)
    {

        //Helpful path references
        DocumentReference userDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
        CollectionReference userDataColl = userDoc.collection("USER_DATA");
        DocumentReference currentLessonTestDoc = userDataColl.document(globalTestList.get(selected_test_index).getTestId());

        WriteBatch batch = globalFirestore.batch();

        //Calculate the average score of the lesson and store it to AVERAGE field in firestore
        currentLessonTestDoc
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {


                        int lessScoresCount = documentSnapshot.getLong("SCORES_COUNT").intValue();
                        int scoresSum = 0;
                        for(int i=1;i<=lessScoresCount;i++)
                        {
                            scoresSum += documentSnapshot.getLong(globalTestList.get(selected_test_index).getTestId()+"_"+i).intValue();
                        }

                        int score_average = scoresSum/lessScoresCount;
                        batch.update(currentLessonTestDoc,"AVERAGE", score_average);
                        batch.commit()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                        completeListener.onSuccess();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        completeListener.onFailure();

                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        completeListener.onFailure();

                    }
                });


    }

     public static void loadAverages(MyCompleteListener completeListener)
     {
         globalLessonTestAverageList.clear();
         globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                 .collection("USER_DATA").get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();

                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots )
                        {
                            docList.put(doc.getId(),doc);
                        }


                        int lessCount = globalLessonsList.size();

                        for(int i=0;i<lessCount;i++)
                        {

                            int tests_count = globalLessonsList.get(i).getNumOfTests();
                            for(int j=1;j<=tests_count;j++)
                            {
                                String key = "lesson" + (i+1) + "test" + j;
                                QueryDocumentSnapshot lessListDoc = docList.get(key);
                                if(lessListDoc!=null)
                                {
                                    int average = lessListDoc.getLong("AVERAGE").intValue();
                                    globalLessonTestAverageList.put(key,average);
                                }

                            }
                            
                         
                        }

                        Log.d("AnyTagYouWant", globalLessonTestAverageList.toString());
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });

          
     }


     public static void mark_AsStudied(final MyCompleteListener completeListener) {

         //Helpful path reference
         DocumentReference userDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
         CollectionReference userDataColl = userDoc.collection("USER_DATA");
         DocumentReference studiedDoc = userDataColl.document("STUDIED");

         WriteBatch batch = globalFirestore.batch();


         userDataColl.document("STUDIED")
                 .get()
                 .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {

                             Map<String, String>  lessonStudied = new ArrayMap<>();
                             String field = "ST_"+globalLessonsList.get(selected_less_index).getLessonName().substring(0,1);
                             String value = globalLessonsList.get(selected_less_index).getLessonName();
                             lessonStudied.put(field,value);

                             batch.set(studiedDoc, lessonStudied,SetOptions.merge());

                         batch.commit()
                                     .addOnSuccessListener(new OnSuccessListener<Void>() {
                                         @Override
                                         public void onSuccess(Void unused) {

                                             completeListener.onSuccess();

                                         }
                                     })
                                     .addOnFailureListener(new OnFailureListener() {
                                         @Override
                                         public void onFailure(@NonNull Exception e) {

                                             completeListener.onFailure();

                                         }
                                     });
                         }

                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         completeListener.onFailure();
                     }
                 });
     }

     public static void loadLessonToStudy(MyCompleteListener completeListener)
     {

            globalStudiedList.clear();
            globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                 .collection("USER_DATA").document("STUDIED")
                 .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {


                            Map<String, Object> map = document.getData();
                            if (map != null) {
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    globalStudiedList.add(entry.getValue().toString());
                                }
                            }

                            //So what you need to do with your list
                            Collections.sort(globalStudiedList);
                            for (String s : globalStudiedList) {
                                Log.d("TAG", s);
                            }

                        }

                    }
                    completeListener.onSuccess();
                }
            });


     }

     public static void mark_AsChallenged(final MyCompleteListener completeListener) {

         //Helpful path reference
         DocumentReference userDoc = globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
         CollectionReference userDataColl = userDoc.collection("USER_DATA");
         DocumentReference studiedDoc = userDataColl.document("CHALLENGED");

         WriteBatch batch = globalFirestore.batch();


         userDataColl.document("CHALLENGED")
                 .get()
                 .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {

                         Map<String, String>  challengeTaken = new ArrayMap<>();
                         String field = "CH_"+globalTestList.get(selected_test_index).getTestId();
                         String value = globalTestList.get(selected_test_index).getTestId();
                         challengeTaken.put(field,value);

                         batch.set(studiedDoc, challengeTaken,SetOptions.merge());

                         batch.commit()
                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void unused) {

                                         completeListener.onSuccess();

                                     }
                                 })
                                 .addOnFailureListener(new OnFailureListener() {
                                     @Override
                                     public void onFailure(@NonNull Exception e) {

                                         completeListener.onFailure();

                                     }
                                 });
                     }

                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         completeListener.onFailure();
                     }
                 });
     }

     public static void loadChallengeToTake(MyCompleteListener completeListener)
     {

         globalChallengedList.clear();
         globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                 .collection("USER_DATA").document("CHALLENGED")
                 .get()
                 .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         if (task.isSuccessful()) {
                             DocumentSnapshot document = task.getResult();
                             if (document.exists()) {


                                 Map<String, Object> map = document.getData();
                                 if (map != null) {
                                     for (Map.Entry<String, Object> entry : map.entrySet()) {
                                         globalChallengedList.add(entry.getValue().toString());
                                     }
                                 }

                                 //So what you need to do with your list
                                 Collections.sort(globalChallengedList);
                                 for (String s : globalChallengedList) {
                                     Log.d("Global Challenged List", s);
                                 }

                             }

                         }
                         completeListener.onSuccess();
                     }
                 });


     }



    public static void loadBookmarkIDs(MyCompleteListener completeListener)
    {
        globalBookmrkIDList.clear();
        globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("BOOKMARKS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        int count = profile.getBookmarksCount();

                        for(int i =0;i<count;i++)
                        {
                            String bmId = documentSnapshot.getString("BM"+String.valueOf(i+1)+"_ID");
                            globalBookmrkIDList.add(bmId);
                        }
                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();

                    }
                });
    }

    public static void loadBookmarks(MyCompleteListener completeListener)
    {
        globalBookmarksList.clear();
        tmp=0;

        if(globalBookmrkIDList.size()==0) completeListener.onSuccess();

        for(int i =0;i<globalBookmrkIDList.size();i++)
        {
            String docID = globalBookmrkIDList.get(i);
            globalFirestore.collection("TestQuestions").document(docID)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists())
                            {
                                globalBookmarksList.add(new QuestionModel(
                                        documentSnapshot.getId(),
                                        documentSnapshot.getString("QUESTION"),
                                        documentSnapshot.getString("A"),
                                        documentSnapshot.getString("B"),
                                        documentSnapshot.getString("C"),
                                        documentSnapshot.getLong("ANSWER").intValue(),
                                        0,
                                        -1,
                                        false

                                ));

                            }
                            tmp++;
                            if(tmp==globalBookmrkIDList.size())
                            {
                                completeListener.onSuccess();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            completeListener.onFailure();
                        }
                    });


        }

    }

     public static void loadWrongQIDs(MyCompleteListener completeListener)
     {
         globalWrongQIDList.clear();
         globalFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                 .collection("USER_DATA").document("WRONGS")
                 .get()
                 .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                     @Override
                     public void onSuccess(DocumentSnapshot documentSnapshot) {

                         int count = profile.getWrongsCount();

                         for(int i =0;i<count;i++)
                         {
                             String wrongId = documentSnapshot.getString("WR"+String.valueOf(i+1)+"_ID");
                             globalWrongQIDList.add(wrongId);
                         }
                         completeListener.onSuccess();

                     }
                 })
                 .addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         completeListener.onFailure();

                     }
                 });
     }


    public static void getTopUsers(MyCompleteListener myCompleteListener)
    {
        globalRankUsersList.clear();
        String myUID = FirebaseAuth.getInstance().getUid();
        globalFirestore.collection("USERS")
                .whereGreaterThan("TOTAL_SCORE",0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        int rank = 1;
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots)
                        {
                            globalRankUsersList.add(new LeaderboardModel(
                                    doc.getString("NAME"),
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    rank
                            ));

                            if(myUID.compareTo(doc.getId())==0) //see if this is myUser which means  it is in the top 20
                            {
                                imOnBoard = true;
                                performance.setRank(rank);
                            }

                            rank++;
                        }

                        myCompleteListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myCompleteListener.onFailure();
                    }
                });
    }

    public static void getUsersCount(MyCompleteListener myCompleteListener)
    {
        globalFirestore.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        global_users_count = documentSnapshot.getLong("COUNT").intValue();
                        myCompleteListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        myCompleteListener.onFailure();

                    }
                });
    }




    public static void loadData(final MyCompleteListener completeListener)
    {
        loadLessons(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                getUserData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        getUsersCount(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {

                                loadAllTestData(new MyCompleteListener() {
                                    @Override
                                    public void onSuccess() {

                                        loadAverages(new MyCompleteListener() {
                                            @Override
                                            public void onSuccess() {
                                                loadBookmarkIDs(completeListener);
                                                loadWrongQIDs(completeListener);

                                            }

                                            @Override
                                            public void onFailure() {
                                                completeListener.onFailure();
                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure() {
                                        completeListener.onFailure();
                                    }
                                });

                            }


                            @Override
                            public void onFailure() {
                                completeListener.onFailure();
                            }
                        });

                    }

                    @Override
                    public void onFailure() {
                        completeListener.onFailure();
                    }
                });
            }

            @Override
            public void onFailure() {
                completeListener.onFailure();
            }
        });
    }
}

