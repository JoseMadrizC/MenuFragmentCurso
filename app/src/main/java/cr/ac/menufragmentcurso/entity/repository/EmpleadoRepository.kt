package cr.ac.menufragmentcurso.entity.repository

import com.google.gson.Gson
import cr.ac.menufragmentcurso.entity.Empleado
import cr.ac.menufragmentcurso.service.EmpleadoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EmpleadoRepository {

    val empleadoService : EmpleadoService

    companion object {
        @JvmStatic
        val instance by lazy {
            EmpleadoRepository().apply {  }
        }
    }



    constructor(){
    val retrofit = Retrofit.Builder()
        .baseUrl("https://etiquicia.click/restAPI/api.php/records/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    empleadoService = retrofit.create(EmpleadoService::class.java)



    }

    fun save(empleado: Empleado){
    empleadoService.addEmpleado(empleado).execute()
    }

    fun delete(empleado: Empleado){
        empleado?.idEmpleado?.let { empleadoService.deleteID(it).execute() }
    }

    fun edit(empleado: Empleado){
        empleado.idEmpleado?.let { empleadoService.update(it, empleado).execute() }
    }

    fun datos(): List<Empleado>{
    return empleadoService.getEmpleado().execute().body()!!.records
    }
}