package project.riskyiman.doit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import project.riskyiman.doit.model.Task

class SharedViewModel : ViewModel() {
    private val _tasks = MutableLiveData<MutableList<Task>>(mutableListOf())
    val tasks: LiveData<MutableList<Task>> get() = _tasks

    fun addTask(task: Task) {
        _tasks.value?.add(task)
        _tasks.value = _tasks.value // Trigger observer
    }

    fun removeTask(task: Task) {
        _tasks.value?.remove(task)
        _tasks.value = _tasks.value // Trigger observer
    }
}
