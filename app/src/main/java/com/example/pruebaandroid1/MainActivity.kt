package com.example.pruebaandroid1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pruebaandroid1.ui.theme.PruebaAndroid1Theme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        db = FirebaseFirestore.getInstance()
        checkFirebaseConnection()
        cleanPendingTasks()
        setContent {
            PruebaAndroid1Theme {
                TaskManagerScreen(db, ::cleanPendingTasks)
            }
        }
    }

    private fun checkFirebaseConnection() {
        db.collection("test").get()
            .addOnSuccessListener {
                Log.d("FirebaseConnection", "Conectado a Firebase")
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseConnection", "Error al conectar a Firebase", e)
            }
    }

    private fun cleanPendingTasks() {
        db.collection("pendingTasks")
            .whereEqualTo("isDone", true)
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    db.collection("pendingTasks").document(document.id).delete()
                }
            }
            .addOnFailureListener { e ->
                Log.e("CleanPendingTasks", "Error al limpiar tareas pendientes", e)
            }
    }
}

@Composable
fun TaskManagerScreen(db: FirebaseFirestore, cleanPendingTasks: () -> Unit) {
    var taskDescription by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<Task>()) }
    var showDoneTasks by remember { mutableStateOf(false) }

    LaunchedEffect(showDoneTasks) {
        val collection = if (showDoneTasks) "doneTasks" else "pendingTasks"
        db.collection(collection).addSnapshotListener { snapshot, _ ->
            tasks = snapshot?.toObjects(Task::class.java) ?: listOf()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = taskDescription,
            onValueChange = { taskDescription = it },
            label = { Text("Descripción de la Tarea") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                val newTaskRef = db.collection("pendingTasks").document()
                val newTask = Task(id = newTaskRef.id, description = taskDescription)
                newTaskRef.set(newTask)
                taskDescription = ""
            },
            modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
        ) {
            Text("Añadir Tarea")
        }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Button(onClick = { showDoneTasks = false }, modifier = Modifier.weight(1f)) {
                Text("Tareas Pendientes")
            }
            Button(onClick = { showDoneTasks = true }, modifier = Modifier.weight(1f)) {
                Text("Tareas Hechas")
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
            items(tasks) { task ->
                TaskItem(task, db, cleanPendingTasks)
            }
        }
    }
}
@Composable
fun TaskItem(task: Task, db: FirebaseFirestore, cleanPendingTasks: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(task.description)
            if (expanded) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = {
                        db.collection("pendingTasks").document(task.id).delete()
                            .addOnSuccessListener {
                                db.collection("doneTasks").document(task.id).set(task.copy(isDone = true))
                                cleanPendingTasks()
                            }
                    }) {
                        Text("Marcar como Hecho")
                    }
                    TextButton(onClick = {
                        db.collection("doneTasks").document(task.id).delete()
                            .addOnSuccessListener {
                                db.collection("pendingTasks").document(task.id).set(task.copy(isDone = false))
                                cleanPendingTasks()
                            }
                    }) {
                        Text("Marcar como Pendiente")
                    }
                    TextButton(onClick = {
                        db.collection("pendingTasks").document(task.id).delete()
                        db.collection("doneTasks").document(task.id).delete()
                    }) {
                        Text("Borrar")
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TaskManagerScreenPreview() {
    PruebaAndroid1Theme {
        TaskManagerScreen(FirebaseFirestore.getInstance(), {})
    }
}