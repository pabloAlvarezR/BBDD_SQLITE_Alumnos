package www.iesmurgi.Proyecto4_Alumnos_SQLite_PabloAlvarezRelat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class UsuariosAdapter(private val lista: MutableList<Usuarios>,
                      private val onItemDelete:(Int)->Unit,
                      private val onItemUpdate:(Usuarios)->Unit):RecyclerView.Adapter<UsuariosViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.usuarios_esqueleto,parent,false)
        return UsuariosViewHolder(vista)
    }

    override fun onBindViewHolder(holder: UsuariosViewHolder, position: Int) {
        holder.inflar(lista[position], onItemDelete, onItemUpdate)
    }

    override fun getItemCount(): Int {
        return lista.size
    }

    // Agregar método onItemClick en esta sección
    fun onItemClick(position: Int) {
        val usuario = lista[position]
        val id = usuario.id
        val nombre = usuario.nombre
        val curso = usuario.curso
        val email = usuario.email
        lateinit var conexion: BaseDatosAlumnos
        // Mostrar el diálogo de actualización de datos

    }
}
