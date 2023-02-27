package com.bignerdranch.android.lifeoptimizer

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*
import androidx.lifecycle.Observer

private const val DIALOG_DATE = "DialogDate"
private const val TAG = "TaskFragment"
private const val ARG_TASK_ID = "task_id"
private const val REQUEST_DATE = 0
private const val DATE_FORMAT = "EEE, MMM, dd"
private const val REQUEST_CONTACT = 1
class TaskFragment: Fragment(), DatePickerFragment.Callbacks {

    private lateinit var task: Task
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var sendButton: Button
    private lateinit var helperButton: Button


    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)

    }

    //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskId: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID
        Log.d(TAG, "args bundle crime ID: $taskId")
        taskDetailViewModel.loadTask(taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task,container,false)
        titleField = view.findViewById(R.id.task_title) as EditText
        dateButton = view.findViewById(R.id.task_date) as Button
        solvedCheckBox = view.findViewById(R.id.task_complete) as CheckBox
        sendButton = view.findViewById(R.id.task_report) as Button
        helperButton = view.findViewById(R.id.task_helper) as Button
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskDetailViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            Observer {task->
                task?.let {
                this.task = task
                updateUI()
            }
            }
        )
    }


    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int ){
                // This space intentionally left blank
            }
            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int ){
                task.title = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }
        titleField.addTextChangedListener(titleWatcher)
        solvedCheckBox.apply {
            setOnCheckedChangeListener {_, isChecked->task.isSolved =
            isChecked
            }
        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(task.date).apply {
                setTargetFragment(this@TaskFragment, REQUEST_DATE)
                show(this@TaskFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        sendButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getTaskReport())
                putExtra(
                    Intent.EXTRA_SUBJECT, getString(R.string.task_report_helper)
                )
            }.also {
                intent-> val chooserIntent =
                    Intent.createChooser(intent, getString(R.string.send_report))
                startActivity(chooserIntent)
            }
        }

        helperButton.apply {
            val pickContactIntent =
                Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)

            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }
            val packageManager: PackageManager = requireActivity().packageManager
            val resolvedActivity: ResolveInfo? =
                packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity == null) {
                isEnabled = false
            }

        }
    }

    override fun onStop() {
        super.onStop()
        taskDetailViewModel.saveTask(task)
    }

    override fun onDateSelected(date: Date) {
        task.date = date
        updateUI()
    }
    private fun updateUI() {
        titleField.setText(task.title)
        dateButton.text = task.date.toString()
        solvedCheckBox.apply {
            isChecked = task.isSolved
            jumpDrawablesToCurrentState()
        }
        if(task.helper.isNotEmpty()) {
            helperButton.text = task.helper
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            resultCode != Activity.RESULT_OK -> return
            requestCode == REQUEST_CONTACT && data != null -> {
                val contactUri: Uri? = data.data
                // Specify which fields you want your query to return values for
                val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                // Perform your query - the contactUri is like a "where" clause here
                val cursor = contactUri?.let {
                    requireActivity().contentResolver
                        .query(it, queryFields, null, null, null)
                }
                cursor?.use {
                    // Verify cursor contains at least one result
                    if (it.count == 0) {
                        return
                    }
                    // Pull out the first column of the first row of data -
                    // that is your suspect's name
                    it.moveToFirst()
                    val helper = it.getString(0)
                    task.helper = helper
                    taskDetailViewModel.saveTask(task)
                    helperButton.text = helper
                }
            }
        }
        }


    private fun getTaskReport(): String {
        val solvedTask = if (task.isSolved) {
            getString(R.string.task_report_solved)
        }
        else {
            getString(R.string.task_report_unsolved)
        }

        val dateString = DateFormat.format(DATE_FORMAT,task.date).toString()
        var helper = if (task.helper.isBlank()) {
            getString(R.string.task_report_no_helper)
        }
        else {
            getString(R.string.task_report_helper,task.helper)
        }

        return getString(R.string.task_report,
        task.title,dateString,solvedTask,helper)
    }

    companion object {
        fun newInstance(taskId: UUID): TaskFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID,taskId)
            }
            return TaskFragment().apply {
                arguments = args
            }
        }
    }
}