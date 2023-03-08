package fr.rainbow.functions

import android.content.Context
import android.util.Log
import fr.rainbow.favorite
import java.io.BufferedWriter
import java.io.File


object file {

    fun BufferedWriter.writeLn(line: String) {
        this.write(line)
        this.newLine()
    }

    fun writeFile(context:Context ,favorites: ArrayList<favorite>){
        val path = context.filesDir
        val file = File(path,"somefile.txt")
        file.createNewFile()
        file.bufferedWriter().use { out ->
            favorites.forEach {
                out.writeLn("${it.name}, ${it.latitude}, ${it.longitude}")
            }
        }
    }

    fun readFile(context:Context){
        val path = context.filesDir
        val output = File(path,"somefile.txt").bufferedReader().use{it.readLines()}
        if(!output.isNullOrEmpty()){
            for(i in output){
                Log.d("fr.rainbow.favorite",i)
            }
            Log.d("fr.rainbow.favorite", context.filesDir.toString())
            Log.d("fr.rainbow.favorite", output.toString())
        }else{
            Log.d("fr.rainbow.favorite","nothing found")
        }

    }
}