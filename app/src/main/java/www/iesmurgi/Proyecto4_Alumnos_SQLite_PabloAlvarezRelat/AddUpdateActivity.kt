package www.iesmurgi.Proyecto4_Alumnos_SQLite_PabloAlvarezRelat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import www.iesmurgi.Proyecto4_Alumnos_SQLite_PabloAlvarezRelat.databinding.ActivityAddUpdateBinding

class AddUpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddUpdateBinding
    var nombre=""
    var asignatura=""
    var email=""
    var id: Int? = null
    lateinit var conexion: BaseDatosAlumnos
    var editar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        conexion= BaseDatosAlumnos(this)
        cogerDatos()
        setListeners()
    }
    private fun cogerDatos() {
        val datos = intent.extras
        if(datos!=null){
            editar= true
            binding.btnCrear.text="EDITAR"
            val usuario = datos.getSerializable("USUARIO") as Usuarios
            id=usuario.id
            binding.etNombre.setText(usuario.nombre)
            binding.etAsignatura.setText(usuario.curso)//No esta en el esqueleto
            binding.etEmail.setText(usuario.email)
        }
    }
    private fun setListeners() {
        binding.btnVolver.setOnClickListener {
            finish()
        }
        binding.btnCrear.setOnClickListener {
            crearRegistro()
        }
    }
    private fun crearRegistro() {
        nombre=binding.etNombre.text.toString().trim()
        email=binding.etEmail.text.toString().trim()

        asignatura=binding.etAsignatura.text.toString().trim()

        if(nombre.length<3){
            binding.etNombre.setError("El campo nombre debe tener al menos 3 caracteres")
            return
        }
        if(email.length<6){
            binding.etEmail.setError("El campo email debe tener al menos 6 caracteres")
            binding.etEmail.requestFocus()
            return
        }
        //el email no esta duplicado

        if(asignatura.length<3){
            binding.etAsignatura.setError("El campo asignatura debe tener al menos 3 caracteres")
            return
        }


        if(conexion.existeEmail(email, id)){
            binding.etEmail.setError("El email YA está registrado.")
            binding.etEmail.requestFocus()
            return
        }
        if(!editar){
            val usuario=Usuarios(1, nombre,asignatura,email)
            if(conexion.crear(usuario)>-1){
                finish()
            }
            else{
                Toast.makeText(this, "NO se pudo guardar el registro!!!", Toast.LENGTH_SHORT).show()
            }
        }else{
            val usuario=Usuarios(id, nombre,asignatura, email)
            if(conexion.update(usuario)>-1){
                finish()
            }
            else{
                Toast.makeText(this, "NO se pudo editar el registro!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}