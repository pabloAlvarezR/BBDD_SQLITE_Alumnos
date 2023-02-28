package www.iesmurgi.Proyecto4_Alumnos_SQLite_PabloAlvarezRelat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import www.iesmurgi.Proyecto4_Alumnos_SQLite_PabloAlvarezRelat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var conexion: BaseDatosAlumnos
    lateinit var miAdapter: UsuariosAdapter
    var lista = mutableListOf<Usuarios>()

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    conexion = BaseDatosAlumnos(this)
    setRecycler()
    setListeners() //Cdo pulsemos el boton flotante
}

override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_opciones, menu)
    return super.onCreateOptionsMenu(menu)
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when(item.itemId){
        R.id.item_crear->{
            startActivity(Intent(this, AddUpdateActivity::class.java))
            true
        }
        R.id.item_borrar_todo->{ //Si tenemos implementado el adapter y la BD lo codificamos
           conexion.borrarTodo()
           lista.clear()
            miAdapter.notifyDataSetChanged()
            binding.tvNo.visibility = View.VISIBLE
            true
        }
        R.id.item_salir->{
            finish()
            true
        }
        else->true
    }
}

    private fun setListeners() {
        val fab_create = binding.create
        fab_create.setOnClickListener {
            startActivity(Intent(this, AddUpdateActivity::class.java))
        }

        val miAdapter = UsuariosAdapter(lista,
            { position -> conexion.borrar(position) },
            { usuario -> conexion.update(usuario) })

        val fab_update = binding.update
        fab_update.setOnClickListener {
            val position = 0 // la posición deseada
            val usuario = lista[position]
            usuario.id?.let { it1 -> showDialogUpdate(it1, usuario.nombre, usuario.curso, usuario.email) }
        }


        val fab_delete = binding.delete
        fab_delete.setOnClickListener {
            conexion.borrarTodo()
            lista.clear()
            miAdapter.notifyDataSetChanged()
            binding.tvNo.visibility = View.VISIBLE
        }
    }

    private fun showDialogUpdate(id: Int, nombre: String, curso: String, email: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_update, null)
        dialogBuilder.setView(dialogView)

        val nombresAlumnos = conexion.leerNombres()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nombresAlumnos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val spinnerAlumnos = dialogView.findViewById<Spinner>(R.id.spinner_alumnos)
        spinnerAlumnos.adapter = adapter

        val etNombre = dialogView.findViewById<EditText>(R.id.et_nombre)
        val etCurso = dialogView.findViewById<EditText>(R.id.et_curso)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_email)

        spinnerAlumnos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Aquí se puede obtener el nombre del usuario seleccionado
                val nombreUsuario = parent.getItemAtPosition(position) as String
                // Luego, se puede leer el usuario de la base de datos por su nombre y cargar sus datos en los EditText
                val usuario = conexion.leerUsuarioPorNombre(nombreUsuario)
                etNombre.setText(usuario!!.nombre)
                etCurso.setText(usuario.curso)
                etEmail.setText(usuario.email)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hace nada
            }
        }


        dialogBuilder.setTitle("Modificar usuario")
        dialogBuilder.setMessage("Introduce los nuevos datos del usuario")
        dialogBuilder.setPositiveButton("Guardar") { dialog, _ ->
            val usuario = Usuarios(id, etNombre.text.toString(), etCurso.text.toString(), etEmail.text.toString())
            if (conexion.existeEmail(usuario.email, usuario.id)) {
                Toast.makeText(this, "El correo electrónico ya está en uso", Toast.LENGTH_SHORT).show()
            } else {
                conexion.update(usuario)
                lista.clear()
                lista.addAll(conexion.leerTodos())
                miAdapter.notifyDataSetChanged()
            }
        }
        dialogBuilder.setNegativeButton("Cancelar") { dialog, _ ->
            // Deja en blanco, no hace nada
        }
        val b = dialogBuilder.create()
        b.show()
    }




    private fun setRecycler() {
    lista = conexion.leerTodos()
    binding.tvNo.visibility = View.INVISIBLE
    if (lista.size == 0) {
        binding.tvNo.visibility = View.VISIBLE
        return
    }
    val layoutManager = LinearLayoutManager(this)
    binding.recUsuarios.layoutManager = layoutManager
    miAdapter = UsuariosAdapter(lista, { onItemDelete(it) }) {
            usuario->onItemUpdate(usuario)
    }
    binding.recUsuarios.adapter = miAdapter
}



    private fun onItemUpdate(usuario: Usuarios) {
    //pasamos el usuario al activity updatecreate
    val i = Intent(this, AddUpdateActivity::class.java).apply {
        putExtra("USUARIO", usuario)
    }
    startActivity(i)
}

private fun onItemDelete(position: Int) {
    val usuario = lista[position]
    conexion.borrar(usuario.id)
    //borramos de la lista e indicamos al adapter que hemos
    //eliminado un registro
    lista.removeAt(position)
    if (lista.size == 0) {
        binding.tvNo.visibility = View.VISIBLE
    }
    miAdapter.notifyItemRemoved(position)
}

override fun onResume() {
    super.onResume()
    setRecycler()
}
}