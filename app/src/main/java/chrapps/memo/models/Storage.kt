package chrapps.memo.models

class Storage(var cardMap: HashMap<Int, Card>) {

    constructor() : this(HashMap())
}