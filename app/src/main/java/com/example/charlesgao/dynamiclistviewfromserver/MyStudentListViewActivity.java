package com.example.charlesgao.dynamiclistviewfromserver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import Adapter.StudentAdapter;
import Entity.Student;
import Service.ServiceRulesException;
import Service.ShowStudentsListService;


public class MyStudentListViewActivity extends Activity {

    SwipeRefreshLayout swipeRefreshLayout;

    private ListView listViewStudent;
    private List<Student> studentList;
    private StudentAdapter studentAdapter;
    private static ProgressDialog progressDialog;
    public final static String MSG_STUDENT_ERROR ="LOADING ERROR...";
    public final static String MSG_STUDENT_SUCCESS ="LOADING SUCCESS!";
    public final static String MSG_CONNECTION_ERROR ="CONNECTION ERROR...";
    public final static int FLAG_STUDENT_SUCCESS =1;
    ShowStudentsListService showStudentsListService = new ShowStudentsListService();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_refrashable_student_activity);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.id_layout_swipe_refresh);

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MyStudentListViewActivity.this);
        }
        progressDialog.setTitle("Please wait");
        progressDialog.setMessage("Refreshing..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        listViewStudent = (ListView) findViewById(R.id.id_swipe_refreshable_listView_student);

//        /**
//         * Data Source: from local
//         */
//        studentList = new ArrayList<>();
//        studentList.add(new Student(1, "Tom", 22));
//        studentList.add(new Student(2, "Alice", 21));
//        studentList.add(new Student(3, "Charlie", 23));
//
//        /**
//         * Setup and Initialize the Adapter : see the constructor of {@link StudentAdapter}
//         */
//        studentAdapter = new StudentAdapter(MyStudentListViewActivity.this, R.layout.each_student_item, studentList);
//
//        /**
//         * Connection
//         */
//        listViewStudent.setAdapter(studentAdapter);


        /**
         * Sub-Thread:
         */
        final Thread getStudentListThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /**
                     * Data Source: from Server
                     */
                    studentList = showStudentsListService.getStudents();
                    iHandler.sendEmptyMessage(FLAG_STUDENT_SUCCESS);


                } catch (ServiceRulesException e){
                    e.printStackTrace();
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putSerializable("errorMsg", e.getMessage() );
                    msg.setData(data);
                    iHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putSerializable("errorMsg", MSG_STUDENT_ERROR );
                    Log.d("TAG","!!!!!!!!!!!!!!!!!!!!!!");
                    msg.setData(data);
                    iHandler.sendMessage(msg);
                }
            }
        });
        getStudentListThread.start();

        // This is a swipeRefreshLayout that can update the info from the Server by swiping it.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // SwipeUpdate(); can add more behavior
//                getStudentListThread.run();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            /**
                             * Data Source: from Server
                             */
                            studentList = showStudentsListService.getStudents();
                            iHandler.sendEmptyMessage(FLAG_STUDENT_SUCCESS);

                        } catch (ServiceRulesException e){
                            e.printStackTrace();
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putSerializable("errorMsg", e.getMessage() );
                            msg.setData(data);
                            iHandler.sendMessage(msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putSerializable("errorMsg", MSG_STUDENT_ERROR );
                            Log.d("TAG","!!!!!!!!!!!!!!!!!!!!!!");
                            msg.setData(data);
                            iHandler.sendMessage(msg);
                        }
                    }
                }).start();
                Toast.makeText(MyStudentListViewActivity.this, "REFRESHING...", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

        private static class IHandler extends android.os.Handler {

            private final WeakReference<Activity> mActivity;

            public IHandler(MyStudentListViewActivity myStudentActivity){
                mActivity = new WeakReference<Activity>(myStudentActivity);
            }

            @Override
            public void handleMessage(Message msg) {
                // NOW, mActivity have the object on RegisterActivity!!!!!!!
                // Get a object from RegisterActivity

                int flag = msg.what;
                switch (flag){
                    case 0:
                        if (progressDialog != null) progressDialog.dismiss();
                        String errorMsg = msg.getData().getSerializable("errorMsg").toString();
                        ((MyStudentListViewActivity)(mActivity.get())).showTip(errorMsg);
                        break;

                    case FLAG_STUDENT_SUCCESS:
                        if (progressDialog != null) progressDialog.dismiss();
                        ((MyStudentListViewActivity)(mActivity.get())).showTip(MSG_STUDENT_SUCCESS);
                        ((MyStudentListViewActivity)(mActivity.get())).loadDataListView();

                        break;
                    default:
                        break;

                }

            }
        }

        private IHandler iHandler = new IHandler(this);

    public void showTip(String string){

        Toast.makeText(MyStudentListViewActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    public void loadDataListView(){

            /**
             * Setup and Initialize the Adapter : see the constructor of {@link StudentAdapter}
             */
            studentAdapter = new StudentAdapter(MyStudentListViewActivity.this, R.layout.each_student_item, studentList);

            /**
             * Connection
             */
            listViewStudent.setAdapter(studentAdapter);


    }


    // Here can add more behavior
    public void SwipeUpdate(){}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_list_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
