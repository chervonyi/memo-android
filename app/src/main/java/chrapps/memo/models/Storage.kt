package chrapps.memo.models

import android.content.Context
import android.content.Context.MODE_PRIVATE
import java.io.*
import java.nio.file.Files.exists




class Storage(var cardMap: HashMap<Int, Card>) {

    constructor() : this(HashMap()) { }
}