package www.iesmurgi.Proyecto4_Alumnos_SQLite_PabloAlvarezRelat

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatosAlumnos(contexto: Context):SQLiteOpenHelper(contexto,DATABASE,null,VERSION){
    companion object{
        const val VERSION=1
        const val DATABASE="alumnos.sql"
        const val TABLA="alumnos_tb"
    }

    override fun onCreate(bd: SQLiteDatabase?) {
        val q = "CREATE TABLE $TABLA(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "curso TEXT NOT NULL, "+
                "email TEXT NOT NULL UNIQUE)"

        bd?.execSQL(q)
    }

    override fun onUpgrade(bd: SQLiteDatabase?, p1: Int, p2: Int) {
        val q="DROP TABLE IF EXISTS $TABLA"
        bd?.execSQL(q)
        onCreate(bd)
    }
    //CRUD create, read, update, delete
    //Crear un registro
    fun crear(alumno: Usuarios) :Long{
        val conexion=this.writableDatabase
        val valores = ContentValues().apply {
            put("NOMBRE", alumno.nombre)
            put ("EMAIL", alumno.email)
            put ("CURSO",alumno.curso)
        }
        val cod=conexion.insert(TABLA, null, valores)
        conexion.close()
        return cod
    }
    @SuppressLint("Range")
    fun leerTodos(): MutableList<Usuarios>{ //ver todos los registros
        val lista = mutableListOf<Usuarios>()
        val conexion = this.readableDatabase
        val consulta="SELECT * FROM $TABLA ORDER BY nombre"
        try{
            val cursor = conexion.rawQuery(consulta, null)
            if(cursor.moveToFirst()){
                do{
                    val alumno=Usuarios(
                        cursor.getInt(cursor.getColumnIndex("id")),
                        cursor.getString(cursor.getColumnIndex("nombre")),
                        cursor.getString(cursor.getColumnIndex("curso")),
                        cursor.getString(cursor.getColumnIndex("email"))
                    )
                    lista.add(alumno)
                }while(cursor.moveToNext())
            }
            cursor.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
        conexion.close()
        return lista
    }
    @SuppressLint("Range")
    fun leerNombres() : MutableList<String>{
        val lista = mutableListOf<String>()
        val conexion = this.readableDatabase
        val consulta="SELECT nombre FROM $TABLA ORDER BY nombre"
        try{
            val cursor = conexion.rawQuery(consulta, null)
            if(cursor.moveToFirst()){
                do{
                    val nombre = cursor.getString(cursor.getColumnIndex("nombre"))
                    lista.add(nombre)
                }while(cursor.moveToNext())
            }
            cursor.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
        conexion.close()
        return lista
    }

    @SuppressLint("Range")
    fun leerUsuarioPorNombre(nombre: String): Usuarios? {
        val conexion = this.readableDatabase
        val consulta = "SELECT * FROM $TABLA WHERE nombre = ?"
        val argumentos = arrayOf(nombre)
        var usuario: Usuarios? = null
        try {
            val cursor = conexion.rawQuery(consulta, argumentos)
            if (cursor.moveToFirst()) {
                usuario = Usuarios(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("nombre")),
                    cursor.getString(cursor.getColumnIndex("curso")),
                    cursor.getString(cursor.getColumnIndex("email"))
                )
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        conexion.close()
        return usuario
    }


    //metodo para comprobar que el email es unico
    fun existeEmail(email: String, id: Int?): Boolean {
        val consulta = if (id == null) "SELECT id from $TABLA where email='$email'" else
            "SELECT id from $TABLA where email='$email' AND id!=$id"
        val conexion = this.readableDatabase
        var filas = 0
        try {
            val cursor = conexion.rawQuery(consulta, null)
            filas = cursor.count
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        conexion.close()
        return (filas!=0)
    }
    fun borrar(id: Int?){
        val q="DELETE FROM $TABLA WHERE id=$id"
        val conexion= this.writableDatabase
        conexion.execSQL(q)
        conexion.close()
    }
    fun update(usuario: Usuarios): Int{
        //val q="UPDATE $TABLA SET nombre='${usuario.nombre}', email='${usuario.email}' where id=${usuario.id}"
        val conexion=this.writableDatabase
        val valores = ContentValues().apply {
            put("NOMBRE", usuario.nombre)
            put("CURSO",usuario.curso)
            put("EMAIL", usuario.email)
        }
        val update = conexion.update(TABLA, valores, "id=?", arrayOf(usuario.id.toString()))
        conexion.close()
        return  update
    }

    fun borrarTodo(){
        val q="DELETE FROM $TABLA"
        val conexion = this.writableDatabase
        conexion.execSQL(q)
        conexion.close()
    }

}
